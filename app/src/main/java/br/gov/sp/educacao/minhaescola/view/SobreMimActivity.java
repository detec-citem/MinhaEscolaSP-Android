package br.gov.sp.educacao.minhaescola.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;

import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import br.gov.sp.educacao.minhaescola.R;
import br.gov.sp.educacao.minhaescola.adapter.TelefoneAdapter;
import br.gov.sp.educacao.minhaescola.banco.CarteirinhaQueries;
import br.gov.sp.educacao.minhaescola.banco.TurmaQueries;
import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;
import br.gov.sp.educacao.minhaescola.model.Aluno;
import br.gov.sp.educacao.minhaescola.model.ContatoAluno;
import br.gov.sp.educacao.minhaescola.model.ContatoResponsavel;
import br.gov.sp.educacao.minhaescola.model.Endereco;
import br.gov.sp.educacao.minhaescola.model.Rematricula;
import br.gov.sp.educacao.minhaescola.model.Responsavel;
import br.gov.sp.educacao.minhaescola.model.Turma;
import br.gov.sp.educacao.minhaescola.task.EnviarRematriculaTask;
import br.gov.sp.educacao.minhaescola.util.FileChooser;
import br.gov.sp.educacao.minhaescola.util.ImageCrop;
import br.gov.sp.educacao.minhaescola.util.MyPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.gov.sp.educacao.minhaescola.util.ImageCrop.takeAlbumAction;
import static br.gov.sp.educacao.minhaescola.util.UrlServidor.URLSERVIDOR_TRACKER;
import static br.gov.sp.educacao.minhaescola.util.UrlServidor.URL_SERVIDOR;

