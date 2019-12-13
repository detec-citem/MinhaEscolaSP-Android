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

import java.util.ArrayList;
import java.util.List;

import br.gov.sp.educacao.minhaescola.R;
import br.gov.sp.educacao.minhaescola.banco.EscolaQueries;
import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;
import br.gov.sp.educacao.minhaescola.model.Aluno;
import br.gov.sp.educacao.minhaescola.model.ContatoEscola;
import br.gov.sp.educacao.minhaescola.model.Escola;
import br.gov.sp.educacao.minhaescola.util.MyPreferences;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

import static br.gov.sp.educacao.minhaescola.util.Utils.formatarTelefone;

public class MinhaEscolaActivity
        extends AppCompatActivity {

    public @BindView(R.id.minhaEscola_btnVoltar) ImageView btnVoltar;

    public @BindView(R.id.minhaEscola_txtNomeEscola) TextView txtNome;
    public @BindView(R.id.minhaEscola_txtEndereco) TextView txtEndereco;
    public @BindView(R.id.minhaEscola_txtTelefone) TextView txtTelefone;
    public @BindView(R.id.minhaEscola_txtDiretor) TextView txtDiretor;
    public @BindView(R.id.minhaEscola_txtEmail) TextView txtEmail;
    public @BindView(R.id.minhaEscola_txtTitulo) TextView txtTitulo;

    private UsuarioQueries usuarioQueries;
    private EscolaQueries escolaQueries;

    private MyPreferences mPref;

    private Aluno aluno;
    private Escola escola;

    private String nome_escola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_minha_escola);

        ButterKnife.bind(this);

        nome_escola = getIntent().getStringExtra("nome_escola");

        contentOnCreate();
    }

    private void contentOnCreate() {

        Window window = this.getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul_background_minha_escola));
        }

        mPref = new MyPreferences(this);

        usuarioQueries = new UsuarioQueries(getApplicationContext());
        escolaQueries = new EscolaQueries(getApplicationContext());

        if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

            txtTitulo.setText("escola");

            int cod_aluno = (int) getIntent().getIntExtra("cd_aluno",0);

            aluno = usuarioQueries.getAluno(cod_aluno);
        }
        else {

            aluno = usuarioQueries.getAluno();
        }

        if(aluno != null){

            escola = escolaQueries.getEscola(aluno.getCd_aluno(), nome_escola);
        }
        else{

            refreshAluno();

            escola = escolaQueries.getEscola(aluno.getCd_aluno(), nome_escola);
        }

        if (escola.getNome_escola() != null) {

            txtNome.setText(escola.getNome_escola());

            txtDiretor.setText(escola.getNome_diretor());

            txtEmail.setText(escola.getEmail_escola());

            txtEndereco.setText(escola.getEndereco_unidade());
        }

        ArrayList<ContatoEscola> contatosEscola = escolaQueries.getContatosEscola(escola.getCd_escola());

        if(!contatosEscola.isEmpty()) {

            txtTelefone.setText(concatenaTelefones(contatosEscola));
        }
    }

    @OnClick(R.id.minhaEscola_btnVoltar)
    public void onClick() {

        onBackPressed();
    }

    public String concatenaTelefones(List<ContatoEscola> contatosEscola) {

        String telefonesConcatenados= "";

        boolean primeiro = true;

        if(contatosEscola != null && contatosEscola.size() > 0) {

            for (ContatoEscola contato : contatosEscola) {

                if (primeiro) {

                    telefonesConcatenados = formatarTelefone(contato.getContato_escola());

                    primeiro = false;
                }
                else {

                    telefonesConcatenados = (telefonesConcatenados + "\n" + formatarTelefone(contato.getContato_escola()));
                }
            }
        }

        return telefonesConcatenados;
    }

    private void refreshAluno(){

        if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

            int cod_aluno = (int) getIntent().getIntExtra("cd_aluno",0);

            aluno = usuarioQueries.getAluno(cod_aluno);
        }
        else {

            aluno = usuarioQueries.getAluno();
        }
    }
}
