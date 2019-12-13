package br.gov.sp.educacao.minhaescola.model;

public class Aluno {

    private int cd_aluno;
    private String numero_cpf;
    private String numero_rg;
    private String digito_rg;
    private String nome;
    private String token;
    private String login;
    private String senha;
    private String uf_ra;
    private String numero_ra;
    private String digito_ra;
    private String email_aluno;
    private String municipio_nascimento;
    private String uf_nascimento;
    private String data_nascimento;
    private String nacionalidade;
    private String nomeMae;
    private String nomePai;
    private boolean escola_centralizada;
    private int idade;
    private boolean responde_rematricula;

    public int getCd_aluno() {
        return cd_aluno;
    }

    public void setCd_aluno(int cd_aluno) {
        this.cd_aluno = cd_aluno;
    }

    public String getNumero_cpf() {
        return numero_cpf;
    }

    public void setNumero_cpf(String numero_cpf) {
        this.numero_cpf = numero_cpf;
    }

    public String getNumero_rg() {
        return numero_rg;
    }

    public void setNumero_rg(String numero_rg) {
        this.numero_rg = numero_rg;
    }

    public String getDigito_rg() {
        return digito_rg;
    }

    public void setDigito_rg(String digito_rg) {
        this.digito_rg = digito_rg;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public String getUf_ra() {
        return uf_ra;
    }

    public void setUf_ra(String uf_ra) {
        this.uf_ra = uf_ra;
    }

    public String getNumero_ra() {
        return numero_ra;
    }

    public void setNumero_ra(String numero_ra) {
        this.numero_ra = numero_ra;
    }

    public String getDigito_ra() {
        return digito_ra;
    }

    public void setDigito_ra(String digito_ra) {
        this.digito_ra = digito_ra;
    }

    public String getEmail_aluno() {
        return email_aluno;
    }

    public void setEmail_aluno(String email_aluno) {
        this.email_aluno = email_aluno;
    }

    public String getMunicipio_nascimento() {
        return municipio_nascimento;
    }

    public void setMunicipio_nascimento(String municipio_nascimento) {
        this.municipio_nascimento = municipio_nascimento;
    }

    public String getUf_nascimento() {
        return uf_nascimento;
    }

    public void setUf_nascimento(String uf_nascimento) {
        this.uf_nascimento = uf_nascimento;
    }

    public String getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(String data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public String getNomePai() {
        return nomePai;
    }

    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
    }

    public boolean isEscola_centralizada() {
        return escola_centralizada;
    }

    public void setEscola_centralizada(boolean escola_centralizada) {
        this.escola_centralizada = escola_centralizada;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public boolean isResponde_rematricula() {
        return responde_rematricula;
    }

    public void setResponde_rematricula(boolean responde_rematricula) {
        this.responde_rematricula = responde_rematricula;
    }
}
