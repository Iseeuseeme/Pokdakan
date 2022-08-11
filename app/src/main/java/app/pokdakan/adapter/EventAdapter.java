package app.pokdakan.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import app.pokdakan.model.EventModel;
import app.pokdakan.R;

import static app.pokdakan.BASE.URL_ASSET;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.PendudukHolder> {
    List<EventModel> data;
    Context c;
    AlertDialog.Builder mAlert;
    KLIK klik;
    public EventAdapter(Context c, KLIK klik) {
        this.c = c;
        this.klik=klik;
        mAlert=new AlertDialog.Builder(c);
    }

    public void Update(List<EventModel> data){
        this.data=data;
        notifyDataSetChanged();
    }


    public interface KLIK{
        void view(EventModel data);
    }

    @NonNull
    @Override
    public PendudukHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.item_event_card, parent, false);
        return new PendudukHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PendudukHolder holder, final int position) {
        holder.title.setText(data.get(position).getJudul());
        holder.description.setText(data.get(position).getDeskripsi());
        holder.date.setText(setTgl(data.get(position).getTgl_mulai())+" s/d "+setTgl(data.get(position).getTgl_selesai()));

        Glide.with(c).load(URL_ASSET+data.get(position).getGambar())
                .fitCenter() // menyesuaikan ukuran imageview
                .crossFade() // animasi
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);
        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                klik.view(data.get(position));
            }
        });
        holder.cardln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                klik.view(data.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data!=null ? data.size() : 0;
    }

    public class PendudukHolder extends RecyclerView.ViewHolder{
        TextView title, description, date;
        LinearLayout lyt_parent;
        CardView cardln;
        ImageView image;
        public PendudukHolder(@NonNull View v) {
            super(v);
            title=v.findViewById(R.id.title);
            description=v.findViewById(R.id.description);
            date=v.findViewById(R.id.date);
            image=v.findViewById(R.id.image);
            lyt_parent=v.findViewById(R.id.lyt_parent);
            cardln=v.findViewById(R.id.cardln);
        }
    }

    public String setTgl( String tgl){
        int stringLength = tgl.length();
        String tahun = tgl.substring(0, 4);
        String bulan = tgl.substring(5, stringLength-2);
        if(bulan.equals("01-")){
            bulan = "Januari";
        }
        else if(bulan.equals("02-")){
            bulan = "Februari";
        }
        else if(bulan.equals("03-")){
            bulan = "Maret";
        }
        else if(bulan.equals("04-")){
            bulan = "April";
        }
        else if(bulan.equals("05-")){
            bulan = "Mei";
        }
        else if(bulan.equals("06-")){
            bulan = "Juni";
        }
        else if(bulan.equals("07-")){
            bulan = "Juli";
        }
        else if(bulan.equals("08-")){
            bulan = "Agustus";
        }
        else if(bulan.equals("09-")){
            bulan = "September";
        }
        else if(bulan.equals("10-")){
            bulan = "Oktober";
        }
        else if(bulan.equals("11-")){
            bulan = "November";
        }
        else if(bulan.equals("12-")){
            bulan = "Desember";
        }

        String hari = tgl.substring(8);

        String tanggalnya = hari+" "+bulan+" "+tahun;
        String cek = tanggalnya.substring(0,1);
        if(cek.equals("0")){
            String tgl_baru = tanggalnya.substring(1);
            return tgl_baru;
        }
        else {
            return tanggalnya;
        }
    }
}
