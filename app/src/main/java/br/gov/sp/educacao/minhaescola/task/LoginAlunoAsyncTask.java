package br.gov.sp.educacao.minhaescola.task;

import android.app.ProgressDialog;

import android.content.Intent;

import android.os.AsyncTask;
import android.os.Handler;

import android.widget.Toast;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;

import br.gov.sp.educacao.minhaescola.requests.LoginRequest;

import br.gov.sp.educacao.minhaescola.view.LoginAlunoActivity;
import br.gov.sp.educacao.minhaescola.view.MenuActivity;

public class LoginAlunoAsyncTask extends AsyncTask <String, Void, Integer> {

    private WeakReference<LoginAlunoActivity> loginActivityWeakRef;

    private UsuarioQueries usuarioQueries;

    public LoginAlunoAsyncTask(LoginAlunoActivity loginActivity) {

        this.loginActivityWeakRef = new WeakReference<>(loginActivity);

        usuarioQueries = new UsuarioQueries(loginActivityWeakRef.get());
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        LoginAlunoActivity loginAlunoActivity = loginActivityWeakRef.get();

        loginAlunoActivity.loginAlunoProgress = new ProgressDialog(loginAlunoActivity);
        loginAlunoActivity.loginAlunoProgress.setMax(100);
        loginAlunoActivity.loginAlunoProgress.setTitle("Carregando...");
        loginAlunoActivity.loginAlunoProgress.setCancelable(false);
        loginAlunoActivity.loginAlunoProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        loginAlunoActivity.loginAlunoProgress.show();

        update_progress(0, 98);
    }

    @Override
    protected Integer doInBackground(String... strings) {

        LoginAlunoActivity loginAlunoActivity = loginActivityWeakRef.get();

        String login = strings[0];
        String senha = strings[1];

        LoginRequest loginRequest = new LoginRequest();

        JSONObject alunoJson = loginRequest.executeLoginRequest(login, senha, loginAlunoActivity);

        if(alunoJson != null){

            return usuarioQueries.inserirAluno(alunoJson, login, senha);
        }
        else{

            return 0;
        }
    }

    @Override
    protected void onPostExecute(Integer response) {

        super.onPostExecute(response);

        LoginAlunoActivity loginAlunoActivity = loginActivityWeakRef.get();

        switch (response) {

            case 1:

                loginAlunoActivity.loginAlunoProgress.dismiss();

                loginAlunoActivity.startActivity(new Intent(loginAlunoActivity, MenuActivity.class));

                loginActivityWeakRef.clear();

                loginAlunoActivity.finish();

                break;

            case 0:

                loginAlunoActivity.loginAlunoProgress.dismiss();

                loginActivityWeakRef.clear();

                Toast.makeText(loginAlunoActivity, "Usuário ou senha incorretos", Toast.LENGTH_SHORT).show();

                break;

            case -1:

                loginAlunoActivity.loginAlunoProgress.dismiss();

                loginActivityWeakRef.clear();

                Toast.makeText(loginAlunoActivity, "Ops! É necessário o perfil Aluno para acessar", Toast.LENGTH_SHORT).show(); //TODO validar mensagem

                break;

            case -2:

                loginAlunoActivity.loginAlunoProgress.dismiss();

                loginActivityWeakRef.clear();

                Toast.makeText(loginAlunoActivity, "Ops! Verifique sua conexão e tente novamente", Toast.LENGTH_SHORT).show(); //TODO validar mensagem

                break;
        }
    }


    public void update_progress (int from, int to) {

        final LoginAlunoActivity loginAlunoActivity = loginActivityWeakRef.get();

        final Handler handler1 = new Handler();

        class Task implements Runnable {

            int start,end;

            Task(int a,int b) { start = a; end = b;}

            @Override
            public void run() {

                for (int i =start ; i <= end; i++) {

                    final int value = i;
                    try {

                        Thread.sleep(50);
                    }
                    catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                    handler1.post(new Runnable() {

                        @Override
                        public void run() {

                            loginAlunoActivity.loginAlunoProgress.setProgress(value);
                        }
                    });
                }
            }
        }

        Thread t = new Thread(new Task(from, to));
        t.start();
    }
}
