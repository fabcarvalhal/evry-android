package fabriciocarvalhal.com.br.evry.Eventos;

/**
 * Created by Fabrício Carvalhal on 20/05/2017.
 */


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;
import java.util.List;

import fabriciocarvalhal.com.br.evry.DetalhesEvento.DetalhesEventoActivity;
import fabriciocarvalhal.com.br.evry.R;
import fabriciocarvalhal.com.br.evry.adapters.DrawerAdapter;
import fabriciocarvalhal.com.br.evry.adapters.EventosAdapter;
import fabriciocarvalhal.com.br.evry.model.Curso;
import fabriciocarvalhal.com.br.evry.model.Evento;


public class EventosFragment extends Fragment implements EventosContract.View, GoogleApiClient.OnConnectionFailedListener{


    private EventosContract.UserActionsListener mUserActionsListener;
    private RecyclerView mRecyclerView;
    private ProgressBar books_progress;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private EventosAdapter adapter;
    private boolean preenchido = false;
    private View view;
    private DrawerAdapter drawerAdapter;
    private RecyclerView mDrawerRecycleView;
    private GoogleApiClient mGoogleApiClient;
    private ImageView imUser;
    private TextView nomeUser;


    public EventosFragment() {
        // Required empty public constructor
    }


    public static EventosFragment newInstance() {
        EventosFragment fragment = new EventosFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mUserActionsListener = new EventosPresenter(getActivity(),this);
        adapter = new EventosAdapter(getActivity(),new ArrayList<Evento>(),mUserActionsListener);
        drawerAdapter = new DrawerAdapter(getActivity(),new ArrayList<Curso>(),mUserActionsListener);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_eventos, container, false);

        initReciclerview(view);

        return view;
    }

    private void initReciclerview(View view) {


        books_progress = (ProgressBar) view.findViewById(R.id.books_progress);

        //Configurando SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_books);
        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mUserActionsListener.loadEvents(true,true);
            }
        });



        //configurando RecyclerView
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerView_books);
        mDrawerRecycleView = (RecyclerView) getActivity().findViewById(R.id.left_drawer_lista);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();

                if ((adapter.getItemCount()) <= llm.findLastCompletelyVisibleItemPosition() + 1 ){

                    mUserActionsListener.loadEvents(false,false);

                }
            }

        });

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(llm);

        mRecyclerView.setAdapter(adapter);
        imUser = (ImageView) getActivity().findViewById(R.id.imguser);
        nomeUser = (TextView) getActivity().findViewById(R.id.nomeuser);

        LinearLayoutManager llm2 = new LinearLayoutManager(getActivity());
        llm2.setOrientation(LinearLayoutManager.VERTICAL);

        mDrawerRecycleView.setLayoutManager(llm2);
        mDrawerRecycleView.setAdapter(drawerAdapter);


    }


    @Override
    public void setProgressIndicator(boolean active) {

        if (active){

            books_progress.setVisibility(View.VISIBLE);

        }else{

            if (mSwipeRefreshLayout.isRefreshing()){
                mSwipeRefreshLayout.setRefreshing(false);
            }

            books_progress.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showEvents(List<Evento> books) {
        adapter.replaceData(books);


    }

    @Override
    public void moreEvents(List<Evento> books) {
        for (Evento b : books){
            adapter.addListaItem(b,adapter.getItemCount());
        }
    }


    @Override
    public void showEventDetailUi(int noteId,int position) {

        Intent detalhe =  new Intent(getActivity(), DetalhesEventoActivity.class);
        detalhe.putExtra("event_id", (adapter.getItem(position).getId()));

        startActivity(detalhe);

    }

    @Override
    public void updateItemCheckMark(int position, boolean status) {
            if(status){

                adapter.getItem(position).setIsOnUserEvents("1");
            }else{
                adapter.getItem(position).setIsOnUserEvents("0");
            }
            adapter.updateImage(position);

    }

    @Override
    public void showMenuItens(ArrayList<Curso> cursos) {
        drawerAdapter.replaceData(cursos);
    }

    @Override
    public void closeDrawer() {
        ((EventosActivity) getActivity()).closeDrawer();
    }

    @Override
    public void changeNavTitle(String title) {
        ((EventosActivity) getActivity()).setNavTitle(title);
    }

    @Override
    public void logout() {


        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if(status.isSuccess()){
                            SharedPreferences sharedPref = getActivity().getSharedPreferences("shared",0);
                            sharedPref.edit().clear().apply();
                            mUserActionsListener.loadMenuCursos();
                        }


                    }
                });
    }

    @Override
    public void loadMenuHeader() {

        SharedPreferences shared = getActivity().getSharedPreferences("shared",0);
        if(shared.contains("userid")){

            if(!shared.getString("urlPhoto", "").equals("")){
                Glide.with(getActivity()).
                        load(shared.getString("urlPhoto","")).
                        centerCrop().
                        crossFade().
                        into(this.imUser);
            }else{
                imUser.setBackgroundResource(R.drawable.user);
            }

            Log.i("IMG", shared.getString("urlPhoto","nao tem"));
            nomeUser.setText(shared.getString("user_name",""));
        }else{
            nomeUser.setText("");
            imUser.setBackgroundResource(R.drawable.user);
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        if (!preenchido) {

            mUserActionsListener.loadEvents(true, false);
            mUserActionsListener.loadMenuCursos();
            preenchido = true;

        }

        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared",0);
        if (sharedPref.contains("userid")){
            mUserActionsListener.loadMenuCursos();

        }
        mUserActionsListener.loadDrawerHeader();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}