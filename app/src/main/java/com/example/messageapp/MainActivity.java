package com.example.messageapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CONTACTS_ASK_PERMISSIONS = 1001;
    private static final int REQUEST_SMS_ASK_PERMISSIONS = 1002;
    Button btn1,btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        AnhXa();
        addEvents();
    }
    private void addEvents(){
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XuLyMoManHinhDanhBa();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XuLyMoManHinhTinNhan();
            }
        });
    }

    private void XuLyMoManHinhTinNhan() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{""+
            "android.permission.READ_SMS"},REQUEST_SMS_ASK_PERMISSIONS);
        }else{
            Intent intent = new Intent(MainActivity.this,DocTinNhan.class);
            intent.setClassName("com.example.messageapp","com.example.messageapp.DocTinNhan");
            startActivity(intent);
        }
    }

    private void XuLyMoManHinhDanhBa() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{""+
                    "android.permission.READ_CONTACTS"},REQUEST_SMS_ASK_PERMISSIONS);
        }else{
            Intent intent = new Intent(MainActivity.this, DanhBa.class);
            intent.setClassName("com.example.messageapp","com.example.messageapp.DanhBa");
            startActivity(intent);
        }

    }

    private void AnhXa() {
        btn1 = (Button) findViewById(R.id.btn_1);
        btn2 = (Button) findViewById(R.id.btn_2);

    }
}