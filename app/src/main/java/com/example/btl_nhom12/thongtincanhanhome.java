package com.example.btl_nhom12;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_nhom12.doituong.nguoidung;

public class thongtincanhanhome extends AppCompatActivity implements  View.OnClickListener {
    CardView btncapnhat, btnkiemtra;
    ImageView btnhome;
    private SQLite databaseHelper;
    TextView txtuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thongtincanhanhome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
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

    private void getWidget() {
        btncapnhat = findViewById(R.id.capnhatthongtin);
        btnhome = findViewById(R.id.home1);
        btnkiemtra = findViewById(R.id.kiemtrachiso);
        btncapnhat.setOnClickListener(this);
        btnkiemtra.setOnClickListener(this);
        btnhome.setOnClickListener(this);
        txtuser = findViewById(R.id.user);
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

    @Override
    public void onClick(View view) {
        if(btncapnhat == view)
        {
            Intent intentcapnhat = new Intent(this, thongtincanhan.class);
            startActivity(intentcapnhat);
        }
        if(btnkiemtra == view)
        {
            Intent intentkiemtra = new Intent(this, kiemtrachiso.class);
            startActivity(intentkiemtra);
        }
        if(btnhome == view)
        {
            Intent intenthome = new Intent(this, MainActivity.class);
            startActivity(intenthome);
        }
    }
}