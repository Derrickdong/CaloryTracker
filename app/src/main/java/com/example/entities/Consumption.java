package com.example.entities;

import java.io.Serializable;
import java.util.Date;

public class Consumption implements Serializable {
    private Integer id;
    private Date date;
    private Integer quantity;
    private Food food;
    private Users userid;

    public Consumption( Date date, Integer quantity, Food food, Users userid) {
        this.id = 1;
        this.date = date;
        this.quantity = quantity;
        this.food = food;
        this.userid = userid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public Users getUserid() {
        return userid;
    }

    public void setUserid(Users userid) {
        this.userid = userid;
    }
}
