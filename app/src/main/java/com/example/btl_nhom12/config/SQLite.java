package com.example.btl_nhom12.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLite extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TTCN.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_NAME = "tblnd";
    private static final String KEY_MA_USER = "mangdung";
    private static final String KEY_NAME = "hoten";
    private static final String KEY_DOB = "dob";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_SDT = "dienthoai";
    private static final String KEY_MK = "matkhau";

    private static final String TABLE_NAME_HEALTH = "tblsk";
    private static final String KEY_ID = "id";
    private static final String KEY_chieucao = "chieucao";
    private static final String KEY_cannang = "cannang";
    private static final String KEY_bmi = "bmi";
    private static final String KEY_sdt = "sdt";
    private static final String KEY_ngaythang = "ngaythangdo";

    public SQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("SQLite", "onCreate called");
        String create_table = String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TABLE_NAME, KEY_MA_USER, KEY_NAME, KEY_EMAIL, KEY_DOB, KEY_SDT, KEY_MK);
        sqLiteDatabase.execSQL(create_table);

        String createHealthTable = String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT NOT NULL, " +       // Chiều cao
                        "%s TEXT NOT NULL, " +       // Cân nặng
                        "%s TEXT NOT NULL, " +       // BMI
                        "%s TEXT NOT NULL, " +       // SĐT
                        "%s TEXT NOT NULL)",         // Ngày tháng đo
                TABLE_NAME_HEALTH, KEY_ID, KEY_chieucao, KEY_cannang, KEY_bmi, KEY_sdt, KEY_ngaythang
        );
        sqLiteDatabase.execSQL(createHealthTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d("SQLite", "onUpgrade called: Old Version = " + oldVersion + ", New Version = " + newVersion);

        if (oldVersion < 2) {
            // Tạo bảng mới
            String createHealthTable = String.format(
                    "CREATE TABLE IF NOT EXISTS %s (" +
                            "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "%s TEXT NOT NULL, " +
                            "%s TEXT NOT NULL, " +
                            "%s TEXT NOT NULL, " +
                            "%s TEXT NOT NULL, " +
                            "%s TEXT NOT NULL)",
                    TABLE_NAME_HEALTH, KEY_ID, KEY_chieucao, KEY_cannang, KEY_bmi, KEY_sdt, KEY_ngaythang
            );
            sqLiteDatabase.execSQL(createHealthTable);
            Log.d("SQLite", "Health table created during upgrade");
        }
    }


}
