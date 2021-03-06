package br.com.zup.edu.ligaqualidade.desafioemprestimoimobiliario.modifique.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Proponent {

    private String id;
    private String proposalId;
    private String name;
    private Integer age;
    private BigDecimal monthlyIncome;
    private Boolean isMain;
    private EventActionEnum action;

    public Proponent(String id, String proposalId, String name, Integer age, BigDecimal monthlyIncome,
        Boolean isMain, EventActionEnum action) {
        this.id = id;
        this.proposalId = proposalId;
        this.name = name;
        this.age = age;
        this.monthlyIncome = monthlyIncome;
        this.isMain = isMain;
        this.action = action;
    }

    public String getProposalId() {
        return proposalId;
    }

    public void setProposalId(String proposalId) {
        this.proposalId = proposalId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public Boolean getMain() {
        return isMain;
    }

    public void setMain(Boolean main) {
        isMain = main;
    }

    public EventActionEnum getAction() {
        return action;
    }

    public void setAction(EventActionEnum action) {
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proponent proponent = (Proponent) o;
        return Objects.equals(id, proponent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
