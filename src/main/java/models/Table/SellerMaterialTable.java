package models.Table;

import java.io.Serializable;

//класс для формирования таблицы "Мои материалы" для поставщика
public class SellerMaterialTable implements Serializable {

    private int idproduct;
    private String manufacturedProduct;
    private String typeProduct;
    private String nameProduct;
    private String secondNameProduct;
    private Double areaProduct;
    private Double depthProduct;
    private String classProduct;
    private Double costProduct;


    public SellerMaterialTable(String manufacturedProduct, String typeProduct, String nameProduct, String secondNameProduct, Double areaProduct, Double depthProduct, String classProduct, Double costProduct) {
        this.manufacturedProduct = manufacturedProduct;
        this.typeProduct = typeProduct;
        this.nameProduct = nameProduct;
        this.secondNameProduct = secondNameProduct;
        this.areaProduct = areaProduct;
        this.depthProduct = depthProduct;
        this.classProduct = classProduct;
        this.costProduct = costProduct;
    }

    public SellerMaterialTable(int idproduct, String manufacturedProduct, String typeProduct, String nameProduct, String secondNameProduct, Double areaProduct, Double depthProduct, String classProduct, Double costProduct) {
        this.idproduct = idproduct;
        this.manufacturedProduct = manufacturedProduct;
        this.typeProduct = typeProduct;
        this.nameProduct = nameProduct;
        this.secondNameProduct = secondNameProduct;
        this.areaProduct = areaProduct;
        this.depthProduct = depthProduct;
        this.classProduct = classProduct;
        this.costProduct = costProduct;
    }

    public SellerMaterialTable() {
    }

    public void setIdproduct(int idproduct) {
        this.idproduct = idproduct;
    }

    public void setManufacturedProduct(String manufacturedProduct) {
        this.manufacturedProduct = manufacturedProduct;
    }

    public void setTypeProduct(String typeProduct) {
        this.typeProduct = typeProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public void setSecondNameProduct(String secondNameProduct) {
        this.secondNameProduct = secondNameProduct;
    }

    public void setAreaProduct(Double areaProduct) {
        this.areaProduct = areaProduct;
    }

    public void setDepthProduct(Double depthProduct) {
        this.depthProduct = depthProduct;
    }

    public void setClassProduct(String classProduct) {
        this.classProduct = classProduct;
    }

    public void setCostProduct(Double costProduct) {
        this.costProduct = costProduct;
    }

    public int getIdproduct() {
        return idproduct;
    }

    public String getManufacturedProduct() {
        return manufacturedProduct;
    }

    public String getTypeProduct() {
        return typeProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public String getSecondNameProduct() {
        return secondNameProduct;
    }

    public Double getAreaProduct() {
        return areaProduct;
    }

    public Double getDepthProduct() {
        return depthProduct;
    }

    public String getClassProduct() {
        return classProduct;
    }

    public Double getCostProduct() {
        return costProduct;
    }
}
