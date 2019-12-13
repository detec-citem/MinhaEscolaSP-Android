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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.gov.sp.educacao.minhaescola.R;

import br.gov.sp.educacao.minhaescola.adapter.HorarioAulaAdapter;

import br.gov.sp.educacao.minhaescola.banco.HorarioAulaQueries;
import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;

import br.gov.sp.educacao.minhaescola.model.Aluno;
import br.gov.sp.educacao.minhaescola.model.HorarioAula;

import br.gov.sp.educacao.minhaescola.util.MyPreferences;
import butterknife.BindView;
import butterknife.ButterKnife;

import static br.gov.sp.educacao.minhaescola.model.HorarioAula.QUARTA;
import static br.gov.sp.educacao.minhaescola.model.HorarioAula.QUINTA;
import static br.gov.sp.educacao.minhaescola.model.HorarioAula.SEGUNDA;
import static br.gov.sp.educacao.minhaescola.model.HorarioAula.SEXTA;
import static br.gov.sp.educacao.minhaescola.model.HorarioAula.TERCA;


public class HorarioAulasActivity
        extends AppCompatActivity {

    private String diaAtual;

    public @BindView(R.id.horario_listaHorario) ListView listViewHorarios;
    public @BindView(R.id.horario_txtDiaSemana) TextView txtDiaSemana;

    private MyPreferences mPref;

    private ArrayList<HorarioAula> listaHorarios;

    private HorarioAulaAdapter adapter;

    private UsuarioQueries usuarioQueries;
    private HorarioAulaQueries horarioAulaQueries;

    private Aluno aluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_horario_aulas);

        ButterKnife.bind(this);

        Window window = this.getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul_horario_aula));
        }

        usuarioQueries = new UsuarioQueries(getApplicationContext());

        horarioAulaQueries = new HorarioAulaQueries(getApplicationContext());

        aluno = usuarioQueries.getAluno(getIntent().getIntExtra("cd_aluno", 0));

        Calendar c = Calendar.getInstance();

        int diaSemana = c.get(Calendar.DAY_OF_WEEK);

        switch (diaSemana) {

            case 2:

                diaAtual = SEGUNDA;

                break;

            case 3:

                diaAtual = TERCA;

                break;

            case 4:

                diaAtual = QUARTA;

                break;

            case 5:

                diaAtual = QUINTA;

                break;

            case 6:

                diaAtual = SEXTA;

                break;

            default:

                diaAtual = SEGUNDA;

                break;
        }

        txtDiaSemana.setText(diaAtual.toLowerCase());

        mPref = new MyPreferences(this);

        if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

            aluno = usuarioQueries.getAluno(getIntent().getIntExtra("cd_aluno", 0));
        }
        else {

            aluno = usuarioQueries.getAluno();
        }

        listaHorarios = horarioAulaQueries.getHorariosAula(aluno.getCd_aluno());

        adapter = new HorarioAulaAdapter(separarHorarioPorDia(diaAtual), this);

        listViewHorarios.setAdapter(adapter);
    }

    public void avancarDia(View view) {

        switch (diaAtual) {

            case SEGUNDA:

                diaAtual = TERCA;

                txtDiaSemana.setText(diaAtual.toLowerCase());

                adapter = new HorarioAulaAdapter(separarHorarioPorDia(diaAtual), this);

                listViewHorarios.setAdapter(adapter);

                break;

            case TERCA:

                diaAtual = QUARTA;

                txtDiaSemana.setText(diaAtual.toLowerCase());

                adapter = new HorarioAulaAdapter(separarHorarioPorDia(diaAtual), this);

                listViewHorarios.setAdapter(adapter);

                break;

            case QUARTA:

                diaAtual = QUINTA;

                txtDiaSemana.setText(diaAtual.toLowerCase());

                adapter = new HorarioAulaAdapter(separarHorarioPorDia(diaAtual), this);

                listViewHorarios.setAdapter(adapter);

                break;

            case QUINTA:

                diaAtual = SEXTA;

                txtDiaSemana.setText(diaAtual.toLowerCase());

                adapter = new HorarioAulaAdapter(separarHorarioPorDia(diaAtual), this);

                listViewHorarios.setAdapter(adapter);

                break;

            case SEXTA:

                diaAtual = SEGUNDA;

                txtDiaSemana.setText(diaAtual.toLowerCase());

                adapter = new HorarioAulaAdapter(separarHorarioPorDia(diaAtual), this);

                listViewHorarios.setAdapter(adapter);

                break;
        }
    }

    public void voltarDia(View view) {

        switch (diaAtual) {

            case SEGUNDA:

                diaAtual = SEXTA;

                txtDiaSemana.setText(diaAtual.toLowerCase());

                adapter = new HorarioAulaAdapter(separarHorarioPorDia(diaAtual), this);

                listViewHorarios.setAdapter(adapter);

                break;

            case TERCA:

                diaAtual = SEGUNDA;

                txtDiaSemana.setText(diaAtual.toLowerCase());

                adapter = new HorarioAulaAdapter(separarHorarioPorDia(diaAtual), this);

                listViewHorarios.setAdapter(adapter);

                break;

            case QUARTA:

                diaAtual = TERCA;

                txtDiaSemana.setText(diaAtual.toLowerCase());

                adapter = new HorarioAulaAdapter(separarHorarioPorDia(diaAtual), this);

                listViewHorarios.setAdapter(adapter);

                break;

            case QUINTA:

                diaAtual = QUARTA;

                txtDiaSemana.setText(diaAtual.toLowerCase());

                adapter = new HorarioAulaAdapter(separarHorarioPorDia(diaAtual), this);

                listViewHorarios.setAdapter(adapter);

                break;

            case SEXTA:

                diaAtual = QUINTA;

                txtDiaSemana.setText(diaAtual.toLowerCase());

                adapter = new HorarioAulaAdapter(separarHorarioPorDia(diaAtual), this);

                listViewHorarios.setAdapter(adapter);

                break;
        }
    }

    public List<HorarioAula> separarHorarioPorDia(String dia) {

        List<HorarioAula> horariosDoDia = new ArrayList<>();

        for(HorarioAula horarioAula : listaHorarios) {

            if(horarioAula.getDia_semana().equals(dia)) {

                horariosDoDia.add(horarioAula);
            }
        }
        return horariosDoDia;
    }

    public void voltarMenu(View v) {

        onBackPressed();
    }
}
