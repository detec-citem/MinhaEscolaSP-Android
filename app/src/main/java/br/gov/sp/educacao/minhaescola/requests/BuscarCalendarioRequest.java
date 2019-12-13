package br.gov.sp.educacao.minhaescola.requests;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import br.gov.sp.educacao.minhaescola.banco.CalendarioQueries;
import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;
import br.gov.sp.educacao.minhaescola.model.Aluno;
import br.gov.sp.educacao.minhaescola.model.DiasEventos;
import br.gov.sp.educacao.minhaescola.util.ConnectionFactory;
import br.gov.sp.educacao.minhaescola.util.ConnectionReader;
import br.gov.sp.educacao.minhaescola.util.UrlServidor;

public class BuscarCalendarioRequest {

    //Controla o número de tentativas (Máx - 3)

    private static int mTentativas = 0;

    private static String numBimestres;
    //Referência do objeto para realizar queries

    private static UsuarioQueries usuarioQueries;

    private static CalendarioQueries calendarioQueries;

    //Método que prepara o JSON, estabelece a conexão e recebe a resposta

    public static int executeRequest(String token, int codTurma, int cd_aluno, Context context){

        //Cria o objeto para fazer queries

        calendarioQueries = new CalendarioQueries(context);

        //Criar objeto usuário para queries do usuario (revaliadacao Token)
        usuarioQueries = new UsuarioQueries(context);

        try {

            //Cria o objeto para conexão, com o URL + código da turma, token do usuário e método GET

            HttpsURLConnection httpsURLConnection = ConnectionFactory.createHttpsUrlConnection("GET",
                    UrlServidor.URL_CALENDARIO + String.valueOf(codTurma),
                    token);
            httpsURLConnection.connect();

            int statusCode = httpsURLConnection.getResponseCode();

            //Estado 200 e 201 significa conexão com sucesso

            if (statusCode == 201
                    || statusCode == 200) {

                String apiResponse = ConnectionReader.readStringFromHttpsURLConnection(true,
                        httpsURLConnection);
                //Fecha a conexão

                httpsURLConnection.disconnect();

                //Cria um objeto JSONArray a partir da String recebida da conexão

                if(apiResponse.contains("Calendário não encontrado.")){

                    return 3;
                }

                JSONArray jsonResponse = new JSONArray(apiResponse);

                //Cria o HashMap que relaciona os meses com os eventos em seus dias

                Map<Integer, List<DiasEventos>> mapaCalendario = new HashMap<>();

                //calendarioQueries.deleteBimestres(String.valueOf(cd_aluno));

                //Iteração pelos 4 bimestres

                for(int i = 0; i < 4; i++) {

                    //Determina quantos meses existe no array calendário em cada objeto(i)

                    int calendarioSize = jsonResponse.getJSONObject(i).getJSONArray("calendario").length();

                    //Determina o número do Bimestre sendo iterado

                    String numBimestre = jsonResponse.getJSONObject(i).getString("Bimestre");

                    //Determina a data de início do Bimestre que está sendo iterado

                    String inicioBimestre = jsonResponse.getJSONObject(i).getString("DataInicio");

                    //Determina a data final do Bimestre que está sendo iterado

                    String fimBimestre = jsonResponse.getJSONObject(i).getString("DataFim");

                    //Insere os dados do Bimestre na tabela "bimestres"

                    calendarioQueries.inserirBimestres(cd_aluno, numBimestre, inicioBimestre, fimBimestre);

                    //Iteração pelos meses dentro do array calendário em cada objeto(i)

                    for(int j = 0; j < calendarioSize; j++) {

                        String bimestre = jsonResponse.getJSONObject(i).getString("Bimestre");

                        //Determina o número do mês que está em iteração

                        Integer mes = Integer.valueOf(jsonResponse.getJSONObject(i)
                                .getJSONArray("calendario")
                                .getJSONObject(j)
                                .get("Mes").toString());

                        //Cria a lista de DiasEventos que será associada a um mês

                        List<DiasEventos> listaDiasEventos = new ArrayList<DiasEventos>();

                        //Determina o número de dias com eventos dentro de cada mês(j) dentro do objeto(i)

                        int mesSize = jsonResponse.getJSONObject(i)
                                .getJSONArray("calendario")
                                .getJSONObject(j)
                                .getJSONArray("DiasEventos")
                                .length();

                        //Iteração pelos dias com eventos dentro de cada mês(j), de cada objeto(i)

                        for(int k = 0; k < mesSize; k++) {

                            //Determina o valor do dia com evento

                            int dia = Integer.valueOf(jsonResponse.getJSONObject(i)
                                    .getJSONArray("calendario")
                                    .getJSONObject(j)
                                    .getJSONArray("DiasEventos")
                                    .getJSONObject(k)
                                    .get("Dia")
                                    .toString());

                            //Determina se o dia é letivo ou não

                            boolean letivo = Boolean.valueOf(jsonResponse.getJSONObject(i)
                                    .getJSONArray("calendario")
                                    .getJSONObject(j)
                                    .getJSONArray("DiasEventos")
                                    .getJSONObject(k)
                                    .get("Letivo")
                                    .toString());

                            //Determina o nome do evento

                            String nomeEvento = jsonResponse.getJSONObject(i)
                                    .getJSONArray("calendario")
                                    .getJSONObject(j)
                                    .getJSONArray("DiasEventos")
                                    .getJSONObject(k)
                                    .get("NomeEvento")
                                    .toString();

                            //Determina a descrição do evento

                            String descricaoEvento = jsonResponse.getJSONObject(i)
                                    .getJSONArray("calendario")
                                    .getJSONObject(j)
                                    .getJSONArray("DiasEventos")
                                    .getJSONObject(k)
                                    .get("DescricaoEvento")
                                    .toString();

                            //Determina o nome da Disciplina que possui evento
                            //(Avaliação/Atividade/Trabalho/Outros)

                            String nomeDisciplina = jsonResponse.getJSONObject(i)
                                    .getJSONArray("calendario")
                                    .getJSONObject(j)
                                    .getJSONArray("DiasEventos")
                                    .getJSONObject(k)
                                    .get("Disciplina")
                                    .toString();

                            //Cria e popula o objeto DiasEventos com as informações de cada dia com evento

                            DiasEventos diasEventos = new DiasEventos(bimestre, String.valueOf(mes), String.valueOf(dia), letivo, nomeEvento, descricaoEvento,nomeDisciplina);

                            //Adiciona cada objeto DiasEventos na lista

                            listaDiasEventos.add(diasEventos);
                        }

                        //Se o mapa não contém a chave para o mês sendo iterado

                        if(!mapaCalendario.containsKey(mes)) {

                            //Adiciona no mapa a chave(mês) com o valor(lista)

                            mapaCalendario.put(mes, listaDiasEventos);
                        }

                        //Se o mapa já contém a chave para o mês sendo iterado

                        else {

                            //Pega os valores(lista) associados à chave(mês) e os adiciona na lista atual

                            listaDiasEventos.addAll(mapaCalendario.get(mes));

                            //Adiciona no mapa a chave(mês) associada ao valor(lista) que contém os novos
                            //valores iterados com adição dos itens já existentes para aquela chave(mês)

                            mapaCalendario.put(mes, listaDiasEventos);
                        }
                    }
                }
                calendarioQueries.inserirCalendarioTurma(mapaCalendario, cd_aluno);
            }
            else
            if(statusCode == 400
                    && mTentativas < 3){

                //Se o código de reposta for != 200 || 201 tente revalidar o token
                //O número de tentativas para revalidar o token é limitado em 3
                httpsURLConnection.disconnect();

                RevalidaTokenRequest revalidaTokenRequest = new RevalidaTokenRequest();

                String newToken = revalidaTokenRequest.executeRequest(context);

                //O número de tentativas para revalidar o token é limitado em 3

                if(newToken != null
                        && mTentativas < 3) {

                    //Atualiza o token do usuário no SQLite
                    usuarioQueries.atualizarToken(newToken);

                    //Atualiza a quantidade de tentativas
                    mTentativas++;

                    //Atualiza o objeto usuário com o token válido
                    //Executa novamente o executeRequest desta classe
                    return executeRequest(newToken, codTurma,cd_aluno, context);
                }
                else {

                    httpsURLConnection.disconnect();
                    //Se o token estiver nulo ou o número máximo de tentativas for alcançado
                    //retorna 2 (Ocorreu um erro)

                    return 2;
                }
            }
        }
        catch (IOException | JSONException e) {

            //Se houver exceção retorna 2 (Ocorreu um erro)

            return 2;
        }

        //Zera o número de tentativas

        mTentativas = 0;

        //Sucesso retorna 1

        return 1;
    }
}
