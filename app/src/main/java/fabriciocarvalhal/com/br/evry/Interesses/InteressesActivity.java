package fabriciocarvalhal.com.br.evry.Interesses;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.ArrayList;
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
    private InteressesAdapter adapter;
    private String tipo_requisicao;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interesses);
        Intent it = getIntent();
        if (it != null){
            Bundle extras = it.getExtras();
            if(extras != null){
                Log.i("extras",String.valueOf(extras.getInt("curso")) );
            }
        }


        rcvInteresses = (RecyclerView) findViewById(R.id.recyclerView_interesses);
        adapter = new InteressesAdapter(this,new ArrayList<Interesse>(),this);
        rcvInteresses.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        rcvInteresses.setLayoutManager(llm);

        this.tipo_requisicao = "listInteresses";
        NetworkConnection.getInstance(this).conectionVolley(this,this.urlGetIntereses, Request.Method.GET);


    }


    @Override
    public void addInteresse(int id) {
        if(this.ItensSelecionados.size() < 2){
            this.ItensSelecionados.add(id);
        }else{
            Toast.makeText(this,"Você só pode selecionar 2 interesses", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void removeInteresse(int id) {
        if(this.ItensSelecionados.contains(id))
            this.ItensSelecionados.remove(this.ItensSelecionados.indexOf(id));

    }


    @Override
    public Map<String, String> doBefore() {
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
            }
        }
    }

    @Override
    public void erroServer(VolleyError error) {

    }
}
