package com.example.btl_nhom12.entity;


public class FoodCalories {
    private String foodName;
    private String servingSize;
    private Integer calories;
    private Long kj;

    public FoodCalories(){

    }

    public FoodCalories(String foodName, String servingSize, Integer calories, Long kj) {
        this.foodName = foodName;
        this.servingSize = servingSize;
        this.calories = calories;
        this.kj = kj;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Long getKj() {
        return kj;
    }

    public void setKj(Long kj) {
        this.kj = kj;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public String getServingSize() {
        return servingSize;
    }

    public void setServingSize(String servingSize) {
        this.servingSize = servingSize;
    }
}
