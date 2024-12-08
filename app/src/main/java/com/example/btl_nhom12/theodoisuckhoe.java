package com.example.btl_nhom12;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class theodoisuckhoe extends AppCompatActivity implements View.OnClickListener {
    ImageView btnthemtheodoi;

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
    }

    private void getWidget() {
        btnthemtheodoi = findViewById(R.id.themtheodoi);
        btnthemtheodoi.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btnthemtheodoi)
        {
            Intent intent = new Intent(this,kiemtrachiso.class);
            startActivity(intent);
        }
    }
}