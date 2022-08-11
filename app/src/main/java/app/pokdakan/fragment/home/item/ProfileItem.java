package app.pokdakan.fragment.home.item;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import app.pokdakan.BASE;
import app.pokdakan.R;

import static app.pokdakan.ConstantValues.TAG_ALAMAT;
import static app.pokdakan.ConstantValues.TAG_EMAIL;
import static app.pokdakan.ConstantValues.TAG_ID;
import static app.pokdakan.ConstantValues.TAG_NAMA;
import static app.pokdakan.ConstantValues.TAG_ROLE;
import static app.pokdakan.ConstantValues.TAG_TELEPON;
import static app.pokdakan.ConstantValues.my_shared_preferences;
import static app.pokdakan.ConstantValues.session_status;

public class ProfileItem extends Fragment {

    EditText nama, email, alamat, telepon, password, password_konfirmasi;
    Button simpan;
    ProgressDialog pd;
    SharedPreferences sharedpreferences;
    Boolean session = false;
    String s_id, s_nama, s_email, s_telepon, s_alamat;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.item_profile, container, false);
        sharedpreferences = getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        s_id = sharedpreferences.getString(TAG_ID, null);
        s_email = sharedpreferences.getString(TAG_EMAIL, null);
        s_nama = sharedpreferences.getString(TAG_NAMA, null);
        s_telepon = sharedpreferences.getString(TAG_TELEPON, null);
        s_alamat = sharedpreferences.getString(TAG_ALAMAT, null);

        AndroidNetworking.initialize(getContext());
        pd=new ProgressDialog(getContext());
        pd.setCancelable(false);

        nama = root.findViewById(R.id.nama);
        email = root.findViewById(R.id.email);
        alamat = root.findViewById(R.id.alamat);
        telepon = root.findViewById(R.id.telepon);
        password = root.findViewById(R.id.password);
        password_konfirmasi = root.findViewById(R.id.password_konfirmasi);
        simpan = root.findViewById(R.id.simpan);

        nama.setText(s_nama);
        email.setText(s_email);
        telepon.setText(s_telepon);
        alamat.setText(s_alamat);

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validasi()){
                    update();
                }
            }
        });
        return root;
    }

    private void update() {
        pd.setMessage("Loading ...");
        pd.show();
        AndroidNetworking.post(BASE.URL+"updateprofile")
                .addBodyParameter("id", s_id)
                .addBodyParameter("name", nama.getText().toString().trim())
                .addBodyParameter("email", email.getText().toString().trim())
                .addBodyParameter("telepon", telepon.getText().toString().trim())
                .addBodyParameter("alamat", alamat.getText().toString().trim())
                .addBodyParameter("password", password.getText().toString().trim())
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

                                Toast.makeText(getContext(),response.getString("pesan"), Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getContext(),response.getString("pesan"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.cancel();
                        Toast.makeText(getContext(),anError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validasi() {
        if (!password.getText().toString().trim().equals("")) {
            if (nama.getText().toString().trim().equals("")) {
                nama.setError("Wajib Diisi");
                return false;
            }
            else if (email.getText().toString().trim().equals("")) {
                email.setError("Wajib Diisi");
                return false;
            }
            else if (!isValidEmail(email.getText().toString().trim())) {
                email.setError("Email Tidak Valid");
                return false;
            }
            else if (telepon.getText().toString().trim().equals("")) {
                telepon.setError("Wajib Diisi");
                return false;
            }
            else if (alamat.getText().toString().trim().equals("")) {
                alamat.setError("Wajib Diisi");
                return false;
            }
            else if (password_konfirmasi.getText().toString().trim().equals("")) {
                password_konfirmasi.setError("Wajib Diisi");
                return false;
            }
            else if (!password_konfirmasi.getText().toString().trim().equals(password.getText().toString().trim())) {
                password_konfirmasi.setError("Password Konfirmasi Tidak Sama");
                return false;
            }
            else {
                return true;
            }
        }
        else {
            if (nama.getText().toString().trim().equals("")) {
                nama.setError("Wajib Diisi");
                return false;
            }
            else if (email.getText().toString().trim().equals("")) {
                email.setError("Wajib Diisi");
                return false;
            }
            else if (!isValidEmail(email.getText().toString().trim())) {
                email.setError("Email Tidak Valid");
                return false;
            }
            else if (telepon.getText().toString().trim().equals("")) {
                telepon.setError("Wajib Diisi");
                return false;
            }
            else if (alamat.getText().toString().trim().equals("")) {
                alamat.setError("Wajib Diisi");
                return false;
            }
            else {
                return true;
            }
        }
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
