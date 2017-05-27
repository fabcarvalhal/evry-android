package fabriciocarvalhal.com.br.evry.adapters;

/**
 * Created by Fabrício Carvalhal on 20/05/2017.
 */

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import fabriciocarvalhal.com.br.evry.model.Evento;
import fabriciocarvalhal.com.br.evry.Eventos.EventosContract;
import fabriciocarvalhal.com.br.evry.R;

import java.sql.SQLOutput;
import java.util.List;
import java.util.logging.Logger;

import fabriciocarvalhal.com.br.evry.Eventos.EventosContract;
import fabriciocarvalhal.com.br.evry.model.Evento;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


public class EventosAdapter extends RecyclerView.Adapter<EventosAdapter.MyViewHolder>{

    private LayoutInflater mLayoutInflater;
    private List<Evento> mlista;
    private Context c;
    private EventosContract.UserActionsListener mUserActionsListener;

    public EventosAdapter(Context c, List<Evento> b, EventosContract.UserActionsListener mUserActionsListener) {

        mlista = b;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mUserActionsListener = mUserActionsListener;
        this.c = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = mLayoutInflater.inflate(R.layout.celula_evento, parent, false);


        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }


    public void addListaItem(Evento c, int position) {
        mlista.add(c);
        notifyItemInserted(position);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        Glide.with(c).
                load(mlista.get(position).getImagem()).
                centerCrop().
                crossFade().
                into(holder.img_book);

        holder.tv_titulo.setText(mlista.get(position).getNome());
        holder.tv_data.setText(mlista.get(position).getData_ini()+" à "+mlista.get(position).getData_fim());

        if(mlista.get(position).getIsOnUserEvents().equals("0")){
            holder.mark_event.setBackgroundResource(R.drawable.ic_bookmark_grey600_24dp);
        }else{
            holder.mark_event.setBackgroundResource(R.drawable.bookmark_check);
        }


        holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserActionsListener.openEventDetails(getItem(position),position);
            }
        });

    }

    public void replaceData(List<Evento> ev) {
        setList(ev);
        notifyDataSetChanged();
    }

    private void setList(List<Evento> books) {
        mlista = checkNotNull(books);
    }

    public Evento getItem(int position){
        return mlista.get(position);
    }
    @Override
    public int getItemCount() {
        return mlista.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CardView rl_item;
        public ImageView img_book;
        public TextView tv_titulo,tv_data;
        public Button mark_event;



        public MyViewHolder(View itemView) {
            super(itemView);

            rl_item = (CardView) itemView.findViewById(R.id.rl_item);

            img_book = (ImageView) itemView.findViewById(R.id.img_evento);

            tv_titulo =(TextView)itemView.findViewById(R.id.tv_titulo);

            mark_event = (Button) itemView.findViewById(R.id.mark_event);

            tv_data = (TextView) itemView.findViewById(R.id.tv_data);

           ;
        }


    }
}