package fabriciocarvalhal.com.br.evry.DetalhesEvento;

import android.support.annotation.Nullable;

import fabriciocarvalhal.com.br.evry.model.Evento;

/**
 * Created by Fabrício Carvalhal on 21/05/2017.
 */


public interface DetalhesEventoContract {

    interface View {


        void showEvent(Evento event);
    }

    interface UserActionsListener {

        void loadEvent(int event_id);
        void openEvent(Evento event);
        void addEventToMyList(Evento event);

    }

}