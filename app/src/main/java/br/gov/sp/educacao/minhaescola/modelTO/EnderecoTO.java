package br.gov.sp.educacao.minhaescola.modelTO;

import org.json.JSONArray;
import org.json.JSONObject;

import br.gov.sp.educacao.minhaescola.model.Endereco;

public class EnderecoTO {

    private JSONObject alunoJson;
    private int cd_aluno;

    public EnderecoTO(JSONObject alunoJson, int cd_aluno) {

        this.alunoJson = alunoJson;
        this.cd_aluno = cd_aluno;
    }

    public Endereco getEnderecoFromJson(){

        Endereco endereco = new Endereco();

        try{

            endereco.setCd_aluno(cd_aluno);

            endereco.setUf_estado(alunoJson.getString("UfEstado"));
            endereco.setCidade(alunoJson.getString("Cidade"));
            endereco.setEndereco(alunoJson.getString("EnderecoAluno"));
            endereco.setNumero_endereco(alunoJson.getString("NumeroEndereco"));
            endereco.setComplemento(alunoJson.getString("Complemento"));
            endereco.setNumero_cep(alunoJson.getString("NumeroDoCEP"));
            endereco.setTipo_logradouro(alunoJson.getInt("TipoLogradouro"));
            endereco.setLocalizacao_diferenciada(alunoJson.getInt("LocalizacaoDiferenciada"));
            endereco.setBairro(alunoJson.getString("Bairro"));
            endereco.setLatitude(alunoJson.getString("Latitude"));
            endereco.setLongitude(alunoJson.getString("Longitude"));
            endereco.setLatitude_indic(alunoJson.getString("LatitudeIndicativo"));
            endereco.setLongitude_indic(alunoJson.getString("LongitudeIndicativo"));
            endereco.setEnvio_comprovante(alunoJson.getBoolean("EnvioComprovante"));

            return endereco;
        }
        catch (Exception e){

            e.printStackTrace();
            return null;
        }
    }
}
