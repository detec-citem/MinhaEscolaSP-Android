package br.gov.sp.educacao.minhaescola.banco;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import br.gov.sp.educacao.minhaescola.model.Turma;
import br.gov.sp.educacao.minhaescola.modelTO.TurmaTO;
import br.gov.sp.educacao.minhaescola.util.MyPreferences;

public class TurmaQueries {

    private SQLiteDatabase db;
    private MyPreferences mPref;
    private TurmaTO turmaTO;

    public TurmaQueries(Context ctx) {

        DBCore auxDB = new DBCore(ctx);

        db = auxDB.getWritableDatabase();

        mPref = new MyPreferences(ctx);
    }

    public void inserirTurmas(JSONArray jsonTurmas, int cd_aluno) {

        turmaTO = new TurmaTO(jsonTurmas);

        ArrayList<Turma> listaTurmas = turmaTO.getTurmasFromJson(cd_aluno);

        ContentValues valores = new ContentValues();

        for (Turma turma:listaTurmas) {

            valores.clear();

            valores.put("cd_turma", turma.getCd_turma());
            valores.put("cd_aluno", turma.getCd_aluno());
            valores.put("ano_letivo", turma.getAno_letivo());
            valores.put("nome_turma", turma.getNome_turma());
            valores.put("nome_escola", turma.getNome_escola());
            valores.put("tipo_ensino", turma.getTipo_ensino());
            valores.put("situacao_aprovacao", turma.getSituacao_aprovacao());
            valores.put("situacao_matricula", turma.getSituacao_matricula());
            valores.put("dt_inicio_matricula", turma.getDt_inicio_matricula());
            valores.put("dt_fim_matricula", turma.getDt_fim_matricula());
            valores.put("cd_tipo_ensino", turma.getCd_tipo_ensino());
            valores.put("cd_matricula_aluno", turma.getCd_matricula_aluno());

            db.insertWithOnConflict("turma", null, valores, SQLiteDatabase.CONFLICT_REPLACE);
        }
        valores.clear();
    }

    public void inserirTurmasResponsavel(JSONArray jsonTurmas, int cd_aluno){

        turmaTO = new TurmaTO(jsonTurmas);

        ArrayList<Turma> listaTurmas = turmaTO.getTurmasFromJsonResponsavel(cd_aluno);

        ContentValues valores = new ContentValues();

        for (Turma turma:listaTurmas) {

            valores.clear();

            valores.put("cd_turma", turma.getCd_turma());
            valores.put("cd_aluno", turma.getCd_aluno());
            valores.put("ano_letivo", turma.getAno_letivo());
            valores.put("nome_turma", turma.getNome_turma());
            valores.put("nome_escola", turma.getNome_escola());
            valores.put("tipo_ensino", turma.getTipo_ensino());
            valores.put("situacao_aprovacao", turma.getSituacao_aprovacao());
            valores.put("situacao_matricula", turma.getSituacao_matricula());
            valores.put("dt_inicio_matricula", turma.getDt_inicio_matricula());
            valores.put("dt_fim_matricula", turma.getDt_fim_matricula());
            valores.put("cd_tipo_ensino", turma.getCd_tipo_ensino());
            valores.put("cd_matricula_aluno", turma.getCd_matricula_aluno());

            db.insertWithOnConflict("turma", null, valores, SQLiteDatabase.CONFLICT_REPLACE);
        }
        valores.clear();
    }

    public ArrayList<Turma> getTurmas(int cd_aluno) {

        ArrayList<Turma> turmas = null;

        Cursor cursor = db.rawQuery("SELECT cd_turma, " +
                "cd_aluno, " +
                "ano_letivo, " +
                "nome_turma, " +
                "nome_escola, " +
                "situacao_aprovacao, " +
                "situacao_matricula, " +
                "dt_inicio_matricula, " +
                "dt_fim_matricula, " +
                "tipo_ensino, " +
                "cd_tipo_ensino, " +
                "cd_matricula_aluno " +
                "FROM turma WHERE cd_aluno = " + cd_aluno, null);

        if(cursor.moveToFirst()) {

            turmas = new ArrayList<>(cursor.getCount());

            Turma turma;

            do {

                turma = new Turma();

                turma.setCd_turma(cursor.getInt(0));
                turma.setCd_aluno(cursor.getInt(1));
                turma.setAno_letivo(cursor.getInt(2));
                turma.setNome_turma(cursor.getString(3));
                turma.setNome_escola(cursor.getString(4));
                turma.setSituacao_aprovacao(cursor.getString(5));
                turma.setSituacao_matricula(cursor.getString(6));
                turma.setDt_inicio_matricula(cursor.getString(7));
                turma.setDt_fim_matricula(cursor.getString(8));
                turma.setTipo_ensino(cursor.getString(9));
                turma.setCd_tipo_ensino(cursor.getInt(10));
                turma.setCd_matricula_aluno(cursor.getLong(11));

                turmas.add(turma);
            }
            while (cursor.moveToNext());
        }

        cursor.close();

        return turmas;
    }

