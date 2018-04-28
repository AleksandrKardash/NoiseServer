package models.Table;

import java.io.Serializable;

//класс для формирования таблицы общего расчета количества материалов
public class MaterialTable implements Serializable {

    private String nameMat;
    private double areaMat;
    private double depthMat;
    private double countMat;

    public MaterialTable(String nameMat, double areaMat, double depthMat, double countMat) {
        this.nameMat = nameMat;
        this.areaMat = areaMat;
        this.depthMat = depthMat;
        this.countMat = countMat;
    }

    public MaterialTable() {
    }

    public void setNameMat(String nameMat) {
        this.nameMat = nameMat;
    }

    public void setAreaMat(double areaMat) {
        this.areaMat = areaMat;
    }

    public void setDepthMat(double depthMat) {
        this.depthMat = depthMat;
    }

    public void setCountMat(double countMat) {
        this.countMat = countMat;
    }

    public String getNameMat() {
        return nameMat;
    }

    public double getAreaMat() {
        return areaMat;
    }

    public double getDepthMat() {
        return depthMat;
    }

    public double getCountMat() {
        return countMat;
    }
}
