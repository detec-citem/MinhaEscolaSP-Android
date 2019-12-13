package br.gov.sp.educacao.minhaescola.model;

public class Carteirinha {

    private int cd_carteirinha;
    private int cd_aluno;
    private String rg;
    private String data_nascimento;
    private String nome;
    private String apelido;
    private String ra;
    private String nome_escola;
    private String validade;
    private String nome_turma;
    private String qr_criptografado;
    private String foto_qr;
    private String foto_aluno;

    public int getCd_carteirinha() {
        return cd_carteirinha;
    }

    public void setCd_carteirinha(int cd_carteirinha) {
        this.cd_carteirinha = cd_carteirinha;
    }

    public int getCd_aluno() {
        return cd_aluno;
    }

    public void setCd_aluno(int cd_aluno) {
        this.cd_aluno = cd_aluno;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(String data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public String getNome_escola() {
        return nome_escola;
    }

    public void setNome_escola(String nome_escola) {
        this.nome_escola = nome_escola;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public String getNome_turma() {
        return nome_turma;
    }

    public void setNome_turma(String nome_turma) {
        this.nome_turma = nome_turma;
    }

    public String getQr_criptografado() {
        return qr_criptografado;
    }

    public void setQr_criptografado(String qr_criptografado) {
        this.qr_criptografado = qr_criptografado;
    }

    public String getFoto_qr() {
        return foto_qr;
    }

    public void setFoto_qr(String foto_qr) {
        this.foto_qr = foto_qr;
    }

    public String getFoto_aluno() {
        return foto_aluno;
    }

    public void setFoto_aluno(String foto_aluno) {
        this.foto_aluno = foto_aluno;
    }
}
