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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static app.pokdakan.ConstantValues.TAG_ALAMAT;
import static app.pokdakan.ConstantValues.TAG_EMAIL;
import static app.pokdakan.ConstantValues.TAG_ID;
import static app.pokdakan.ConstantValues.TAG_NAMA;
import static app.pokdakan.ConstantValues.TAG_PASSWORD;
import static app.pokdakan.ConstantValues.TAG_ROLE;
import static app.pokdakan.ConstantValues.TAG_TELEPON;
import static app.pokdakan.ConstantValues.my_shared_preferences;
import static app.pokdakan.ConstantValues.session_status;

public class LoginActivity extends AppCompatActivity {

    EditText ed_email, ed_password;
    Button loginBtn;
    ProgressDialog pd;
    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id, email, password;
    TextView daftar, lupapassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        email = sharedpreferences.getString(TAG_EMAIL, null);
        password = sharedpreferences.getString(TAG_PASSWORD, null);


        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        loginBtn = findViewById(R.id.loginBtn);
        daftar = findViewById(R.id.daftar);
        lupapassword = findViewById(R.id.lupapassword);

        AndroidNetworking.initialize(getApplicationContext());
        pd=new ProgressDialog(this);
        pd.setCancelable(false);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateLogin()){
                    proseslogin();
                }
            }
        });
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        lupapassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ResetActivity.class);
                startActivity(intent);
            }
        });
    }

    private void proseslogin(){
        pd.setMessage("Loading ...");
        pd.show();
        AndroidNetworking.post(BASE.URL+"login")
                .addBodyParameter("email", ed_email.getText().toString().trim())
                .addBodyParameter("password", ed_password.getText().toString().trim())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.cancel();
                        try {
                            if(response.getString("status").equals("200")) {
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putBoolean(session_status, true);
                                editor.putString(TAG_ID, response.getString("id"));
                                editor.putString(TAG_NAMA, response.getString("name"));
                                editor.putString(TAG_EMAIL, response.getString("email"));
                                editor.putString(TAG_ALAMAT, response.getString("alamat"));
                                editor.putString(TAG_TELEPON, response.getString("telepon"));
                                editor.putString(TAG_ROLE, response.getString("role"));
                                editor.commit();

                                Toast.makeText(getApplicationContext(), response.getString("pesan")+" "+response.getString("role"), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),response.getString("pesan"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.cancel();
                        Toast.makeText(getApplicationContext(),anError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateLogin() {
        if (ed_email.getText().toString().trim().equals("")) {
            ed_email.setError("Wajib Diisi");
            return false;
        }
        else if (!isValidEmail(ed_email.getText().toString().trim())) {
            ed_email.setError("Email Tidak Valid");
            return false;
        }
        else if (ed_password.getText().toString().trim().equals("")) {
            ed_password.setError("Wajib Diisi");
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {

        String regExpn = "^[a-z0-9_$@.!%*?&]{6,24}$";
        CharSequence inputStr = password;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }
}
