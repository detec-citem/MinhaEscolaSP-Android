package br.gov.sp.educacao.minhaescola.task;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;
import br.gov.sp.educacao.minhaescola.requests.EnviarFotoRequest;
import br.gov.sp.educacao.minhaescola.view.EnviarFotoActivity;
import br.gov.sp.educacao.minhaescola.view.MenuActivity;

public class EnviarFotoAsyncTask extends AsyncTask<Void, Void, Integer> {

    private WeakReference<EnviarFotoActivity> enviarFotoActivityWeakReference;

    private byte[] fotoByteArray;

    private UsuarioQueries usuarioQueries;

    public EnviarFotoAsyncTask(byte[] fotoByteArray, EnviarFotoActivity enviarFotoActivity) {

        this.enviarFotoActivityWeakReference = new WeakReference<>(enviarFotoActivity);

        this.fotoByteArray = fotoByteArray;

        usuarioQueries = new UsuarioQueries(enviarFotoActivity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        EnviarFotoActivity enviarFotoActivity = enviarFotoActivityWeakReference.get();

        enviarFotoActivity.progressDialog = new ProgressDialog(enviarFotoActivity);
        enviarFotoActivity.progressDialog.setMessage("Enviando foto");
        enviarFotoActivity.progressDialog.setTitle("Aguarde...");
        enviarFotoActivity.progressDialog.setCancelable(false);
        enviarFotoActivity.progressDialog.show();

    }

    @Override
    protected Integer doInBackground(Void... voids) {

        EnviarFotoActivity enviarFotoActivity = enviarFotoActivityWeakReference.get();

        String token = usuarioQueries.getToken();

        EnviarFotoRequest enviarFotoRequest = new EnviarFotoRequest();

        int response = enviarFotoRequest.executeRequest(fotoByteArray, token, enviarFotoActivity);


        return response;
    }

    @Override
    protected void onPostExecute(Integer response) {
        super.onPostExecute(response);

        EnviarFotoActivity enviarFotoActivity = enviarFotoActivityWeakReference.get();

        enviarFotoActivity.progressDialog.dismiss();

        switch (response) {

            case 0:
                Toast.makeText(enviarFotoActivity, "Foto enviada para aprovação!", Toast.LENGTH_LONG).show();
                break;

            case -1:
                Toast.makeText(enviarFotoActivity, "Ocorreu um erro ao enviar a foto. Tente novamente em alguns minutos", Toast.LENGTH_LONG).show();
                break;

            case -2:
                Toast.makeText(enviarFotoActivity, "Não encontrou a foto, ou a foto passa de 1MB", Toast.LENGTH_LONG).show();
                break;

            case -3:
                Toast.makeText(enviarFotoActivity, "O detector facial não detectou uma face na foto", Toast.LENGTH_LONG).show();
                break;

            case -4:
                Toast.makeText(enviarFotoActivity, "Erro desconhecido durante a detecção facial", Toast.LENGTH_LONG).show();
                break;

            case -5:
                Toast.makeText(enviarFotoActivity, "Sessão expirada. Faça login novamente", Toast.LENGTH_LONG).show();
                break;

            case -6:
                Toast.makeText(enviarFotoActivity, "O detector facial não detectou uma face na foto", Toast.LENGTH_LONG).show();
                break;

            case -7:
                Toast.makeText(enviarFotoActivity, "Ocorreu um erro ao enviar a foto. Tente novamente em alguns minutos!", Toast.LENGTH_LONG).show();
                break;

            case -8:
                Toast.makeText(enviarFotoActivity, "Ocorreu um erro ao enviar a foto. Tente novamente em alguns minutos!", Toast.LENGTH_LONG).show();
                break;
        }

        enviarFotoActivity.startActivity(new Intent(enviarFotoActivity, MenuActivity.class));

        enviarFotoActivityWeakReference.clear();

        enviarFotoActivity.finish();

    }
}
