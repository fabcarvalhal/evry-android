package fabriciocarvalhal.com.br.evry.util_conection;

import com.google.gson.JsonObject;

import org.json.JSONObject;

/**
 * Created by rafael on 23/03/17.
 */

public class BaseRequest {

    private boolean error;
    private JsonObject data;
    private JSONObject jsonObject;
    private boolean hasNextPage;

    public boolean isErro() {
        return error;
    }

    public void setErro(boolean erro) {
        this.error = erro;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

    public boolean getHasNextPage(){
        return this.hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage){this.hasNextPage = hasNextPage;}
    @Override
    public String toString() {
        return "BaseRequest{" +
                "erro=" + error +
                ", data=" + data +
                '}';
    }

    public void setData(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }
}
