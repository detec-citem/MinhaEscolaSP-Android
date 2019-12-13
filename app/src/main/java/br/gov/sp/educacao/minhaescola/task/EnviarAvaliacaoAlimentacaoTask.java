package br.gov.sp.educacao.minhaescola.task;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import br.gov.sp.educacao.minhaescola.banco.UsuarioQueries;
import br.gov.sp.educacao.minhaescola.model.Aluno;
import br.gov.sp.educacao.minhaescola.model.AvaliacaoAlimentacao;
import br.gov.sp.educacao.minhaescola.requests.EnviarAvaliacaoAlimentacaoRequest;
import br.gov.sp.educacao.minhaescola.view.MenuActivity;
import br.gov.sp.educacao.minhaescola.view.new_alimentacao.AvaliacaoConcluirActivity;


//Class que executa de modo assíncrono o EnviarAvaliacaoAlimentacaoRequest


public class EnviarAvaliacaoAlimentacaoTask extends AsyncTask<Object, Void, Integer> {

    private UsuarioQueries usuarioQueries;

    //Cria a Weak Reference para a AlimentacaoConcluirActivity

    private WeakReference<AvaliacaoConcluirActivity> avaliacaoConcluirWeakReference;

    //Construtor com a referência para a Weak Reference

    public EnviarAvaliacaoAlimentacaoTask(AvaliacaoConcluirActivity avaliacaoConcluirActivity) {

        this.avaliacaoConcluirWeakReference = new WeakReference<>(avaliacaoConcluirActivity);

        usuarioQueries = new UsuarioQueries(avaliacaoConcluirActivity);
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        //Pega a referência para a AlimentacaoConcluirActivity

        AvaliacaoConcluirActivity mConcluirActivity = avaliacaoConcluirWeakReference.get();

        //Mostra o progress dialog na activity

        mConcluirActivity.progressDialog.show();
    }

    @Override
    protected Integer doInBackground(Object... params) {

        //Pega a referência para a AlimentacaoConcluirActivity

        AvaliacaoConcluirActivity mConcluirActivity = avaliacaoConcluirWeakReference.get();

        Aluno aluno = usuarioQueries.getAluno();

        //Pega a referência para o objeto avaliacaoAlimentacao a partir do parâmetro 0

        AvaliacaoAlimentacao avaliacaoAlimentacao = (AvaliacaoAlimentacao) params[0];

        int cd_turma = avaliacaoAlimentacao.getCodTurma();

        int resultado = 0;

        try {

            //Executa o request passando a Activity, o usuário e a avaliação
            //O código retornado pela tentativa de conexão é atribuído para
            //a variável resultado

            EnviarAvaliacaoAlimentacaoRequest enviarAvalAlimRequest = new EnviarAvaliacaoAlimentacaoRequest();

            resultado = enviarAvalAlimRequest.executeRequest(mConcluirActivity, avaliacaoAlimentacao, aluno.getCd_aluno(), cd_turma);
        }
        catch (Exception e) {

            //Caso ocorra exceção retorna resultado como código 400
            e.printStackTrace();
            resultado = 400;
        }

        //Retorna o código do resultado

        return resultado;
    }

    @Override
    protected void onPostExecute(Integer resultado) {

        //Pega a referência para a AlimentacaoConcluirActivity

        AvaliacaoConcluirActivity mConcluirActivity = avaliacaoConcluirWeakReference.get();

        //Finaliza a exibição do progress dialog na activity

        mConcluirActivity.progressDialog.dismiss();

        //Resultado com código 200 || 201 mostra que o request foi feito com sucesso

        if(resultado== 200
                || resultado == 201) {

            //Exibe mensagem Toast na Activity, mostrando que a avaliação foi enviada com sucesso

            Toast.makeText(mConcluirActivity, "Avaliação enviada com sucesso", Toast.LENGTH_SHORT).show();

            //Cria o intent para retornar à Activity MenuActivity

            Intent enviar = new Intent(mConcluirActivity, MenuActivity.class);

            //Inicializa a MenuActivity

            mConcluirActivity.startActivity(enviar);
        }
        else {

            //Exibe mensagem Toast na Activity, mostrando que houve um erro no envio da avaliação

            Toast.makeText(mConcluirActivity, "Falha ao finalizar a avaliação, tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }
}


