package app.pokdakan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;

import static app.pokdakan.ConstantValues.my_shared_preferences;
import static app.pokdakan.ConstantValues.session_status;

public class ResetActivity extends AppCompatActivity {

    EditText ed_email;
    Button resetBtn;
    ImageView back;
    ProgressDialog pd;
    SharedPreferences sharedpreferences;
    Boolean session = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);

        ed_email = findViewById(R.id.ed_email);
        resetBtn = findViewById(R.id.resetBtn);
        back = findViewById(R.id.back);

        AndroidNetworking.initialize(getApplicationContext());
        pd=new ProgressDialog(this);
        pd.setCancelable(false);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateReset()){
                    prosesReset();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void prosesReset(){
        pd.setMessage("Loading ...");
        pd.show();
        Toast.makeText(getApplicationContext(),"Check Your Email", Toast.LENGTH_SHORT).show();
    }

    private boolean validateReset() {
        if (ed_email.getText().toString().trim().equals("")) {
            ed_email.setError("Wajib Diisi");
            return false;
        }
        else if (!isValidEmail(ed_email.getText().toString().trim())) {
            ed_email.setError("Email Tidak Valid");
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
