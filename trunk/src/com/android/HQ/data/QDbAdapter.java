package com.android.HQ.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class QDbAdapter {


    public static final String INDEX = "ind";
    public static final String AYAS = "ayas";
    public static final String NAME = "name";
    public static final String TNAME = "tname";
    public static final String REV_ORDER = "rev_order";

    private static final String TAG = "QDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table q_metadata (_id integer primary key, ind integer not null,"
                    + "ayas text not null, name text not null, tname text not null, rev_order integer not null);";
    
    private static final String Q_Text_Create =
        "create table q_text (_id integer primary key autoincrement, sura_num integer not null,"
                + "aya_num int not null, verse_txt text not null);";
    //private static final String DATABASE_CREATE = "create table q_metadata(_id integer primary key autoincrement, ind integer not null, tname text not null);";
    private static final String DATABASE_NAME = "hq_db";
    private static final String DATABASE_TABLE = "q_metadata";
    private static final String SURA_TBL = "q_text";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
            db.execSQL(Q_Text_Create);                                      
        }
       
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public QDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public QDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
        mDbHelper.close();
        mDb.close();
    }
    public void insert(int index, int ayas, String name, String tname, int order){
    	
    	ContentValues initialValues = new ContentValues();
        initialValues.put("ind", index);
        initialValues.put("ayas", ayas);
        initialValues.put("name", name);
        initialValues.put("tname", tname);
        initialValues.put("rev_order", order);
    	mDb.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public void insertSura(int suraNum, int ayaNum, String verse){
    	ContentValues initialValues = new ContentValues();
    	initialValues.put("sura_num", suraNum);
    	initialValues.put("aya_num", ayaNum);
    	initialValues.put("verse_txt", verse);
    	mDb.insert(SURA_TBL, null, initialValues);
    }
    
    public boolean deleteAll() {
		return (mDb.delete(DATABASE_TABLE,null, null) > 0);
	}
    
    public void deleteSura(int suraNum)
    {
    	mDb.delete("q_text", "sura_num=?", new String[]{Integer.toString(suraNum)});
    }

   /**
     * Return a Cursor over the list of all notes in the database
     * 
     * @return Cursor over all notes
     */
    public Cursor fetchAllMetadata() {
    	
    	return  mDb.query(DATABASE_TABLE, null, null, null, null, null, null);
   	}
    
    public boolean isSuraStored(String suraNum){
    	Cursor c = mDb.query(SURA_TBL, null, "sura_num=?", new String[]{suraNum}, null, null, null);
    	if (c.getCount() <= 0)
    		return false;
    	else
    		return true;
    }
    
    public Cursor fetchSura(String suraNum){
    	return mDb.query(SURA_TBL, null, "sura_num=?", new String[]{suraNum}, null, null, null);    	
    }
    
    public int suraCount() {
    	Cursor c = mDb.query(DATABASE_TABLE, null, null, null, null, null, null);
    	int count = c.getCount();
    	c.close();
    	return count;
    }
}
  