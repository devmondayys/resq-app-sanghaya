package com.example.javanew_resq;


import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Faq extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    Button button2;
    TextView textView;
    FirebaseUser user;
    DrawerLayout drawerLayout;
    Button sidebar_open;
    NavigationView navigationView;
    String[] item = {"The system automatically rings at 7:10 to remind students of the time 7:15 to indicate the start of the flag ceremony and listing of tardiness in the morning. It also offers remote triggering capabilities via the ResQ app for emergency situations like earthquakes and fires. "};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterText;



    ActivityResultLauncher<String[]> mPermissionResultLauncher;
    private boolean isCallPermissionGranted = false;
    private boolean isLocationPermissionGranted = false;
    private DatabaseReference mDatabase;

    private int PARAMEDIC_LINE;

    private static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        button2 = findViewById(R.id.button2);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();
        String admin = "stec.jhsandshs@gmail.com";
        String maintenance = "resqproject2024@gmail.com";
        drawerLayout = findViewById(R.id.drawerLayout);
        sidebar_open = findViewById(R.id.sidebar_open);
        navigationView = findViewById(R.id.NavigationView);

        TextView text1 = findViewById(R.id.faqtext1);
        TextView text2 = findViewById(R.id.faqtext2);
        TextView text3 = findViewById(R.id.faqtext3);
        TextView text4 = findViewById(R.id.faqtext4);
        TextView text5 = findViewById(R.id.faqtext5);
        TextView text6 = findViewById(R.id.faqtext6);
        TextView text7 = findViewById(R.id.faqtext7);

        ImageView faqbutton1 = findViewById(R.id.faqbutton1);
        ImageView faqbutton2 = findViewById(R.id.faqbutton2);
        ImageView faqbutton3 = findViewById(R.id.faqbutton3);
        ImageView faqbutton4 = findViewById(R.id.faqbutton4);
        ImageView faqbutton5 = findViewById(R.id.faqbutton5);
        ImageView faqbutton6 = findViewById(R.id.faqbutton6);
        ImageView faqbutton7 = findViewById(R.id.faqbutton7);

        faqbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text1.getVisibility() == View.GONE){
                    text1.setVisibility(View.VISIBLE);
                    faqbutton1.setImageResource(R.drawable.ic_up1);
                } else {
                    text1.setVisibility(View.GONE);
                    faqbutton1.setImageResource(R.drawable.ic_down1);
                }
            }
        });

        faqbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text2.getVisibility() == View.GONE){
                    text2.setVisibility(View.VISIBLE);
                    faqbutton2.setImageResource(R.drawable.ic_up1);
                } else {
                    text2.setVisibility(View.GONE);
                    faqbutton2.setImageResource(R.drawable.ic_down1);
                }
            }
        });

        faqbutton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text3.getVisibility() == View.GONE){
                    text3.setVisibility(View.VISIBLE);
                    faqbutton3.setImageResource(R.drawable.ic_up1);
                } else {
                    text3.setVisibility(View.GONE);
                    faqbutton3.setImageResource(R.drawable.ic_down1);
                }
            }
        });

        faqbutton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text4.getVisibility() == View.GONE){
                    text4.setVisibility(View.VISIBLE);
                    faqbutton4.setImageResource(R.drawable.ic_up1);
                } else {
                    text4.setVisibility(View.GONE);
                    faqbutton4.setImageResource(R.drawable.ic_down1);
                }
            }
        });

        faqbutton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text5.getVisibility() == View.GONE){
                    text5.setVisibility(View.VISIBLE);
                    faqbutton5.setImageResource(R.drawable.ic_up1);
                } else {
                    text5.setVisibility(View.GONE);
                    faqbutton5.setImageResource(R.drawable.ic_down1);
                }
            }
        });

        faqbutton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text6.getVisibility() == View.GONE){
                    text6.setVisibility(View.VISIBLE);
                    faqbutton6.setImageResource(R.drawable.ic_up1);
                } else {
                    text6.setVisibility(View.GONE);
                    faqbutton6.setImageResource(R.drawable.ic_down1);
                }
            }
        });

        faqbutton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text7.getVisibility() == View.GONE){
                    text7.setVisibility(View.VISIBLE);
                    faqbutton7.setImageResource(R.drawable.ic_up1);
                } else {
                    text7.setVisibility(View.GONE);
                    faqbutton7.setImageResource(R.drawable.ic_down1);
                }
            }
        });

        sidebar_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.navMenu) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

                if (itemId == R.id.navMenu2) {
                    Intent intent = new Intent(getApplicationContext(), Settings2.class);
                    startActivity(intent);
                }
                if (itemId == R.id.navMenu3) {
                    String url = "https://console.firebase.google.com/u/7/project/resqdtb/database/resqdtb-default-rtdb/data";

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
                if (itemId == R.id.navMenu4) {
                    Intent intent = new Intent(getApplicationContext(), Manual.class);
                    startActivity(intent);
                }

                if (itemId == R.id.navMenu5) {
                    drawerLayout.close();
                }
                return false;
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        DatabaseReference reference = mDatabase.child("PARAMEDIC_LINE");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                auth.signOut();
                Intent intent = new Intent(Faq.this, Login.class);
                startActivity(intent);
                finish();
                Toast.makeText(Faq.this, "Logout Successful", Toast.LENGTH_SHORT).show();
            }
        });

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

        String email = user.getUid();


        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

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

}