package br.gov.sp.educacao.minhaescola.task;

import android.app.ProgressDialog;

import android.content.Intent;

import android.os.AsyncTask;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import java.util.ArrayList;

import br.gov.sp.educacao.minhaescola.banco.HorarioAulaQueries;
import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;

import br.gov.sp.educacao.minhaescola.model.HorarioAula;

import br.gov.sp.educacao.minhaescola.modelTO.HorarioAulaTO;

import br.gov.sp.educacao.minhaescola.requests.BuscarHorariosRequest;

import br.gov.sp.educacao.minhaescola.view.HorarioAulasActivity;
import br.gov.sp.educacao.minhaescola.view.MenuActivity;

public class HorarioAulasAsyncTask
        extends AsyncTask<Integer, Void, JSONObject> {

    private WeakReference<MenuActivity> menuWeakRef;

    private UsuarioQueries usuarioQueries;

    private HorarioAulaQueries horarioAulaQueries;

    private int cd_aluno;

    public HorarioAulasAsyncTask(MenuActivity menuAct) {

        this.menuWeakRef = new WeakReference<>(menuAct);

        usuarioQueries = new UsuarioQueries(menuWeakRef.get());

        horarioAulaQueries = new HorarioAulaQueries(menuWeakRef.get());
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

        int cd_turma = integers[0];

        cd_aluno = integers[1];

        BuscarHorariosRequest buscarHorariosRequest = new BuscarHorariosRequest();

        JSONObject responseRequest = buscarHorariosRequest.executeRequest(usuarioQueries.getToken(), cd_turma, menuAct);

        return responseRequest;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {

        super.onPostExecute(jsonObject);

        MenuActivity menuAct = menuWeakRef.get();

        if(jsonObject == null) {

            menuAct.progressDialog.dismiss();

            Toast.makeText(menuAct, "Ops! Verifique sua conexão e tente novamente", Toast.LENGTH_SHORT).show(); //TODO Validar texto

            menuAct.esconderSombraCarregamento();

            return;
        }

        try {

            JSONArray jsonAulas = jsonObject.getJSONArray("Aulas");

            if(jsonAulas.length() == 0){

                menuAct.esconderSombraCarregamento();

                Toast.makeText(menuAct, "As informações sobre o horário das aulas não estão disponíveis. Verifique com sua escola.", Toast.LENGTH_LONG).show();

                return;
            }
        }
        catch (JSONException e) {

            e.printStackTrace();
        }

        HorarioAulaTO horarioAulaTO = new HorarioAulaTO(jsonObject, cd_aluno);

        ArrayList<HorarioAula> horariosAulas = horarioAulaTO.getHorariosFromJson();

        horarioAulaQueries.inserirHorariosAulas(horariosAulas);

        menuAct.esconderSombraCarregamento();

        Intent horarioIntent = new Intent(menuAct, HorarioAulasActivity.class);

        horarioIntent.putExtra("cd_aluno", cd_aluno);

        menuAct.startActivity(horarioIntent);

        menuWeakRef.clear();
    }
}
