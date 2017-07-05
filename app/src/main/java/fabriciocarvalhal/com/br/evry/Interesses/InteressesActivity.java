package fabriciocarvalhal.com.br.evry.Interesses;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import fabriciocarvalhal.com.br.evry.R;

/**
 * Created by fabricio on 05/07/17.
 */

public class InteressesActivity extends AppCompatActivity{

    private RecyclerView rcvInteresses;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interesses);
    }


}
