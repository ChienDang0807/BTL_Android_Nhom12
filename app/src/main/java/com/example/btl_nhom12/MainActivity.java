package com.example.btl_nhom12;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import android.database.sqlite.*;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_nhom12.doituong.nguoidung;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        checkLoginState();
        setContentView(R.layout.activity_main);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        finishAffinity();
                    }
                });
        getWidget();
        databaseHelper = new SQLite(this);
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString("user_sdt", null);
        nguoidung updatedUser = databaseHelper.getUserBysdt(phoneNumber);
        if (updatedUser != null) {
            txtuser.setText(updatedUser.getTen());
        }

    }
    private void checkLoginState() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("is_logged_in", false);

        if (!isLoggedIn) {
            Intent intent = new Intent(MainActivity.this, dangnhap.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userPhone = sharedPreferences.getString("user_sdt", null);
        nguoidung updatedUser = databaseHelper.getUserBysdt(userPhone);
        if (updatedUser != null) {
            txtuser.setText(updatedUser.getTen());
        }
    }

    private void logout() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, dangky.class);
        startActivity(intent);
        finish();
    }
    private void getWidget() {
        cardthongtin = findViewById(R.id.thongtincanhan);
        cardthongtin.setOnClickListener(this);
        cardtheodoi = findViewById(R.id.theodoisk);
        cardtheodoi.setOnClickListener(this);
        carddexuat = findViewById(R.id.dexuat);
        carddexuat.setOnClickListener(this);
        cardtrogiup = findViewById(R.id.trogiup);
        cardtrogiup.setOnClickListener(this);
        btnlogout = findViewById(R.id.logout);
        btnlogout.setOnClickListener(this);
        txtuser = findViewById(R.id.user);

    }
    SQLiteDatabase database;
    CardView cardthongtin, cardtheodoi, carddexuat, cardtrogiup;
    ImageView btnlogout;
    TextView txtuser;
    private SQLite databaseHelper;
    @Override
    public void onClick(View view) {
        if(cardthongtin == view)
        {
            Intent intent = new Intent(this,thongtincanhanhome.class);
            startActivity(intent);
        }
        if(cardtheodoi == view)
        {
            Intent intent = new Intent(this,theodoisuckhoe.class);
            startActivity(intent);
        }
        if(carddexuat == view)
        {
            Intent intent = new Intent(this,dexuat.class);
            startActivity(intent);
        }
        if(cardtrogiup == view)
        {
            Intent intent = new Intent(this,trogiup.class);
            startActivity(intent);
        }
        if(view == btnlogout)
        {
            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // Xóa toàn bộ dữ liệu
            editor.apply();

            Intent intent = new Intent(MainActivity.this, dangnhap.class);
            startActivity(intent);
            finish(); // Đóng MainActivity
        }


    }
}