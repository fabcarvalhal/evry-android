package fabriciocarvalhal.com.br.evry.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import fabriciocarvalhal.com.br.evry.R;
import fabriciocarvalhal.com.br.evry.util_conection.BaseRequest;
import fabriciocarvalhal.com.br.evry.util_conection.NetworkConnection;
import fabriciocarvalhal.com.br.evry.util_conection.ResponseConnection;

/**
 * Created by Fabrício Carvalhal on 03/06/2017.
 */

public class LoginActivity extends AppCompatActivity implements ResponseConnection, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private Toolbar mToolbar;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private String url = "http://evry.esy.es/api/login";
    private String id, tipo_conta, email, nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mToolbar = (Toolbar)findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
       // findViewById(R.id.sign_out_button).setOnClickListener(this);
      //  findViewById(R.id.disconnect_button).setOnClickListener(this);


        if (null == savedInstanceState) {
            //initFragment(EventosFragment.newInstance());
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
   // private void initFragment(Fragment notesFragment) {
        // Add the NotesFragment to the layout
        //FragmentManager fragmentManager = getSupportFragmentManager();
        //FragmentTransaction transaction = fragmentManager.beginTransaction();
        //transaction.add(R.id.container_frag, notesFragment);
        //transaction.commit();
  //  }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from
        //   GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            this.handleSignInResult(result);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;


        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("aa", "handleSignInResult:" + result.getStatus());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            /*Intent it = new Intent(this, CadastroActivity.class);

            it.putExtra("nome_usuario", result.getSignInAccount().getDisplayName());
            it.putExtra("email_usuario", result.getSignInAccount().getEmail());
            startActivity(it);*/
            Log.i("Logado como:", result.getSignInAccount().getId());

            this.id = result.getSignInAccount().getId();
            this.email = result.getSignInAccount().getEmail();
            this.nome = result.getSignInAccount().getDisplayName();
            this.tipo_conta = "GOOGLE";
            NetworkConnection.getInstance(this).conectionVolley(this,url,Request.Method.POST);
        } else {

            Log.i("ERRO LOGIN", result.getStatus().toString());

        }
    }


    @Override
    public Map<String, String> doBefore() {
        Map<String, String> params = new HashMap<>();
        params.put("id_conta_delegada",id);
        params.put("tipo",tipo_conta);
        params.put("email",email);
        params.put("nome", nome);
        Log.i("dsd",params.toString());
        return params;

    }

    @Override
    public void doAfter(BaseRequest object) {
        JsonObject j = object.getData().getAsJsonArray("data").getAsJsonObject();
        j.get("mensagem").getAsString();
    }



    @Override
    public void erroServer(VolleyError error) {

    }
}
