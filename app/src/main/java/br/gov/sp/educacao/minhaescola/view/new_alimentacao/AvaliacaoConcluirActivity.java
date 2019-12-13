package br.gov.sp.educacao.minhaescola.view.new_alimentacao;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import br.gov.sp.educacao.minhaescola.R;
import br.gov.sp.educacao.minhaescola.model.AvaliacaoAlimentacao;
import br.gov.sp.educacao.minhaescola.requests.EnviarAvaliacaoAlimentacaoRequest;
import br.gov.sp.educacao.minhaescola.task.EnviarAvaliacaoAlimentacaoTask;
import br.gov.sp.educacao.minhaescola.view.MenuActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AvaliacaoConcluirActivity extends AppCompatActivity {

    public @BindView (R.id.avaliacao_concluir_voltar) ImageView btnVoltar;
    public @BindView (R.id.avaliacao_concluir_btnConcluir) Button btnConcluir;

    private EnviarAvaliacaoAlimentacaoTask enviarAvaliacaoTask;

    private AvaliacaoAlimentacao avaliacao;

    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao_concluir);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul_background_alimentacao_mensagem));
        }

        avaliacao = (AvaliacaoAlimentacao) getIntent().getSerializableExtra("avaliacao");

        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Enviando");
        progressDialog.setTitle("Aguarde...");
        progressDialog.setCancelable(false);

        enviarAvaliacaoTask = new EnviarAvaliacaoAlimentacaoTask(this);
    }

    @OnClick(R.id.avaliacao_concluir_btnConcluir)
    public void onClickBtnConcluir(){

        try{

            enviarAvaliacaoTask = new EnviarAvaliacaoAlimentacaoTask(this);
            enviarAvaliacaoTask.execute(avaliacao);
        }
        catch (Exception e){

            e.printStackTrace();
        }
    }

    @OnClick(R.id.avaliacao_concluir_voltar)
    public void onClickVoltar(){

        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(this, MenuActivity.class));
        finish();
    }
}