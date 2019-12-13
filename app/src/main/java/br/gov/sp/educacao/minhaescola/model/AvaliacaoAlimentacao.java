package br.gov.sp.educacao.minhaescola.model;

import java.io.Serializable;

public class AvaliacaoAlimentacao implements Serializable {

    private int[] questao1;
    private int[] questao2;
    private int[] questao3;
    private int[] questao4;
    private int[] questao5;
    private int codTurma;

    public int[] getQuestao1() {
        return questao1;
    }

    public void setQuestao1(int[] questao1) {
        this.questao1 = questao1;
    }

    public int[] getQuestao2() {
        return questao2;
    }

    public void setQuestao2(int[] questao2) {
        this.questao2 = questao2;
    }

    public int[] getQuestao3() {
        return questao3;
    }

    public void setQuestao3(int[] questao3) {
        this.questao3 = questao3;
    }

    public int[] getQuestao4() {
        return questao4;
    }

    public void setQuestao4(int[] questao4) {
        this.questao4 = questao4;
    }

    public int[] getQuestao5() {
        return questao5;
    }

    public void setQuestao5(int[] questao5) {
        this.questao5 = questao5;
    }

    public int getCodTurma() {
        return codTurma;
    }

    public void setCodTurma(int codTurma) {
        this.codTurma = codTurma;
    }
}
