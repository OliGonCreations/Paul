package com.oligon.paul;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "data";
    private static final String TABLE_INHALT = "inhalt";

    private static final String KEY_ID = "id";
    private static final String KEY_SPRUCH = "spruch";
    private static final String KEY_LAND = "land";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        // String CREATE_SPRUECHE = "CREATE TABLE "+TABLE_INHALT+" ("+KEY_ID
        // +" INTEGER PRIMARY KEY,"+KEY_SPRUCH+" TEXT,"+KEY_LAND+" TEXT)";
        String CREATE_SPRUECHE = "CREATE TABLE inhalt (id INTEGER PRIMARY KEY,spruch TEXT,land TEXT)";
        db.execSQL(CREATE_SPRUECHE);
        setDefaultLabel(db);
    }

    public void setDefaultLabel(SQLiteDatabase db) {
        String[][] array = MainActivity.loadArray();
        for (int i = 0; i < array.length; i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_SPRUCH, array[i][0]);
            values.put(KEY_LAND, array[i][1]);

            db.insert(TABLE_INHALT, null, values);

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INHALT);

        onCreate(db);

    }

    // Spruch hinzufügen
    public void addSpruch(Spruch spruch) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SPRUCH, spruch.getSpruch());
        values.put(KEY_LAND, spruch.getLand());

        db.insert(TABLE_INHALT, null, values);
        db.close();

    }

    // Spruch nach id bekommen
    public Spruch getSpruch(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_INHALT, new String[]{KEY_ID,
                KEY_SPRUCH, KEY_LAND}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Spruch spruch = new Spruch(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));

        return spruch;
    }

    // Alle Sprüche zurück bekommen
    public List<Spruch> getAllContacts() {
        List<Spruch> spruchListe = new ArrayList<Spruch>();
        String selectQuery = "SELECT * FROM " + TABLE_INHALT;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Spruch spruch = new Spruch();
                spruch.setID(Integer.parseInt(cursor.getString(0)));
                spruch.setSpruch(cursor.getString(1));
                spruch.setLand(cursor.getString(2));

                spruchListe.add(spruch);
            } while (cursor.moveToNext());
        }
        return spruchListe;
    }

    // Anzahl der Sprüche
    public int getSpruchCount() {

        String countQuery = "SELECT * FROM " + TABLE_INHALT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int res = cursor.getCount();
        cursor.close();

        return res;
    }

    public int updateSpruch(Spruch spruch) {

        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SPRUCH, spruch.getSpruch());
        values.put(KEY_LAND, spruch.getLand());

        return db.update(TABLE_INHALT, values, KEY_ID + " = ?",
                new String[]{String.valueOf(spruch.getID())});
    }

    public void deleteSpruch(Spruch spruch) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_INHALT, KEY_ID + " = ?",
                new String[]{String.valueOf(spruch.getID())});
        db.close();

    }

}
