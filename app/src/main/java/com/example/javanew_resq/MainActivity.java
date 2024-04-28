package com.example.javanew_resq;


import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.Manifest;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    Button button2;
    TextView textView;
    FirebaseUser user;
    BottomSheetDialog sheetDialog;
    static int PERMISSION_CODE= 100;
    private boolean firebutton = true;
    private boolean quakebutton = true;



    ActivityResultLauncher<String[]> mPermissionResultLauncher;
    private boolean isCallPermissionGranted = false;
    private boolean isLocationPermissionGranted = false;
    private DatabaseReference mDatabase;

    private static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        button2 = findViewById(R.id.button2);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        DatabaseReference reference = mDatabase.child("PARAMEDIC_LINE");

        mPermissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {

                if (result.get(Manifest.permission.CALL_PHONE) != null){

                    isCallPermissionGranted = Boolean.TRUE.equals(result.get(Manifest.permission.CALL_PHONE));

                }

                if (result.get(Manifest.permission.ACCESS_FINE_LOCATION) != null){

                    isLocationPermissionGranted = Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_FINE_LOCATION));

                }

            }
        });

        requestPermission();

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
    private void requestPermission(){

        isCallPermissionGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED;

        isLocationPermissionGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        List<String> permissionRequest = new ArrayList<String>();

        if (!isCallPermissionGranted){

            permissionRequest.add(Manifest.permission.CALL_PHONE);
        }
        if (!isLocationPermissionGranted){

            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!permissionRequest.isEmpty()){

            mPermissionResultLauncher.launch(permissionRequest.toArray(new String[0]));
        }


    }
    private void showDialog() {

        final Dialog sheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetStyle);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.bottomsheet_dialog,
                (LinearLayout) findViewById(R.id.sheet));
        sheetDialog.setContentView(view);

        Button paramedbtn;
        Button firedeptbtn;
        Button policebtn;
        Button firebtn;
        Button quakebtn;

        paramedbtn = sheetDialog.findViewById(R.id.paramed_call);
        firedeptbtn = sheetDialog.findViewById(R.id.firedept_call);
        policebtn = sheetDialog.findViewById(R.id.police_call);
        firebtn = sheetDialog.findViewById(R.id.fire_alarm);
        quakebtn = sheetDialog.findViewById(R.id.earthquake_alarm);



        firebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!firebutton){
                    firebutton=true;
                    firebtn.setBackground(getResources().getDrawable(R.drawable.act_fire));
                    mDatabase.child("FIRE_STATUS").setValue(firebutton);
                }
                else{
                    firebutton=false;
                    firebtn.setBackground(getResources().getDrawable(R.drawable.def_fire));
                    quakebtn.setBackground(getResources().getDrawable(R.drawable.act_quake));
                    mDatabase.child("FIRE_STATUS").setValue(firebutton);
                }
            }

        });
        quakebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!quakebutton){
                    quakebutton=true;
                    quakebtn.setBackground(getResources().getDrawable(R.drawable.act_quake));
                    mDatabase.child("EARTHQUAKE_STATUS").setValue(quakebutton);
                }
                else{
                    quakebutton=false;
                    quakebtn.setBackground(getResources().getDrawable(R.drawable.def_quake));
                    firebtn.setBackground(getResources().getDrawable(R.drawable.act_fire));
                    mDatabase.child("EARTHQUAKE_STATUS").setValue(quakebutton);
                }
            }

        });

        policebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:4185400"));
                startActivity(intent);
            }

        });


        firedeptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:56416540"));
                startActivity(intent);
            }

        });

        paramedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:119981"));
                startActivity(intent);
            }

        });


        sheetDialog.show();

    }

}