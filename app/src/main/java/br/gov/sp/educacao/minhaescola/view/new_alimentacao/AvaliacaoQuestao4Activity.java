package br.gov.sp.educacao.minhaescola.view.new_alimentacao;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;

import br.gov.sp.educacao.minhaescola.R;
import br.gov.sp.educacao.minhaescola.model.AvaliacaoAlimentacao;
import br.gov.sp.educacao.minhaescola.view.MenuActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AvaliacaoQuestao4Activity extends AppCompatActivity {

    public @BindView(R.id.avaliacao_q4_btnConcluir) Button btnConcluir;

    public @BindView(R.id.avaliacao_q4_voltar) ImageView btnVoltar;

    public @BindView (R.id.avaliacao_q4_opc1) LinearLayout linearOpc1;
    public @BindView (R.id.avaliacao_q4_opc2) LinearLayout linearOpc2;
    public @BindView (R.id.avaliacao_q4_opc3) LinearLayout linearOpc3;
    public @BindView (R.id.avaliacao_q4_opc4) LinearLayout linearOpc4;
    public @BindView (R.id.avaliacao_q4_opc5) LinearLayout linearOpc5;
    public @BindView (R.id.avaliacao_q4_opc6) LinearLayout linearOpc6;
    public @BindView (R.id.avaliacao_q4_opc7) LinearLayout linearOpc7;
    public @BindView (R.id.avaliacao_q4_opc8) LinearLayout linearOpc8;

    public @BindView (R.id.avaliacao_q4_opc1_check) LottieAnimationView checkOpc1;
    public @BindView (R.id.avaliacao_q4_opc2_check) LottieAnimationView checkOpc2;
    public @BindView (R.id.avaliacao_q3_opc3_check) LottieAnimationView checkOpc3;
    public @BindView (R.id.avaliacao_q4_opc4_check) LottieAnimationView checkOpc4;
    public @BindView (R.id.avaliacao_q4_opc5_check) LottieAnimationView checkOpc5;
    public @BindView (R.id.avaliacao_q4_opc6_check) LottieAnimationView checkOpc6;
    public @BindView (R.id.avaliacao_q4_opc7_check) LottieAnimationView checkOpc7;
    public @BindView (R.id.avaliacao_q4_opc8_check) LottieAnimationView checkOpc8;

    private boolean boolOpc1 = false;
    private boolean boolOpc2 = false;
    private boolean boolOpc3 = false;
    private boolean boolOpc4 = false;
    private boolean boolOpc5 = false;
    private boolean boolOpc6 = false;
    private boolean boolOpc7 = false;
    private boolean boolOpc8 = false;

    private AvaliacaoAlimentacao avaliacao;

    // opc1 = Sabor / Tempero
    // opc2 = Aparência
    // opc3 = Cardápio repetitivo
    // opc4 = Combinação dos alimentos ruim
    // opc5 = Comida gordurosa
    // opc6 = Textura ruim (duro, malcozido)
    // opc7 = Má qualidade dos alimentos
    // opc8 = Questões de higiene

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao_questao4);

        ButterKnife.bind(this);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul_background_alimentacao_mensagem));
        }

        avaliacao = (AvaliacaoAlimentacao) getIntent().getSerializableExtra("avaliacao");
    }

    @OnClick(R.id.avaliacao_q4_btnConcluir)
    public void clickConcluir(){

        int[] opcoes = new int[quantidadeCheckado()];

        int indice = 0;

        if(boolOpc1){

            opcoes[indice] = 13;
            indice++;
        }
        if(boolOpc2){

            opcoes[indice] = 14;
            indice++;
        }
        if(boolOpc3){

            opcoes[indice] = 15;
            indice++;
        }
        if(boolOpc4){

            opcoes[indice] = 16;
            indice++;
        }
        if(boolOpc5){

            opcoes[indice] = 17;
            indice++;
        }
        if(boolOpc6){

            opcoes[indice] = 18;
            indice++;
        }
        if(boolOpc7){

            opcoes[indice] = 19;
            indice++;
        }
        if(boolOpc8){

            opcoes[indice] = 20;
            indice++;
        }

        avaliacao.setQuestao4(opcoes);

        Intent intent = new Intent(this, AvaliacaoConcluirActivity.class);

        intent.putExtra("avaliacao", avaliacao);

        startActivity(intent);
        finish();
    }

    @OnClick({R.id.avaliacao_q4_opc1,
            R.id.avaliacao_q4_opc2,
            R.id.avaliacao_q4_opc3,
            R.id.avaliacao_q4_opc4,
            R.id.avaliacao_q4_opc5,
            R.id.avaliacao_q4_opc6,
            R.id.avaliacao_q4_opc7,
            R.id.avaliacao_q4_opc8})
    public void clickOpcao(View view) {

        switch (view.getId()) {

            case R.id.avaliacao_q4_opc1:

                if (!boolOpc1) {

                    boolOpc1 = true;
                    startCheckAnimation(checkOpc1);
                }
                else {

                    boolOpc1 = false;
                    startCheckAnimation(checkOpc1);
                }

                break;

            case R.id.avaliacao_q4_opc2:

                if (!boolOpc2) {

                    boolOpc2 = true;
                    startCheckAnimation(checkOpc2);
                }
                else {

                    boolOpc2 = false;
                    startCheckAnimation(checkOpc2);
                }

                break;

            case R.id.avaliacao_q4_opc3:

                if (!boolOpc3) {

                    boolOpc3 = true;
                    startCheckAnimation(checkOpc3);
                }
                else {

                    boolOpc3 = false;
                    startCheckAnimation(checkOpc3);
                }

                break;

            case R.id.avaliacao_q4_opc4:

                if (!boolOpc4) {

                    boolOpc4 = true;
                    startCheckAnimation(checkOpc4);
                }
                else {

                    boolOpc4 = false;
                    startCheckAnimation(checkOpc4);
                }

                break;

            case R.id.avaliacao_q4_opc5:

                if (!boolOpc5) {

                    boolOpc5 = true;
                    startCheckAnimation(checkOpc5);
                }
                else {

                    boolOpc5 = false;
                    startCheckAnimation(checkOpc5);
                }

                break;

            case R.id.avaliacao_q4_opc6:

                if (!boolOpc6) {

                    boolOpc6 = true;
                    startCheckAnimation(checkOpc6);
                }
                else {

                    boolOpc6 = false;
                    startCheckAnimation(checkOpc6);
                }

                break;

            case R.id.avaliacao_q4_opc7:

                if (!boolOpc7) {

                    boolOpc7 = true;
                    startCheckAnimation(checkOpc7);
                } else {

                    boolOpc7 = false;
                    startCheckAnimation(checkOpc7);
                }
                break;

            case R.id.avaliacao_q4_opc8:

                if (!boolOpc8) {

                    boolOpc8 = true;
                    startCheckAnimation(checkOpc8);
                }
                else {

                    boolOpc8 = false;
                    startCheckAnimation(checkOpc8);
                }

                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(this, MenuActivity.class));
        finish();
    }

    @OnClick(R.id.avaliacao_q4_voltar)
    public void btnVoltarClick(){

        onBackPressed();
    }

    private void startCheckAnimation(final LottieAnimationView animationView) {

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(400);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                animationView.setProgress((Float) valueAnimator.getAnimatedValue());
            }
        });

        if (animationView.getProgress() == 0f) {

            animator.start();

        }
        else {

            animator.reverse();
        }
    }

    private int quantidadeCheckado(){

        int qtd = 0;

        if(boolOpc1){

            qtd++;
        }
        if(boolOpc2){

            qtd++;
        }
        if(boolOpc3){

            qtd++;
        }
        if(boolOpc4){

            qtd++;
        }
        if(boolOpc5){

            qtd++;
        }
        if(boolOpc6){

            qtd++;
        }
        if(boolOpc7){

            qtd++;
        }
        if(boolOpc8){

            qtd++;
        }

        return qtd;
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
