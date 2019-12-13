package br.gov.sp.educacao.minhaescola.model;

public class Nota {

    private int cd_nota;
    private int cd_aluno;
    private int bimestre;
    private int cd_disciplina;
    private String nome_disciplina;
    private double nota;

    private String descricaoAtividade;
    private String notas;

    public String getDescricaoAtividade() {
        return descricaoAtividade;
    }

    public void setDescricaoAtividade(String descricaoAtividade) {
        this.descricaoAtividade = descricaoAtividade;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public int getCd_nota() {
        return cd_nota;
    }

    public void setCd_nota(int cd_nota) {
        this.cd_nota = cd_nota;
    }

    public int getCd_aluno() {
        return cd_aluno;
    }

    public void setCd_aluno(int cd_aluno) {
        this.cd_aluno = cd_aluno;
    }

    public int getBimestre() {
        return bimestre;
    }

    public void setBimestre(int bimestre) {
        this.bimestre = bimestre;
    }

    public int getCd_disciplina() {
        return cd_disciplina;
    }

    public void setCd_disciplina(int cd_disciplina) {
        this.cd_disciplina = cd_disciplina;
    }

    public String getNome_disciplina() {
        return nome_disciplina;
    }

    public void setNome_disciplina(String nome_disciplina) {
        this.nome_disciplina = nome_disciplina;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }
}
