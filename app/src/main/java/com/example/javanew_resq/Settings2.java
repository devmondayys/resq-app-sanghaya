package com.example.javanew_resq;


import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.Manifest;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Settings2 extends AppCompatActivity {

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
    Button timeButton;
    Button timeButton2;
    private EditText aphoneNumberEditText, bphoneNumberEditText, cphoneNumberEditText;
    private Button aaddNumberButton, baddNumberButton, caddNumberButton;
    private EditText labelEditTextPara, labelEditTextFire, labelEditTextPolice;
    private ListView phoneNumbersListViewPara, phoneNumbersListViewFire, phoneNumbersListViewPolice;
    private ArrayList<PhoneNumber> paramedicsList, fireDeptList, policeDeptList;
    private PhoneNumberAdapter paramedicsAdapter, fireDeptAdapter, policeDeptAdapter;
    private DatabaseReference mDatabase;
    private LinearLayout timesContainer;
    int hour, minute;
    private ListView timesListView;
    private TimeAdapter timeAdapter;
    private List<TimeEntry> timeEntries;
    private TextView noTimeAlarmTextView;




    ActivityResultLauncher<String[]> mPermissionResultLauncher;
    private boolean isCallPermissionGranted = false;
    private boolean isLocationPermissionGranted = false;

    private int PARAMEDIC_LINE;

    private static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    private Bundle savedInstanceState;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
        aphoneNumberEditText = findViewById(R.id.aphoneNumberEditText);
        bphoneNumberEditText = findViewById(R.id.bphoneNumberEditText);
        cphoneNumberEditText = findViewById(R.id.cphoneNumberEditText);

        aaddNumberButton = findViewById(R.id.aaddNumberButton);
        baddNumberButton = findViewById(R.id.baddNumberButton);
        caddNumberButton = findViewById(R.id.caddNumberButton);



        aphoneNumberEditText = findViewById(R.id.aphoneNumberEditText);
        labelEditTextPara = findViewById(R.id.labelEditTextPara);
        aaddNumberButton = findViewById(R.id.aaddNumberButton);
        phoneNumbersListViewPara = findViewById(R.id.phoneNumbersListViewPara);

        bphoneNumberEditText = findViewById(R.id.bphoneNumberEditText);
        labelEditTextFire = findViewById(R.id.labelEditTextFire);
        baddNumberButton = findViewById(R.id.baddNumberButton);
        phoneNumbersListViewFire = findViewById(R.id.phoneNumbersListViewFire);

        cphoneNumberEditText = findViewById(R.id.cphoneNumberEditText);
        labelEditTextPolice = findViewById(R.id.labelEditTextPolice);
        caddNumberButton = findViewById(R.id.caddNumberButton);
        phoneNumbersListViewPolice = findViewById(R.id.phoneNumbersListViewPolice);

        // Initialize lists and adapters
        paramedicsList = new ArrayList<>();
        fireDeptList = new ArrayList<>();
        policeDeptList = new ArrayList<>();

        paramedicsAdapter = new PhoneNumberAdapter(this, paramedicsList, "paramedics");
        fireDeptAdapter = new PhoneNumberAdapter(this, fireDeptList, "fire_dept");
        policeDeptAdapter = new PhoneNumberAdapter(this, policeDeptList, "police_dept");

        phoneNumbersListViewPara.setAdapter(paramedicsAdapter);
        phoneNumbersListViewFire.setAdapter(fireDeptAdapter);
        phoneNumbersListViewPolice.setAdapter(policeDeptAdapter);

        // Set onClickListeners for buttons
        aaddNumberButton.setOnClickListener(v -> addPhoneNumber("paramedics", aphoneNumberEditText, labelEditTextPara));
        baddNumberButton.setOnClickListener(v -> addPhoneNumber("fire_dept", bphoneNumberEditText, labelEditTextFire));
        caddNumberButton.setOnClickListener(v -> addPhoneNumber("police_dept", cphoneNumberEditText, labelEditTextPolice));
        TextView noParamedicsTextView = findViewById(R.id.noParamedicsTextView);
        TextView noFireDeptTextView = findViewById(R.id.noFireDeptTextView);
        TextView noPoliceDeptTextView = findViewById(R.id.noPoliceDeptTextView);
        // Retrieve phone numbers from Firebase
        retrievePhoneNumbers("paramedics", paramedicsList, paramedicsAdapter, noParamedicsTextView);
        retrievePhoneNumbers("fire_dept", fireDeptList, fireDeptAdapter, noFireDeptTextView);
        retrievePhoneNumbers("police_dept", policeDeptList, policeDeptAdapter, noPoliceDeptTextView);



        mDatabase = FirebaseDatabase.getInstance().getReference();
        timesListView = findViewById(R.id.times_list_view);

        // Initialize timeEntries before using it
        timeEntries = new ArrayList<>();

        // Initialize the adapter with the non-null list
        timeAdapter = new TimeAdapter((Context) this, timeEntries);
        timesListView.setAdapter(timeAdapter);
        noTimeAlarmTextView = findViewById(R.id.no_time_alarm_text_view);

        Button timeButton2 = findViewById(R.id.timeButton2);
        timeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        retrieveAndDisplayTimes();

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
                    drawerLayout.close();
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

//        setbutton3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (timeButton2.getVisibility() == View.GONE) {
//                    timeButton2.setVisibility(View.VISIBLE);
//                    setbutton3.setImageResource(R.drawable.ic_up1);
//                } else {
//                    timeButton2.setVisibility(View.GONE);
//                    setbutton3.setImageResource(R.drawable.ic_down1);
//                }
//            }
//        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                auth.signOut();
                Intent intent = new Intent(Settings2.this, Login.class);
                startActivity(intent);
                finish();
                Toast.makeText(Settings2.this, "Logout Successful", Toast.LENGTH_SHORT).show();
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

        String email = user.getUid();


        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

    }

    private void requestPermission() {

        isCallPermissionGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED;

        isLocationPermissionGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        List<String> permissionRequest = new ArrayList<String>();

        if (!isCallPermissionGranted) {

            permissionRequest.add(Manifest.permission.CALL_PHONE);
        }
        if (!isLocationPermissionGranted) {

            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!permissionRequest.isEmpty()) {

            mPermissionResultLauncher.launch(permissionRequest.toArray(new String[0]));
        }


        // Retrieve phone numbers from database
    }

    private void showTimePickerDialog() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        mDatabase = FirebaseDatabase.getInstance().getReference("alarm");
        TimePickerDialog mTimePicker2 = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String key = mDatabase.child("times").push().getKey();
                TimeEntry timeEntry = new TimeEntry(selectedHour, selectedMinute, key);
                if (key != null) {
                    mDatabase.child("times").child(key).setValue(timeEntry);
                }
            }
        }, hour, minute, true);
        mTimePicker2.setTitle("Select Time");
        mTimePicker2.show();
    }

    private void retrieveAndDisplayTimes() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("alarm/times").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                timeEntries.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TimeEntry timeEntry = snapshot.getValue(TimeEntry.class);
                    if (timeEntry != null) {
                        timeEntries.add(timeEntry);
                    }
                }

                // Show or hide the "No time alarm added" message
                if (timeEntries.isEmpty()) {
                    noTimeAlarmTextView.setVisibility(View.VISIBLE);
                } else {
                    noTimeAlarmTextView.setVisibility(View.GONE);
                }

                timeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }




    private void addPhoneNumber(String node, EditText phoneNumberEditText, EditText labelEditText) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String label = labelEditText.getText().toString().trim();
        if (!phoneNumber.isEmpty() && !label.isEmpty()) {
            mDatabase = FirebaseDatabase.getInstance().getReference(node);
            String key = mDatabase.push().getKey();
            if (key != null) {
                PhoneNumber phoneNumberObject = new PhoneNumber(phoneNumber, label);
                mDatabase.child(key).setValue(phoneNumberObject).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Settings2.this, "Phone number added successfully", Toast.LENGTH_SHORT).show();
                        phoneNumberEditText.setText(""); // Clear the input field
                        labelEditText.setText(""); // Clear the label field
                    } else {
                        Toast.makeText(Settings2.this, "Failed to add phone number", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Toast.makeText(Settings2.this, "Please enter both phone number and label", Toast.LENGTH_SHORT).show();
        }
    }

    private void retrievePhoneNumbers(String node, ArrayList<PhoneNumber> numbersList, PhoneNumberAdapter adapter, TextView noContactsTextView) {
        mDatabase = FirebaseDatabase.getInstance().getReference(node);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                numbersList.clear();

                // Populate the numbersList
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PhoneNumber phoneNumber = snapshot.getValue(PhoneNumber.class);
                    if (phoneNumber != null) {
                        numbersList.add(phoneNumber);
                    }
                }

                // Check if the list is empty and show a message if it is
                if (numbersList.isEmpty()) {
                    noContactsTextView.setVisibility(View.VISIBLE);
                    noContactsTextView.setText("No contacts available");
                } else {
                    noContactsTextView.setVisibility(View.GONE);
                }

                // Notify the adapter of data changes
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Settings2.this, "Failed to retrieve phone numbers", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }


    private void deletePhoneNumberFromDatabase(String node, String phoneNumber) {
        mDatabase = FirebaseDatabase.getInstance().getReference(node);
        mDatabase.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Settings2.this, "Phone number deleted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Settings2.this, "Failed to delete phone number", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Settings2.this, "Failed to delete phone number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static class PhoneNumber {
        private String phoneNumber;
        private String label;

        public PhoneNumber() {
            // Default constructor required for calls to DataSnapshot.getValue(PhoneNumber.class)
        }

        public PhoneNumber(String phoneNumber, String label) {
            this.phoneNumber = phoneNumber;
            this.label = label;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }



    private class PhoneNumberAdapter extends ArrayAdapter<PhoneNumber> {
        private final Context context;
        private final ArrayList<PhoneNumber> values;
        private final String category;

        public PhoneNumberAdapter(@NonNull Context context, ArrayList<PhoneNumber> values, String category) {
            super(context, R.layout.phone_number_item, values);
            this.context = context;
            this.values = values;
            this.category = category;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.phone_number_item, parent, false);
            }

            TextView phoneNumberTextView = convertView.findViewById(R.id.phoneNumberTextView);
            TextView labelTextView = convertView.findViewById(R.id.labelTextView);
            ImageView deleteButton = convertView.findViewById(R.id.deleteButton);

            PhoneNumber phoneNumber = values.get(position);
            phoneNumberTextView.setText(phoneNumber.getPhoneNumber());
            labelTextView.setText(phoneNumber.getLabel()); // You can set label here if needed

            deleteButton.setOnClickListener(v -> deletePhoneNumberFromDatabase(category, phoneNumber.getPhoneNumber()));

            return convertView;
        }

    }
    public static class TimeEntry {
        private int hour;
        private int minute;
        private String key;

        // No-argument constructor required for Firebase
        public TimeEntry() {
        }

        public TimeEntry(int hour, int minute, String key) {
            this.hour = hour;
            this.minute = minute;
            this.key = key;
        }

        // Getters and setters
        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

}
