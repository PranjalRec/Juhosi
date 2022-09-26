package in.astudentzone.pranjal;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyAssetsFragment extends Fragment {

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    ArrayList<MyAssetsModel> myAssetsModels = new ArrayList<>();
    String uid, policyName, policyNumber, premium = "123123", dueDate;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user;

    public static Fragment newInstance (){
        return new MyAssetsFragment();
    }


    public MyAssetsFragment() {

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_assets, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewMyAssets);
        floatingActionButton = view.findViewById(R.id.floatingActionButtonAddNew);

        user = auth.getCurrentUser();

        if(user != null){
            uid = user.getUid();
            reference = database.getReference().child("users").child(uid).child("assets");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for(DataSnapshot dst: snapshot.getChildren()){
                            if(dst.exists()){

                                Object policyNam = dst.child("selected asset").getValue();
                                Object dueDateO = dst.child("policy end date").getValue();

                                policyNumber = dst.getKey();
                                if(policyNam != null && dueDate != null){
                                    policyName = policyNam.toString();
                                    dueDate = dueDateO.toString();
                                }
//                                policyName = dst.child("selected asset").getValue().toString();
                                myAssetsModels.add(new MyAssetsModel(policyName,policyNumber,dueDate , premium));
                            }

                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                        MyAssetsAdapter myAssetsAdapter = new MyAssetsAdapter(myAssetsModels,requireContext());
                        recyclerView.setAdapter(myAssetsAdapter);
                        myAssetsAdapter.notifyDataSetChanged();
                        myAssetsModels = new ArrayList<>();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}