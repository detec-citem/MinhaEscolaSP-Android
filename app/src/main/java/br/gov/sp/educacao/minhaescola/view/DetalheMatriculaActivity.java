package br.gov.sp.educacao.minhaescola.view;

import android.os.Build;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import br.gov.sp.educacao.minhaescola.R;
import br.gov.sp.educacao.minhaescola.banco.TurmaQueries;
import br.gov.sp.educacao.minhaescola.model.Turma;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.gov.sp.educacao.minhaescola.util.Utils.formatarData;
import static br.gov.sp.educacao.minhaescola.util.Utils.getAnoData;

public class DetalheMatriculaActivity
        extends AppCompatActivity {

    public @BindView(R.id.detalhe_txtAno) TextView txtAno;
    public @BindView(R.id.detalhe_txtTurma) TextView txtTurma;
    public @BindView(R.id.detalhe_txtEscola) TextView txtEscola;
    public @BindView(R.id.detalhe_txtSituacao) TextView txtSituacao;
    public @BindView(R.id.detalhe_txtRendimento) TextView txtRendimento;
    public @BindView(R.id.detalhe_txtDataInicio) TextView txtDataInicio;
    public @BindView(R.id.detalhe_txtDataFim) TextView txtDataFim;

    public @BindView(R.id.detalhe_btnVoltar) ImageView btnVoltar;

    private Turma turma;

    private TurmaQueries turmaQueries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detalhe_matricula);

        ButterKnife.bind(this);

        Window window = this.getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul_background_sobre_mim));
        }

        contentOnCreate();
    }

    private void contentOnCreate() {

        turmaQueries = new TurmaQueries(getApplicationContext());

        btnVoltar = findViewById(R.id.detalhe_btnVoltar);

        turma = turmaQueries.getTurma(getIntent().getIntExtra("cd_turma", 0));

        txtAno.setText(String.valueOf(turma.getAno_letivo()));
        txtTurma.setText(turma.getNome_turma());
        txtEscola.setText(turma.getNome_escola());

        if(turma.getSituacao_matricula() != null && !turma.getSituacao_matricula().contains("null")) {

            int anoAtual = Calendar.getInstance().get(Calendar.YEAR);
            int anoTurma = getAnoData(turma.getDt_fim_matricula());

            if(turma.getSituacao_matricula().contains("Ativo") && anoTurma < anoAtual){

                txtSituacao.setText("Encerrada");
            }
            else{

                txtSituacao.setText(turma.getSituacao_matricula());
            }
        }

        if(turma.getSituacao_aprovacao() != null && !turma.getSituacao_aprovacao().contains("null")) {

            txtRendimento.setText(turma.getSituacao_aprovacao());
        }

        txtDataInicio.setText(formatarData(turma.getDt_inicio_matricula()));
        txtDataFim.setText(formatarData(turma.getDt_fim_matricula()));
    }

    @OnClick(R.id.detalhe_btnVoltar)
    public void onClick() {

        onBackPressed();

        finish();
    }
}
