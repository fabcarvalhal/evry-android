package fabriciocarvalhal.com.br.evry.util_conection;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by rafael on 08/09/15.
 */
public interface ResponseConnection {

    Map<String,String> doBefore();

    void doAfter(BaseRequest object);

    void erroServer(VolleyError error);
}
