package br.gov.sp.educacao.minhaescola.task;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import br.gov.sp.educacao.minhaescola.requests.EnviarMatriculaRequest;
import br.gov.sp.educacao.minhaescola.view.SobreMimActivity;

public class EnviarRematriculaTask extends AsyncTask<JSONObject, Void, JSONObject> {

    private WeakReference<SobreMimActivity> sobreMimRef;

    public EnviarRematriculaTask(SobreMimActivity activity) {

        this.sobreMimRef = new WeakReference<>(activity);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        SobreMimActivity sobreMimActivity = sobreMimRef.get();

        sobreMimActivity.dialogRequisicao = new ProgressDialog(sobreMimActivity);
        sobreMimActivity.dialogRequisicao.setTitle("Enviando...");
        sobreMimActivity.dialogRequisicao.setCancelable(false);
        sobreMimActivity.dialogRequisicao.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        sobreMimActivity.dialogRequisicao.show();

    }

    @Override
    protected JSONObject doInBackground(JSONObject... jsonObjects) {

        SobreMimActivity sobreMimActivity = sobreMimRef.get();

        EnviarMatriculaRequest enviarMatriculaRequest = new EnviarMatriculaRequest();

        return enviarMatriculaRequest.executeRequest(jsonObjects[0], sobreMimActivity);
    }

    @Override
    protected void onPostExecute(JSONObject jsonResponse) {
        super.onPostExecute(jsonResponse);

        SobreMimActivity sobreMimActivity = sobreMimRef.get();

        if(jsonResponse.toString().contains("Erro")){

            try {

                String erro = jsonResponse.getString("Erro");

                sobreMimActivity.exibirMensagemErro(erro);
            }
            catch (JSONException e) {

                sobreMimActivity.exibirMensagemErro("Falha ao realizar rematrícula. Realize o login e tente novamente!");
                e.printStackTrace();
            }
        }
        else{

            try {

                int novo_codigo_rematricula = jsonResponse.getInt("Codigo");
                sobreMimActivity.salvarAlteracoesNoBanco(novo_codigo_rematricula);
            }
            catch (JSONException e) {

                sobreMimActivity.exibirMensagemErro("Falha ao realizar rematrícula. Realize o login e tente novamente!");
                e.printStackTrace();
            }
        }

        sobreMimActivity.dialogRequisicao.dismiss();
        sobreMimRef.clear();
    }
}
