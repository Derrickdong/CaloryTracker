package com.example.SQLiteDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.entities.Report;

@Database(entities = {ReportC.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class ReportDatabase extends RoomDatabase {

    public abstract ReportDao reportDao();

    private static volatile ReportDatabase INSTANCE;
    static ReportDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (ReportDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ReportDatabase.class, "report_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
