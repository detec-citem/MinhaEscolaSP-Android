package br.gov.sp.educacao.minhaescola.model;

public class ContatoResponsavel {

    // 1 = residencia
    // 2 = comercio
    // 3 = celular
    // 4 = recado

    private int cd_contato_responsavel;
    private int cd_responsavel;
    private int codigo_fone_telefone;
    private int validacao_sms;
    private int codigo_tipo_telefone;
    private String codigo_ddd;
    private String telefone_responsavel;
    private String complemento_telefone;
    private int operacao;

    public int getCd_contato_responsavel() {
        return cd_contato_responsavel;
    }

    public void setCd_contato_responsavel(int cd_contato_responsavel) {
        this.cd_contato_responsavel = cd_contato_responsavel;
    }

    public int getCd_responsavel() {
        return cd_responsavel;
    }

    public void setCd_responsavel(int cd_responsavel) {
        this.cd_responsavel = cd_responsavel;
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

    public String getTelefone_responsavel() {
        return telefone_responsavel;
    }

    public void setTelefone_responsavel(String telefone_responsavel) {
        this.telefone_responsavel = telefone_responsavel;
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