    public Turma getTurmaAtiva(int cd_aluno) {

        ArrayList<Turma> turmas = null;

        Cursor cursor = db.rawQuery("SELECT cd_turma, " +
                "cd_aluno, " +
                "ano_letivo, " +
                "nome_turma, " +
                "nome_escola, " +
                "situacao_aprovacao, " +
                "situacao_matricula, " +
                "dt_inicio_matricula, " +
                "dt_fim_matricula, " +
                "tipo_ensino, " +
                "cd_tipo_ensino, " +
                "cd_matricula_aluno " +
                "FROM turma " +
                "WHERE cd_aluno = " + cd_aluno +
                " AND situacao_matricula = ?;", new String[]{"Ativo"});

        if(cursor.moveToFirst()) {

            turmas = new ArrayList<>(cursor.getCount());

            Turma turma;

            do {

                turma = new Turma();

                turma.setCd_turma(cursor.getInt(0));
                turma.setCd_aluno(cursor.getInt(1));
                turma.setAno_letivo(cursor.getInt(2));
                turma.setNome_turma(cursor.getString(3));
                turma.setNome_escola(cursor.getString(4));
                turma.setSituacao_aprovacao(cursor.getString(5));
                turma.setSituacao_matricula(cursor.getString(6));
                turma.setDt_inicio_matricula(cursor.getString(7));
                turma.setDt_fim_matricula(cursor.getString(8));
                turma.setTipo_ensino(cursor.getString(9));
                turma.setCd_matricula_aluno(cursor.getLong(11));

                turmas.add(turma);
            }
            while (cursor.moveToNext());
        }
        else {

            cursor.close();

            return null;
        }

        cursor.close();

        Calendar cal = GregorianCalendar.getInstance();

        int anoAtual = cal.get(Calendar.YEAR);

        Turma turmaAtiva = new Turma();

        for (Turma turma : turmas) {

            if(turma.getAno_letivo() == anoAtual) {

                turmaAtiva = turma;

                break;
            }
        }

        if(turmaAtiva.getCd_turma() == 0) {

            return null;
        }
        else {

            return turmaAtiva;
        }
    }

    public Turma getTurmaAtivaRegular(int cd_aluno) {

        ArrayList<Turma> turmas = null;

        Cursor cursor = db.rawQuery("SELECT cd_turma, " +
                "cd_aluno, " +
                "ano_letivo, " +
                "nome_turma, " +
                "nome_escola, " +
                "situacao_aprovacao, " +
                "situacao_matricula, " +
                "dt_inicio_matricula, " +
                "dt_fim_matricula, " +
                "tipo_ensino, " +
                "cd_tipo_ensino, " +
                "cd_matricula_aluno " +
                "FROM turma " +
                "WHERE cd_aluno = " + cd_aluno +
                " AND date('now') >= dt_inicio_matricula " +
                "AND date('now') <= dt_fim_matricula " +
                "AND situacao_matricula = ?;", new String[]{"Ativo"});

        if(cursor.moveToFirst()) {

            turmas = new ArrayList<>(cursor.getCount());

            Turma turma;

            do {

                turma = new Turma();

                turma.setCd_turma(cursor.getInt(0));
                turma.setCd_aluno(cursor.getInt(1));
                turma.setAno_letivo(cursor.getInt(2));
                turma.setNome_turma(cursor.getString(3));
                turma.setNome_escola(cursor.getString(4));
                turma.setSituacao_aprovacao(cursor.getString(5));
                turma.setSituacao_matricula(cursor.getString(6));
                turma.setDt_inicio_matricula(cursor.getString(7));
                turma.setDt_fim_matricula(cursor.getString(8));
                turma.setTipo_ensino(cursor.getString(9));
                turma.setCd_tipo_ensino(cursor.getInt(10));
                turma.setCd_matricula_aluno(cursor.getLong(11));

                turmas.add(turma);
            }
            while (cursor.moveToNext());
        }
        else {

            cursor.close();

            return null;
        }

        cursor.close();

        Calendar cal = GregorianCalendar.getInstance();

        int anoAtual = cal.get(Calendar.YEAR);

        Turma turmaAtiva = new Turma();

        for (Turma turma : turmas) {

            if((turma.getAno_letivo() == anoAtual) && ((turma.getCd_tipo_ensino() == 2) || turma.getCd_tipo_ensino() == 14)) {

                turmaAtiva = turma;

                break;
            }
        }

        if(turmaAtiva.getCd_turma() == 0) {

            return null;
        }
        else {

            return turmaAtiva;
        }
    }

