package br.gov.sp.educacao.minhaescola.banco;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;

import java.util.ArrayList;

import br.gov.sp.educacao.minhaescola.model.ContatoEscola;
import br.gov.sp.educacao.minhaescola.model.Escola;
import br.gov.sp.educacao.minhaescola.modelTO.EscolaTO;
import br.gov.sp.educacao.minhaescola.util.MyPreferences;

public class EscolaQueries {

    private SQLiteDatabase db;
    private MyPreferences mPref;
    private EscolaTO escolaTO;

    public EscolaQueries(Context ctx) {

        DBCore auxDB = new DBCore(ctx);

        db = auxDB.getWritableDatabase();

        mPref = new MyPreferences(ctx);
    }

    public void inserirEscolas(JSONArray jsonEscolas, int cd_aluno) {

        escolaTO = new EscolaTO(jsonEscolas);

        boolean isResp = (mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL);

        ArrayList<Escola> listaEscolas = escolaTO.getEscolasFromJson(cd_aluno, isResp);

        ContentValues valores = new ContentValues();

        for (Escola escola : listaEscolas) {

            valores.clear();

            valores.put("cd_escola", escola.getCd_escola());
            if(isResp) {

                valores.put("cd_aluno", escola.getCd_aluno());
            }
            else{

                valores.put("cd_aluno", cd_aluno);
            }
            valores.put("cd_unidade", escola.getCd_unidade());
            valores.put("endereco_unidade", escola.getEndereco_unidade());
            valores.put("nome_escola", escola.getNome_escola());
            valores.put("nome_abreviado_escola", escola.getNome_abreviado_escola());
            valores.put("email_escola", escola.getEmail_escola());
            valores.put("municipio", escola.getMunicipio());
            valores.put("nome_diretor", escola.getNome_diretor());


            db.insertWithOnConflict("escola", null, valores, SQLiteDatabase.CONFLICT_REPLACE);

            for (ContatoEscola contatoEscola : escola.getContatosEscola()) {

                valores.clear();

                valores.put("cd_escola", contatoEscola.getCd_escola());
                valores.put("contato_escola", contatoEscola.getContato_escola());

                db.insertWithOnConflict("contato_escola", null, valores, SQLiteDatabase.CONFLICT_REPLACE);
            }
        }

        valores.clear();
    }

    public ArrayList<ContatoEscola> getContatosEscola(int cd_escola) {

        Cursor cursor = db.rawQuery("SELECT " +
                                         "cd_escola, " +
                                         "contato_escola " +
                                         "FROM contato_escola " +
                                         "WHERE cd_escola = " + cd_escola, null);

        ArrayList<ContatoEscola> contatos = new ArrayList<>(cursor.getCount());

        if(cursor.moveToFirst()) {

            ContatoEscola contato;

            do {

                contato = new ContatoEscola();

                //contato.setCd_contato_escola(cursor.getInt(0));
                contato.setCd_escola(cursor.getInt(0));
                contato.setContato_escola(cursor.getString(1));

                contatos.add(contato);
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return contatos;
    }

    public Escola getEscola(int cd_aluno, String nome_escola) {

        Escola escola = new Escola();

        Cursor cursor = db.rawQuery("SELECT cd_escola, " +
                                         "cd_aluno, " +
                                         "cd_unidade, " +
                                         "endereco_unidade, " +
                                         "nome_escola, " +
                                         "nome_abreviado_escola, " +
                                         "email_escola, " +
                                         "municipio, " +
                                         "nome_diretor " +
                                         "FROM escola " +
                                         "WHERE cd_aluno = " + cd_aluno +
                                         " AND nome_escola = ?", new String[]{nome_escola});

        if(cursor.moveToFirst()) {

            escola.setCd_escola(cursor.getInt(0));
            escola.setCd_aluno(cursor.getInt(1));
            escola.setCd_unidade(cursor.getInt(2));
            escola.setEndereco_unidade(cursor.getString(3));
            escola.setNome_escola(cursor.getString(4));
            escola.setNome_abreviado_escola(cursor.getString(5));
            escola.setEmail_escola(cursor.getString(6));
            escola.setMunicipio(cursor.getString(7));
            escola.setNome_diretor(cursor.getString(8));

        }

        cursor.close();

        return escola;
    }
}
