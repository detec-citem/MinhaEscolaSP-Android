package br.gov.sp.educacao.minhaescola.view;

import android.os.Build;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.gov.sp.educacao.minhaescola.R;

import br.gov.sp.educacao.minhaescola.adapter.NotasAdapter;

import br.gov.sp.educacao.minhaescola.banco.NotasQueries;
import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;

import br.gov.sp.educacao.minhaescola.model.Aluno;
import br.gov.sp.educacao.minhaescola.model.Nota;

import br.gov.sp.educacao.minhaescola.util.MyPreferences;

import butterknife.ButterKnife;
import butterknife.BindView;

public class NotasActivity
        extends AppCompatActivity {

    private ArrayList<Nota> listaNotas;

    private MyPreferences mPref;

    private int bimestreSelecionado;

    public @BindView(R.id.notas_txtBimestre) TextView txtbimestre;

    public @BindView(R.id.notas_lvNotas) ListView lvNotas;

    private UsuarioQueries usuarioQueries;

    private NotasQueries notasQueries;

    private NotasAdapter notasAdapter;

    private Aluno aluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notas);

        ButterKnife.bind(this);

        Window window = this.getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul_minhas_notas));
        }

        usuarioQueries = new UsuarioQueries(this);

        notasQueries = new NotasQueries(this);

        aluno = usuarioQueries.getAluno(getIntent().getIntExtra("cd_aluno", 0));

        bimestreSelecionado = notasQueries.maiorBimestre();

        contentOnCreate();
    }

    private void contentOnCreate() {

        mPref = new MyPreferences(this);

        if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

            aluno = usuarioQueries.getAluno(getIntent().getIntExtra("cd_aluno", 0));
        }
        else {

            aluno = usuarioQueries.getAluno();
        }

        montarNotasBimestre();
    }

    public void voltarBimestre(View view) {

        switch (bimestreSelecionado){

            case 1:

                bimestreSelecionado = 4;

                break;

            case 2:

                bimestreSelecionado = 1;

                break;

            case 3:

                bimestreSelecionado = 2;

                break;

            case 4:

                bimestreSelecionado = 3;

                break;
        }

        montarNotasBimestre();
    }

    public void avancarBimestre(View view) {

        switch (bimestreSelecionado) {

            case 1:

                bimestreSelecionado = 2;

                break;

            case 2:

                bimestreSelecionado = 3;

                break;

            case 3:

                bimestreSelecionado = 4;

                break;

            case 4:

                bimestreSelecionado = 1;

                break;
        }

        montarNotasBimestre();
    }

    private void montarNotasBimestre() {

        txtbimestre.setText(bimestreSelecionado + "º bimestre");

        listaNotas = notasQueries.getNotas(aluno.getCd_aluno(), bimestreSelecionado);

        if (listaNotas != null
                && !listaNotas.isEmpty()) {

            removerDisciplinaAnosIniciais();

            notasAdapter = new NotasAdapter(listaNotas, this);

            lvNotas.setAdapter(notasAdapter);

            lvNotas.setVisibility(View.VISIBLE);
        }
        else {

            lvNotas.setVisibility(View.INVISIBLE);

            Toast.makeText(this, "Ainda não há notas para este bimestre", Toast.LENGTH_SHORT).show();
        }
    }

    public void voltarMenu(View v) {

        onBackPressed();
    }

    private void removerDisciplinaAnosIniciais(){

        for(int i = 0; i < listaNotas.size(); i++){

            if(listaNotas.get(i).getCd_disciplina() == 1000){

                listaNotas.remove(i);

                break;
            }
        }
    }
}
