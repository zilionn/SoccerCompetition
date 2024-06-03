package br.edu.fateczl.soccercompetition.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GenericDao extends SQLiteOpenHelper {

    private static final String DATABASE = "SOCCER.DB";
    private static final int DATABASE_VER = 2;
    private static final String CREATE_TABLE_TEAM =
            "CREATE TABLE time ( " +
                    "id INT NOT NULL PRIMARY KEY, " +
                    "name VARCHAR(50) NOT NULL, " +
                    "city VARCHAR(80) NOT NULL);";
    private static final String CREATE_TABLE_PLAYER =
            "CREATE TABLE jogador ( " +
                    "id INT NOT NULL PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "birth VARCHAR(10) NOT NULL, " +
                    "hight DECIMAL(4, 2) NOT NULL, " +
                    "weight DECIMAL(4, 1) NOT NULL, " +
                    "id_team INT NOT NULL, " +
                    "FOREIGN KEY (id_team) REFERENCES team(id));";

    public GenericDao(Context context){
        super(context, DATABASE, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TEAM);
        db.execSQL(CREATE_TABLE_PLAYER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion){
            db.execSQL("DROP TABLE IF EXISTS jogador");
            db.execSQL("DROP TABLE IF EXISTS time");
            onCreate(db);
        }
    }
}
