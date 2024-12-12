package com.example.btl_nhom12;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.database.sqlite.*;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_nhom12.config.SQLite;
import com.example.btl_nhom12.entity.nguoidung;
import com.example.btl_nhom12.repository.UserRepository;
import com.example.btl_nhom12.service.dangky;
import com.example.btl_nhom12.service.dangnhap;
import com.example.btl_nhom12.service.dexuat;
import com.example.btl_nhom12.service.theodoisuckhoe;
import com.example.btl_nhom12.service.thongtincanhanhome;
import com.example.btl_nhom12.service.trogiup;

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

        // Gọi getWritableDatabase để kích hoạt onCreate() hoặc onUpgrade()
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        userRepository = new UserRepository(databaseHelper);
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString("user_sdt", null);
        nguoidung updatedUser = userRepository.getUserBysdt(phoneNumber);
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
        nguoidung updatedUser = userRepository.getUserBysdt(userPhone);
        if (updatedUser != null) {
            txtuser.setText("Xin chào, " + updatedUser.getTen().toUpperCase());
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
    private UserRepository userRepository;
    @Override
    public void onClick(View view) {
        if(cardthongtin == view)
        {
            Intent intent = new Intent(this, thongtincanhanhome.class);
            startActivity(intent);
        }
        if(cardtheodoi == view)
        {
            Intent intent = new Intent(this, theodoisuckhoe.class);
            startActivity(intent);
        }
        if(carddexuat == view)
        {
            Intent intent = new Intent(this, dexuat.class);
            startActivity(intent);
        }
        if(cardtrogiup == view)
        {
            Intent intent = new Intent(this, trogiup.class);
            startActivity(intent);
        }
        if(view == btnlogout)
        {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        // Thực hiện đăng xuất nếu người dùng chọn "Có"
                        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear(); // Xóa toàn bộ dữ liệu
                        editor.apply();

                        Intent intent = new Intent(MainActivity.this, dangnhap.class);
                        startActivity(intent);
                        finish(); // Đóng Activity hiện tại
                    })
                    .setNegativeButton("Không", (dialog, which) -> {
                        // Đóng hộp thoại nếu người dùng chọn "Không"
                        dialog.dismiss();
                    })
                    .show();
        }


    }
}