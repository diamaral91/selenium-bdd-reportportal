package br.com.frontendproject.enums;

public enum BeneficiosEnum {
    
    CONTA_CORRENTE("Conta Corrente"),
    CARTAO_MULTIPLO("Cartão");

    private final String beneficio;

    BeneficiosEnum(final String beneficio) {
        this.beneficio = beneficio;
    }

    public String getValue() {
        return this.beneficio;
    }
}
