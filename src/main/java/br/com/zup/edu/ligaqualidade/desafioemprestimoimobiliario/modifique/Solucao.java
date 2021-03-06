package br.com.zup.edu.ligaqualidade.desafioemprestimoimobiliario.modifique;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.zup.edu.ligaqualidade.desafioemprestimoimobiliario.modifique.model.EventActionEnum;
import br.com.zup.edu.ligaqualidade.desafioemprestimoimobiliario.modifique.model.EventTypeEnum;
import br.com.zup.edu.ligaqualidade.desafioemprestimoimobiliario.modifique.model.Proponent;
import br.com.zup.edu.ligaqualidade.desafioemprestimoimobiliario.modifique.model.Proposal;
import br.com.zup.edu.ligaqualidade.desafioemprestimoimobiliario.modifique.model.Warranty;

public class Solucao {

    private static final BigDecimal MIN_LOAN_VALUE = new BigDecimal(30000.00);
    private static final BigDecimal MAX_LOAN_VALUE = new BigDecimal(3000000.00);
    private static final Integer MIN_NUMBER_INSTALLMENTS = 24;
    private static final Integer MAX_NUMBER_INSTALLMENTS = 180;

    public static String processMessages(List<String> messages) {

        String result = "";

        // decodifica as mensagens em propostas com seus respectivos proponentes e garantias
        List<Proposal> proposals = decodeMessages(messages);

        // aplica as regras de validação nas propostas, retornando apenas o ID das propostas válidas
        result = validateProposals(proposals);

        // retorna o id das propostas válidas
        return result;
    }

    private static List<Proposal> decodeMessages(List<String> messages) {

        List<Proposal> proposals = new ArrayList();
        List<Proponent> proponents = new ArrayList();
        List<Warranty> warranties = new ArrayList();

        messages.forEach(message -> {
            try {
                List<String> eventData = Arrays.asList(message.split("\\s*,\\s*"));

                switch (EventTypeEnum.getByValue(eventData.get(1))) {
                    case PROPOSAL:
                        proposals.add(buildProposal(eventData));
                        break;
                    case PROPONENT:
                        proponents.add(buildProponents(eventData));
                        break;
                    case WARRANTY:
                        warranties.add(buildWarranties(eventData));
                        break;
                }
            } catch (Exception e) {
                // do nothing - propostas invalidas serão simplesmente desconsideradas
            }
        });

        Map<String, Proposal> proposalResult = buildProposalList(proposals);
        handleWarranties(proposalResult, warranties);
        handleProponets(proposalResult, proponents);

        return new ArrayList(proposalResult.values());
    }

    private static void handleProponets(Map<String, Proposal> proposalResult, List<Proponent> proponents) {
        // proponente só executa os added, qualquer action diferente disso sera desconsiderada
        proponents.stream().filter(proponent -> proponent.getAction() == EventActionEnum.ADDED).forEach(proponent -> {
            proposalResult.get(proponent.getProposalId()).getProponents().add(proponent);
        });
    }

    private static void handleWarranties(Map<String, Proposal> proposalResult, List<Warranty> warranties) {
        // executa primeiro todos os added
        warranties.stream().filter(warranty -> warranty.getAction() == EventActionEnum.ADDED).forEach(warranty -> {
            proposalResult.get(warranty.getProposalId()).getWarranties().add(warranty);
        });

        // executa os updates
        warranties.stream().filter(warranty -> warranty.getAction() == EventActionEnum.UPDATED).forEach(warranty -> {
            int index = 0;
            for (Warranty warr : proposalResult.get(warranty.getProposalId()).getWarranties()) {
                if (warr.getId() == warranty.getId()) {
                    proposalResult.get(warranty.getProposalId()).getWarranties().set(index, warranty);
                }
                index++;
            }
        });

        // por ultimo executa todos os remove
        warranties.stream().filter(warranty -> warranty.getAction() == EventActionEnum.REMOVED).forEach(warranty -> {
            int index = 0;
            for (Warranty warr : proposalResult.get(warranty.getProposalId()).getWarranties()) {
                if (warr.getId() == warranty.getId()) {
                    proposalResult.get(warranty.getProposalId()).getWarranties().remove(index);
                }
                index++;
            }
        });
    }

