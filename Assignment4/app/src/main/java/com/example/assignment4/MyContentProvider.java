package com.example.assignment4;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.multifragments.provider";
    private static final String BASE_PATH = "items";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private static final int ITEMS = 1;
    private static final int ITEM_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, ITEMS);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", ITEM_ID);
    }

    private DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor;

        switch (uriMatcher.match(uri)) {
            case ITEMS:
                cursor = db.query(DatabaseHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ITEM_ID:
                long id = ContentUris.parseId(uri);
                cursor = db.query(DatabaseHelper.TABLE_NAME, projection, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long id = db.insert(DatabaseHelper.TABLE_NAME, null, values);
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int rowsUpdated;

        switch (uriMatcher.match(uri)) {
            case ITEMS:
                rowsUpdated = db.update(DatabaseHelper.TABLE_NAME, values, selection, selectionArgs);
                break;
            case ITEM_ID:
                long id = ContentUris.parseId(uri);
                rowsUpdated = db.update(DatabaseHelper.TABLE_NAME, values, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int rowsDeleted;

        switch (uriMatcher.match(uri)) {
            case ITEMS:
                rowsDeleted = db.delete(DatabaseHelper.TABLE_NAME, selection, selectionArgs);
                break;
            case ITEM_ID:
                long id = ContentUris.parseId(uri);
                rowsDeleted = db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ITEMS:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + BASE_PATH;
            case ITEM_ID:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + BASE_PATH;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}