package app.pokdakan.fragment.sign;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import app.pokdakan.LoginActivity;
import app.pokdakan.R;

import static app.pokdakan.ConstantValues.TAG_ALAMAT;
import static app.pokdakan.ConstantValues.TAG_EMAIL;
import static app.pokdakan.ConstantValues.TAG_ID;
import static app.pokdakan.ConstantValues.TAG_NAMA;
import static app.pokdakan.ConstantValues.TAG_ROLE;
import static app.pokdakan.ConstantValues.TAG_TELEPON;
import static app.pokdakan.ConstantValues.my_shared_preferences;
import static app.pokdakan.ConstantValues.session_status;

public class SignFragment extends Fragment {

    Button signout;
    SharedPreferences sharedpreferences;
    Boolean session = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sign_out, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Sign Out");
        sharedpreferences = getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);


        signout = root.findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(session_status, false);
                editor.putString(TAG_ID, null);
                editor.putString(TAG_NAMA, null);
                editor.putString(TAG_EMAIL, null);
                editor.putString(TAG_ALAMAT, null);
                editor.putString(TAG_TELEPON, null);
                editor.putString(TAG_ROLE, null);
                editor.commit();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return root;
    }
}
