package br.gov.sp.educacao.minhaescola.modelTO;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.gov.sp.educacao.minhaescola.model.HorarioAula;

public class HorarioAulaTO {

    private JSONObject jsonHorarios;
    private int cd_aluno;

    public HorarioAulaTO(JSONObject jsonHorarios, int cd_aluno) {

        this.jsonHorarios = jsonHorarios;
        this.cd_aluno = cd_aluno;
    }

    public ArrayList<HorarioAula> getHorariosFromJson(){

        ArrayList<HorarioAula> horariosAula = new ArrayList<>();

        try {

            JSONArray jsonAulas = jsonHorarios.getJSONArray("Aulas");

            HorarioAula horario;

            for(int i = 0; i < jsonAulas.length(); i++){

                horario = new HorarioAula();

                horario.setCd_aluno(cd_aluno);
                horario.setCd_materia(jsonAulas.getJSONObject(i).getInt("CodigoMateria"));
                horario.setNome_materia(jsonAulas.getJSONObject(i).getString("NomeMateriaCompleto"));
                horario.setNome_professor(jsonAulas.getJSONObject(i).getString("NomeProfessor"));
                horario.setData_hora_inicio(jsonAulas.getJSONObject(i).getString("DataHoraInicio"));
                horario.setData_hora_fim(jsonAulas.getJSONObject(i).getString("DataHoraFim"));
                horario.setNome_turma(jsonAulas.getJSONObject(i).getString("NomeTurma"));
                horario.setDia_semana(jsonAulas.getJSONObject(i).getString("DiaSemana"));

                horariosAula.add(horario);
            }

            return horariosAula;
        }
        catch (JSONException e) {

            e.printStackTrace();
            return null;
        }
    }
}
