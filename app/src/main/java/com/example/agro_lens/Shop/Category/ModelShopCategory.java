package com.example.agro_lens.Shop.Category;

public class ModelShopCategory {

    String name,link,treename;

    public ModelShopCategory() {
    }

    public ModelShopCategory(String name, String link, String treename) {
        this.name = name;
        this.link = link;
        this.treename = treename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTreename() {
        return treename;
    }

    public void setTreename(String treename) {
        this.treename = treename;
    }
}
