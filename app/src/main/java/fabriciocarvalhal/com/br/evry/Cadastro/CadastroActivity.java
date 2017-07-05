package fabriciocarvalhal.com.br.evry.Cadastro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fabriciocarvalhal.com.br.evry.R;
import fabriciocarvalhal.com.br.evry.model.Curso;
import fabriciocarvalhal.com.br.evry.util_conection.BaseRequest;
import fabriciocarvalhal.com.br.evry.util_conection.NetworkConnection;
import fabriciocarvalhal.com.br.evry.util_conection.ResponseConnection;
import fabriciocarvalhal.com.br.evry.util_conection.TentarNovamente;
import fabriciocarvalhal.com.br.evry.util_conection.TestarConexao;

/**
 * Created by Fabrício Carvalhal on 03/06/2017.
 */

public class CadastroActivity extends AppCompatActivity implements ResponseConnection, TentarNovamente {

    private Toolbar mToolbar;
    private String urlCursos = "http://evry.esy.es/";
    private List<Curso> cursosList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        mToolbar = (Toolbar)findViewById(R.id.toolbar3);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Cadastro");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        this.loadSpinner();
// Create an ArrayAdapter using the string array and a default spinner layout
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
          //      R.array.cursos_array, R.layout.spinner_item);
        ArrayAdapter<Curso> adp = new ArrayAdapter<Curso>(this,R.layout.spinner_item,cursosList);


// Specify the layout to use when the list of choices appears
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adp);
    }

    private void loadSpinner(){
        if (TestarConexao.VerificaConexao(this, this)) {

            NetworkConnection.getInstance(this).conectionVolley(this, this.urlCursos, Request.Method.GET);
        }
    }

    @Override
    public Map<String, String> doBefore() {
        return null;
    }

    @Override
    public void doAfter(BaseRequest object) {
        if(!object.isErro()){
            Gson gson = new Gson();
            List<Curso> listaCursos = new ArrayList<>();
            Curso[] cursos = gson.fromJson(object.getData().getAsJsonArray("cursos"),Curso[].class);

            for(Curso cs : cursos){
                cs.setId(cs.getId());
                cs.setNome(cs.getNome());
                listaCursos.add(cs);

            }


        }
    }

    @Override
    public void erroServer(VolleyError error) {

    }

    @Override
    public void tentarNovamente() {

    }
}
