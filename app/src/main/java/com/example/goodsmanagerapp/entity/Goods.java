package com.example.goodsmanagerapp.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "goods")
public class Goods {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String series;
    private String qualityLevel;
    private String viscosityLevel;
    private String spec;
    private String unit;
    private String applicableModel;
    private double price; // 基础价格
    // 新增：三个客户价格
    private double priceCustomer1;
    private double priceCustomer2;
    private double priceCustomer3;
    private int stock;
    private String category;

    // 全参构造（包含三个客户价格）
    public Goods(String name, String series, String qualityLevel, String viscosityLevel,
                 String spec, String unit, String applicableModel, double price,
                 double priceCustomer1, double priceCustomer2, double priceCustomer3,
                 int stock, String category) {
        this.name = name;
        this.series = series;
        this.qualityLevel = qualityLevel;
        this.viscosityLevel = viscosityLevel;
        this.spec = spec;
        this.unit = unit;
        this.applicableModel = applicableModel;
        this.price = price;
        this.priceCustomer1 = priceCustomer1;
        this.priceCustomer2 = priceCustomer2;
        this.priceCustomer3 = priceCustomer3;
        this.stock = stock;
        this.category = category;
    }

    // Getter和Setter（新增三个客户价格的方法）
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSeries() { return series; }
    public void setSeries(String series) { this.series = series; }
    public String getQualityLevel() { return qualityLevel; }
    public void setQualityLevel(String qualityLevel) { this.qualityLevel = qualityLevel; }
    public String getViscosityLevel() { return viscosityLevel; }
    public void setViscosityLevel(String viscosityLevel) { this.viscosityLevel = viscosityLevel; }
    public String getSpec() { return spec; }
    public void setSpec(String spec) { this.spec = spec; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getApplicableModel() { return applicableModel; }
    public void setApplicableModel(String applicableModel) { this.applicableModel = applicableModel; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    // 客户价格的Getter/Setter
    public double getPriceCustomer1() { return priceCustomer1; }
    public void setPriceCustomer1(double priceCustomer1) { this.priceCustomer1 = priceCustomer1; }
    public double getPriceCustomer2() { return priceCustomer2; }
    public void setPriceCustomer2(double priceCustomer2) { this.priceCustomer2 = priceCustomer2; }
    public double getPriceCustomer3() { return priceCustomer3; }
    public void setPriceCustomer3(double priceCustomer3) { this.priceCustomer3 = priceCustomer3; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}