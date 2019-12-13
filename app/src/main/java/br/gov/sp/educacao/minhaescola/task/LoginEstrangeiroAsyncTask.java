package br.gov.sp.educacao.minhaescola.task;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;
import br.gov.sp.educacao.minhaescola.model.ContatoResponsavel;
import br.gov.sp.educacao.minhaescola.modelTO.ContatoResponsavelTO;
import br.gov.sp.educacao.minhaescola.requests.BuscarAlunosRequest;
import br.gov.sp.educacao.minhaescola.requests.LoginRequest;
import br.gov.sp.educacao.minhaescola.util.MyPreferences;
import br.gov.sp.educacao.minhaescola.view.AndroidDatabaseManagerME;
import br.gov.sp.educacao.minhaescola.view.LoginEstrangeiroActivity;
import br.gov.sp.educacao.minhaescola.view.MenuActivity;

public class LoginEstrangeiroAsyncTask extends AsyncTask<String, Void, Integer> {

    private WeakReference<LoginEstrangeiroActivity> loginActivityWeakRef;

    private UsuarioQueries usuarioQueries;

    private BuscarAlunosRequest buscarAlunosRequest;

    public LoginEstrangeiroAsyncTask(LoginEstrangeiroActivity logEstrAct) {

        this.loginActivityWeakRef = new WeakReference<>(logEstrAct);
        usuarioQueries = new UsuarioQueries(loginActivityWeakRef.get());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        LoginEstrangeiroActivity loginEstrAct = loginActivityWeakRef.get();

        loginEstrAct.loginEstrProgress = new ProgressDialog(loginEstrAct);
        loginEstrAct.loginEstrProgress.setMax(100);
        loginEstrAct.loginEstrProgress.setTitle("Carregando...");
        loginEstrAct.loginEstrProgress.setCancelable(false);
        loginEstrAct.loginEstrProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        loginEstrAct.loginEstrProgress.show();

        update_progress(0, 42);
    }

    @Override
    protected Integer doInBackground(String... strings) {

        LoginEstrangeiroActivity loginEstrAct = loginActivityWeakRef.get();

        String login = strings[0];
        String senha = strings[1];

        LoginRequest loginRequest = new LoginRequest();
        JSONObject respJson = loginRequest.executeLoginRequest(login, senha, loginEstrAct);

        List<Integer> perfis = new ArrayList<>();

        if(respJson == null){
            perfis.clear();
            return 0;
        }

        try {

            JSONArray jsonPerfis = respJson.getJSONArray("Perfis");

            for (int i = 0; i < jsonPerfis.length(); i++){

                perfis.add(jsonPerfis.getJSONObject(i).getInt("Codigo"));
            }
        }
        catch (JSONException e) {
            perfis.clear();
            e.printStackTrace();
            return -2;
        }

        boolean temPerfil = false;

        for(int i = 0; i < perfis.size(); i++){

            if(perfis.get(i) == MyPreferences.PERFIL_RESPONSAVEL){

                temPerfil = true;
                break;
            }
        }

        if(!temPerfil){
            perfis.clear();
            return -1;
        }

        try {

            if(!loginRequest.executeSelecionarPerfilRequest(respJson.getString("Token"), loginEstrAct)){

                return -3;
            }
        }
        catch (JSONException e) {
            perfis.clear();
            e.printStackTrace();
            return -3;
        }


        loginEstrAct.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                update_progress(42, 98);
            }
        });

        buscarAlunosRequest = new BuscarAlunosRequest();

        try {

            JSONObject responseBuscarAlunos = buscarAlunosRequest.executeBuscarAlunosRequest(respJson.getString("Token"));

            int cd_responsavel = responseBuscarAlunos.getInt("CodigoResponsavel");

            String email = responseBuscarAlunos.getString("Email");

            usuarioQueries.inserirResponsavel(respJson, login, senha, cd_responsavel);

            usuarioQueries.inserirEmailResponsavel(cd_responsavel, email);

            usuarioQueries.inserirAlunosResponsavel(responseBuscarAlunos);

            JSONArray jsonContatosResp = responseBuscarAlunos.getJSONArray("TelefoneResponsavel");

            ContatoResponsavelTO contatoResponsavelTO = new ContatoResponsavelTO(jsonContatosResp, cd_responsavel);

            ArrayList<ContatoResponsavel> contatos = contatoResponsavelTO.getContatoResponsavelFromJson();

            usuarioQueries.inserirContatosResponsavel(contatos);
        }
        catch (JSONException e) {

            e.printStackTrace();
            return -2;
        }

        perfis.clear();
        return 1;
    }

    @Override
    protected void onPostExecute(Integer response) {
        super.onPostExecute(response);

        LoginEstrangeiroActivity loginEstrAct = loginActivityWeakRef.get();

        switch (response){

            case 1:

                loginEstrAct.loginEstrProgress.dismiss();
                loginActivityWeakRef.clear();
                loginEstrAct.startActivity(new Intent(loginEstrAct, MenuActivity.class));

                break;

            case 0:

                loginEstrAct.loginEstrProgress.dismiss();
                loginActivityWeakRef.clear();
                Toast.makeText(loginEstrAct, "Usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
                break;

            case -1:

                loginEstrAct.loginEstrProgress.dismiss();
                loginActivityWeakRef.clear();
                Toast.makeText(loginEstrAct, "Ops! É necessário o perfil Responsável para acessar", Toast.LENGTH_SHORT).show();
                break;

            case -2:

                loginEstrAct.loginEstrProgress.dismiss();
                loginActivityWeakRef.clear();
                Toast.makeText(loginEstrAct, "Ops! Verifique sua conexão e tente novamente", Toast.LENGTH_SHORT).show();
                break;

            case -3:

                loginEstrAct.loginEstrProgress.dismiss();
                loginActivityWeakRef.clear();
                Toast.makeText(loginEstrAct, "Erro ao selecionar perfil", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    public void update_progress (int from, int to) {

        final LoginEstrangeiroActivity loginEstrAct = loginActivityWeakRef.get();

        final Handler handler1 = new Handler();

        class Task implements Runnable {

            int start,end;

            Task(int a,int b) { start = a; end = b;}

            @Override
            public void run() {

                for (int i =start ; i <= end; i++) {

                    final int value = i;
                    try {

                        Thread.sleep(25);
                    }
                    catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                    handler1.post(new Runnable() {

                        @Override
                        public void run() {

                            loginEstrAct.loginEstrProgress.setProgress(value);
                        }
                    });
                }
            }
        }

        Thread t = new Thread(new Task(from, to));
        t.start();
    }
}
