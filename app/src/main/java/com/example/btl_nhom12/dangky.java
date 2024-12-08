package com.example.btl_nhom12;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class dangky extends AppCompatActivity implements View.OnClickListener{
    EditText etName, etEmail, etDOB, etPhone, etPassword;
    private Button btnRegister, btnlogin;
    private SQLite databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dangky);
        databaseHelper = new SQLite(this);

        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etDOB = findViewById(R.id.dob);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        btnRegister = findViewById(R.id.btn_register);
        btnlogin = findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(this);


        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String dob = etDOB.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(dob)
                    || TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
                Toast.makeText(dangky.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                databaseHelper.addUser(name, email, dob, phone, password);
                Toast.makeText(dangky.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, dangnhap.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == btnlogin)
        {
            Intent intent = new Intent(dangky.this, dangnhap.class);
            startActivity(intent);
        }
    }
}