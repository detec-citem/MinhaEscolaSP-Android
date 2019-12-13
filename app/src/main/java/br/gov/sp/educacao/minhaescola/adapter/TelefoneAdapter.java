package br.gov.sp.educacao.minhaescola.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import br.gov.sp.educacao.minhaescola.R;
import br.gov.sp.educacao.minhaescola.model.ContatoAluno;
import br.gov.sp.educacao.minhaescola.model.ContatoResponsavel;
import br.gov.sp.educacao.minhaescola.view.SobreMimActivity;

public class TelefoneAdapter extends BaseAdapter {

    public ArrayList<ContatoAluno> contatosAlunos;

    public ArrayList<ContatoResponsavel> contatosResponsavel;

    public boolean isAluno = true;

    public SobreMimActivity act;

    public TelefoneAdapter(ArrayList<ContatoAluno> contatosAlunos, ArrayList<ContatoResponsavel> contatosResponsavel, SobreMimActivity act) {

        if(contatosAlunos != null){

            this.contatosAlunos = contatosAlunos;
            isAluno = true;
        }
        else{

            isAluno = false;
            this.contatosResponsavel = contatosResponsavel;
        }

        this.act = act;
    }

    @Override
    public int getCount() {

        if(isAluno){

            return contatosAlunos.size();
        }
        else{

            return contatosResponsavel.size();
        }
    }

    @Override
    public Object getItem(int i) {

        if(isAluno){

            return contatosAlunos.get(i);
        }
        else{

            return contatosResponsavel.get(i);
        }
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = act.getLayoutInflater().inflate(R.layout.item_telefone, viewGroup, false);

        final TextView txtTelefone = v.findViewById(R.id.sobre_item_telefone);

        final Button btnEditar = v.findViewById(R.id.sobre_item_telefone_editar);

        final Button btnExcluir = v.findViewById(R.id.sobre_item_telefone_excluir);

        final EditText edtComplemento = v.findViewById(R.id.sobre_edit_complemento_telefone);

        if(isAluno){

            final ContatoAluno contatoAluno = (ContatoAluno) getItem(i);

            txtTelefone.setText("("+contatoAluno.getCodigo_ddd()+") " + contatoAluno.getTelefone_aluno());

            btnEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    act.editarContatoAluno(contatoAluno);
                }
            });

            btnExcluir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    act.excluirContatoAluno(contatoAluno);
                }
            });

        }
        else{

            final ContatoResponsavel contatoResponsavel = (ContatoResponsavel) getItem(i);

            txtTelefone.setText("("+contatoResponsavel.getCodigo_ddd()+") " + contatoResponsavel.getTelefone_responsavel());

            btnEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    act.editarContatoResp(contatoResponsavel);
                }
            });

            btnExcluir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    act.excluirContatoResp(contatoResponsavel);
                }
            });

        }

        return v;
    }
}
