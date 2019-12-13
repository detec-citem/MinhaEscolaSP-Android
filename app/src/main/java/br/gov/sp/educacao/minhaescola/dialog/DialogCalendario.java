package br.gov.sp.educacao.minhaescola.dialog;

import android.app.Dialog;
import android.app.DialogFragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;

import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.Calendar;

import br.gov.sp.educacao.minhaescola.R;
import br.gov.sp.educacao.minhaescola.model.DiasEventos;

import static br.gov.sp.educacao.minhaescola.util.CaldroidFragmentListener.isAvaliacao;

public class DialogCalendario
        extends DialogFragment {;

    private AlertDialog mAlertDialog;

    public static final String TAG = "DialogCalendario";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View view = inflater.inflate(R.layout.fragment_dialog_calendario, null, false);

        return inicializaAlertDialog(view);
    }

    private AlertDialog inicializaAlertDialog(final View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Bundle bundle = getArguments();

        ArrayList<DiasEventos> listaEventos = bundle.getParcelableArrayList("listaEventos");

        final int anoAtual = Calendar.getInstance().get(Calendar.YEAR);

        String dataFormart = listaEventos.get(0).getDia() + "/"+ listaEventos.get(0).getMes() + "/" + anoAtual;

        builder.setTitle(adicionaZeroNasDatas(dataFormart));

        if(listaEventos.size() > 1) {

            String mensagem = "";

            for(int i = 0; i < listaEventos.size(); i++) {

                if(isAvaliacao(listaEventos.get(i).getNomeEvento())) {

                    mensagem = mensagem + listaEventos.get(i).getNomeEvento() + ": " +
                               listaEventos.get(i).getNomeDisciplina() + "\n" + "\n";
                }

                mensagem = mensagem + listaEventos.get(i).getDescricaoEvento() + "\n" + "\n";
            }
            builder.setMessage(mensagem);
        }
        else {

            builder.setMessage(listaEventos.get(0).getDescricaoEvento());
        }

        builder.setView(view);

        mAlertDialog = builder.create();

        mAlertDialog.show();

        return mAlertDialog;
    }

    private String adicionaZeroNasDatas(String data) {

        String [] dataSeparada = data.split("[/]");

        if(dataSeparada[0].length() == 1) {

            dataSeparada[0] = "0" + dataSeparada[0];
        }

        if(dataSeparada[1].length() == 1) {

            dataSeparada[1] = "0" + dataSeparada[1];
        }

        return dataSeparada[0] + "/" + dataSeparada[1] + "/" + dataSeparada[2];
    }
}
