package com.seuapp.fichamedica.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.seuapp.fichamedica.MainActivity;
import com.seuapp.fichamedica.R;
import com.seuapp.fichamedica.database.FichaDbHelper;
import com.seuapp.fichamedica.model.Ficha;
import com.seuapp.fichamedica.view.HistoricoActivity;

public class VisualizacaoActivity extends AppCompatActivity {

    private FichaDbHelper db;
    private TextView tvNome, tvIdade, tvPeso, tvAltura, tvPressao, tvImc, tvFaixa;
    private Button btnEditar, btnVoltarMain, btnHistorico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizacao);

        // 1️⃣ Instancia helper e views
        db             = new FichaDbHelper(this);
        tvNome         = findViewById(R.id.tvNome);
        tvIdade        = findViewById(R.id.tvIdade);
        tvPeso         = findViewById(R.id.tvPeso);
        tvAltura       = findViewById(R.id.tvAltura);
        tvPressao      = findViewById(R.id.tvPressao);
        tvImc          = findViewById(R.id.tvImc);
        tvFaixa        = findViewById(R.id.tvFaixa);
        btnEditar      = findViewById(R.id.btnEditar);
        btnVoltarMain  = findViewById(R.id.btnVoltarMain);
        btnHistorico   = findViewById(R.id.btnHistorico);

        // 2️⃣ Recupera o ID da ficha ou carrega a última
        int fichaId = getIntent().getIntExtra("fichaId", -1);
        Ficha ficha;
        if (fichaId != -1) {
            ficha = db.getFichaById(fichaId);
        } else {
            ficha = db.getLastFicha();
        }

        // 3️⃣ Se não houver ficha, sai
        if (ficha == null) {
            Toast.makeText(this, "Nenhuma ficha encontrada.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 4️⃣ Preenche a tela
        preencherFicha(ficha);

        // 5️⃣ Botão Editar
        btnEditar.setOnClickListener(v -> {
            Intent it = new Intent(this, MainActivity.class);
            it.putExtra("fichaId", ficha.getId());
            startActivity(it);
            finish();
        });

        // 6️⃣ Botão Voltar para Tela Principal
        btnVoltarMain.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        // 7️⃣ Botão Histórico
        btnHistorico.setOnClickListener(v -> {
            startActivity(new Intent(this, HistoricoActivity.class));
            finish();
        });
    }

    private void preencherFicha(Ficha f) {
        tvNome.setText("Nome: " + f.getNome());
        tvIdade.setText("Idade: " + f.getIdade());
        tvPeso.setText("Peso: " + f.getPeso());
        tvAltura.setText("Altura: " + f.getAltura());
        tvPressao.setText("Pressão: " + f.getPressao());

        double imc = f.getPeso() / (f.getAltura() * f.getAltura());
        tvImc.setText("IMC: " + String.format("%.1f", imc));

        String faixa;
        if (imc < 18.5)        faixa = "Abaixo do peso";
        else if (imc < 25)     faixa = "Normal";
        else if (imc < 30)     faixa = "Sobrepeso";
        else                   faixa = "Obesidade";
        tvFaixa.setText("Faixa: " + faixa);
    }
}