    public ArrayList<Turma> getTurmasAtivas(int cd_aluno) {

        ArrayList<Turma> turmas = null;

        Cursor cursor = db.rawQuery("SELECT cd_turma, " +
                "cd_aluno, " +
                "ano_letivo, " +
                "nome_turma, " +
                "nome_escola, " +
                "situacao_aprovacao, " +
                "situacao_matricula, " +
                "dt_inicio_matricula, " +
                "dt_fim_matricula, " +
                "tipo_ensino, " +
                "cd_tipo_ensino, " +
                "cd_matricula_aluno " +
                "FROM turma " +
                "WHERE cd_aluno = " + cd_aluno +
                " AND situacao_matricula = ?;", new String[]{"Ativo"});

        if(cursor.moveToFirst()) {

            turmas = new ArrayList<>(cursor.getCount());

            Turma turma;

            do {

                turma = new Turma();

                turma.setCd_turma(cursor.getInt(0));
                turma.setCd_aluno(cursor.getInt(1));
                turma.setAno_letivo(cursor.getInt(2));
                turma.setNome_turma(cursor.getString(3));
                turma.setNome_escola(cursor.getString(4));
                turma.setSituacao_aprovacao(cursor.getString(5));
                turma.setSituacao_matricula(cursor.getString(6));
                turma.setDt_inicio_matricula(cursor.getString(7));
                turma.setDt_fim_matricula(cursor.getString(8));
                turma.setTipo_ensino(cursor.getString(9));
                turma.setCd_tipo_ensino(cursor.getInt(10));
                turma.setCd_matricula_aluno(cursor.getLong(11));

                turmas.add(turma);
            }
            while (cursor.moveToNext());
        }
        else {

            cursor.close();

            return null;
        }

        cursor.close();

        Calendar cal = GregorianCalendar.getInstance();

        int anoAtual = cal.get(Calendar.YEAR);

        ArrayList<Turma> turmasAtivas = new ArrayList<>();

        for (Turma turma : turmas) {

            if(turma.getAno_letivo() == anoAtual && turma.getCd_turma() != 0 && turma.getSituacao_matricula() != "Inativo") {

                turmasAtivas.add(turma);
            }
        }

        if(turmas.size() == 0){

            return null;
        }
        else{

            return turmasAtivas;
        }
    }

    public Turma getTurma(int cd_turma) {

        Turma turma = new Turma();

        Cursor cursor = db.rawQuery("SELECT cd_turma, " +
                "cd_aluno, " +
                "ano_letivo, " +
                "nome_turma, " +
                "nome_escola, " +
                "situacao_aprovacao, " +
                "situacao_matricula, " +
                "dt_inicio_matricula, " +
                "dt_fim_matricula, " +
                "tipo_ensino, " +
                "cd_tipo_ensino, " +
                "cd_matricula_aluno " +
                "FROM turma WHERE cd_turma = " + cd_turma, null);

        if(cursor.moveToFirst()) {

            turma.setCd_turma(cursor.getInt(0));
            turma.setCd_aluno(cursor.getInt(1));
            turma.setAno_letivo(cursor.getInt(2));
            turma.setNome_turma(cursor.getString(3));
            turma.setNome_escola(cursor.getString(4));
            turma.setSituacao_aprovacao(cursor.getString(5));
            turma.setSituacao_matricula(cursor.getString(6));
            turma.setDt_inicio_matricula(cursor.getString(7));
            turma.setDt_fim_matricula(cursor.getString(8));
            turma.setTipo_ensino(cursor.getString(9));
            turma.setCd_tipo_ensino(cursor.getInt(10));
            turma.setCd_matricula_aluno(cursor.getLong(11));
        }

        cursor.close();

        return turma;
    }
}

