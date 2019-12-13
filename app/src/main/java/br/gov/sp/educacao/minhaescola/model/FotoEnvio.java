package br.gov.sp.educacao.minhaescola.model;

public class FotoEnvio {

    private byte[] Foto;

    public FotoEnvio(byte[] foto) {
        Foto = foto;
    }

    public byte[] getFoto() {
        return Foto;
    }

    public void setFoto(byte[] foto) {
        Foto = foto;
    }
}
