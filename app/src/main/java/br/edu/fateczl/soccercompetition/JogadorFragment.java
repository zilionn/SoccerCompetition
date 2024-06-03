package br.edu.fateczl.soccercompetition;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.List;

import br.edu.fateczl.soccercompetition.controller.JogadorController;
import br.edu.fateczl.soccercompetition.controller.TimeController;
import br.edu.fateczl.soccercompetition.model.Jogador;
import br.edu.fateczl.soccercompetition.model.Time;
import br.edu.fateczl.soccercompetition.persistence.JogadorDao;
import br.edu.fateczl.soccercompetition.persistence.TimeDao;

public class JogadorFragment extends Fragment {

    private View view;
    private EditText etIDJogador, etNomeJogador, etDataNascJogador, etAlturaJogador, etPesoJogador;
    private Button btnInserirJogador, btnModificarJogador, btnExcluirJogador, btnListarJogador, btnBuscarJogador;
    private TextView tvListarJogador;
    private Spinner spTimeJogador;
    private JogadorController jCont;
    private TimeController tCont;
    private List<Time> times;

    public JogadorFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_jogador, container, false);
        etIDJogador = view.findViewById(R.id.etIDJogador);
        etNomeJogador = view.findViewById(R.id.etNomeJogador);
        etDataNascJogador = view.findViewById(R.id.etDataNascJogador);
        etAlturaJogador = view.findViewById(R.id.etAlturaJogador);
        etPesoJogador = view.findViewById(R.id.etPesoJogador);
        btnInserirJogador = view.findViewById(R.id.btnInserirJogador);
        btnModificarJogador = view.findViewById(R.id.btnModificarJogador);
        btnExcluirJogador = view.findViewById(R.id.btnExcluirJogador);
        btnListarJogador = view.findViewById(R.id.btnListarJogador);
        btnBuscarJogador = view.findViewById(R.id.btnBuscarJogador);
        tvListarJogador = view.findViewById(R.id.tvListarJogador);
        tvListarJogador.setMovementMethod(new ScrollingMovementMethod());
        spTimeJogador = view.findViewById(R.id.spTimeJogador);

        jCont = new JogadorController(new JogadorDao(view.getContext()));
        tCont = new TimeController(new TimeDao(view.getContext()));

        preencheSpinner();

        btnInserirJogador.setOnClickListener(op -> acaoInserir());
        btnModificarJogador.setOnClickListener(op -> acaoModificar());
        btnExcluirJogador.setOnClickListener(op -> acaoExcluir());
        btnBuscarJogador.setOnClickListener(op -> acaoBuscar());
        btnListarJogador.setOnClickListener(op -> acaoListar());
        return view;
    }

    private void acaoInserir() {
        int spPos = spTimeJogador.getSelectedItemPosition();
        if(spPos > 0){
            Jogador jogador = montaJogador();
            try {
                jCont.inserir(jogador);
                Toast.makeText(view.getContext(), "Jogador contratado com sucesso", Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            limpaCampos();
        } else {
            Toast.makeText(view.getContext(), "Selecione um time", Toast.LENGTH_LONG).show();
        }
    }

    private void acaoModificar() {
        int spPos = spTimeJogador.getSelectedItemPosition();
        if(spPos > 0){
            Jogador jogador = montaJogador();
            try {
                jCont.modificar(jogador);
                Toast.makeText(view.getContext(), "Jogador atualizado com sucesso", Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            limpaCampos();
        } else {
            Toast.makeText(view.getContext(), "Selecione um time", Toast.LENGTH_LONG).show();
        }
    }

    private void acaoExcluir() {
        Jogador jogador = montaJogador();
        try {
            jCont.deletar(jogador);
            Toast.makeText(view.getContext(), "Jogador se aposentadou", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limpaCampos();
    }

    private void acaoBuscar() {
        Jogador jogador = montaJogador();
        try {
            times = tCont.listar();
            jogador = jCont.buscar(jogador);
            if(jogador.getNome() != null){
                preencheCampos(jogador);
            } else {
                Toast.makeText(view.getContext(), "Jogador fugiu da concentração e não foi encontrado", Toast.LENGTH_LONG).show();
                limpaCampos();
            }
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void acaoListar() {
        try {
            List<Jogador> jogadores = jCont.listar();
            StringBuffer buffer = new StringBuffer();
            for(Jogador j : jogadores){
                buffer.append(j.toString() + "\n");
            }
            tvListarJogador.setText(buffer.toString());
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void preencheSpinner() {
        Time t0 = new Time();
        t0.setCodigo(0);
        t0.setNome("Selecione um time");
        t0.setCidade("");

        try {
            List<Time> times = tCont.listar();
            times.add(0, t0);

            ArrayAdapter ad = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, times);
            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spTimeJogador.setAdapter(ad);
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Jogador montaJogador(){
        Jogador j = new Jogador();
        j.setId(Integer.parseInt(etIDJogador.getText().toString()));
        j.setNome(etNomeJogador.getText().toString());
        j.setDataNasc(etDataNascJogador.getText().toString());
        if (!etAlturaJogador.getText().toString().isEmpty()) {
            j.setAltura(Float.parseFloat(etAlturaJogador.getText().toString()));
        } else {
            j.setAltura(0.0f);
        }
        if (!etPesoJogador.getText().toString().isEmpty()) {
            j.setPeso(Float.parseFloat(etPesoJogador.getText().toString()));
        } else {
            j.setPeso(0.0f);
        }
        j.setTime((Time) spTimeJogador.getSelectedItem());

        return j;
    }

    private void limpaCampos(){
        etIDJogador.setText("");
        etNomeJogador.setText("");
        etDataNascJogador.setText("");
        etAlturaJogador.setText("");
        etPesoJogador.setText("");
        spTimeJogador.setSelection(0);
    }

    private void preencheCampos(Jogador j){
        etIDJogador.setText(String.valueOf(j.getId()));
        etNomeJogador.setText(j.getNome());
        etDataNascJogador.setText(j.getDataNasc());
        etAlturaJogador.setText(String.valueOf(j.getAltura()));
        etPesoJogador.setText(String.valueOf(j.getPeso()));

        int cont = 1;
        for(Time t : times){
            if(t.getCodigo() == j.getTime().getCodigo()){
                spTimeJogador.setSelection(cont);
            } else {
                cont++;
            }
        }
        if(cont > times.size()){
            spTimeJogador.setSelection(0);
        }
    }
}