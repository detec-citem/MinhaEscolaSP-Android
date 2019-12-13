package br.gov.sp.educacao.minhaescola.banco;import android.content.ContentValues;import android.content.Context;import android.database.Cursor;import android.database.sqlite.SQLiteDatabase;import java.util.ArrayList;import java.util.Calendar;import java.util.HashMap;import java.util.List;import java.util.Map;import br.gov.sp.educacao.minhaescola.model.DiasEventos;public class CalendarioQueries {    private SQLiteDatabase db;    public CalendarioQueries(Context ctx) {        DBCore auxDB = new DBCore(ctx);        db = auxDB.getWritableDatabase();    }    public void inserirBimestres(int codigo, String numBimestre, String inicio, String fim) {        if (!temCalendario(codigo)) {            ContentValues valores = new ContentValues();            valores.put("cd_aluno", codigo);            valores.put("bimestre", numBimestre);            valores.put("data_inicio", removeZeroNasDatas(inicio));            valores.put("data_fim", removeZeroNasDatas(fim));            db.insert("bimestre", null, valores);        }    }    private String removeZeroNasDatas(String data) {        String[] dataSeparada = data.split("[/]");        if (dataSeparada[0].indexOf('0') == 0) {            dataSeparada[0] = dataSeparada[0].replace('0', ' ').trim();        }        if (dataSeparada[1].indexOf('0') == 0) {            dataSeparada[1] = dataSeparada[1].replace('0', ' ').trim();        }        return dataSeparada[0] + "/" + dataSeparada[1] + "/" + dataSeparada[2];    }    public void inserirCalendarioTurma(Map<Integer, List<DiasEventos>> calendario, int cd_aluno) {        db.delete("calendario", null, null);        for (int i = 1; i <= 12; i++) {            if (calendario.containsKey(i)) {                List<DiasEventos> listaDiasEventos = new ArrayList<>();                listaDiasEventos.addAll(calendario.get(i));                for (DiasEventos diasEventos : listaDiasEventos) {                    ContentValues valores = new ContentValues();                    valores.put("cd_bimestre", diasEventos.getBimestre());                    valores.put("cod_aluno", cd_aluno);                    valores.put("mes", diasEventos.getMes());                    valores.put("dia", diasEventos.getDia());                    valores.put("letivo", diasEventos.isLetivo());                    valores.put("nome_evento", diasEventos.getNomeEvento());                    valores.put("descricao_evento", diasEventos.getDescricaoEvento());                    valores.put("disciplina", diasEventos.getNomeDisciplina());                    db.insert("calendario", null, valores);                }            }        }    }    private boolean temCalendario(int aluno) {        boolean temNota = false;        Cursor cursor = db.rawQuery("SELECT calendario.cd_bimestre," +                        "mes," +                        "dia," +                        "letivo," +                        "nome_evento," +                        "descricao_evento," +                        "disciplina " +                        "FROM calendario, " +                        "bimestre " +                        "WHERE calendario.cod_aluno = " +                        "bimestre.cd_aluno AND cd_aluno =" + "'" + aluno + "'",                null);        if (cursor.moveToFirst()) {            temNota = true;        }        cursor.close();        return temNota;    }    public boolean temCalendarioBanco(int cd_aluno) {        Cursor cursor = db.rawQuery("SELECT calendario.cd_bimestre," +                        "mes," +                        "dia," +                        "letivo," +                        "nome_evento," +                        "descricao_evento," +                        "disciplina " +                        "FROM calendario, " +                        "bimestre " +                        "WHERE calendario.cod_aluno = " +                        "bimestre.cd_aluno AND cd_aluno =" + "'" + cd_aluno + "'",                null);        boolean temCalendario = false;        if (cursor.moveToFirst()) {            temCalendario = true;        }        cursor.close();        return temCalendario;    }    public List<String> getDiasFerias(int cd_aluno) {        List<String> listaDiasFerias = new ArrayList<>();        Cursor cursor = db.rawQuery("SELECT bimestre," +                "data_inicio," +                "data_fim " +                "from bimestre " +                "WHERE cd_aluno =  " + "'" + cd_aluno + "'" +                "ORDER BY bimestre", null);        while (cursor.moveToNext()) {            listaDiasFerias.add(cursor.getString(1));            listaDiasFerias.add(cursor.getString(2));        }        cursor.close();        return listaDiasFerias;    }    public Map<String, List<DiasEventos>> getDiasEventos(int cd_aluno) {        Map<String, List<DiasEventos>> mapaDiasEventos = new HashMap<>();        Cursor cursor = db.rawQuery("SELECT " +                "t1.cd_bimestre, " +                "mes, " +                "dia, " +                "letivo, " +                "nome_evento, " +                "descricao_evento," +                "disciplina " +                "FROM calendario t1 " +                "INNER JOIN bimestre t2 on " +                "(t1.cod_aluno = t2.cd_aluno AND t1.cd_bimestre = t2.bimestre)" +                "WHERE cd_aluno = '" + cd_aluno + "'", null);        while (cursor.moveToNext()) {            DiasEventos diasEventos = new DiasEventos();            List<DiasEventos> listaDiasEventos = new ArrayList<>();            diasEventos.setBimestre(cursor.getString(0));            diasEventos.setMes(cursor.getString(1));            diasEventos.setDia(cursor.getString(2));            diasEventos.setLetivo(cursor.getInt(3) == 1);            diasEventos.setNomeEvento(cursor.getString(4));            diasEventos.setDescricaoEvento(cursor.getString(5));            diasEventos.setNomeDisciplina(cursor.getString(6));            listaDiasEventos.add(diasEventos);            final int anoAtual = Calendar.getInstance().get(Calendar.YEAR);            if (!mapaDiasEventos.containsKey(diasEventos.getDia() + "/" + diasEventos.getMes() + "/" + anoAtual)) {                mapaDiasEventos.put(diasEventos.getDia() + "/" + diasEventos.getMes() + "/" + anoAtual, listaDiasEventos);            } else {                listaDiasEventos.addAll(mapaDiasEventos.get(diasEventos.getDia() + "/" + diasEventos.getMes() + "/" + anoAtual));                mapaDiasEventos.put(diasEventos.getDia() + "/" + diasEventos.getMes() + "/" + anoAtual, listaDiasEventos);            }        }        cursor.close();        return mapaDiasEventos;    }}