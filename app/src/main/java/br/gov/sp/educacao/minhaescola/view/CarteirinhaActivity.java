package br.gov.sp.educacao.minhaescola.view;

import android.graphics.Bitmap;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.gov.sp.educacao.minhaescola.R;

import br.gov.sp.educacao.minhaescola.banco.CarteirinhaQueries;
import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;

import br.gov.sp.educacao.minhaescola.model.Aluno;
import br.gov.sp.educacao.minhaescola.model.Carteirinha;

import br.gov.sp.educacao.minhaescola.util.MyPreferences;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

import static br.gov.sp.educacao.minhaescola.util.ImageHelper.bitmapFromBase64;

public class CarteirinhaActivity
        extends AppCompatActivity {

    public @BindView(R.id.carteira_txtNomeEscola) TextView txtEscola;
    public @BindView(R.id.carteira_txtDataNasc) TextView txtDataNasc;
    public @BindView(R.id.carteira_txtApelido) TextView txtApelido;
    public @BindView(R.id.carteira_txtNome) TextView txtNome;
    public @BindView(R.id.carteira_txtCodigo) TextView txtCodigo;
    public @BindView(R.id.carteira_txtValidade) TextView txtValidade;
    public @BindView(R.id.carteira_txtRg) TextView txtRg;
    public @BindView(R.id.carteira_txtTurma) TextView txtTurma;

    public @BindView(R.id.carteira_imgFoto) ImageView imgFoto;
    public @BindView(R.id.carteira_imgQr) ImageView imgQr;

    public @BindView(R.id.carteira_btnVoltar) ImageView btnVoltar;
    public @BindView(R.id.carteira_progress) ProgressBar progressBar;
    public @BindView(R.id.carteira_shape_voltar) ImageView carteira_shape_voltar;

    private List<View> listaView = new ArrayList<>();

    private MyPreferences mPref;

    private Aluno aluno;

    private UsuarioQueries usuarioQueries;

    private CarteirinhaQueries carteirinhaQueries;

    private Carteirinha carteirinha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_carteirinha);

        ButterKnife.bind(this);

        contentOnCreate();
    }

    private void contentOnCreate() {

        mPref = new MyPreferences(getApplicationContext());

        usuarioQueries = new UsuarioQueries(getApplicationContext());

        carteirinhaQueries = new CarteirinhaQueries(getApplicationContext());

        listaView.add(txtEscola);
        listaView.add(txtDataNasc);
        listaView.add(txtApelido);
        listaView.add(txtNome);
        listaView.add(txtValidade);
        listaView.add(txtCodigo);
        listaView.add(txtRg);
        listaView.add(txtTurma);
        listaView.add(imgFoto);
        listaView.add(imgQr);
        listaView.add(btnVoltar);
        listaView.add(carteira_shape_voltar);

        esconderViews();

        if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

            aluno = usuarioQueries.getAluno(getIntent().getIntExtra("cd_aluno", 0));
        }
        else {

            aluno = usuarioQueries.getAluno();
        }

        carteirinha = carteirinhaQueries.getCarteirinha(aluno.getCd_aluno());

        montarViews();
    }

    @OnClick(R.id.carteira_btnVoltar)
    public void onClick() {

        onBackPressed();
    }

    public void esconderViews() {

        for (View view : listaView) {

            view.setVisibility(View.INVISIBLE);
        }

        progressBar.setVisibility(View.VISIBLE);
    }

    public void montarViews() {

        txtEscola.setText(carteirinha.getNome_escola());
        txtDataNasc.setText("Nascimento: " + carteirinha.getData_nascimento());
        txtApelido.setText(carteirinha.getApelido());
        txtNome.setText(carteirinha.getNome());
        txtCodigo.setText(carteirinha.getQr_criptografado());
        txtValidade.setText("Validade: " + carteirinha.getValidade());
        txtTurma.setText(carteirinha.getNome_turma());

        if(carteirinha.getRg() == null
                || carteirinha.getRg().equals("")) {

            txtRg.setText("RG: Sem RG");
        }
        else {

            txtRg.setText("RG: " + carteirinha.getRg());
        }

        Bitmap fotoAluno = bitmapFromBase64(carteirinha.getFoto_aluno());

        imgFoto.setImageBitmap(fotoAluno);

        Bitmap fotoQrCode = bitmapFromBase64(carteirinha.getFoto_qr());

        imgQr.setImageBitmap(fotoQrCode);

        for (View view : listaView) {

            view.setVisibility(View.VISIBLE);
        }

        progressBar.setVisibility(View.GONE);
    }
}
