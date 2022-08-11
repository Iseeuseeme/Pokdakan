package app.pokdakan.fragment.home.item;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.pokdakan.BASE;
import app.pokdakan.FileCompressor;
import app.pokdakan.MainActivity;
import app.pokdakan.R;

import static app.pokdakan.BASE.URL_ASSET;
import static app.pokdakan.ConstantValues.TAG_ALAMAT;
import static app.pokdakan.ConstantValues.TAG_EMAIL;
import static app.pokdakan.ConstantValues.TAG_ID;
import static app.pokdakan.ConstantValues.TAG_NAMA;
import static app.pokdakan.ConstantValues.TAG_ROLE;
import static app.pokdakan.ConstantValues.TAG_TELEPON;
import static app.pokdakan.ConstantValues.my_shared_preferences;
import static app.pokdakan.ConstantValues.session_status;

public class EditEventItem extends Fragment {

    ImageView gambar;
    Button simpan;
    EditText judul, tgl_selesai, tgl_mulai, deskripsi, lokasi;
    ProgressDialog pd;
    SharedPreferences sharedpreferences;
    Boolean session = false;
    String s_id, s_nama, s_email, s_telepon, s_alamat;
    DatePickerDialog picker;
    String tanggal, bulan, s_tgl_mulai, s_tgl_selesai;
    private static int RESULT_LOAD_IMAGE = 1;
    File mPhotoFile;

    FileCompressor mCompressor;

    String a_judul, a_deskripsi, a_tgl_mulai, a_tgl_selesai, a_gambar, a_user_id, a_id, a_lokasi, a_harga, a_terjual;

    Spinner satuan_1, satuan_2, satuan_3, satuan_4;
    String s_satuan_1, s_satuan_2, s_satuan_3, s_satuan_4;
    EditText jumlah_1, jumlah_2, jumlah_3, jumlah_4;
    ArrayAdapter<CharSequence> adapter_satuan_1;
    ArrayAdapter<CharSequence> adapter_satuan_2;
    ArrayAdapter<CharSequence> adapter_satuan_3;
    ArrayAdapter<CharSequence> adapter_satuan_4;
    String a_produk, a_jumlah;

    EditText produk, jumlah, harga, terjual;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.item_edit_event, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Edit Event");
        sharedpreferences = getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
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

        mCompressor = new FileCompressor(getContext());
        s_tgl_mulai =a_tgl_mulai;
        s_tgl_selesai =a_tgl_selesai;

        AndroidNetworking.initialize(getContext());
        pd=new ProgressDialog(getContext());
        pd.setCancelable(false);

        gambar = root.findViewById(R.id.gambar);
        simpan = root.findViewById(R.id.simpan);
        judul = root.findViewById(R.id.judul);
        tgl_selesai = root.findViewById(R.id.tgl_selesai);
        tgl_mulai = root.findViewById(R.id.tgl_mulai);
        deskripsi = root.findViewById(R.id.deskripsi);
        lokasi = root.findViewById(R.id.lokasi);

        satuan_1 = root.findViewById(R.id.satuan_1);
        satuan_2 = root.findViewById(R.id.satuan_2);
        satuan_3 = root.findViewById(R.id.satuan_3);
        satuan_4 = root.findViewById(R.id.satuan_4);
        jumlah_1 = root.findViewById(R.id.jumlah_1);
        jumlah_2 = root.findViewById(R.id.jumlah_2);
        jumlah_3 = root.findViewById(R.id.jumlah_3);
        jumlah_4 = root.findViewById(R.id.jumlah_4);

        produk = root.findViewById(R.id.produk);
        jumlah = root.findViewById(R.id.jumlah);
        harga = root.findViewById(R.id.harga);
        terjual = root.findViewById(R.id.terjual);

        tgl_mulai.setInputType(InputType.TYPE_NULL);
        tgl_selesai.setInputType(InputType.TYPE_NULL);

        judul.setText(a_judul);
        deskripsi.setText(a_deskripsi);
        tgl_mulai.setText(a_tgl_mulai);
        tgl_selesai.setText(a_tgl_selesai);
        lokasi.setText(a_lokasi);
        produk.setText(a_produk);
        jumlah.setText(a_jumlah);
        harga.setText(a_harga);
        terjual.setText(a_terjual);
        Glide.with(getContext()).load(URL_ASSET+a_gambar)
                .fitCenter() // menyesuaikan ukuran imageview
                .crossFade() // animasi
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(gambar);

        adapter_satuan_1 = ArrayAdapter.createFromResource(getContext(), R.array.list_satuan, android.R.layout.simple_spinner_item);
        adapter_satuan_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        satuan_1.setAdapter(adapter_satuan_1);

        s_satuan_1 = satuan_1.getSelectedItem().toString();;
        satuan_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                s_satuan_1 = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter_satuan_2 = ArrayAdapter.createFromResource(getContext(), R.array.list_satuan, android.R.layout.simple_spinner_item);
        adapter_satuan_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        satuan_2.setAdapter(adapter_satuan_2);

