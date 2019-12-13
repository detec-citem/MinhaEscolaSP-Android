package br.gov.sp.educacao.minhaescola.model;

import android.support.annotation.NonNull;

public class Turma implements Comparable<Turma>{

    private int cd_turma;
    private int cd_aluno;
    private int ano_letivo;
    private int cd_tipo_ensino;
    private String tipo_ensino;
    private String nome_turma;
    private String nome_escola;
    private String situacao_aprovacao;
    private String situacao_matricula;
    private String dt_inicio_matricula;
    private String dt_fim_matricula;
    private long cd_matricula_aluno;

    public int getCd_turma() {
        return cd_turma;
    }

    public void setCd_turma(int cd_turma) {
        this.cd_turma = cd_turma;
    }

    public int getCd_aluno() {
        return cd_aluno;
    }

    public void setCd_aluno(int cd_aluno) {
        this.cd_aluno = cd_aluno;
    }

    public int getAno_letivo() {
        return ano_letivo;
    }

    public void setAno_letivo(int ano_letivo) {
        this.ano_letivo = ano_letivo;
    }

    public int getCd_tipo_ensino() {
        return cd_tipo_ensino;
    }

    public void setCd_tipo_ensino(int cd_tipo_ensino) {
        this.cd_tipo_ensino = cd_tipo_ensino;
    }

    public String getNome_turma() {
        return nome_turma;
    }

    public void setNome_turma(String nome_turma) {
        this.nome_turma = nome_turma;
    }

    public String getNome_escola() {
        return nome_escola;
    }

    public void setNome_escola(String nome_escola) {
        this.nome_escola = nome_escola;
    }

    public String getSituacao_aprovacao() {
        return situacao_aprovacao;
    }

    public void setSituacao_aprovacao(String situacao_aprovacao) {
        this.situacao_aprovacao = situacao_aprovacao;
    }

    public String getSituacao_matricula() {
        return situacao_matricula;
    }

    public void setSituacao_matricula(String situacao_matricula) {
        this.situacao_matricula = situacao_matricula;
    }

    public String getDt_inicio_matricula() {
        return dt_inicio_matricula;
    }

    public void setDt_inicio_matricula(String dt_inicio_matricula) {
        this.dt_inicio_matricula = dt_inicio_matricula;
    }

    public String getTipo_ensino() {
        return tipo_ensino;
    }

    public void setTipo_ensino(String tipo_ensino) {
        this.tipo_ensino = tipo_ensino;
    }

    public String getDt_fim_matricula() {
        return dt_fim_matricula;
    }

    public void setDt_fim_matricula(String dt_fim_matricula) {
        this.dt_fim_matricula = dt_fim_matricula;
    }

    public long getCd_matricula_aluno() {
        return cd_matricula_aluno;
    }

    public void setCd_matricula_aluno(long cd_matricula_aluno) {
        this.cd_matricula_aluno = cd_matricula_aluno;
    }

    @Override
    public int compareTo(@NonNull Turma turma) {
        if(this.ano_letivo > turma.getAno_letivo()){
            return -1;
        }
        if(this.ano_letivo < turma.getAno_letivo()){
            return 1;
        }
        return 0;
    }
}
