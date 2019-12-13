package br.gov.sp.educacao.minhaescola.banco;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import br.gov.sp.educacao.minhaescola.R;
import br.gov.sp.educacao.minhaescola.model.HorarioAula;

public class HorarioAulaQueries {

    private SQLiteDatabase db;

    public HorarioAulaQueries(Context ctx) {

        DBCore auxDB = new DBCore(ctx);

        db = auxDB.getWritableDatabase();
    }

    public void inserirHorariosAulas(ArrayList<HorarioAula> horarios) {

        ContentValues valores = new ContentValues();

        for(HorarioAula horario : horarios) {

            if(!temHorario(horario)) {

                valores.clear();

                valores.put("cd_aluno", horario.getCd_aluno());
                valores.put("cd_materia", horario.getCd_materia());
                valores.put("nome_materia", horario.getNome_materia());
                valores.put("nome_professor", horario.getNome_professor());
                valores.put("data_hora_inicio", horario.getData_hora_inicio());
                valores.put("data_hora_fim", horario.getData_hora_fim());
                valores.put("nome_turma", horario.getNome_turma());
                valores.put("dia_semana", horario.getDia_semana());

                db.insertWithOnConflict("horario_aula", null, valores, SQLiteDatabase.CONFLICT_REPLACE);
            }
        }
    }

    public boolean temHorarioBanco(int cd_aluno) {

        Cursor cursor = db.rawQuery("SELECT cd_horario_aula " +
                                         "FROM horario_aula " +
                                         "WHERE cd_aluno = " + cd_aluno, null);

        boolean temHorarioBanco = false;

        if(cursor.moveToFirst()) {

            temHorarioBanco = true;
        }
        cursor.close();

        return temHorarioBanco;
    }

    public ArrayList<HorarioAula> getHorariosAula(int cd_aluno){

        ArrayList<HorarioAula> horarios = null;

        Cursor cursor = db.rawQuery("SELECT cd_horario_aula, " +
                                         "cd_aluno, " +
                                         "cd_materia, " +
                                         "nome_materia, " +
                                         "nome_professor, " +
                                         "data_hora_inicio, " +
                                         "data_hora_fim, " +
                                         "nome_turma, " +
                                         "dia_semana FROM horario_aula WHERE cd_aluno = " + cd_aluno, null);

        if(cursor.moveToFirst()) {

            horarios = new ArrayList<>(cursor.getCount());

            HorarioAula horario;

            do {

                horario = new HorarioAula();

                horario.setCd_horario_aula(cursor.getInt(0));
                horario.setCd_aluno(cursor.getInt(1));
                horario.setCd_materia(cursor.getInt(2));
                horario.setNome_materia(cursor.getString(3));
                horario.setNome_professor(cursor.getString(4));
                horario.setData_hora_inicio(cursor.getString(5));
                horario.setData_hora_fim(cursor.getString(6));
                horario.setNome_turma(cursor.getString(7));
                horario.setDia_semana(cursor.getString(8));

                horarios.add(horario);

            }
            while (cursor.moveToNext());
        }

        cursor.close();

        return horarios;
    }

    public int getIdImagemMateria(int codigoMateria) {

        Cursor cursor = db.rawQuery("SELECT idImagem FROM turmasImagens WHERE codigoMateria = " + codigoMateria, null);

        if(cursor.moveToFirst()) {

            int id = cursor.getInt(0);

            cursor.close();

            return id;
        }
        else {

            return R.drawable.materia_default;
        }
    }

    public boolean temHorario(HorarioAula horario) {

        Cursor cursor = db.rawQuery("SELECT cd_horario_aula " +
                                         "FROM horario_aula " +
                                         "WHERE cd_aluno = " + horario.getCd_aluno() +
                                         " AND cd_materia = '" + horario.getCd_materia() + "'" +
                                         " AND data_hora_inicio = '" + horario.getData_hora_inicio() + "'" +
                                         " AND dia_semana = '" + horario.getDia_semana() + "'", null);

        boolean temHorario = false;

        if(cursor.moveToFirst()) {

            temHorario = true;
        }
        cursor.close();

        return temHorario;
    }
}
