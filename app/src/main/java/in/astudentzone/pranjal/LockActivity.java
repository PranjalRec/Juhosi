package in.astudentzone.pranjal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hanks.passcodeview.PasscodeView;

import org.w3c.dom.Text;

public class LockActivity extends AppCompatActivity {

    PasscodeView passcodeView;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user;
    String pin;
    TextView textViewForgotPin;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        passcodeView = findViewById(R.id.passcode_view);
        textViewForgotPin = findViewById(R.id.textViewForgotPin);

        user = auth.getCurrentUser();

    }

    void loginWithPin(String pin){
        passcodeView.setLocalPasscode(pin);
        passcodeView.setPasscodeLength(4)
                .setListener(new PasscodeView.PasscodeViewListener() {
                    @Override
                    public void onFail() {
                        Toast.makeText(LockActivity.this, "Password is wrong", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String number) {
                        Intent i = new Intent(LockActivity.this, UserDataActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
    }

    @Override
    protected void onStart() {
        if(user != null){
            sharedPreferences = getSharedPreferences("savePin",MODE_PRIVATE);
            pin = sharedPreferences.getString("pin","");
            if(pin.equals("")){
                Intent intent1 = new Intent(LockActivity.this,UserDataActivity.class);
                startActivity(intent1);
                finish();
                super.onStart();
            }else{

                loginWithPin(pin);
                textViewForgotPin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        auth.signOut();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("pin","");
                        editor.commit();
                        Intent i = new Intent(LockActivity.this, SignInActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                super.onStart();
            }
        }
        else{
            Intent intent = new Intent(LockActivity.this,SignInActivity.class);
            startActivity(intent);
            finish();
            super.onStart();
        }

    }
}