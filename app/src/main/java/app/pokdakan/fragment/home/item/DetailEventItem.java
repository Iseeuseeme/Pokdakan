package app.pokdakan.fragment.home.item;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.pokdakan.BASE;
import app.pokdakan.R;
import app.pokdakan.adapter.EventAdapterAll;
import app.pokdakan.adapter.EventIkanAdapter;
import app.pokdakan.model.EventIkanModel;
import app.pokdakan.model.EventModel;

import static app.pokdakan.BASE.URL_ASSET;
import static app.pokdakan.ConstantValues.TAG_ALAMAT;
import static app.pokdakan.ConstantValues.TAG_EMAIL;
import static app.pokdakan.ConstantValues.TAG_ID;
import static app.pokdakan.ConstantValues.TAG_NAMA;
import static app.pokdakan.ConstantValues.TAG_TELEPON;
import static app.pokdakan.ConstantValues.my_shared_preferences;

public class DetailEventItem extends Fragment implements EventIkanAdapter.KLIK {

    ProgressDialog pd;
    SharedPreferences sharedpreferences;
    Boolean session = false;
    String s_id, s_nama, s_email, s_telepon, s_alamat;

    String a_judul, a_deskripsi, a_tgl_mulai, a_tgl_selesai, a_gambar, a_user_id, a_id, a_lokasi, a_produk, a_jumlah, a_harga, a_terjual;

    ImageView image;
    TextView judul, lokasi, deskripsi, produk, jumlah, harga, terjual;
    RecyclerView rv;
    Button kontak_wa;
    EventIkanAdapter eventIkanAdapter;
    TextView no_data;
    List<EventIkanModel> data;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.item_detail_event, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Detail Event");
        sharedpreferences = getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        s_id = sharedpreferences.getString(TAG_ID, null);
        s_email = sharedpreferences.getString(TAG_EMAIL, null);
        s_nama = sharedpreferences.getString(TAG_NAMA, null);
        s_telepon = sharedpreferences.getString(TAG_TELEPON, null);
        s_alamat = sharedpreferences.getString(TAG_ALAMAT, null);

        a_judul = getArguments().getString("judul");
        a_deskripsi = getArguments().getString("deskripsi");
        a_tgl_mulai = getArguments().getString("tgl_mulai");
        a_tgl_selesai = getArguments().getString("tgl_selesai");
        a_gambar = getArguments().getString("gambar");
        a_user_id = getArguments().getString("user_id");
        a_id = getArguments().getString("id");
        a_lokasi = getArguments().getString("lokasi");
        a_produk = getArguments().getString("produk");
        a_jumlah = getArguments().getString("berat");
        a_harga = getArguments().getString("harga");
        a_terjual = getArguments().getString("terjual");

        rv = root.findViewById(R.id.rv);
        image = root.findViewById(R.id.image);
        judul = root.findViewById(R.id.judul);
        deskripsi = root.findViewById(R.id.deskripsi);
        lokasi = root.findViewById(R.id.lokasi);
        kontak_wa = root.findViewById(R.id.kontak_wa);
        produk = root.findViewById(R.id.produk);
        jumlah = root.findViewById(R.id.jumlah);
        harga = root.findViewById(R.id.harga);
        terjual = root.findViewById(R.id.terjual);

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setHasFixedSize(true);
        eventIkanAdapter=new EventIkanAdapter(getContext(), this);
        rv.setAdapter(eventIkanAdapter);
        data=new ArrayList<>();
//        rv.setNestedScrollingEnabled(false);

        Glide.with(getContext()).load(URL_ASSET+a_gambar)
                .fitCenter() // menyesuaikan ukuran imageview
                .crossFade() // animasi
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image);

        judul.setText(a_judul);
        deskripsi.setText(a_deskripsi);
        lokasi.setText(a_lokasi);
        produk.setText(": "+a_produk);
        jumlah.setText(": "+a_jumlah+"Kg");
        terjual.setText(a_terjual+" Kg");
        harga.setText(": "+formatRupiah(Double.parseDouble(a_harga)).replace("Rp", "Rp "));
//        getikan();

        kontak_wa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.whatsapp.com/send?phone="+s_telepon;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        return root;
    }

    @Override
    public void view(final EventIkanModel data) {

    }

    private String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    private void getikan(){
        AndroidNetworking.post(BASE.URL+"eventikan")
                .addBodyParameter("event_id", a_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("status").equals("200")) {
                                data.clear();
                                JSONArray ja=response.getJSONArray("data");
                                for(int i=0;i<ja.length();i++){
                                    JSONObject jo=ja.getJSONObject(i);
                                    data.add(new EventIkanModel(jo.getString("id"),
                                            jo.getString("event_id"),
                                            jo.getString("produk"),
                                            jo.getString("jumlah"),
                                            jo.getString("satuan")));
                                }
                                eventIkanAdapter.Update(data);
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
                        Toast.makeText(getContext(),anError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
