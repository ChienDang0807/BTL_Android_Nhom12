package com.example.btl_nhom12.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.btl_nhom12.R;
import com.example.btl_nhom12.entity.chisosuckhoe;

import java.util.ArrayList;

public class AdapterChiso extends ArrayAdapter<chisosuckhoe> {
    private Context context;
    private ArrayList<chisosuckhoe> list;

    public AdapterChiso(@NonNull Context context, int itemlv, ArrayList<chisosuckhoe> list) {
        super(context, R.layout.listviewchiso, list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listviewchiso, null, true);
        chisosuckhoe chiso = list.get(position);

        TextView date = view.findViewById(R.id.txtngaythangdo);
        date.setText(chiso.getDate());

        TextView cannang = view.findViewById(R.id.txtcannang);
        cannang.setText(chiso.getWeight());

        TextView chieucao = view.findViewById(R.id.txtchieucao);
        chieucao.setText(chiso.getHeight());

        TextView bmi = view.findViewById(R.id.txtbmi);
        bmi.setText(chiso.getBmi());

    return view;
}

}

