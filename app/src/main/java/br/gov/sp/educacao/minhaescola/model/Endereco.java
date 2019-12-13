package br.gov.sp.educacao.minhaescola.model;

public class Endereco {

    private int cd_endereco;
    private int cd_aluno;
    private String uf_estado;
    private String cidade;
    private String endereco;
    private String numero_endereco;
    private String complemento;
    private String numero_cep;
    private int tipo_logradouro;
    private int localizacao_diferenciada;
    private String bairro;
    private String latitude;
    private String longitude;
    private String latitude_indic;
    private String longitude_indic;
    private boolean envio_comprovante;

    public int getCd_endereco() {
        return cd_endereco;
    }

    public void setCd_endereco(int cd_endereco) {
        this.cd_endereco = cd_endereco;
    }

    public int getCd_aluno() {
        return cd_aluno;
    }

    public void setCd_aluno(int cd_aluno) {
        this.cd_aluno = cd_aluno;
    }

    public String getUf_estado() {
        return uf_estado;
    }

    public void setUf_estado(String uf_estado) {
        this.uf_estado = uf_estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero_endereco() {
        return numero_endereco;
    }

    public void setNumero_endereco(String numero_endereco) {
        this.numero_endereco = numero_endereco;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getNumero_cep() {
        return numero_cep;
    }

    public void setNumero_cep(String numero_cep) {
        this.numero_cep = numero_cep;
    }

    public int getTipo_logradouro() {
        return tipo_logradouro;
    }

    public void setTipo_logradouro(int tipo_logradouro) {
        this.tipo_logradouro = tipo_logradouro;
    }

    public int getLocalizacao_diferenciada() {
        return localizacao_diferenciada;
    }

    public void setLocalizacao_diferenciada(int localizacao_diferenciada) {
        this.localizacao_diferenciada = localizacao_diferenciada;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude_indic() {
        return latitude_indic;
    }

    public void setLatitude_indic(String latitude_indic) {
        this.latitude_indic = latitude_indic;
    }

    public String getLongitude_indic() {
        return longitude_indic;
    }

    public void setLongitude_indic(String longitude_indic) {
        this.longitude_indic = longitude_indic;
    }

    public boolean isEnvio_comprovante() {
        return envio_comprovante;
    }

    public void setEnvio_comprovante(boolean envio_comprovante) {
        this.envio_comprovante = envio_comprovante;
    }
}
