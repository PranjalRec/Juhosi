package in.astudentzone.pranjal;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AssetDetailDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    EditText editTextAccountNo, editTextCertificateNo, editTextPolicyStartDate, editTextPolicyEndDate, editTextSumAssured;
    Button buttonCancel, buttonSubmit;
    String accountNo, certificateNo, policyStartDate, policyEndDate, sumAssured, uid, selectedAsset,policyNumber, lifeInsCompanyName;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user;
    TextView textViewPolicyStartDate;
    String selectedDate;

    Spinner spinnerLifeInsCompanyName;

    ArrayList<String> lifeInsCompanyNames = new ArrayList<>();

    public AssetDetailDialogFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_asset_detail_dialog, container, false);
        editTextAccountNo = view.findViewById(R.id.editTextAccountNumber);
        editTextCertificateNo = view.findViewById(R.id.editTextCertificateNumber);
        textViewPolicyStartDate = view.findViewById(R.id.TextViewPolicyStartDate);
        editTextPolicyEndDate = view.findViewById(R.id.editTextPolicyEndDate);
        editTextSumAssured = view.findViewById(R.id.editTextSumAssured);
        buttonCancel = view.findViewById(R.id.buttonCancelAssetDialog);
        buttonSubmit = view.findViewById(R.id.buttonSubmitAssetDialog);
        spinnerLifeInsCompanyName = view.findViewById(R.id.spinnerLifeInsuranceCompanyName);

        lifeInsCompanyNames.add("Life Insurance Corporation of India");
        lifeInsCompanyNames.add("Aditya Birla Sun Life Insurance");
        lifeInsCompanyNames.add("Aditya Birla Health Insurance");
        lifeInsCompanyNames.add("Aegon Life Insurance");
        lifeInsCompanyNames.add("Ageas Federal Life Insurance");
        lifeInsCompanyNames.add("Aviva Life");
        lifeInsCompanyNames.add("Bajaj Allianz Life Insurance");
        lifeInsCompanyNames.add("Bajaj Allianz General Insurance");
        lifeInsCompanyNames.add("Bharti AXA Life insurance");
        lifeInsCompanyNames.add("Canara HSBC OBC Life Insurance");
        lifeInsCompanyNames.add("care Health Insurance");
        lifeInsCompanyNames.add("Cholamandalam MS General Insurance");
        lifeInsCompanyNames.add("Digit Insurance");
        lifeInsCompanyNames.add("Edelweiss Tokio Life Insurance");
        lifeInsCompanyNames.add("Exide Life Insurance");
        lifeInsCompanyNames.add("Future Generali India Life Insurance Company Limited");
        lifeInsCompanyNames.add("HDFC Life Insurance");
        lifeInsCompanyNames.add("ICICI Lombard General Insurance (Motor)");
        lifeInsCompanyNames.add("ICICI Lombard General Insurance (Health)");
        lifeInsCompanyNames.add("ICICI Predential Life insurance");
        lifeInsCompanyNames.add("IFFCO Tokio General Insurance");
        lifeInsCompanyNames.add("IndiaFirst Life Insurance");
        lifeInsCompanyNames.add("Kotak Life Insurance");
        lifeInsCompanyNames.add("Magma HDI Motor Insurance");
        lifeInsCompanyNames.add("Manipal Cigna Health Insurance Company Limited");
        lifeInsCompanyNames.add("Niva Bupa Health Insurance Company Limited");
        lifeInsCompanyNames.add("Max Life Insurance");
        lifeInsCompanyNames.add("Oriental General Insurance");
        lifeInsCompanyNames.add("PNB MetLife Insurance");
        lifeInsCompanyNames.add("Pramerica Life Insurance");
        lifeInsCompanyNames.add("Reliance Nippon Life Insurance");
        lifeInsCompanyNames.add("Royal Sundaram General Insurance Co. Limited");
        lifeInsCompanyNames.add("SBI Life Insurance");
        lifeInsCompanyNames.add("Shriram General Insurance - Quote Payment");
        lifeInsCompanyNames.add("Shriram General Insurance");
        lifeInsCompanyNames.add("Shriram General Insurance Co Ltd");
        lifeInsCompanyNames.add("Star Union Dai Ichi Life Insurance");
        lifeInsCompanyNames.add("Tata AIA Life Insurance");
        lifeInsCompanyNames.add("The Oriental Insurance Company Limited");

        Bundle bundle = getArguments();
        selectedAsset = bundle.getString("selectedAsset");
        policyNumber = bundle.getString("policyNumber");

        if(selectedAsset.equals("Life Insurance") || selectedAsset.equals("Term Insurance")){
            spinnerLifeInsCompanyName.setVisibility(View.VISIBLE);
            ArrayAdapter arrayAdapter = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,
                    lifeInsCompanyNames);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerLifeInsCompanyName.setAdapter(arrayAdapter);

        }



        textViewPolicyStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker datePicker = new DatePicker();
                datePicker.show(getActivity().getSupportFragmentManager(),"Select Date");
            }
        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        spinnerLifeInsCompanyName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lifeInsCompanyName = lifeInsCompanyNames.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountNo = editTextAccountNo.getText().toString().trim();
                certificateNo = editTextCertificateNo.getText().toString().trim();
                policyStartDate = editTextPolicyStartDate.getText().toString().trim();
                policyEndDate = editTextPolicyEndDate.getText().toString().trim();
                sumAssured = editTextSumAssured.getText().toString().trim();






                if(accountNo.equals("") || certificateNo.equals("") || policyStartDate.equals("")
                        || policyEndDate.equals("") || sumAssured.equals("") || policyNumber==null || selectedAsset == null){
                    Toast.makeText(requireContext(), "Enter all the details carefully", Toast.LENGTH_SHORT).show();
                }
                else{


                    buttonSubmit.setClickable(false);
                    user = auth.getCurrentUser();
                    uid = user.getUid();
                    reference = database.getReference().child("users").child(uid).child("assets")
                            .child(policyNumber);

                    reference.child("account number").setValue(accountNo);
                    reference.child("certificate number").setValue(certificateNo);
                    reference.child("policy start date").setValue(policyStartDate);
                    reference.child("policy end date").setValue(policyEndDate);
                    reference.child("sum assured").setValue(sumAssured);
                    reference.child("selected asset").setValue(selectedAsset);
                    reference.child("company name").setValue(lifeInsCompanyName);
                    Toast.makeText(requireContext(), "Details saved successfully", Toast.LENGTH_SHORT).show();
                    getDialog().dismiss();
                }
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,
                android.R.style.ThemeOverlay_Material_ActionBar);
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        selectedDate = dayOfMonth + "/" + month + "/" + year;
        Toast.makeText(requireContext(), selectedDate, Toast.LENGTH_SHORT).show();
        textViewPolicyStartDate.setText(selectedDate);
    }
}