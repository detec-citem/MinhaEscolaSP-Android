package br.gov.sp.educacao.minhaescola.view;

import android.os.Build;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import br.gov.sp.educacao.minhaescola.R;

import br.gov.sp.educacao.minhaescola.adapter.HistoricoAdapter;

import br.gov.sp.educacao.minhaescola.banco.TurmaQueries;
import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;

import br.gov.sp.educacao.minhaescola.model.Aluno;
import br.gov.sp.educacao.minhaescola.model.Turma;

import br.gov.sp.educacao.minhaescola.util.MyPreferences;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

public class HistoricoMatriculaActivity
        extends AppCompatActivity {

    private MyPreferences mPref;
    private ArrayList<Turma> turmas;

    public @BindView(R.id.historico_btnVoltar) ImageView btnVoltar;
    public @BindView(R.id.historico_listMatriculas) ListView listaMatriculas;

    private Aluno aluno;

    private UsuarioQueries usuarioQueries;
    private TurmaQueries turmaQueries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_historico_matricula);

        ButterKnife.bind(this);

        Window window = this.getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul_background_sobre_mim));
        }

        contentOnCreate();
    }

    @OnClick(R.id.historico_btnVoltar)
    public void onClick() {

        onBackPressed();
    }

    private void contentOnCreate() {

        mPref = new MyPreferences(getApplicationContext());

        usuarioQueries = new UsuarioQueries(getApplicationContext());

        turmaQueries = new TurmaQueries(getApplicationContext());


        if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

            int cd_aluno = getIntent().getIntExtra("cd_aluno", 0);

            aluno = usuarioQueries.getAluno(cd_aluno);
        }
        else {

            aluno = usuarioQueries.getAluno();
        }

        turmas = turmaQueries.getTurmas(aluno.getCd_aluno());

        Collections.sort(turmas);

        HistoricoAdapter historicoAdapter = new HistoricoAdapter(turmas, this);

        listaMatriculas.setAdapter(historicoAdapter);
    }
}
