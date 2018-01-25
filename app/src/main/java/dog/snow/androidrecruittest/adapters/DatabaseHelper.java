package dog.snow.androidrecruittest.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;

import dog.snow.androidrecruittest.Models.DataModel;

/**
 * Created by Rohail on 1/24/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "resruitment";

    // Table Names
    private static final String TABLE_DATA = "data";

    // column names
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_ID = "id";
    private static final String KEY_ICON = "icon";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_NAME = "name";
    private static final String KEY_URL = "url";

    //DATA Table
    private static final String CREATE_TABLE_DATA = "CREATE TABLE "
            + TABLE_DATA + " (" + KEY_ID + " TEXT, " + KEY_TIMESTAMP
            + " TEXT, " + KEY_ICON + " TEXT, " + KEY_DESCRIPTION + " TEXT, " + KEY_NAME + " TEXT, " + KEY_URL + " TEXT" + ")";

    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);

        // create new tables
        onCreate(db);
    }

    /**
     * INSERT*******************************************************************
     * *
     */
    public void insertData(DataModel trx) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_TIMESTAMP, trx.getTimestamp() + "");
        values.put(KEY_ID, trx.getId());
        values.put(KEY_ICON, trx.getIcon() + "");
        values.put(KEY_DESCRIPTION, trx.getDescription() + "");
        values.put(KEY_NAME, trx.getName() + "");
        values.put(KEY_URL, trx.getUrl() + "");

        // Inserting Row
        db.insert(TABLE_DATA, null, values);
    }

    public void insertAllData(ArrayList<DataModel> trxList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        for (DataModel trx : trxList) {

            values.put(KEY_TIMESTAMP, trx.getTimestamp() + "");
            values.put(KEY_ID, trx.getId());
            values.put(KEY_ICON, trx.getIcon() + "");
            values.put(KEY_DESCRIPTION, trx.getDescription() + "");
            values.put(KEY_NAME, trx.getName() + "");
            values.put(KEY_URL, trx.getUrl() + "");

            // Inserting Row
            db.insert(TABLE_DATA, null, values);

        }
    }

    /**
     * SELECT*******************************************************************
     * *
     */

    public ArrayList<DataModel> selectAllData() throws ParseException {

        ArrayList<DataModel> transactionsList = new ArrayList<DataModel>();

        String selectQuery = "SELECT * FROM " + TABLE_DATA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataModel trx = new DataModel();

                trx.setTimestamp(cursor.getString(0));
                trx.setId(cursor.getString(1));
                trx.setIcon(cursor.getString(2));
                trx.setDescription(cursor.getString(3));
                trx.setName(cursor.getString(4));
                trx.setUrl(cursor.getString(5));
                // Adding Transaction
                transactionsList.add(trx);
            } while (cursor.moveToNext());
        }
        return transactionsList;
    }

    /**
     * DROP table
     */

    public void dropDataTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);

        // create new tables
        onCreate(db);
    }

}
