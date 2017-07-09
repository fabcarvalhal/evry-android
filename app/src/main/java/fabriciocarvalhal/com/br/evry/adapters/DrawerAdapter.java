package fabriciocarvalhal.com.br.evry.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fabriciocarvalhal.com.br.evry.Eventos.EventosContract;
import fabriciocarvalhal.com.br.evry.R;
import fabriciocarvalhal.com.br.evry.model.Curso;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Fabrício Carvalhal on 09/07/2017.
 */

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder> {
    private ArrayList<Curso> drawerMenuList;
    private EventosContract.UserActionsListener mUserActionsListener;
    private Context context;

    public DrawerAdapter(Context c, ArrayList<Curso> b, EventosContract.UserActionsListener mUserActionsListener) {
        this.drawerMenuList = b;
        this.mUserActionsListener = mUserActionsListener;
        this.context = c;
    }
    @Override
    public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new DrawerViewHolder(view);
    }

    public void replaceData(ArrayList<Curso> cursos){
        setList(cursos);
        notifyDataSetChanged();
    }
    private void setList(ArrayList<Curso> cursos) {
        drawerMenuList = checkNotNull(cursos);
    }
    @Override
    public void onBindViewHolder(DrawerViewHolder holder, final int position) {
        holder.title.setText(drawerMenuList.get(position).getNome());
        holder.icon.setImageResource(R.drawable.book);
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,drawerMenuList.get(position).getNome(),Toast.LENGTH_SHORT).show();
                mUserActionsListener.filterEventsByCourse(getItem(position).getId());
            }
        });

    }
    @Override
    public int getItemCount() {
        return drawerMenuList.size();
    }

    public Curso getItem(int position){
        return drawerMenuList.get(position);
    }

    class DrawerViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;
        LinearLayout ll;

        public DrawerViewHolder(View itemView) {
            super(itemView);
            ll = (LinearLayout) itemView.findViewById(R.id.linear_item_menu);
            title = (TextView) itemView.findViewById(R.id.txtItemMenu);
            icon = (ImageView) itemView.findViewById(R.id.imgItemMenu);
        }
    }
}