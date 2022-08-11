package app.pokdakan.fragment.home.item;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.pokdakan.BASE;
import app.pokdakan.R;
import app.pokdakan.adapter.EventAdapter;
import app.pokdakan.model.EventModel;

import static app.pokdakan.ConstantValues.TAG_ID;
import static app.pokdakan.ConstantValues.TAG_ROLE;
import static app.pokdakan.ConstantValues.my_shared_preferences;

public class EventItem extends Fragment implements EventAdapter.KLIK {

    SharedPreferences sharedpreferences;
    String id, role;
    LinearLayout splash;
    RecyclerView rv;
    EventAdapter eventAdapter;
    TextView no_data;
    List<EventModel> data;
    ProgressDialog pd;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.item_event, container, false);

        sharedpreferences = getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedpreferences.getString(TAG_ID, null);
        role = sharedpreferences.getString(TAG_ROLE, null);

        rv = root.findViewById(R.id.rv);
        splash = root.findViewById(R.id.splash);
        no_data = root.findViewById(R.id.no_data);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setHasFixedSize(true);
        eventAdapter=new EventAdapter(getContext(), this);
        rv.setAdapter(eventAdapter);
        data=new ArrayList<>();

        AndroidNetworking.initialize(getContext());
        pd=new ProgressDialog(getContext());
        pd.setCancelable(false);

        get();


        return root;
    }

    private void get(){
        AndroidNetworking.post(BASE.URL+"event")
                .addBodyParameter("user_id", id)
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
        final CharSequence[] dialogitem = {"Edit", "Hapus"};
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Silahkan Pilih :");
        builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item){
                    case 0 :
                        Fragment fragment = new EditEventItem();
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
                        break;
                    case 1 :
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        hapus(data.getId());
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder_delete = new AlertDialog.Builder(getContext());
                        builder_delete.setCancelable(false);
                        builder_delete.setMessage("Apakah anda yakin ingin mengahapus "+data.getJudul()+" dari data event?").setPositiveButton("Ya", dialogClickListener)
                                .setNegativeButton("Tidak", dialogClickListener).show();
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void hapus(final String id){
        pd.setMessage("Proses Hapus ...");
        pd.show();
        AndroidNetworking.post(BASE.URL+"hapusevent")
                .addBodyParameter("id", id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.cancel();
                        try {
                            if(response.getString("status").equals("200")) {
                                get();
                                Toast.makeText(getContext(),response.getString("pesan"), Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getContext(),response.getString("pesan"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(),"Gagal Menghapus, Coba Lagi", Toast.LENGTH_LONG).show();
//                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.cancel();
                        Toast.makeText(getContext(),"Gagal Menghapus, Coba Lagi", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
