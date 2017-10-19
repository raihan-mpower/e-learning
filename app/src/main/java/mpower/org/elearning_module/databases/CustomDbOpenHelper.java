package mpower.org.elearning_module.databases;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;

import java.io.File;

import mpower.org.elearning_module.R;
import mpower.org.elearning_module.application.ELearningApp;

/**
 * Created by sabbir on 10/19/17.
 */

public abstract class CustomDbOpenHelper {

    private final String path;
    private final String name;
    private final SQLiteDatabase.CursorFactory factory;
    private final int newVersion;

    private SQLiteDatabase database = null;
    private boolean isInitializing = false;

   public CustomDbOpenHelper(String path, String name, SQLiteDatabase.CursorFactory factory, int version){
        if (version < 1) {
            throw new IllegalArgumentException("Version must be >= 1, was " + version);
        }

        this.path = path;
        this.name = name;
        this.factory = factory;
        newVersion = version;
    }

    public synchronized SQLiteDatabase getWritableDatabase() {
        if (database != null && database.isOpen() && !database.isReadOnly()) {
            return database; // The database is already open for business
        }

        if (isInitializing) {
            throw new IllegalStateException("getWritableDatabase called recursively");
        }

        // If we have a read-only database open, someone could be using it
        // (though they shouldn't), which would cause a lock to be held on
        // the file, and our attempts to open the database read-write would
        // fail waiting for the file lock. To prevent that, we acquire the
        // lock on the read-only database, which shuts out other users.

        boolean success = false;
        SQLiteDatabase db = null;
        // if (database != null) database.lock();
        try {
            isInitializing = true;
            if (name == null) {
                db = SQLiteDatabase.create(null);
            } else {
                db = SQLiteDatabase.openOrCreateDatabase(path + File.separator + name, factory);
                // db = mContext.openOrCreateDatabase(name, 0, factory);
            }

            int version = db.getVersion();
            if (version < newVersion) {
                db.beginTransaction();
                try {
                    if (version == 0) {
                        onCreate(db);
                    } else {
                        onUpgrade(db, version, newVersion);
                    }
                    db.setVersion(newVersion);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }

            onOpen(db);
            success = true;
            return db;
        } finally {
            isInitializing = false;
            if (success) {
                if (database != null) {
                    try {
                        database.close();
                    } catch (Exception e) {

                    }
                    // database.unlock();
                }
                database = db;
            } else {
                // if (database != null) database.unlock();
                if (db != null) {
                    db.close();
                }
            }
        }
    }


    /**
     * Create and/or open a database. This will be the same object returned by
     * {@link #getWritableDatabase} unless some problem, such as a full disk, requires the database
     * to be opened read-only. In that case, a read-only database object will be returned. If the
     * problem is fixed, a future call to {@link #getWritableDatabase} may succeed, in which case
     * the read-only database object will be closed and the read/write object will be returned in
     * the future.
     *
     * @throws SQLiteException if the database cannot be opened
     * @return a database object valid until {@link #getWritableDatabase} or {@link #close} is
     *         called.
     */
    @SuppressLint("StringFormatInvalid")
    public synchronized SQLiteDatabase getReadableDatabase() {
        if (database != null && database.isOpen()) {
            return database; // The database is already open for business
        }

        if (isInitializing) {
            throw new IllegalStateException("getReadableDatabase called recursively");
        }

        try {
            return getWritableDatabase();
        } catch (SQLiteException e) {
            if (name == null) {
                throw e; // Can't open a temp database read-only!
            }

        }

        SQLiteDatabase db = null;
        try {
            isInitializing = true;
            String path = this.path + File.separator + name;
            // mContext.getDatabasePath(name).getPath();
            try {
                db = SQLiteDatabase.openDatabase(path, factory, SQLiteDatabase.OPEN_READONLY);
            } catch (RuntimeException e) {

                String cardstatus = Environment.getExternalStorageState();
                if (!cardstatus.equals(Environment.MEDIA_MOUNTED)) {
                    throw new RuntimeException(
                            ELearningApp.getInstance().getString(R.string.sdcard_unmounted, cardstatus));
                } else {
                    throw e;
                }
            }

            if (db.getVersion() != newVersion) {
                throw new SQLiteException("Can't upgrade read-only database from version "
                        + db.getVersion() + " to " + newVersion + ": " + path);
            }

            onOpen(db);

            database = db;
            return database;
        } finally {
            isInitializing = false;
            if (db != null && db != database) {
                db.close();
            }
        }
    }


    /**
     * Close any open database object.
     */
    public synchronized void close() {
        if (isInitializing) {
            throw new IllegalStateException("Closed during initialization");
        }

        if (database != null && database.isOpen()) {
            database.close();
            database = null;
        }
    }


    /**
     * Called when the database is created for the first time. This is where the creation of tables
     * and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    public abstract void onCreate(SQLiteDatabase db);


    /**
     * Called when the database needs to be upgraded. The implementation should use this method to
     * drop tables, add tables, or do anything else it needs to upgrade to the new schema version.
     * <p>
     * The SQLite ALTER TABLE documentation can be found <a
     * href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns you can use
     * ALTER TABLE to insert them into a live table. If you rename or remove columns you can use
     * ALTER TABLE to rename the old table, then create the new table and then populate the new
     * table with the contents of the old table.
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);


    /**
     * Called when the database has been opened. Override method should check
     * {@link SQLiteDatabase#isReadOnly} before updating the database.
     *
     * @param db The database.
     */
    public void onOpen(SQLiteDatabase db) {
    }

}
