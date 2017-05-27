package fabriciocarvalhal.com.br.evry.model;

/**
 * Created by Fabrício Carvalhal on 20/05/2017.
 */

import java.util.List;


public class ResponseEventos {

    private boolean erro;
    private boolean existNexPage;
    private List<Evento> data;

    public ResponseEventos(boolean erro, boolean existNexPage, List<Evento> data) {
        this.erro = erro;
        this.existNexPage = existNexPage;
        this.data = data;
    }


    public boolean isErro() {
        return erro;
    }

    public void setErro(boolean erro) {
        this.erro = erro;
    }

    public boolean isExistNexPage() {
        return existNexPage;
    }

    public void setExistNexPage(boolean existNexPage) {
        this.existNexPage = existNexPage;
    }

    public List<Evento> getData() {
        return data;
    }

    public void setData(List<Evento> data) {
        this.data = data;
    }
}