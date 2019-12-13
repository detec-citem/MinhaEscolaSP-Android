package br.gov.sp.educacao.minhaescola.modelTO;

import org.json.JSONObject;

import br.gov.sp.educacao.minhaescola.model.Rematricula;

public class RematriculaTO {

    private JSONObject rematriculaJson;

    private int cd_aluno;

    public RematriculaTO(int cd_aluno, JSONObject rematriculaJson) {

        this.rematriculaJson = rematriculaJson;
        this.cd_aluno = cd_aluno;
    }

    public Rematricula getRematriculaFromJson(){

        Rematricula rematricula = new Rematricula();

        try{

            rematricula.setCd_interesse_rematricula(rematriculaJson.getInt("CodigoInteresseRematricula"));
            rematricula.setCd_aluno(cd_aluno);
            rematricula.setAno_letivo(rematriculaJson.getInt("AnoLetivo"));
            rematricula.setAno_letivo_rematricula(rematriculaJson.getInt("AnoLetivoRematricula"));
            rematricula.setInteresse_continuidade(rematriculaJson.getBoolean("InteresseContinuidade"));
            rematricula.setInteresse_novotec(rematriculaJson.getBoolean("InteresseNovotec"));
            rematricula.setInteresse_turno_integral(rematriculaJson.getBoolean("InteresseTurnoIntegral"));
            rematricula.setInteresse_espanhol(rematriculaJson.getBoolean("InteresseEspanhol"));
            rematricula.setInteresse_noturno(rematriculaJson.getBoolean("InteresseTurnoNoturno"));
            rematricula.setAceito_termos_responsabilidade(rematriculaJson.getBoolean("AceitoTermoResponsabilidade"));
            rematricula.setEixo_ensino_profissional_um(rematriculaJson.getInt("EixoEnsinoProfissionalUm"));
            rematricula.setEixo_ensino_profissional_dois(rematriculaJson.getInt("EixoEnsinoProfissionalDois"));
            rematricula.setEixo_ensino_profissional_tres(rematriculaJson.getInt("EixoEnsinoProfissionalTres"));
            rematricula.setObservacao_opc_noturno(rematriculaJson.getInt("CodigoOpcaoNoturna"));

        }
        catch(Exception e){

            e.printStackTrace();
            return null;
        }

        return rematricula;
    }
}
