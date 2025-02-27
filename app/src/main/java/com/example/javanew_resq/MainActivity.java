package com.example.javanew_resq;

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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    Button button2;
    TextView textView;
    TextView textView1;
    FirebaseUser user;
    ImageView status_light;
    ImageView connection;
    BottomSheetDialog sheetDialog;
    private boolean firebutton = true;
    private boolean quakebutton = true;
    DrawerLayout drawerLayout;
    Button sidebar_open;
    NavigationView navigationView;

    private DatabaseReference mDatabase;

    private ArrayList<String> paraLabels = new ArrayList<>();
    private ArrayList<String> paraNumbers = new ArrayList<>();
    private ArrayList<String> fireLabels = new ArrayList<>();
    private ArrayList<String> fireNumbers = new ArrayList<>();
    private ArrayList<String> policeLabels = new ArrayList<>();
    private ArrayList<String> policeNumbers = new ArrayList<>();
    private Button showDialogMedButton, showDialogFireButton, showDialogPoliceButton;

    ActivityResultLauncher<String[]> mPermissionResultLauncher;
    private boolean isCallPermissionGranted = false;
    private boolean isLocationPermissionGranted = false;

    private static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;

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

        Typeface typeface = ResourcesCompat.getFont(this, R.font.official_font);
        textView.setTypeface(typeface);
        textView1.setTypeface(typeface);

        // Check for internet connection and prompt if not connected
        if (!isNetworkConnected()) {
            showInternetPrompt();
        }

        if (user != null) {
            if (user.getUid().equalsIgnoreCase("rcHgvzu9wJTP5k65LlSjFu4KFO93")) {
                textView.setText("ADMIN");
            } else if (user.getUid().equalsIgnoreCase("xhwQuG9n7XQJ5Jlr9tZq0zuhiyE2")) {
                textView.setText("MAINTENANCE");
            } else {
                textView.setText("RESQ");
            }
        } else {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
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
                    // Close drawer only
                } else if (itemId == R.id.navMenu2) {
                    startActivity(new Intent(getApplicationContext(), Settings2.class));
                } else if (itemId == R.id.navMenu3) {
                    String url = "https://console.firebase.google.com/u/7/project/resqdtb/database/resqdtb-default-rtdb/data";
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                } else if (itemId == R.id.navMenu4) {
                    startActivity(new Intent(getApplicationContext(), Manual.class));
                } else if (itemId == R.id.navMenu5) {
                    startActivity(new Intent(getApplicationContext(), Faq.class));
                }

                // Close drawer after item selection
                drawerLayout.closeDrawer(GravityCompat.START);
                return true; // Returning true keeps the item highlighted
            }
        });


        // Initialize Firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();

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
                if (result.get(Manifest.permission.CALL_PHONE) != null) {
                    isCallPermissionGranted = Boolean.TRUE.equals(result.get(Manifest.permission.CALL_PHONE));
                }

                if (result.get(Manifest.permission.ACCESS_FINE_LOCATION) != null) {
                    isLocationPermissionGranted = Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_FINE_LOCATION));
                }
            }
        });

        requestPermission();

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("Alert!")
                        .setMessage("Please confirm activation of the alarm system for the emergency.")
                        .setCancelable(true)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss(); // Dismiss the original AlertDialog
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

    private void requestPermission() {
        isCallPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
        isLocationPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        List<String> permissionRequest = new ArrayList<>();

        if (!isCallPermissionGranted) {
            permissionRequest.add(Manifest.permission.CALL_PHONE);
        }
        if (!isLocationPermissionGranted) {
            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!permissionRequest.isEmpty()) {
            mPermissionResultLauncher.launch(permissionRequest.toArray(new String[0]));
        }
    }

    @SuppressLint({"CutPasteId", "UseCompatLoadingForDrawables"})
    private void showDialog() {
        final Dialog sheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetStyle);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.bottomsheet_dialog, (LinearLayout) findViewById(R.id.sheet));
        sheetDialog.setContentView(view);

        Button firebtn = view.findViewById(R.id.fire_alarm);
        Button quakebtn = view.findViewById(R.id.earthquake_alarm);
        Button showDialogMedButton = view.findViewById(R.id.paramed_call);
        Button showDialogFireButton = view.findViewById(R.id.firedept_call);
        Button showDialogPoliceButton = view.findViewById(R.id.police_call);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        firebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If fire button is not activated
                if (!firebutton) {
                    // Activate fire button (set to play)
                    firebutton = true;
                    firebtn.setBackgroundResource(R.drawable.def_fire); // Set activated drawable
                    mDatabase.child("ev").setValue(1); // 1 = Play

                    // Deactivate quake button if it was activated
                    if (quakebutton) {
                        quakebutton = false;
                        quakebtn.setBackgroundResource(R.drawable.act_quake); // Set default drawable
                        mDatabase.child("qv").setValue(0); // 0 = Off
                    }
                } else {
                    // Deactivate fire button (set to off)
                    firebutton = false;
                    firebtn.setBackgroundResource(R.drawable.act_fire); // Set default drawable
                    mDatabase.child("ev").setValue(0); // 0 = Off
                }
            }
        });

        quakebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If quake button is not activated
                if (!quakebutton) {
                    // Activate quake button (set to play)
                    quakebutton = true;
                    quakebtn.setBackgroundResource(R.drawable.def_quake); // Set activated drawable
                    mDatabase.child("qv").setValue(1); // 1 = Play

                    // Deactivate fire button if it was activated
                    if (firebutton) {
                        firebutton = false;
                        firebtn.setBackgroundResource(R.drawable.act_fire); // Set default drawable
                        mDatabase.child("ev").setValue(0); // 0 = Off
                    }
                } else {
                    // Deactivate quake button (set to off)
                    quakebutton = false;
                    quakebtn.setBackgroundResource(R.drawable.act_quake); // Set default drawable
                    mDatabase.child("qv").setValue(0); // 0 = Off
                }
            }
        });



        showDialogMedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchPhoneNumbersMed();
            }
        });

        showDialogFireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchPhoneNumbersFire();
            }
        });

        showDialogPoliceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchPhoneNumbersPolice();
            }
        });

        sheetDialog.show();
    }

    @SuppressLint("MissingPermission")
    private void makePhoneCall(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber)));
        } else {
            Toast.makeText(MainActivity.this, "Call Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchPhoneNumbersMed() {
        mDatabase = FirebaseDatabase.getInstance().getReference("paramedics");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                paraLabels.clear();
                paraNumbers.clear();

                // Retrieve each phone number with label
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String label = snapshot.child("label").getValue(String.class);
                        String number = snapshot.child("phoneNumber").getValue(String.class);

                        if (label != null && number != null) {
                            paraLabels.add(label + ": " + number);
                            paraNumbers.add(number);
                        }
                    }

                    if (paraLabels.isEmpty()) {
                        paraLabels.add("No contacts");
                        paraNumbers.add(null); // Mark as non-clickable
                    }
                } else {
                    paraLabels.add("No contacts");
                    paraNumbers.add(null); // Mark as non-clickable
                }

                // After fetching the phone numbers, show the dialog
                showDialogsMed();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to fetch phone numbers.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialogsMed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Call Paramedics");

        final CharSequence[] labelsArray = paraLabels.toArray(new CharSequence[0]);

        builder.setItems(labelsArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (paraNumbers.get(which) == null) {
                    return; // Prevent clicking "No contacts"
                }

                String selectedNumber = paraNumbers.get(which);
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + selectedNumber));
                MainActivity.this.startActivity(intent);
            }
        });

        builder.show();
    }

