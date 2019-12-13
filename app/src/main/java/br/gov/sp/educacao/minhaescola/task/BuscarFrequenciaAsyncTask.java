package br.gov.sp.educacao.minhaescola.task;

import android.app.ProgressDialog;

import android.content.Intent;

import android.os.AsyncTask;

import android.widget.Toast;

import org.json.JSONArray;

import java.lang.ref.WeakReference;

import java.util.ArrayList;

import br.gov.sp.educacao.minhaescola.banco.FrequenciasQueries;
import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;

import br.gov.sp.educacao.minhaescola.model.Frequencia;

import br.gov.sp.educacao.minhaescola.modelTO.FrequenciasTO;

import br.gov.sp.educacao.minhaescola.requests.BuscarNotasFrequenciaRequest;

import br.gov.sp.educacao.minhaescola.view.FrequenciaActivity;
import br.gov.sp.educacao.minhaescola.view.MenuActivity;

public class BuscarFrequenciaAsyncTask
        extends AsyncTask<Integer, Void, JSONArray> {

    private WeakReference<MenuActivity> menuActivityWeakReference;

    private UsuarioQueries usuarioQueries;

    private FrequenciasQueries frequenciasQueries;

    private int cd_aluno;

    private int cd_turma;

    public BuscarFrequenciaAsyncTask(MenuActivity menuActivity) {

        this.menuActivityWeakReference = new WeakReference(menuActivity);

        usuarioQueries = new UsuarioQueries(menuActivityWeakReference.get());

        frequenciasQueries = new FrequenciasQueries(menuActivityWeakReference.get());
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        MenuActivity menuActivity = menuActivityWeakReference.get();

        menuActivity.mostrarSombraCarregamento();
    }

    @Override
    protected JSONArray doInBackground(Integer... integers) {

        MenuActivity menuActivity = menuActivityWeakReference.get();

        cd_aluno = integers[0];

        cd_turma= integers[1];

        BuscarNotasFrequenciaRequest buscarNotasFreqRequest = new BuscarNotasFrequenciaRequest();

        JSONArray responseRequest = buscarNotasFreqRequest.executeRequest(cd_aluno, cd_turma, menuActivity);

        return responseRequest;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {

        super.onPostExecute(jsonArray);

        MenuActivity menuActivity = menuActivityWeakReference.get();

        if(jsonArray == null){

            menuActivity.esconderSombraCarregamento();

            Toast.makeText(menuActivity, "Ops! Verifique sua conexão e tente novamente", Toast.LENGTH_SHORT).show(); //TODO Validar texto

            return;
        }
        if(jsonArray.toString().contains("Dados não encontrados")){

            menuActivity.esconderSombraCarregamento();

            Toast.makeText(menuActivity, "As informações sobre a frequência estarão disponíveis em breve", Toast.LENGTH_LONG).show();

            return;
        }

        FrequenciasTO frequenciasTO = new FrequenciasTO(jsonArray, cd_aluno);

        ArrayList<Frequencia> frequencias = frequenciasTO.getFrequenciaFromJson();

        frequenciasQueries.inserirFreq(frequencias);

        menuActivity.esconderSombraCarregamento();

        Intent frequenciaIntent = new Intent(menuActivity, FrequenciaActivity.class);

        frequenciaIntent.putExtra("cd_aluno", cd_aluno);

        menuActivity.startActivity(frequenciaIntent);

    }
}
