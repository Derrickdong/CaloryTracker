package com.example.SQLiteDatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity
public class ReportC implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @ColumnInfo(name = "date")
    private Date date;
    @ColumnInfo(name = "total_cal_consumed")
    private Integer totalcaloriesconsumed;
    @ColumnInfo(name = "total_cal_burned")
    private Integer totalcaloriesburrend;
    @ColumnInfo(name = "total_steps")
    private Integer totalsteps;
    @ColumnInfo(name = "cal_goal")
    private Integer setcaloriegoal;
    @ColumnInfo(name = "user_id")
    private Integer userid;

    public ReportC(){}

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

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer uid) {
        this.userid = uid;
    }
}
