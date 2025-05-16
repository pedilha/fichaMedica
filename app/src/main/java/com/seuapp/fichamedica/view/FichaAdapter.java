package com.seuapp.fichamedica.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.seuapp.fichamedica.R;
import com.seuapp.fichamedica.model.Ficha;

import java.util.List;

public class FichaAdapter extends ArrayAdapter<Ficha> {

    public FichaAdapter(Context context, List<Ficha> fichas) {
        super(context, 0, fichas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.layout_lista, parent, false);
        }

        Ficha f = getItem(position);

        // Preenche cada campo do item
        ((TextView) convertView.findViewById(R.id.tvId))
                .setText(String.valueOf("Ficha "+f.getId()));
        ((TextView) convertView.findViewById(R.id.tvNome))
                .setText("Nome: "+f.getNome());
        ((TextView) convertView.findViewById(R.id.tvIdade))
                .setText(String.valueOf("Idade: "+f.getIdade()));
        ((TextView) convertView.findViewById(R.id.tvAltura))
                .setText(String.valueOf("Altura "+f.getAltura()));
        ((TextView) convertView.findViewById(R.id.tvPeso))
                .setText(String.valueOf("Peso "+f.getPeso()));

        double imc = f.getPeso() / (f.getAltura() * f.getAltura());
        ((TextView) convertView.findViewById(R.id.tvImc))
                .setText(String.format("IMC: "+"%.1f", imc));

        ((TextView) convertView.findViewById(R.id.tvPressao))
                .setText("Pressão Arterial: "+f.getPressao());

        return convertView;
    }
}
