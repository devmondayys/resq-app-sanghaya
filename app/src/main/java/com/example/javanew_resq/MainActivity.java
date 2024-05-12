package com.example.javanew_resq;


import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
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
import com.google.firebase.database.ChildEventListener;
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

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    Button button2;
    TextView textView;
    TextView textView1;
    TextView conndeet;
    FirebaseUser user;
    ImageView status_light;
    ImageView connection;
    BottomSheetDialog sheetDialog;
    static int PERMISSION_CODE= 100;
    private boolean firebutton = true;
    private boolean quakebutton = true;
    DrawerLayout drawerLayout;
    Button sidebar_open;
    NavigationView navigationView;

    private DatabaseReference mDatabase;



    ActivityResultLauncher<String[]> mPermissionResultLauncher;
    private boolean isCallPermissionGranted = false;
    private boolean isLocationPermissionGranted = false;

    private int PARAMEDIC_LINE;

    private static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    private Object text;
    private String title;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        button2 = findViewById(R.id.button2);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();


        drawerLayout = findViewById(R.id.drawerLayout);
        sidebar_open = findViewById(R.id.sidebar_open);
        navigationView = findViewById(R.id.NavigationView);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        textView = findViewById(R.id.user);
        textView1 = findViewById(R.id.emer_title);
        status_light = findViewById(R.id.status_light);
        connection = findViewById(R.id.connection);
        conndeet = findViewById(R.id.conndeet);

        mDatabase = FirebaseDatabase.getInstance().getReference("device_status");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Integer value = dataSnapshot.getValue(Integer.class);
                // Update the TextView with the retrieved value
                if (value != null) {
                    // Set different texts based on the value
                    switch (value) {
                        case 0:
                            status_light.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.baseline_circle_24_off));
                            break;
                        case 1:
                            status_light.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.baseline_circle_24_starting));
                            break;
                        case 2:
                            status_light.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.baseline_circle_24_active));
                            break;
                        default:
                            // Set a default text for other values
                            status_light.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.baseline_circle_24));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                status_light.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.baseline_circle_24));
            }
        });



        mDatabase = FirebaseDatabase.getInstance().getReference("wifi_status");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Integer value = dataSnapshot.getValue(Integer.class);
                // Update the TextView with the retrieved value
                if (value != null) {
                    // Set different texts based on the value
                    switch (value) {
                        case 0:
                            connection.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.baseline_circle_24_off));
                            break;
                        case 1:
                            connection.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.baseline_circle_24_starting));
                            break;
                        case 2:
                            connection.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.baseline_circle_24_active));
                            break;
                        default:
                            // Set a default text for other values
                            connection.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.baseline_circle_24));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                connection.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.baseline_circle_24));
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference("Wifi/ssid");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);

                // Update the TextView with the retrieved value
                if (value != null) {
                    // Assuming 'value' is the text retrieved from the database
                    String connectedText = "Connected to: ";
                    SpannableString spannableString = new SpannableString(connectedText + value);

// Make the 'value' part bold
                    StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                    spannableString.setSpan(boldSpan, connectedText.length(), spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// Make the 'value' part white
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.WHITE);
                    spannableString.setSpan(colorSpan, connectedText.length(), spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// Set the SpannableString to the TextView
                    conndeet.setText(spannableString);

// Set the SpannableString to the TextView
                    conndeet.setText(spannableString);
                } else{
                    conndeet.setText("Connecting...");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                conndeet.setText("Connection Failed");
            }
        });








        int num = 1;
        if (user.getUid().equalsIgnoreCase("rcHgvzu9wJTP5k65LlSjFu4KFO93")){
            textView.setText("ADMIN");
        } else if (user.getUid().equalsIgnoreCase("xhwQuG9n7XQJ5Jlr9tZq0zuhiyE2")) {
            textView.setText("MAINTENANCE");
        } else{
            textView.setText("DEFAULT");
        }


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
                    drawerLayout.close();
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
                    Intent intent = new Intent(getApplicationContext(), Faq.class);
                    startActivity(intent);
                }
                return false;
            }
        });
//             Initialize Firebase database
            mDatabase = FirebaseDatabase.getInstance().getReference();

            // Attach a listener to read the data at our reference "example"


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                auth.signOut();
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
                Toast.makeText(MainActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
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


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("Alert!")
                        .setMessage("Please confirm activation of the alarm system for the emergency.")
                        .setCancelable(true)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                showDialog();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();

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
    @SuppressLint({"CutPasteId", "UseCompatLoadingForDrawables"})
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
        AlertDialog.Builder builder;

        user = auth.getCurrentUser();

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
                    mDatabase.child("ev").setValue(0);

                }
            else{
                    firebutton=false;
                    mDatabase.child("ev").setValue(1);
                }
            }

        });
        quakebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!quakebutton){
                    quakebutton=true;
                    mDatabase.child("qv").setValue(0);
                }
                else{
                    quakebutton=false;
                    mDatabase.child("qv").setValue(1);
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
                intent.setData(Uri.parse("tel:56416540"));
                startActivity(intent);
            }

        });


        sheetDialog.show();

    }

}