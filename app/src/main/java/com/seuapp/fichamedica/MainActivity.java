package com.seuapp.fichamedica;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.seuapp.fichamedica.database.FichaDbHelper;
import com.seuapp.fichamedica.model.Ficha;
import com.seuapp.fichamedica.view.HistoricoActivity;
import com.seuapp.fichamedica.view.VisualizacaoActivity;

public class MainActivity extends AppCompatActivity {

    private FichaDbHelper db;
    private EditText etNome, etIdade, etPeso, etAltura, etPressao;
    private Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1️⃣ Instancia helper e views
        db         = new FichaDbHelper(this);
        etNome     = findViewById(R.id.etNome);
        etIdade    = findViewById(R.id.etIdade);
        etPeso     = findViewById(R.id.etPeso);
        etAltura   = findViewById(R.id.etAltura);
        etPressao  = findViewById(R.id.etPressao);
        btnSalvar  = findViewById(R.id.btnSalvar);

        // 2️⃣ Detecta se veio um fichaId para edição
        Intent intent = getIntent();
        int fichaId   = intent.getIntExtra("fichaId", -1);
        if (fichaId != -1) {
            // Modo EDIÇÃO
            Ficha f = db.getFichaById(fichaId);
            if (f != null) {
                etNome    .setText(f.getNome());
                etIdade   .setText(String.valueOf(f.getIdade()));
                etPeso    .setText(String.valueOf(f.getPeso()));
                etAltura  .setText(String.valueOf(f.getAltura()));
                etPressao .setText(f.getPressao());
                btnSalvar .setText("Atualizar");

                btnSalvar.setOnClickListener(v -> {
                    // Atualiza o objeto com novos valores
                    f.setNome(etNome.getText().toString().trim());
                    f.setIdade(Integer.parseInt(etIdade.getText().toString()));
                    f.setPeso(Double.parseDouble(etPeso.getText().toString().replace(",", ".")));
                    f.setAltura(Double.parseDouble(etAltura.getText().toString().replace(",", ".")));
                    f.setPressao(etPressao.getText().toString().trim());

                    int rows = db.updateFicha(f);
                    if (rows > 0) {
                        Toast.makeText(this, "Atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Erro na atualização.", Toast.LENGTH_SHORT).show();
                    }
                    // Encerra para voltar à Visualização ou Histórico
                    finish();
                });
            }
        } else {
            // Modo NOVO registro
            btnSalvar.setOnClickListener(v -> {
                try {
                    String nome  = etNome.getText().toString().trim();
                    int    idade = Integer.parseInt(etIdade.getText().toString());
                    double peso  = Double.parseDouble(etPeso.getText().toString().replace(",", "."));
                    double alt   = Double.parseDouble(etAltura.getText().toString().replace(",", "."));
                    String press = etPressao.getText().toString().trim();

                    Ficha f = new Ficha(nome, idade, press, peso, alt);
                    long id = db.insertFicha(f);
                    if (id > 0) {
                        Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
                        // opcional: limpar campos ou fechar
                        etNome   .setText("");
                        etIdade  .setText("");
                        etPeso   .setText("");
                        etAltura .setText("");
                        etPressao.setText("");
                    } else {
                        Toast.makeText(this, "Erro ao salvar.", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // 3️⃣ Navegações
        findViewById(R.id.btnVizu).setOnClickListener(v ->
                startActivity(new Intent(this, VisualizacaoActivity.class))
        );
        findViewById(R.id.btnHist).setOnClickListener(v ->
                startActivity(new Intent(this, HistoricoActivity.class))
        );
    }
}
