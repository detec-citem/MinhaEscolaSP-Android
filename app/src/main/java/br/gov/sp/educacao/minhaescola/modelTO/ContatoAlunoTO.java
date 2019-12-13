package br.gov.sp.educacao.minhaescola.modelTO;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import br.gov.sp.educacao.minhaescola.model.ContatoAluno;

public class ContatoAlunoTO {

    private JSONArray arrayContato;

    public ContatoAlunoTO(JSONArray arrayContato) {

        this.arrayContato = arrayContato;
    }

    public ArrayList<ContatoAluno> getContatoAlunoFromJson() {

        ArrayList<ContatoAluno> contatosAluno = new ArrayList<>();

        for (int i = 0; i < arrayContato.length(); i++) {

            ContatoAluno contatoAluno = new ContatoAluno();

            try {

                contatoAluno.setCd_aluno(arrayContato.getJSONObject(i).getInt("CodigoAluno"));
                contatoAluno.setCodigo_fone_telefone(arrayContato.getJSONObject(i).getInt("CodigoFoneTelefone"));
                contatoAluno.setValidacao_sms(arrayContato.getJSONObject(i).getInt("ValidacaoSMS"));
                contatoAluno.setCodigo_tipo_telefone(arrayContato.getJSONObject(i).getInt("CodigoTipoTelefone"));
                contatoAluno.setCodigo_ddd(arrayContato.getJSONObject(i).getString("CodigoDDD"));
                contatoAluno.setTelefone_aluno(arrayContato.getJSONObject(i).getString("TelefoneAluno"));
                contatoAluno.setComplemento_telefone(arrayContato.getJSONObject(i).getString("ComplementoTelefone"));

                contatosAluno.add(contatoAluno);
            }
            catch (JSONException e) {

                e.printStackTrace();
            }
        }

        return contatosAluno;
    }

}
