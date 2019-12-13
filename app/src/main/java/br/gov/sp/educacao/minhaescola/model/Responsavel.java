package br.gov.sp.educacao.minhaescola.model;

public class Responsavel {

    private int cd_responsavel;
    private String token;
    private String login;
    private String senha;
    private String email;

    public int getCd_responsavel() {
        return cd_responsavel;
    }

    public void setCd_responsavel(int cd_responsavel) {
        this.cd_responsavel = cd_responsavel;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
