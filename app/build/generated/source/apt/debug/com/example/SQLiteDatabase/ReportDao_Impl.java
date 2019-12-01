package com.example.SQLiteDatabase;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.database.Cursor;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("unchecked")
public class ReportDao_Impl implements ReportDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfReportC;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfReportC;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfReportC;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public ReportDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfReportC = new EntityInsertionAdapter<ReportC>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `ReportC`(`id`,`date`,`total_cal_consumed`,`total_cal_burned`,`total_steps`,`cal_goal`,`user_id`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ReportC value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getId());
        }
        final Long _tmp;
        _tmp = Converters.dateToTimestamp(value.getDate());
        if (_tmp == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindLong(2, _tmp);
        }
        if (value.getTotalcaloriesconsumed() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindLong(3, value.getTotalcaloriesconsumed());
        }
        if (value.getTotalcaloriesburrend() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, value.getTotalcaloriesburrend());
        }
        if (value.getTotalsteps() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, value.getTotalsteps());
        }
        if (value.getSetcaloriegoal() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindLong(6, value.getSetcaloriegoal());
        }
        if (value.getUserid() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindLong(7, value.getUserid());
        }
      }
    };
    this.__deletionAdapterOfReportC = new EntityDeletionOrUpdateAdapter<ReportC>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `ReportC` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ReportC value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getId());
        }
      }
    };
    this.__updateAdapterOfReportC = new EntityDeletionOrUpdateAdapter<ReportC>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR REPLACE `ReportC` SET `id` = ?,`date` = ?,`total_cal_consumed` = ?,`total_cal_burned` = ?,`total_steps` = ?,`cal_goal` = ?,`user_id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ReportC value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getId());
        }
        final Long _tmp;
        _tmp = Converters.dateToTimestamp(value.getDate());
        if (_tmp == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindLong(2, _tmp);
        }
        if (value.getTotalcaloriesconsumed() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindLong(3, value.getTotalcaloriesconsumed());
        }
        if (value.getTotalcaloriesburrend() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, value.getTotalcaloriesburrend());
        }
        if (value.getTotalsteps() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, value.getTotalsteps());
        }
        if (value.getSetcaloriegoal() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindLong(6, value.getSetcaloriegoal());
        }
        if (value.getUserid() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindLong(7, value.getUserid());
        }
        if (value.getId() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindLong(8, value.getId());
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM reportc";
        return _query;
      }
    };
  }

  @Override
  public void insertAll(ReportC... reports) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfReportC.insert(reports);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public long insert(ReportC report) {
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfReportC.insertAndReturnId(report);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(ReportC report) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfReportC.handle(report);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateReports(ReportC... reports) {
    __db.beginTransaction();
    try {
      __updateAdapterOfReportC.handleMultiple(reports);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public List<ReportC> getAll() {
    final String _sql = "SELECT * FROM reportc";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfDate = _cursor.getColumnIndexOrThrow("date");
      final int _cursorIndexOfTotalcaloriesconsumed = _cursor.getColumnIndexOrThrow("total_cal_consumed");
      final int _cursorIndexOfTotalcaloriesburrend = _cursor.getColumnIndexOrThrow("total_cal_burned");
      final int _cursorIndexOfTotalsteps = _cursor.getColumnIndexOrThrow("total_steps");
      final int _cursorIndexOfSetcaloriegoal = _cursor.getColumnIndexOrThrow("cal_goal");
      final int _cursorIndexOfUserid = _cursor.getColumnIndexOrThrow("user_id");
      final List<ReportC> _result = new ArrayList<ReportC>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ReportC _item;
        _item = new ReportC();
        final Integer _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getInt(_cursorIndexOfId);
        }
        _item.setId(_tmpId);
        final Date _tmpDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = Converters.fromTimestamp(_tmp);
        _item.setDate(_tmpDate);
        final Integer _tmpTotalcaloriesconsumed;
        if (_cursor.isNull(_cursorIndexOfTotalcaloriesconsumed)) {
          _tmpTotalcaloriesconsumed = null;
        } else {
          _tmpTotalcaloriesconsumed = _cursor.getInt(_cursorIndexOfTotalcaloriesconsumed);
        }
        _item.setTotalcaloriesconsumed(_tmpTotalcaloriesconsumed);
        final Integer _tmpTotalcaloriesburrend;
        if (_cursor.isNull(_cursorIndexOfTotalcaloriesburrend)) {
          _tmpTotalcaloriesburrend = null;
        } else {
          _tmpTotalcaloriesburrend = _cursor.getInt(_cursorIndexOfTotalcaloriesburrend);
        }
        _item.setTotalcaloriesburrend(_tmpTotalcaloriesburrend);
        final Integer _tmpTotalsteps;
        if (_cursor.isNull(_cursorIndexOfTotalsteps)) {
          _tmpTotalsteps = null;
        } else {
          _tmpTotalsteps = _cursor.getInt(_cursorIndexOfTotalsteps);
        }
        _item.setTotalsteps(_tmpTotalsteps);
        final Integer _tmpSetcaloriegoal;
        if (_cursor.isNull(_cursorIndexOfSetcaloriegoal)) {
          _tmpSetcaloriegoal = null;
        } else {
          _tmpSetcaloriegoal = _cursor.getInt(_cursorIndexOfSetcaloriegoal);
        }
        _item.setSetcaloriegoal(_tmpSetcaloriegoal);
        final Integer _tmpUserid;
        if (_cursor.isNull(_cursorIndexOfUserid)) {
          _tmpUserid = null;
        } else {
          _tmpUserid = _cursor.getInt(_cursorIndexOfUserid);
        }
        _item.setUserid(_tmpUserid);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public ReportC findById(int id) {
    final String _sql = "SELECT * FROM reportc WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfDate = _cursor.getColumnIndexOrThrow("date");
      final int _cursorIndexOfTotalcaloriesconsumed = _cursor.getColumnIndexOrThrow("total_cal_consumed");
      final int _cursorIndexOfTotalcaloriesburrend = _cursor.getColumnIndexOrThrow("total_cal_burned");
      final int _cursorIndexOfTotalsteps = _cursor.getColumnIndexOrThrow("total_steps");
      final int _cursorIndexOfSetcaloriegoal = _cursor.getColumnIndexOrThrow("cal_goal");
      final int _cursorIndexOfUserid = _cursor.getColumnIndexOrThrow("user_id");
      final ReportC _result;
      if(_cursor.moveToFirst()) {
        _result = new ReportC();
        final Integer _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getInt(_cursorIndexOfId);
        }
        _result.setId(_tmpId);
        final Date _tmpDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = Converters.fromTimestamp(_tmp);
        _result.setDate(_tmpDate);
        final Integer _tmpTotalcaloriesconsumed;
        if (_cursor.isNull(_cursorIndexOfTotalcaloriesconsumed)) {
          _tmpTotalcaloriesconsumed = null;
        } else {
          _tmpTotalcaloriesconsumed = _cursor.getInt(_cursorIndexOfTotalcaloriesconsumed);
        }
        _result.setTotalcaloriesconsumed(_tmpTotalcaloriesconsumed);
        final Integer _tmpTotalcaloriesburrend;
        if (_cursor.isNull(_cursorIndexOfTotalcaloriesburrend)) {
          _tmpTotalcaloriesburrend = null;
        } else {
          _tmpTotalcaloriesburrend = _cursor.getInt(_cursorIndexOfTotalcaloriesburrend);
        }
        _result.setTotalcaloriesburrend(_tmpTotalcaloriesburrend);
        final Integer _tmpTotalsteps;
        if (_cursor.isNull(_cursorIndexOfTotalsteps)) {
          _tmpTotalsteps = null;
        } else {
          _tmpTotalsteps = _cursor.getInt(_cursorIndexOfTotalsteps);
        }
        _result.setTotalsteps(_tmpTotalsteps);
        final Integer _tmpSetcaloriegoal;
        if (_cursor.isNull(_cursorIndexOfSetcaloriegoal)) {
          _tmpSetcaloriegoal = null;
        } else {
          _tmpSetcaloriegoal = _cursor.getInt(_cursorIndexOfSetcaloriegoal);
        }
        _result.setSetcaloriegoal(_tmpSetcaloriegoal);
        final Integer _tmpUserid;
        if (_cursor.isNull(_cursorIndexOfUserid)) {
          _tmpUserid = null;
        } else {
          _tmpUserid = _cursor.getInt(_cursorIndexOfUserid);
        }
        _result.setUserid(_tmpUserid);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ReportC> findByDate(Date date) {
    final String _sql = "SELECT * FROM reportc WHERE date = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final Long _tmp;
    _tmp = Converters.dateToTimestamp(date);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfDate = _cursor.getColumnIndexOrThrow("date");
      final int _cursorIndexOfTotalcaloriesconsumed = _cursor.getColumnIndexOrThrow("total_cal_consumed");
      final int _cursorIndexOfTotalcaloriesburrend = _cursor.getColumnIndexOrThrow("total_cal_burned");
      final int _cursorIndexOfTotalsteps = _cursor.getColumnIndexOrThrow("total_steps");
      final int _cursorIndexOfSetcaloriegoal = _cursor.getColumnIndexOrThrow("cal_goal");
      final int _cursorIndexOfUserid = _cursor.getColumnIndexOrThrow("user_id");
      final List<ReportC> _result = new ArrayList<ReportC>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ReportC _item;
        _item = new ReportC();
        final Integer _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getInt(_cursorIndexOfId);
        }
        _item.setId(_tmpId);
        final Date _tmpDate;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = Converters.fromTimestamp(_tmp_1);
        _item.setDate(_tmpDate);
        final Integer _tmpTotalcaloriesconsumed;
        if (_cursor.isNull(_cursorIndexOfTotalcaloriesconsumed)) {
          _tmpTotalcaloriesconsumed = null;
        } else {
          _tmpTotalcaloriesconsumed = _cursor.getInt(_cursorIndexOfTotalcaloriesconsumed);
        }
        _item.setTotalcaloriesconsumed(_tmpTotalcaloriesconsumed);
        final Integer _tmpTotalcaloriesburrend;
        if (_cursor.isNull(_cursorIndexOfTotalcaloriesburrend)) {
          _tmpTotalcaloriesburrend = null;
        } else {
          _tmpTotalcaloriesburrend = _cursor.getInt(_cursorIndexOfTotalcaloriesburrend);
        }
        _item.setTotalcaloriesburrend(_tmpTotalcaloriesburrend);
        final Integer _tmpTotalsteps;
        if (_cursor.isNull(_cursorIndexOfTotalsteps)) {
          _tmpTotalsteps = null;
        } else {
          _tmpTotalsteps = _cursor.getInt(_cursorIndexOfTotalsteps);
        }
        _item.setTotalsteps(_tmpTotalsteps);
        final Integer _tmpSetcaloriegoal;
        if (_cursor.isNull(_cursorIndexOfSetcaloriegoal)) {
          _tmpSetcaloriegoal = null;
        } else {
          _tmpSetcaloriegoal = _cursor.getInt(_cursorIndexOfSetcaloriegoal);
        }
        _item.setSetcaloriegoal(_tmpSetcaloriegoal);
        final Integer _tmpUserid;
        if (_cursor.isNull(_cursorIndexOfUserid)) {
          _tmpUserid = null;
        } else {
          _tmpUserid = _cursor.getInt(_cursorIndexOfUserid);
        }
        _item.setUserid(_tmpUserid);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
