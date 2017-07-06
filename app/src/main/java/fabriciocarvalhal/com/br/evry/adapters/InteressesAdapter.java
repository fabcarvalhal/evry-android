package fabriciocarvalhal.com.br.evry.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import fabriciocarvalhal.com.br.evry.Interesses.InteressesContract;
import fabriciocarvalhal.com.br.evry.R;
import fabriciocarvalhal.com.br.evry.model.Interesse;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by fabricio on 05/07/17.
 */

public class InteressesAdapter extends RecyclerView.Adapter<InteressesAdapter.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private List<Interesse> mInteresses;
    // Store the context for easy access
    private Context mContext;
    private InteressesContract.mUserActionsListener mUserActionsListener;

    // Pass in the contact array into the constructor
    public InteressesAdapter(Context context, List<Interesse> interesse, InteressesContract.mUserActionsListener actionsListener) {
        mInteresses = interesse;
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mUserActionsListener = actionsListener;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_interesse, parent, false);


        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.nome.setText(mInteresses.get(position).getNome());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mUserActionsListener.addInteresse(mInteresses.get(position).getId());
                }else{
                    mUserActionsListener.removeInteresse(mInteresses.get(position).getId());
                }
            }
        });
    }

    public void replaceData(List<Interesse> in){
        Log.i("setando", " set interesses");
        setList(in);
        notifyDataSetChanged();
    }

    private void setList(List<Interesse> ints) {
        mInteresses = checkNotNull(ints);
    }

    @Override
    public int getItemCount() {
        return mInteresses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nome;
        public CheckBox checkBox;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);


            nome = (TextView) itemView.findViewById(R.id.nome);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
        }
    }
}
