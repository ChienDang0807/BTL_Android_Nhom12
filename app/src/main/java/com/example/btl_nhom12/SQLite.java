package com.example.btl_nhom12;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.btl_nhom12.doituong.nguoidung;

public class SQLite extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TTCN.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "tblnd";
    private static final String KEY_MA_USER = "mangdung";
    private static final String KEY_NAME = "hoten";
    private static final String KEY_DOB = "dob";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_SDT = "dienthoai";
    private static final String KEY_MK = "matkhau";

    public SQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_table = String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                TABLE_NAME, KEY_MA_USER, KEY_NAME, KEY_EMAIL, KEY_DOB, KEY_SDT, KEY_MK);
        sqLiteDatabase.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
        onCreate(sqLiteDatabase);
    }

    public void addUser(String name, String email, String dob, String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_DOB, dob);
        values.put(KEY_SDT, phone);
        values.put(KEY_MK, password);

        long result = db.insert(TABLE_NAME, null, values);
        if (result == -1) {
            Log.e("SQLite", "Failed to insert user");
        } else {
            Log.d("SQLite", "User inserted successfully");
        }
        db.close();
    }
    public boolean checkUser(String dienthoai, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM tblnd WHERE dienthoai = ? AND matkhau = ?";
        Cursor cursor = db.rawQuery(query, new String[]{dienthoai, password});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }
    public nguoidung getUserBysdt(String sdt) {
        SQLiteDatabase db = this.getReadableDatabase();
        nguoidung user = null;

        Cursor cursor = db.query(
                TABLE_NAME, // Tên bảng
                null,       // Các cột cần lấy (null = lấy tất cả)
                KEY_SDT + " = ?", // Điều kiện WHERE
                new String[] { sdt }, // Giá trị của điều kiện
                null,       // GROUP BY
                null,       // HAVING
                null        // ORDER BY
        );

        if (cursor != null && cursor.moveToFirst()) {
            user = new nguoidung(
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_DOB)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_SDT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_MK))
            );
            cursor.close();
        }

        db.close();
        return user;
    }

    public void updateUser(nguoidung user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getTen());  // Cập nhật tên
        values.put(KEY_DOB, user.getNgaysinh());  // Cập nhật ngày sinh
        values.put(KEY_EMAIL, user.getEmail());  // Cập nhật email
        values.put(KEY_MK, user.getMatkhau());  // Cập nhật mật khẩu

        // Cập nhật thông tin của người dùng dựa trên số điện thoại
        int rowsAffected = db.update(TABLE_NAME, values, KEY_SDT + " = ?", new String[]{user.getSdt()});
        db.close();

        if (rowsAffected == 0) {
            Log.d("Update Error", "Không tìm thấy người dùng với số điện thoại: " + user.getSdt());
        } else {
            Log.d("Update Success", "Cập nhật thành công.");
        }
    }
}
