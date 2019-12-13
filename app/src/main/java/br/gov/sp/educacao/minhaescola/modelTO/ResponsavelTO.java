package br.gov.sp.educacao.minhaescola.modelTO;

import org.json.JSONObject;

import br.gov.sp.educacao.minhaescola.model.Responsavel;

public class ResponsavelTO {

    private JSONObject respJson;
    private String login, senha;
    private int cd_responsavel;

    public ResponsavelTO(JSONObject respJson, String login, String senha, int cd_responsavel) {

        this.respJson = respJson;
        this.login = login;
        this.senha = senha;
        this.cd_responsavel = cd_responsavel;
    }

    public Responsavel getResponsavelFromJson(){

        Responsavel resp = new Responsavel();

        try {

            resp.setCd_responsavel(cd_responsavel);
            resp.setLogin(login);
            resp.setSenha(senha);
            resp.setToken(respJson.getString("Token"));
        }
        catch (Exception e){

            e.printStackTrace();
            return null;
        }

        return resp;
    }
}
