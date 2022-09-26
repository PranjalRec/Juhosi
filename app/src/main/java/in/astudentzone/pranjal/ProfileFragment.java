package in.astudentzone.pranjal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    Spinner spinnerMaritalStatus;
    TextView textViewName, textViewPhone, textViewMaritalStatus, textViewEmail, textViewGender;
    EditText editTextAddress, editTextPincode, editTextAnnualIncome;
    Button buttonChangeData;
    ScrollView scrollView;
    ProgressBar progressBar;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user;

    String uid;
    String name, phone, annualIncome, address, pincode, gender, email, maritalStatus, maritalStatusChanged,
            addressChanged,pincodeChanged, annualIncomeChanged;
    String[] maritalStatusItems = new String[]{"Married", "Unmarried"};

    public ProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        textViewGender = view.findViewById(R.id.textViewGenderProfile);
        spinnerMaritalStatus = view.findViewById(R.id.spinnerMaritalStatusProfile);
        textViewName = view.findViewById(R.id.textViewNameProfile);
        textViewPhone = view.findViewById(R.id.textViewPhoneProfile);
        editTextAnnualIncome = view.findViewById(R.id.editTextAnnualIncomeProfile);
        textViewMaritalStatus = view.findViewById(R.id.textViewMaritalStatusProfile);
        textViewEmail = view.findViewById(R.id.textViewEmailProfile);
        editTextAddress = view.findViewById(R.id.editTextAddressProfile);
        editTextPincode = view.findViewById(R.id.editTextPincodeProfile);
        buttonChangeData = view.findViewById(R.id.buttonChangeDataProfile);
        scrollView = view.findViewById(R.id.scrollViewProfile);
        progressBar = view.findViewById(R.id.progressBarProfile);

        ArrayAdapter<String> maritalStatusAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item,
                maritalStatusItems);
        spinnerMaritalStatus.setAdapter(maritalStatusAdapter);

        spinnerMaritalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maritalStatusChanged = maritalStatusItems[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        user = auth.getCurrentUser();

        if(user != null){
            uid = user.getUid();
            reference = database.getReference().child("users").child(uid).child("details");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        scrollView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        name = snapshot.child("name").getValue().toString();
                        phone = snapshot.child("phone").getValue().toString();
                        email = snapshot.child("email").getValue().toString();
                        annualIncome = snapshot.child("annual income").getValue().toString();
                        address = snapshot.child("address").getValue().toString();
                        pincode = snapshot.child("pincode").getValue().toString();
                        gender = snapshot.child("gender").getValue().toString();
                        maritalStatus = snapshot.child("marital status").getValue().toString();

                        textViewName.setText(name);
                        textViewPhone.setText(phone);
                        textViewEmail.setText(email);
                        editTextAnnualIncome.setText(annualIncome);
                        editTextAddress.setText(address);
                        editTextPincode.setText(pincode);
                        textViewGender.setText(gender);
                        textViewMaritalStatus.setText(maritalStatus);


                        buttonChangeData.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addressChanged = editTextAddress.getText().toString().trim();
                                pincodeChanged = editTextPincode.getText().toString().trim();
                                annualIncomeChanged = editTextAnnualIncome.getText().toString().trim();

                                if(addressChanged.equals("") || pincodeChanged.equals("") || annualIncomeChanged.equals("")){
                                    Toast.makeText(requireContext(), "Fill details correctly", Toast.LENGTH_SHORT).show();
                                }else{
                                    buttonChangeData.setClickable(false);
                                    reference.child("address").setValue(addressChanged);
                                    reference.child("pincode").setValue(pincodeChanged);
                                    reference.child("annual income").setValue(annualIncomeChanged);
                                    reference.child("marital status").setValue(maritalStatusChanged);

                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(requireContext(), "Login first to see profile", Toast.LENGTH_SHORT).show();
        }



        return view;
    }
}