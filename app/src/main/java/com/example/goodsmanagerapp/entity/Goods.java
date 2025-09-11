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
    private double price;
    private int stock;
    private String category; // 新增：货物分类

    // 全参构造（含category）
    public Goods(String name, String series, String qualityLevel, String viscosityLevel,
                 String spec, String unit, String applicableModel, double price, int stock, String category) {
        this.name = name;
        this.series = series;
        this.qualityLevel = qualityLevel;
        this.viscosityLevel = viscosityLevel;
        this.spec = spec;
        this.unit = unit;
        this.applicableModel = applicableModel;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    // Getter + Setter（新增category的Get/Set）
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
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}