package br.gov.sp.educacao.minhaescola.view;

import android.os.Build;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.gov.sp.educacao.minhaescola.R;

import br.gov.sp.educacao.minhaescola.adapter.FrequenciaAdapter;

import br.gov.sp.educacao.minhaescola.banco.FrequenciasQueries;
import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;

import br.gov.sp.educacao.minhaescola.model.Aluno;
import br.gov.sp.educacao.minhaescola.model.Frequencia;

import br.gov.sp.educacao.minhaescola.util.MyPreferences;

import butterknife.ButterKnife;
import butterknife.BindView;

public class FrequenciaActivity
        extends AppCompatActivity {

    private int bimestreSelecionado;

    private MyPreferences mPref;

    private UsuarioQueries usuarioQueries;

    private ArrayList<Frequencia> listaFrequencia;

    private FrequenciasQueries frequenciasQueries;

    private Aluno aluno;

    public @BindView(R.id.frequencia_txtBimestre) TextView txtBimestre;

    public @BindView(R.id.frequencia_listaFrequencia) ListView lvFrequencia;

    private FrequenciaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_frequencia);

        ButterKnife.bind(this);

        Window window = this.getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul_frequencia));
        }

        usuarioQueries = new UsuarioQueries(getApplicationContext());

        frequenciasQueries = new FrequenciasQueries(getApplicationContext());

        aluno = usuarioQueries.getAluno(getIntent().getIntExtra("cd_aluno", 0));

        bimestreSelecionado = frequenciasQueries.maiorBimestre();

        contentOnCreate();
    }

    private void contentOnCreate() {

        aluno = usuarioQueries.getAluno();

        mPref = new MyPreferences(getApplicationContext());

        if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

            aluno = usuarioQueries.getAluno(getIntent().getIntExtra("cd_aluno", 0));
        }
        else {

            aluno = usuarioQueries.getAluno();
        }

        montarFrequenciaBimestre();
    }

    public void avancarBim(View v) {

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

        montarFrequenciaBimestre();
    }

    public void voltatBim(View v){

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

        montarFrequenciaBimestre();
    }

    public void montarFrequenciaBimestre() {

        txtBimestre.setText(bimestreSelecionado + "º bimestre");

        listaFrequencia = frequenciasQueries.getFreq(aluno.getCd_aluno(), bimestreSelecionado);

        if(!listaFrequencia.isEmpty()) {

            filtrarDisciplinaAnosIniciais();

            adapter = new FrequenciaAdapter(listaFrequencia, this);

            lvFrequencia.setAdapter(adapter);
            lvFrequencia.setVisibility(View.VISIBLE);
        }
        else {

            lvFrequencia.setVisibility(View.INVISIBLE);

            Toast.makeText(this, "Ainda não há frequencia para este bimestre", Toast.LENGTH_SHORT).show();
        }
    }

    public void voltarFrequenciaMenu(View v) {

        onBackPressed();
    }

    public void filtrarDisciplinaAnosIniciais(){

        for (int i = 0; i < listaFrequencia.size(); i++) {

            if(listaFrequencia.get(i).getCd_disciplina() == 1000){

                removerMateriasQuebra();
                break;
            }
        }

    }

    public void removerMateriasQuebra(){

        ArrayList<Frequencia> listaFrequenciaFiltrada = new ArrayList<>();

        for (int i = 0; i < listaFrequencia.size(); i++) {

            if(listaFrequencia.get(i).getFalta() >= 0){

                listaFrequenciaFiltrada.add(listaFrequencia.get(i));
            }
        }

        listaFrequencia = listaFrequenciaFiltrada;
    }


}
