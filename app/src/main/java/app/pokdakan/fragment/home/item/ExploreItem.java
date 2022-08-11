package app.pokdakan.fragment.home.item;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.pokdakan.BASE;
import app.pokdakan.DataGenerator;
import app.pokdakan.R;
import app.pokdakan.adapter.AdapterEvent;
import app.pokdakan.adapter.EventAdapter;
import app.pokdakan.adapter.EventAdapterAll;
import app.pokdakan.model.Event;
import app.pokdakan.model.EventModel;

import static app.pokdakan.ConstantValues.TAG_ID;
import static app.pokdakan.ConstantValues.my_shared_preferences;

public class ExploreItem extends Fragment implements EventAdapterAll.KLIK {

    SharedPreferences sharedpreferences;
    String id;
    LinearLayout splash;
    RecyclerView rv;
    EventAdapterAll eventAdapter;
    TextView no_data;
    List<EventModel> data;
    ProgressDialog pd;
    EditText et_search;
    ImageButton bt_clear, bt_search;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.item_explore, container, false);

        sharedpreferences = getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedpreferences.getString(TAG_ID, null);

        rv = root.findViewById(R.id.rv);
        splash = root.findViewById(R.id.splash);
        no_data = root.findViewById(R.id.no_data);
        et_search = root.findViewById(R.id.et_search);
        bt_clear = root.findViewById(R.id.bt_clear);
        bt_search = root.findViewById(R.id.bt_search);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setHasFixedSize(true);
        eventAdapter=new EventAdapterAll(getContext(), this);
        rv.setAdapter(eventAdapter);
        data=new ArrayList<>();

        et_search.clearFocus();

        AndroidNetworking.initialize(getContext());
        pd=new ProgressDialog(getContext());
        pd.setCancelable(false);

        get("");
        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                String cari = s.toString();
                get(cari);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });

        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get("");
                et_search.setText("");
            }
        });
        return root;
    }

    private void get(String cari){
        AndroidNetworking.post(BASE.URL+"allevent")
                .addBodyParameter("cari", cari)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        splash.setVisibility(View.GONE);
                        try {
                            if(response.getString("status").equals("200")){
                                data.clear();
                                JSONArray ja=response.getJSONArray("data");
                                for(int i=0;i<ja.length();i++){
                                    JSONObject jo=ja.getJSONObject(i);
                                    data.add(new EventModel(jo.getString("id"),
                                            jo.getString("user_id"),
                                            jo.getString("judul"),
                                            jo.getString("deskripsi"),
                                            jo.getString("tgl_mulai"),
                                            jo.getString("tgl_selesai"),
                                            jo.getString("gambar_event"),
                                            jo.getString("name"),
                                            jo.getString("lokasi"),
                                            jo.getString("produk_dijual"),
                                            jo.getString("berat"),
                                            jo.getString("harga"),
                                            jo.getString("terjual")));
                                }
                                eventAdapter.Update(data);
                                if(ja.length() > 0){
                                    no_data.setVisibility(View.GONE);
                                    rv.setVisibility(View.VISIBLE);
                                }
                                else {
                                    no_data.setVisibility(View.VISIBLE);
                                    rv.setVisibility(View.GONE);
                                }
                            }else{
                                no_data.setVisibility(View.VISIBLE);
                                rv.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        splash.setVisibility(View.GONE);
                        Toast.makeText(getContext(), anError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void view(final EventModel data) {
        Fragment fragment = new DetailEventItem();
        Bundle args = new Bundle();
        args.putString("id", data.getId());
        args.putString("user_id", data.getUser_id());
        args.putString("judul", data.getJudul());
        args.putString("deskripsi", data.getDeskripsi());
        args.putString("tgl_mulai", data.getTgl_mulai());
        args.putString("tgl_selesai", data.getTgl_selesai());
        args.putString("gambar", data.getGambar());
        args.putString("lokasi", data.getLokasi());
        args.putString("produk", data.getProduk());
        args.putString("berat", data.getBerat());
        args.putString("harga", data.getHarga());
        args.putString("terjual", data.getTerjual());
        fragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
    }
}
