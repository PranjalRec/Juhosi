package in.astudentzone.pranjal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {

    EditText editTextEmail;
    Button buttonReset;
    String mail;

    FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        editTextEmail = findViewById(R.id.editTextEmailForgot);
        buttonReset = findViewById(R.id.buttonSignReset);

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail = editTextEmail.getText().toString().trim();
                if(mail.matches("")){

                    Toast.makeText(ForgotActivity.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                }
                else {
                    auth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ForgotActivity.this, "Password reset email sent successfully",
                                        Toast.LENGTH_SHORT).show();

                            }
                            else{
                                Toast.makeText(ForgotActivity.this, task.getException().getLocalizedMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}