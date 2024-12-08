package com.example.btl_nhom12;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class kiemtrachiso extends AppCompatActivity implements  View.OnClickListener    {
    ImageView btnhome, btnngaythang;
    EditText txtngaythang;
    private int style = AlertDialog.THEME_HOLO_DARK;
    Button btnbmi, btnthem;

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
            datePickerDialog = new DatePickerDialog(this, style, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int t, int m, int d) {
                    txtngaythang.setText(d+"/"+(m+1)+"/"+ t);
                }
            },yy,mm,dd);
            datePickerDialog.show();
        }
        if(btnthem == view)
        {
            Intent intent = new Intent(this,theodoisuckhoe.class);
            startActivity(intent);
        }

    }
}