package fabriciocarvalhal.com.br.evry.adapters;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;

import fabriciocarvalhal.com.br.evry.model.Curso;

/**
 * Created by Fabrício Carvalhal on 05/07/2017.
 */

public class CursosAdapter extends ArrayAdapter<Curso>{



    public CursosAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<Curso> objects) {
        super(context, resource, textViewResourceId, objects);
    }


}
