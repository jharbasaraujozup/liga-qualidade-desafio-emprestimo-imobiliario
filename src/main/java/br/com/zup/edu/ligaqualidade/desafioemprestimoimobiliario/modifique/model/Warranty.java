package br.com.zup.edu.ligaqualidade.desafioemprestimoimobiliario.modifique.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Warranty {

    private String id;
    private BigDecimal value;
    private String province;
    private String proposalId;
    private EventActionEnum action;

    public Warranty(String id, String proposalId,
        EventActionEnum action) {
        this.id = id;
        this.proposalId = proposalId;
        this.action = action;
    }

    public Warranty(String id, BigDecimal value, String province, String proposalId,
        EventActionEnum action) {
        this.id = id;
        this.value = value;
        this.province = province;
        this.proposalId = proposalId;
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

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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
        Warranty warranty = (Warranty) o;
        return Objects.equals(id, warranty.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
