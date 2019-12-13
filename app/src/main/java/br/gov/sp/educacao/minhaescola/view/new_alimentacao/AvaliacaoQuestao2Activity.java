package br.gov.sp.educacao.minhaescola.view.new_alimentacao;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import br.gov.sp.educacao.minhaescola.R;
import br.gov.sp.educacao.minhaescola.model.AvaliacaoAlimentacao;
import br.gov.sp.educacao.minhaescola.view.MenuActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AvaliacaoQuestao2Activity extends AppCompatActivity {

    public @BindView(R.id.avaliacao_q2_opc1) LinearLayout linearOpc1;
    public @BindView(R.id.avaliacao_q2_opc2) LinearLayout linearOpc2;
    public @BindView(R.id.avaliacao_q2_opc3) LinearLayout linearOpc3;
    public @BindView(R.id.avaliacao_q2_opc4) LinearLayout linearOpc4;
    public @BindView(R.id.avaliacao_q2_opc5) LinearLayout linearOpc5;

    public @BindView(R.id.avaliacao_q2_voltar) ImageView btnVoltar;

    private AvaliacaoAlimentacao avaliacao;

    // OPC 1 = Estava ótima, adorei!
    // OPC 2 = Gostei!
    // OPC 3 = Não gostei
    // OPC 4 = Não sei, não comi
    // OPC 5 = Não foi servido

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao_questao2);

        ButterKnife.bind(this);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul_background_alimentacao_mensagem));
        }

        avaliacao = (AvaliacaoAlimentacao) getIntent().getSerializableExtra("avaliacao");
    }

    @OnClick({R.id.avaliacao_q2_opc1,
            R.id.avaliacao_q2_opc2,
            R.id.avaliacao_q2_opc3,
            R.id.avaliacao_q2_opc4,
            R.id.avaliacao_q2_opc5})
    public void onClickOpc(View view){

        Intent intent = new Intent();

        switch (view.getId()){

            case R.id.avaliacao_q2_opc1:

                intent = new Intent(AvaliacaoQuestao2Activity.this, AvaliacaoQuestao3Activity.class);
                avaliacao.setQuestao2(new int[]{3});
                break;

            case R.id.avaliacao_q2_opc2:

                intent = new Intent(AvaliacaoQuestao2Activity.this, AvaliacaoQuestao3Activity.class);
                avaliacao.setQuestao2(new int[]{4});
                break;

            case R.id.avaliacao_q2_opc3:

                intent = new Intent(AvaliacaoQuestao2Activity.this, AvaliacaoQuestao4Activity.class);
                avaliacao.setQuestao2(new int[]{5});
                break;

            case R.id.avaliacao_q2_opc4:

                intent = new Intent(AvaliacaoQuestao2Activity.this, AvaliacaoQuestao5Activity.class);
                avaliacao.setQuestao2(new int[]{6});
                break;

            case R.id.avaliacao_q2_opc5:

                intent = new Intent(AvaliacaoQuestao2Activity.this, AvaliacaoConcluirActivity.class);
                avaliacao.setQuestao2(new int[]{7});
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

    @OnClick(R.id.avaliacao_q2_voltar)
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
