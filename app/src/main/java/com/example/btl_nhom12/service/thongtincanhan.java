package com.example.btl_nhom12.service;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_nhom12.MainActivity;
import com.example.btl_nhom12.R;
import com.example.btl_nhom12.config.SQLite;
import com.example.btl_nhom12.entity.nguoidung;
import com.example.btl_nhom12.repository.UserRepository;

import java.util.Calendar;

public class thongtincanhan extends AppCompatActivity implements View.OnClickListener {
    ImageView btnngaysinh, btnhome;
    private int style = AlertDialog.THEME_HOLO_DARK;
    private SQLite databaseHelper;
    private UserRepository userRepository;
    private nguoidung currentUser;
    Button btncapnhat;
    TextView txthoten, txtngaysinh, txtmail, txtsdt, txtmatkhau, txtuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thongtincanhan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWidget();
        getDateNow();

        databaseHelper = new SQLite(this);
        userRepository = new UserRepository(databaseHelper);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString("user_sdt", null);
        currentUser = userRepository.getUserBysdt(phoneNumber);
        if(currentUser != null)
        {
            txthoten.setText(currentUser.getTen());
            txtngaysinh.setText(currentUser.getNgaysinh());
            txtmail.setText(currentUser.getEmail());
            txtsdt.setText(currentUser.getSdt());
            txtmatkhau.setText(currentUser.getMatkhau());
            txtuser.setText(currentUser.getTen());
        }
        else
        {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDateNow() {
        Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);
        txtngaysinh.setText(dd+"/"+(mm + 1)+"/"+yy);
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userPhone = sharedPreferences.getString("user_sdt", null);
        nguoidung updatedUser = userRepository.getUserBysdt(userPhone);
        if (updatedUser != null) {
            txtuser.setText(updatedUser.getTen());
        }
    }


    private void getWidget() {
        btnngaysinh = findViewById(R.id.calngaysinh);
        btnngaysinh.setOnClickListener(this);
        txtngaysinh = findViewById(R.id.txtngaysinh);
        btnhome = findViewById(R.id.home);
        btnhome.setOnClickListener(this);
        txtuser = findViewById(R.id.tennguoidung);
        txthoten = findViewById(R.id.txthoten);
        txtngaysinh = findViewById(R.id.txtngaysinh);
        txtmail = findViewById(R.id.txtemail);
        txtsdt = findViewById(R.id.txtdienthoai);
        btncapnhat = findViewById(R.id.btncapnhat);
        txtmatkhau = findViewById(R.id.txtmatkhau);
        btncapnhat.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if( btnngaysinh == view)
        {
            // Lấy ngày tháng năm từ TextView txtngaysinh
            String currentDate = txtngaysinh.getText().toString().trim();
            int dd, mm, yy;

            if (!TextUtils.isEmpty(currentDate) && currentDate.contains("/")) {
                // Phân tách ngày/tháng/năm từ chuỗi
                String[] dateParts = currentDate.split("/");
                dd = Integer.parseInt(dateParts[0]);
                mm = Integer.parseInt(dateParts[1]) - 1; // Tháng bắt đầu từ 0 trong Calendar
                yy = Integer.parseInt(dateParts[2]);
            } else {
                // Nếu TextView không có giá trị, lấy ngày tháng hiện tại
                Calendar c = Calendar.getInstance();
                yy = c.get(Calendar.YEAR);
                mm = c.get(Calendar.MONTH);
                dd = c.get(Calendar.DAY_OF_MONTH);
            }

            // Tạo DatePickerDialog với ngày, tháng, năm lấy từ TextView
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view1, t, m, d) -> {
                txtngaysinh.setText(d + "/" + (m + 1) + "/" + t); // Cập nhật TextView với ngày được chọn
            }, yy, mm, dd);

            datePickerDialog.show();
        }

        if(btnhome == view)
    {
        Intent intenthome = new Intent(this, MainActivity.class);
        startActivity(intenthome);
    }
        if (btncapnhat == view) {
            String name = txthoten.getText().toString().trim();
            String dob = txtngaysinh.getText().toString().trim();
            String email = txtmail.getText().toString().trim();
            String password = txtmatkhau.getText().toString().trim();
            String sdt = txtsdt.getText().toString().trim();

            // Kiểm tra dữ liệu đầu vào
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(dob) || TextUtils.isEmpty(email) || TextUtils.isEmpty(sdt) || TextUtils.isEmpty(password)) {
                Toast.makeText(thongtincanhan.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                // Cập nhật thông tin trong đối tượng currentUser
                currentUser.setTen(name);
                currentUser.setNgaysinh(dob);
                currentUser.setEmail(email);
                currentUser.setMatkhau(password);
                currentUser.setSdt(sdt);

                try {
                    // Gọi hàm updateUser
                    userRepository.updateUser(currentUser);
                    Toast.makeText(thongtincanhan.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();

                    // Làm tươi dữ liệu
                    currentUser = userRepository.getUserBysdt(sdt);  // Lấy lại thông tin người dùng sau khi cập nhật
                    if (currentUser != null) {
                        // Cập nhật giao diện người dùng với thông tin mới
                        txthoten.setText(currentUser.getTen());
                        txtngaysinh.setText(currentUser.getNgaysinh());
                        txtmail.setText(currentUser.getEmail());
                        txtmatkhau.setText(currentUser.getMatkhau());
                    }

                    finish();
                } catch (RuntimeException e) {
                    Toast.makeText(thongtincanhan.this, "Cập nhật thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}