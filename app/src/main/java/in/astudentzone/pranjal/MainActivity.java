package in.astudentzone.pranjal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    String name,phone,uid;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = auth.getCurrentUser();



        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        frameLayout = findViewById(R.id.frameLayout);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new
                ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.bringToFront();
        updateUserDataHeader();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
                        break;

                    case R.id.nav_profile:
                        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);
                        break;

                    case R.id.nav_assets:
                        bottomNavigationView.setSelectedItemId(R.id.bottom_assets);
                        break;

                    case R.id.nav_logout:
                        auth.signOut();
                        sharedPreferences = getSharedPreferences("savePin",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("pin","");
                        editor.commit();
                        Intent i = new Intent(MainActivity.this, SignInActivity.class);
                        startActivity(i);
                        finish();
                        break;

                    case R.id.nav_set_pin:
                        Intent intent = new Intent(MainActivity.this, PinSetActivity.class);
                        startActivity(intent);
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.bottom_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new HomeFragment()).commit();
                        return true;

                    case R.id.bottom_assets:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new AssetsFragment()).commit();
                        return true;

                    case R.id.bottom_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new ProfileFragment()).commit();
                        return true;
                }
                return false;
            }
        });



    }


    void updateUserDataHeader(){
        View navigationHeader = navigationView.getHeaderView(0);
        TextView textViewEmailNotVerified = navigationHeader.findViewById(R.id.textViewEmailNotVerified);
        TextView textViewName = navigationHeader.findViewById(R.id.textViewNameHeader);
        TextView textViewEmail = navigationHeader.findViewById(R.id.textViewEmailHeader);
        ProgressBar progressBar = navigationHeader.findViewById(R.id.progressBarHeader);

        if(user!=null){
            uid = user.getUid();

            if(! user.isEmailVerified()){
                textViewEmailNotVerified.setVisibility(View.VISIBLE);
                textViewEmailNotVerified.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MainActivity.this, "Email sent successfully, after verification please login again",
                                        Toast.LENGTH_LONG).show();
                                auth.signOut();
                                Intent i = new Intent(MainActivity.this, SignInActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Something went wrong",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){
                        progressBar.setVisibility(View.INVISIBLE);

                        String mail = snapshot.child("users").child(uid).child("details").child("email").getValue().toString();
                        String name = snapshot.child("users").child(uid).child("details").child("name").getValue().toString();
                        textViewName.setText(name);
                        textViewEmail.setText(mail);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}