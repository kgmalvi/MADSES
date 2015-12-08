package com.android.mads.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Administrator on 06-12-2015.
 */
public class SQLiteHandler extends SQLiteOpenHelper {

    public static final String TAG = SQLiteHandler.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "login.db";
    private static final String TABLE_NAME = "users";

    private static final String KEY_ID = "id";                                                      //CHANGES
    private static final String KEY_UID = "uid";
    private static final String KEY_FNAME = "fname";
    private static final String KEY_LNAME = "lname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_DOB = "dob";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_BLOODGROUP = "blodgroup";
    private static final String KEY_CONTACT1 = "contact1";
    private static final String KEY_CONTACT2 = "contact2";
    private static final String KEY_VECHILENAME = "vechiclename";
    private static final String KEY_FUELTYPE = "fueltype";
    private static final String KEY_MILEAGE = "mileage";
    private static final String KEY_CREATED_AT = "created_at";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {                                                        //CHANGES
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMAEY KEY, " + KEY_FNAME
                + " TEXT, " + KEY_LNAME + " TEXT," + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_DOB + " TEXT," + KEY_ADDRESS + " TEXT," + KEY_BLOODGROUP
                + " TEXT," + KEY_CONTACT1 + " TEXT," + KEY_CONTACT2 + " TEXT NULL,"
                + KEY_VECHILENAME + " TEXT," + KEY_FUELTYPE + " TEXT," + KEY_MILEAGE
                + " TEXT" + KEY_UID + " TEXT," + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);

        Log.d(TAG, " DATABASE TABLE CREATED");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    public void addUser(String uid, String fname, String lname, String email, String dob,
                        String address, String bloodgroup, String contact1, String contact2,
                        String vechiclename, String fueltype, String mileage, String created_at) {                  //CHANGES
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_UID, uid);
        values.put(KEY_FNAME, fname);
        values.put(KEY_LNAME, lname);                                                                //CHANGES
        values.put(KEY_EMAIL, email);
        values.put(KEY_DOB, dob);
        values.put(KEY_ADDRESS, address);
        values.put(KEY_BLOODGROUP, bloodgroup);
        values.put(KEY_CONTACT1, contact1);
        values.put(KEY_CONTACT2, contact2);
        values.put(KEY_VECHILENAME, vechiclename);
        values.put(KEY_FUELTYPE, fueltype);
        values.put(KEY_MILEAGE, mileage);
        values.put(KEY_CREATED_AT, created_at);

        long id = db.insert(TABLE_NAME, null, values);
        db.close();

        Log.d(TAG, "NEW USER ADDED" + id);

    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> temp = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            temp.put("uid", cursor.getColumnName(1));                                              //CHANGES
            temp.put("fname", cursor.getColumnName(2));
            temp.put("lname", cursor.getColumnName(3));
            temp.put("email", cursor.getColumnName(4));
            temp.put("dob", cursor.getColumnName(5));
            temp.put("address", cursor.getColumnName(6));
            temp.put("bloodgroup", cursor.getColumnName(7));
            temp.put("contact1", cursor.getColumnName(8));
            temp.put("contact2", cursor.getColumnName(9));
            temp.put("vechiclename", cursor.getColumnName(10));
            temp.put("fueltype", cursor.getColumnName(11));
            temp.put("mileage", cursor.getColumnName(12));
            temp.put("created_at", cursor.getColumnName(13));

        }
        cursor.close();
        db.close();

        Log.d(TAG, "Fetching user info");
        return temp;
    }

    public int getRowCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery, null);
        int rowcount = c.getCount();

        db.close();
        c.close();
        return rowcount;

    }

    public void deleteUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();

        Log.d(TAG, "User deleted");
    }
}
