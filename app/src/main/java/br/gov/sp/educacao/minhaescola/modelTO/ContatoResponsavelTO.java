package br.gov.sp.educacao.minhaescola.modelTO;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import br.gov.sp.educacao.minhaescola.model.ContatoResponsavel;

public class ContatoResponsavelTO {

    private JSONArray arrayContato;

    private int cd_responsavel;

    public ContatoResponsavelTO(JSONArray arrayContato, int cd_responsavel) {

        this.arrayContato = arrayContato;
        this.cd_responsavel = cd_responsavel;
    }

    public ArrayList<ContatoResponsavel> getContatoResponsavelFromJson() {

        ArrayList<ContatoResponsavel> contatosResponsavel = new ArrayList<>();

        for (int i = 0; i < arrayContato.length(); i++) {

            ContatoResponsavel contatoResponsavel = new ContatoResponsavel();

            try {

                contatoResponsavel.setCd_responsavel(cd_responsavel);
                contatoResponsavel.setCodigo_fone_telefone(arrayContato.getJSONObject(i).getInt("CodigoFoneTelefone"));
                contatoResponsavel.setValidacao_sms(arrayContato.getJSONObject(i).getInt("ValidacaoSMS"));
                contatoResponsavel.setCodigo_tipo_telefone(arrayContato.getJSONObject(i).getInt("CodigoTipoTelefone"));
                contatoResponsavel.setCodigo_ddd(arrayContato.getJSONObject(i).getString("CodigoDDD"));
                contatoResponsavel.setTelefone_responsavel(arrayContato.getJSONObject(i).getString("TelefoneResponsavel"));
                contatoResponsavel.setComplemento_telefone(arrayContato.getJSONObject(i).getString("ComplementoTelefone"));

                contatosResponsavel.add(contatoResponsavel);
            }
            catch (JSONException e) {

                e.printStackTrace();
            }
        }

        return contatosResponsavel;
    }
}
