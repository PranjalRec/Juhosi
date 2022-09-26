package in.astudentzone.pranjal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDataActivity extends AppCompatActivity {

    Spinner spinnerGender,spinnerMaritalStatus;
    EditText editTextName, editTextPhone, editTextAnnualIncome, editTextAddress, editTextPincode;
    Button buttonSubmitUserData;
    String[] genderItems = new String[]{"Male", "Female", "Other"};
    String[] maritalStatusItems = new String[]{"Married", "Unmarried"};
    String name, phone, annualIncome, address, pincode, gender, email, maritalStatus, uid;
    ScrollView scrollView;
    ProgressBar progressBar;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        spinnerGender = findViewById(R.id.spinnerGender);
        spinnerMaritalStatus = findViewById(R.id.spinnerMaritalStatus);
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextAnnualIncome = findViewById(R.id.editTextAnnualIncome);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextPincode = findViewById(R.id.editTextPincode);
        buttonSubmitUserData = findViewById(R.id.buttonSubmitUserData);
        scrollView = findViewById(R.id.scrollViewUserData);
        progressBar = findViewById(R.id.progressBarUserData);

        user = auth.getCurrentUser();
        uid = user.getUid();
        email = user.getEmail();

        reference = database.getReference().child("users").child(uid).child("details");

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                genderItems);
        ArrayAdapter<String> maritalStatusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                maritalStatusItems);

        spinnerGender.setAdapter(genderAdapter);
        spinnerMaritalStatus.setAdapter(maritalStatusAdapter);


        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = genderItems[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerMaritalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maritalStatus = maritalStatusItems[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonSubmitUserData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editTextName.getText().toString().trim();
                phone = editTextPhone.getText().toString().trim();
                annualIncome = editTextAnnualIncome.getText().toString().trim();
                address = editTextAddress.getText().toString().trim();
                pincode = editTextPincode.getText().toString().trim();
                saveDataToFirebase();

            }
        });

    }

    void saveDataToFirebase(){
        reference.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){

                    if(name.equals("") || phone.equals("") || annualIncome.equals("") || address.equals("") || pincode.equals("")){
                        Toast.makeText(UserDataActivity.this, "Fill all the details carefully", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(UserDataActivity.this, "Your data saved successfully.", Toast.LENGTH_SHORT).show();
                        reference.child("name").setValue(name);
                        reference.child("phone").setValue(phone);
                        reference.child("email").setValue(email);
                        reference.child("annual income").setValue(annualIncome);
                        reference.child("address").setValue(address);
                        reference.child("pincode").setValue(pincode);
                        reference.child("marital status").setValue(maritalStatus);
                        reference.child("gender").setValue(gender);

                        Intent i = new Intent(UserDataActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserDataActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        user = auth.getCurrentUser();
        uid = user.getUid();
        email = user.getEmail();
        reference = database.getReference().child("users").child(uid).child("details");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Intent i = new Intent(UserDataActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    scrollView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        super.onStart();
    }
}