package fabriciocarvalhal.com.br.evry.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Fabrício Carvalhal on 20/05/2017.
 */


public class Evento  {
    private int id;
    private String nome;
    private String imagem;
    private String data_ini;
    private String data_fim;
    private String local;
    private String publicado;
    private String isOnUserEvents;
    private String detalhes;

    public Evento(){

    }

    public Evento(int id, String nome, String imagem, String data_ini, String data_fim, String local,String publicado,String isOnUserEvents,String detalhes) {
        this.setId(id);
        this.setNome(nome);
        this.setImagem(imagem);
        this.setData_ini(data_ini);
        this.setData_fim(data_fim);
        this.setLocal(local);
        this.setPublicado(publicado);
        this.setIsOnUserEvents(isOnUserEvents);
        this.setDetalhes(detalhes);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getData_ini() {
        return data_ini;
    }

    public void setData_ini(String data_ini) {
        this.data_ini = data_ini;
    }

    public String getData_fim() {
        return data_fim;
    }

    public void setData_fim(String data_fim) {
        this.data_fim = data_fim;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getPublicado() {
        return publicado;
    }

    public void setPublicado(String publicado) {
        this.publicado = publicado;
    }

    public String getIsOnUserEvents() {
        return isOnUserEvents;
    }

    public void setIsOnUserEvents(String isOnUserEvents) {
        this.isOnUserEvents = isOnUserEvents;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }


}