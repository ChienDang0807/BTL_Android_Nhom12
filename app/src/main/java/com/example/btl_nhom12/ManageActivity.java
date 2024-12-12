package com.example.btl_nhom12;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_nhom12.config.SQLite;
import com.example.btl_nhom12.entity.nguoidung;
import com.example.btl_nhom12.repository.HealthRecordRepository;
import com.example.btl_nhom12.repository.UserRepository;
import com.example.btl_nhom12.service.dangnhap;
import com.example.btl_nhom12.service.theodoisuckhoe;


import java.util.Calendar;

public class ManageActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView btnngaydo, btnhome, btnlogout;
    Button btncapnhat, btnxoa;
    private UserRepository userRepository;
    private HealthRecordRepository healthRecordRepository;
    private nguoidung currentUser;
    EditText txtngaydo, txtcannang, txtchieucao;
    TextView txtuser;
    String ngaythang, cannang, chieucao, sdt, bmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWidget();
        SQLite databaseHelper = new SQLite(this);
        userRepository = new UserRepository(databaseHelper);
        healthRecordRepository = new HealthRecordRepository(databaseHelper);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString("user_sdt", null);
        currentUser = userRepository.getUserBysdt(phoneNumber);
        if(currentUser != null)
        {
            txtuser.setText(currentUser.getTen());
        }
        else
        {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
        }

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
         sdt = intent.getStringExtra("sdt");
         bmi = intent.getStringExtra("bmi");
         ngaythang = intent.getStringExtra("ngaythang");
         chieucao = intent.getStringExtra("chieucao");
         cannang = intent.getStringExtra("cannang");

        // Hiển thị dữ liệu lên giao diện
        txtngaydo.setText(ngaythang);
        txtcannang.setText(cannang);
        txtchieucao.setText(chieucao);

    }

    private void getWidget() {
        btnngaydo = findViewById(R.id.calngaydo);
        btnhome = findViewById(R.id.home);
        btnlogout = findViewById(R.id.logout);
        btncapnhat = findViewById(R.id.btncapnhat);
        btnxoa = findViewById(R.id.btnxoa);
        txtngaydo = findViewById(R.id.txtngaydo);
        txtcannang = findViewById(R.id.txtcannang);
        txtchieucao = findViewById(R.id.txtchieucao);
        txtuser = findViewById(R.id.tennguoidung);
        btncapnhat.setOnClickListener(this);
        btnxoa.setOnClickListener(this);
        btnhome.setOnClickListener(this);
        btnlogout.setOnClickListener(this);
        btnngaydo.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if( btnngaydo == view)
        {
            // Lấy ngày tháng năm từ TextView txtngaysinh
            String currentDate = txtngaydo.getText().toString().trim();
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
                txtngaydo.setText(d + "/" + (m + 1) + "/" + t); // Cập nhật TextView với ngày được chọn
            }, yy, mm, dd);

            datePickerDialog.show();
        }

        if(btnhome == view)
        {
            Intent intenthome = new Intent(this, MainActivity.class);
            startActivity(intenthome);
        }
        if(view == btnlogout)
        {
            new AlertDialog.Builder(ManageActivity.this)
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        // Thực hiện đăng xuất nếu người dùng chọn "Có"
                        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear(); // Xóa toàn bộ dữ liệu
                        editor.apply();

                        Intent intent = new Intent(ManageActivity.this, dangnhap.class);
                        startActivity(intent);
                        finish(); // Đóng Activity hiện tại
                    })
                    .setNegativeButton("Không", (dialog, which) -> {
                        // Đóng hộp thoại nếu người dùng chọn "Không"
                        dialog.dismiss();
                    })
                    .show();
        }
        if(view == btncapnhat)
        {
            updateHealthRecord();

        }
        if(view == btnxoa)
        {
            deleteRecord(txtchieucao.getText().toString(), txtcannang.getText().toString(), txtngaydo.getText().toString());
        }

    }
    private void deleteRecord(String chieucao, String cannang, String ngaythang) {
        SQLite databaseHelper = new SQLite(this);

        // Tạo Dialog xác nhận
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa bản ghi này không?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xóa bản ghi trong cơ sở dữ liệu
                        try {
                            healthRecordRepository.deleteHealthRecordByAttributes(txtchieucao.getText().toString(), txtcannang.getText().toString(), txtngaydo.getText().toString());
                            Toast.makeText(ManageActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ManageActivity.this, theodoisuckhoe.class);
                            startActivity(intent);
                            finish();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            Toast.makeText(ManageActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();

                        }

                    }
                })
                .setNegativeButton("Hủy", null) // Không làm gì nếu chọn "Hủy"
                .show();
    }
    public void updateHealthRecord() {
        // Lấy dữ liệu từ các trường nhập liệu hoặc các giá trị hiện tại
        String chieucaomoi = txtchieucao.getText().toString().trim();
        String cannangmoi = txtcannang.getText().toString().trim();
        String ngaythangmoi = txtngaydo.getText().toString().trim();
        String sdtmoi = sdt;

        double bmi1 = Double.parseDouble(cannangmoi) / (Double.parseDouble(chieucaomoi) * Double.parseDouble(chieucaomoi));
        double roundedBmi = Math.round(bmi1 * 10000.0) / 10000.0;
        String bmimoi = String.valueOf(roundedBmi);

        // Kiểm tra các trường không rỗng
        if (chieucaomoi.isEmpty() || cannangmoi.isEmpty()  || ngaythangmoi.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng SQLite
        SQLite databaseHelper = new SQLite(this);

        // Gọi phương thức cập nhật
        try {
            healthRecordRepository.updateHealthRecordByAttributes(
                    chieucao, cannang, bmi, sdt, ngaythang,
                    chieucaomoi, cannangmoi, bmimoi, sdtmoi, ngaythangmoi);
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, theodoisuckhoe.class);
            startActivity(intent);
            finish();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Không tìm thấy bản ghi để cập nhật!", Toast.LENGTH_SHORT).show();

        }
    }


}