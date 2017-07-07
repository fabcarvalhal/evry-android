package fabriciocarvalhal.com.br.evry.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;
import java.util.Map;

import fabriciocarvalhal.com.br.evry.Cadastro.CadastroActivity;
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
    private String id, email, nome,urlPhoto,tipo_conta;

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

            Log.i("Logado como:", result.getSignInAccount().getId());

            this.id = result.getSignInAccount().getId();
            this.email = result.getSignInAccount().getEmail();
            this.nome = result.getSignInAccount().getDisplayName();
            this.tipo_conta = "1";
            Uri accimg = result.getSignInAccount().getPhotoUrl();
            this.urlPhoto = accimg != null ? accimg.toString() : null;
            NetworkConnection.getInstance(this).conectionVolley(this,url,Request.Method.POST);
        } else {
            Toast.makeText(this,"Não foi possível fazer login", Toast.LENGTH_SHORT);
            Log.i("ERRO LOGIN", result.getStatus().toString());

        }
    }


    @Override
    public Map<String, String> doBefore() {
        Map<String, String> params = new HashMap<>();
        params.put("id_conta_delegada",id);
        params.put("tipo",String.valueOf(tipo_conta));
        params.put("email",email);
        params.put("nome", nome);
        Log.i("dsd",params.toString());
        return params;

    }

    @Override
    public void doAfter(BaseRequest object) {

        if(object.isErro()){
            String mensagem = object.getData().get("mensagem").getAsString();
            if(mensagem.equals("Usuário não encontrado")){
                Intent it = new Intent(this, CadastroActivity.class);
                it.putExtra("nome",this.nome);
                it.putExtra("tipo",this.tipo_conta);
                it.putExtra("email",this.email);
                it.putExtra("idgoogle",this.id);
                it.putExtra("urlPhoto",this.urlPhoto);
                startActivity(it);
                finish();
            }

        }else{

            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("shared",0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("userid", object.getData().get("userid").getAsString());
            editor.putString("user_name", this.nome);
            editor.putString("urlPhoto",this.urlPhoto);
            editor.commit();
            finish();
        }


    }



    @Override
    public void erroServer(VolleyError error) {

    }
}
