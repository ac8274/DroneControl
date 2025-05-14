package com.example.dronecontrol;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.BaseColumns;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;

public class GpxProvider extends ContentProvider {

    private static final String TAG = "GpxProvider";

    // Authority is a unique name for your content provider
    public static final String AUTHORITY = "com.example.dronecontrol.gpxprovider";

    // Define the base URI for requesting a specific GPX file
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/gpx_file");

    // MIME type for a single GPX file
    private static final String GPX_ITEM_MIME_TYPE = "application/gpx+xml"; // More specific MIME type

    // Constants for the UriMatcher
    private static final int GPX_FILE = 200;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, "gpx_file/*", GPX_FILE); // Match "content://.../gpx_file/path/to/file.gpx"
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case GPX_FILE:
                return GPX_ITEM_MIME_TYPE;
            default:
                return null;
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final int match = sUriMatcher.match(uri);
        MatrixCursor cursor = null;

        switch (match) {
            case GPX_FILE:
                // Extract the file path from the URI
                String filePath = uri.getLastPathSegment();
                if (filePath != null) {
                    File file = new File(filePath);
                    if (file.exists() && file.getName().toLowerCase().endsWith(".gpx")) {
                        cursor = new MatrixCursor(new String[]{BaseColumns._ID, "_display_name", "_size", "file_path_uri"});
                        Uri fileUri = Uri.fromFile(file);
                        cursor.addRow(new Object[]{1, file.getName(), file.length(), fileUri.toString()});
                    } else {
                        Log.w(TAG, "GPX file not found or invalid path: " + filePath);
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        final int match = sUriMatcher.match(uri);
        if (match == GPX_FILE) {
            String filePath = uri.getLastPathSegment();
            if (filePath != null) {
                File file = new File(filePath);
                if (file.exists() && file.getName().toLowerCase().endsWith(".gpx")) {
                    return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
                }
            }
            throw new FileNotFoundException("Could not open file for URI: " + uri);
        }
        throw new FileNotFoundException("Invalid URI for opening file: " + uri);
    }
}