        s_satuan_2 = satuan_2.getSelectedItem().toString();;
        satuan_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                s_satuan_2 = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter_satuan_3 = ArrayAdapter.createFromResource(getContext(), R.array.list_satuan, android.R.layout.simple_spinner_item);
        adapter_satuan_3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        satuan_3.setAdapter(adapter_satuan_3);

        s_satuan_3 = satuan_3.getSelectedItem().toString();;
        satuan_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                s_satuan_3 = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter_satuan_4 = ArrayAdapter.createFromResource(getContext(), R.array.list_satuan, android.R.layout.simple_spinner_item);
        adapter_satuan_4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        satuan_4.setAdapter(adapter_satuan_4);

        s_satuan_4 = satuan_4.getSelectedItem().toString();;
        satuan_4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                s_satuan_4 = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tgl_mulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                // date picker dialog
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                bulan = String.valueOf(monthOfYear + 1);
                                String bulan_fix = bulan.length() == 1 ? "0"+bulan : bulan;
                                String tanggal_fic = String.valueOf(dayOfMonth).length() == 1 ? "0"+dayOfMonth : String.valueOf(dayOfMonth);
                                if(bulan.equals("1")){
                                    bulan = "Januari";
                                }
                                else if(bulan.equals("2")){
                                    bulan = "Februari";
                                }
                                else if(bulan.equals("3")){
                                    bulan = "Maret";
                                }
                                else if(bulan.equals("4")){
                                    bulan = "April";
                                }
                                else if(bulan.equals("5")){
                                    bulan = "Mei";
                                }
                                else if(bulan.equals("6")){
                                    bulan = "Juni";
                                }
                                else if(bulan.equals("7")){
                                    bulan = "Juli";
                                }
                                else if(bulan.equals("8")){
                                    bulan = "Agustus";
                                }
                                else if(bulan.equals("9")){
                                    bulan = "September";
                                }
                                else if(bulan.equals("10")){
                                    bulan = "Oktober";
                                }
                                else if(bulan.equals("11")){
                                    bulan = "November";
                                }
                                else if(bulan.equals("12")){
                                    bulan = "Desember";
                                }
                                s_tgl_mulai = year + "-" + bulan_fix + "-" + tanggal_fic;
                                tgl_mulai.setText(s_tgl_mulai);
//                                tgl_mulai.setText(dayOfMonth+" "+bulan+" "+year);
                            }
                        }, cldr.get(Calendar.YEAR),
                        cldr.get(Calendar.MONTH),
                        cldr.get(Calendar.DAY_OF_MONTH));
                long currentTime = new Date().getTime();
                picker.getDatePicker().setMinDate(currentTime);
                picker.show();
            }
        });
        tgl_selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                // date picker dialog
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                bulan = String.valueOf(monthOfYear + 1);
                                String bulan_fix = bulan.length() == 1 ? "0"+bulan : bulan;
                                String tanggal_fic = String.valueOf(dayOfMonth).length() == 1 ? "0"+dayOfMonth : String.valueOf(dayOfMonth);
                                if(bulan.equals("1")){
                                    bulan = "Januari";
                                }
                                else if(bulan.equals("2")){
                                    bulan = "Februari";
                                }
                                else if(bulan.equals("3")){
                                    bulan = "Maret";
                                }
                                else if(bulan.equals("4")){
                                    bulan = "April";
                                }
                                else if(bulan.equals("5")){
                                    bulan = "Mei";
                                }
                                else if(bulan.equals("6")){
                                    bulan = "Juni";
                                }
                                else if(bulan.equals("7")){
                                    bulan = "Juli";
                                }
                                else if(bulan.equals("8")){
                                    bulan = "Agustus";
                                }
                                else if(bulan.equals("9")){
                                    bulan = "September";
                                }
                                else if(bulan.equals("10")){
                                    bulan = "Oktober";
                                }
                                else if(bulan.equals("11")){
                                    bulan = "November";
                                }
                                else if(bulan.equals("12")){
                                    bulan = "Desember";
                                }
                                s_tgl_selesai = year + "-" + bulan_fix + "-" + tanggal_fic;
                                tgl_selesai.setText(s_tgl_selesai);
//                                tgl_selesai.setText(dayOfMonth+" "+bulan+" "+year);
                            }
                        }, cldr.get(Calendar.YEAR),
                        cldr.get(Calendar.MONTH),
                        cldr.get(Calendar.DAY_OF_MONTH));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = format.parse(s_tgl_mulai);
                    long currentTime = date.getTime();
                    picker.getDatePicker().setMinDate(currentTime);
//                    System.out.println(date);
                } catch (ParseException e) {
//                    e.printStackTrace();
                }
