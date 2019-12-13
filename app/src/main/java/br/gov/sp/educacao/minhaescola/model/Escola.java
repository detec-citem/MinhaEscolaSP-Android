package br.gov.sp.educacao.minhaescola.model;

import java.util.ArrayList;

public class Escola {

    private int cd_escola;
    private int cd_aluno;
    private int cd_unidade;
    private String nome_escola;
    private String endereco_unidade;
    private String nome_abreviado_escola;
    private String email_escola;
    private String municipio;
    private String nome_diretor;
    private ArrayList<ContatoEscola> contatosEscola;


    public int getCd_escola() {
        return cd_escola;
    }

    public void setCd_escola(int cd_escola) {
        this.cd_escola = cd_escola;
    }

    public int getCd_aluno() {
        return cd_aluno;
    }

    public void setCd_aluno(int cd_aluno) {
        this.cd_aluno = cd_aluno;
    }

    public int getCd_unidade() {
        return cd_unidade;
    }

    public void setCd_unidade(int cd_unidade) {
        this.cd_unidade = cd_unidade;
    }

    public String getEndereco_unidade() {
        return endereco_unidade;
    }

    public void setEndereco_unidade(String endereco_unidade) {
        this.endereco_unidade = endereco_unidade;
    }

    public String getNome_escola() {
        return nome_escola;
    }

    public void setNome_escola(String nome_escola) {
        this.nome_escola = nome_escola;
    }

    public String getNome_abreviado_escola() {
        return nome_abreviado_escola;
    }

    public void setNome_abreviado_escola(String nome_abreviado_escola) {
        this.nome_abreviado_escola = nome_abreviado_escola;
    }

    public String getEmail_escola() {
        return email_escola;
    }

    public void setEmail_escola(String email_escola) {
        this.email_escola = email_escola;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getNome_diretor() {
        return nome_diretor;
    }

    public void setNome_diretor(String nome_diretor) {
        this.nome_diretor = nome_diretor;
    }

    public ArrayList<ContatoEscola> getContatosEscola() {
        return contatosEscola;
    }

    public void setContatosEscola(ArrayList<ContatoEscola> contatosEscola) {
        this.contatosEscola = contatosEscola;
    }
}
