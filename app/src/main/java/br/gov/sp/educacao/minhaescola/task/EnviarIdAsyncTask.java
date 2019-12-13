package br.gov.sp.educacao.minhaescola.task;

import android.os.AsyncTask;

import android.util.Log;

import java.lang.ref.WeakReference;

import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;
import br.gov.sp.educacao.minhaescola.model.Dispositivo;
import br.gov.sp.educacao.minhaescola.requests.EnviarIdPushRequest;
import br.gov.sp.educacao.minhaescola.view.MenuActivity;

public class EnviarIdAsyncTask
        extends AsyncTask<Dispositivo, Void, String> {

    private WeakReference<MenuActivity> menuRef;

    public EnviarIdAsyncTask(MenuActivity menuAct) {

        this.menuRef = new WeakReference<>(menuAct);
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Dispositivo... params) {

        String respostaId = "";

        try {

            MenuActivity menuActivity = menuRef.get();

            UsuarioQueries usuarioQueries = new UsuarioQueries(menuActivity);

            String token = usuarioQueries.getToken();

            EnviarIdPushRequest enviarIdPushRequest = new EnviarIdPushRequest();

            enviarIdPushRequest.executeRequest(token, params[0]);

        }
        catch (Exception e) {

            e.printStackTrace();

            respostaId = "Erro";
        }

        return respostaId;
    }

    @Override
    protected void onPostExecute(String result) {

        if (!result.equals("Erro")) {

            Log.e("R", "RespostaId: Salvou o ID " + result);
        }
        else {

            Log.e("R", "RespostaId: Não salvou o ID " + result);
        }

        menuRef.clear();
    }
}
