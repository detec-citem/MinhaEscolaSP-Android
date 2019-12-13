package br.gov.sp.educacao.minhaescola.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.gov.sp.educacao.minhaescola.model.Aluno;
import br.gov.sp.educacao.minhaescola.model.ContatoAluno;
import br.gov.sp.educacao.minhaescola.model.ContatoResponsavel;
import br.gov.sp.educacao.minhaescola.model.Endereco;
import br.gov.sp.educacao.minhaescola.model.Rematricula;
import br.gov.sp.educacao.minhaescola.model.Responsavel;
import br.gov.sp.educacao.minhaescola.modelTO.AlunoTO;
import br.gov.sp.educacao.minhaescola.modelTO.ContatoAlunoTO;
import br.gov.sp.educacao.minhaescola.modelTO.EnderecoTO;
import br.gov.sp.educacao.minhaescola.modelTO.RematriculaTO;
import br.gov.sp.educacao.minhaescola.modelTO.ResponsavelTO;
import br.gov.sp.educacao.minhaescola.util.MyPreferences;

public class UsuarioQueries {

    private SQLiteDatabase db;
    private MyPreferences mPref;
    private TurmaQueries turmaQueries;
    private EscolaQueries escolaQueries;

    public UsuarioQueries(Context ctx) {

        DBCore auxDB = new DBCore(ctx);

        db = auxDB.getWritableDatabase();

        mPref = new MyPreferences(ctx);

        turmaQueries = new TurmaQueries(ctx);

        escolaQueries = new EscolaQueries(ctx);
    }

