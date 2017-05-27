package fabriciocarvalhal.com.br.evry.DetalhesEvento;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import fabriciocarvalhal.com.br.evry.R;
import fabriciocarvalhal.com.br.evry.model.Evento;

/**
 * Created by Fabrício Carvalhal on 21/05/2017.
 */

public class DetalhesEventoActivity extends AppCompatActivity  {

    private Toolbar mToolbar;
    private int event_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detalhe_evento);


        Intent intent = getIntent();
        if (intent != null){
            Bundle extras = intent.getExtras();
            if (extras != null){
                event_id = extras.getInt("event_id");
            }
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar_detalhe_evento);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (null == savedInstanceState) {
            initFragment(DetalhesEventoFragment.newInstance(event_id));
        }

    }


    private void initFragment(Fragment notesFragment) {
        // Add the NotesFragment to the layout
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container_frag, notesFragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}
