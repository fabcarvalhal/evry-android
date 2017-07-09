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
import fabriciocarvalhal.com.br.evry.model.Curso;
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
    private String url_filtered = "http://evry.esy.es/api/events_bycourse/";
    private int filteredByCourse = 0;
    private boolean isFiltered = false;
    private String url_menuItens = "http://evry.esy.es/api/cursos";
    private boolean isMenuLoad = false;
    private boolean isEventLoad=  false;

    public EventosPresenter(Activity activity , EventosContract.View eventosView){

        this.activity = activity;
        this.eventosView = eventosView;
        SharedPreferences shared = activity.getSharedPreferences("shared", Context.MODE_PRIVATE);
        this.userid = shared.getString("userid","0");

    }

    @Override
    public void loadEvents(boolean forceUpdate,boolean fromRefresh) {
        isUpdate = forceUpdate;
        isEventLoad = true;
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
                if (isFiltered) {
                    NetworkConnection.getInstance(activity).conectionVolley(new ResponseConnection() {
                        @Override
                        public Map<String, String> doBefore() {
                            return null;
                        }

                        @Override
                        public void doAfter(BaseRequest object) {
                            if(!object.isErro()) {
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

                                existNextPage = object.getHasNextPage();
                                carregando = false;
                                isEventLoad = false;
                                eventosView.setProgressIndicator(false);
                            }
                        }

                        @Override
                        public void erroServer(VolleyError error) {

                        }
                    }, url_filtered + userid + "/" + filteredByCourse + "/" + pagina, Request.Method.GET);
                } else {
                    NetworkConnection.getInstance(activity).conectionVolley(new ResponseConnection() {
                        @Override
                        public Map<String, String> doBefore() {
                            return null;
                        }

                        @Override
                        public void doAfter(BaseRequest object) {
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

                            existNextPage = object.getHasNextPage();
                            carregando = false;
                            isEventLoad = false;
                            eventosView.setProgressIndicator(false);
                        }

                        @Override
                        public void erroServer(VolleyError error) {

                        }
                    }, url + userid + "/" + pagina, Request.Method.GET);

                }
            }
    }

    @Override
    public void loadMenuCursos() {
        if(TestarConexao.VerificaConexao(activity,this)) {
            this.isMenuLoad = true;
            NetworkConnection.getInstance(activity).conectionVolley(this, url_menuItens, Request.Method.GET);
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
    public void filterEventsByCourse(int id) {
        if(id == 0 ){
            this.isFiltered = false;
        }else if(id > 0){
            this.isFiltered = true;
        }
        this.filteredByCourse = id;
        this.loadEvents(true,false);
    }

    @Override
    public void closeDrawer() {
        eventosView.closeDrawer();
    }

    @Override
    public void changeNavTitle(String title) {
        eventosView.changeNavTitle(title);
    }

    @Override
    public void logout() {
        this.userid = "0";
        eventosView.logout();
    }

    @Override
    public void loadDrawerHeader() {
        eventosView.loadMenuHeader();
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

//            if (!this.isAddEvent && !this.isRemoveEvent && !this.isMenuLoad && isEventLoad) {
//
//            }else
            if(this.isRemoveEvent || this.isAddEvent){
                if(this.isRemoveEvent){
                    eventosView.updateItemCheckMark(addingOrRemovingEventPosition,false);
                }else if(this.isAddEvent){
                    eventosView.updateItemCheckMark(addingOrRemovingEventPosition,true);
                }

                this.isAddEvent = false;
                this.isRemoveEvent = false;
                Toast.makeText(activity,object.getData().get("mensagem").getAsString(), Toast.LENGTH_SHORT).show();

            }else if(isMenuLoad && !isEventLoad){
                Gson gson = new Gson();
                ArrayList<Curso> cursosList = new ArrayList<>();
                Curso[] cursos = gson.fromJson(object.getData().getAsJsonArray("cursos"), Curso[].class);
                cursosList.add(new Curso(0,"Home"));
                for(Curso c : cursos){
                    c.setId(c.getId());
                    c.setNome(c.getNome());
                    cursosList.add(c);
                }
                if(!userid.equals("0")){
                    cursosList.add(new Curso(-1,"Sair"));
                }
                this.isMenuLoad = false;
                eventosView.showMenuItens(cursosList);
            }
        }else{
            this.isAddEvent = false;
            this.isRemoveEvent = false;
            this.isMenuLoad = false;
            this.isFiltered = false;
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
            if(this.isMenuLoad){
                NetworkConnection.getInstance(activity).conectionVolley(this, url_menuItens, Request.Method.GET);
            }
        }
    }




}