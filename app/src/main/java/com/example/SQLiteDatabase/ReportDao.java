package com.example.SQLiteDatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.entities.Report;

import java.util.Date;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ReportDao {
    @Query("SELECT * FROM reportc")
    List<ReportC> getAll();

    @Query("SELECT * FROM reportc WHERE id = :id LIMIT 1")
    ReportC findById(int id);

    @Query("SELECT * FROM reportc WHERE date = :date LIMIT 1")
    List<ReportC> findByDate(Date date);

    @Insert
    void insertAll(ReportC...reports);

    @Insert
    long insert(ReportC report);

    @Delete
    void delete(ReportC report);

    @Update(onConflict = REPLACE)
    public void updateReports(ReportC...reports);

    @Query("DELETE FROM reportc")
    void deleteAll();
}
