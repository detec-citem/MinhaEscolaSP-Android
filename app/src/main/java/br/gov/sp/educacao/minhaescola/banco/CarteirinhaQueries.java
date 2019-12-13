package br.gov.sp.educacao.minhaescola.banco;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.graphics.Bitmap;

import org.json.JSONObject;

import br.gov.sp.educacao.minhaescola.model.Carteirinha;

import br.gov.sp.educacao.minhaescola.modelTO.CarteirinhaTO;

import static br.gov.sp.educacao.minhaescola.util.ImageHelper.bitmapFromBase64;

public class CarteirinhaQueries {

    private SQLiteDatabase db;

    public CarteirinhaQueries(Context ctx) {

        DBCore auxDB = new DBCore(ctx);

        db = auxDB.getWritableDatabase();
    }

    public void inserirCarteirinha(JSONObject jsonCarteirinha, int cd_aluno) {

        CarteirinhaTO carteirinhaTO = new CarteirinhaTO(jsonCarteirinha, cd_aluno);

        Carteirinha carteirinha = carteirinhaTO.getCartFromJson();

        ContentValues valores = new ContentValues();

        valores.put("cd_aluno", cd_aluno);
        valores.put("rg", carteirinha.getRg());
        valores.put("data_nascimento", carteirinha.getData_nascimento());
        valores.put("nome", carteirinha.getNome());
        valores.put("apelido", carteirinha.getApelido());
        valores.put("ra", carteirinha.getRa());
        valores.put("nome_escola", carteirinha.getNome_escola());
        valores.put("validade", carteirinha.getValidade());
        valores.put("nome_turma", carteirinha.getNome_turma());
        valores.put("qr_criptografado", carteirinha.getQr_criptografado());
        valores.put("foto_qr", carteirinha.getFoto_qr());
        valores.put("foto_aluno", carteirinha.getFoto_aluno());

        db.insertWithOnConflict("carteirinha", null, valores, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public Carteirinha getCarteirinha(int cd_aluno){

        Carteirinha carteirinha = new Carteirinha();

        Cursor cursor = db.rawQuery("SELECT cd_carteirinha, " +
                                         "cd_aluno, " +
                                         "rg, " +
                                         "data_nascimento, " +
                                         "nome, " +
                                         "apelido, " +
                                         "ra, " +
                                         "nome_escola, " +
                                         "validade, " +
                                         "nome_turma, " +
                                         "qr_criptografado, " +
                                         "foto_qr, " +
                                         "foto_aluno FROM carteirinha " +
                                         "WHERE cd_aluno = " + cd_aluno, null);

        if(cursor.moveToFirst()) {

            carteirinha.setCd_carteirinha(cursor.getInt(0));
            carteirinha.setCd_aluno(cursor.getInt(1));
            carteirinha.setRg(cursor.getString(2));
            carteirinha.setData_nascimento(cursor.getString(3));
            carteirinha.setNome(cursor.getString(4));
            carteirinha.setApelido(cursor.getString(5));
            carteirinha.setRa(cursor.getString(6));
            carteirinha.setNome_escola(cursor.getString(7));
            carteirinha.setValidade(cursor.getString(8));
            carteirinha.setNome_turma(cursor.getString(9));
            carteirinha.setQr_criptografado(cursor.getString(10));
            carteirinha.setFoto_qr(cursor.getString(11));
            carteirinha.setFoto_aluno(cursor.getString(12));
        }

        return carteirinha;
    }

    public boolean temCarteirinha(int cd_aluno) {

        Cursor cursor = db.rawQuery("SELECT cd_carteirinha FROM carteirinha WHERE cd_aluno = " + cd_aluno, null);

        boolean temCarteirinha = false;

        if(cursor.moveToFirst()) {

            temCarteirinha = true;
        }
        cursor.close();

        return temCarteirinha;
    }

    public Bitmap getImgCarteirinha(int cd_aluno) {

        Bitmap bitmapImg = null;

        Cursor cursor = db.rawQuery("SELECT foto_aluno FROM carteirinha WHERE cd_aluno = " + cd_aluno, null);

        if(cursor.moveToFirst()) {

            String fotoBase64 = cursor.getString(0);

            bitmapImg = bitmapFromBase64(fotoBase64);
        }
        cursor.close();

        return bitmapImg;
    }
}
