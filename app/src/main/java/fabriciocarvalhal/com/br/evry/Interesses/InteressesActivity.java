package fabriciocarvalhal.com.br.evry.Interesses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fabriciocarvalhal.com.br.evry.R;
import fabriciocarvalhal.com.br.evry.adapters.InteressesAdapter;
import fabriciocarvalhal.com.br.evry.model.Interesse;
import fabriciocarvalhal.com.br.evry.util_conection.BaseRequest;
import fabriciocarvalhal.com.br.evry.util_conection.NetworkConnection;
import fabriciocarvalhal.com.br.evry.util_conection.ResponseConnection;

/**
 * Created by fabricio on 05/07/17.
 */

public class InteressesActivity extends AppCompatActivity implements InteressesContract.mUserActionsListener, ResponseConnection{

    private RecyclerView rcvInteresses;
    public ArrayList<Integer> ItensSelecionados = new ArrayList<Integer>();
    private InteressesContract.mUserActionsListener mUserActionsListener;
    private String urlGetIntereses = "http://evry.esy.es/api/interesses";
    private String urlCadastro = "http://evry.esy.es/api/cadastrar";
    private InteressesAdapter adapter;
    private String tipo_requisicao;
    private String nome;
    private String id;
    private String tipo_conta;
    private String email;
    private int curso;
    private int cursando;
    private String instituicao;
    private Button btnFinish;
    private String urlPhoto;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interesses);
        Intent it = getIntent();
        if (it != null){
            Bundle extras = it.getExtras();
            if(extras != null){
                this.nome = extras.getString("nome");
                this.email = extras.getString("email");
                this.tipo_conta = extras.getString("tipo");
                this.id = extras.getString("id");
                this.cursando = extras.getInt("cursando");
                this.curso = extras.getInt("curso");
                this.instituicao = extras.getString("instituicao");
                this.urlPhoto = extras.getString("urlPhoto");
            }
        }

        btnFinish = (Button) findViewById(R.id.finishCad);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo_requisicao = "finishCadastro";
                NetworkConnection.getInstance(InteressesActivity.this).conectionVolley(InteressesActivity.this,urlCadastro, Request.Method.POST);
            }
        });
        rcvInteresses = (RecyclerView) findViewById(R.id.recyclerView_interesses);
        adapter = new InteressesAdapter(InteressesActivity.this,new ArrayList<Interesse>(),this);
        rcvInteresses.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        rcvInteresses.setLayoutManager(llm);

        this.tipo_requisicao = "listInteresses";
        NetworkConnection.getInstance(this).conectionVolley(this,this.urlGetIntereses, Request.Method.GET);


    }


    @Override
    public int getInteressesCount() {
        return this.ItensSelecionados.size();
    }

    @Override
    public void addInteresse(int id) {
        this.ItensSelecionados.add(id);
    }

    @Override
    public void removeInteresse(int id) {
        if(this.ItensSelecionados.contains(id))
            this.ItensSelecionados.remove(this.ItensSelecionados.indexOf(id));

    }

    @Override
    public void showAlertaQtdInteresse() {
        Toast.makeText(this,"Só é possível escolher 2 interesses", Toast.LENGTH_SHORT).show();
    }


    @Override
    public Map<String, String> doBefore() {
        if(tipo_requisicao.equals("finishCadastro")){
            Map<String, String> params = new HashMap<>();
            params.put("nome", nome);
            params.put("id_conta_delegada",id);
            params.put("tipo",tipo_conta);
            params.put("email",email);
            params.put("curso",String.valueOf(curso));
            params.put("cursando", String.valueOf(cursando));
            params.put("instituicao",instituicao);
            Gson gson = new Gson();
            params.put("interesses",gson.toJson(this.ItensSelecionados));

            Log.i("dsd",params.toString());
            return params;

        }


        return null;
    }

    @Override
    public void doAfter(BaseRequest object) {
        if(!object.isErro()){
            if(this.tipo_requisicao.equals("listInteresses")){
                Gson gson = new Gson();
                List<Interesse> interessesList = new ArrayList<>();

                Interesse[] interesses = gson.fromJson(object.getData().getAsJsonArray("interesses"),Interesse[].class);

                for (Interesse i : interesses){
                    Log.i("interesse", i.getNome());
                    i.setId(i.getId());
                    i.setNome(i.getNome());
                    interessesList.add(i);
                }

                adapter.replaceData(interessesList);
                this.tipo_requisicao = null;
            }else if (this.tipo_requisicao.equals("finishCadastro") ){

                SharedPreferences sharedPref =getApplicationContext().getSharedPreferences("shared",0);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("userid", object.getData().get("userid").getAsInt());
                editor.putString("user_name", this.nome);
                editor.putString("urlPhoto",this.urlPhoto);
                editor.commit();

                finish();

            }
        }
    }

    @Override
    public void erroServer(VolleyError error) {

    }
}
