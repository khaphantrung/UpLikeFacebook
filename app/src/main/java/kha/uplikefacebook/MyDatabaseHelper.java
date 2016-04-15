package kha.uplikefacebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kha on 4/5/2016.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLite";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UserFacebook_Manager";
    private static final String TABLE_USER = "UserFacebook";
    private static final String COLUMN_USER_ID ="User_Id";
    private static final String COLUMN_USER_ACCESSTOKEN = "User_AccessToken";
    private static final String COLUMN_USER_APP_ID = "User_AppId";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");

        String script = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_USER_ID + " TEXT PRIMARY KEY,"
                + COLUMN_USER_APP_ID + " TEXT,"
                + COLUMN_USER_ACCESSTOKEN + " TEXT" + ")";

        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }
    public void addUser(UserFacebook user) {
        Log.i(TAG, "MyDatabaseHelper.addNote ... " + user.getUserId());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, user.getUserId());
        values.put(COLUMN_USER_APP_ID, user.getmAppID());
        values.put(COLUMN_USER_ACCESSTOKEN, user.getAccessToken());

        db.insert(TABLE_USER, null, values);
        db.close();
    }
    public List<UserFacebook> getAllUserFacebook() {
        Log.i(TAG, "MyDatabaseHelper.getAllNotes ... " );

        List<UserFacebook> userList = new ArrayList<UserFacebook>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                UserFacebook userFacebook = new UserFacebook();
                userFacebook.setUserId(cursor.getString(0));
                userFacebook.setmAppID(cursor.getString(1));
                userFacebook.setAccessToken(cursor.getString(2));

                userList.add(userFacebook);
            } while (cursor.moveToNext());
        }

        // return note list

        return userList;
    }
    public boolean isExistUserFacebook(String userId){
        List<UserFacebook> userList = getAllUserFacebook();
        for (UserFacebook user : userList) {
            if(user.getUserId().equals(userId))
                return true;
        }
        return false;
    }
    public int updateUser(UserFacebook user) {
        Log.i(TAG, "MyDatabaseHelper.updateNote ... "  + user.getUserId());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, user.getUserId());
        values.put(COLUMN_USER_APP_ID, user.getmAppID());
        values.put(COLUMN_USER_ACCESSTOKEN, user.getAccessToken());
        // updating row
        return db.update(TABLE_USER, values, COLUMN_USER_ID+ " = ?",
                new String[]{String.valueOf(user.getUserId())});
    }
}
