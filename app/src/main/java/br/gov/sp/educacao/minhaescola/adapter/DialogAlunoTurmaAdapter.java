package br.gov.sp.educacao.minhaescola.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.gov.sp.educacao.minhaescola.R;
import br.gov.sp.educacao.minhaescola.model.Turma;

public class DialogAlunoTurmaAdapter  extends BaseAdapter {

    private Activity act;

    private ArrayList<Turma> turmas;


    public DialogAlunoTurmaAdapter(Activity act, ArrayList<Turma> turmas) {

        this.act = act;

        this.turmas = turmas;
    }

    @Override
    public int getCount() {

        return turmas.size();
    }

    @Override
    public Object getItem(int i) {

        return turmas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = act.getLayoutInflater().inflate(R.layout.layout_spinner_dialog_turma, viewGroup, false);

        Turma turma = turmas.get(i);

        final TextView txtNomeTurma = v.findViewById(R.id.dialog_item_turma);

        final TextView txtNomeEscola = v.findViewById(R.id.dialog_item_escola);

        txtNomeTurma.setText(turma.getNome_turma());

        txtNomeEscola.setText(turma.getNome_escola());

        return v;
    }
}
