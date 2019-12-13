package br.gov.sp.educacao.minhaescola.modelTO;

import org.json.JSONArray;
import org.json.JSONException;


import java.util.ArrayList;


import br.gov.sp.educacao.minhaescola.model.Turma;

public class TurmaTO {

    private JSONArray arrayTurmas;

    public TurmaTO(JSONArray arrayTurmas) {

        this.arrayTurmas = arrayTurmas;
    }

    public ArrayList<Turma> getTurmasFromJson(int cd_aluno) {

        ArrayList<Turma> listaTurmas = new ArrayList<>();

        for(int i = 0; i < arrayTurmas.length(); i++) {

            Turma turma = new Turma();

            turma.setCd_aluno(cd_aluno);

            try {

                turma.setCd_turma(arrayTurmas.getJSONObject(i).getInt("CodigoTurma"));
                turma.setNome_turma(arrayTurmas.getJSONObject(i).getString("NomeTurma"));
                turma.setAno_letivo(arrayTurmas.getJSONObject(i).getInt("AnoLetivo"));
                turma.setNome_escola(arrayTurmas.getJSONObject(i).getString("NomeEscola"));
                turma.setTipo_ensino(arrayTurmas.getJSONObject(i).getString("TipoEnsino"));
                turma.setSituacao_aprovacao(arrayTurmas.getJSONObject(i).getString("SituacaoAprovacao"));
                turma.setSituacao_matricula(arrayTurmas.getJSONObject(i).getString("SituacaoMatricula"));

                String dataInicio = arrayTurmas.getJSONObject(i).getString("DtInicioMatricula").split("T")[0];
                turma.setDt_inicio_matricula(dataInicio);

                String dataFim = arrayTurmas.getJSONObject(i).getString("DtFimMatricula").split("T")[0];
                turma.setDt_fim_matricula(dataFim);

                turma.setCd_tipo_ensino(arrayTurmas.getJSONObject(i).getInt("CodigoTipoEnsino"));
                turma.setCd_matricula_aluno(arrayTurmas.getJSONObject(i).getLong("CodigoMatriculaAluno"));

                listaTurmas.add(turma);

            }
            catch (JSONException e) {

                e.printStackTrace();
            }
        }
        return listaTurmas;
    }

    public ArrayList<Turma> getTurmasFromJsonResponsavel(int cd_aluno) {

        ArrayList<Turma> listaTurmas = new ArrayList<>();

        for(int i = 0; i < arrayTurmas.length(); i++) {

            Turma turma = new Turma();

            turma.setCd_aluno(cd_aluno);

            try {

                turma.setCd_turma(arrayTurmas.getJSONObject(i).getInt("CodigoTurma"));
                turma.setNome_turma(arrayTurmas.getJSONObject(i).getString("NomeTurma"));
                turma.setAno_letivo(arrayTurmas.getJSONObject(i).getInt("AnoLetivo"));
                turma.setNome_escola(arrayTurmas.getJSONObject(i).getString("NomeEscola"));
                turma.setTipo_ensino(arrayTurmas.getJSONObject(i).getString("TipoEnsino"));
                turma.setSituacao_aprovacao(arrayTurmas.getJSONObject(i).getString("SituacaoAprovacao"));
                turma.setSituacao_matricula(arrayTurmas.getJSONObject(i).getString("SituacaoMatricula"));
                turma.setDt_inicio_matricula(arrayTurmas.getJSONObject(i).getString("DtInicioMatricula"));
                turma.setDt_fim_matricula(arrayTurmas.getJSONObject(i).getString("DtFimMatricula"));
                turma.setCd_tipo_ensino(arrayTurmas.getJSONObject(i).getInt("CodigoTipoEnsino"));
                turma.setCd_matricula_aluno(arrayTurmas.getJSONObject(i).getLong("CodigoMatriculaAluno"));

                listaTurmas.add(turma);

            }
            catch (JSONException e) {

                e.printStackTrace();
            }
        }
        return listaTurmas;
    }
}
