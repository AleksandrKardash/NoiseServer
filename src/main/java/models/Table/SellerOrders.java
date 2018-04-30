package models.Table;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

public class SellerOrders implements Serializable {

    private int idOrder;
    private String adress;
    private String telNumber;
    private Double value;
    private Date dateOrder;
    private String processed;
    private ArrayList<MaterialTable> listProduct = new ArrayList<>();

    public SellerOrders(int idOrder, String adress, String telNumber, Double value, Date dateOrder, String processed, ArrayList<MaterialTable> listProduct) {
        this.idOrder = idOrder;
        this.adress = adress;
        this.telNumber = telNumber;
        this.value = value;
        this.dateOrder = dateOrder;
        this.processed = processed;
        this.listProduct = listProduct;
    }

    public SellerOrders(int idOrder, String adress, String telNumber, Double value, Date dateOrder, String processed) {
        this.idOrder = idOrder;
        this.adress = adress;
        this.telNumber = telNumber;
        this.value = value;
        this.dateOrder = dateOrder;
        this.processed = processed;
    }

    public SellerOrders() {
    }

    public ArrayList<MaterialTable> getListProduct() {
        return listProduct;
    }

    public String getProcessed() {
        return processed;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public String getAdress() {
        return adress;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public Double getValue() {
        return value;
    }

    public Date getDateOrder() {
        return dateOrder;
    }

    public void setListProduct(ArrayList<MaterialTable> listProduct) {
        this.listProduct = listProduct;
    }

    public void setProcessed(String processed) {
        this.processed = processed;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public void setDateOrder(Date dateOrder) {
        this.dateOrder = dateOrder;
    }
}
