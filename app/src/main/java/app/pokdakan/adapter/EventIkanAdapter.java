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

import app.pokdakan.R;
import app.pokdakan.model.EventIkanModel;
import app.pokdakan.model.EventModel;

import static app.pokdakan.BASE.URL_ASSET;

public class EventIkanAdapter extends RecyclerView.Adapter<EventIkanAdapter.PendudukHolder> {
    List<EventIkanModel> data;
    Context c;
    AlertDialog.Builder mAlert;
    KLIK klik;
    public EventIkanAdapter(Context c, KLIK klik) {
        this.c = c;
        this.klik=klik;
        mAlert=new AlertDialog.Builder(c);
    }

    public void Update(List<EventIkanModel> data){
        this.data=data;
        notifyDataSetChanged();
    }


    public interface KLIK{
        void view(EventIkanModel data);
    }

    @NonNull
    @Override
    public PendudukHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.list_produk, parent, false);
        return new PendudukHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PendudukHolder holder, final int position) {
        holder.produk.setText(data.get(position).getProduk());
        holder.jumlah.setText(": "+data.get(position).getJumlah()+" "+data.get(position).getSatuan());

    }

    @Override
    public int getItemCount() {
        return data!=null ? data.size() : 0;
    }

    public class PendudukHolder extends RecyclerView.ViewHolder{
        TextView produk, jumlah;
        public PendudukHolder(@NonNull View v) {
            super(v);
            produk=v.findViewById(R.id.produk);
            jumlah=v.findViewById(R.id.jumlah);
        }
    }
}
