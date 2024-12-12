package com.example.btl_nhom12.service;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_nhom12.MainActivity;
import com.example.btl_nhom12.ManageActivity;
import com.example.btl_nhom12.R;
import com.example.btl_nhom12.adapter.AdapterChiso;
import com.example.btl_nhom12.config.SQLite;
import com.example.btl_nhom12.entity.chisosuckhoe;
import com.example.btl_nhom12.entity.nguoidung;
import com.example.btl_nhom12.repository.HealthRecordRepository;
import com.example.btl_nhom12.repository.UserRepository;

import java.util.ArrayList;

public class theodoisuckhoe extends AppCompatActivity implements View.OnClickListener {
    ImageView btnthemtheodoi,btnlogout, btnhome;
    private SQLite databaseHelper;
    private HealthRecordRepository healthRecordRepository;
    private UserRepository userRepository;

    TextView txtuser;
    private nguoidung currentUser;
    ListView listView;
    ArrayList<chisosuckhoe> list;
    ArrayAdapter<chisosuckhoe> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_theodoisuckhoe);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWidget();
        databaseHelper = new SQLite(this);
        userRepository = new UserRepository(databaseHelper);
        healthRecordRepository = new  HealthRecordRepository(databaseHelper);

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

        list = new ArrayList<>();
        list =  healthRecordRepository.getHealthRecordsByPhone(phoneNumber);
        adapter = new AdapterChiso(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy item được chọn
                chisosuckhoe selectedRecord = list.get(position);

                // Tạo Intent để gửi dữ liệu
                Intent intent = new Intent(theodoisuckhoe.this, ManageActivity.class);

                // Đưa thông tin vào Intent
                intent.putExtra("ngaythang", selectedRecord.getDate());
                intent.putExtra("sdt", selectedRecord.getSdt());
                intent.putExtra("bmi", selectedRecord.getBmi());
                intent.putExtra("chieucao", selectedRecord.getHeight());
                intent.putExtra("cannang", selectedRecord.getWeight());

                // Chuyển sang màn hình quản lý
                startActivity(intent);

                return true;
            }
        });
    }

    private void getWidget() {
        btnthemtheodoi = findViewById(R.id.themtheodoi);
        btnthemtheodoi.setOnClickListener(this);
        txtuser = findViewById(R.id.user);
        btnlogout = findViewById(R.id.logout);
        btnlogout.setOnClickListener(this);
        listView = findViewById(R.id.danhsachchiso);
        btnhome = findViewById(R.id.home1);
        btnhome.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        if(btnhome == view)
        {
            Intent intenthome = new Intent(this, MainActivity.class);
            startActivity(intenthome);
        }
        if(view == btnthemtheodoi)
        {
            Intent intent = new Intent(this,kiemtrachiso.class);
            startActivity(intent);
        }
        if(view == btnlogout)
        {
            new AlertDialog.Builder(theodoisuckhoe.this)
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        // Thực hiện đăng xuất nếu người dùng chọn "Có"
                        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear(); // Xóa toàn bộ dữ liệu
                        editor.apply();

                        Intent intent = new Intent(theodoisuckhoe.this, dangnhap.class);
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