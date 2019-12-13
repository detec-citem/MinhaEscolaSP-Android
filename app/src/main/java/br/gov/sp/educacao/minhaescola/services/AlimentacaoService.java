package br.gov.sp.educacao.minhaescola.services;

import android.app.IntentService;

import android.content.Context;
import android.content.Intent;

import br.gov.sp.educacao.minhaescola.receivers.AlimentacaoReceiver;
import br.gov.sp.educacao.minhaescola.requests.AlimentacaoRequest;

/**
 * Classe que estende IntentService
 * Este serviço faz a chamada para acontecer o request junto à API
 */

public class AlimentacaoService
        extends IntentService {

    //Construtor do Serviço, passando String com o nome da classe

    public AlimentacaoService() {

        super("AlimentacaoService");
    }

    int cd_turma;

    //Método que faz o Intent com os Dados que serão recebidos pelo Service
    //O Intent é criado passando o contexto e um objeto usuário
    //As credenciais do usuário serão utilizados na criação e execução do request

    public static Intent makeDataIntent(Context context, String token, int cd_turma) {

        //Cria e retorna o novo Intent, colocando o objeto usuário nos Extras

        return new Intent(context, AlimentacaoService.class).putExtra("Token", token).putExtra("cd_turma", cd_turma);

    }

    //Método que define como o Service deve lidar com o Intent recebido

    @Override
    protected void onHandleIntent(Intent intent) {

        //Pega o objeto do usuário do Intent e seta a referência para novo objeto usuário

        String token = (String) intent.getStringExtra("Token");

        int cd_turma = (int) intent.getIntExtra("cd_turma", 0);

        //Cria a variável podeResponder com valor inicial 2

        int podeResponder = 2;

        try {

            //Executa o request para estabelecer a conexão com a API, passando o context e usuário
            //A resposta será transformada em valor int e setada para a varíavel podeResponder

            podeResponder =  AlimentacaoRequest.executeAlimentacaoRequest(getBaseContext(), token);
        }
        catch (Exception e) {

            //Caso ocorra uma exceção, mostre o StackTrace

            e.printStackTrace();
        }

        //Faz e envia o BroadCast para o BroadCastReceiver
        //O Intent a ser passado na chamada é criado com o método makeDataIntent
        //que retorna um Intent

        sendBroadcast(AlimentacaoReceiver.makeDataIntent(getApplicationContext(), podeResponder, cd_turma));
    }
}
