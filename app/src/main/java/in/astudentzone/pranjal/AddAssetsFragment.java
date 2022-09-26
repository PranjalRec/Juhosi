package in.astudentzone.pranjal;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddAssetsFragment extends Fragment {

    Spinner spinner;
    EditText editTextPolicyNumber;
    Button buttonNext;
    ArrayList<String> assetsType = new ArrayList<>();
    String selectedAsset, policyNumber, uid;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference, referenceAssets;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user;
    ArrayAdapter arrayAdapter;

    ArrayList<String> assetsList = new ArrayList<>();

    boolean isPolicyNumberPresent = false;

    public static Fragment newInstance (){
        return new AddAssetsFragment();
    }

    public AddAssetsFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        user = auth.getCurrentUser();
        if(user!=null){
            uid = user.getUid();
            reference = database.getReference().child("users").child(uid).child("assets");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dsp : snapshot.getChildren()){
                        assetsList.add(dsp.getKey());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_assets, container, false);

        referenceAssets = database.getReference().child("assetTypes");

        referenceAssets.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                assetsType.clear();
                for(DataSnapshot assets : snapshot.getChildren()){
                    assetsType.add(assets.getValue().toString());
                }

                arrayAdapter = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,
                        assetsType);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        spinner = view.findViewById(R.id.spinnerAssets);
        editTextPolicyNumber = view.findViewById(R.id.editTextPolicyNumber);
        buttonNext = view.findViewById(R.id.buttonNextAddAssets);




        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAsset = assetsType.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPolicyNumberPresent = false;
                policyNumber = editTextPolicyNumber.getText().toString().trim();
                if(policyNumber.equals("") || selectedAsset.equals("")){
                    Toast.makeText(requireContext(), "Fill all the details", Toast.LENGTH_SHORT).show();
                }
                else{
                    for(String s : assetsList){
                        if(s.equals(policyNumber)){
                            isPolicyNumberPresent = true;
                            break;
                        }

                    }

                    if(isPolicyNumberPresent == true){
                        Toast.makeText(requireContext(), "Policy number already exists", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Bundle bundle = new Bundle();
                        bundle.putString("selectedAsset", selectedAsset);
                        bundle.putString("policyNumber", policyNumber);
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        AssetDetailDialogFragment assetDetailDialogFragment = new AssetDetailDialogFragment();
                        assetDetailDialogFragment.setArguments(bundle);
                        fragmentTransaction.commit();
                        assetDetailDialogFragment.show(fragmentManager,"dialog fragment");
                        spinner.setSelection(0);
                        editTextPolicyNumber.setText("");
                    }

                }
            }
        });



        return view;
    }


}