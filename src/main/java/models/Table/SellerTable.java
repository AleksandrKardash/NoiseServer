package models.Table;

import java.io.Serializable;
import java.util.ArrayList;

public class SellerTable implements Serializable {

    private String seller;
    private String delivery;
    private double cost;
    private String adress;
    private ArrayList<MaterialTable> listProduct = new ArrayList<>();

    public SellerTable(String seller, String delivery, double cost, String adress) {
        this.seller = seller;
        this.delivery = delivery;
        this.cost = cost;
        this.adress = adress;
    }

    public SellerTable(String seller, String delivery, double cost, String adress, ArrayList<MaterialTable> listProduct) {
        this.seller = seller;
        this.delivery = delivery;
        this.cost = cost;
        this.adress = adress;
        this.listProduct = listProduct;
    }

    public SellerTable() {
    }

    public String getSeller() {
        return seller;
    }

    public String getDelivery() {
        return delivery;
    }

    public double getCost() {
        return cost;
    }

    public String getAdress() {
        return adress;
    }

    public ArrayList<MaterialTable> getListProduct() {
        return listProduct;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setListProduct(ArrayList<MaterialTable> listProduct) {
        this.listProduct = listProduct;
    }

}
