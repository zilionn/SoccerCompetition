package br.edu.fateczl.soccercompetition.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.fateczl.soccercompetition.model.Jogador;
import br.edu.fateczl.soccercompetition.model.Time;

public class JogadorDao implements IJogadorDao, ICRUDDao<Jogador> {

    private final Context context;
    private GenericDao gDao;
    private SQLiteDatabase database;

    public JogadorDao(Context context){
        this.context = context;
    }

    @Override
    public JogadorDao open() throws SQLException {
        gDao = new GenericDao(context);
        database = gDao.getWritableDatabase();
        return this;
    }

    @Override
    public void close() {
        gDao.close();
    }

    @Override
    public void insert(Jogador jogador) throws SQLException {
        ContentValues contentValues = getContentValues(jogador);
        database.insert("jogador", null, contentValues);
    }

    @Override
    public int update(Jogador jogador) throws SQLException {
        ContentValues contentValues = getContentValues(jogador);
        int ret = database.update("jogador", contentValues, "id = " + jogador.getId(), null);
        return ret;
    }

    @Override
    public void delete(Jogador jogador) throws SQLException {
        database.delete("jogador", "id = " + jogador.getId(), null);
    }

    @SuppressLint("Range")
    @Override
    public Jogador findOne(Jogador jogador) throws SQLException {
        String sql = "SELECT t.codigo AS cod_time, t.nome AS nome_time, t.cidade AS cid_time, j.id, j.nome, j.data_nasc, j.altura, j.peso " +
                "FROM time t, jogador j " +
                "WHERE t.codigo = j.codigo_time AND j.id = " + jogador.getId();
        Cursor cursor = database.rawQuery(sql, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        if(!cursor.isAfterLast()){
            Time time = new Time();
            time.setCodigo(cursor.getInt(cursor.getColumnIndex("cod_time")));
            time.setNome(cursor.getString(cursor.getColumnIndex("nome_time")));
            time.setCidade(cursor.getString(cursor.getColumnIndex("cid_time")));

            jogador.setId(cursor.getInt(cursor.getColumnIndex("id")));
            jogador.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            jogador.setDataNasc(cursor.getString(cursor.getColumnIndex("data_nasc")));
            jogador.setAltura(Float.parseFloat(cursor.getString(cursor.getColumnIndex("altura"))));
            jogador.setPeso(Float.parseFloat(cursor.getString(cursor.getColumnIndex("peso"))));
            jogador.setTime(time);
        }
        cursor.close();
        return jogador;
    }

    @SuppressLint("Range")
    @Override
    public List<Jogador> findAll() throws SQLException {
        List<Jogador> jogadores = new ArrayList<>();
        String sql = "SELECT t.codigo AS cod_time, t.nome AS nome_time, t.cidade AS cid_time, j.id, j.nome, j.data_nasc, j.altura, j.peso " +
                "FROM time t, jogador j " +
                "WHERE t.codigo = j.codigo_time ";
        Cursor cursor = database.rawQuery(sql, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        while(!cursor.isAfterLast()){
            Jogador jogador = new Jogador();

            Time time = new Time();
            time.setCodigo(cursor.getInt(cursor.getColumnIndex("cod_time")));
            time.setNome(cursor.getString(cursor.getColumnIndex("nome_time")));
            time.setCidade(cursor.getString(cursor.getColumnIndex("cid_time")));

            jogador.setId(cursor.getInt(cursor.getColumnIndex("id")));
            jogador.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            jogador.setDataNasc(cursor.getString(cursor.getColumnIndex("data_nasc")));
            jogador.setAltura(Float.parseFloat(cursor.getString(cursor.getColumnIndex("altura"))));
            jogador.setPeso(Float.parseFloat(cursor.getString(cursor.getColumnIndex("peso"))));
            jogador.setTime(time);

            jogadores.add(jogador);
            cursor.moveToNext();
        }
        cursor.close();
        return jogadores;
    }

    private static ContentValues getContentValues(Jogador jogador){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", jogador.getId());
        contentValues.put("nome", jogador.getNome());
        contentValues.put("data_nasc", jogador.getDataNasc());
        contentValues.put("altura", jogador.getAltura());
        contentValues.put("peso", jogador.getPeso());
        contentValues.put("codigo_time", jogador.getTime().getCodigo());
        return contentValues;
    }
}
