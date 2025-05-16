package com.seuapp.fichamedica.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.seuapp.fichamedica.model.Ficha;

import java.util.ArrayList;
import java.util.List;

public class FichaDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME  = "fichas.db";
    private static final int    DB_VER   = 1;
    public  static final String TBL_FICHAS = "fichas";

    // colunas
    public static final String COL_ID      = "id";
    public static final String COL_NOME    = "nome";
    public static final String COL_IDADE   = "idade";
    public static final String COL_PESO    = "peso";
    public static final String COL_ALTURA  = "altura";
    public static final String COL_PRESSAO = "pressao";

    public FichaDbHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = ""
                + "CREATE TABLE " + TBL_FICHAS + " ("
                + COL_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NOME    + " TEXT NOT NULL, "
                + COL_IDADE   + " INTEGER NOT NULL, "
                + COL_PESO    + " REAL NOT NULL, "
                + COL_ALTURA  + " REAL NOT NULL, "
                + COL_PRESSAO + " TEXT NOT NULL"
                + ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_FICHAS);
        onCreate(db);
    }

    // inserção
    public long insertFicha(Ficha f) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NOME,    f.getNome());
        cv.put(COL_IDADE,   f.getIdade());
        cv.put(COL_PESO,    f.getPeso());
        cv.put(COL_ALTURA,  f.getAltura());
        cv.put(COL_PRESSAO, f.getPressao());
        return db.insert(TBL_FICHAS, null, cv);
    }

    // buscar última ficha
    public Ficha getLastFicha() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TBL_FICHAS, null, null, null, null, null,
                COL_ID + " DESC", "1");
        if (!c.moveToFirst()) return null;
        Ficha f = new Ficha(
                c.getString(c.getColumnIndexOrThrow(COL_NOME)),
                c.getInt   (c.getColumnIndexOrThrow(COL_IDADE)),
                c.getString(c.getColumnIndexOrThrow(COL_PRESSAO)),
                c.getDouble(c.getColumnIndexOrThrow(COL_PESO)),
                c.getDouble(c.getColumnIndexOrThrow(COL_ALTURA))
        );
        f.setId(c.getInt(c.getColumnIndexOrThrow(COL_ID)));
        c.close();
        return f;
    }

    // buscar por ID
    public Ficha getFichaById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TBL_FICHAS, null,
                COL_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);
        if (!c.moveToFirst()) return null;
        Ficha f = new Ficha(
                c.getString(c.getColumnIndexOrThrow(COL_NOME)),
                c.getInt   (c.getColumnIndexOrThrow(COL_IDADE)),
                c.getString(c.getColumnIndexOrThrow(COL_PRESSAO)),
                c.getDouble(c.getColumnIndexOrThrow(COL_PESO)),
                c.getDouble(c.getColumnIndexOrThrow(COL_ALTURA))
        );
        f.setId(id);
        c.close();
        return f;
    }

    // listar todas
    public List<Ficha> getAllFichas() {
        List<Ficha> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TBL_FICHAS, null, null, null, null, null,
                COL_ID + " DESC");
        while(c.moveToNext()) {
            Ficha f = new Ficha(
                    c.getString(c.getColumnIndexOrThrow(COL_NOME)),
                    c.getInt   (c.getColumnIndexOrThrow(COL_IDADE)),
                    c.getString(c.getColumnIndexOrThrow(COL_PRESSAO)),
                    c.getDouble(c.getColumnIndexOrThrow(COL_PESO)),
                    c.getDouble(c.getColumnIndexOrThrow(COL_ALTURA))
            );
            f.setId(c.getInt(c.getColumnIndexOrThrow(COL_ID)));
            lista.add(f);
        }
        c.close();
        return lista;
    }
    public int updateFicha(Ficha f) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NOME,    f.getNome());
        cv.put(COL_IDADE,   f.getIdade());
        cv.put(COL_PESO,    f.getPeso());
        cv.put(COL_ALTURA,  f.getAltura());
        cv.put(COL_PRESSAO, f.getPressao());
        // atualiza pela chave primária
        return db.update(
                TBL_FICHAS,
                cv,
                COL_ID + "=?",
                new String[]{String.valueOf(f.getId())}
        );
    }

    // estatísticas
    public int getCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TBL_FICHAS, null);
        c.moveToFirst();
        int cnt = c.getInt(0);
        c.close();
        return cnt;
    }
    public double getAvgIdade() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT AVG(" + COL_IDADE + ") FROM " + TBL_FICHAS, null);
        c.moveToFirst();
        double v = c.getDouble(0);
        c.close();
        return v;
    }
    public double getAvgImc() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT AVG(" + COL_PESO + "/("+COL_ALTURA+"*"+COL_ALTURA+")) FROM " + TBL_FICHAS,
                null);
        c.moveToFirst();
        double v = c.getDouble(0);
        c.close();
        return v;
    }
}
