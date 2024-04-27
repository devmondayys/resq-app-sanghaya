package com.example.javanew_resq;


import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    Button button2;
    TextView textView;
    FirebaseUser user;
    BottomSheetDialog sheetDialog;
    static int PERMISSION_CODE= 100;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        button2 = findViewById(R.id.button2);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
        }

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();

            }
        });

    }
    private void showDialog() {

        final Dialog sheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetStyle);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.bottomsheet_dialog,
                (LinearLayout) findViewById(R.id.sheet));
        sheetDialog.setContentView(view);

        Button paramedbtn;
        Button firedeptbtn;
        Button policebtn;

        paramedbtn = sheetDialog.findViewById(R.id.paramed_call);
        firedeptbtn = sheetDialog.findViewById(R.id.firedept_call);
        policebtn = sheetDialog.findViewById(R.id.police_call);

        policebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:4185400"));
                startActivity(intent);
            }

        });


        firedeptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:56416540"));
                startActivity(intent);
            }

        });

        paramedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:09682008068"));
                startActivity(intent);
            }


        });

        sheetDialog.show();

    }

}