public class SobreMimActivity
        extends AppCompatActivity {

    private final int OPERACAO_INSERIR = 1;
    private final int OPERACAO_ALTERAR = 2;
    private final int OPERACAO_DELETAR = 3;

    public @BindView(R.id.sobre_txtTitulo) TextView txtTitulo;
    public @BindView(R.id.sobre_foto) ImageView imgFotoAluno;
    public @BindView(R.id.sobre_nomeAluno) TextView txtNome;

    public @BindView(R.id.sobre_ra) TextView txtRA;
    public @BindView(R.id.sobre_rg) TextView txtRG;
    public @BindView(R.id.sobre_cpf) TextView txtCPF;

    public @BindView(R.id.sobre_data_nascimento) TextView txtDataNasc;
    public @BindView(R.id.sobre_local_nascimento) TextView txtLocalNasc;
    public @BindView(R.id.sobre_nacionalidade) TextView txtNacionalidade;

    public @BindView(R.id.sobre_turma_atual) TextView txtTurmaAtual;
    public @BindView(R.id.sobre_tipo_ensino) TextView txtTipoEnsino;

    public @BindView(R.id.sobre_nome_mae) TextView txtNomeMae;
    public @BindView(R.id.sobre_nome_pai) TextView txtNomePai;

    public @BindView(R.id.sobre_endereco_tipo_logradouro) TextView txtTipoLogradouro;
    public @BindView(R.id.sobre_endereco_loc_diferenciada) TextView txtLocDiferenciada;
    public @BindView(R.id.sobre_endereco_endereco) TextView txtEndereco;
    public @BindView(R.id.sobre_endereco_complemento) TextView txtComplemento;
    public @BindView(R.id.sobre_endereco_bairro) TextView txtBairro;
    public @BindView(R.id.sobre_endereco_cidade_uf) TextView txtCidadeUf;
    public @BindView(R.id.sobre_endereco_cep) TextView txtCEP;
    public @BindView(R.id.sobre_btn_editar_endereco) Button btnEditarEndereco;
    public @BindView(R.id.sobre_layout_endereco) LinearLayout layoutEndereco;

    public @BindView(R.id.sobre_linear_comprovante) LinearLayout layoutComprovante;
    public @BindView(R.id.sobre_btn_upload_comprovante) Button btnEnvioComprovante;
    public @BindView(R.id.sobre_txt_status_comprovante) TextView txtStatusComprovante;
    public @BindView(R.id.sobre_btn_ajuda_comprovante) TextView btnAjudaComprovante;

    public @BindView(R.id.sobre_txt_email) TextView txtEmail;
    public @BindView(R.id.sobre_btn_editar_email) Button btnEditarEmail;
    public @BindView(R.id.sobre_layout_email) LinearLayout layoutEmailAluno;

    public @BindView(R.id.sobre_layout_telefones) LinearLayout layout_telefones;
    public @BindView(R.id.sobre_lista_telefone_aluno) ListView lvTelefoneAluno;
    public @BindView(R.id.sobre_adicionar_telefone_aluno) Button btnAddTelAluno;

    public @BindView(R.id.sobre_layout_email_resp) LinearLayout layout_emailResponsavel;
    public @BindView(R.id.sobre_txt_email_resp) TextView txtEmailResponsavel;
    public @BindView(R.id.sobre_btn_editar_email_resp) Button btnEditarEmailResponsavel;

    public @BindView(R.id.sobre_telefone_resp) LinearLayout layout_telefonesResponsavel;
    public @BindView(R.id.sobre_lista_telefone_resp) ListView lvTelefoneResp;
    public @BindView(R.id.sobre_adicionar_telefone_responsavel) Button btnAddTelResp;

    public @BindView(R.id.sobre_layout_rematricula) LinearLayout layoutRematricula;

    public @BindView(R.id.sobre_opc_sim) RadioButton opcRematriculaSim;
    public @BindView(R.id.sobre_opc_nao) RadioButton opcRematriculaNao;

    public @BindView(R.id.sobre_layout_check) LinearLayout layoutCheck;

    public @BindView(R.id.sobre_check_novatec) CheckBox checkOpcNovatec;
    public @BindView(R.id.sobre_check_integral) CheckBox checkOpcIntegral;
    public @BindView(R.id.sobre_check_espanhol) CheckBox checkOpcEspanhol;
    public @BindView(R.id.sobre_check_noturno) CheckBox checkOpcNoturno;

    public @BindView(R.id.sobre_layout_justificatica_noturno) LinearLayout layoutJustificativaNoturno;
    public @BindView(R.id.sobre_check_noturno_trabalho) CheckBox checkOpcNoturnoTrabalho;
    public @BindView(R.id.sobre_check_noturno_cursos) CheckBox checkOpcNoturnoCursos;
    public @BindView(R.id.sobre_check_noturno_opcao) CheckBox checkOpcNoturnoOpcao;
    public @BindView(R.id.sobre_check_noturno_outros) CheckBox checkOpcNoturnoOutros;

    public @BindView(R.id.sobre_layout_materias_profissionalizante) LinearLayout layoutNovatec;
    public @BindView(R.id.sobre_spinner_materias_1) Spinner spnMateria1;
    public @BindView(R.id.sobre_spinner_materias_2) Spinner spnMateria2;
    public @BindView(R.id.sobre_spinner_materias_3) Spinner spnMateria3;

    public @BindView(R.id.sobre_btn_salvar) Button btnSalvar;

    public FirebaseAnalytics mFirebaseAnalytics;

    private MyPreferences mPref;

    private UsuarioQueries usuarioQueries;
    private CarteirinhaQueries carteirinhaQueries;
    private TurmaQueries turmaQueries;

    private ArrayList<ContatoAluno> contatosAlunoDeletar;
    private ArrayList<ContatoResponsavel> contatosResponsavelDeletar;

    private Aluno aluno;

    private Turma turmaRegular;

    private Endereco endereco;

    private Bitmap bitmapComprovante;

    private boolean isPDF = false;

    private String pdfBase64;

    private Responsavel responsavel;

    private ArrayList<ContatoResponsavel> contatosResponsavel;

    private ArrayList<ContatoAluno> listaContatos;

    private Rematricula rematricula;

    private TelefoneAdapter telefoneAdapter;

    private TelefoneAdapter telefoneRespAdapter;

    public ProgressDialog dialogRequisicao;

    private String bmpComprovante;

    private boolean atualizou_endereco = false;
    private boolean atualizou_comprovante = false;
    private boolean atualizou_email_aluno = false;
    private boolean atualizou_email_responsavel = false;
    private boolean criou_email_aluno = false;
    private boolean criou_email_responsavel = false;
    private boolean atualizou_telefone_aluno = false;
    private boolean atualizou_telefone_responsavel = false;
    private boolean atualizou_questionario = false;

    private String latitude;
    private String longitude;

    String caminhoArquivoFoto;

    String nomeArquivoFoto;

    private File foto;

    AlertDialog dialogAjudaComprovante;

    int PERMISSION_ALL = 1;

    final int COD_RESULT_GEO = 44;

    String[] PERMISSIONS_CAMERA = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    String[] PERMISSIONS_ALBUM = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre_mim);

        ButterKnife.bind(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul_background_sobre_mim));
        }

        mPref = new MyPreferences(getApplicationContext());

        inicializarBanco();

        carregarAluno();

        foto = null;

        bitmapComprovante = null;

        caminhoArquivoFoto = "";

        nomeArquivoFoto = null;

        if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL){

            carregarRepsonsavel();
        }

        montatLayoutSobreMim();
    }

    private void inicializarBanco() {

        usuarioQueries = new UsuarioQueries(getApplicationContext());

        carteirinhaQueries = new CarteirinhaQueries(getApplicationContext());

        turmaQueries = new TurmaQueries(getApplicationContext());
    }

    public void carregarAluno() {

        if (mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

            int cod_aluno = (int) getIntent().getIntExtra("cd_aluno", 0);

            aluno = usuarioQueries.getAluno(cod_aluno);
        }
        else {

            aluno = usuarioQueries.getAluno();
        }

        endereco = usuarioQueries.getEndereco(aluno.getCd_aluno());

        listaContatos = usuarioQueries.getContatosAluno(aluno.getCd_aluno());

        turmaRegular = turmaQueries.getTurmaAtivaRegular(aluno.getCd_aluno());

        rematricula = usuarioQueries.getRematricula(aluno.getCd_aluno());

    }

    public void carregarRepsonsavel(){

        responsavel = usuarioQueries.getResponsavel();

        contatosResponsavel = usuarioQueries.getContatosResponsavel(responsavel.getCd_responsavel());
    }

    public void montatLayoutSobreMim(){

        final int idadeAluno = aluno.getIdade();

        //region ---- Foto do aluno ----
        if (carteirinhaQueries.temCarteirinha(aluno.getCd_aluno())) {

            Bitmap fotoAluno = carteirinhaQueries.getImgCarteirinha(aluno.getCd_aluno());

            imgFotoAluno.setImageBitmap(fotoAluno);
        }
        //endregion

        //region ---- Nome ----
        String[] nomeSeparado = aluno.getNome().split(" ");
        String primeiroNome = nomeSeparado[0].toLowerCase();

        txtTitulo.setText(primeiroNome);

        txtNome.setText(aluno.getNome());
        //endregion

        //region ---- RA ----
        String ra = aluno.getNumero_ra() + aluno.getDigito_ra();

        StringBuilder stringBuilder = new StringBuilder(ra.trim());

        while (stringBuilder.substring(0, 1).equals("0")) {

            stringBuilder.delete(0, 1);
        }

        stringBuilder.insert(stringBuilder.length() - 1, "-");

        txtRA.setText(stringBuilder.toString());
        //endregion

        //region ---- Turma ativa ----
        if(turmaRegular != null){

            txtTurmaAtual.setText(turmaRegular.getNome_turma());

            txtTipoEnsino.setText(turmaRegular.getTipo_ensino());
        }
        //endregion

        //region ---- RG ----
        if (!aluno.getNumero_rg().contains("null")) {

            txtRG.setText(formatarRG(aluno.getNumero_rg(), aluno.getDigito_rg()));
        }
        //endregion

        //region ---- CPF ----
        if (!aluno.getNumero_cpf().equals("null")) {
            try{

                String cpf = aluno.getNumero_cpf();

                StringBuilder stringBuilderCpf = new StringBuilder(cpf.trim());

                while (stringBuilderCpf.substring(0, 1).equals("0")) {

                    stringBuilderCpf.delete(0, 1);
                }

                txtCPF.setText(stringBuilderCpf.toString());
            }
            catch (Exception e){

                txtCPF.setText(aluno.getNumero_cpf());
            }


        }
        //endregion

        //region ---- Data de Nascimento ----
        if (!aluno.getData_nascimento().equals("null")) {

            txtDataNasc.setText(aluno.getData_nascimento());
        }
        //endregion

        //region ---- Local de Nascimento ----
        String localNascimento = "";

        if (!aluno.getMunicipio_nascimento().equals("null")) {

            localNascimento = aluno.getMunicipio_nascimento();

            if (!aluno.getUf_nascimento().equals("null")) {

                localNascimento = localNascimento + " - " + aluno.getUf_nascimento();
            }

            txtLocalNasc.setText(localNascimento);
        }
        else if (!aluno.getUf_nascimento().equals("null")) {

            localNascimento = aluno.getUf_nascimento();

            txtLocalNasc.setText(localNascimento);
        }
        //endregion

        //region ---- Nacionalidade ----
        if (!aluno.getNacionalidade().equals("null")) {

            txtNacionalidade.setText(aluno.getNacionalidade());
        }
        //endregion

        //region ---- Nome da Mãe ----
        if (!aluno.getNomeMae().equals("null")) {

            txtNomeMae.setText(aluno.getNomeMae());
        }
        //endregion

        //region ---- Nome do Pai ----
        if (!aluno.getNomePai().equals("null")) {

            txtNomePai.setText(aluno.getNomePai());
        }
        //endregion

        //region ---- Endereço ----
        //Logradouro
        if(endereco.getTipo_logradouro() == 1){

            txtTipoLogradouro.setText("RURAL");
        }
        if(endereco.getTipo_logradouro() == 0){

            txtTipoLogradouro.setText("URBANA");
        }

        //Localização diferenciada
        if(endereco.getLocalizacao_diferenciada() == 1){

            txtLocDiferenciada.setText(("Área de Assentamento").toUpperCase());
        }
        if(endereco.getLocalizacao_diferenciada() == 2){

            txtLocDiferenciada.setText(("Terra indígena").toUpperCase());
        }
        if(endereco.getLocalizacao_diferenciada() == 3){

            txtLocDiferenciada.setText(("Área onde se localiza comunidade remanescente de quilombos").toUpperCase());
        }
        if(endereco.getLocalizacao_diferenciada() == 7){

            txtLocDiferenciada.setText(("Não está em área de localização diferenciada").toUpperCase());
        }

        //Endereço
        if(!endereco.getEndereco().equals("null")){

            txtEndereco.setText(endereco.getEndereco() + ", " + endereco.getNumero_endereco());
        }
        if(!endereco.getComplemento().equals("null")){

            txtComplemento.setText(endereco.getComplemento());
        }
        if(!endereco.getBairro().equals("null")){

            txtBairro.setText(endereco.getBairro());
        }
        if(!endereco.getCidade().equals("null")){

            txtCidadeUf.setText(endereco.getCidade() + " - SP");
        }
        if(!endereco.getNumero_cep().equals("null")){

            txtCEP.setText(endereco.getNumero_cep());
        }
        if(endereco.getLongitude().equals("null")){

            layoutEndereco.setBackgroundResource(R.drawable.shape_quadrado_borda_vermelho);
        }

        btnEditarEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(turmaRegular == null){


                }
                else if(idadeAluno >= 18 || mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL){

                    mostrarDialogEdicaoEndereco();
                }
                else{

                    mostrarAvisoMenorIdade();
                }
            }
        });
        //endregion

        //region ---- Comprovante de residência ----
        layoutComprovante.setVisibility(View.GONE);
        //endregion

        //region ---- Telefone ----

        telefoneAdapter = new TelefoneAdapter(listaContatos, null, this);

        lvTelefoneAluno.setAdapter(telefoneAdapter);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float logicalDensity = metrics.density;

        int px = (int) Math.ceil(40 * logicalDensity);

        px = (px * telefoneAdapter.getCount()) + px ;

        lvTelefoneAluno.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, px,0));

        btnAddTelAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(turmaRegular != null){


                    addContatoAluno();
                }
            }
        });

        //endregion

        //region ---- E-mail ----
        if(!aluno.getEmail_aluno().equals("null")){

            txtEmail.setText(aluno.getEmail_aluno());
        }
        else{

            if(idadeAluno >= 18 && aluno.isResponde_rematricula() && turmaRegular != null){

                layoutEmailAluno.setBackgroundResource(R.drawable.shape_quadrado_borda_vermelho);
            }
        }

        btnEditarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(turmaRegular == null){


                }
                else if(idadeAluno >= 18 || mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL){

                    mostrarDialogEdicaoEmailAluno();
                }
                else{

                    mostrarAvisoMenorIdade();
                }
            }
        });


        //endregion

        //region ---- Telefone Responsável ----
        if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL){

            telefoneRespAdapter = new TelefoneAdapter(null, contatosResponsavel, this);

            lvTelefoneResp.setAdapter(telefoneRespAdapter);

            int pxR = (int) Math.ceil(40 * logicalDensity);

            pxR = (pxR * telefoneRespAdapter.getCount()) + pxR ;

            lvTelefoneResp.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pxR,0));

            btnAddTelResp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(turmaRegular != null){

                        addContatoResp();
                    }

                }
            });
        }
        else{

            layout_telefonesResponsavel.setVisibility(View.GONE);
        }
        //endregion

        //region ---- E-mail Responsável ----
        if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL){

            txtEmailResponsavel.setText(responsavel.getEmail());
        }
        else{

            layout_emailResponsavel.setVisibility(View.GONE);
        }

        btnEditarEmailResponsavel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(turmaRegular != null){

                    mostrarDialogEdicaoEmailResponsavel();
                }
            }
        });
        //endregion

        if(turmaRegular == null){

            btnSalvar.setVisibility(View.GONE);
            return;
        }

        //region ---- Questionario Rematricula ----

        if((mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL && aluno.isResponde_rematricula()) ||
                (aluno.isResponde_rematricula() && aluno.getIdade() >= 18)){

            layoutRematricula.setVisibility(View.VISIBLE);

            AlertDialog alertDialog = new AlertDialog.Builder(SobreMimActivity.this).create();

            alertDialog.setMessage("Preencha todas as informações para realizar a rematrícula");


            alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });

            alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.shape_dialog);

            alertDialog.show();

            opcRematriculaSim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {

                    if(isCheck){

                        opcRematriculaNao.setChecked(false);
                        layoutCheck.setVisibility(View.VISIBLE);
                        rematricula.setInteresse_continuidade(true);
                        atualizou_questionario = true;

                        if(compoundButton.isPressed() ){

                            if(!endereco.isEnvio_comprovante()){

                                montarLayoutEnvioComprovante();
                            }
                        }
                    }
                }
            });

            opcRematriculaNao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {

                    if(isCheck){

                        opcRematriculaSim.setChecked(false);
                        layoutCheck.setVisibility(View.GONE);
                        rematricula.setInteresse_continuidade(false);
                        atualizou_questionario = true;
                        if(compoundButton.isPressed()){

                            if(!endereco.isEnvio_comprovante()){

                                montarLayoutEnvioComprovante();
                            }
                        }

                        checkOpcNoturno.setChecked(false);
                        checkOpcIntegral.setChecked(false);
                        checkOpcEspanhol.setChecked(false);
                        checkOpcNovatec.setChecked(false);

                        rematricula.setInteresse_noturno(false);
                        rematricula.setInteresse_turno_integral(false);
                        rematricula.setInteresse_novotec(false);
                        rematricula.setInteresse_espanhol(false);
                    }
                }
            });

            if(turmaRegular.getCd_tipo_ensino() == 14){

                //checkOpcNoturno.setVisibility(View.GONE);
                checkOpcNovatec.setVisibility(View.GONE);
            }
            if(turmaRegular.getNome_turma().contains("9") && turmaRegular.getCd_tipo_ensino() == 14){

                checkOpcEspanhol.setVisibility(View.VISIBLE);
            }
        }

        //region  --- onclick das opções ---
        checkOpcNovatec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){

                    layoutNovatec.setVisibility(View.VISIBLE);

                    rematricula.setInteresse_novotec(true);

                    checkOpcNoturno.setChecked(false);
                    rematricula.setInteresse_noturno(false);

                    checkOpcIntegral.setChecked(false);
                    rematricula.setInteresse_turno_integral(false);
                }
                else{

                    layoutNovatec.setVisibility(View.GONE);
                    uncheckMateriasNovotec();
                    rematricula.setInteresse_novotec(false);
                }

                atualizou_questionario = true;
                if(compoundButton.isPressed()){

                    if(!endereco.isEnvio_comprovante()){

                        montarLayoutEnvioComprovante();
                    }
                }
            }
        });

        checkOpcNoturno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){

                    checkOpcIntegral.setChecked(false);
                    rematricula.setInteresse_turno_integral(false);

                    checkOpcNovatec.setChecked(false);
                    rematricula.setInteresse_novotec(false);

                    rematricula.setInteresse_noturno(true);

                    layoutJustificativaNoturno.setVisibility(View.VISIBLE);

                }
                else{

                    rematricula.setInteresse_noturno(false);
                    layoutJustificativaNoturno.setVisibility(View.GONE);

                    checkOpcNoturnoTrabalho.setChecked(false);
                    checkOpcNoturnoCursos.setChecked(false);
                    checkOpcNoturnoOpcao.setChecked(false);
                    checkOpcNoturnoOutros.setChecked(false);

                    rematricula.setObservacao_opc_noturno(0);
                }
                atualizou_questionario = true;
                if(compoundButton.isPressed()){

                    if(!endereco.isEnvio_comprovante()){

                        montarLayoutEnvioComprovante();
                    }
                }
            }
        });

        checkOpcIntegral.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){

                    rematricula.setInteresse_novotec(false);
                    checkOpcNovatec.setChecked(false);

                    rematricula.setInteresse_noturno(false);
                    checkOpcNoturno.setChecked(false);

                    rematricula.setInteresse_turno_integral(true);
                }
                else{

                    rematricula.setInteresse_turno_integral(false);
                }

                atualizou_questionario = true;
                if(compoundButton.isPressed()){

                    if(!endereco.isEnvio_comprovante()){

                        montarLayoutEnvioComprovante();
                    }
                }
            }
        });

        checkOpcEspanhol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){

                    rematricula.setInteresse_espanhol(true);

                }
                else{

                    rematricula.setInteresse_espanhol(false);
                }

                atualizou_questionario = true;
                if(compoundButton.isPressed()){

                    if(!endereco.isEnvio_comprovante()){

                        montarLayoutEnvioComprovante();
                    }
                }
            }
        });

        //endregion

        //region ---- onClick das matérias novotec ----

        ArrayList<String> listaMateria1 = new ArrayList<>();

        listaMateria1.add("Selecione");
        listaMateria1.add("Administração");
        listaMateria1.add("Logística");
        listaMateria1.add("Marketing");
        listaMateria1.add("Recursos Humanos");
        listaMateria1.add("Informática");
        listaMateria1.add("Informática para Internet");
        listaMateria1.add("Redes de Computadores");
        listaMateria1.add("Desenvolvimento de Sistemas");


        ArrayAdapter<String> adapterMateria1 = new ArrayAdapter<String>(this, R.layout.layout_spinner_item_sobre_mim, listaMateria1);

        ArrayAdapter<String> adapterMateria2 = new ArrayAdapter<String>(this, R.layout.layout_spinner_item_sobre_mim, listaMateria1);

        ArrayAdapter<String> adapterMateria3 = new ArrayAdapter<String>(this, R.layout.layout_spinner_item_sobre_mim, listaMateria1);

        spnMateria1.setAdapter(adapterMateria1);

        spnMateria2.setAdapter(adapterMateria2);

        spnMateria3.setAdapter(adapterMateria3);

        spnMateria1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String materia = (String) adapterView.getItemAtPosition(i);

                if (spnMateria2.getSelectedItem().toString().equals(materia) && !spnMateria2.getSelectedItem().toString().equals("Selecione")){

                    spnMateria2.setSelection(0);
                }

                if (spnMateria3.getSelectedItem().toString().equals(materia) && !spnMateria3.getSelectedItem().toString().equals("Selecione")){

                    spnMateria3.setSelection(0);
                }

                switch (materia){

                    case "Selecione":

                        rematricula.setEixo_ensino_profissional_um(0);
                        break;

                    case "Administração":

                        rematricula.setEixo_ensino_profissional_um(1);
                        break;

                    case "Logística":

                        rematricula.setEixo_ensino_profissional_um(2);
                        break;

                    case "Marketing":

                        rematricula.setEixo_ensino_profissional_um(3);
                        break;

                    case "Recursos Humanos":

                        rematricula.setEixo_ensino_profissional_um(4);
                        break;

                    case "Informática":

                        rematricula.setEixo_ensino_profissional_um(5);
                        break;

                    case "Informática para Internet":

                        rematricula.setEixo_ensino_profissional_um(6);
                        break;

                    case "Redes de Computadores":

                        rematricula.setEixo_ensino_profissional_um(7);
                        break;

                    case "Desenvolvimento de Sistemas":

                        rematricula.setEixo_ensino_profissional_um(8);
                        break;
                }

                if(!endereco.isEnvio_comprovante()){

                    montarLayoutEnvioComprovante();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnMateria2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String materia = (String) adapterView.getItemAtPosition(i);

                if (spnMateria1.getSelectedItem().toString().equals(materia)  && !spnMateria1.getSelectedItem().toString().equals("Selecione")){

                    spnMateria1.setSelection(0);
                }

                if (spnMateria3.getSelectedItem().toString().equals(materia) && !spnMateria3.getSelectedItem().toString().equals("Selecione")){

                    spnMateria3.setSelection(0);
                }

                switch (materia){

                    case "Selecione":

                        rematricula.setEixo_ensino_profissional_dois(0);
                        break;

                    case "Administração":

                        rematricula.setEixo_ensino_profissional_dois(1);
                        break;

                    case "Logística":

                        rematricula.setEixo_ensino_profissional_dois(2);
                        break;

                    case "Marketing":

                        rematricula.setEixo_ensino_profissional_dois(3);
                        break;

                    case "Recursos Humanos":

                        rematricula.setEixo_ensino_profissional_dois(4);
                        break;

                    case "Informática":

                        rematricula.setEixo_ensino_profissional_dois(5);
                        break;

                    case "Informática para Internet":

                        rematricula.setEixo_ensino_profissional_dois(6);
                        break;

                    case "Redes de Computadores":

                        rematricula.setEixo_ensino_profissional_dois(7);
                        break;

                    case "Desenvolvimento de Sistemas":

                        rematricula.setEixo_ensino_profissional_dois(8);
                        break;
                }

                if(!endereco.isEnvio_comprovante()){

                    montarLayoutEnvioComprovante();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnMateria3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String materia = (String) adapterView.getItemAtPosition(i);

                if (spnMateria2.getSelectedItem().toString().equals(materia)  && !spnMateria2.getSelectedItem().toString().equals("Selecione")){

                    spnMateria2.setSelection(0);
                }

                if (spnMateria1.getSelectedItem().toString().equals(materia)  && !spnMateria1.getSelectedItem().toString().equals("Selecione")){

                    spnMateria1.setSelection(0);
                }

                switch (materia){

                    case "Selecione":

                        rematricula.setEixo_ensino_profissional_tres(0);
                        break;

                    case "Administração":

                        rematricula.setEixo_ensino_profissional_tres(1);
                        break;

                    case "Logística":

                        rematricula.setEixo_ensino_profissional_tres(2);
                        break;

                    case "Marketing":

                        rematricula.setEixo_ensino_profissional_tres(3);
                        break;

                    case "Recursos Humanos":

                        rematricula.setEixo_ensino_profissional_tres(4);
                        break;

                    case "Informática":

                        rematricula.setEixo_ensino_profissional_tres(5);
                        break;

                    case "Informática para Internet":

                        rematricula.setEixo_ensino_profissional_tres(6);
                        break;

                    case "Redes de Computadores":

                        rematricula.setEixo_ensino_profissional_tres(7);
                        break;

                    case "Desenvolvimento de Sistemas":

                        rematricula.setEixo_ensino_profissional_tres(8);
                        break;
                }

                if(!endereco.isEnvio_comprovante()){

                    montarLayoutEnvioComprovante();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //endregion

        //region ---- onClick da justificativa noturno ----

        checkOpcNoturnoTrabalho.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){

                    checkOpcNoturnoCursos.setChecked(false);
                    checkOpcNoturnoOpcao.setChecked(false);
                    checkOpcNoturnoOutros.setChecked(false);

                    rematricula.setObservacao_opc_noturno(1);
                }
                else{

                    if(rematricula.getObservacao_opc_noturno() == 1){

                        rematricula.setObservacao_opc_noturno(0);
                    }
                }

                atualizou_questionario = true;
                if(compoundButton.isPressed()){

                    if(!endereco.isEnvio_comprovante()){

                        montarLayoutEnvioComprovante();
                    }
                }
            }
        });

        checkOpcNoturnoCursos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){

                    checkOpcNoturnoTrabalho.setChecked(false);
                    checkOpcNoturnoOpcao.setChecked(false);
                    checkOpcNoturnoOutros.setChecked(false);

                    rematricula.setObservacao_opc_noturno(2);
                }
                else{

                    if(rematricula.getObservacao_opc_noturno() == 2){

                        rematricula.setObservacao_opc_noturno(0);
                    }
                }

                atualizou_questionario = true;
                if(compoundButton.isPressed()){

                    if(!endereco.isEnvio_comprovante()){

                        montarLayoutEnvioComprovante();
                    }
                }
            }
        });

        checkOpcNoturnoOpcao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){

                    checkOpcNoturnoCursos.setChecked(false);
                    checkOpcNoturnoTrabalho.setChecked(false);
                    checkOpcNoturnoOutros.setChecked(false);

                    rematricula.setObservacao_opc_noturno(3);
                }
                else{

                    if(rematricula.getObservacao_opc_noturno() == 3){

                        rematricula.setObservacao_opc_noturno(0);
                    }
                }

                atualizou_questionario = true;
                if(compoundButton.isPressed()){

                    if(!endereco.isEnvio_comprovante()){

                        montarLayoutEnvioComprovante();
                    }
                }
            }
        });

        checkOpcNoturnoOutros.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){

                    checkOpcNoturnoCursos.setChecked(false);
                    checkOpcNoturnoOpcao.setChecked(false);
                    checkOpcNoturnoTrabalho.setChecked(false);

                    rematricula.setObservacao_opc_noturno(4);
                }
                else{

                    if(rematricula.getObservacao_opc_noturno() == 4){

                        rematricula.setObservacao_opc_noturno(0);
                    }
                }

                atualizou_questionario = true;
                if(compoundButton.isPressed()){

                    if(!endereco.isEnvio_comprovante()){

                        montarLayoutEnvioComprovante();
                    }
                }
            }
        });

        //endregion

        //region --- recuperar matricula salva ---
        if(rematricula != null){

            if(rematricula.isInteresse_continuidade()){

                opcRematriculaSim.setChecked(true);

                if(rematricula.isInteresse_novotec()){

                    checkOpcNovatec.setChecked(true);
                    layoutNovatec.setVisibility(View.VISIBLE);

                    spnMateria1.setSelection(rematricula.getEixo_ensino_profissional_um());
                    spnMateria2.setSelection(rematricula.getEixo_ensino_profissional_dois());
                    spnMateria3.setSelection(rematricula.getEixo_ensino_profissional_tres());

                }
                if(rematricula.isInteresse_turno_integral()){

                    checkOpcIntegral.setChecked(true);
                }
                if(rematricula.isInteresse_espanhol()){

                    checkOpcEspanhol.setChecked(true);
                }
                if(rematricula.isInteresse_noturno()){

                    checkOpcNoturno.setChecked(true);

                    if(rematricula.getObservacao_opc_noturno() != 0){

                        switch (rematricula.getObservacao_opc_noturno()){

                            case 1:

                                checkOpcNoturnoTrabalho.setChecked(true);
                                break;

                            case 2:
                                checkOpcNoturnoCursos.setChecked(true);
                                break;

                            case 3:
                                checkOpcNoturnoOpcao.setChecked(true);
                                break;

                            case 4:
                                checkOpcNoturnoOutros.setChecked(true);
                                break;
                        }
                    }
                }
            }
            else{

                opcRematriculaNao.setChecked(true);
            }

            atualizou_questionario = false;
        }
        else{

            rematricula = new Rematricula();

            if(rematricula.getAno_letivo() == 0){

                rematricula.setAno_letivo(turmaRegular.getAno_letivo());
                rematricula.setAno_letivo_rematricula(turmaRegular.getAno_letivo() + 1);
            }
        }
        //endregion

        //endregion

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_ALUNO && aluno.getIdade() < 18){

                    Toast.makeText(SobreMimActivity.this, "Somente alunos maiores de 18 anos e responsáveis podem realizar a rematrícula.", Toast.LENGTH_LONG).show();
                }
                else if(atualizou_endereco && !atualizou_comprovante){

                    Toast.makeText(SobreMimActivity.this, "Envie o comprovante de endereço!", Toast.LENGTH_LONG).show();
                }
                else if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL && responsavel.getEmail().equals("null")){

                    Toast.makeText(SobreMimActivity.this, "É necessário cadastrar um e-mail.", Toast.LENGTH_LONG).show();
                }
                else if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_ALUNO && aluno.getEmail_aluno().equals("null")){

                    Toast.makeText(SobreMimActivity.this, "É necessário cadastrar um e-mail.", Toast.LENGTH_LONG).show();
                }
                else if(!aluno.isResponde_rematricula()){

                    Toast.makeText(SobreMimActivity.this, "A rematricula não está disponível no momento.", Toast.LENGTH_LONG).show();
                }
                else if(!opcRematriculaSim.isChecked() && !opcRematriculaNao.isChecked()){

                    Toast.makeText(SobreMimActivity.this, "Selecione 'Sim' ou 'Não' se deseja continuar na rede pública no próximo ano", Toast.LENGTH_LONG).show();
                }
                else if(opcRematriculaSim.isChecked() && checkOpcNovatec.isChecked() && spnMateria1.getSelectedItemPosition() == 0){

                    Toast.makeText(SobreMimActivity.this, "Preencha a primeira opção de curso do ensino profissionalizante.", Toast.LENGTH_LONG).show();
                }
                else if(atualizou_questionario && !atualizou_comprovante && !endereco.isEnvio_comprovante()){

                    Toast.makeText(SobreMimActivity.this, "É necessário enviar um comprovante de residência para realizar a rematrícula.", Toast.LENGTH_LONG).show();
                }
                else{

                    mostrarTermos();
                }
            }
        });

        contatosResponsavelDeletar = new ArrayList<>();
        contatosAlunoDeletar = new ArrayList<>();

    }

    private void uncheckMateriasNovotec() {

        atualizou_questionario = true;

        if(!endereco.isEnvio_comprovante()){

            montarLayoutEnvioComprovante();
        }

        rematricula.setEixo_ensino_profissional_um(0);

        rematricula.setEixo_ensino_profissional_dois(0);

        rematricula.setEixo_ensino_profissional_tres(0);

        spnMateria1.setSelection(0);

        spnMateria2.setSelection(0);

        spnMateria3.setSelection(0);

    }

    public String formatarRG(String rg, String digito) {

        long rg_long = Long.valueOf(rg);

        String rgFormatado = String.valueOf(rg_long) + "-" + digito;

        return rgFormatado;
    }

    @OnClick(R.id.sobre_foto) public void onClick() {

        if (mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

            Toast.makeText(SobreMimActivity.this,
                    "Somente o aluno pode enviar a foto para aprovação",
                    Toast.LENGTH_SHORT)
                    .show();
        }
        else if (!carteirinhaQueries.temCarteirinha(aluno.getCd_aluno())) {

            startActivity(new Intent(SobreMimActivity.this, EnviarFotoActivity.class));
        }
    }

    public void voltarSobreMenu(View v) {

        onBackPressed();
    }

    public void mostrarDialogEdicaoEndereco(){

        if(turmaRegular == null){

            return;
        }

        AlertDialog dialogEdicaoEndereco;

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(SobreMimActivity.this);


        View viewDialog = getLayoutInflater().inflate(R.layout.dialog_editar_endereco, null);

        Spinner spnLogradouro = viewDialog.findViewById(R.id.sobre_edit_endereco_logradouro);

        if(endereco.getTipo_logradouro() < 2){

            spnLogradouro.setSelection(endereco.getTipo_logradouro());
        }

        final int tipoLogOf = endereco.getTipo_logradouro();

        spnLogradouro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String logradouro = (String) adapterView.getItemAtPosition(i);

                switch (logradouro){

                    case "Urbana":

                        endereco.setTipo_logradouro(0);
                        break;

                    case "Rural":

                        endereco.setTipo_logradouro(1);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Spinner spnLocDif = viewDialog.findViewById(R.id.sobre_edit_endereco_loc_dif);

        ArrayAdapter adapter = (ArrayAdapter) spnLocDif.getAdapter();

        String locDifOf = "";

        final int idLocDifOf = endereco.getLocalizacao_diferenciada();

        switch (endereco.getLocalizacao_diferenciada()){

            case 7:

                locDifOf = "Não está em área de localização diferenciada";
                break;

            case 2:

                locDifOf = "Terra indígena";
                break;

            case 1:

                locDifOf = "Área de Assentamento";
                break;

            case 3:

                locDifOf = "Área onde se localiza comunidade remanescene de quilombos";
                break;
        }

        int locDifPositionOf = adapter.getPosition(locDifOf);

        spnLocDif.setSelection(locDifPositionOf);

        spnLocDif.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String loc_dif = (String) adapterView.getItemAtPosition(i);

                switch (loc_dif){

                    case "Não está em área de localização diferenciada":

                        endereco.setLocalizacao_diferenciada(7);
                        break;

                    case "Terra indígena":

                        endereco.setLocalizacao_diferenciada(2);
                        break;

                    case "Área de Assentamento":

                        endereco.setLocalizacao_diferenciada(1);
                        break;

                    case "Área onde se localiza comunidade remanescene de quilombos":

                        endereco.setLocalizacao_diferenciada(3);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

        });

        final EditText editEndereco = viewDialog.findViewById(R.id.sobre_edit_endereco_endereco);
        if(!endereco.getEndereco().equals("null")){

            editEndereco.setText(endereco.getEndereco());
        }

        final EditText editNumero = viewDialog.findViewById(R.id.sobre_edit_endereco_numero);
        if(!endereco.getNumero_endereco().equals("null")){

            editNumero.setText(endereco.getNumero_endereco());
        }

        final EditText editComplemento = viewDialog.findViewById(R.id.sobre_edit_endereco_complemento);
        if(!endereco.getComplemento().equals("null")){

            editComplemento.setText(endereco.getComplemento());
        }

        final EditText editBairro = viewDialog.findViewById(R.id.sobre_edit_endereco_bairro);
        if(!endereco.getBairro().equals("null")){

            editBairro.setText(endereco.getBairro());
        }

        final Spinner spnCidade = viewDialog.findViewById(R.id.sobre_edit_endereco_cidade);
        if(!endereco.getCidade().equals("null")){

            ArrayList<String> municipios = usuarioQueries.getMunicipios();

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, municipios);

            spnCidade.setAdapter(arrayAdapter);

            int spinnerPosition = arrayAdapter.getPosition(endereco.getCidade());

            spnCidade.setSelection(spinnerPosition);

            spnCidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    endereco.setCidade(adapterView.getItemAtPosition(i).toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        final EditText editCep = viewDialog.findViewById(R.id.sobre_edit_endereco_cep);
        if(!endereco.getNumero_cep().equals("null")){

            editCep.setText(endereco.getNumero_cep());
        }

        mDialogBuilder.setView(viewDialog);

        mDialogBuilder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                endereco.setEndereco(editEndereco.getText().toString());
                endereco.setNumero_endereco(editNumero.getText().toString());
                endereco.setComplemento(editComplemento.getText().toString());
                endereco.setBairro(editBairro.getText().toString());

                endereco.setNumero_cep(editCep.getText().toString());

                txtEndereco.setText(endereco.getEndereco() + ", " + endereco.getNumero_endereco());
                txtComplemento.setText(endereco.getComplemento());
                txtBairro.setText(endereco.getBairro());
                txtCidadeUf.setText(endereco.getCidade() + " - " + "SP");
                txtCEP.setText(endereco.getNumero_cep());

                atualizou_endereco = true;

                GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
                int result = googleApiAvailability.isGooglePlayServicesAvailable(SobreMimActivity.this);

                if (result == ConnectionResult.SUCCESS) {

                    Intent mapaActivityIntent = new Intent(SobreMimActivity.this, MapaActivity.class);

                    mapaActivityIntent.putExtra(MapaActivity.ENDERECO_KEY,
                            endereco.getEndereco() + ", " + endereco.getNumero_endereco() +
                                    " - " + endereco.getBairro() + ", " + endereco.getCidade() +
                                    " - SP, " + endereco.getNumero_cep() + ", Brasil");

                    startActivityForResult(mapaActivityIntent, COD_RESULT_GEO);
                }
                else {

                    googleApiAvailability.makeGooglePlayServicesAvailable(SobreMimActivity.this);
                }

                dialogInterface.dismiss();
            }
        });

        mDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                endereco.setTipo_logradouro(tipoLogOf);

                endereco.setLocalizacao_diferenciada(idLocDifOf);

                dialogInterface.dismiss();
            }
        });

        dialogEdicaoEndereco = mDialogBuilder.create();

        dialogEdicaoEndereco.getWindow().setBackgroundDrawableResource(R.drawable.shape_dialog);

        dialogEdicaoEndereco.setCanceledOnTouchOutside(false);

        dialogEdicaoEndereco.show();
    }

    public void mostrarDialogEdicaoEmailAluno(){

        AlertDialog dialogEdicaoEmail;

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(SobreMimActivity.this);

        View viewDialog = getLayoutInflater().inflate(R.layout.dialog_editar_email, null);

        final TextView txtEmailAtual = viewDialog.findViewById(R.id.sobre_edit_email_atual);
        if(!aluno.getEmail_aluno().equals("null")){

            txtEmailAtual.setText(aluno.getEmail_aluno());
        }
        else{

            txtEmailAtual.setText("Nenhum");
        }

        final EditText editEmailNovo = viewDialog.findViewById(R.id.sobre_edit_email_novo);

        final EditText editEmailConfirma = viewDialog.findViewById(R.id.sobre_edit_email_confirma);

        mDialogBuilder.setView(viewDialog);

        mDialogBuilder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(editEmailNovo.getText().toString().length() < 8){

                    Toast.makeText(SobreMimActivity.this, "Digite um e-mail valido.", Toast.LENGTH_SHORT).show();
                }
                else{

                    if(editEmailNovo.getText().toString().equals(editEmailConfirma.getText().toString())){

                        if(aluno.getEmail_aluno().equals("null")){

                            criou_email_aluno = true;
                        }
                        else{

                            atualizou_email_aluno = true;
                        }

                        aluno.setEmail_aluno(editEmailNovo.getText().toString());

                        txtEmail.setText(aluno.getEmail_aluno());


                        layoutEmailAluno.setBackgroundResource(R.drawable.shape_quadrado_borda);

                        Toast.makeText(SobreMimActivity.this, "E-mail salvo!", Toast.LENGTH_SHORT).show();

                        dialogInterface.dismiss();
                    }
                    else{

                        Toast.makeText(SobreMimActivity.this, "Os e-mails digitados não são iguais.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        mDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        dialogEdicaoEmail = mDialogBuilder.create();

        dialogEdicaoEmail.getWindow().setBackgroundDrawableResource(R.drawable.shape_dialog);

        dialogEdicaoEmail.setCanceledOnTouchOutside(false);

        dialogEdicaoEmail.show();
    }

    public void mostrarDialogEdicaoEmailResponsavel(){

        AlertDialog dialogEdicaoEmail;

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(SobreMimActivity.this);

        View viewDialog = getLayoutInflater().inflate(R.layout.dialog_editar_email, null);

        final TextView txtEmailAtual = viewDialog.findViewById(R.id.sobre_edit_email_atual);
        if(!responsavel.getEmail().equals("null")){

            txtEmailAtual.setText(responsavel.getEmail());
        }
        else{

            txtEmailAtual.setText("Nenhum");
        }

        final EditText editEmailNovo = viewDialog.findViewById(R.id.sobre_edit_email_novo);

        final EditText editEmailConfirma = viewDialog.findViewById(R.id.sobre_edit_email_confirma);

        mDialogBuilder.setView(viewDialog);

        mDialogBuilder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(editEmailNovo.getText().toString().length() < 8){

                    Toast.makeText(SobreMimActivity.this, "Digite um e-mail valido.", Toast.LENGTH_SHORT).show();
                }
                else{

                    if(editEmailNovo.getText().toString().equals(editEmailConfirma.getText().toString())){

                        if(responsavel.getEmail().equals("null")){

                            criou_email_responsavel = true;
                        }
                        else{

                            atualizou_email_responsavel = true;
                        }

                        responsavel.setEmail(editEmailNovo.getText().toString());

                        txtEmailResponsavel.setText(responsavel.getEmail());


                        Toast.makeText(SobreMimActivity.this, "E-mail salvo!", Toast.LENGTH_SHORT).show();

                        dialogInterface.dismiss();
                    }
                    else{

                        Toast.makeText(SobreMimActivity.this, "Os e-mails digitados não são iguais.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        mDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        dialogEdicaoEmail = mDialogBuilder.create();

        dialogEdicaoEmail.getWindow().setBackgroundDrawableResource(R.drawable.shape_dialog);

        dialogEdicaoEmail.setCanceledOnTouchOutside(false);

        dialogEdicaoEmail.show();
    }

    public void montarLayoutEnvioComprovante(){

        layoutComprovante.setVisibility(View.VISIBLE);


        if((atualizou_endereco && !atualizou_comprovante) ||
                (atualizou_questionario && !endereco.isEnvio_comprovante() && !atualizou_comprovante)){

            layoutComprovante.setBackgroundResource(R.drawable.shape_quadrado_borda_vermelho);
        }
        else{

            layoutComprovante.setBackgroundResource(R.drawable.shape_quadrado_borda);

            txtStatusComprovante.setText("Não selecionado");
        }

        //region -- Ajuda Comprovante --
        btnAjudaComprovante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog dialogAjudaComprovante;

                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(SobreMimActivity.this);

                View viewDialog = getLayoutInflater().inflate(R.layout.dialog_ajuda_comprovante, null);

                mDialogBuilder.setView(viewDialog);

                mDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                    }
                });

                dialogAjudaComprovante = mDialogBuilder.create();

                dialogAjudaComprovante.getWindow().setBackgroundDrawableResource(R.drawable.shape_dialog);

                dialogAjudaComprovante.show();
            }
        });
        //endregion

        btnEnvioComprovante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(SobreMimActivity.this);

                View viewDialog = getLayoutInflater().inflate(R.layout.dialog_selecao_comprovante, null);

                mDialogBuilder.setView(viewDialog);

                RelativeLayout btnEnvioCamera = viewDialog.findViewById(R.id.comprovante_btn_camera);

                RelativeLayout btnEnvioGaleria = viewDialog.findViewById(R.id.comprovante_btn_galeria);

                RelativeLayout btnEnvioPdf = viewDialog.findViewById(R.id.comprovante_btn_pdf);

                //region Enviar comprovante da camera

                btnEnvioCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(!hasPermissions(SobreMimActivity.this, PERMISSIONS_CAMERA)) {

                            ActivityCompat.requestPermissions(SobreMimActivity.this, PERMISSIONS_CAMERA, PERMISSION_ALL);
                        }
                        else {

                            try {

                                tirarFoto();

                                dialogAjudaComprovante.dismiss();
                            }
                            catch (Exception e) {

                                e.printStackTrace();
                            }
                        }
                    }
                });

                //endregion

                //region Enviar comprovante da galeria
                btnEnvioGaleria.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(!hasPermissions(SobreMimActivity.this, PERMISSIONS_ALBUM)) {

                            ActivityCompat.requestPermissions(SobreMimActivity.this, PERMISSIONS_ALBUM, PERMISSION_ALL);
                        }
                        else {

                            takeAlbumAction(SobreMimActivity.this);
                        }
                    }
                });
                //endregion

                //region comprovante PDF

                btnEnvioPdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        FileChooser fileChooser = new FileChooser(SobreMimActivity.this);

                        fileChooser.setFileListener(new FileChooser.FileSelectedListener() {
                            @Override
                            public void fileSelected(final File file) {

                                String filename = file.getAbsolutePath();

                                Log.i("File Name", filename);

                                try {

                                    byte[] bytes = new byte[(int) file.length()];

                                    DataInputStream dis = new DataInputStream(new FileInputStream(file));

                                    dis.readFully(bytes);

                                    Base64.encode(bytes, android.util.Base64.DEFAULT);

                                    pdfBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);

                                    isPDF = true;

                                    atualizou_comprovante = true;

                                    layoutComprovante.setBackgroundResource(R.drawable.shape_quadrado_borda);

                                    txtStatusComprovante.setText("Selecionado");

                                    dialogAjudaComprovante.dismiss();

                                }
                                catch(Exception e) {

                                    e.printStackTrace();

                                    Toast.makeText(SobreMimActivity.this, "Falha ao pegar arquivo PDF.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        fileChooser.showDialog();

                    }
                });

                //endregion

                dialogAjudaComprovante = mDialogBuilder.create();

                dialogAjudaComprovante.getWindow().setBackgroundDrawableResource(R.drawable.shape_dialog);

                dialogAjudaComprovante.show();
            }
        });
    }

    private void tirarFoto() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            try {

                foto = criarArquivoFoto();

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

                Uri contentUri = Uri.fromFile(foto);

                mediaScanIntent.setData(contentUri);

                this.sendBroadcast(mediaScanIntent);
            }
            catch (Exception ex) {

                ex.printStackTrace();
            }

            if (foto != null) {

                try {

                    Uri photoURI = FileProvider.getUriForFile(this, "br.gov.sp.educacao.minhaescola.fileprovider", foto);//Uri.fromFile(foto);

                    grantUriPermission("br.gov.sp.educacao.minhaescola.fileprovider", photoURI, takePictureIntent.FLAG_GRANT_READ_URI_PERMISSION);

                    grantUriPermission("br.gov.sp.educacao.minhaescola.fileprovider", photoURI, takePictureIntent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                    startActivityForResult(takePictureIntent, 5);
                }
                catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }
    }

    public boolean hasPermissions(Context context, String... permissions) {

        if (context != null
                && permissions != null) {

            for (String permission : permissions) {

                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

                    return false;
                }
            }
        }

        return true;
    }

    public void mostrarAvisoMenorIdade(){

        Toast.makeText(SobreMimActivity.this,
                "Somente alunos com 18 anos ou mais podem atualizar os dados.",
                Toast.LENGTH_SHORT).show();
    }

    public void irHistorico(View v) {

        Bundle params = new Bundle();

        params.putInt("ButtonID", v.getId());

        String btnName = "Mód_HistóricoMatrícula";

        Intent intent = new Intent(this, HistoricoMatriculaActivity.class);

        intent.putExtra("cd_aluno", aluno.getCd_aluno());

        startActivity(intent);

        //Tracker Módulo
        if (URL_SERVIDOR.equals(URLSERVIDOR_TRACKER)) {

            params.putString(FirebaseAnalytics.Param.CONTENT_TYPE, btnName);

            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, params);
            mFirebaseAnalytics.logEvent(btnName, params);
        }
    }

    public void addContatoAluno(){

        if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_ALUNO && aluno.getIdade() < 18){

            Toast.makeText(SobreMimActivity.this, "Somente alunos maiores de 18 anos e responsáveis podem realizar alterações.", Toast.LENGTH_LONG).show();

            return;
        }

        final ContatoAluno contato = new ContatoAluno();

        AlertDialog dialogEdicaoTelefone;

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(SobreMimActivity.this);

        View viewDialog = getLayoutInflater().inflate(R.layout.dialog_editar_telefone, null);

        final EditText editDDD = viewDialog.findViewById(R.id.sobre_edit_ddd);

        final EditText editTelefone = viewDialog.findViewById(R.id.sobre_edit_telefone);

        final Spinner spnTipoTelefone = viewDialog.findViewById(R.id.sobre_spn_tipo_telefone);

        final CheckBox chkSms = viewDialog.findViewById(R.id.sobre_check_sms_telefone);

        final EditText edtComplemento = viewDialog.findViewById(R.id.sobre_edit_complemento_telefone);

        chkSms.setVisibility(View.GONE);

        spnTipoTelefone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String tipoTelefone = adapterView.getItemAtPosition(i).toString();

                switch (tipoTelefone){

                    case "Residencial" :

                        contato.setCodigo_tipo_telefone(1);
                        break;

                    case "Comercial" :

                        contato.setCodigo_tipo_telefone(2);
                        break;

                    case "Celular" :

                        contato.setCodigo_tipo_telefone(3);
                        break;

                    case "Recados" :

                        contato.setCodigo_tipo_telefone(4);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mDialogBuilder.setView(viewDialog);

        mDialogBuilder.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(editDDD.getText().toString().length() < 2){

                    Toast.makeText(SobreMimActivity.this, "DDD incorreto", Toast.LENGTH_SHORT).show();
                }
                else if(editTelefone.getText().toString().length() < 8){

                    Toast.makeText(SobreMimActivity.this, "Telefone incorreto", Toast.LENGTH_SHORT).show();
                }
                else{

                    contato.setCodigo_ddd(editDDD.getText().toString());
                    contato.setTelefone_aluno(editTelefone.getText().toString());
                    if(chkSms.isChecked()){

                        contato.setValidacao_sms(1);
                    }
                    else{

                        contato.setValidacao_sms(0);
                    }
                    contato.setOperacao(OPERACAO_INSERIR);
                    contato.setComplemento_telefone(edtComplemento.getText().toString());
                    contato.setCd_aluno(aluno.getCd_aluno());

                    listaContatos.add(contato);

                    telefoneAdapter.notifyDataSetChanged();

                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);

                    float logicalDensity = metrics.density;

                    int px = (int) Math.ceil(40 * logicalDensity);

                    px = (px * telefoneAdapter.getCount()) + px ;

                    lvTelefoneAluno.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, px,0));

                    atualizou_telefone_aluno = true;

                    Toast.makeText(SobreMimActivity.this, "Contato salvo!", Toast.LENGTH_SHORT).show();

                    dialogInterface.dismiss();
                }
            }
        });

        mDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        dialogEdicaoTelefone = mDialogBuilder.create();

        dialogEdicaoTelefone.getWindow().setBackgroundDrawableResource(R.drawable.shape_dialog);

        dialogEdicaoTelefone.setCanceledOnTouchOutside(false);

        dialogEdicaoTelefone.show();

    }

    public void addContatoResp(){

        final ContatoResponsavel contato = new ContatoResponsavel();

        AlertDialog dialogEdicaoTelefone;
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(SobreMimActivity.this);
        View viewDialog = getLayoutInflater().inflate(R.layout.dialog_editar_telefone, null);
        final EditText editDDD = viewDialog.findViewById(R.id.sobre_edit_ddd);
        final EditText editTelefone = viewDialog.findViewById(R.id.sobre_edit_telefone);
        final Spinner spnTipoTelefone = viewDialog.findViewById(R.id.sobre_spn_tipo_telefone);
        final CheckBox chkSms = viewDialog.findViewById(R.id.sobre_check_sms_telefone);
        final EditText edtComplemento = viewDialog.findViewById(R.id.sobre_edit_complemento_telefone);

        spnTipoTelefone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String tipoTelefone = adapterView.getItemAtPosition(i).toString();

                switch (tipoTelefone){

                    case "Residencial" :

                        contato.setCodigo_tipo_telefone(1);
                        break;

                    case "Comercial" :

                        contato.setCodigo_tipo_telefone(2);
                        break;

                    case "Celular" :

                        contato.setCodigo_tipo_telefone(3);
                        break;

                    case "Recados" :

                        contato.setCodigo_tipo_telefone(4);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mDialogBuilder.setView(viewDialog);

        mDialogBuilder.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(editDDD.getText().toString().length() < 2){

                    Toast.makeText(SobreMimActivity.this, "DDD incorreto", Toast.LENGTH_SHORT).show();
                }
                else if(editTelefone.getText().toString().length() < 8){

                    Toast.makeText(SobreMimActivity.this, "Telefone incorreto", Toast.LENGTH_SHORT).show();
                }
                else{

                    contato.setCodigo_ddd(editDDD.getText().toString());
                    contato.setTelefone_responsavel(editTelefone.getText().toString());
                    if(chkSms.isChecked()){

                        contato.setValidacao_sms(1);
                    }
                    else{

                        contato.setValidacao_sms(0);
                    }

                    contato.setOperacao(OPERACAO_INSERIR);

                    contato.setComplemento_telefone(edtComplemento.getText().toString());

                    contato.setCd_responsavel(responsavel.getCd_responsavel());

                    contatosResponsavel.add(contato);

                    telefoneRespAdapter.notifyDataSetChanged();

                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);

                    float logicalDensity = metrics.density;

                    int px = (int) Math.ceil(40 * logicalDensity);

                    px = (px * telefoneRespAdapter.getCount()) + px ;

                    lvTelefoneResp.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, px,0));

                    atualizou_telefone_responsavel = true;

                    Toast.makeText(SobreMimActivity.this, "Contato salvo!", Toast.LENGTH_SHORT).show();

                    dialogInterface.dismiss();
                }
            }
        });

        mDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        dialogEdicaoTelefone = mDialogBuilder.create();

        dialogEdicaoTelefone.getWindow().setBackgroundDrawableResource(R.drawable.shape_dialog);

        dialogEdicaoTelefone.setCanceledOnTouchOutside(false);

        dialogEdicaoTelefone.show();

    }

    public void editarContatoAluno(final ContatoAluno contatoAluno){

        if(turmaRegular == null){

            return;
        }

        if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_ALUNO && aluno.getIdade() < 18){

            Toast.makeText(SobreMimActivity.this, "Somente alunos maiores de 18 anos e responsáveis podem realizar alterações.", Toast.LENGTH_LONG).show();

            return;
        }

        AlertDialog dialogEdicaoTelefone;

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(SobreMimActivity.this);

        View viewDialog = getLayoutInflater().inflate(R.layout.dialog_editar_telefone, null);

        final EditText editDDD = viewDialog.findViewById(R.id.sobre_edit_ddd);

        editDDD.setText(contatoAluno.getCodigo_ddd());

        final EditText editTelefone = viewDialog.findViewById(R.id.sobre_edit_telefone);

        editTelefone.setText(contatoAluno.getTelefone_aluno());

        final EditText edtComplemento = viewDialog.findViewById(R.id.sobre_edit_complemento_telefone);

        if(!contatoAluno.getComplemento_telefone().equals("null")){

            edtComplemento.setText(contatoAluno.getComplemento_telefone());
        }

        final Spinner spnTipoTelefone = viewDialog.findViewById(R.id.sobre_spn_tipo_telefone);


        switch (contatoAluno.getCodigo_tipo_telefone()){

            case 1 :

                spnTipoTelefone.setSelection(0);
                break;

            case 2 :

                spnTipoTelefone.setSelection(1);
                break;

            case 3 :

                spnTipoTelefone.setSelection(2);
                break;

            case 4 :

                spnTipoTelefone.setSelection(3);
                break;
        }

        final CheckBox chkSms = viewDialog.findViewById(R.id.sobre_check_sms_telefone);

        chkSms.setVisibility(View.GONE);

        spnTipoTelefone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String tipoTelefone = adapterView.getItemAtPosition(i).toString();

                switch (tipoTelefone){

                    case "Residencial" :

                        contatoAluno.setCodigo_tipo_telefone(1);
                        break;

                    case "Comercial" :

                        contatoAluno.setCodigo_tipo_telefone(2);
                        break;

                    case "Celular" :

                        contatoAluno.setCodigo_tipo_telefone(3);
                        break;

                    case "Recados" :

                        contatoAluno.setCodigo_tipo_telefone(4);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mDialogBuilder.setView(viewDialog);

        mDialogBuilder.setPositiveButton("Atualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(editDDD.getText().toString().length() < 2){

                    Toast.makeText(SobreMimActivity.this, "DDD incorreto", Toast.LENGTH_SHORT).show();
                }
                else if(editTelefone.getText().toString().length() < 8){

                    Toast.makeText(SobreMimActivity.this, "Telefone incorreto", Toast.LENGTH_SHORT).show();
                }
                else{

                    contatoAluno.setCodigo_ddd(editDDD.getText().toString());
                    contatoAluno.setTelefone_aluno(editTelefone.getText().toString());
                    if(chkSms.isChecked()){

                        contatoAluno.setValidacao_sms(1);
                    }
                    else{

                        contatoAluno.setValidacao_sms(0);
                    }

                    if(contatoAluno.getOperacao() != OPERACAO_INSERIR){

                        contatoAluno.setOperacao(OPERACAO_ALTERAR);
                    }

                    contatoAluno.setComplemento_telefone(edtComplemento.getText().toString());

                    for(int m = 0; m < listaContatos.size(); m++){

                        if(contatoAluno.getCd_contato_aluno() == listaContatos.get(m).getCd_contato_aluno() &&
                                contatoAluno.getTelefone_aluno() == listaContatos.get(m).getTelefone_aluno()){

                            listaContatos.remove(m);

                            break;
                        }

                    }

                    listaContatos.add(contatoAluno);

                    telefoneAdapter.notifyDataSetChanged();

                    atualizou_telefone_aluno = true;

                    Toast.makeText(SobreMimActivity.this, "Contato salvo!", Toast.LENGTH_SHORT).show();

                    dialogInterface.dismiss();
                }
            }
        });

        mDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        dialogEdicaoTelefone = mDialogBuilder.create();

        dialogEdicaoTelefone.getWindow().setBackgroundDrawableResource(R.drawable.shape_dialog);

        dialogEdicaoTelefone.setCanceledOnTouchOutside(false);

        dialogEdicaoTelefone.show();

    }

    public void editarContatoResp(final ContatoResponsavel contatoResponsavel){

        if(turmaRegular == null){

            return;
        }

        AlertDialog dialogEdicaoTelefone;

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(SobreMimActivity.this);

        View viewDialog = getLayoutInflater().inflate(R.layout.dialog_editar_telefone, null);

        final EditText editDDD = viewDialog.findViewById(R.id.sobre_edit_ddd);

        editDDD.setText(contatoResponsavel.getCodigo_ddd());

        final EditText editTelefone = viewDialog.findViewById(R.id.sobre_edit_telefone);

        editTelefone.setText(contatoResponsavel.getTelefone_responsavel());

        final EditText edtComplemento = viewDialog.findViewById(R.id.sobre_edit_complemento_telefone);

        if(!contatoResponsavel.getComplemento_telefone().equals("null")){

            edtComplemento.setText(contatoResponsavel.getComplemento_telefone());
        }

        final Spinner spnTipoTelefone = viewDialog.findViewById(R.id.sobre_spn_tipo_telefone);

        switch (contatoResponsavel.getCodigo_tipo_telefone()){

            case 1 :

                spnTipoTelefone.setSelection(0);
                break;

            case 2 :

                spnTipoTelefone.setSelection(1);
                break;

            case 3 :

                spnTipoTelefone.setSelection(2);
                break;

            case 4 :

                spnTipoTelefone.setSelection(3);
                break;
        }

        final CheckBox chkSms = viewDialog.findViewById(R.id.sobre_check_sms_telefone);

        if(contatoResponsavel.getValidacao_sms() == 1){

            chkSms.setChecked(true);
        }

        spnTipoTelefone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String tipoTelefone = adapterView.getItemAtPosition(i).toString();

                switch (tipoTelefone){

                    case "Residencial" :

                        contatoResponsavel.setCodigo_tipo_telefone(1);
                        break;

                    case "Comercial" :

                        contatoResponsavel.setCodigo_tipo_telefone(2);
                        break;

                    case "Celular" :

                        contatoResponsavel.setCodigo_tipo_telefone(3);
                        break;

                    case "Recados" :

                        contatoResponsavel.setCodigo_tipo_telefone(4);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mDialogBuilder.setView(viewDialog);

        mDialogBuilder.setPositiveButton("Atualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(editDDD.getText().toString().length() < 2){

                    Toast.makeText(SobreMimActivity.this, "DDD incorreto", Toast.LENGTH_SHORT).show();
                }
                else if(editTelefone.getText().toString().length() < 8){

                    Toast.makeText(SobreMimActivity.this, "Telefone incorreto", Toast.LENGTH_SHORT).show();
                }
                else{

                    contatoResponsavel.setCodigo_ddd(editDDD.getText().toString());
                    contatoResponsavel.setTelefone_responsavel(editTelefone.getText().toString());
                    if(chkSms.isChecked()){

                        contatoResponsavel.setValidacao_sms(1);
                    }
                    else{

                        contatoResponsavel.setValidacao_sms(0);
                    }

                    contatoResponsavel.setComplemento_telefone(edtComplemento.getText().toString());

                    if(contatoResponsavel.getOperacao() != OPERACAO_INSERIR){

                        contatoResponsavel.setOperacao(OPERACAO_ALTERAR);
                    }

                    for(int m = 0; m < contatosResponsavel.size(); m++){

                        if(contatoResponsavel.getCd_contato_responsavel() == contatosResponsavel.get(m).getCd_contato_responsavel() &&
                                contatoResponsavel.getTelefone_responsavel() == contatosResponsavel.get(m).getTelefone_responsavel()){

                            contatosResponsavel.remove(m);

                            break;
                        }

                    }

                    contatosResponsavel.add(contatoResponsavel);

                    telefoneRespAdapter.notifyDataSetChanged();

                    atualizou_telefone_responsavel = true;

                    Toast.makeText(SobreMimActivity.this, "Contato salvo!", Toast.LENGTH_SHORT).show();

                    dialogInterface.dismiss();
                }
            }
        });

        mDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        dialogEdicaoTelefone = mDialogBuilder.create();

        dialogEdicaoTelefone.getWindow().setBackgroundDrawableResource(R.drawable.shape_dialog);

        dialogEdicaoTelefone.setCanceledOnTouchOutside(false);

        dialogEdicaoTelefone.show();

    }

    public void excluirContatoAluno(final ContatoAluno contatoAluno){

        if(turmaRegular == null){

            return;
        }

        if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_ALUNO && aluno.getIdade() < 18){

            Toast.makeText(SobreMimActivity.this, "Somente alunos maiores de 18 anos e responsáveis podem realizar alterações.", Toast.LENGTH_LONG).show();

            return;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(SobreMimActivity.this).create();

        alertDialog.setMessage("Tem certeza que deseja apagar o número (" +
                contatoAluno.getCodigo_ddd() + ") " + contatoAluno.getTelefone_aluno() + " ?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "APAGAR",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        for(int i = 0; i < listaContatos.size(); i++){

                            if(contatoAluno.getTelefone_aluno() == listaContatos.get(i).getTelefone_aluno()){

                                contatosAlunoDeletar.add(listaContatos.get(i));
                                listaContatos.remove(i);
                                atualizou_telefone_aluno = true;
                                Toast.makeText(SobreMimActivity.this, "Número apagado!", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }

                        telefoneAdapter.notifyDataSetChanged();

                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);

                        float logicalDensity = metrics.density;

                        int px = (int) Math.ceil(40 * logicalDensity);

                        px = (px * telefoneAdapter.getCount()) + px ;

                        lvTelefoneAluno.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, px,0));

                        dialog.dismiss();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.shape_dialog);

        alertDialog.show();

    }

    public void excluirContatoResp(final ContatoResponsavel contatoResponsavel){

        if(turmaRegular == null){

            return;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(SobreMimActivity.this).create();

        alertDialog.setMessage("Tem certeza que deseja apagar o número (" +
                contatoResponsavel.getCodigo_ddd() + ") " + contatoResponsavel.getTelefone_responsavel() + " ?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "APAGAR",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        for(int i = 0; i < contatosResponsavel.size(); i++){

                            if(contatoResponsavel.getTelefone_responsavel() == contatosResponsavel.get(i).getTelefone_responsavel()){

                                contatosResponsavelDeletar.add(contatosResponsavel.get(i));
                                contatosResponsavel.remove(i);
                                atualizou_telefone_responsavel = true;
                                Toast.makeText(SobreMimActivity.this, "Número apagado!", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }

                        telefoneRespAdapter.notifyDataSetChanged();

                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);

                        float logicalDensity = metrics.density;

                        int px = (int) Math.ceil(40 * logicalDensity);

                        px = (px * telefoneRespAdapter.getCount()) + px ;

                        lvTelefoneResp.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, px,0));

                        dialog.dismiss();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.shape_dialog);

        alertDialog.show();
    }

    public void mostrarTermos(){

        AlertDialog dialogTermos;

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(SobreMimActivity.this);

        View viewDialog = getLayoutInflater().inflate(R.layout.dialog_termos, null);

        mDialogBuilder.setView(viewDialog);

        mDialogBuilder.setPositiveButton("Li e concordo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                rematricula.setAceito_termos_responsabilidade(true);

                realizarRematriculaAluno();
            }
        });

        mDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        dialogTermos = mDialogBuilder.create();

        dialogTermos.getWindow().setBackgroundDrawableResource(R.drawable.shape_dialog);

        dialogTermos.show();
    }

    private void realizarRematriculaAluno() {

        if(!atualizou_endereco &&
                !atualizou_comprovante &&
                !atualizou_email_aluno &&
                !atualizou_email_responsavel &&
                !criou_email_aluno &&
                !criou_email_responsavel &&
                !atualizou_telefone_aluno &&
                !atualizou_telefone_responsavel &&
                !atualizou_questionario){

            Toast.makeText(this, "Não há nenhuma alteração para salvar", Toast.LENGTH_LONG).show();

            return;
        }

        JSONObject jsonEnvio = new JSONObject();

        //region ---------- rematricula ----------
        JSONObject jsonQuestionario = new JSONObject();
        if(atualizou_questionario){

            try {

                if(rematricula == null){

                    jsonQuestionario.put("inCodigoInteresseRematricula", 0);
                }
                else{

                    jsonQuestionario.put("inCodigoInteresseRematricula", rematricula.getCd_interesse_rematricula());
                }

                jsonQuestionario.put("inAnoletivo", rematricula.getAno_letivo());
                jsonQuestionario.put("inAnoletivoRematricula", rematricula.getAno_letivo_rematricula());
                jsonQuestionario.put("inCodigoAluno", aluno.getCd_aluno());

                jsonQuestionario.put("inFl_InteresseContinuidade", rematricula.isInteresse_continuidade());

                jsonQuestionario.put("inFl_InteresseNovoTec", rematricula.isInteresse_novotec());
                jsonQuestionario.put("inCodigoEixoNovotecOpcaoUm", rematricula.getEixo_ensino_profissional_um());
                jsonQuestionario.put("inCodigoEixoNovotecOpcaoDois", rematricula.getEixo_ensino_profissional_dois());
                jsonQuestionario.put("inCodigoEixoNovotecOpcaoTres", rematricula.getEixo_ensino_profissional_tres());

                jsonQuestionario.put("inFl_InteresseTurnoIntegral", rematricula.isInteresse_turno_integral());

                jsonQuestionario.put("inFl_InteresseEspanhol", rematricula.isInteresse_espanhol());

                jsonQuestionario.put("inFl_InteresseTurnoNoturno", rematricula.isInteresse_noturno());
                jsonQuestionario.put("inCodigoObservacaoOpcaoNoturno", rematricula.getObservacao_opc_noturno());

                jsonQuestionario.put("inFl_AceiteTermoResponsabilidade", true);
                jsonQuestionario.put("inCodigoMatriculaOrigem", turmaRegular.getCd_matricula_aluno());

                if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL){

                    jsonQuestionario.put("inCodigoResponsavel", responsavel.getCd_responsavel());
                }
                else{

                    jsonQuestionario.put("inCodigoResponsavel", 0);
                }

                jsonQuestionario.put("inCodigoSistemaInteresseRematricula", 2);

                jsonEnvio.put("enInteresseRematricula", jsonQuestionario);

            }
            catch (JSONException e) {

                e.printStackTrace();
            }
        }
        else{

            try {

                jsonEnvio.put("enInteresseRematricula", jsonQuestionario);
            }
            catch (JSONException e) {

                e.printStackTrace();
            }
        }
        //endregion

        //region ---------- endereco ----------
        try{

            JSONObject jsonEndereco = new JSONObject();

            if(atualizou_endereco || atualizou_comprovante){

                jsonEndereco.put("inCodigoAluno", aluno.getCd_aluno());
                jsonEndereco.put("inLogradouro", endereco.getEndereco());
                jsonEndereco.put("inNumero", endereco.getNumero_endereco());
                jsonEndereco.put("inBairro", endereco.getBairro());
                jsonEndereco.put("inCidade", endereco.getCidade());
                jsonEndereco.put("inUF", "SP");
                jsonEndereco.put("inComplemento", endereco.getComplemento());
                jsonEndereco.put("inCep", endereco.getNumero_cep());
                jsonEndereco.put("inZonaLogradouro", String.valueOf(endereco.getTipo_logradouro()));
                jsonEndereco.put("inCodigoLocalizacaoDiferenciada", endereco.getLocalizacao_diferenciada());
                jsonEndereco.put("inCodigoMunicipioDNE", usuarioQueries.getCodMunicipio(endereco.getCidade()));
                jsonEndereco.put("inLatitude", endereco.getLatitude());
                jsonEndereco.put("inLongitude", endereco.getLongitude());
                jsonEndereco.put("inLatitudeIndi", endereco.getLatitude());
                jsonEndereco.put("inLongitudeIndi", endereco.getLongitude());

                if(isPDF){

                    jsonEndereco.put("inNomeExtensaoArquivo", ".pdf");
                    jsonEndereco.put("inBinarioArquivo", pdfBase64);
                }
                else {

                    ////Mudança////
                    if(bitmapComprovante != null) {

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                        bitmapComprovante.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                        byte[] byteArray = byteArrayOutputStream.toByteArray();

                        String encoded = Base64.encodeToString(byteArray, Base64.NO_WRAP);

                        jsonEndereco.put("inNomeExtensaoArquivo", ".png");

                        jsonEndereco.put("inBinarioArquivo", encoded);
                    }
                    else if(bmpComprovante != null){

                        jsonEndereco.put("inNomeExtensaoArquivo", ".png");

                        jsonEndereco.put("inBinarioArquivo", bmpComprovante);
                    }
                }
            }

            jsonEnvio.put("enEnderecoAluno", jsonEndereco);
        }
        catch (Exception e){

            e.printStackTrace();
        }
        //endregion

        //region ---------- email aluno ----------
        try{

            JSONObject jsonEmailAluno = new JSONObject();

            if(atualizou_email_aluno || criou_email_aluno){

                jsonEmailAluno.put("inCodigoAluno", aluno.getCd_aluno());
                jsonEmailAluno.put("inEmail", aluno.getEmail_aluno());
                jsonEnvio.put("enContatoAluno", jsonEmailAluno);
            }
            else{

                jsonEnvio.put("enContatoAluno", jsonEmailAluno);
            }

        }
        catch (Exception e){

            e.printStackTrace();
        }
        //endregion

        //region ---------- responsavel----------
        try{

            JSONObject responsavelEnvio = new JSONObject();

            if(atualizou_email_responsavel || criou_email_responsavel || atualizou_telefone_responsavel){

                responsavelEnvio.put("inCodigoResponsavel", responsavel.getCd_responsavel());

                JSONObject emailRespJson = new JSONObject();

                if(atualizou_email_responsavel || criou_email_responsavel){

                    if(criou_email_responsavel){

                        emailRespJson.put("inCodigoOperacao", OPERACAO_INSERIR);
                    }
                    else if(atualizou_email_responsavel){

                        emailRespJson.put("inCodigoOperacao", OPERACAO_ALTERAR);
                    }

                    emailRespJson.put("inCodigoResponsavel", responsavel.getCd_responsavel());
                    emailRespJson.put("inEmailResponsavel", responsavel.getEmail());
                }

                responsavelEnvio.put("enContatoResponsavelAluno", emailRespJson);

                JSONArray arrayTelefonesResponsavel = new JSONArray();

                if(atualizou_telefone_responsavel){

                    try{

                        if(!contatosResponsavelDeletar.isEmpty()){

                            JSONObject contatoJson;


                            for(ContatoResponsavel contato : contatosResponsavelDeletar){

                                contatoJson = new JSONObject();

                                contatoJson.put("inCodigoOperacao", OPERACAO_DELETAR);
                                contatoJson.put("inCodigoTelefoneResponsavel", contato.getCodigo_fone_telefone());
                                contatoJson.put("inCodigoResponsavel", responsavel.getCd_responsavel());
                                contatoJson.put("inEnviaSMS", (contato.getValidacao_sms() == 1));
                                contatoJson.put("inTipoTelefone", contato.getCodigo_tipo_telefone());
                                contatoJson.put("inDDD", contato.getCodigo_ddd());
                                contatoJson.put("inNumero", contato.getTelefone_responsavel());
                                contatoJson.put("inComplemento", contato.getComplemento_telefone());
                                contatoJson.put("inCodigoVerificador", 0);

                                arrayTelefonesResponsavel.put(contatoJson);
                            }
                        }

                        JSONObject contatoJson;


                        for(ContatoResponsavel contato : contatosResponsavel) {

                            contatoJson = new JSONObject();

                            if(contato.getOperacao() == OPERACAO_ALTERAR || contato.getOperacao() == OPERACAO_INSERIR){

                                contatoJson.put("inCodigoOperacao", contato.getOperacao());
                                contatoJson.put("inCodigoTelefoneResponsavel", contato.getCodigo_fone_telefone());
                                contatoJson.put("inCodigoResponsavel", responsavel.getCd_responsavel());
                                contatoJson.put("inEnviaSMS", (contato.getValidacao_sms() == 1));
                                contatoJson.put("inTipoTelefone", contato.getCodigo_tipo_telefone());
                                contatoJson.put("inDDD", contato.getCodigo_ddd());
                                contatoJson.put("inNumero", contato.getTelefone_responsavel());
                                contatoJson.put("inComplemento", contato.getComplemento_telefone());
                                contatoJson.put("inCodigoVerificador", 0);

                                arrayTelefonesResponsavel.put(contatoJson);

                            }
                        }
                    }
                    catch (Exception e){

                        e.printStackTrace();
                    }

                }

                JSONObject jsonVazio = new JSONObject();

                arrayTelefonesResponsavel.put(jsonVazio);

                responsavelEnvio.put("lstTelefoneResponsavelAluno", arrayTelefonesResponsavel);

                jsonEnvio.put("enResponsavelAluno", responsavelEnvio);
            }
            else{

                JSONObject emailRespJson = new JSONObject();

                responsavelEnvio.put("enContatoResponsavelAluno", emailRespJson);

                JSONArray arrayTelefonesResponsavel = new JSONArray();

                JSONObject jsonVazio = new JSONObject();

                arrayTelefonesResponsavel.put(jsonVazio);

                responsavelEnvio.put("lstTelefoneResponsavelAluno", arrayTelefonesResponsavel);

                jsonEnvio.put("enResponsavelAluno", responsavelEnvio);
            }
        }
        catch (Exception e){

            e.printStackTrace();
        }
        //endregion

        //region ---------- telefone aluno ----------
        try{

            JSONArray jsonArrayTelefones = new JSONArray();

            if(atualizou_telefone_aluno){

                if(!contatosAlunoDeletar.isEmpty()){

                    JSONObject contatoJson;

                    for(ContatoAluno contato : contatosAlunoDeletar){

                        contatoJson = new JSONObject();

                        contatoJson.put("inCodigoOperacao", OPERACAO_DELETAR);
                        contatoJson.put("inCodigoTelefoneAluno", contato.getCodigo_fone_telefone());
                        contatoJson.put("inCodigoAluno", contato.getCd_aluno());
                        contatoJson.put("inTipoTelefone", contato.getCodigo_tipo_telefone());
                        contatoJson.put("inDDD", contato.getCodigo_ddd());
                        contatoJson.put("inNumero", contato.getTelefone_aluno());
                        contatoJson.put("inComplemento", contato.getComplemento_telefone());
                        contatoJson.put("inCodigoVerificador", 0);

                        jsonArrayTelefones.put(contatoJson);
                    }
                }

                for(ContatoAluno contatoAluno : listaContatos){

                    if(contatoAluno.getOperacao() != 0){

                        JSONObject contatoJson = new JSONObject();

                        contatoJson = new JSONObject();

                        contatoJson.put("inCodigoOperacao", contatoAluno.getOperacao());
                        contatoJson.put("inCodigoTelefoneAluno", contatoAluno.getCodigo_fone_telefone());
                        contatoJson.put("inCodigoAluno", contatoAluno.getCd_aluno());
                        contatoJson.put("inTipoTelefone", contatoAluno.getCodigo_tipo_telefone());
                        contatoJson.put("inDDD", contatoAluno.getCodigo_ddd());
                        contatoJson.put("inNumero", contatoAluno.getTelefone_aluno());
                        contatoJson.put("inComplemento", contatoAluno.getComplemento_telefone());
                        contatoJson.put("inCodigoVerificador", 0);

                        jsonArrayTelefones.put(contatoJson);

                    }
                }
            }

            jsonEnvio.put("lstTelefoneAluno", jsonArrayTelefones);

        }
        catch (Exception e){

            e.printStackTrace();
        }
        //endregion

        EnviarRematriculaTask enviarRematriculaTask = new EnviarRematriculaTask(this);

        enviarRematriculaTask.execute(jsonEnvio);

        foto = null;

        bmpComprovante = null;

        bitmapComprovante = null;
    }

    public void salvarAlteracoesNoBanco(int cd_interesse_rematricula){

        if(atualizou_email_aluno || criou_email_aluno){

            usuarioQueries.atualizarEmailAluno(aluno.getCd_aluno(), aluno.getEmail_aluno());
        }
        if(atualizou_telefone_aluno){

            usuarioQueries.deletarTelefonesAluno(aluno.getCd_aluno());

            usuarioQueries.inserirContatosAlunos(listaContatos);
        }
        if(atualizou_telefone_responsavel){

            usuarioQueries.deletarTelefonesResponsavel(responsavel.getCd_responsavel());

            usuarioQueries.inserirContatosResponsavel(contatosResponsavel);
        }
        if(atualizou_email_responsavel || criou_email_responsavel){

            usuarioQueries.atualizarEmailResponsavel(responsavel.getCd_responsavel(), responsavel.getEmail());
        }
        if(atualizou_questionario && cd_interesse_rematricula != 0){

            usuarioQueries.deletarRematricula(rematricula.getCd_interesse_rematricula());

            rematricula.setCd_interesse_rematricula(cd_interesse_rematricula);

            usuarioQueries.inserirRematricula(rematricula);
        }
        if(atualizou_endereco || atualizou_comprovante){

            usuarioQueries.deletarEnderecoAluno(aluno.getCd_aluno());

            if(atualizou_comprovante){

                endereco.setEnvio_comprovante(true);
            }

            usuarioQueries.inserirEnderecoAluno(endereco);
        }

        mostrarDialogConfirmacao();
    }

    private void mostrarDialogConfirmacao() {

        AlertDialog alertDialog = new AlertDialog.Builder(SobreMimActivity.this).create();


        if(atualizou_questionario){

            alertDialog.setMessage("Rematrícula realizada com sucesso!");
        }
        else{

            alertDialog.setMessage("Dados atualizados com sucesso!");
        }

        alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.shape_dialog);

        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && data.getExtras() != null) {

            switch (requestCode) {

                case UCrop.REQUEST_CROP: {

                    UCrop.getOutput(data);

                    Uri resultUri = UCrop.getOutput(data);

                    String resultPath = ImageCrop.getRealPathFromURI(getApplicationContext(), resultUri);

                    if (resultPath == null && resultUri != null) {

                        resultPath = resultUri.getPath();
                    }
                    else {

                        Toast.makeText(this, "É necessário checar as permissões de acesso", Toast.LENGTH_SHORT).show();

                        return;
                    }

                    /////Mudança/////
                    if(foto != null) {

                        try {

                            FileInputStream fileInputStream = new FileInputStream(foto);

                            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

                            byte[] byteArray = new byte[bufferedInputStream.available()];

                            bufferedInputStream.read(byteArray, 0, byteArray.length);

                            String base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);

                            bmpComprovante = base64;

                        }
                        catch(Exception e) {

                            e.printStackTrace();
                        }
                    }
                    else {

                        try {

                            File file = new File(resultPath);

                            FileInputStream fileInputStream = new FileInputStream(file);

                            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

                            byte[] byteArray = new byte[bufferedInputStream.available()];

                            bufferedInputStream.read(byteArray, 0, byteArray.length);

                            String base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);

                            bmpComprovante = base64;
                        }
                        catch(Exception e) {

                            e.printStackTrace();
                        }
                    }

                    /////Mudança//////
                    if (bmpComprovante != null  ) {

                        atualizou_comprovante = true;

                        isPDF = false;

                        txtStatusComprovante.setText("Selecionado");

                        layoutComprovante.setBackgroundResource(R.drawable.shape_quadrado_borda);
                    }
                    else {

                        Toast.makeText(this, "Falha ao selecionar foto!", Toast.LENGTH_SHORT).show();
                    }

                    dialogAjudaComprovante.dismiss();

                    break;
                }

                case ImageCrop.PICK_FROM_ALBUM: {

                    ImageCrop.pickFromAlbum(this, data);

                    dialogAjudaComprovante.dismiss();

                    break;
                }

                case 5 : {

                    if (resultCode == RESULT_OK) {

                        try {

                            Bundle extras = data.getExtras();

                            bitmapComprovante = (Bitmap) extras.get("data");

                            Uri uri = Uri.parse("file:///storage/emulated/0/Pictures/" + foto.getName());

                            ImageCrop.pickFromAlbum2(this, uri);

                            atualizou_comprovante = true;

                            layoutComprovante.setBackgroundResource(R.drawable.shape_quadrado_borda);

                            txtStatusComprovante.setText("Selecionado");
                        }
                        catch (Exception e) {

                            e.printStackTrace();
                        }
                    }

                    dialogAjudaComprovante.dismiss();

                    break;
                }

                case COD_RESULT_GEO: {

                    latitude = String.valueOf(data.getDoubleExtra("latitude", 0));

                    longitude = String.valueOf(data.getDoubleExtra("longitude", 0));

                    if(latitude.equals("0") && longitude.equals("0")){

                        Toast.makeText(this, "Falha ao localizar endereço!", Toast.LENGTH_SHORT).show();

                        return;
                    }

                    if (!endereco.getLatitude().equals(latitude) || !endereco.getLongitude().equals(longitude)) {

                        endereco.setLongitude(longitude);

                        endereco.setLatitude(latitude);

                        atualizou_endereco = true;

                        layoutEndereco.setBackgroundResource(R.drawable.shape_quadrado_borda);

                        montarLayoutEnvioComprovante();
                    }
                    break;
                }
            }
        }
        else if(data != null && requestCode == ImageCrop.PICK_FROM_ALBUM) {

            ImageCrop.pickFromAlbum(this, data);

            dialogAjudaComprovante.dismiss();
        }
        else {

            if(foto != null && requestCode != ImageCrop.PICK_FROM_ALBUM && requestCode != UCrop.REQUEST_CROP) {

                Uri uri = Uri.parse("file:///storage/emulated/0/Pictures/" + foto.getName());

                ImageCrop.pickFromCamera2(this, uri);
            }
        }
    }

    private File criarArquivoFoto() throws IOException {

        File arquivoFoto = null;

        File diretorioParaSalvar = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));

        arquivoFoto = File.createTempFile("pic3_",".jpg", diretorioParaSalvar);

        nomeArquivoFoto = arquivoFoto.getName();

        caminhoArquivoFoto = arquivoFoto.getAbsolutePath();

        return arquivoFoto;
    }

    public void exibirMensagemErro(String mensagem){

        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
    }
}
