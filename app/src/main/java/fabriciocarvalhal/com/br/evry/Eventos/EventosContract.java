package fabriciocarvalhal.com.br.evry.Eventos;

import android.support.annotation.NonNull;
import fabriciocarvalhal.com.br.evry.model.Evento;
import java.util.List;

/**
 * * Created by Fabrício Carvalhal on 20/05/2017.
 */


public interface EventosContract {

    interface View{

        void setProgressIndicator(boolean active);

        void showEvents(List<Evento> eventos);

        void moreEvents(List<Evento> eventos);

        void showEventDetailUi(int noteId,int position);

    }

    interface UserActionsListener {

        void loadEvents(boolean forceUpdate,boolean fromRefresh);


        void openEventDetails(@NonNull Evento requestedEvent,int position);
    }
}