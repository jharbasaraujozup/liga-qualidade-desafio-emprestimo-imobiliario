package br.com.zup.edu.ligaqualidade.desafioemprestimoimobiliario.modifique.model;

import java.util.Arrays;

public enum EventActionEnum {
    CREATED("created"),
    ADDED("added"),
    UPDATED("updated"),
    DELETED("deleted"),
    REMOVED("removed");

    public String action;

    EventActionEnum(String action) {
        this.action = action;
    }

    public static EventActionEnum getByValue(final String action){
        return Arrays.stream(values()).filter(value -> value.action.equals(action)).findFirst().orElse(null);
    }
}
