package in.astudentzone.pranjal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PinSetActivity extends AppCompatActivity {

    EditText editTextPin, editTextConfirmPin;
    Button buttonSave;
    String pin, confirmPin;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_set);

        editTextPin = findViewById(R.id.editTextPin);
        editTextConfirmPin = findViewById(R.id.editTextConfirmPin);
        buttonSave = findViewById(R.id.buttonSavePin);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin = editTextPin.getText().toString().trim();
                confirmPin = editTextConfirmPin.getText().toString().trim();

                if(pin.length() == 4){
                    if(pin.equals(confirmPin)){
                        sharedPreferences = getSharedPreferences("savePin",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("pin",pin);
                        editor.commit();
                        Toast.makeText(PinSetActivity.this, "Pin set Success", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(PinSetActivity.this, "Pin and Confirm Pin do not match", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(PinSetActivity.this, "Enter a 4 digit pin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}