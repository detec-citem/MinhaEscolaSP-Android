package br.gov.sp.educacao.minhaescola.model;

public class ContatoEscola {

    private int cd_contato_escola;
    private int cd_escola;
    private String contato_escola;

    public int getCd_contato_escola() {
        return cd_contato_escola;
    }

    public void setCd_contato_escola(int cd_contato_escola) {
        this.cd_contato_escola = cd_contato_escola;
    }

    public int getCd_escola() {
        return cd_escola;
    }

    public void setCd_escola(int cd_escola) {
        this.cd_escola = cd_escola;
    }

    public String getContato_escola() {
        return contato_escola;
    }

    public void setContato_escola(String contato_escola) {
        this.contato_escola = contato_escola;
    }
}
