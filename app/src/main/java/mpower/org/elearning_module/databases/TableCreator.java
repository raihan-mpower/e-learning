package mpower.org.elearning_module.databases;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by sabbir on 11/13/17.
 */

public class TableCreator {


    /**
     * just give the table name and its propertires
     *    e.g  Person(id int,name text);
     * */
    public static void createTables(SQLiteDatabase db,List<String> sqls){
        for (String sql:sqls){
            db.execSQL("CREATE TABLE IF NOT EXISTS "+sql);
        }
    }

    public static void createTable(SQLiteDatabase db,String sql){

            db.execSQL("CREATE TABLE IF NOT EXISTS "+sql);
    }

    public static void deleteTables(SQLiteDatabase db,List<String> tables){
        for (String table:tables){
            db.execSQL("DROP TABLE IF EXISTS "+table);
        }
    }

    public static void deleteTable(SQLiteDatabase db,String tableName){
        db.execSQL("DROP TABLE IF EXISTS "+tableName);
    }
}
