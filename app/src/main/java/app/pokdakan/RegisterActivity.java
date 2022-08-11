package app.pokdakan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import static app.pokdakan.ConstantValues.TAG_EMAIL;
import static app.pokdakan.ConstantValues.TAG_ID;
import static app.pokdakan.ConstantValues.TAG_NAMA;
import static app.pokdakan.ConstantValues.TAG_PASSWORD;
import static app.pokdakan.ConstantValues.my_shared_preferences;
import static app.pokdakan.ConstantValues.session_status;

public class RegisterActivity extends AppCompatActivity {

    EditText ed_email, ed_password, ed_name, ed_password_confrim, ed_telepon, ed_alamat;
    Button daftarBtn;
    ProgressDialog pd;
    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id, email, password, name;
    TextView masuk;
    ImageView back;
    Spinner role;
    String s_role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        name = sharedpreferences.getString(TAG_NAMA, null);
        email = sharedpreferences.getString(TAG_EMAIL, null);
        password = sharedpreferences.getString(TAG_PASSWORD, null);


        ed_email = findViewById(R.id.ed_email);
        ed_alamat = findViewById(R.id.ed_alamat);
        ed_telepon = findViewById(R.id.ed_telepon);
        ed_password = findViewById(R.id.ed_password);
        daftarBtn = findViewById(R.id.daftarBtn);
        masuk = findViewById(R.id.masuk);
        ed_password_confrim = findViewById(R.id.ed_password_confrim);
        ed_name = findViewById(R.id.ed_name);
        back = findViewById(R.id.back);
        role = findViewById(R.id.role);

        AndroidNetworking.initialize(getApplicationContext());
        pd=new ProgressDialog(this);
        pd.setCancelable(false);

        s_role = role.getSelectedItem().toString();;
        role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                s_role = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        daftarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateRegis()){
                    prosesDaftar();
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

        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void prosesDaftar(){
        pd.setMessage("Loading ...");
        pd.show();
        AndroidNetworking.post(BASE.URL+"daftar")
                .addBodyParameter("name", ed_name.getText().toString().trim())
                .addBodyParameter("role", s_role)
                .addBodyParameter("email", ed_email.getText().toString().trim())
                .addBodyParameter("telepon", ed_telepon.getText().toString().trim())
                .addBodyParameter("alamat", ed_alamat.getText().toString().trim())
                .addBodyParameter("password", ed_password.getText().toString().trim())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.cancel();
                        try {
                            if(response.getString("status").equals("200")) {
                                Toast.makeText(getApplicationContext(), response.getString("pesan"), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
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

    private boolean validateRegis() {
        if (ed_name.getText().toString().trim().equals("")) {
            ed_name.setError("Wajib Diisi");
            return false;
        }
        else if (ed_email.getText().toString().trim().equals("")) {
            ed_email.setError("Wajib Diisi");
            return false;
        }
        else if (!isValidEmail(ed_email.getText().toString().trim())) {
            ed_email.setError("Email Tidak Valid");
            return false;
        }
        else if (ed_telepon.getText().toString().trim().equals("")) {
            ed_telepon.setError("Wajib Diisi");
            return false;
        }
        else if (ed_alamat.getText().toString().trim().equals("")) {
            ed_alamat.setError("Wajib Diisi");
            return false;
        }
        else if (ed_password.getText().toString().trim().equals("")) {
            ed_password.setError("Wajib Diisi");
            return false;
        }
        else if (ed_password_confrim.getText().toString().trim().equals("")) {
            ed_password_confrim.setError("Wajib Diisi");
            return false;
        }
        else if (!ed_password.getText().toString().trim().equals(ed_password_confrim.getText().toString().trim())) {
            ed_password_confrim.setError("Konfirmasi Password Salah");
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
