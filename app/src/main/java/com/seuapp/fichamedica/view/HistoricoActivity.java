package com.seuapp.fichamedica.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.seuapp.fichamedica.R;
import com.seuapp.fichamedica.database.FichaDbHelper;
import com.seuapp.fichamedica.model.Ficha;

import java.util.List;

public class HistoricoActivity extends AppCompatActivity {

    private FichaDbHelper db;
    private ListView   lv;
    private Button     btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historico);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EdgeToEdge.enable(this);

        db       = new FichaDbHelper(this);
        lv       = findViewById(R.id.Lista);
        btnVoltar= findViewById(R.id.btnVoltar);

        List<Ficha> lista = db.getAllFichas();
        FichaAdapter adapter = new FichaAdapter(this, lista);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener((parent, view, position, id) -> {
            Ficha f = lista.get(position);
            startActivity(new Intent(this, VisualizacaoActivity.class)
                    .putExtra("fichaId", f.getId()));
        });

        btnVoltar.setOnClickListener(v -> finish());
    }
}
