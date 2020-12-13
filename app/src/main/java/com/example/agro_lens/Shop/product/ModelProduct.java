package com.example.agro_lens.Shop.product;

public class ModelProduct {

    String productId,productName,productImage,discountAvailable,discountNote,discountPrice,originalPrice,productCategory,productDescription,treename,productQuantity;


    public ModelProduct() {
    }

    public ModelProduct(String productId, String productName, String productImage, String discountAvailable, String discountNote, String discountPrice, String originalPrice, String productCategory, String productDescription, String treename,String productQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.discountAvailable = discountAvailable;
        this.discountNote = discountNote;
        this.discountPrice = discountPrice;
        this.originalPrice = originalPrice;
        this.productCategory = productCategory;
        this.productDescription = productDescription;
        this.treename = treename;
        this.productQuantity=productQuantity;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getDiscountAvailable() {
        return discountAvailable;
    }

    public void setDiscountAvailable(String discountAvailable) {
        this.discountAvailable = discountAvailable;
    }

    public String getDiscountNote() {
        return discountNote;
    }

    public void setDiscountNote(String discountNote) {
        this.discountNote = discountNote;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getTreename() {
        return treename;
    }

    public void setTreename(String treename) {
        this.treename = treename;
    }
}
