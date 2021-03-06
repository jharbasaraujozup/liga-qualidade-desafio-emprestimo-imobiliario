package br.com.zup.edu.ligaqualidade.desafioemprestimoimobiliario.modifique;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.zup.edu.ligaqualidade.desafioemprestimoimobiliario.modifique.model.EventActionEnum;
import br.com.zup.edu.ligaqualidade.desafioemprestimoimobiliario.modifique.model.EventTypeEnum;
import br.com.zup.edu.ligaqualidade.desafioemprestimoimobiliario.modifique.model.Proponent;
import br.com.zup.edu.ligaqualidade.desafioemprestimoimobiliario.modifique.model.Proposal;
import br.com.zup.edu.ligaqualidade.desafioemprestimoimobiliario.modifique.model.Warranty;

public class Solucao {

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

                switch (EventTypeEnum.valueOf(eventData.get(1))) {
                    case PROPOSAL:
                        proposals.add(buildProposal(eventData));
                        break;
                    case PROPONENT:
                        proponents.add(buildProponents(eventData));
                        break;
                    case WARRANT:
                        warranties.add(buildWarranties(eventData));
                        break;
                }
            } catch (Exception e) {
                // do nothing
            }
        });

        // Monta a lista final, executando primeiro os created/added, depois os updated,
        // e na sequencia os deleted/removed
        List<Proposal> proposalResult = new ArrayList();

        //FIXME não deu tempo de percorrer as propostas montando a lista final


        return proposalResult;

    }

    /**
     * Aplica as setuintes regras
     *
     * O valor do empréstimo deve estar entre R$ 30.000,00 e R$ 3.000.000,00
     * O empréstimo deve ser pago em no mínimo 2 anos e no máximo 15 anos
     * Deve haver no mínimo 2 proponentes por proposta
     * Deve haver exatamente 1 proponente principal por proposta
     * Todos os proponentes devem ser maiores de 18 anos
     * Deve haver no mínimo 1 garantia de imóvel por proposta
     * A soma do valor das garantias deve ser maior ou igual ao dobro do valor do empréstimo
     * As garantias de imóvel dos estados PR, SC e RS não são aceitas
     * A renda do proponente principal deve ser pelo menos:
     * - 4 vezes o valor da parcela do empréstimo, se a idade dele for entre 18 e 24 anos
     * - 3 vezes o valor da parcela do empréstimo, se a idade dele for entre 24 e 50 anos
     * - 2 vezes o valor da parcela do empréstimo, se a idade dele for acima de 50 anos
     *
     * @param proposals
     * @return
     */
    private static String validateProposals(List<Proposal> proposals) {
        // TODO
        return "";
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