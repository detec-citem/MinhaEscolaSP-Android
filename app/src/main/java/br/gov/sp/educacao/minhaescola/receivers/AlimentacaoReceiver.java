package br.gov.sp.educacao.minhaescola.receivers;

import android.content.BroadcastReceiver;

import android.content.Context;

import android.content.Intent;

import android.widget.Toast;

import java.lang.ref.WeakReference;

import br.gov.sp.educacao.minhaescola.view.MenuActivity;
import br.gov.sp.educacao.minhaescola.view.new_alimentacao.AvaliacaoQuestao1Activity;
import br.gov.sp.educacao.minhaescola.view.new_alimentacao.CardapioActivity;

/**
 * Classe que estende a classe BroadCastReceiver
 * Ela se comunica com o Service e repassa os resultados recebidos
 * pelo serviço para a Activity
 */

public class AlimentacaoReceiver
        extends BroadcastReceiver {

    //String ACTION_VIEW com o filtro de intent para ALIMENTACAO

    public final static String ACTION_VIEW_ALIMENTACAO = "br.gov.sp.educacao.minhaescola.VIEW_ALIMENTACAO";

    //Referência para a Activity em que o Receiver é registrado (MenuActivity)

    ////private MenuActivity mMenuActivity;

    private WeakReference<CardapioActivity> cardapioActivityWeakReference;


    //Construtor do Receiver, recebendo a Activity em que ele é criado (MenuActivity)

    public AlimentacaoReceiver(CardapioActivity activity) {

        //Seta a referência do objeto para a Activity recebida (MenuActivity)

        this.cardapioActivityWeakReference = new WeakReference<>(activity);

        ////mMenuActivity = activity;
    }

    //Método que faz o Intent com os Dados que serão recebidos pelo Receiver
    //O Intent é criado passando o contexto e um int que determina se o aluno
    //pode responder ou não a avaliação alimentar no dia

    public static Intent makeDataIntent(Context context, int podeResponder, int cd_turma) {

        //O método retorna o intent com o filtro ACTION_VIEW_ALIMENTACAO e com a variável
        //podeResponder incluída no Extras
        //O pacote é setado para o Pacote encontrado no Context (Segurança)

        return new Intent(AlimentacaoReceiver.ACTION_VIEW_ALIMENTACAO).putExtra("Pode Responder", podeResponder).putExtra("cd_turma", cd_turma)
                .setPackage(context.getPackageName());
    }

    //Método que estabalece como o Receiver irá tratar o Intent recebido

    @Override
    public void onReceive(Context context, Intent intent) {

        //Pega o valor dos Extras, chave "Pode Responder"

        int permissao = intent.getIntExtra("Pode Responder", 2);

        int cd_turma = intent.getIntExtra("cd_turma", 0);

        //A resposta foi recebida então o progress dialog tem sua exibição
        //finalizada na Activity

        cardapioActivityWeakReference.get().progressDialogCardapio.dismiss();

        //Se o int recebido do Intent for 1 significa que o aluno pode fazer a Avaliação Alimentar no dia

        if(permissao == 1) {

            //Cria o Intent para a primeira questão da Avaliação Alimentar

            Intent intent1 = new Intent(cardapioActivityWeakReference.get(), AvaliacaoQuestao1Activity.class);

            intent.putExtra("cd_turma", cd_turma);

            //Inicializa a MenuActivity

            cardapioActivityWeakReference.get().startActivity(intent1);
            cardapioActivityWeakReference.get().finish();
        }

        //Se o int recebido for 0 significa que o aluno já faz a avaliação no dia

        else if(permissao == 0) {

            //Mostra uma Toast na Activity informando que o aluno já fez a avaliação no dia

            Toast.makeText(cardapioActivityWeakReference.get(), "É possível avaliar 1 vez por dia, apenas em dias de aula", Toast.LENGTH_LONG).show(); //TODO Validar texto

            //cardapioActivityWeakReference.get().startActivity(new Intent(cardapioActivityWeakReference.get(), MenuActivity.class));
            cardapioActivityWeakReference.get().finish();
        }

        //Se o int recebido for 2 significa que houve um erro na comunicação com a API

        else {

            //Mostra uma Toast na Activity informando que houve um erro

            Toast.makeText(cardapioActivityWeakReference.get(), "Verifique sua conexão e tente novamente", Toast.LENGTH_SHORT).show();
            //cardapioActivityWeakReference.get().startActivity(new Intent(cardapioActivityWeakReference.get(), MenuActivity.class));
            cardapioActivityWeakReference.get().finish();
        }
        cardapioActivityWeakReference.clear();
    }
}
