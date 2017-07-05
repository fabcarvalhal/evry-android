package fabriciocarvalhal.com.br.evry.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fabriciocarvalhal.com.br.evry.R;

/**
 * Created by fabricio on 05/07/17.
 */

public class InteressesAdapter extends RecyclerView.Adapter<InteressesAdapter.ViewHolder> {

    private List<Interesse> mInteresses;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public InteressesAdapter(Context context, List<Interesse> interesse) {
        mInteresses = interesse;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nome;
        public CheckBox checkBox;
        public RelativeLayout rl;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            rl = (RelativeLayout) itemView.findViewById(R.id.celulaInt);
            nome = (TextView) itemView.findViewById(R.id.nome);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
        }
    }
}
