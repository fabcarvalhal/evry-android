package fabriciocarvalhal.com.br.evry.Interesses;

import fabriciocarvalhal.com.br.evry.model.Interesse;

/**
 * Created by Fabrício Carvalhal on 05/07/2017.
 */

public interface InteressesContract {

    interface View{
        void addInteresse(Interesse i);
    }

    interface mUserActionsListener {
        void addInteresse( int id);
        void removeInteresse(int id);
    }
}
