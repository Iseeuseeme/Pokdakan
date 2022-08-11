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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
import app.pokdakan.R;

import static app.pokdakan.ConstantValues.TAG_ALAMAT;
import static app.pokdakan.ConstantValues.TAG_EMAIL;
import static app.pokdakan.ConstantValues.TAG_ID;
import static app.pokdakan.ConstantValues.TAG_NAMA;
import static app.pokdakan.ConstantValues.TAG_TELEPON;
import static app.pokdakan.ConstantValues.my_shared_preferences;
import static app.pokdakan.ConstantValues.session_status;

public class CreateItemSupplier extends Fragment {

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

    Spinner satuan_1, satuan_2, satuan_3, satuan_4;
    String s_satuan_1, s_satuan_2, s_satuan_3, s_satuan_4;
    EditText jumlah_1, jumlah_2, jumlah_3, jumlah_4;
    EditText produk_1, produk_2, produk_3, produk_4;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.item_create_event_supplier, container, false);
        sharedpreferences = getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        s_id = sharedpreferences.getString(TAG_ID, null);
        s_email = sharedpreferences.getString(TAG_EMAIL, null);
        s_nama = sharedpreferences.getString(TAG_NAMA, null);
        s_telepon = sharedpreferences.getString(TAG_TELEPON, null);
        s_alamat = sharedpreferences.getString(TAG_ALAMAT, null);

        mCompressor = new FileCompressor(getContext());
        s_tgl_mulai ="";
        s_tgl_selesai ="";

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

        produk_1 = root.findViewById(R.id.produk_1);
        produk_2 = root.findViewById(R.id.produk_2);
        produk_3 = root.findViewById(R.id.produk_3);
        produk_4 = root.findViewById(R.id.produk_4);
        satuan_1 = root.findViewById(R.id.satuan_1);
        satuan_2 = root.findViewById(R.id.satuan_2);
        satuan_3 = root.findViewById(R.id.satuan_3);
        satuan_4 = root.findViewById(R.id.satuan_4);
        jumlah_1 = root.findViewById(R.id.jumlah_1);
        jumlah_2 = root.findViewById(R.id.jumlah_2);
        jumlah_3 = root.findViewById(R.id.jumlah_3);
        jumlah_4 = root.findViewById(R.id.jumlah_4);

        tgl_mulai.setInputType(InputType.TYPE_NULL);
        tgl_selesai.setInputType(InputType.TYPE_NULL);

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
        return root;
    }

    private void upload() {
        pd.setMessage("Loading ...");
        pd.show();
        AndroidNetworking.upload(BASE.URL+"createevent")
                .addMultipartFile("image",mPhotoFile)
                .addMultipartParameter("user_id", s_id)
                .addMultipartParameter("judul", judul.getText().toString().trim())
                .addMultipartParameter("lokasi", lokasi.getText().toString().trim())
                .addMultipartParameter("deskripsi", deskripsi.getText().toString().trim())
                .addMultipartParameter("tgl_mulai", s_tgl_mulai)
                .addMultipartParameter("tgl_selesai", s_tgl_selesai)
                .addMultipartParameter("produk1", produk_1.getText().toString().trim())
                .addMultipartParameter("produk2", produk_2.getText().toString().trim())
                .addMultipartParameter("produk3", produk_3.getText().toString().trim())
                .addMultipartParameter("produk4", produk_4.getText().toString().trim())
                .addMultipartParameter("jumlah1", jumlah_1.getText().toString().trim())
                .addMultipartParameter("jumlah2", jumlah_2.getText().toString().trim())
                .addMultipartParameter("jumlah3", jumlah_3.getText().toString().trim())
                .addMultipartParameter("jumlah4", jumlah_4.getText().toString().trim())
                .addMultipartParameter("satuan1", s_satuan_1)
                .addMultipartParameter("satuan2", s_satuan_2)
                .addMultipartParameter("satuan3", s_satuan_3)
                .addMultipartParameter("satuan4", s_satuan_4)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pd.cancel();
                        try {
                            if(response.getString("status").equals("200")) {
                                judul.setText("");
                                deskripsi.setText("");
                                tgl_mulai.setText("");
                                tgl_selesai.setText("");
                                s_tgl_mulai = "";
                                s_tgl_selesai = "";
                                mPhotoFile = null;
                                jumlah_1.setText("");
                                jumlah_2.setText("");
                                jumlah_3.setText("");
                                jumlah_4.setText("");
                                gambar.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_image_150dp));
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
        else if (!produk_1.getText().toString().trim().equals("")) {
            if (jumlah_1.getText().toString().trim().equals("")) {
                jumlah_1.setError("Wajib Diisi");
                return false;
            }
            else {
                return true;
            }
        }
        else if (!produk_2.getText().toString().trim().equals("")) {
            if (jumlah_2.getText().toString().trim().equals("")) {
                jumlah_2.setError("Wajib Diisi");
                return false;
            }
            else {
                return true;
            }
        }
        else if (!produk_3.getText().toString().trim().equals("")) {
            if (jumlah_3.getText().toString().trim().equals("")) {
                jumlah_3.setError("Wajib Diisi");
                return false;
            }
            else {
                return true;
            }
        }
        else if (!produk_4.getText().toString().trim().equals("")) {
            if (jumlah_4.getText().toString().trim().equals("")) {
                jumlah_4.setError("Wajib Diisi");
                return false;
            }
            else {
                return true;
            }
        }
        else if (mPhotoFile == null) {
            Toast.makeText(getContext(),"Pilih Gambar Terlebih Dahulu", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }
}
