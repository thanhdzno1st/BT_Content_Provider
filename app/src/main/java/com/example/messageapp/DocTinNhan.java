package com.example.messageapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.messageapp.Model.TinNhan;
import com.example.messageapp.adapter.AdapterTinNhan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DocTinNhan extends AppCompatActivity {
    private static final int REQUEST_SMS_PERMISSION = 1002;
    ListView lvDocTinNhan;
    ArrayList<TinNhan> dsTinNhan;
    AdapterTinNhan adapterTinNhan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doc_tin_nhan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControl();

        // Kiểm tra quyền READ_SMS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, REQUEST_SMS_PERMISSION);
        } else {
            docToanBoTinNhan();
        }
    }

    private void docToanBoTinNhan() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor != null) {
            dsTinNhan.clear();
            while (cursor.moveToNext()) {
                // Lấy vị trí tên cột trong dữ liệu
                int indexPhoneNumber = cursor.getColumnIndex("address");
                int indexTimeStamp = cursor.getColumnIndex("date");
                int indexBody = cursor.getColumnIndex("body");

                // Lấy dữ liệu trong các cột
                String phoneNumber = cursor.getString(indexPhoneNumber);
                String timeStamp = cursor.getString(indexTimeStamp);
                String body = cursor.getString(indexBody);

                // In ra dữ liệu để kiểm tra
                Log.d("DocTinNhan", "Phone: " + phoneNumber + ", Time: " + timeStamp + ", Body: " + body);

                // Đưa vào mảng
                dsTinNhan.add(new TinNhan(phoneNumber, sdf.format(Long.parseLong(timeStamp)), body));
            }
            cursor.close();
            adapterTinNhan.notifyDataSetChanged();
        } else {
            Log.e("DocTinNhan", "Không có dữ liệu trong hộp thư đến.");
            Toast.makeText(this, "Không có tin nhắn nào trong hộp thư đến.", Toast.LENGTH_SHORT).show();
        }
    }


    private void addControl() {
        lvDocTinNhan = findViewById(R.id.lvDocTinNhan);
        dsTinNhan = new ArrayList<>();
        adapterTinNhan = new AdapterTinNhan(this, R.layout.item_tinnhan, dsTinNhan);
        lvDocTinNhan.setAdapter(adapterTinNhan);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Được cấp quyền, đọc tin nhắn
                docToanBoTinNhan();
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền đọc tin nhắn để sử dụng tính năng này.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
