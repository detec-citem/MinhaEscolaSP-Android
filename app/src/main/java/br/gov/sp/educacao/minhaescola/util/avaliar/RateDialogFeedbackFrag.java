package br.gov.sp.educacao.minhaescola.util.avaliar;

import android.content.Intent;

import android.net.Uri;

import android.os.Bundle;

import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.Toast;

import br.gov.sp.educacao.minhaescola.BuildConfig;
import br.gov.sp.educacao.minhaescola.R;
import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;

import br.gov.sp.educacao.minhaescola.util.MyPreferences;
import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

public class RateDialogFeedbackFrag
        extends RateDialogFrag {

    private static final String RATING_KEY = "ratingMinhaEscola";

    private float rating;

    private UsuarioQueries usuarioQueries;

    private String user;

    private MyPreferences mPref;

    public @BindView(R.id.et_feedback) EditText etFeedback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rate_dialog_feedback, container);

        //etFeedback = (EditText) view.findViewById(R.id.et_feedback);

        ButterKnife.bind(this, view);

        usuarioQueries = new UsuarioQueries(getContext());

        mPref = new MyPreferences(getContext());

        MyPreferences mPref = new MyPreferences(getDialog().getContext());

        if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL && usuarioQueries.getResponsavel() != null){

            user = usuarioQueries.getResponsavel().getLogin();
        }
        else if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_ALUNO && usuarioQueries.getAluno() != null){

            user = usuarioQueries.getAluno().getLogin();
        }
        //View bt = view.findViewById(R.id.bt_no);
        //bt.setOnClickListener(this);

        //bt = view.findViewById(R.id.bt_send);
        //bt.setOnClickListener(this);

        if( savedInstanceState != null ) {

            rating = savedInstanceState.getFloat(RATING_KEY);
        }

        return view;
    }

    public void setRating( float rating ) {

        this.rating = rating;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putFloat( RATING_KEY, rating );

        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.bt_send)
    public void onClick() {

        String feedback = etFeedback.getText().toString();

        if(feedback.length() > 0 ) {

            Intent intent = new Intent(Intent.ACTION_SENDTO);

            String app_version = BuildConfig.VERSION_NAME;

            intent.setType("text/plain");
            intent.setData(Uri.parse("mailto:"));

            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"seesp.mobile@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Avaliação do aplicativo Minha Escola SP");
            intent.putExtra(Intent.EXTRA_TEXT, "Estrelas: " + rating + "\n\nAvaliação: " + feedback + "\n\nLogin: " + user + "\n\nVersão do app: " + app_version);

            getActivity().startActivity(Intent.createChooser(intent, "Enviar e-mail"));
        }
        else {

            Toast.makeText( getActivity(), "Entre com o feedback", Toast.LENGTH_SHORT ).show();

            return;
        }
        dismiss();
    }

    @OnClick(R.id.bt_no)
    public void btNoOnClick() {

        dismiss();
    }

    /*@Override
    public void onClick(View view) {

        String feedback = etFeedback.getText().toString();

        if( view.getId() == R.id.bt_send && feedback.length() > 0 ) {

            Intent intent = new Intent(Intent.ACTION_SENDTO);

            intent.setType("text/plain");
            intent.setData(Uri.parse("mailto:"));

            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"seesp.mobile@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Avaliação do aplicativo Minha Escola SP");
            intent.putExtra(Intent.EXTRA_TEXT, "Estrelas: " + rating + "\n\nAvaliação: " + feedback + "\n\nLogin: " + user);

            getActivity().startActivity(Intent.createChooser(intent, "Enviar e-mail"));
        }
        else if( view.getId() == R.id.bt_send ) {

            Toast.makeText( getActivity(), "Entre com o feedback", Toast.LENGTH_SHORT ).show();

            return;
        }
        dismiss();
    }*/
}
