package fabriciocarvalhal.com.br.evry.DetalhesEvento;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import fabriciocarvalhal.com.br.evry.R;
import fabriciocarvalhal.com.br.evry.model.Evento;

/**
 * Created by Fabrício Carvalhal on 21/05/2017.
 */

public class DetalhesEventoFragment extends Fragment implements  DetalhesEventoContract.View {

    private static final int EVENT_ID = 0;
    private DetalhesEventoContract.UserActionsListener mUserActionsListener;
    private View view;
    private ImageView img_book;
    private TextView tv_titulo,tv_data,tv_desc;
    private FloatingActionButton fabbtnAddOrRemoveEvent;
    private int event_id;
    private Evento evento;


    public DetalhesEventoFragment() {
        // Required empty public constructor

    }

    public static DetalhesEventoFragment newInstance(int ev) {
        DetalhesEventoFragment fragment = new DetalhesEventoFragment();
        Bundle args = new Bundle();
        args.putInt("EVENT_ID",ev);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserActionsListener = new DetalhesEventoPresenter(getActivity(),this);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detalhe_eventos, container, false);

        initViews(view);

        return view;
    }

    private void initViews(View view) {
        img_book = (ImageView) view.findViewById(R.id.img_book);
        tv_titulo = (TextView) view.findViewById(R.id.tv_titulo);
        tv_desc = (TextView) view.findViewById(R.id.tv_desc);
        tv_data = (TextView) view.findViewById(R.id.tv_data);
        fabbtnAddOrRemoveEvent = (FloatingActionButton) view.findViewById(R.id.btnAddOrRemoveEvent);
        fabbtnAddOrRemoveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserActionsListener.addEventToMyList(evento);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {

            mUserActionsListener.loadEvent(getArguments().getInt("EVENT_ID"));
        }
    }


    @Override
    public void showEvent(Evento event) {

        if (event != null) {
            this.evento = event;

            Glide.with(getActivity())
                    .load(event.getImagem())
                    .fitCenter().crossFade()
                    .into(img_book);
            tv_titulo.setText(event.getNome());
            tv_desc.setText(event.getDetalhes());
            tv_data.setText(event.getData_ini() + " à " + event.getData_fim());




            if(event.getIsOnUserEvents().equals("1")){



                    fabbtnAddOrRemoveEvent.setImageDrawable(getResources().getDrawable(R.drawable.bookmark_check));

            }else{

                    fabbtnAddOrRemoveEvent.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_grey600_24dp));

            }


        }
    }
}