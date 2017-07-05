package fabriciocarvalhal.com.br.evry.Cadastro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fabriciocarvalhal.com.br.evry.Interesses.InteressesActivity;
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

public class CadastroActivity extends AppCompatActivity implements ResponseConnection, TentarNovamente, Spinner.OnItemSelectedListener {

    private Toolbar mToolbar;
    private String urlCursos = "http://evry.esy.es/api/cursos";
    private List<Curso> cursosList = new ArrayList<Curso>();
    private Spinner spinner;
    private EditText txtInstituicao;
    private Curso cursoSelecionado;
    private Button btnProximo;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private String nome;
    private String email;
    private String tipo;
    private String id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        mToolbar = (Toolbar)findViewById(R.id.toolbar3);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Cadastro");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        if (intent != null){
            Bundle extras = intent.getExtras();
            if (extras != null){
                nome = extras.getString("nome");
                email= extras.getString("email");
                tipo = extras.getString("tipo");
                id   = extras.getString("idgoogle");
            }
        }

        initViews();
        this.loadSpinner();


    }


    private void initViews(){
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        radioGroup = (RadioGroup) findViewById(R.id.rgCursando);
        btnProximo = (Button) findViewById(R.id.btnProximo);
        txtInstituicao = (EditText) findViewById(R.id.edit_ies);

        btnProximo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);
                Intent it = new Intent(CadastroActivity.this,InteressesActivity.class);
                startActivity(it);
                /*Toast.makeText(CadastroActivity.this,
                        radioButton.getText(), Toast.LENGTH_SHORT).show();*/

            }

        });
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
            Curso[] cursos = gson.fromJson(object.getData().getAsJsonArray("cursos"),Curso[].class);
            List<String> l = new ArrayList<>();
            l.add("Selecione o curso");
            for(Curso cs : cursos){
                cs.setId(cs.getId());
                cs.setNome(cs.getNome());
                cursosList.add(cs);
                l.add(cs.getNome());

            }
            ArrayAdapter<String> adp = new ArrayAdapter<String>(this,R.layout.spinner_item,l);
            adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adp);

        }
    }

    @Override
    public void erroServer(VolleyError error) {

    }

    @Override
    public void tentarNovamente() {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position > 0) {
            this.cursoSelecionado = cursosList.get(position-1);
            //Toast.makeText(CadastroActivity.this,
             //       cursosList.get(position-1).getNome(), Toast.LENGTH_SHORT).show();
        }else{
            this.cursoSelecionado = null;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        this.cursoSelecionado = null;
    }


}
