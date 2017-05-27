package fabriciocarvalhal.com.br.evry.Eventos;


import android.support.annotation.NonNull;
import android.util.Log;

import fabriciocarvalhal.com.br.evry.model.Evento;
import android.app.Activity;
import fabriciocarvalhal.com.br.evry.model.ResponseEventos;
import fabriciocarvalhal.com.br.evry.util_conection.BaseRequest;
import fabriciocarvalhal.com.br.evry.util_conection.TentarNovamente;
import fabriciocarvalhal.com.br.evry.util_conection.TestarConexao;
import fabriciocarvalhal.com.br.evry.util_conection.NetworkConnection;
import fabriciocarvalhal.com.br.evry.util_conection.ResponseConnection;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Fabrício Carvalhal on 20/05/2017.
 */

public class EventosPresenter implements ResponseConnection, EventosContract.UserActionsListener,TentarNovamente{

    private String url_base = "http://evry.esy.es/api/events/1/";
    private String url = "";
    private EventosContract.View eventosView;
    private Activity activity;
    private boolean isUpdate, carregando = false, existNextPage = true;
    private int pagina = 0;

    public EventosPresenter(Activity activity , EventosContract.View eventosView){

        this.activity = activity;
        this.eventosView = eventosView;
    }

    @Override
    public void loadEvents(boolean forceUpdate,boolean fromRefresh) {
        isUpdate = forceUpdate;

        if(forceUpdate){
            pagina = 0;
            existNextPage = true;
        }else{
            pagina++;
        }

        url = url_base+pagina;
        if (existNextPage)
            if (TestarConexao.VerificaConexao(activity, this) && !carregando) {

                if (!fromRefresh)
                    eventosView.setProgressIndicator(true);

                carregando = true;

                NetworkConnection.getInstance(activity).conectionVolley(this, url, Request.Method.GET);
            }

    }


    @Override
    public void openEventDetails(@NonNull Evento requestedEvent,int position) {
        eventosView.showEventDetailUi(requestedEvent.getId(),position);
    }

    @Override
    public Map<String, String> doBefore() {
        return null;
    }



    @Override
    public void doAfter(BaseRequest object) {

        if (!object.isErro()){
            Gson gson = new Gson();
            List<Evento> eventosList = new ArrayList<>();

            Evento[] eventos = gson.fromJson(object.getData().getAsJsonArray("eventos"),Evento[].class);
            for (Evento e : eventos){
                e.setId(e.getId());
                e.setNome(e.getNome());
                e.setData_fim(e.getData_fim());
                e.setData_ini(e.getData_ini());

                eventosList.add(e);
            }
            if (isUpdate) {
                eventosView.showEvents(eventosList);
            }else {

                eventosView.moreEvents(eventosList);
            }
        }

        carregando = false;
        eventosView.setProgressIndicator(false);
    }




    @Override
    public void erroServer(VolleyError error) {
        eventosView.setProgressIndicator(false);
        TestarConexao.calldialog(activity,this);
        carregando = false;
    }


    public void tentarNovamente() {

        if (TestarConexao.VerificaConexao(activity,this)) {

            if (!isUpdate)
                eventosView.setProgressIndicator(true);

            carregando = true;
            NetworkConnection.getInstance(activity).conectionVolley(this, url, Request.Method.GET);
        }
    }


}