package com.example.SQLiteDatabase;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public class ReportDatabase_Impl extends ReportDatabase {
  private volatile ReportDao _reportDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `ReportC` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `date` INTEGER, `total_cal_consumed` INTEGER, `total_cal_burned` INTEGER, `total_steps` INTEGER, `cal_goal` INTEGER, `user_id` INTEGER)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"9c680d6d87f83c8184545430e0e1d913\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `ReportC`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsReportC = new HashMap<String, TableInfo.Column>(7);
        _columnsReportC.put("id", new TableInfo.Column("id", "INTEGER", false, 1));
        _columnsReportC.put("date", new TableInfo.Column("date", "INTEGER", false, 0));
        _columnsReportC.put("total_cal_consumed", new TableInfo.Column("total_cal_consumed", "INTEGER", false, 0));
        _columnsReportC.put("total_cal_burned", new TableInfo.Column("total_cal_burned", "INTEGER", false, 0));
        _columnsReportC.put("total_steps", new TableInfo.Column("total_steps", "INTEGER", false, 0));
        _columnsReportC.put("cal_goal", new TableInfo.Column("cal_goal", "INTEGER", false, 0));
        _columnsReportC.put("user_id", new TableInfo.Column("user_id", "INTEGER", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReportC = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesReportC = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoReportC = new TableInfo("ReportC", _columnsReportC, _foreignKeysReportC, _indicesReportC);
        final TableInfo _existingReportC = TableInfo.read(_db, "ReportC");
        if (! _infoReportC.equals(_existingReportC)) {
          throw new IllegalStateException("Migration didn't properly handle ReportC(com.example.SQLiteDatabase.ReportC).\n"
                  + " Expected:\n" + _infoReportC + "\n"
                  + " Found:\n" + _existingReportC);
        }
      }
    }, "9c680d6d87f83c8184545430e0e1d913", "9b9bf549e3e213c9ecb7e2b971c95fdc");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "ReportC");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `ReportC`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public ReportDao reportDao() {
    if (_reportDao != null) {
      return _reportDao;
    } else {
      synchronized(this) {
        if(_reportDao == null) {
          _reportDao = new ReportDao_Impl(this);
        }
        return _reportDao;
      }
    }
  }
}
