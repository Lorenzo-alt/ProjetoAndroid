package com.usjtlorenzo.projetoandroid.Modelos;

public class IconeItem {

    private String nomeIconeCor;
    private int iconeCor;

    public IconeItem(String nomeIconeCor, int iconeCor){
        this.nomeIconeCor = nomeIconeCor;
        this.iconeCor = iconeCor;
    }

    public IconeItem() {
    }

    public String getNomeIconeCor() {
        return nomeIconeCor;
    }

    public int getIconeCor() {
        return iconeCor;
    }

    public void setNomeIconeCor(String nomeIconeCor) {
        this.nomeIconeCor = nomeIconeCor;
    }

    public void setIconeCor(int iconeCor) {
        this.iconeCor = iconeCor;
    }
}