    private static Map<String, Proposal> buildProposalList(List<Proposal> proposals) {
        LinkedHashMap<String, Proposal> proposalList = new LinkedHashMap();

        // executa os create
        proposals.stream().filter(proposal -> proposal.getAction() == EventActionEnum.CREATED).forEach(proposal -> {
            // usa o putIfAbsent pra não fazer o create do mesmo ID mais de uma vez
            proposalList.putIfAbsent(proposal.getId(), proposal);
        });

        // executa os update
        proposals.stream().filter(proposal -> proposal.getAction() == EventActionEnum.UPDATED).forEach(proposal -> {
            proposalList.put(proposal.getId(), proposal);
        });

        // executa os delete
        proposals.stream().filter(proposal -> proposal.getAction() == EventActionEnum.DELETED).forEach(proposal -> {
            proposalList.remove(proposal.getId());
        });

        return proposalList;
    }

    private static String validateProposals(List<Proposal> proposals) {
        List<String> validProposals = new ArrayList();

        proposals.forEach(proposal -> {
            Boolean isOk = Boolean.TRUE;

            // O valor do empréstimo deve estar entre R$ 30.000,00 e R$ 3.000.000,00
            if (proposal.getLoanValue().compareTo(MIN_LOAN_VALUE) < 0
                || proposal.getLoanValue().compareTo(MAX_LOAN_VALUE) > 0) {
                isOk = Boolean.FALSE;
            }

            // O empréstimo deve ser pago em no mínimo 2 anos e no máximo 15 anos
            if (proposal.getNumberInstallments() < MIN_NUMBER_INSTALLMENTS
                || proposal.getNumberInstallments() > MAX_NUMBER_INSTALLMENTS) {
                isOk = Boolean.FALSE;
            }

            // Deve haver no mínimo 2 proponentes por proposta
            if (proposal.getProponents().size() < 2) {
                isOk = Boolean.FALSE;
            }

            // Deve haver exatamente 1 proponente principal por proposta
            if (proposal.getProponents().stream().filter(proponent -> proponent.getMain()).collect(Collectors.toList()).size() != 1) {
                isOk = Boolean.FALSE;
            }

            // TODO implementar regras abaixo
//            Todos os proponentes devem ser maiores de 18 anos
//            Deve haver no mínimo 1 garantia de imóvel por proposta
//            A soma do valor das garantias deve ser maior ou igual ao dobro do valor do empréstimo
//            As garantias de imóvel dos estados PR, SC e RS não são aceitas
//            A renda do proponente principal deve ser pelo menos:
//            4 vezes o valor da parcela do empréstimo, se a idade dele for entre 18 e 24 anos
//            3 vezes o valor da parcela do empréstimo, se a idade dele for entre 24 e 50 anos
//            2 vezes o valor da parcela do empréstimo, se a idade dele for acima de 50 anos

            if (isOk) {
                validProposals.add(proposal.getId());
            }
        });
        return String.join(",", validProposals);
    }

    private static Warranty buildWarranties(List<String> eventData) {
        EventActionEnum action = EventActionEnum.getByValue(eventData.get(2));
        if (action == EventActionEnum.DELETED) {
            return new Warranty(
                eventData.get(5),
                eventData.get(4),
                action
            );
        } else {
            return new Warranty(
                eventData.get(5),
                new BigDecimal(eventData.get(6)),
                eventData.get(7),
                eventData.get(4),
                action
            );
        }
    }

    private static Proponent buildProponents(List<String> eventData) {
        return new Proponent(
            eventData.get(5),
            eventData.get(4),
            eventData.get(6),
            Integer.valueOf(eventData.get(7)),
            new BigDecimal(eventData.get(8)),
            Boolean.valueOf(eventData.get(9)),
            EventActionEnum.getByValue(eventData.get(2))
        );
    }

    private static Proposal buildProposal(List<String> eventData) {
        EventActionEnum action = EventActionEnum.getByValue(eventData.get(2));

        if (action == EventActionEnum.DELETED) {
            return new Proposal(
                eventData.get(4),
                action
            );
        } else {
            return new Proposal(
                eventData.get(4),
                new BigDecimal(eventData.get(5)),
                Integer.valueOf(eventData.get(6)),
                action
            );
        }
    }
}