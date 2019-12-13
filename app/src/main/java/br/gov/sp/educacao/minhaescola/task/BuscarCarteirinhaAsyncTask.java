package br.gov.sp.educacao.minhaescola.task;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import br.gov.sp.educacao.minhaescola.banco.CarteirinhaQueries;
import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;
import br.gov.sp.educacao.minhaescola.requests.BuscarCarteirinhaRequest;
import br.gov.sp.educacao.minhaescola.util.MyPreferences;
import br.gov.sp.educacao.minhaescola.view.CarteirinhaActivity;
import br.gov.sp.educacao.minhaescola.view.EnviarFotoActivity;
import br.gov.sp.educacao.minhaescola.view.MenuActivity;

public class BuscarCarteirinhaAsyncTask
        extends AsyncTask<Integer, Void, JSONObject> {

    private WeakReference<MenuActivity> menuWeakRef;

    private int cd_aluno;

    private UsuarioQueries usuarioQueries;
    private CarteirinhaQueries carteirinhaQueries;

    private BuscarCarteirinhaRequest buscarCartRequest;

    private MyPreferences mPref;

    public BuscarCarteirinhaAsyncTask(MenuActivity menuAct){

        this.menuWeakRef = new WeakReference<>(menuAct);

        usuarioQueries = new UsuarioQueries(menuAct);

        mPref = new MyPreferences(menuAct);

        carteirinhaQueries = new CarteirinhaQueries(menuAct);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        MenuActivity menuAct = menuWeakRef.get();

        menuAct.mostrarSombraCarregamento();
    }

    @Override
    protected JSONObject doInBackground(Integer... integers) {

        MenuActivity menuAct = menuWeakRef.get();

        cd_aluno = integers[0];

        String token = usuarioQueries.getToken();

        buscarCartRequest = new BuscarCarteirinhaRequest();

        JSONObject response = buscarCartRequest.executeRequest(token, cd_aluno, menuAct);

        if(response == null){

            try {

                response = new JSONObject().put("Falha", "Falha");
            }
            catch (JSONException e) {

                e.printStackTrace();
            }
        }

        return response;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);

        MenuActivity menuAct = menuWeakRef.get();

        menuAct.esconderSombraCarregamento();

        if(jsonObject.has("Erro")){

            if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL){

                Toast.makeText(menuAct, "É necessário ter uma foto aprovada pela direção da escola para acessar", Toast.LENGTH_LONG).show();
            }
            else{

                Intent intent = new Intent(menuAct, EnviarFotoActivity.class);

                menuAct.startActivity(intent);
            }
        }
        else if(jsonObject.has("Falha")){

            Toast.makeText(menuAct, "Ops! Falha ao buscar carteirinha, tente novamente", Toast.LENGTH_LONG).show();
        }
        else{

            carteirinhaQueries.inserirCarteirinha(jsonObject, cd_aluno);

            Intent intent = new Intent(menuAct, CarteirinhaActivity.class);

            if(mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL){

                intent.putExtra("cd_aluno", cd_aluno);
            }

            menuWeakRef.clear();

            menuAct.startActivity(intent);
        }
    }
}
