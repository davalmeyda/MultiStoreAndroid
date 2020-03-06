package com.panaceasoft.psmultistore.ui.product;

public enum SingleDavid {
    INSTANCE;

    float precio = 0;
    String cod_id = "";


    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float value) {
        this.precio = value;
    }

    public String getCodigo() {
        return cod_id;
    }

    public void setCodigo(String value) {
        this.cod_id = value;
    }
}
