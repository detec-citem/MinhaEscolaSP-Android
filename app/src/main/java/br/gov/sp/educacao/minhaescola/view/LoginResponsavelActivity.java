package br.gov.sp.educacao.minhaescola.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import br.gov.sp.educacao.minhaescola.R;
import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;
import br.gov.sp.educacao.minhaescola.task.LoginResponsavelAsyncTask;

import br.gov.sp.educacao.minhaescola.util.MyPreferences;
import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;

import static br.gov.sp.educacao.minhaescola.util.Utils.isOnline;
import static br.gov.sp.educacao.minhaescola.util.Utils.lerArquivoEstatico;

public class LoginResponsavelActivity
        extends AppCompatActivity {

    public @BindView(R.id.spnEstadosRG) Spinner spnEstado;
    public @BindView(R.id.loginAluno_editRA) EditText editRg;
    public @BindView(R.id.loginResp_editDigRG) EditText editDigRg;
    public @BindView(R.id.loginResp_editSenha) EditText editSenha;

    private String rg;
    private String digRg;
    private String siglaRg;
    private String senha;

    private MyPreferences mPref;

    private UsuarioQueries usuarioQueries;

    private LoginResponsavelAsyncTask loginAsyncTask;

    public ProgressDialog loginRespProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Window window = this.getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul_splash));
        }

        setContentView(R.layout.activity_login_responsavel);

        ButterKnife.bind(this);

        usuarioQueries = new UsuarioQueries(this);

        contentOnCreate();
    }

    private void contentOnCreate() {

        editRg.requestFocus();

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this,
                R.layout.layout_spinner_item,
                getResources().getStringArray(R.array.estados));
        spnEstado.setAdapter(adapterSpinner);
    }

    @OnTextChanged(R.id.loginAluno_editRA)
    public void onTextChanged() {

        /*
        if(editRg.length() == 8) {

            editDigRg.requestFocus();
        }
        */
    }

    @OnItemSelected(R.id.spnEstadosRG)
    public void onItemSelected(Spinner spinner, int position) {

        siglaRg = (String) spinner.getAdapter().getItem(position);
    }

    public void exibirAjudaLoginResp(View v) {

        final AlertDialog ajudaLogin;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (v.getId()) {

            case R.id.loginResp_ajudaRG:

                builder.setMessage("Caso ainda não esteja cadastrado(a), " +
                        "solicite o cadastro na secretaria da escola do(a) aluno(a)");

                builder.setPositiveButton("Entendi", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface d, int arg1) {

                        d.dismiss();
                    }
                });

                ajudaLogin = builder.create();
                ajudaLogin.getWindow().setBackgroundDrawableResource(R.drawable.shape_dialog);
                ajudaLogin.show();

                break;

            case R.id.loginResp_ajudaSenha:

                builder.setMessage("A primeira senha é o CPF, somente " +
                        "os números, por exemplo: 46734986143");

                builder.setPositiveButton("Entendi", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface d, int arg1) {

                        d.dismiss();
                    }
                });

                ajudaLogin = builder.create();
                ajudaLogin.getWindow().setBackgroundDrawableResource(R.drawable.shape_dialog);
                ajudaLogin.show();

                break;
        }
    }

    public void voltarSelecaoPerfil(View v) {

        finish();

        startActivity(new Intent(this, SelecaoPerfilActivity.class));
    }

    public void esqueceuSenha(View v) {

        startActivity(new Intent(this, EsqueceuSenhaActivity.class));
    }

    public void loginEstrangeiro(View v) {

        finish();

        startActivity(new Intent(this, LoginEstrangeiroActivity.class));
    }

    public void loginResponsavel(View v) {

        if (validarPerfil()) {

            if(isOnline(this) == true) {

                rg = editRg.getText().toString();
                digRg = editDigRg.getText().toString();

                String login = "RG" + rg + digRg + siglaRg;
                senha = editSenha.getText().toString();

                if(login.equals("RG111119999SP") && senha.equals("testelogin")){

                    mPref = new MyPreferences(this);

                    try {

                        mPref.mockResponsavel();

                        loginRespProgress = new ProgressDialog(this);
                        loginRespProgress.setTitle("Carregando...");
                        loginRespProgress.setCancelable(false);
                        loginRespProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        loginRespProgress.show();

                        usuarioQueries.inserirResponsavel(new JSONObject(lerArquivoEstatico(R.raw.mock_login_responsavel, this).toString()),
                                login, senha, 756807);

                        usuarioQueries.inserirAlunosResponsavel(new JSONObject(lerArquivoEstatico(R.raw.mock_alunos_responsavel, this).toString()));

                        loginRespProgress.dismiss();

                        startActivity(new Intent(this, MenuActivity.class));

                        finish();

                    }
                    catch (Exception e){

                        e.printStackTrace();
                    }
                }
                else{

                    loginAsyncTask = new LoginResponsavelAsyncTask(this);

                    loginAsyncTask.execute(login, senha);
                }
            }
            else {

                Toast.makeText(getApplicationContext(), "Verifique sua conexão com a Internet", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validarPerfil() {

        if(editRg.length() == 0 ) {

            editRg.setError("Campo vazio!");

            return false;
        }

        if(editSenha.length() == 0 ) {

            editSenha.setError("Campo vazio!");

            return false;
        }
        return true;
    }
}
