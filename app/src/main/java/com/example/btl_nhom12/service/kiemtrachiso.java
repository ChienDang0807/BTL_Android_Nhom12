package com.example.btl_nhom12.service;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.example.btl_nhom12.repository.HealthRecordRepository;
import com.example.btl_nhom12.repository.UserRepository;

import java.util.Calendar;

public class kiemtrachiso extends AppCompatActivity implements  View.OnClickListener    {
    ImageView btnhome, btnngaythang, btnlogout;
    EditText txtngaythang, txtcannang, txtchieucao;
    private  int style = AlertDialog.THEME_HOLO_DARK;
    Button btnbmi, btnthem;
    private nguoidung currentUser;
    private UserRepository userRepository;
    private HealthRecordRepository healthRecordRepository;
    TextView txtuser, txtbmi;
    String phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_kiemtrachiso);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWidget();
        getDateNow();
        SQLite databaseHelper = new SQLite(this);
        userRepository = new UserRepository(databaseHelper);
        healthRecordRepository = new HealthRecordRepository(databaseHelper);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString("user_sdt", null);
        currentUser = userRepository.getUserBysdt(phoneNumber);
        if(currentUser != null)
        {
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
        txtngaythang.setText(dd+"/"+(mm + 1)+"/"+yy);
    }

    private void getWidget() {
        btnhome = findViewById(R.id.home1);
        btnhome.setOnClickListener(this);
        btnngaythang = findViewById(R.id.calngaythang);
        btnngaythang.setOnClickListener(this);
        txtngaythang = findViewById(R.id.txtngaythang);
        btnthem = findViewById(R.id.btnthem);
        btnthem.setOnClickListener(this);
        btnbmi = findViewById(R.id.btnbmi);
        btnbmi.setOnClickListener(this);
        txtuser = findViewById(R.id.user);
        txtcannang = findViewById(R.id.txtcannang);
        txtchieucao = findViewById(R.id.txtchieucao);
        txtbmi = findViewById(R.id.txtbmi);
        btnlogout = findViewById(R.id.logout);
        btnlogout.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if(btnhome == view)
        {
            Intent intenthome = new Intent(this, MainActivity.class);
            startActivity(intenthome);
        }
        if( btnngaythang == view)
        {
            Calendar c = Calendar.getInstance();
            int yy = c.get(Calendar.YEAR);
            int mm = c.get(Calendar.MONTH);
            int dd = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int t, int m, int d) {
                    txtngaythang.setText(d+"/"+(m+1)+"/"+ t);
                }
            },yy,mm,dd);
            datePickerDialog.show();
        }
        if(btnthem == view)
        {

            Intent intent = new Intent(this, theodoisuckhoe.class);
            startActivity(intent);
        }
        if (btnbmi == view) {
            String date = txtngaythang.getText().toString().trim();
            String edcannang = txtcannang.getText().toString().trim();
            String edchieucao = txtchieucao.getText().toString().trim();

            // Kiểm tra trường ngày tháng
            if (date.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập ngày tháng!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra trường cân nặng
            if (edcannang.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập cân nặng!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra trường chiều cao
            if (edchieucao.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập chiều cao!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Chuyển đổi sang kiểu double
                double cannang = Double.parseDouble(edcannang);
                double chieucao = Double.parseDouble(edchieucao);

                // Kiểm tra chiều cao hợp lệ
                if (chieucao <= 0) {
                    Toast.makeText(this, "Chiều cao phải lớn hơn 0!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tính BMI
                double bmi = cannang / (chieucao * chieucao);

                // Làm tròn BMI
                double roundedBmi = Math.round(bmi * 10000.0) / 10000.0;

                // Hiển thị kết quả BMI dưới dạng chuỗi
                txtbmi.setText(String.valueOf(roundedBmi));

                // Lưu thông tin BMI vào cơ sở dữ liệu
                healthRecordRepository.addHealthRecord(phoneNumber, String.valueOf(chieucao), String.valueOf(cannang), String.valueOf(roundedBmi), date);
                Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();


            } catch (NumberFormatException e) {
                // Xử lý lỗi khi nhập dữ liệu không phải số
                Toast.makeText(this, "Cân nặng và chiều cao phải là số hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        }

        if(btnlogout == view)
        {
            new AlertDialog.Builder(kiemtrachiso.this)
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        // Thực hiện đăng xuất nếu người dùng chọn "Có"
                        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear(); // Xóa toàn bộ dữ liệu
                        editor.apply();

                        Intent intent = new Intent(kiemtrachiso.this, dangnhap.class);
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