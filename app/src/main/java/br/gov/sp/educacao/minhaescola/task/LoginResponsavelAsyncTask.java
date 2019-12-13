package br.gov.sp.educacao.minhaescola.task;

import android.app.ProgressDialog;

import android.content.Intent;

import android.os.AsyncTask;
import android.os.Handler;

import android.util.Log;
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
import br.gov.sp.educacao.minhaescola.view.LoginResponsavelActivity;
import br.gov.sp.educacao.minhaescola.view.MenuActivity;

public class LoginResponsavelAsyncTask
        extends AsyncTask <String, Void, Integer> {

    private WeakReference<LoginResponsavelActivity> loginActivityWeakRef;

    private UsuarioQueries usuarioQueries;

    private BuscarAlunosRequest buscarAlunosRequest;

    public LoginResponsavelAsyncTask(LoginResponsavelActivity logRespAct) {

        this.loginActivityWeakRef = new WeakReference<>(logRespAct);

        usuarioQueries = new UsuarioQueries(loginActivityWeakRef.get());
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        LoginResponsavelActivity loginRespAct = loginActivityWeakRef.get();

        loginRespAct.loginRespProgress = new ProgressDialog(loginRespAct);
        loginRespAct.loginRespProgress.setMax(100);
        loginRespAct.loginRespProgress.setTitle("Carregando...");
        loginRespAct.loginRespProgress.setCancelable(false);
        loginRespAct.loginRespProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        loginRespAct.loginRespProgress.show();

        update_progress(0, 42);
    }

    @Override
    protected Integer doInBackground(String... strings) {

        LoginResponsavelActivity loginRespAct = loginActivityWeakRef.get();

        String login = strings[0];
        String senha = strings[1];

        LoginRequest loginRequest = new LoginRequest();

        JSONObject respJson = loginRequest.executeLoginRequest(login, senha, loginRespAct);

        List<Integer> perfis = new ArrayList<>();

        if(respJson == null) {

            perfis.clear();

            return 0;
        }

        try {

            JSONArray jsonPerfis = respJson.getJSONArray("Perfis");

            for (int i = 0; i < jsonPerfis.length(); i++) {

                perfis.add(jsonPerfis.getJSONObject(i).getInt("Codigo"));
            }
        }
        catch (JSONException e) {

            perfis.clear();

            e.printStackTrace();

            return -2;
        }

        boolean temPerfil = false;

        for(int i = 0; i < perfis.size(); i++) {

            if(perfis.get(i) == MyPreferences.PERFIL_RESPONSAVEL) {

                temPerfil = true;

                break;
            }
        }

        if(!temPerfil) {

            perfis.clear();

            return -1;
        }

        try {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(!loginRequest.executeSelecionarPerfilRequest(respJson.getString("Token"), loginRespAct)) {

                return -3;
            }
        }
        catch (JSONException e) {

            perfis.clear();

            e.printStackTrace();

            return -3;
        }


        loginRespAct.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                update_progress(42, 98);
            }
        });

        buscarAlunosRequest = new BuscarAlunosRequest();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {

            JSONObject responseBuscarAlunos = buscarAlunosRequest.executeBuscarAlunosRequest(respJson.getString("Token"));

            if(responseBuscarAlunos.toString().contains("CodigoResponsavel")){

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
            else{

                return -4;
            }
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

        LoginResponsavelActivity loginRespAct = loginActivityWeakRef.get();

        switch (response){

            case 1:

                loginRespAct.loginRespProgress.dismiss();

                loginActivityWeakRef.clear();

                loginRespAct.startActivity(new Intent(loginRespAct, MenuActivity.class));

                loginRespAct.finish();

                break;

            case 0:

                loginRespAct.loginRespProgress.dismiss();

                loginActivityWeakRef.clear();

                Toast.makeText(loginRespAct, "Usuário ou senha incorretos", Toast.LENGTH_SHORT).show();

                break;

            case -1:

                loginRespAct.loginRespProgress.dismiss();

                loginActivityWeakRef.clear();

                Toast.makeText(loginRespAct, "Ops! É necessário o perfil Responsável para acessar", Toast.LENGTH_SHORT).show();//TODO validar mensagem

                break;

            case -2:

                loginRespAct.loginRespProgress.dismiss();

                loginActivityWeakRef.clear();

                Toast.makeText(loginRespAct, "Ops! Verifique sua conexão e tente novamente", Toast.LENGTH_SHORT).show(); //TODO validar mensagem

                break;

            case -3:

                loginRespAct.loginRespProgress.dismiss();

                loginActivityWeakRef.clear();

                Toast.makeText(loginRespAct, "Erro ao selecionar perfil", Toast.LENGTH_SHORT).show(); //TODO validar mensagem

                break;

            case -4:

                loginRespAct.loginRespProgress.dismiss();

                loginActivityWeakRef.clear();

                Toast.makeText(loginRespAct, "Erro ao buscar alunos", Toast.LENGTH_SHORT).show(); //TODO validar mensagem

                break;
        }
    }

    public void update_progress (int from, int to) {

        final LoginResponsavelActivity loginRespAct = loginActivityWeakRef.get();

        final Handler handler1 = new Handler();

        class Task implements Runnable {

            int start, end;
            int value = 0;

            Task(int a,int b) { start = a; end = b;}

            @Override
            public void run() {

                for (int i = start ; i <= end; i++) {

                    value = i;



                    try {

                        Thread.sleep(25);
                    }
                    catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                    handler1.post(new Runnable() {

                        @Override
                        public void run() {

                            loginRespAct.loginRespProgress.setProgress(value);
                        }
                    });
                }
            }
        }

        Thread t = new Thread(new Task(from, to));

        t.start();
    }
}
