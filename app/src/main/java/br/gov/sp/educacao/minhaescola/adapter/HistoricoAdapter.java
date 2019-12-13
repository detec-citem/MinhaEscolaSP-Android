package br.gov.sp.educacao.minhaescola.adapter;

import android.app.Activity;

import android.content.Intent;

import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import br.gov.sp.educacao.minhaescola.R;
import br.gov.sp.educacao.minhaescola.model.Turma;
import br.gov.sp.educacao.minhaescola.view.DetalheMatriculaActivity;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

public class HistoricoAdapter
        extends BaseAdapter {

    private final List<Turma> turmas;

    private final Activity act;

    //private Turma turma;

    private View mView;

    public @BindView(R.id.historico_item_txtAno) TextView txtAno;
    public @BindView(R.id.historico_item_txtEscola) TextView txtEscola;
    public @BindView(R.id.historico_item_txtTurma) TextView txtTurma;
    public @BindView(R.id.historico_item_layout) LinearLayout layout;

    public HistoricoAdapter(List<Turma> turmas, Activity act) {

        this.turmas = turmas;

        this.act = act;
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

        mView = act.getLayoutInflater().inflate(R.layout.item_historico_list, viewGroup, false);

        final Turma turma = turmas.get(i);

        ButterKnife.bind(this, mView);

        txtAno.setText(String.valueOf(turma.getAno_letivo()));
        txtEscola.setText(turma.getNome_escola());
        txtTurma.setText(turma.getNome_turma());

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mView.getContext(), DetalheMatriculaActivity.class);

                intent.putExtra("cd_turma", turma.getCd_turma());

                mView.getContext().startActivity(intent);
            }
        });

        return mView;
    }

    /*@OnClick(R.id.historico_item_layout)
    public void onClick() {

        Intent intent = new Intent(mView.getContext(), DetalheMatriculaActivity.class);

        intent.putExtra("cd_turma", turma.getCd_turma());

        mView.getContext().startActivity(intent);
    }*/
}
