package com.example.entities;

import java.io.Serializable;

public class Food implements Serializable {
    private Integer foodid;
    private String name;
    private String category;
    private Integer calorieamount;
    private String servingunit;
    private String servingamount;
    private Integer fat;

    public Food(){

    }

    public Food(Integer foodid, String name, String category, Integer calorieamount, String servingunit, String servingamount, Integer fat) {
        this.foodid = foodid;
        this.name = name;
        this.category = category;
        this.calorieamount = calorieamount;
        this.servingunit = servingunit;
        this.servingamount = servingamount;
        this.fat = fat;
    }

    public Integer getFoodid() {
        return foodid;
    }

    public void setFoodid(Integer foodid) {
        this.foodid = foodid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getCalorieamount() {
        return calorieamount;
    }

    public void setCalorieamount(Integer calorieamount) {
        this.calorieamount = calorieamount;
    }

    public String getServingunit() {
        return servingunit;
    }

    public void setServingunit(String servingunit) {
        this.servingunit = servingunit;
    }

    public String getServingamount() {
        return servingamount;
    }

    public void setServingamount(String servingamount) {
        this.servingamount = servingamount;
    }

    public Integer getFat() {
        return fat;
    }

    public void setFat(Integer fat) {
        this.fat = fat;
    }
}
