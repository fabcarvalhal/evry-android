package fabriciocarvalhal.com.br.evry.Eventos;

/**
 * Created by Fabrício Carvalhal on 20/05/2017.
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import fabriciocarvalhal.com.br.evry.DetalhesEvento.DetalhesEventoActivity;
import fabriciocarvalhal.com.br.evry.model.Evento;
import fabriciocarvalhal.com.br.evry.R;
import fabriciocarvalhal.com.br.evry.adapters.EventosAdapter;

import java.util.ArrayList;
import java.util.List;


public class EventosFragment extends Fragment implements EventosContract.View{


    private EventosContract.UserActionsListener mUserActionsListener;
    private RecyclerView mRecyclerView;
    private ProgressBar books_progress;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private EventosAdapter adapter;
    private boolean preenchido = false;
    private View view;

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
        mUserActionsListener = new EventosPresenter(getActivity(),this);
        adapter = new EventosAdapter(getActivity(),new ArrayList<Evento>(),mUserActionsListener);
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
    public void onResume() {
        super.onResume();
        if (!preenchido) {
            mUserActionsListener.loadEvents(true, false);
            preenchido = true;
        }
    }

}