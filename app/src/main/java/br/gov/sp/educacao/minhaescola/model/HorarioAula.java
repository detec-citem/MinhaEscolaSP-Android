package br.gov.sp.educacao.minhaescola.model;

public class HorarioAula {

    private int cd_horario_aula;
    private int cd_aluno;
    private int cd_materia;
    private String nome_materia;
    private String nome_professor;
    private String data_hora_inicio;
    private String data_hora_fim;
    private String nome_turma;
    private String dia_semana;

    public static final String SEGUNDA = "Segunda-Feira";
    public static final String TERCA = "Terça-Feira";
    public static final String QUARTA = "Quarta-Feira";
    public static final String QUINTA = "Quinta-Feira";
    public static final String SEXTA = "Sexta-Feira";

    public int getCd_horario_aula() {
        return cd_horario_aula;
    }

    public void setCd_horario_aula(int cd_horario_aula) {
        this.cd_horario_aula = cd_horario_aula;
    }

    public int getCd_aluno() {
        return cd_aluno;
    }

    public void setCd_aluno(int cd_aluno) {
        this.cd_aluno = cd_aluno;
    }

    public int getCd_materia() {
        return cd_materia;
    }

    public void setCd_materia(int cd_materia) {
        this.cd_materia = cd_materia;
    }

    public String getNome_materia() {
        return nome_materia;
    }

    public void setNome_materia(String nome_materia) {
        this.nome_materia = nome_materia;
    }

    public String getNome_professor() {
        return nome_professor;
    }

    public void setNome_professor(String nome_professor) {
        this.nome_professor = nome_professor;
    }

    public String getData_hora_inicio() {
        return data_hora_inicio;
    }

    public void setData_hora_inicio(String data_hora_inicio) {
        this.data_hora_inicio = data_hora_inicio;
    }

    public String getData_hora_fim() {
        return data_hora_fim;
    }

    public void setData_hora_fim(String data_hora_fim) {
        this.data_hora_fim = data_hora_fim;
    }

    public String getNome_turma() {
        return nome_turma;
    }

    public void setNome_turma(String nome_turma) {
        this.nome_turma = nome_turma;
    }

    public String getDia_semana() {
        return dia_semana;
    }

    public void setDia_semana(String dia_semana) {
        this.dia_semana = dia_semana;
    }
}
