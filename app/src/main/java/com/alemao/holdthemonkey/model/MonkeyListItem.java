package com.alemao.holdthemonkey.model;

import java.util.List;

public class MonkeyListItem {

    static private List<String> categoriesList;

    private String categorie;
    float custo;

    public MonkeyListItem(String categorie, float custo) {
        this.categorie = categorie;
        this.custo = custo;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public float getCusto() {
        return custo;
    }

    public void setCusto(float custo) {
        this.custo = custo;
    }
}
