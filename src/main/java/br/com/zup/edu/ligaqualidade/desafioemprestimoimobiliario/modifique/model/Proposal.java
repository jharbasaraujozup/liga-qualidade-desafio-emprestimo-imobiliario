package br.com.zup.edu.ligaqualidade.desafioemprestimoimobiliario.modifique.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Proposal {

    private String id;
    private BigDecimal loanValue;
    private Integer numberInstallments;
    private EventActionEnum action;
    private List<Proponent> proponents;
    private List<Warranty> warranties;

    public Proposal(String id, BigDecimal loanValue, Integer numberInstallments,
        EventActionEnum action) {
        this.id = id;
        this.loanValue = loanValue;
        this.numberInstallments = numberInstallments;
        this.action = action;
        this.proponents = new ArrayList();
        this.warranties = new ArrayList();
    }

    public Proposal(String id,
        EventActionEnum action) {
        this.id = id;
        this.action = action;
    }

    public List<Proponent> getProponents() {
        return proponents;
    }

    public void setProponents(
        List<Proponent> proponents) {
        this.proponents = proponents;
    }

    public List<Warranty> getWarranties() {
        return warranties;
    }

    public void setWarranties(
        List<Warranty> warranties) {
        this.warranties = warranties;
    }

    public String getId() {
        return id;
    }

    public EventActionEnum getAction() {
        return action;
    }

    public void setAction(EventActionEnum action) {
        this.action = action;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getLoanValue() {
        return loanValue;
    }

    public void setLoanValue(BigDecimal loanValue) {
        this.loanValue = loanValue;
    }

    public Integer getNumberInstallments() {
        return numberInstallments;
    }

    public void setNumberInstallments(Integer numberInstallments) {
        this.numberInstallments = numberInstallments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proposal proposal = (Proposal) o;
        return Objects.equals(id, proposal.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
