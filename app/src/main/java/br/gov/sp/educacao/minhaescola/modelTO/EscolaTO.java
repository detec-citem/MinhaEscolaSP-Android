package br.gov.sp.educacao.minhaescola.modelTO;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import br.gov.sp.educacao.minhaescola.model.ContatoAluno;
import br.gov.sp.educacao.minhaescola.model.ContatoEscola;
import br.gov.sp.educacao.minhaescola.model.Escola;

public class EscolaTO {

    private JSONArray arrayEscolas;

    public EscolaTO(JSONArray arrayEscolas) {

        this.arrayEscolas = arrayEscolas;
    }

    public ArrayList<Escola> getEscolasFromJson(int cd_aluno, boolean isResp) {

        ArrayList<Escola> listaEscolas = new ArrayList<>(arrayEscolas.length());

        for (int i = 0; i < arrayEscolas.length(); i++) {

            Escola escola = new Escola();

            try {

                if(isResp){

                    escola.setCd_aluno(arrayEscolas.getJSONObject(i).getInt("CodigoAluno"));
                }
                else{

                    escola.setCd_aluno(cd_aluno);
                }

                escola.setCd_unidade(arrayEscolas.getJSONObject(i).getInt("CodigoUnidade"));
                escola.setEndereco_unidade(arrayEscolas.getJSONObject(i).getString("EnderecoUnidade"));
                escola.setCd_escola(arrayEscolas.getJSONObject(i).getInt("CodigoEscola"));
                escola.setNome_escola(arrayEscolas.getJSONObject(i).getString("NomeEscola"));
                escola.setNome_abreviado_escola(arrayEscolas.getJSONObject(i).getString("NomeAbreviadoEscola"));
                escola.setEmail_escola(arrayEscolas.getJSONObject(i).getString("EmailEscola"));
                escola.setNome_diretor(arrayEscolas.getJSONObject(i).getString("NomeDiretor"));
                escola.setMunicipio(arrayEscolas.getJSONObject(i).getString("Municipio"));

                JSONArray jsonContatoEscola = arrayEscolas.getJSONObject(i).getJSONArray("TelefoneUnidade");

                ArrayList<ContatoEscola> contatosEscola = new ArrayList<>();

                for(int x = 0; x < jsonContatoEscola.length(); x++){

                    ContatoEscola contatoEscola = new ContatoEscola();

                    contatoEscola.setCd_escola(escola.getCd_escola());
                    contatoEscola.setContato_escola(jsonContatoEscola.getJSONObject(x).getString("ContatoEscola"));

                    contatosEscola.add(contatoEscola);
                }

                escola.setContatosEscola(contatosEscola);

                listaEscolas.add(escola);
            }
            catch (JSONException e) {

                e.printStackTrace();
            }
        }
        return listaEscolas;
    }
}
