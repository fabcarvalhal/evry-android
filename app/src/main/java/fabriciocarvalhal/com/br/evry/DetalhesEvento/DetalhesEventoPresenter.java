package fabriciocarvalhal.com.br.evry.DetalhesEvento;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import fabriciocarvalhal.com.br.evry.Login.LoginActivity;
import fabriciocarvalhal.com.br.evry.model.Evento;
import fabriciocarvalhal.com.br.evry.util_conection.BaseRequest;
import fabriciocarvalhal.com.br.evry.util_conection.NetworkConnection;
import fabriciocarvalhal.com.br.evry.util_conection.ResponseConnection;
import fabriciocarvalhal.com.br.evry.util_conection.TentarNovamente;
import fabriciocarvalhal.com.br.evry.util_conection.TestarConexao;

/**
 * Created by Fabrício Carvalhal on 21/05/2017.
 */

public class DetalhesEventoPresenter implements DetalhesEventoContract.UserActionsListener,TentarNovamente,ResponseConnection {

    private DetalhesEventoContract.View eventoDetalheView;
    private Activity activity;
    private int cod;
    private String url_event = "http://evry.esy.es/api/event/";
    private Evento evento;
    private boolean isAddEvent = false;
    private String url_addEvent = "http://evry.esy.es/api/event/addToList";
    private boolean isRemoveEvent = false;
    private String url_removeEvent = "http://evry.esy.es/api/event/removeFromList";
    private String userid;

    public DetalhesEventoPresenter(Activity activity, DetalhesEventoContract.View eventoDetalheView){
        this.eventoDetalheView = eventoDetalheView;
        this.activity = activity;
        SharedPreferences shared = activity.getSharedPreferences("shared", Context.MODE_PRIVATE);
        this.userid = shared.getString("userid","0");
    }



    @Override
    public void loadEvent(int event_id) {
        this.cod = event_id;

        if (TestarConexao.VerificaConexao(activity,this)){

            NetworkConnection.getInstance(activity).conectionVolley(this,(url_event+cod+"/"+userid), Request.Method.GET);
        }

    }

    @Override
    public void openEvent(Evento event) {
        if(event != null)
            eventoDetalheView.showEvent(event);
    }

    @Override
    public void addEventToMyList(Evento event) {
        SharedPreferences shared = activity.getSharedPreferences("shared", Context.MODE_PRIVATE);

        if(shared.contains("userid")){
            if (event.getIsOnUserEvents().equals("0")) {
                this.isAddEvent = true;
                NetworkConnection.getInstance(activity).conectionVolley(this, url_addEvent, Request.Method.POST);
            }else{
                this.isRemoveEvent = true;
                NetworkConnection.getInstance(activity).conectionVolley(this, url_removeEvent, Request.Method.POST);
            }
        }else{
            Log.i("SHARED", shared.getString("userid","nao tem"));
            Intent it = new Intent(activity, LoginActivity.class);
            activity.startActivity(it);
        }
    }

    @Override
    public Map<String, String> doBefore() {
        if(this.isAddEvent || this.isRemoveEvent){
            Map<String, String> params = new HashMap<>();

            params.put("event_id",String.valueOf(this.evento.getId()) );
            params.put("user_id",userid);
            return params;
        }
        return null;
    }

    @Override
    public void doAfter(BaseRequest object) {
        if (!object.isErro()){
            if(!this.isRemoveEvent && !this.isAddEvent) {
                Gson gson = new Gson();
                evento = gson.fromJson(object.getData().getAsJsonObject("evento"), Evento.class);
                this.openEvent(evento);
            }else if(this.isAddEvent || this.isRemoveEvent){
                this.isAddEvent = false;
                this.isRemoveEvent = false;
                Toast.makeText(activity,object.getData().get("mensagem").getAsString(), Toast.LENGTH_SHORT).show();
                this.loadEvent(this.cod);
            }

        }else{
            this.isAddEvent = false;
            this.isRemoveEvent = false;
            Toast.makeText(activity,object.getData().get("mensagem").getAsString(), Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void erroServer(VolleyError error) {
        Log.i("ERRO: "+error.toString(),"");
    }

    @Override
    public void tentarNovamente() {
        if (TestarConexao.VerificaConexao(activity,this)){
            NetworkConnection.getInstance(activity).conectionVolley(this,(url_event+cod), Request.Method.GET);
        }

    }
}