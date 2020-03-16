package com.caobo.app.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.caobo.app.model.BaseModel;
import com.caobo.app.model.FileBean;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DBOpenHelper extends OrmLiteSqliteOpenHelper {

	public static final int DB_VERSION = 1;
	public DBOpenHelper(Context context) {
		super(context, "cdy.db", null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase,
                         ConnectionSource connectionSource) {
		try {
			TableUtils
					.createTableIfNotExists(connectionSource, BaseModel.class);
			TableUtils
					.createTableIfNotExists(connectionSource, FileBean.class);
			sqLiteDatabase.setVersion(DB_VERSION);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase,
                          ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			updateVersion(connectionSource, oldVersion);
			sqLiteDatabase.setVersion(newVersion);
		} catch (Exception e) {
			Log.e(e.getMessage(), e.getMessage());
		}
	}


	/**
	 * 更新数据库操作：先删除，在创建
	 * @param connectionSource
	 * @param oldVersion
	 */
	private void updateVersion(ConnectionSource connectionSource,int oldVersion) {
		if (oldVersion < DB_VERSION) {
			//删除表
			try {
				TableUtils
						.dropTable(connectionSource, BaseModel.class, true);
				TableUtils
						.dropTable(connectionSource, FileBean.class, true);
			} catch (Exception e) {
				Log.e(e.getMessage(),e.getMessage());
			}
			//创建表
			try {
				TableUtils
						.createTableIfNotExists(connectionSource, BaseModel.class);
				TableUtils
						.createTableIfNotExists(connectionSource, FileBean.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


	private boolean checkColumnExist(SQLiteDatabase db, String tableName,
                                     String columnName) {
		boolean result = false;
		Cursor cursor = null;
		try {
			// 查询一行
			cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 0",
					null);
			result = cursor != null && cursor.getColumnIndex(columnName) != -1;
		} catch (Exception e) {
		} finally {
			if (null != cursor && !cursor.isClosed()) {
				cursor.close();
			}
		}

		return result;
	}


	@Override
	public void close() {
		super.close();
	}
}
