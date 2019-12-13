package br.gov.sp.educacao.minhaescola.view;

import android.app.ProgressDialog;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import br.gov.sp.educacao.minhaescola.R;

import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;
import br.gov.sp.educacao.minhaescola.task.LoginAlunoAsyncTask;

import br.gov.sp.educacao.minhaescola.util.MyPreferences;
import br.gov.sp.educacao.minhaescola.util.Utils;
import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;

import static br.gov.sp.educacao.minhaescola.util.Utils.isOnline;
import static br.gov.sp.educacao.minhaescola.util.Utils.lerArquivoEstatico;

public class LoginAlunoActivity
        extends AppCompatActivity {

    private String ra;
    private String digRa;
    private String siglaRa;
    private String senha;

    private UsuarioQueries usuarioQueries;

    public ProgressDialog loginAlunoProgress;

    public @BindView(R.id.loginAluno_editRA) EditText editRa;
    public @BindView(R.id.loginAluno_editDigRA) EditText editDigRa;
    public @BindView(R.id.loginResp_editSenha) EditText editSenha;
    public @BindView(R.id.spnEstadosRA) Spinner spnEstado;

    private LoginAlunoAsyncTask loginTask;

    private MyPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Window window = this.getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.setStatusBarColor(ContextCompat.getColor(this, R.color.azul_splash));
        }

        setContentView(R.layout.activity_login_aluno);

        ButterKnife.bind(this);

        usuarioQueries = new UsuarioQueries(this);

        contentOnCreate();
    }

    private void contentOnCreate() {

        editRa.requestFocus();

        final ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this,
                R.layout.layout_spinner_item,
                getResources().getStringArray(R.array.estados));

        spnEstado.setAdapter(adapterSpinner);
    }

    @OnTextChanged(R.id.loginAluno_editRA)
    public void onTextChanged() {

        if(editRa.length() == 9) {

            editDigRa.requestFocus();
        }
    }

    @OnItemSelected(R.id.spnEstadosRA)
    public void onItemSelected(Spinner spinner, int position) {

        siglaRa = (String) spinner.getAdapter().getItem(position);
    }

    public void exibirAjudaLoginAluno(View v) {

        final AlertDialog ajudaLogin;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (v.getId()) {

            case R.id.loginAluno_ajudaRA:

                builder.setMessage("O RA (Registro do Aluno) é o número de " +
                        "identificação do aluno no Sistema de Cadastro " +
                        "de Alunos da Secretaria da Educação do Estado " +
                        "de São Paulo. Para saber seu RA, consulte a secretaria de sua escola.");

                builder.setPositiveButton("Entendi", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface d, int arg1) {

                        d.dismiss();
                    }
                });

                ajudaLogin = builder.create();

                ajudaLogin.getWindow().setBackgroundDrawableResource(R.drawable.shape_dialog);

                ajudaLogin.show();

                break;

            case R.id.loginAluno_ajudaSenha:

                builder.setMessage("A primeira senha é a data de nascimento sem as " +
                        "barras, por exemplo: 18062002 (18/06/2002)");

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

    public void loginAluno(View v) {

        if(validarPerfil()) {

            if(isOnline(this)) {

                ra = editRa.getText().toString();

                digRa = editDigRa.getText().toString();

                senha = editSenha.getText().toString();

                String login = ra + digRa + siglaRa;

                if(login.equals("1111199999SP") && senha.equals("testelogin")){

                    mPref = new MyPreferences(this);

                    try {

                        mPref.mockAluno();

                        loginAlunoProgress = new ProgressDialog(this);
                        loginAlunoProgress.setTitle("Carregando...");
                        loginAlunoProgress.setCancelable(false);
                        loginAlunoProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        loginAlunoProgress.show();

                        usuarioQueries.inserirAluno(new JSONObject(lerArquivoEstatico(R.raw.mock_login_aluno, this).toString()), login, senha);

                        loginAlunoProgress.dismiss();

                        startActivity(new Intent(this, MenuActivity.class));

                        finish();

                    }
                    catch (Exception e){

                        e.printStackTrace();
                    }
                }
                else{

                    loginTask = new LoginAlunoAsyncTask(LoginAlunoActivity.this);

                    loginTask.execute(login, senha);
                }
            }
            else {

                Toast.makeText(getApplicationContext(), "Verifique sua conexão com a Internet", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validarPerfil() {

        if(editRa.length() == 0 ) {

            editRa.setError("Campo vazio!");

            return false;
        }

        if(editDigRa.length() == 0 ) {

            editDigRa.setError("Campo vazio!");

            return false;
        }

        if(editSenha.length() == 0 ) {

            editSenha.setError("Campo vazio!");

            return false;
        }
        return true;
    }
}
