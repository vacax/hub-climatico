package edu.pucmm.pw.utils;

public enum DatosEstaticos {

    USUARIO("USUARIO"),
    HEADER_SEGURIDAD_TOKEN("SEGURIDAD-TOKEN"),
    URL_MONGO("URL_MONGO"),
    DB_NOMBRE("DB_NOMBRE"),
    CRUD_DNS_JAVALIN_DOMINIO("CRUD_DNS_JAVALIN_DOMINIO");


    private String valor;

    DatosEstaticos(String valor){
        this.valor =  valor;
    }

    public String getValor() {
        return valor;
    }
}
