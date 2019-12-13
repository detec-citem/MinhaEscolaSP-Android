package br.gov.sp.educacao.minhaescola.view.new_alimentacao;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.style.IconMarginSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import br.gov.sp.educacao.minhaescola.R;
import br.gov.sp.educacao.minhaescola.banco.TurmaQueries;
import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;
import br.gov.sp.educacao.minhaescola.model.Aluno;
import br.gov.sp.educacao.minhaescola.model.AvaliacaoAlimentacao;
import br.gov.sp.educacao.minhaescola.model.Turma;
import br.gov.sp.educacao.minhaescola.util.MyPreferences;
import br.gov.sp.educacao.minhaescola.view.MenuActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AvaliacaoQuestao1Activity extends AppCompatActivity {

    public @BindView(R.id.avaliacao_q1_opc1) LinearLayout linearOpc1;
    public @BindView(R.id.avaliacao_q1_opc2) LinearLayout linearOpc12;

    public @BindView(R.id.avaliacao_q1_voltar) ImageView btnVoltar;

    private AvaliacaoAlimentacao avaliacao;

    private UsuarioQueries usuarioQueries;

    private TurmaQueries turmaQueries;

    private Aluno aluno;

    private Turma turmaAtiva;

    private MyPreferences mPref;

    int cd_turma;

    //opc1 = Foi servido de acordo com o cardápio
    //opc2 = Não foi servido de acordo com o cardápio

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao_questao1);

        ButterKnife.bind(this);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul_background_alimentacao_mensagem));
        }

        avaliacao = new AvaliacaoAlimentacao();

        usuarioQueries = new UsuarioQueries(this);

        turmaQueries = new TurmaQueries(this);

        mPref = new MyPreferences(this);

        cd_turma = getIntent().getIntExtra("cd_turma", 0);

        if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

            aluno = usuarioQueries.getAluno(getIntent().getIntExtra("cd_aluno", 0));
        }
        else {

            aluno = usuarioQueries.getAluno();
        }

        if(cd_turma == 0){

            turmaAtiva = turmaQueries.getTurmaAtiva(aluno.getCd_aluno());

            avaliacao.setCodTurma(turmaAtiva.getCd_turma());
        }
        else{

            avaliacao.setCodTurma(cd_turma);
        }
    }

    @OnClick({R.id.avaliacao_q1_opc1, R.id.avaliacao_q1_opc2})
    public void onClickOpc(View view){

        Intent intent = new Intent(this, AvaliacaoQuestao2Activity.class);

        switch (view.getId()){

            case R.id.avaliacao_q1_opc1:

                avaliacao.setQuestao1(new int[]{1});
                break;

            case R.id.avaliacao_q1_opc2:

                avaliacao.setQuestao1(new int[]{2});
                break;
        }

        intent.putExtra("avaliacao", avaliacao);

        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(this, MenuActivity.class));
        finish();
    }

    @OnClick(R.id.avaliacao_q1_voltar)
    public void btnVoltarClick(){

        onBackPressed();
    }
}

/*
CD_ALTERNATIVA  -  DS_ALTERNATIVA  -  CD_QUESTAO

1 -  Sim - 1
2 -  Não - 1

3 -  Estava ótima, adorei! - 2
4 -  Gostei - 2
5 -  Não gostei - 2
6 -  Não sei, não comi - 2
7 -  Não foi servido - 2

8 -  Sabor / Tempero - 3
9 -  Aparência - 3
10 - Cardápio do dia - 3
11 - Combinação dos alimentos - 3
12 - Qualidade dos alimentos - 3

13 - Sabor / Tempero - 4
14 - Aparência - 4
15 - Cardápio repetitivo - 4
16 - Combinação de alimentos ruim - 4
17 - Comida gordurosa - 4
18 - Textura ruim (duro, malcozido) - 4
19 - Má qualidade dos alimentos - 4
20 - Questões de higiene - 4

21 - Não estava com fome - 5
22 - Não gostei do cardápio de hoje - 5
23 - Cardápio repetitivo - 5
24 - Aparência ruim - 5
25 - Horário ruim - 5
26 - Combinação de alimentos ruim - 5
27 - Má qualidade dos alimentos - 5
28 - Questões de higiene - 5
*/