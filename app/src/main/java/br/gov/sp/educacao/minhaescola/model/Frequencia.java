package br.gov.sp.educacao.minhaescola.model;public class Frequencia {    private int cd_frequencia;    private int cd_aluno;    private int cd_disciplina;    private String nome_disciplina;    private int falta;    private int bimestre;    private int ausencia_compensada;    private float porcentagemFalta;    public int getCd_frequencia() {        return cd_frequencia;    }    public void setCd_frequencia(int cd_frequencia) {        this.cd_frequencia = cd_frequencia;    }    public int getCd_aluno() {        return cd_aluno;    }    public void setCd_aluno(int cd_aluno) {        this.cd_aluno = cd_aluno;    }    public int getCd_disciplina() {        return cd_disciplina;    }    public void setCd_disciplina(int cd_disciplina) {        this.cd_disciplina = cd_disciplina;    }    public String getNome_disciplina() {        return nome_disciplina;    }    public void setNome_disciplina(String nome_disciplina) {        this.nome_disciplina = nome_disciplina;    }    public int getFalta() {        return falta;    }    public void setFalta(int falta) {        this.falta = falta;    }    public int getAusencia_compensada() {        return ausencia_compensada;    }    public int getBimestre() {        return bimestre;    }    public void setBimestre(int bimestre) {        this.bimestre = bimestre;    }    public void setAusencias_compensada(int ausencias_compensada) {        this.ausencia_compensada = ausencias_compensada;    }    public float getPorcentagemFalta() {        return porcentagemFalta;    }    public void setPorcentagemfalta(float porcentagemfalta) {        this.porcentagemFalta = porcentagemfalta;    }}