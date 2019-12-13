package br.gov.sp.educacao.minhaescola.task;

import android.app.ProgressDialog;

import android.content.Intent;

import android.os.AsyncTask;

import android.widget.Toast;

import org.json.JSONArray;

import java.lang.ref.WeakReference;

import java.util.ArrayList;

import br.gov.sp.educacao.minhaescola.banco.NotasQueries;
import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;

import br.gov.sp.educacao.minhaescola.model.Nota;

import br.gov.sp.educacao.minhaescola.modelTO.NotasTO;

import br.gov.sp.educacao.minhaescola.requests.BuscarNotasFrequenciaRequest;

import br.gov.sp.educacao.minhaescola.view.MenuActivity;
import br.gov.sp.educacao.minhaescola.view.NotasActivity;

public class BuscarNotasAsyncTask
        extends AsyncTask<Integer, Void, JSONArray> {

    private WeakReference<MenuActivity> menuActivityWeakReference;

    private UsuarioQueries usuarioQueries;

    private NotasQueries notasQueries;

    private int cd_aluno;

    private int cd_turma;

    private int cd_escola;


    public BuscarNotasAsyncTask(MenuActivity menuActivity) {

        this.menuActivityWeakReference = new WeakReference(menuActivity);

        usuarioQueries = new UsuarioQueries(menuActivityWeakReference.get());

        notasQueries = new NotasQueries(menuActivityWeakReference.get());
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

        cd_turma = integers[1];

        BuscarNotasFrequenciaRequest buscarNotasFreqRequest = new BuscarNotasFrequenciaRequest();

        JSONArray responseRequest = buscarNotasFreqRequest.executeRequest(cd_aluno, cd_turma, menuActivity);

        return responseRequest;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {

        super.onPostExecute(jsonArray);

        MenuActivity menuActivity = menuActivityWeakReference.get();

        if(jsonArray == null) {

            menuActivity.esconderSombraCarregamento();

            Toast.makeText(menuActivity, "Ops! Verifique sua conexão e tente novamente", Toast.LENGTH_SHORT).show();

            return;
        }

        if(jsonArray.toString().contains("Dados não encontrados")){

            menuActivity.esconderSombraCarregamento();

            Toast.makeText(menuActivity, "As informações sobre as notas estarão disponíveis em breve", Toast.LENGTH_LONG).show();

            return;
        }

        NotasTO notasTo = new NotasTO(jsonArray, cd_aluno);

        ArrayList<Nota> notas = notasTo.getNotasFromJson();

        notasQueries.inserirNotas(notas);

        menuActivity.esconderSombraCarregamento();

        Intent notasIntent = new Intent(menuActivity, NotasActivity.class);

        notasIntent.putExtra("cd_aluno", cd_aluno);

        menuActivity.startActivity(notasIntent);
    }
}
