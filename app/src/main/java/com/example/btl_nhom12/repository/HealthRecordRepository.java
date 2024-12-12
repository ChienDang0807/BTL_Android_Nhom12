package com.example.btl_nhom12.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.btl_nhom12.config.SQLite;
import com.example.btl_nhom12.entity.chisosuckhoe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class HealthRecordRepository {

    private final SQLite dbHelper;

    private static final String TABLE_NAME_HEALTH = "tblsk";
    private static final String KEY_ID = "id";
    private static final String KEY_chieucao = "chieucao";
    private static final String KEY_cannang = "cannang";
    private static final String KEY_bmi = "bmi";
    private static final String KEY_sdt = "sdt";
    private static final String KEY_ngaythang = "ngaythangdo";

    public HealthRecordRepository(SQLite dbHelper){
        this.dbHelper = dbHelper;
    }

    public void addHealthRecord(String sdt, String chieucao, String cannang, String bmi, String ngaythang) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_chieucao, chieucao);
        values.put(KEY_cannang, cannang);
        values.put(KEY_bmi, bmi);
        values.put(KEY_sdt, sdt);
        values.put(KEY_ngaythang, ngaythang);

        long result = db.insert(TABLE_NAME_HEALTH, null, values);
        if (result == -1) {
            Log.e("SQLite", "Failed to insert health record");
        } else {
            Log.d("SQLite", "Health record inserted successfully");
        }
        db.close();
    }

    // Lấy danh sách bản ghi theo số điện thoại
    public ArrayList<chisosuckhoe> getHealthRecordsByPhone(String phone) {
        ArrayList<chisosuckhoe> healthList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME_HEALTH + " WHERE " + KEY_sdt + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{phone});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                chisosuckhoe healthRecord = new chisosuckhoe(
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_sdt)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_ngaythang)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_cannang)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_chieucao)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_bmi))
                );
                healthList.add(healthRecord);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        Collections.sort(healthList, new Comparator<chisosuckhoe>() {
            @Override
            public int compare(chisosuckhoe record1, chisosuckhoe record2) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date date1 = dateFormat.parse(record1.getDate());
                    Date date2 = dateFormat.parse(record2.getDate());
                    return date2.compareTo(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        return healthList;
    }

    public void deleteHealthRecordByAttributes(String chieucao, String cannang, String ngaythang) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            int rows = db.delete(TABLE_NAME_HEALTH,
                    KEY_chieucao + " = ? AND " +
                            KEY_cannang + " = ? AND " +
                            KEY_ngaythang + " = ?",
                    new String[]{chieucao, cannang, ngaythang});

            if (rows > 0) {
                Log.d("SQLite", "Xóa thành công bản ghi.");
            } else {
                Log.d("SQLite", "Không tìm thấy bản ghi để xóa.");
            }
        } catch (Exception e) {
            Log.e("SQLite", "Lỗi khi xóa bản ghi", e);
        } finally {
            db.close();
        }
    }
    public void updateHealthRecordByAttributes(String chieucao, String cannang, String bmi, String sdt, String ngaythang,
                                               String newChieucao, String newCannang, String newBmi, String newSdt, String newNgaythang) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Xóa bản ghi cũ có các thuộc tính trùng khớp
        String whereClause = KEY_chieucao + " = ? AND " +
                KEY_cannang + " = ? AND " +
                KEY_bmi + " = ? AND " +
                KEY_sdt + " = ? AND " +
                KEY_ngaythang + " = ?";

        String[] whereArgs = new String[]{chieucao, cannang, bmi, sdt, ngaythang};

        // Thực hiện xóa bản ghi cũ
        db.delete(TABLE_NAME_HEALTH, whereClause, whereArgs);

        // Thêm bản ghi mới với các giá trị mới
        values.put(KEY_chieucao, newChieucao);
        values.put(KEY_cannang, newCannang);
        values.put(KEY_bmi, newBmi);
        values.put(KEY_sdt, newSdt);
        values.put(KEY_ngaythang, newNgaythang);

        // Thực hiện thêm bản ghi mới
        db.insert(TABLE_NAME_HEALTH, null, values);

        db.close();
    }
}