// ------------------------------------------------------------

    private void fetchPhoneNumbersFire() {
        mDatabase = FirebaseDatabase.getInstance().getReference("fire_dept");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fireLabels.clear();
                fireNumbers.clear();

                // Retrieve each phone number with label
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String label = snapshot.child("label").getValue(String.class);
                        String number = snapshot.child("phoneNumber").getValue(String.class);

                        if (label != null && number != null) {
                            fireLabels.add(label + ": " + number);
                            fireNumbers.add(number);
                        }
                    }

                    if (fireLabels.isEmpty()) {
                        fireLabels.add("No contacts");
                        fireNumbers.add(null); // Mark as non-clickable
                    }
                } else {
                    fireLabels.add("No contacts");
                    fireNumbers.add(null); // Mark as non-clickable
                }

                // After fetching the phone numbers, show the dialog
                showDialogsFire();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to fetch phone numbers.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialogsFire() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Call Fire Department");

        final CharSequence[] labelsArray = fireLabels.toArray(new CharSequence[0]);

        builder.setItems(labelsArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (fireNumbers.get(which) == null) {
                    return; // Prevent clicking "No contacts"
                }

                String selectedNumber = fireNumbers.get(which);
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + selectedNumber));
                MainActivity.this.startActivity(intent);
            }
        });

        builder.show();
    }


    private void fetchPhoneNumbersPolice() {
        mDatabase = FirebaseDatabase.getInstance().getReference("police_dept");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                policeLabels.clear();
                policeNumbers.clear();

                // Retrieve each phone number with label
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String label = snapshot.child("label").getValue(String.class);
                        String number = snapshot.child("phoneNumber").getValue(String.class);

                        if (label != null && number != null) {
                            policeLabels.add(label + ": " + number);
                            policeNumbers.add(number);
                        }
                    }

                    if (policeLabels.isEmpty()) {
                        policeLabels.add("No contacts");
                        policeNumbers.add(null); // Use null to mark non-clickable items
                    }
                } else {
                    policeLabels.add("No contacts");
                    policeNumbers.add(null); // Use null to mark non-clickable items
                }

                // Show the dialog after fetching contacts
                showDialogsPolice();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to fetch phone numbers.", Toast.LENGTH_SHORT).show();
                Log.e("FirebaseError", "Database error: " + databaseError.getMessage());
            }
        });
    }

    private void showDialogsPolice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Call Police");

        // Convert labels ArrayList to an array
        final CharSequence[] labelsArray = policeLabels.toArray(new CharSequence[0]);

        builder.setItems(labelsArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Prevent clicking "No contacts"
                if (policeNumbers.get(which) == null) {
                    return; // Do nothing if "No contacts" is clicked
                }

                // Proceed to dial the selected phone number
                String selectedNumber = policeNumbers.get(which);
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + selectedNumber));
                MainActivity.this.startActivity(intent);
            }
        });

        builder.show();
    }





    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void showInternetPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again.")
                .setCancelable(false)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if (isNetworkConnected()) {
                            dialogInterface.dismiss();
                        } else {
                            showInternetPrompt();
                        }
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        finish();
                    }
                })
                .show();
    }
}
