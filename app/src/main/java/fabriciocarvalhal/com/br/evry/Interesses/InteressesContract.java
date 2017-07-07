package fabriciocarvalhal.com.br.evry.Interesses;

/**
 * Created by Fabrício Carvalhal on 05/07/2017.
 */

public interface InteressesContract {


    interface mUserActionsListener {
        int getInteressesCount();
        void addInteresse( int id);
        void removeInteresse(int id);
        void showAlertaQtdInteresse();
    }
}
