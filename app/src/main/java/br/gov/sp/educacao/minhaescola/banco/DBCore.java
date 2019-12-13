package br.gov.sp.educacao.minhaescola.banco;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

import br.gov.sp.educacao.minhaescola.R;

public class DBCore
        extends SQLiteOpenHelper {

    private static final String NOME_DB = "minhaEscolaDB";
    private static final int VERSAO_DB = 13;

    protected Context context;

    public DBCore(Context ctx) {

        super(ctx, NOME_DB, null, VERSAO_DB);

        this.context = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE aluno(" +
                "cd_aluno INTEGER NOT NULL," +
                "numero_cpf TEXT," +
                "numero_rg TEXT," +
                "digito_rg TEXT," +
                "nome TEXT," +
                "token TEXT," +
                "login TEXT," +
                "senha TEXT," +
                "uf_ra TEXT," +
                "numero_ra TEXT," +
                "digito_ra TEXT," +
                "email_aluno TEXT," +
                "municipio_nascimento TEXT," +
                "uf_nascimento TEXT," +
                "data_nascimento TEXT," +
                "nacionalidade TEXT," +
                "nome_mae TEXT," +
                "nome_pai TEXT," +
                "escola_centralizada INTEGER," +
                "idade INTEGER," +
                "responde_rematricula INTEGER," +
                "PRIMARY KEY (cd_aluno)" +
                ");");


        db.execSQL("CREATE TABLE endereco(" +
                "cd_endereco INTEGER NOT NULL," +
                "cd_aluno INTEGER NOT NULL," +
                "uf_estado TEXT," +
                "cidade TEXT," +
                "endereco TEXT," +
                "numero_endereco TEXT," +
                "complemento TEXT," +
                "numero_cep TEXT," +
                "tipo_logradouro INTEGER," +
                "localizacao_diferenciada INTEGER," +
                "bairro TEXT," +
                "latitude TEXT," +
                "longitude TEXT," +
                "latitude_indic TEXT," +
                "longitude_indic TEXT," +
                "envio_comprovante INTEGER," +
                "PRIMARY KEY (cd_endereco)," +
                "CONSTRAINT fg_aluno_endereco FOREIGN KEY (cd_aluno) REFERENCES aluno (cd_aluno)" +
                ");");

        db.execSQL("CREATE TABLE contato_aluno (" +
                "cd_contato_aluno INTEGER NOT NULL," +
                "cd_aluno INTEGER," +
                "codigo_fone_telefone INTEGER," +
                "validacao_sms INTEGER," +
                "codigo_tipo_telefone INTEGER," +
                "codigo_ddd TEXT," +
                "telefone_aluno TEXT," +
                "complemento_telefone TEXT," +
                "PRIMARY KEY (cd_contato_aluno) ," +
                "CONSTRAINT fg_aluno_contato FOREIGN KEY (cd_aluno) REFERENCES aluno (cd_aluno)" +
                ");");


        db.execSQL("CREATE TABLE rematricula (" +
                "cd_interesse_rematricula INTEGER NOT NULL," +
                "cd_aluno INTEGER," +
                "ano_letivo INTEGER," +
                "ano_letivo_rematricula INTEGER," +
                "interesse_continuidade INTEGER," +
                "interesse_novotec INTEGER," +
                "interesse_turno_integral INTEGER," +
                "interesse_espanhol INTEGER," +
                "interesse_noturno INTEGER," +
                "eixo_ensino_profissional_um INTEGER," +
                "eixo_ensino_profissional_dois INTEGER," +
                "eixo_ensino_profissional_tres INTEGER," +
                "aceito_termos_responsabilidade INTEGER," +
                "observacao_opcao_noturno INTEGER," +
                "PRIMARY KEY (cd_interesse_rematricula) ," +
                "CONSTRAINT fg_aluno_rematricula FOREIGN KEY (cd_aluno) REFERENCES aluno (cd_aluno)" +
                ");");

        db.execSQL("CREATE TABLE responsavel (" +
                "cd_responsavel INTEGER NOT NULL," +
                "token TEXT," +
                "login TEXT," +
                "senha TEXT," +
                "email TEXT," +
                "PRIMARY KEY (cd_responsavel) " +
                ");");

        db.execSQL("CREATE TABLE contato_responsavel (" +
                "cd_contato_responsavel INTEGER NOT NULL," +
                "cd_responsavel INTEGER," +
                "codigo_fone_telefone INTEGER," +
                "validacao_sms INTEGER," +
                "codigo_tipo_telefone INTEGER," +
                "codigo_ddd TEXT," +
                "telefone_responsavel TEXT," +
                "complemento_telefone TEXT," +
                "PRIMARY KEY (cd_contato_responsavel) ," +
                "CONSTRAINT fg_responsavel_contato FOREIGN KEY (cd_responsavel) REFERENCES responsavel (cd_responsavel)" +
                ");");


        db.execSQL("CREATE TABLE turma (" +
                "cd_turma INTEGER NOT NULL," +
                "cd_aluno INTEGER NOT NULL," +
                "tipo_ensino TEXT," +
                "ano_letivo INTEGER," +
                "nome_turma TEXT," +
                "nome_escola TEXT," +
                "situacao_aprovacao TEXT," +
                "situacao_matricula TEXT," +
                "dt_inicio_matricula TEXT," +
                "dt_fim_matricula TEXT," +
                "cd_tipo_ensino INTEGER," +
                "cd_matricula_aluno INTEGER," +
                "PRIMARY KEY (cd_turma, cd_aluno) ," +
                "CONSTRAINT fg_aluno_turma FOREIGN KEY (cd_aluno) REFERENCES aluno (cd_aluno)" +
                ");");

        db.execSQL("CREATE TABLE horario_aula (" +
                "cd_horario_aula INTEGER NOT NULL," +
                "cd_aluno INTEGER NOT NULL," +
                "cd_materia INTEGER NOT NULL," +
                "nome_materia TEXT," +
                "nome_professor TEXT," +
                "data_hora_inicio TEXT," +
                "data_hora_fim TEXT," +
                "nome_turma TEXT," +
                "dia_semana TEXT," +
                "PRIMARY KEY (cd_horario_aula) ," +
                "CONSTRAINT fg_aluno_turma FOREIGN KEY (cd_aluno) REFERENCES aluno (cd_aluno)" +
                ");");

        db.execSQL("CREATE TABLE escola (" +
                "cd_escola INTEGER NOT NULL," +
                "cd_aluno INTEGER NOT NULL," +
                "cd_unidade INTEGER," +
                "endereco_unidade TEXT," +
                "nome_escola TEXT," +
                "nome_abreviado_escola TEXT," +
                "email_escola TEXT," +
                "municipio TEXT," +
                "nome_diretor TEXT," +
                "PRIMARY KEY (cd_escola, cd_aluno)" +
                ");");

        db.execSQL("CREATE TABLE contato_escola (" +
                //"cd_contato_escola INTEGER NOT NULL ," +
                "cd_escola INTEGER NOT NULL," +
                "contato_escola TEXT," +
                "PRIMARY KEY (contato_escola)," +
                "CONSTRAINT fg_escola_contato_escola FOREIGN KEY (cd_escola) REFERENCES escola (cd_escola)" +
                ");");

        db.execSQL("CREATE TABLE bimestre (" +
                "cd_bimestre INTEGER NOT NULL," +
                "cd_aluno INTEGER," +
                "bimestre INTEGER," +
                "data_inicio TEXT," +
                "data_fim TEXT," +
                "PRIMARY KEY (cd_bimestre) ," +
                "CONSTRAINT fg_aluno_bimestre FOREIGN KEY (cd_aluno) REFERENCES aluno (cd_aluno)" +
                ");");

        db.execSQL("CREATE TABLE calendario (" +
                "cd_calendario INTEGER NOT NULL," +
                "cd_bimestre INTEGER," +
                "cod_aluno INTEGER," +
                "mes INTEGER," +
                "dia INTEGER," +
                "letivo INTEGER," +
                "nome_evento TEXT," +
                "descricao_evento TEXT," +
                "disciplina TEXT," +
                "PRIMARY KEY (cd_calendario) ," +
                "CONSTRAINT fg_bimestre_calendario FOREIGN KEY (cd_bimestre) REFERENCES bimestre (cd_bimestre)" +
                ");");

        db.execSQL("CREATE TABLE nota (" +
                "cd_nota INTEGER," +
                "cd_aluno INTEGER," +
                "bimestre INTEGER," +
                "cd_disciplina INTEGER," +
                "nome_disciplina TEXT," +
                "nota REAL," +
                "PRIMARY KEY (cd_nota) ," +
                "CONSTRAINT fg_aluno_nota FOREIGN KEY (cd_aluno) REFERENCES aluno (cd_aluno)" +
                ");");

        db.execSQL("CREATE TABLE frequencia (" +
                "cd_frequencia INTEGER NOT NULL," +
                "cd_aluno INTEGER," +
                "bimestre INTEGER," +
                "cd_disciplina INTEGER," +
                "nome_disciplina TEXT," +
                "falta INTEGER," +
                "ausencias_compensada INTEGER," +
                "porcentagem_faltas REAL,"+
                "PRIMARY KEY (cd_frequencia) ," +
                "CONSTRAINT fg_aluno_frequencia FOREIGN KEY (cd_aluno) REFERENCES aluno (cd_aluno)" +
                ");");

        db.execSQL("CREATE TABLE carteirinha (" +
                "cd_carteirinha INTEGER NOT NULL," +
                "cd_aluno INTEGER," +
                "rg TEXT," +
                "data_nascimento TEXT," +
                "nome TEXT," +
                "apelido TEXT," +
                "ra TEXT," +
                "nome_escola TEXT," +
                "validade TEXT," +
                "nome_turma TEXT," +
                "qr_criptografado TEXT," +
                "foto_qr TEXT," +
                "foto_aluno TEXT," +
                "PRIMARY KEY (cd_carteirinha) ," +
                "CONSTRAINT fg_aluno_carteirinha FOREIGN KEY (cd_aluno) REFERENCES aluno (cd_aluno)" +
                ");");

        db.execSQL("CREATE TABLE composicao_nota (" +
                "cd_composicao_nota INTEGER NOT NULL," +
                "cd_nota INTEGER," +
                "notas INTEGER," +
                "descricao TEXT," +
                "PRIMARY KEY (cd_composicao_nota) ," +
                "CONSTRAINT fg_nota_composicao FOREIGN KEY (cd_nota) REFERENCES nota (cd_nota)" +
                ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS turmasImagens(" +
                "codigoMateria INTEGER PRIMARY KEY NOT NULL," +
                "idImagem INTEGER" +
                ");");

        db.execSQL("CREATE TABLE municipios (" +
                "codigo_dne INTEGER NOT NULL," +
                "municipio TEXT," +
                "PRIMARY KEY (codigo_dne) " +
                ");");

        populaTurmasImagem(db);

        popularMunicipiosNoBanco(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        if(VERSAO_DB == 10 || VERSAO_DB == 11 || VERSAO_DB == 12 || VERSAO_DB == 13) {

            List<String> tables = new ArrayList<String>();

            Cursor cursor = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table';", null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                String tableName = cursor.getString(1);

                if (!tableName.equals("android_metadata") && !tableName.equals("sqlite_sequence")){

                    tables.add(tableName);
                }

                cursor.moveToNext();
            }

            cursor.close();

            for(String tableName:tables) {

                db.execSQL("DROP TABLE IF EXISTS " + tableName);
            }

            onCreate(db);
        }
        else {

            onCreate(db);
        }
    }

    protected void byFile(int fileID, SQLiteDatabase bd) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(context.getResources()
                                                                            .openRawResource(fileID), "UTF-8"));
        StringBuilder sql = new StringBuilder();

        String line;

        while ((line = br.readLine()) != null) {

            line = line.trim();

            if (line.length() > 0) {

                sql.append(line);

                if (line.endsWith(";")) {

                    bd.execSQL(sql.toString());

                    sql.delete(0, sql.length());
                }
            }
        }
    }

    public ArrayList<Cursor> getData(String Query) {

        SQLiteDatabase sqlDB = this.getWritableDatabase();

        String[] columns = new String[] { "mesage" };

        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);

        MatrixCursor Cursor2= new MatrixCursor(columns);

        alc.add(null);
        alc.add(null);

        try {

            String maxQuery = Query ;

            Cursor c = sqlDB.rawQuery(maxQuery, null);

            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);

            if (null != c && c.getCount() > 0) {

                alc.set(0,c);

                c.moveToFirst();

                return alc ;
            }
            return alc;
        }
        catch(Exception ex) {

            Cursor2.addRow(new Object[] {"" + ex.getMessage()});

            alc.set(1,Cursor2);

            return alc;
        }
    }

    public void populaTurmasImagem(SQLiteDatabase db) {

        List<Integer> codigosMateria = new ArrayList<>();

        //region ---- ADICIONANDO CODIGOS ----
        codigosMateria.add(1100);
        codigosMateria.add(1111);
        codigosMateria.add(1118);
        codigosMateria.add(1119);
        codigosMateria.add(1130);
        codigosMateria.add(1131);
        codigosMateria.add(1132);
        codigosMateria.add(1200);
        codigosMateria.add(1201);
        codigosMateria.add(1202);
        codigosMateria.add(1300);
        codigosMateria.add(1400);
        codigosMateria.add(1401);
        codigosMateria.add(1407);
        codigosMateria.add(1408);
        codigosMateria.add(1500);
        codigosMateria.add(1800);
        codigosMateria.add(1813);
        codigosMateria.add(1814);
        codigosMateria.add(1816);
        codigosMateria.add(1900);
        codigosMateria.add(1903);
        codigosMateria.add(1905);
        codigosMateria.add(1908);
        codigosMateria.add(2000);
        codigosMateria.add(2100);
        codigosMateria.add(2105);
        codigosMateria.add(2112);
        codigosMateria.add(2200);
        codigosMateria.add(2208);
        codigosMateria.add(2300);
        codigosMateria.add(2306);
        codigosMateria.add(2308);
        codigosMateria.add(2309);
        codigosMateria.add(2400);
        codigosMateria.add(2413);
        codigosMateria.add(2426);
        codigosMateria.add(2500);
        codigosMateria.add(2504);
        codigosMateria.add(2600);
        codigosMateria.add(2605);
        codigosMateria.add(2607);
        codigosMateria.add(2700);
        codigosMateria.add(2707);
        codigosMateria.add(2713);
        codigosMateria.add(2800);
        codigosMateria.add(2812);
        codigosMateria.add(2831);
        codigosMateria.add(2832);
        codigosMateria.add(3100);
        codigosMateria.add(3105);
        codigosMateria.add(3107);
        codigosMateria.add(3108);
        codigosMateria.add(3200);
        codigosMateria.add(5100);
        codigosMateria.add(6900);
        codigosMateria.add(7000);
        codigosMateria.add(7235);
        codigosMateria.add(7240);
        codigosMateria.add(7245);
        codigosMateria.add(8001);
        codigosMateria.add(8002);
        codigosMateria.add(8003);
        codigosMateria.add(8004);
        codigosMateria.add(8005);
        codigosMateria.add(8006);
        codigosMateria.add(8007);
        codigosMateria.add(8008);
        codigosMateria.add(8009);
        codigosMateria.add(8010);
        codigosMateria.add(8011);
        codigosMateria.add(8012);
        codigosMateria.add(8013);
        codigosMateria.add(8014);
        codigosMateria.add(8015);
        codigosMateria.add(8016);
        codigosMateria.add(8017);
        codigosMateria.add(8018);
        codigosMateria.add(8019);
        codigosMateria.add(8020);
        codigosMateria.add(8021);
        codigosMateria.add(8022);
        codigosMateria.add(8023);
        codigosMateria.add(8024);
        codigosMateria.add(8025);
        codigosMateria.add(8026);
        codigosMateria.add(8027);
        codigosMateria.add(8028);
        codigosMateria.add(8029);
        codigosMateria.add(8030);
        codigosMateria.add(8031);
        codigosMateria.add(8032);
        codigosMateria.add(8033);
        codigosMateria.add(8034);
        codigosMateria.add(8035);
        codigosMateria.add(8036);
        codigosMateria.add(8037);
        codigosMateria.add(8038);
        codigosMateria.add(8039);
        codigosMateria.add(8040);
        codigosMateria.add(8041);
        codigosMateria.add(8042);
        codigosMateria.add(8043);
        codigosMateria.add(8044);
        codigosMateria.add(8045);
        codigosMateria.add(8046);
        codigosMateria.add(8047);
        codigosMateria.add(8048);
        codigosMateria.add(8049);
        codigosMateria.add(8050);
        codigosMateria.add(8051);
        codigosMateria.add(8052);
        codigosMateria.add(8053);
        codigosMateria.add(8054);
        codigosMateria.add(8055);
        codigosMateria.add(8056);
        codigosMateria.add(8057);
        codigosMateria.add(8058);
        codigosMateria.add(8059);
        codigosMateria.add(8060);
        codigosMateria.add(8061);
        codigosMateria.add(8062);
        codigosMateria.add(8063);
        codigosMateria.add(8064);
        codigosMateria.add(8065);
        codigosMateria.add(8066);
        codigosMateria.add(8067);
        codigosMateria.add(8068);
        codigosMateria.add(8069);
        codigosMateria.add(8070);
        codigosMateria.add(8071);
        codigosMateria.add(8072);
        codigosMateria.add(8073);
        codigosMateria.add(8074);
        codigosMateria.add(8075);
        codigosMateria.add(8076);
        codigosMateria.add(8077);
        codigosMateria.add(8078);
        codigosMateria.add(8079);
        codigosMateria.add(8080);
        codigosMateria.add(8081);
        codigosMateria.add(8082);
        codigosMateria.add(8083);
        codigosMateria.add(8084);
        codigosMateria.add(8085);
        codigosMateria.add(8086);
        codigosMateria.add(8087);
        codigosMateria.add(8088);
        codigosMateria.add(8089);
        codigosMateria.add(8090);
        codigosMateria.add(8091);
        codigosMateria.add(8092);
        codigosMateria.add(8093);
        codigosMateria.add(8094);
        codigosMateria.add(8095);
        codigosMateria.add(8096);
        codigosMateria.add(8097);
        codigosMateria.add(8098);
        codigosMateria.add(8099);
        codigosMateria.add(8100);
        codigosMateria.add(8101);
        codigosMateria.add(8102);
        codigosMateria.add(8103);
        codigosMateria.add(8104);
        codigosMateria.add(8105);
        codigosMateria.add(8106);
        codigosMateria.add(8107);
        codigosMateria.add(8108);
        codigosMateria.add(8109);
        codigosMateria.add(8110);
        codigosMateria.add(8111);
        codigosMateria.add(8112);
        codigosMateria.add(8113);
        codigosMateria.add(8114);
        codigosMateria.add(8115);
        codigosMateria.add(8116);
        codigosMateria.add(8117);
        codigosMateria.add(8118);
        codigosMateria.add(8119);
        codigosMateria.add(8120);
        codigosMateria.add(8121);
        codigosMateria.add(8122);
        codigosMateria.add(8123);
        codigosMateria.add(8124);
        codigosMateria.add(8125);
        codigosMateria.add(8126);
        codigosMateria.add(8127);
        codigosMateria.add(8128);
        codigosMateria.add(8129);
        codigosMateria.add(8130);
        codigosMateria.add(8131);
        codigosMateria.add(8132);
        codigosMateria.add(8133);
        codigosMateria.add(8134);
        codigosMateria.add(8135);
        codigosMateria.add(8136);
        codigosMateria.add(8137);
        codigosMateria.add(8138);
        codigosMateria.add(8139);
        codigosMateria.add(8140);
        codigosMateria.add(8141);
        codigosMateria.add(8142);
        codigosMateria.add(8143);
        codigosMateria.add(8144);
        codigosMateria.add(8145);
        codigosMateria.add(8146);
        codigosMateria.add(8147);
        codigosMateria.add(8148);
        codigosMateria.add(8149);
        codigosMateria.add(8150);
        codigosMateria.add(8151);
        codigosMateria.add(8152);
        codigosMateria.add(8153);
        codigosMateria.add(8154);
        codigosMateria.add(8155);
        codigosMateria.add(8156);
        codigosMateria.add(8157);
        codigosMateria.add(8158);
        codigosMateria.add(8159);
        codigosMateria.add(8160);
        codigosMateria.add(8161);
        codigosMateria.add(8162);
        codigosMateria.add(8163);
        codigosMateria.add(8164);
        codigosMateria.add(8165);
        codigosMateria.add(8166);
        codigosMateria.add(8167);
        codigosMateria.add(8168);
        codigosMateria.add(8169);
        codigosMateria.add(8170);
        codigosMateria.add(8171);
        codigosMateria.add(8172);
        codigosMateria.add(8173);
        codigosMateria.add(8174);
        codigosMateria.add(8175);
        codigosMateria.add(8176);
        codigosMateria.add(8177);
        codigosMateria.add(8178);
        codigosMateria.add(8179);
        codigosMateria.add(8180);
        codigosMateria.add(8181);
        codigosMateria.add(8182);
        codigosMateria.add(8183);
        codigosMateria.add(8184);
        codigosMateria.add(8185);
        codigosMateria.add(8186);
        codigosMateria.add(8187);
        codigosMateria.add(8188);
        codigosMateria.add(8189);
        codigosMateria.add(8190);
        codigosMateria.add(8191);
        codigosMateria.add(8192);
        codigosMateria.add(8193);
        codigosMateria.add(8194);
        codigosMateria.add(8195);
        codigosMateria.add(8196);
        codigosMateria.add(8197);
        codigosMateria.add(8198);
        codigosMateria.add(8199);
        codigosMateria.add(8200);
        codigosMateria.add(8201);
        codigosMateria.add(8202);
        codigosMateria.add(8203);
        codigosMateria.add(8204);
        codigosMateria.add(8205);
        codigosMateria.add(8206);
        codigosMateria.add(8207);
        codigosMateria.add(8208);
        codigosMateria.add(8209);
        codigosMateria.add(8210);
        codigosMateria.add(8211);
        codigosMateria.add(8212);
        codigosMateria.add(8213);
        codigosMateria.add(8214);
        codigosMateria.add(8215);
        codigosMateria.add(8216);
        codigosMateria.add(8217);
        codigosMateria.add(8218);
        codigosMateria.add(8222);
        codigosMateria.add(8223);
        codigosMateria.add(8224);
        codigosMateria.add(8225);
        //endregion

        //region ---- ASSOCIAR IMAGENS AS MATÉRIAS ----

        for (Integer codigo : codigosMateria) {

            if((codigo >= 1100 && codigo <=1500) || codigo == 2000 || codigo == 6900 || codigo == 7000) {

                //imagem 1100
                int idImg = R.drawable.materia1100;
                db.execSQL("INSERT INTO turmasImagens(codigoMateria, idImagem) VALUES (" + codigo + ", " + idImg + ");");
            }
            else if(codigo >= 1800 && codigo <= 1816) {

                //imagem 1800
                int idImg = R.drawable.materia1800;
                db.execSQL("INSERT INTO turmasImagens(codigoMateria, idImagem) VALUES (" + codigo + ", " + idImg + ");");
            }
            else if((codigo >= 1900 && codigo <= 1908) || (codigo >= 8001 && codigo <= 8225)) {

                //1900
                int idImg = R.drawable.materia1900;
                db.execSQL("INSERT INTO turmasImagens(codigoMateria, idImagem) VALUES (" + codigo + ", " + idImg + ");");
            }
            else if((codigo >= 2100 && codigo <= 2112) || codigo == 5100) {

                //2100
                int idImg = R.drawable.materia2100;
                db.execSQL("INSERT INTO turmasImagens(codigoMateria, idImagem) VALUES (" + codigo + ", " + idImg + ");");
            }
            else if(codigo == 2200 || codigo == 2208) {

                //2200
                int idImg = R.drawable.materia2200;
                db.execSQL("INSERT INTO turmasImagens(codigoMateria, idImagem) VALUES (" + codigo + ", " + idImg + ");");
            }
            else if((codigo >= 2300 && codigo <= 2309) || codigo == 3200) {

                //2300
                int idImg = R.drawable.materia2300;
                db.execSQL("INSERT INTO turmasImagens(codigoMateria, idImagem) VALUES (" + codigo + ", " + idImg + ");");
            }
            else if(codigo == 2400 || codigo == 2413 || codigo == 2426 ) {

                //2400
                int idImg = R.drawable.materia2400;
                db.execSQL("INSERT INTO turmasImagens(codigoMateria, idImagem) VALUES (" + codigo + ", " + idImg + ");");
            }
            else if(codigo == 2500 || codigo == 2504 || codigo == 7240 || codigo == 7245 ){

                //2500
                int idImg = R.drawable.materia2500;
                db.execSQL("INSERT INTO turmasImagens(codigoMateria, idImagem) VALUES (" + codigo + ", " + idImg + ");");
            }
            else if(codigo == 2600 || codigo == 2605 || codigo == 2607 ) {

                //2600
                int idImg = R.drawable.materia2600;
                db.execSQL("INSERT INTO turmasImagens(codigoMateria, idImagem) VALUES (" + codigo + ", " + idImg + ");");
            }
            else if(codigo == 2700 || codigo == 2707 || codigo == 2713 || codigo == 7235) {

                //2700
                int idImg = R.drawable.materia2700;
                db.execSQL("INSERT INTO turmasImagens(codigoMateria, idImagem) VALUES (" + codigo + ", " + idImg + ");");
            }
            else if(codigo == 2800 || codigo == 2812 || codigo == 2831 || codigo == 2832) {

                //2800
                int idImg = R.drawable.materia2800;
                db.execSQL("INSERT INTO turmasImagens(codigoMateria, idImagem) VALUES (" + codigo + ", " + idImg + ");");
            }
            else if(codigo == 3100 || codigo == 3105 || codigo == 3107 || codigo == 3108) {

                //3100
                int idImg = R.drawable.materia3100;
                db.execSQL("INSERT INTO turmasImagens(codigoMateria, idImagem) VALUES (" + codigo + ", " + idImg + ");");
            }
            else {

                //padrão
                int idImg = R.drawable.materia_default;
                db.execSQL("INSERT INTO turmasImagens(codigoMateria, idImagem) VALUES (" + codigo + ", " + idImg + ");");
            }
        }

        //endregion

    }

    public void popularMunicipiosNoBanco(SQLiteDatabase db) {

        String json = null;

        try {

            InputStream is = context.getAssets().open("json_municipios.json");

            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex) {

            ex.printStackTrace();
            return ;
        }

        try {

            JSONArray jsonArray = new JSONArray(json);

            ContentValues valores = new ContentValues();

            for (int i = 0; i < jsonArray.length(); i++) {

                int codigo_dne = jsonArray.getJSONObject(i).getInt("CODIGODNE");
                String municipio = jsonArray.getJSONObject(i).getString("MUNICIPIO");

                valores.clear();

                valores.put("codigo_dne", codigo_dne);
                valores.put("municipio", municipio);

                db.insertWithOnConflict("municipios", null, valores, SQLiteDatabase.CONFLICT_REPLACE);

            }

        }
        catch (Exception e){

            e.printStackTrace();
            return ;
        }
    }
}