    //region ---- INSERT ----
    public int inserirAluno(JSONObject alunoJson, String login, String senha) {

        /*
        RETORNO:

          1 = Sucesso
         -1 = Não encontrou perfil
         -2 = Falha ao salvar no banco

        */

        // Verifica se o usuário tem o perfil de aluno
        try {

            JSONArray arrayPerfil = alunoJson.getJSONArray("Perfis");

            boolean achouPerfil = false;

            for (int i = 0; i < arrayPerfil.length(); i++) {

                if (arrayPerfil.getJSONObject(i).getInt("Codigo") == MyPreferences.PERFIL_ALUNO) {

                    achouPerfil = true;
                }
            }
            if (!achouPerfil) {

                return -1;
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }

        //Converte o JSONObject para o objeto aluno.
        AlunoTO alunoTO = new AlunoTO(alunoJson, login, senha);

        Aluno aluno = alunoTO.getAlunoFromJson();

        if (aluno == null) {

            return -2;
        }

        //Insere o aluno no banco
        ContentValues valoresAluno = new ContentValues();

        valoresAluno.put("cd_aluno", aluno.getCd_aluno());
        valoresAluno.put("token", aluno.getToken());
        valoresAluno.put("nome", aluno.getNome());
        valoresAluno.put("login", aluno.getLogin());
        valoresAluno.put("senha", aluno.getSenha());
        valoresAluno.put("numero_ra", aluno.getNumero_ra());
        valoresAluno.put("digito_ra", aluno.getDigito_ra());
        valoresAluno.put("email_aluno", aluno.getEmail_aluno());
        valoresAluno.put("numero_rg", aluno.getNumero_rg());
        valoresAluno.put("digito_rg", aluno.getDigito_rg());
        valoresAluno.put("numero_cpf", aluno.getNumero_cpf());
        valoresAluno.put("uf_ra", aluno.getUf_ra());
        valoresAluno.put("data_nascimento", aluno.getData_nascimento());
        valoresAluno.put("uf_nascimento", aluno.getUf_nascimento());
        valoresAluno.put("municipio_nascimento", aluno.getMunicipio_nascimento());
        valoresAluno.put("nacionalidade", aluno.getNacionalidade());
        valoresAluno.put("nome_mae", aluno.getNomeMae());
        valoresAluno.put("nome_pai", aluno.getNomePai());
        valoresAluno.put("idade", aluno.getIdade());
        valoresAluno.put("responde_rematricula", aluno.isResponde_rematricula());

        if (aluno.isEscola_centralizada()) {

            valoresAluno.put("escola_centralizada", 1);
        } else {

            valoresAluno.put("escola_centralizada", 1);
        }

        db.insertWithOnConflict("aluno", null, valoresAluno, SQLiteDatabase.CONFLICT_IGNORE);

        //inserir endereco no banco
        EnderecoTO enderecoTO = new EnderecoTO(alunoJson, aluno.getCd_aluno());

        Endereco endereco = enderecoTO.getEnderecoFromJson();

        inserirEnderecoAluno(endereco);

        //inserir as turmas no banco
        try {

            JSONArray jsonTurmas = alunoJson.getJSONArray("Turmas");

            turmaQueries.inserirTurmas(jsonTurmas, aluno.getCd_aluno());
        } catch (JSONException e) {

            e.printStackTrace();

            return -2;
        }

        //inserir as escolas no banco
        try {

            JSONArray jsonEscolas = alunoJson.getJSONArray("Escolas");

            escolaQueries.inserirEscolas(jsonEscolas, aluno.getCd_aluno());

        } catch (JSONException e) {

            e.printStackTrace();

            return -2;
        }

        //inserir Contato Aluno
        try {

            JSONArray jsonContato = alunoJson.getJSONArray("ContatoAluno");

            ContatoAlunoTO contatoAlunoTO = new ContatoAlunoTO(jsonContato);

            inserirContatosAlunos(contatoAlunoTO.getContatoAlunoFromJson());

        } catch (Exception e) {

            e.printStackTrace();
        }

        try{

            if(alunoJson.getJSONArray("InteresseRematricula").length() == 1){

                RematriculaTO rematriculaTO = new RematriculaTO(aluno.getCd_aluno(),
                        alunoJson.getJSONArray("InteresseRematricula").getJSONObject(0));

                inserirRematricula(rematriculaTO.getRematriculaFromJson());
            }
        }
        catch (Exception e){

            e.printStackTrace();
        }


        return 1;
    }

    public void inserirEnderecoAluno(Endereco endereco) {

        ContentValues valoresEndereco = new ContentValues();

        valoresEndereco.put("cd_aluno", endereco.getCd_aluno());
        valoresEndereco.put("cidade", endereco.getCidade());
        valoresEndereco.put("endereco", endereco.getEndereco());
        valoresEndereco.put("numero_endereco", endereco.getNumero_endereco());
        valoresEndereco.put("complemento", endereco.getComplemento());
        valoresEndereco.put("numero_cep", endereco.getNumero_cep());
        valoresEndereco.put("tipo_logradouro", endereco.getTipo_logradouro());
        valoresEndereco.put("localizacao_diferenciada", endereco.getLocalizacao_diferenciada());
        valoresEndereco.put("bairro", endereco.getBairro());
        valoresEndereco.put("latitude", endereco.getLatitude());
        valoresEndereco.put("longitude", endereco.getLongitude());
        valoresEndereco.put("latitude_indic", endereco.getLatitude_indic());
        valoresEndereco.put("longitude_indic", endereco.getLongitude_indic());
        valoresEndereco.put("envio_comprovante", endereco.isEnvio_comprovante());

        db.insertWithOnConflict("endereco", null, valoresEndereco, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void deletarEnderecoAluno(int cd_aluno){

        String query = ("DELETE FROM endereco WHERE cd_aluno = " + cd_aluno);

        db.execSQL(query);
    }

    public void inserirAlunosResponsavel(JSONObject jsonResponse) {

        try {

            JSONArray alunosJson = jsonResponse.getJSONArray("Alunos");

            for (int i = 0; i < alunosJson.length(); i++) {

                JSONObject alunoJson = alunosJson.getJSONObject(i);

                AlunoTO alunoTO = new AlunoTO(alunoJson);

                Aluno aluno = alunoTO.getAlunoFromJsonResponsavel();

                ContentValues valoresAluno = new ContentValues();

                valoresAluno.put("cd_aluno", aluno.getCd_aluno());
                valoresAluno.put("nome", aluno.getNome());
                valoresAluno.put("numero_ra", aluno.getNumero_ra());
                valoresAluno.put("digito_ra", aluno.getDigito_ra());
                valoresAluno.put("email_aluno", aluno.getEmail_aluno());
                valoresAluno.put("numero_rg", aluno.getNumero_rg());
                valoresAluno.put("digito_rg", aluno.getDigito_rg());
                valoresAluno.put("numero_cpf", aluno.getNumero_cpf());
                valoresAluno.put("uf_ra", aluno.getUf_ra());
                valoresAluno.put("data_nascimento", aluno.getData_nascimento());
                valoresAluno.put("uf_nascimento", aluno.getUf_nascimento());
                valoresAluno.put("municipio_nascimento", aluno.getMunicipio_nascimento());
                valoresAluno.put("nacionalidade", aluno.getNacionalidade());
                valoresAluno.put("nome_mae", aluno.getNomeMae());
                valoresAluno.put("nome_pai", aluno.getNomePai());
                valoresAluno.put("idade", aluno.getIdade());
                valoresAluno.put("responde_rematricula", aluno.isResponde_rematricula());

                db.insertWithOnConflict("aluno", null, valoresAluno, SQLiteDatabase.CONFLICT_IGNORE);

                //inserir endereco no banco
                EnderecoTO enderecoTO = new EnderecoTO(alunoJson, aluno.getCd_aluno());

                Endereco endereco = enderecoTO.getEnderecoFromJson();

                ContentValues valoresEndereco = new ContentValues();

                valoresEndereco.put("cd_aluno", endereco.getCd_aluno());
                valoresEndereco.put("cidade", endereco.getCidade());
                valoresEndereco.put("endereco", endereco.getEndereco());
                valoresEndereco.put("numero_endereco", endereco.getNumero_endereco());
                valoresEndereco.put("complemento", endereco.getComplemento());
                valoresEndereco.put("numero_cep", endereco.getNumero_cep());
                valoresEndereco.put("tipo_logradouro", endereco.getTipo_logradouro());
                valoresEndereco.put("localizacao_diferenciada", endereco.getLocalizacao_diferenciada());
                valoresEndereco.put("bairro", endereco.getBairro());
                valoresEndereco.put("latitude", endereco.getLatitude());
                valoresEndereco.put("longitude", endereco.getLongitude());
                valoresEndereco.put("latitude_indic", endereco.getLatitude_indic());
                valoresEndereco.put("longitude_indic", endereco.getLongitude_indic());
                valoresEndereco.put("envio_comprovante", endereco.isEnvio_comprovante());

                db.insertWithOnConflict("endereco", null, valoresEndereco, SQLiteDatabase.CONFLICT_REPLACE);

                //inserir as turmas no banco
                JSONArray jsonTurmas = alunoJson.getJSONArray("Turmas");

                turmaQueries.inserirTurmas(jsonTurmas, aluno.getCd_aluno());

                //inserir as escolas no banco
                JSONArray jsonEscolas = jsonResponse.getJSONArray("Escolas");

                escolaQueries.inserirEscolas(jsonEscolas, aluno.getCd_aluno());

                //inserir Contato Aluno
                JSONArray jsonContato = alunoJson.getJSONArray("ContatoAluno");

                ContatoAlunoTO contatoAlunoTO = new ContatoAlunoTO(jsonContato);

                inserirContatosAlunos(contatoAlunoTO.getContatoAlunoFromJson());

                //inserir interesse rematricula
                if(alunoJson.getJSONArray("InteresseRematricula").length() == 1){

                    RematriculaTO rematriculaTO = new RematriculaTO(aluno.getCd_aluno(),
                            alunoJson.getJSONArray("InteresseRematricula").getJSONObject(0));

                    inserirRematricula(rematriculaTO.getRematriculaFromJson());
                }

            }

            //inserir as escolas e contato do aluno no banco
            JSONArray jsonEscolas = jsonResponse.getJSONArray("Escolas");

            escolaQueries.inserirEscolas(jsonEscolas, 0);
        }
        catch (JSONException e) {

            e.printStackTrace();
        }

    }

    public void inserirResponsavel(JSONObject respJson, String login, String senha, int cd_responsavel) {

        ResponsavelTO respTO = new ResponsavelTO(respJson, login, senha, cd_responsavel);

        Responsavel resp = respTO.getResponsavelFromJson();

        ContentValues valores = new ContentValues();

        valores.put("cd_responsavel", resp.getCd_responsavel());
        valores.put("token", resp.getToken());
        valores.put("login", resp.getLogin());
        valores.put("senha", resp.getSenha());

        db.insertWithOnConflict("responsavel", null, valores, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public void inserirEmailResponsavel(int cd_responsavel, String email){

        String query = "UPDATE responsavel SET email = ? WHERE cd_responsavel = ?";

        SQLiteStatement sqLiteStatement = db.compileStatement(query);

        sqLiteStatement.bindString(1, email);
        sqLiteStatement.bindLong(2, cd_responsavel);

        sqLiteStatement.executeUpdateDelete();

        sqLiteStatement.clearBindings();
        sqLiteStatement.close();
    }

    //endregion

    //region ---- SELECT ----
    public Aluno getAluno() {

        Aluno aluno = null;

        Cursor cursor = db.rawQuery("SELECT " +
                "cd_aluno, " +
                "numero_cpf, " +
                "numero_rg, " +
                "digito_rg, " +
                "nome, " +
                "token, " +
                "login, " +
                "senha, " +
                "uf_ra, " +
                "numero_ra, " +
                "digito_ra, " +
                "email_aluno, " +
                "municipio_nascimento, " +
                "uf_nascimento, " +
                "data_nascimento, " +
                "nacionalidade," +
                "nome_mae," +
                "nome_pai, " +
                "escola_centralizada, " +
                "idade, " +
                "responde_rematricula " +
                "FROM aluno;", null);

        if (cursor.moveToFirst()) {

            aluno = new Aluno();

            aluno.setCd_aluno(cursor.getInt(0));
            aluno.setNumero_cpf(cursor.getString(1));
            aluno.setNumero_rg(cursor.getString(2));
            aluno.setDigito_rg(cursor.getString(3));
            aluno.setNome(cursor.getString(4));
            aluno.setToken(cursor.getString(5));
            aluno.setLogin(cursor.getString(6));
            aluno.setSenha(cursor.getString(7));
            aluno.setUf_ra(cursor.getString(8));
            aluno.setNumero_ra(cursor.getString(9));
            aluno.setDigito_ra(cursor.getString(10));
            aluno.setEmail_aluno(cursor.getString(11));
            aluno.setMunicipio_nascimento(cursor.getString(12));
            aluno.setUf_nascimento(cursor.getString(13));
            aluno.setData_nascimento(cursor.getString(14));
            aluno.setNacionalidade(cursor.getString(15));
            aluno.setNomeMae(cursor.getString(16));
            aluno.setNomePai(cursor.getString(17));
            aluno.setEscola_centralizada((cursor.getInt(18) == 1));
            aluno.setIdade(cursor.getInt(19));
            aluno.setResponde_rematricula(cursor.getInt(20) == 1);

        }

        cursor.close();

        return aluno;
    }

    public Aluno getAluno(int cod_aluno) {

        Aluno aluno = null;

        Cursor cursor = db.rawQuery("SELECT cd_aluno, " +
                "numero_cpf, " +
                "numero_rg, " +
                "digito_rg, " +
                "nome, " +
                "uf_ra, " +
                "numero_ra, " +
                "digito_ra, " +
                "email_aluno, " +
                "municipio_nascimento, " +
                "uf_nascimento, " +
                "data_nascimento, " +
                "nacionalidade," +
                "nome_mae," +
                "nome_pai, " +
                "escola_centralizada, " +
                "idade, " +
                "responde_rematricula " +
                "FROM aluno "+
                "WHERE cd_aluno = ?", new String[]{String.valueOf(cod_aluno)});


        if (cursor.moveToFirst()) {

            aluno = new Aluno();

            aluno.setCd_aluno(cursor.getInt(0));
            aluno.setNumero_cpf(cursor.getString(1));
            aluno.setNumero_rg(cursor.getString(2));
            aluno.setDigito_rg(cursor.getString(3));
            aluno.setNome(cursor.getString(4));
            aluno.setUf_ra(cursor.getString(5));
            aluno.setNumero_ra(cursor.getString(6));
            aluno.setDigito_ra(cursor.getString(7));
            aluno.setEmail_aluno(cursor.getString(8));
            aluno.setMunicipio_nascimento(cursor.getString(9));
            aluno.setUf_nascimento(cursor.getString(10));
            aluno.setData_nascimento(cursor.getString(11));
            aluno.setNacionalidade(cursor.getString(12));
            aluno.setNomeMae(cursor.getString(13));
            aluno.setNomePai(cursor.getString(14));
            aluno.setEscola_centralizada((cursor.getInt(15) == 1));
            aluno.setIdade(cursor.getInt(16));
            aluno.setResponde_rematricula(cursor.getInt(17) == 1);
        }

        cursor.close();

        return aluno;
    }

    public Aluno getAlunoSelecionado(String nomeAluno) {

        Aluno aluno = new Aluno();

        Cursor cursor = db.rawQuery("SELECT cd_aluno, " +
                        "numero_cpf, " +
                        "numero_rg, " +
                        "digito_rg, " +
                        "nome, " +
                        "uf_ra, " +
                        "numero_ra, " +
                        "digito_ra, " +
                        "email_aluno, " +
                        "municipio_nascimento, " +
                        "uf_nascimento, " +
                        "data_nascimento, " +
                        "nacionalidade," +
                        "nome_mae," +
                        "nome_pai, " +
                        "escola_centralizada, " +
                        "idade, " +
                        "responde_rematricula " +
                        "FROM aluno WHERE nome = " + "'" + nomeAluno + "'" + ";",
                null);

        if (cursor.moveToFirst()) {

            aluno.setCd_aluno(cursor.getInt(0));
            aluno.setNumero_cpf(cursor.getString(1));
            aluno.setNumero_rg(cursor.getString(2));
            aluno.setDigito_rg(cursor.getString(3));
            aluno.setNome(cursor.getString(4));
            aluno.setUf_ra(cursor.getString(5));
            aluno.setNumero_ra(cursor.getString(6));
            aluno.setDigito_ra(cursor.getString(7));
            aluno.setEmail_aluno(cursor.getString(8));
            aluno.setMunicipio_nascimento(cursor.getString(9));
            aluno.setUf_nascimento(cursor.getString(10));
            aluno.setData_nascimento(cursor.getString(11));
            aluno.setNacionalidade(cursor.getString(12));
            aluno.setNomeMae(cursor.getString(13));
            aluno.setNomePai(cursor.getString(14));
            aluno.setEscola_centralizada((cursor.getInt(15) == 1));
            aluno.setIdade(cursor.getInt(16));
            aluno.setResponde_rematricula(cursor.getInt(17) == 1);

            cursor.close();
        }
        return aluno;
    }

    public ArrayList<Aluno> getAlunos() {

        ArrayList<Aluno> alunos = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT cd_aluno, " +
                "numero_cpf, " +
                "numero_rg, " +
                "digito_rg, " +
                "nome, " +
                "uf_ra, " +
                "numero_ra, " +
                "digito_ra, " +
                "email_aluno, " +
                "municipio_nascimento, " +
                "uf_nascimento, " +
                "data_nascimento, " +
                "nacionalidade," +
                "nome_mae," +
                "nome_pai, " +
                "escola_centralizada, " +
                "idade, " +
                "responde_rematricula " +
                "FROM aluno;", null);

        if (cursor.moveToFirst()) {

            do {

                Aluno aluno = new Aluno();

                aluno.setCd_aluno(cursor.getInt(0));
                aluno.setNumero_cpf(cursor.getString(1));
                aluno.setNumero_rg(cursor.getString(2));
                aluno.setDigito_rg(cursor.getString(3));
                aluno.setNome(cursor.getString(4));
                aluno.setUf_ra(cursor.getString(5));
                aluno.setNumero_ra(cursor.getString(6));
                aluno.setDigito_ra(cursor.getString(7));
                aluno.setEmail_aluno(cursor.getString(8));
                aluno.setMunicipio_nascimento(cursor.getString(9));
                aluno.setUf_nascimento(cursor.getString(10));
                aluno.setData_nascimento(cursor.getString(11));
                aluno.setNacionalidade(cursor.getString(12));
                aluno.setNomeMae(cursor.getString(13));
                aluno.setNomePai(cursor.getString(14));
                aluno.setEscola_centralizada((cursor.getInt(15) == 1));
                aluno.setIdade(cursor.getInt(16));
                aluno.setResponde_rematricula(cursor.getInt(17) == 1);
                alunos.add(aluno);

            }
            while (cursor.moveToNext());

        } else {

            cursor.close();
            return null;
        }

        cursor.close();
        return alunos;
    }

    public Endereco getEndereco(int cd_aluno) {

        Endereco endereco = null;

        Cursor cursor = db.rawQuery("SELECT cd_endereco, " +
                "cd_aluno, " +
                "uf_estado, " +
                "cidade, " +
                "endereco, " +
                "numero_endereco, " +
                "complemento, " +
                "numero_cep, " +
                "tipo_logradouro, " +
                "localizacao_diferenciada, " +
                "bairro, " +
                "latitude, " +
                "longitude, " +
                "latitude_indic," +
                "longitude_indic, " +
                "envio_comprovante " +
                "FROM endereco " +
                "WHERE cd_aluno = ?", new String[]{String.valueOf(cd_aluno)});

        if (cursor.moveToFirst()) {

            endereco = new Endereco();

            endereco.setCd_endereco(cursor.getInt(0));
            endereco.setCd_aluno(cursor.getInt(1));
            endereco.setUf_estado(cursor.getString(2));
            endereco.setCidade(cursor.getString(3));
            endereco.setEndereco(cursor.getString(4));

            if (cursor.getString(5) == null) {

                endereco.setNumero_endereco("");
            } else {

                endereco.setNumero_endereco(cursor.getString(5));
            }

            endereco.setComplemento(cursor.getString(6));
            endereco.setNumero_cep(cursor.getString(7));
            endereco.setTipo_logradouro(cursor.getInt(8));
            endereco.setLocalizacao_diferenciada(cursor.getInt(9));
            endereco.setBairro(cursor.getString(10));
            endereco.setLatitude(cursor.getString(11));
            endereco.setLongitude(cursor.getString(12));
            endereco.setLatitude_indic(cursor.getString(13));
            endereco.setLongitude_indic(cursor.getString(14));
            endereco.setEnvio_comprovante((cursor.getInt(15) == 1));

        }

        cursor.close();

        return endereco;
    }

    public List<String> getNomesAlunos() {

        Cursor cursor = db.rawQuery("SELECT nome FROM aluno", null);

        ArrayList<String> nomesAlunos = new ArrayList<>(cursor.getCount());

        if (cursor.moveToFirst()) {

            do {

                nomesAlunos.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return nomesAlunos;
    }

    public ArrayList<String> getMunicipios() {

        Cursor cursor = db.rawQuery("SELECT municipio  FROM municipios", null);

        ArrayList<String> municipios = new ArrayList<>();

        if (cursor.moveToFirst()) {

            do{

                municipios.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }
        cursor.close();

        return municipios;
    }

    public String getToken() {

        String token = "";

        if (mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

            Cursor cursor = db.rawQuery("SELECT token FROM responsavel", null);

            if (cursor.moveToFirst()) {

                token = cursor.getString(0);

                cursor.close();
            }
            return token;
        } else {

            Cursor cursor = db.rawQuery("SELECT token FROM aluno", null);

            if (cursor.moveToFirst()) {

                token = cursor.getString(0);

                cursor.close();
            }
            return token;
        }
    }

    public int getCodMunicipio(String municipio) {
        int dne = 0;

        Cursor cursor = db.rawQuery("SELECT codigo_dne FROM municipios WHERE municipio = ?", new String[]{municipio});

        if (cursor.moveToFirst()) {

            dne = cursor.getInt(0);

            cursor.close();
        }
        return dne;
    }

    public Responsavel getResponsavel() {

        Responsavel resp = null;

        Cursor cursor = db.rawQuery("SELECT cd_responsavel, token, login, senha, email FROM responsavel", null);

        if (cursor.moveToFirst()) {

            resp = new Responsavel();

            resp.setCd_responsavel(cursor.getInt(0));
            resp.setToken(cursor.getString(1));
            resp.setLogin(cursor.getString(2));
            resp.setSenha(cursor.getString(3));
            resp.setEmail(cursor.getString(4));
        }

        cursor.close();

        return resp;
    }

    //endregion

    //region ---- UPDATE ----
    public void atualizarToken(String novoToken) {

        ContentValues valores = new ContentValues();

        if (mPref.getPerfilSelecionado() == MyPreferences.PERFIL_RESPONSAVEL) {

            valores.put("token", novoToken);

            db.update("responsavel", valores, null, null);
        } else {

            valores.put("token", novoToken);

            db.update("aluno", valores, null, null);
        }
        valores.clear();
    }

    public void atualizarEmailAluno(int cd_aluno, String email){


        db.execSQL("UPDATE aluno SET email_aluno = ? WHERE cd_aluno = "+ cd_aluno , new String[]{email});
    }

    public void atualizarEmailResponsavel(int cd_responsavel, String email) {


        db.execSQL("UPDATE responsavel SET email= ? WHERE cd_responsavel = " + cd_responsavel, new String[]{email});
    }

        //endregion

    //region ---- ETC ----
    public boolean temUsuarioLogado() {

        boolean temUsuarioLogado = false;

        Cursor cursor = db.rawQuery("SELECT * FROM aluno;", null);

        if (cursor.moveToFirst()) {

            temUsuarioLogado = true;
        } else {

            cursor = db.rawQuery("SELECT * FROM responsavel;", null);

            if (cursor.moveToFirst()) {

                temUsuarioLogado = true;
            }
        }
        cursor.close();

        return temUsuarioLogado;
    }

    //endregion

    //region ---- REMATRICULA ----

    public void inserirRematricula(Rematricula rematricula){

        ContentValues valores = new ContentValues();

        valores.put("cd_interesse_rematricula", rematricula.getCd_interesse_rematricula());
        valores.put("cd_aluno", rematricula.getCd_aluno());
        valores.put("ano_letivo", rematricula.getAno_letivo());
        valores.put("ano_letivo_rematricula", rematricula.getAno_letivo_rematricula());
        valores.put("interesse_continuidade", rematricula.isInteresse_continuidade());
        valores.put("interesse_novotec", rematricula.isInteresse_novotec());
        valores.put("interesse_turno_integral", rematricula.isInteresse_turno_integral());
        valores.put("interesse_espanhol", rematricula.isInteresse_espanhol());
        valores.put("interesse_noturno", rematricula.isInteresse_noturno());
        valores.put("aceito_termos_responsabilidade", rematricula.isAceito_termos_responsabilidade());
        valores.put("eixo_ensino_profissional_um", rematricula.getEixo_ensino_profissional_um());
        valores.put("eixo_ensino_profissional_dois", rematricula.getEixo_ensino_profissional_dois());
        valores.put("eixo_ensino_profissional_tres", rematricula.getEixo_ensino_profissional_tres());
        valores.put("observacao_opcao_noturno", rematricula.getObservacao_opc_noturno());

        db.insertWithOnConflict("rematricula", null, valores, SQLiteDatabase.CONFLICT_REPLACE);

    }

    public Rematricula getRematricula(int cd_aluno) {

        Rematricula rematricula = null;

        Cursor cursor = db.rawQuery("SELECT " +
                "cd_interesse_rematricula, " +
                "cd_aluno, " +
                "ano_letivo, " +
                "ano_letivo_rematricula, " +
                "interesse_continuidade, " +
                "interesse_novotec, " +
                "interesse_turno_integral, " +
                "interesse_espanhol, " +
                "interesse_noturno, " +
                "aceito_termos_responsabilidade, " +
                "eixo_ensino_profissional_um, " +
                "eixo_ensino_profissional_dois, " +
                "eixo_ensino_profissional_tres, " +
                "observacao_opcao_noturno " +
                "FROM rematricula " +
                "WHERE cd_aluno = ?;", new String[]{String.valueOf(cd_aluno)});

        if (cursor.moveToFirst()) {

            rematricula = new Rematricula();

            rematricula.setCd_interesse_rematricula(cursor.getInt(0));
            rematricula.setCd_aluno(cursor.getInt(1));
            rematricula.setAno_letivo(cursor.getInt(2));
            rematricula.setAno_letivo_rematricula(cursor.getInt(3));
            rematricula.setInteresse_continuidade((cursor.getInt(4) == 1));
            rematricula.setInteresse_novotec((cursor.getInt(5) == 1));
            rematricula.setInteresse_turno_integral((cursor.getInt(6) == 1));
            rematricula.setInteresse_espanhol((cursor.getInt(7) == 1));
            rematricula.setInteresse_noturno((cursor.getInt(8) == 1));
            rematricula.setAceito_termos_responsabilidade((cursor.getInt(9) == 1));
            rematricula.setEixo_ensino_profissional_um(cursor.getInt(10));
            rematricula.setEixo_ensino_profissional_dois(cursor.getInt(11));
            rematricula.setEixo_ensino_profissional_tres(cursor.getInt(12));
            rematricula.setObservacao_opc_noturno(cursor.getInt(13));

        }

        cursor.close();

        return rematricula;
    }

    public void deletarRematricula(int cd_interesse_rematricula){


        String query = ("DELETE FROM rematricula WHERE cd_interesse_rematricula = " + cd_interesse_rematricula);

        db.execSQL(query);

    }

    //endregion

    //region ---- TELEFONE ALUNO ----

    public void inserirContatosAlunos(ArrayList<ContatoAluno> contatosAluno) {

        for (ContatoAluno contato : contatosAluno) {

            inserirContatoAluno(contato);
        }
    }

    public void inserirContatoAluno(ContatoAluno contatoAluno) {

        ContentValues valores = new ContentValues();

        valores.put("cd_aluno", contatoAluno.getCd_aluno());
        valores.put("codigo_fone_telefone", contatoAluno.getCodigo_fone_telefone());
        valores.put("validacao_sms", contatoAluno.getValidacao_sms());
        valores.put("codigo_tipo_telefone", contatoAluno.getCodigo_tipo_telefone());
        valores.put("codigo_ddd", contatoAluno.getCodigo_ddd());
        valores.put("telefone_aluno", contatoAluno.getTelefone_aluno());
        valores.put("complemento_telefone", contatoAluno.getComplemento_telefone());

        db.insertWithOnConflict("contato_aluno", null, valores, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public ArrayList<ContatoAluno> getContatosAluno(int cd_aluno) {

        Cursor cursor = db.rawQuery("SELECT cd_contato_aluno, " +
                "cd_aluno, " +
                "codigo_fone_telefone, " +
                "validacao_sms, " +
                "codigo_tipo_telefone, " +
                "codigo_ddd, " +
                "telefone_aluno, " +
                "complemento_telefone " +
                "FROM contato_aluno " +
                "WHERE cd_aluno = " + cd_aluno, null);

        ArrayList<ContatoAluno> contatosAluno = new ArrayList<>();

        if (cursor.moveToFirst()) {

            do {

                ContatoAluno contatoAluno = new ContatoAluno();

                contatoAluno.setCd_contato_aluno(cursor.getInt(0));
                contatoAluno.setCd_aluno(cursor.getInt(1));
                contatoAluno.setCodigo_fone_telefone(cursor.getInt(2));
                contatoAluno.setValidacao_sms(cursor.getInt(3));
                contatoAluno.setCodigo_tipo_telefone(cursor.getInt(4));
                contatoAluno.setCodigo_ddd(cursor.getString(5));
                contatoAluno.setTelefone_aluno(cursor.getString(6));
                contatoAluno.setComplemento_telefone(cursor.getString(7));

                contatosAluno.add(contatoAluno);

            } while (cursor.moveToNext());
        }

        return contatosAluno;
    }

    public void deletarTelefonesAluno(int cd_aluno){

        String query = ("DELETE FROM contato_aluno WHERE cd_aluno = " + cd_aluno);

        db.execSQL(query);

    }

    //endregion

    //region ---- TELEFONE RESPONSAVEL ----

    public ArrayList<ContatoResponsavel> getContatosResponsavel(int cd_responsavel) {

        Cursor cursor = db.rawQuery("SELECT cd_contato_responsavel, " +
                "cd_responsavel, " +
                "codigo_fone_telefone, " +
                "validacao_sms, " +
                "codigo_tipo_telefone, " +
                "codigo_ddd, " +
                "telefone_responsavel, " +
                "complemento_telefone " +
                "FROM contato_responsavel " +
                "WHERE cd_responsavel = " + cd_responsavel, null);

        ArrayList<ContatoResponsavel> contatosResponsavel = new ArrayList<>();

        if (cursor.moveToFirst()) {

            do {

                ContatoResponsavel contatoResponsavel = new ContatoResponsavel();

                contatoResponsavel.setCd_contato_responsavel(cursor.getInt(0));
                contatoResponsavel.setCd_responsavel(cursor.getInt(1));
                contatoResponsavel.setCodigo_fone_telefone(cursor.getInt(2));
                contatoResponsavel.setValidacao_sms(cursor.getInt(3));
                contatoResponsavel.setCodigo_tipo_telefone(cursor.getInt(4));
                contatoResponsavel.setCodigo_ddd(cursor.getString(5));
                contatoResponsavel.setTelefone_responsavel(cursor.getString(6));
                contatoResponsavel.setComplemento_telefone(cursor.getString(7));

                contatosResponsavel.add(contatoResponsavel);

            } while (cursor.moveToNext());
        }

        return contatosResponsavel;
    }

    public void inserirContatoResponsavel(ContatoResponsavel contatoResponsavel) {

        ContentValues valores = new ContentValues();

        valores.put("cd_responsavel", contatoResponsavel.getCd_responsavel());
        valores.put("codigo_fone_telefone", contatoResponsavel.getCodigo_fone_telefone());
        valores.put("validacao_sms", contatoResponsavel.getValidacao_sms());
        valores.put("codigo_tipo_telefone", contatoResponsavel.getCodigo_tipo_telefone());
        valores.put("codigo_ddd", contatoResponsavel.getCodigo_ddd());
        valores.put("telefone_responsavel", contatoResponsavel.getTelefone_responsavel());
        valores.put("complemento_telefone", contatoResponsavel.getComplemento_telefone());

        db.insertWithOnConflict("contato_responsavel", null, valores, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void inserirContatosResponsavel(ArrayList<ContatoResponsavel> contatosResponsavel) {

        for (ContatoResponsavel contato : contatosResponsavel) {

            inserirContatoResponsavel(contato);
        }
    }

    public void deletarTelefonesResponsavel(int cd_responsavel){

        String query = ("DELETE FROM contato_responsavel WHERE cd_responsavel = " + cd_responsavel);

        db.execSQL(query);

    }

    //endregion

    //DELETAR BANCO
    public void deleteTable(String nameTable) {

        db.beginTransaction();

        try {

            db.delete(nameTable,
                    null,
                    null);

            db.setTransactionSuccessful();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            db.endTransaction();
        }
    }

}
