package br.com.zup.edu.ligaqualidade.desafioemprestimoimobiliario.modifique.model;

import java.util.Arrays;

public enum EventTypeEnum {
    PROPOSAL("proposal"),
    WARRANT("warrant"),
    PROPONENT("proponent");

    public String type;

    EventTypeEnum(String type) {
        this.type = type;
    }

    public static EventTypeEnum getByValue(final String type){
        return Arrays.stream(values()).filter(value -> value.type.equals(type)).findFirst().orElse(null);
    }
}
