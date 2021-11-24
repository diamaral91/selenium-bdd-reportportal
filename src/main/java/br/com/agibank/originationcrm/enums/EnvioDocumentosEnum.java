package br.com.frontendproject.enums;

public enum EnvioDocumentosEnum {

    WHATSAPP("WhatsApp"),
    MENSAGEM_TEXTO("Mensagem de Texto (SMS)");

    private String envio;

    EnvioDocumentosEnum(final String envio) {
        this.envio = envio;
    }

    public String getValue() {
        return this.envio;
    }

}
