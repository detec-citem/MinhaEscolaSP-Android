package br.gov.sp.educacao.minhaescola.modelTO;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.gov.sp.educacao.minhaescola.model.Aluno;
import br.gov.sp.educacao.minhaescola.model.ContatoAluno;

public class AlunoTO {

    private JSONObject alunoJson;
    private String login, senha;

    public AlunoTO(JSONObject alunoJson, String login, String senha) {

        this.alunoJson = alunoJson;
        this.login = login;
        this.senha = senha;
    }

    public AlunoTO(JSONObject alunoJson){

        this.alunoJson = alunoJson;
    }

    public Aluno getAlunoFromJson(){

        Aluno aluno  = new Aluno();

        try {

            aluno.setCd_aluno(alunoJson.getInt("IdUsuario"));
            aluno.setNumero_cpf(alunoJson.getString("NumeroCpf"));
            aluno.setNumero_rg(alunoJson.getString("NumeroRg"));
            aluno.setDigito_rg(alunoJson.getString("DigitoRg"));
            aluno.setNome(alunoJson.getString("Nome"));
            aluno.setToken(alunoJson.getString("Token"));
            aluno.setLogin(login);
            aluno.setSenha(senha);
            aluno.setUf_ra(alunoJson.getString("UfRA"));
            aluno.setNumero_ra(alunoJson.getString("NumeroRA"));
            aluno.setDigito_ra(alunoJson.getString("DigitoRA"));
            aluno.setEmail_aluno(alunoJson.getString("EmailAluno"));
            aluno.setMunicipio_nascimento(alunoJson.getString("MunicipioNascimento"));
            aluno.setUf_nascimento(alunoJson.getString("UfNascimento"));
            aluno.setData_nascimento(alunoJson.getString("DataNascimento"));
            aluno.setNacionalidade(alunoJson.getString("Nacionalidade"));
            aluno.setNomeMae(alunoJson.getString("NomeDaMae"));
            aluno.setNomePai(alunoJson.getString("NomeDoPai"));
            aluno.setEscola_centralizada(alunoJson.getBoolean("EscolaCentralizada"));
            aluno.setIdade(alunoJson.getInt("Idade"));
            aluno.setResponde_rematricula(alunoJson.getBoolean("RespondeRematricula"));

        }
        catch (JSONException e) {

            e.printStackTrace();
            return null;
        }

        return aluno;
    }

    public Aluno getAlunoFromJsonResponsavel(){

        Aluno aluno  = new Aluno();

        try {

            aluno.setCd_aluno(alunoJson.getInt("CodigoAluno"));
            aluno.setNome(alunoJson.getString("NomeAluno"));
            aluno.setNumero_cpf(alunoJson.getString("NumeroCpf"));
            aluno.setUf_ra(alunoJson.getString("UfRA"));
            aluno.setNumero_ra(alunoJson.getString("NumeroRA"));
            aluno.setDigito_ra(alunoJson.getString("DigitoRA"));
            aluno.setNumero_rg(alunoJson.getString("NumeroRg"));
            aluno.setDigito_rg(alunoJson.getString("DigitoRg"));
            aluno.setEmail_aluno(alunoJson.getString("EmailAluno"));
            aluno.setMunicipio_nascimento(alunoJson.getString("MunicipioNascimento"));
            aluno.setUf_nascimento(alunoJson.getString("UfNascimento"));
            aluno.setData_nascimento(alunoJson.getString("DataNascimento"));
            aluno.setNacionalidade(alunoJson.getString("Nacionalidade"));
            aluno.setNomeMae(alunoJson.getString("NomeDaMae"));
            aluno.setNomePai(alunoJson.getString("NomeDoPai"));
            aluno.setIdade(alunoJson.getInt("Idade"));
            aluno.setResponde_rematricula(alunoJson.getBoolean("RespondeRematricula"));

        }
        catch (JSONException e) {

            e.printStackTrace();
            return null;
        }

        return aluno;
    }

}
