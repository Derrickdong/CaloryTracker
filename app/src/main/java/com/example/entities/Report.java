package com.example.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

public class Report implements Serializable {
    private Integer id;
    private Date date;
    private Integer totalcaloriesconsumed;
    private Integer totalcaloriesburrend;
    private Integer totalsteps;
    private Integer setcaloriegoal;
    private Users userid;

    public Report(){}

    public Report(Integer id, Date date, Integer totalcaloriesconsumed, Integer totalcaloriesburrend, Integer totalsteps, Integer setcaloriegoal, Users userid) {
        this.id = id;
        this.date = date;
        this.totalcaloriesconsumed = totalcaloriesconsumed;
        this.totalcaloriesburrend = totalcaloriesburrend;
        this.totalsteps = totalsteps;
        this.setcaloriegoal = setcaloriegoal;
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

    public Integer getTotalcaloriesconsumed() {
        return totalcaloriesconsumed;
    }

    public void setTotalcaloriesconsumed(Integer totalcaloriesconsumed) {
        this.totalcaloriesconsumed = totalcaloriesconsumed;
    }

    public Integer getTotalcaloriesburrend() {
        return totalcaloriesburrend;
    }

    public void setTotalcaloriesburrend(Integer totalcaloriesburrend) {
        this.totalcaloriesburrend = totalcaloriesburrend;
    }

    public Integer getTotalsteps() {
        return totalsteps;
    }

    public void setTotalsteps(Integer totalsteps) {
        this.totalsteps = totalsteps;
    }

    public Integer getSetcaloriegoal() {
        return setcaloriegoal;
    }

    public void setSetcaloriegoal(Integer setcaloriegoal) {
        this.setcaloriegoal = setcaloriegoal;
    }

    public Users getUsername() {
        return userid;
    }

    public void setUsername(Users userid) {
        this.userid = userid;
    }

}