//                long currentTime = new Date().getTime();
//                picker.getDatePicker().setMinDate(currentTime);
                picker.show();
            }
        });

        gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(pickPhoto, RESULT_LOAD_IMAGE);
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validasi()){
                    upload();
                }
            }
        });

//        getikan();
        return root;
    }

    private void getikan(){
        AndroidNetworking.post(BASE.URL+"eventikan")
                .addBodyParameter("event_id", a_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.cancel();
                        try {
                            if(response.getString("status").equals("200")) {
                                JSONArray ja=response.getJSONArray("data");
                                if(ja.length() >= 1){
                                    JSONObject jo1=ja.getJSONObject(0);
                                    jumlah_1.setText(jo1.getString("jumlah"));
                                    int spinnerPosition_1 = adapter_satuan_1.getPosition(jo1.getString("satuan"));
                                    satuan_1.setSelection(spinnerPosition_1);;
                                }
                                if(ja.length() >= 2) {
                                    JSONObject jo2 = ja.getJSONObject(1);
                                    jumlah_2.setText(jo2.getString("jumlah"));
                                    int spinnerPosition_2 = adapter_satuan_2.getPosition(jo2.getString("satuan"));
                                    satuan_2.setSelection(spinnerPosition_2);
                                    ;
                                }
                                if(ja.length() >= 3) {
                                    JSONObject jo3 = ja.getJSONObject(2);
                                    jumlah_3.setText(jo3.getString("jumlah"));
                                    int spinnerPosition_3 = adapter_satuan_3.getPosition(jo3.getString("satuan"));
                                    satuan_3.setSelection(spinnerPosition_3);
                                }
                                if(ja.length() >= 4) {
                                    JSONObject jo4 = ja.getJSONObject(3);
                                    jumlah_4.setText(jo4.getString("jumlah"));
                                    int spinnerPosition_4 = adapter_satuan_4.getPosition(jo4.getString("satuan"));
                                    satuan_4.setSelection(spinnerPosition_4);
                                }
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

    private void upload() {
        pd.setMessage("Loading ...");
        pd.show();
        AndroidNetworking.upload(BASE.URL+"updateevent")
                .addMultipartFile("image",mPhotoFile)
                .addMultipartParameter("id", a_id)
                .addMultipartParameter("user_id", s_id)
                .addMultipartParameter("judul", judul.getText().toString().trim())
                .addMultipartParameter("lokasi", lokasi.getText().toString().trim())
                .addMultipartParameter("deskripsi", deskripsi.getText().toString().trim())
                .addMultipartParameter("tgl_mulai", s_tgl_mulai)
                .addMultipartParameter("tgl_selesai", s_tgl_selesai)
                .addMultipartParameter("produk_dijual", produk.getText().toString().trim())
                .addMultipartParameter("berat", jumlah.getText().toString().trim())
                .addMultipartParameter("harga", harga.getText().toString().trim())
                .addMultipartParameter("terjual", terjual.getText().toString().trim())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.cancel();
                        try {
                            if(response.getString("status").equals("200")) {
                                Toast.makeText(getContext(),response.getString("pesan"), Toast.LENGTH_LONG).show();
                                FragmentManager fm = getFragmentManager();
                                fm.popBackStack();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == RESULT_LOAD_IMAGE) {
            Uri selectedImage = data.getData();
            try {
                mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(getContext(),"berhasil", Toast.LENGTH_LONG).show();
            Glide.with(getContext()).load(mPhotoFile)
                    .fitCenter() // menyesuaikan ukuran imageview
                    .crossFade() // animasi
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(gambar);
        }
        else {
            Toast.makeText(getContext(),"gagal", Toast.LENGTH_LONG).show();
        }
    }

    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private boolean validasi() {
        if (judul.getText().toString().trim().equals("")) {
            judul.setError("Wajib Diisi");
            return false;
        }
        else if (s_tgl_mulai.equals("")) {
            tgl_mulai.setError("Wajib Diisi");
            return false;
        }
        else if (s_tgl_selesai.equals("")) {
            tgl_selesai.setError("Email Tidak Valid");
            return false;
        }
        else if (lokasi.getText().toString().trim().equals("")) {
            lokasi.setError("Wajib Diisi");
            return false;
        }
        else if (deskripsi.getText().toString().trim().equals("")) {
            deskripsi.setError("Wajib Diisi");
            return false;
        }
        else if (produk.getText().toString().trim().equals("")) {
            produk.setError("Wajib Diisi");
            return false;
        }
        else if (jumlah.getText().toString().trim().equals("")) {
            jumlah.setError("Wajib Diisi");
            return false;
        }
        else if (harga.getText().toString().trim().equals("")) {
            harga.setError("Wajib Diisi");
            return false;
        }
        else if (terjual.getText().toString().trim().equals("")) {
            terjual.setError("Wajib Diisi");
            return false;
        }
        else {
            return true;
        }
    }
}
