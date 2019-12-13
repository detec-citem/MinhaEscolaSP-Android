package br.gov.sp.educacao.minhaescola.view.new_alimentacao;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.gov.sp.educacao.minhaescola.R;
import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;
import br.gov.sp.educacao.minhaescola.receivers.AlimentacaoReceiver;
import br.gov.sp.educacao.minhaescola.services.AlimentacaoService;
import br.gov.sp.educacao.minhaescola.util.MyPreferences;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CardapioActivity extends AppCompatActivity {

    public @BindView(R.id.cardapio_btn_avaliar) Button btnAvaliar;
    public @BindView(R.id.cardapio_img_voltar) ImageView btnVoltar;

    public @BindView(R.id.cardapio_comidaSegunda) TextView txtSegunda;
    public @BindView(R.id.cardapio_comidaTerca) TextView txtTerca;
    public @BindView(R.id.cardapio_comidaQuarta) TextView txtQuarta;
    public @BindView(R.id.cardapio_comidaQuinta) TextView txtQuinta;
    public @BindView(R.id.cardapio_comidaSexta) TextView txtSexta;

    public @BindView(R.id.cardapio_laySegunda) LinearLayout laySegunda;
    public @BindView(R.id.cardapio_layTerca) LinearLayout layTerca;
    public @BindView(R.id.cardapio_layQuarta) LinearLayout layQuarta;
    public @BindView(R.id.cardapio_layQuinta) LinearLayout layQuinta;
    public @BindView(R.id.cardapio_laySexta) LinearLayout laySexta;

    private AlimentacaoReceiver mAlimentacaoReceiver;

    public ProgressDialog progressDialogCardapio;

    private UsuarioQueries usuarioQueries;

    public int cdAluno = 0;

    private int cd_turma;

    private MyPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);

        ButterKnife.bind(this);

        progressDialogCardapio = new ProgressDialog(this);
        progressDialogCardapio.setMessage("Verificando");
        progressDialogCardapio.setTitle("Aguarde...");
        progressDialogCardapio.setCancelable(false);

        usuarioQueries = new UsuarioQueries(this);

        mPref = new MyPreferences(this);

        if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

            cdAluno = getIntent().getIntExtra("cd_aluno", 0);
        }

        cd_turma = getIntent().getIntExtra("cd_turma", 0);

        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul_background_alimentacao_mensagem));
        }

        progressDialogCardapio.setMessage("Verificando");
        progressDialogCardapio.show();

        alimentacaoPodeResponder();
    }

    @OnClick(R.id.cardapio_btn_avaliar)
    public void onAvaliarClick(){

        progressDialogCardapio.setMessage("Verificando");
        progressDialogCardapio.show();

        alimentacaoPodeResponder();
    }

    @OnClick(R.id.cardapio_img_voltar)
    public void onVoltarClick(){

        onBackPressed();
    }

    public void registerAlimentacaoReceiver() {

        IntentFilter intentFilter = new IntentFilter(AlimentacaoReceiver.ACTION_VIEW_ALIMENTACAO);

        registerReceiver(mAlimentacaoReceiver, intentFilter);
    }

    public void alimentacaoPodeResponder() {

        mAlimentacaoReceiver = new AlimentacaoReceiver(this);

        registerAlimentacaoReceiver();

        startService(AlimentacaoService.makeDataIntent(getApplicationContext(), usuarioQueries.getToken(), cd_turma));

    }
}
