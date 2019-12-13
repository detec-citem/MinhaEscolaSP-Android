package br.gov.sp.educacao.minhaescola.model;

public class DadosBoletim {

    private String Ano;
    private String Ra;
    private String Dig;
    private String Uf;
    private String Chave;
    private String DataNascimento;
    private String CodigoTurma;

    public String getAno() {
        return Ano;
    }

    public void setAno(String ano) {
        Ano = ano;
    }

    public String getRa() {
        return Ra;
    }

    public void setRa(String ra) {
        Ra = ra;
    }

    public String getDig() {
        return Dig;
    }

    public void setDig(String dig) {
        Dig = dig;
    }

    public String getUf() {
        return Uf;
    }

    public void setUf(String uf) {
        Uf = uf;
    }

    public String getChave() {
        return Chave;
    }

    public void setChave(String chave) {
        Chave = chave;
    }

    public String getDataNascimento() {
        return DataNascimento;
    }

    public String getCodigoTurma() {
        return CodigoTurma;
    }

    public void setCodigoTurma(String codigoTurma) {
        CodigoTurma = codigoTurma;
    }

    public void setDataNascimento(String dataNascimento) {
        DataNascimento = dataNascimento;
    }
}
