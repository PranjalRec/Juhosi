package in.astudentzone.pranjal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    EditText editTextMailIn,editTextPasswordIn;
    TextView textViewForgot,textViewSignUp;
    Button buttonSignIn;
    String mail,password;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        buttonSignIn = findViewById(R.id.buttonSignIn);
        editTextMailIn = findViewById(R.id.editTextEmailIn);
        editTextPasswordIn = findViewById(R.id.editTextPasswordIn);
        textViewForgot = findViewById(R.id.textViewForgot);
        textViewSignUp = findViewById(R.id.textViewAlredyAccount);

        textViewForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, ForgotActivity.class);
                startActivity(i);
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mail = editTextMailIn.getText().toString().trim();
                password = editTextPasswordIn.getText().toString().trim();

                if(mail.matches("") && password.matches("")){
                    Toast.makeText(SignInActivity.this, "Enter your email and password first.",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    signInWithMailPassword();
                }

            }
        });

    }


    public void signInWithMailPassword(){
            auth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        Toast.makeText(SignInActivity.this, "Logged In successfully.",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignInActivity.this,UserDataActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(SignInActivity.this, task.getException().getLocalizedMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }
}