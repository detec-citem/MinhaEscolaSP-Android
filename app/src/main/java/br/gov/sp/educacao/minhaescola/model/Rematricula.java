package br.gov.sp.educacao.minhaescola.model;

public class Rematricula {

    /*
    1    ADMINISTRAÇÃO
    2    LOGÍSTICA
    3    MARKETING
    4    RECURSOS HUMANOS
    5    INFORMÁTICA
    6    INFORMÁTICA PARA INTERNET
    7    REDES DE COMPUTADORES
    8    DESENVOLVIMENTO DE SISTEMA
    */

    private int cd_interesse_rematricula;
    private int cd_aluno;
    private int ano_letivo;
    private int ano_letivo_rematricula;
    private boolean interesse_continuidade;
    private boolean interesse_novotec;
    private boolean interesse_turno_integral;
    private boolean interesse_espanhol;
    private boolean interesse_noturno;
    private int eixo_ensino_profissional_um;
    private int eixo_ensino_profissional_dois;
    private int eixo_ensino_profissional_tres;
    private int observacao_opc_noturno;
    private boolean aceito_termos_responsabilidade;

    public int getCd_interesse_rematricula() {
        return cd_interesse_rematricula;
    }

    public void setCd_interesse_rematricula(int cd_interesse_rematricula) {
        this.cd_interesse_rematricula = cd_interesse_rematricula;
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

    public int getAno_letivo_rematricula() {
        return ano_letivo_rematricula;
    }

    public void setAno_letivo_rematricula(int ano_letivo_rematricula) {
        this.ano_letivo_rematricula = ano_letivo_rematricula;
    }

    public boolean isInteresse_continuidade() {
        return interesse_continuidade;
    }

    public void setInteresse_continuidade(boolean interesse_continuidade) {
        this.interesse_continuidade = interesse_continuidade;
    }

    public boolean isInteresse_novotec() {
        return interesse_novotec;
    }

    public void setInteresse_novotec(boolean interesse_novotec) {
        this.interesse_novotec = interesse_novotec;
    }

    public boolean isInteresse_turno_integral() {
        return interesse_turno_integral;
    }

    public void setInteresse_turno_integral(boolean interesse_turno_integral) {
        this.interesse_turno_integral = interesse_turno_integral;
    }

    public boolean isInteresse_espanhol() {
        return interesse_espanhol;
    }

    public void setInteresse_espanhol(boolean interesse_espanhol) {
        this.interesse_espanhol = interesse_espanhol;
    }

    public boolean isInteresse_noturno() {
        return interesse_noturno;
    }

    public void setInteresse_noturno(boolean interesse_noturno) {
        this.interesse_noturno = interesse_noturno;
    }

    public boolean isAceito_termos_responsabilidade() {
        return aceito_termos_responsabilidade;
    }

    public void setAceito_termos_responsabilidade(boolean aceito_termos_responsabilidade) {
        this.aceito_termos_responsabilidade = aceito_termos_responsabilidade;
    }

    public int getEixo_ensino_profissional_um() {
        return eixo_ensino_profissional_um;
    }

    public void setEixo_ensino_profissional_um(int eixo_ensino_profissional_um) {
        this.eixo_ensino_profissional_um = eixo_ensino_profissional_um;
    }

    public int getEixo_ensino_profissional_dois() {
        return eixo_ensino_profissional_dois;
    }

    public void setEixo_ensino_profissional_dois(int eixo_ensino_profissional_dois) {
        this.eixo_ensino_profissional_dois = eixo_ensino_profissional_dois;
    }

    public int getEixo_ensino_profissional_tres() {
        return eixo_ensino_profissional_tres;
    }

    public void setEixo_ensino_profissional_tres(int eixo_ensino_profissional_tres) {
        this.eixo_ensino_profissional_tres = eixo_ensino_profissional_tres;
    }

    public int getObservacao_opc_noturno() {
        return observacao_opc_noturno;
    }

    public void setObservacao_opc_noturno(int observacao_opc_noturno) {
        this.observacao_opc_noturno = observacao_opc_noturno;
    }
}
