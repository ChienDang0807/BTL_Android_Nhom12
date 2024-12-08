package com.example.btl_nhom12;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class dangnhap extends AppCompatActivity implements View.OnClickListener{
     EditText etdt, etPassword;
     Button btnLogin, btnRegister;
    private SQLite databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);

        databaseHelper = new SQLite(this);

        etdt = findViewById(R.id.phone);
        etPassword = findViewById(R.id.pass);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);

        btnLogin.setOnClickListener(v -> {
            String sdt = etdt.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(sdt) || TextUtils.isEmpty(password)) {
                Toast.makeText(dangnhap.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else if (databaseHelper.checkUser(sdt, password)) {
                saveLoginState(sdt);
                Toast.makeText(dangnhap.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_sdt", sdt); // Lưu số điện thoại
                editor.apply();


                // Chuyển sang Activity khác
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(dangnhap.this, "Thông tin không chính xác!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveLoginState(String sdt) {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("is_logged_in", true);
        editor.putString("user_sdt", sdt);
        editor.apply();
    }

    @Override
    public void onClick(View view) {
        if(view == btnRegister)
        {
            Intent intent = new Intent(dangnhap.this, dangky.class);
            startActivity(intent);
        }
    }
}