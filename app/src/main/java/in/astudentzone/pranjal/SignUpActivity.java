package in.astudentzone.pranjal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpActivity extends AppCompatActivity {

    ImageView imageViewSignUp;
    EditText editTextEmail,editTextPassword;
    Button buttonSignUp;
    TextView textViewAlreadyAc;
    String email,password;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        imageViewSignUp = findViewById(R.id.imageViewSignUp);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewAlreadyAc = findViewById(R.id.textViewAlredyAccount);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);


        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString().trim();
                password = editTextPassword.getText().toString().trim();

                if(email.matches("") && password.matches("")){

                    Toast.makeText(SignUpActivity.this, "Enter correct email and password",
                            Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    signUpWithEmailPassword();
                }

            }
        });

        textViewAlreadyAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(i);
                finish();
            }
        });


    }


    public void signUpWithEmailPassword(){

            auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        buttonSignUp.setClickable(false);
                        user = auth.getCurrentUser();

                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(SignUpActivity.this, "Email sent successfully",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignUpActivity.this, "Something went wrong",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                        Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                        startActivity(intent);
                        finish();

                    }
                    else{
                        Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

}