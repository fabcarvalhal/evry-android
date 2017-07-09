package fabriciocarvalhal.com.br.evry.Eventos;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fabriciocarvalhal.com.br.evry.Login.LoginActivity;
import fabriciocarvalhal.com.br.evry.model.Evento;
import fabriciocarvalhal.com.br.evry.util_conection.BaseRequest;
import fabriciocarvalhal.com.br.evry.util_conection.NetworkConnection;
import fabriciocarvalhal.com.br.evry.util_conection.ResponseConnection;
import fabriciocarvalhal.com.br.evry.util_conection.TentarNovamente;
import fabriciocarvalhal.com.br.evry.util_conection.TestarConexao;

/**
 * Created by Fabrício Carvalhal on 20/05/2017.
 */

public class EventosPresenter implements ResponseConnection, EventosContract.UserActionsListener,TentarNovamente{

    private String url_base = "http://evry.esy.es/api/events/";
    private String url = "";
    private EventosContract.View eventosView;
    private Activity activity;
    private boolean isUpdate, carregando = false, existNextPage = true;
    private int pagina = 0;
    private String userid;
    private boolean isAddEvent = false;
    private String url_addEvent = "http://evry.esy.es/api/event/addToList";
    private boolean isRemoveEvent = false;
    private String url_removeEvent = "http://evry.esy.es/api/event/removeFromList";
    private int addingOrRemovingEventPosition;
    private int addingOrRemovingEventId;

    public EventosPresenter(Activity activity , EventosContract.View eventosView){

        this.activity = activity;
        this.eventosView = eventosView;
        SharedPreferences shared = activity.getSharedPreferences("shared", Context.MODE_PRIVATE);
        this.userid = shared.getString("userid","0");
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

                NetworkConnection.getInstance(activity).conectionVolley(this, url+userid, Request.Method.GET);
            }

    }


    @Override
    public void openEventDetails(@NonNull Evento requestedEvent,int position) {
        eventosView.showEventDetailUi(requestedEvent.getId(),position);
    }

    @Override
    public void addEventToMyList(Evento ev, int position) {
        SharedPreferences shared = activity.getSharedPreferences("shared", Context.MODE_PRIVATE);

        if(shared.contains("userid")){
            this.addingOrRemovingEventPosition = position;
            this.addingOrRemovingEventId = ev.getId();
            if (ev.getIsOnUserEvents().equals("0")) {
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

            params.put("event_id",String.valueOf(this.addingOrRemovingEventId) );
            params.put("user_id",userid);
            return params;
        }

        return null;
    }



    @Override
    public void doAfter(BaseRequest object) {

        if (!object.isErro()){

            if (!this.isAddEvent && !this.isRemoveEvent) {
                Gson gson = new Gson();
                List<Evento> eventosList = new ArrayList<>();

                Evento[] eventos = gson.fromJson(object.getData().getAsJsonArray("eventos"), Evento[].class);
                for (Evento e : eventos) {
                    e.setId(e.getId());
                    e.setNome(e.getNome());
                    e.setData_fim(e.getData_fim());
                    e.setData_ini(e.getData_ini());

                    eventosList.add(e);
                }
                if (isUpdate) {
                    eventosView.showEvents(eventosList);
                } else {

                    eventosView.moreEvents(eventosList);
                }

                this.existNextPage = object.getHasNextPage();
                carregando = false;
                eventosView.setProgressIndicator(false);
            }else if(this.isRemoveEvent || this.isAddEvent){
                if(this.isRemoveEvent){
                    eventosView.updateItemCheckMark(addingOrRemovingEventPosition,false);
                }else if(this.isAddEvent){
                    eventosView.updateItemCheckMark(addingOrRemovingEventPosition,true);
                }

                this.isAddEvent = false;
                this.isRemoveEvent = false;
                Toast.makeText(activity,object.getData().get("mensagem").getAsString(), Toast.LENGTH_SHORT).show();

            }
        }else{
            this.isAddEvent = false;
            this.isRemoveEvent = false;
            Toast.makeText(activity,object.getData().get("mensagem").getAsString(), Toast.LENGTH_SHORT).show();
        }

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