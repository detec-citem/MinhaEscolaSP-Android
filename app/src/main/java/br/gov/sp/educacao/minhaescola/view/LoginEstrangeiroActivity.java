package br.gov.sp.educacao.minhaescola.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import br.gov.sp.educacao.minhaescola.R;
import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;
import br.gov.sp.educacao.minhaescola.task.LoginEstrangeiroAsyncTask;

import br.gov.sp.educacao.minhaescola.util.MyPreferences;
import butterknife.ButterKnife;
import butterknife.BindView;

import static br.gov.sp.educacao.minhaescola.util.Utils.isOnline;
import static br.gov.sp.educacao.minhaescola.util.Utils.lerArquivoEstatico;

public class LoginEstrangeiroActivity
        extends AppCompatActivity {

    public @BindView(R.id.loginEstr_editRNE) EditText editRNE;
    public @BindView(R.id.loginEstr_editSenha) EditText editSenha;

    private String rne;
    private String senha;

    private MyPreferences mPref;

    public ProgressDialog loginEstrProgress;

    private UsuarioQueries usuarioQueries;

    private LoginEstrangeiroAsyncTask loginEstrAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Window window = this.getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul_splash));
        }

        setContentView(R.layout.activity_login_estrangeiro);

        ButterKnife.bind(this);

        usuarioQueries = new UsuarioQueries(this);

        editRNE.requestFocus();
    }

    public void exibirAjudaLoginEstr(View v) {

        final AlertDialog ajudaLogin;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (v.getId()) {

            case R.id.loginEstr_ajudaRNE:

                builder.setMessage("Caso ainda não esteja cadastrado(a), solicite " +
                        "o cadastro na secretaria da escola do(a) aluno(a)");

                builder.setPositiveButton("Entendi", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface d, int arg1) {

                        d.dismiss();
                    }
                });

                ajudaLogin = builder.create();
                ajudaLogin.getWindow().setBackgroundDrawableResource(R.drawable.shape_dialog);
                ajudaLogin.show();

                break;

            case R.id.loginEstr_ajudaSenha:

                builder.setMessage("A primeira senha é a data de nascimento " +
                        "sem as barras, por exemplo: 18061985 (18/06/1985)");

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

        if(validarPerfil()) {

            if(isOnline(this)) {

                rne = editRNE.getText().toString();
                senha = editSenha.getText().toString();

                if(rne.equals("RG111119999SP") && senha.equals("testelogin")){

                    mPref = new MyPreferences(this);

                    try {

                        mPref.mockResponsavel();

                        loginEstrProgress = new ProgressDialog(this);
                        loginEstrProgress.setTitle("Carregando...");
                        loginEstrProgress.setCancelable(false);
                        loginEstrProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        loginEstrProgress.show();

                        usuarioQueries.inserirResponsavel(new JSONObject(lerArquivoEstatico(R.raw.mock_login_responsavel, this).toString()),
                                rne, senha, 756807);

                        usuarioQueries.inserirAlunosResponsavel(new JSONObject(lerArquivoEstatico(R.raw.mock_alunos_responsavel, this).toString()));

                        loginEstrProgress.dismiss();

                        startActivity(new Intent(this, MenuActivity.class));

                        finish();

                    }
                    catch (Exception e){

                        e.printStackTrace();
                    }
                }
                else{

                    loginEstrAsyncTask = new LoginEstrangeiroAsyncTask(this);

                    loginEstrAsyncTask.execute(rne, senha);
                }

            }
            else {

                Toast.makeText(getApplicationContext(), "Verifique sua conexão com a Internet", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validarPerfil() {

        if(editRNE.length() == 0 ) {

            editRNE.setError("Campo vazio!");

            return false;
        }

        if(editSenha.length() == 0 ) {

            editSenha.setError("Campo vazio!");

            return false;
        }
        return true;
    }
}
