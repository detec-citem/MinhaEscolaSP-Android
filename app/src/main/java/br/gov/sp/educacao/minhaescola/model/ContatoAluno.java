package br.gov.sp.educacao.minhaescola.model;

public class ContatoAluno {


    // 1 = residencia
    // 2 = comercio
    // 3 = celular
    // 4 = recado

    private int cd_contato_aluno;
    private int cd_aluno;
    private int codigo_fone_telefone;
    private int validacao_sms;
    private int codigo_tipo_telefone;
    private String codigo_ddd;
    private String telefone_aluno;
    private String complemento_telefone;
    private int operacao;

    public int getCd_contato_aluno() {
        return cd_contato_aluno;
    }

    public void setCd_contato_aluno(int cd_contato_aluno) {
        this.cd_contato_aluno = cd_contato_aluno;
    }

    public int getCd_aluno() {
        return cd_aluno;
    }

    public void setCd_aluno(int cd_aluno) {
        this.cd_aluno = cd_aluno;
    }

    public int getCodigo_fone_telefone() {
        return codigo_fone_telefone;
    }

    public void setCodigo_fone_telefone(int codigo_fone_telefone) {
        this.codigo_fone_telefone = codigo_fone_telefone;
    }

    public int getValidacao_sms() {
        return validacao_sms;
    }

    public void setValidacao_sms(int validacao_sms) {
        this.validacao_sms = validacao_sms;
    }

    public int getCodigo_tipo_telefone() {
        return codigo_tipo_telefone;
    }

    public void setCodigo_tipo_telefone(int codigo_tipo_telefone) {
        this.codigo_tipo_telefone = codigo_tipo_telefone;
    }

    public String getCodigo_ddd() {
        return codigo_ddd;
    }

    public void setCodigo_ddd(String codigo_ddd) {
        this.codigo_ddd = codigo_ddd;
    }

    public String getTelefone_aluno() {
        return telefone_aluno;
    }

    public void setTelefone_aluno(String telefone_aluno) {
        this.telefone_aluno = telefone_aluno;
    }

    public int getOperacao() {
        return operacao;
    }

    public void setOperacao(int operacao) {
        this.operacao = operacao;
    }

    public String getComplemento_telefone() {
        return complemento_telefone;
    }

    public void setComplemento_telefone(String complemento_telefone) {
        this.complemento_telefone = complemento_telefone;
    }
}
