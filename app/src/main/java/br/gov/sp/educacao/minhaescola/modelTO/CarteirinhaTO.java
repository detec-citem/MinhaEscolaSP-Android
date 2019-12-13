package br.gov.sp.educacao.minhaescola.modelTO;

import org.json.JSONObject;

import br.gov.sp.educacao.minhaescola.model.Carteirinha;

public class CarteirinhaTO {

    private JSONObject jsonCarteirinha;
    private int cd_aluno;

    public CarteirinhaTO(JSONObject jsonCarteirinha, int cd_aluno) {

        this.jsonCarteirinha = jsonCarteirinha;
        this.cd_aluno = cd_aluno;
    }

    public Carteirinha getCartFromJson(){

        Carteirinha carteirinha = new Carteirinha();

        carteirinha.setCd_aluno(cd_aluno);

        try {

            carteirinha.setRg(jsonCarteirinha.getString("Rg"));
            carteirinha.setData_nascimento(jsonCarteirinha.getString("DataNascimento"));
            carteirinha.setNome(jsonCarteirinha.getString("Nome"));
            carteirinha.setApelido(jsonCarteirinha.getString("Apelido"));
            carteirinha.setRa(jsonCarteirinha.getString("Ra"));
            carteirinha.setNome_escola(jsonCarteirinha.getString("NomeEscola"));
            carteirinha.setValidade(jsonCarteirinha.getString("Validade"));
            carteirinha.setNome_turma(jsonCarteirinha.getString("DescricaoTurma"));
            carteirinha.setQr_criptografado(jsonCarteirinha.getString("CodigoAlunoCriptografado"));
            carteirinha.setFoto_qr(jsonCarteirinha.getString("QrCode"));
            carteirinha.setFoto_aluno(jsonCarteirinha.getString("Foto"));
        }
        catch (Exception e){

            e.printStackTrace();
            return null;
        }


        return carteirinha;
    }
}
