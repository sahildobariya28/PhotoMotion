package photo.photomotion.motion.photomotionlivewallpaper.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.util.Log;

import photo.photomotion.motion.photomotionlivewallpaper.customView.controllersapp.Utils;
import photo.photomotion.motion.photomotionlivewallpaper.customView.beans.Point;
import photo.photomotion.motion.photomotionlivewallpaper.customView.beans.Project;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MotionInPhoto";
    private static final int DATABASE_VERSION = 2;
    private static DatabaseHandler db;

    public static DatabaseHandler getInstance(Context context) {
        if (db == null) {
            db = new DatabaseHandler(context);
        }
        return db;
    }

    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        Log.i("INFO", "CRIANDO BASE DE DADOS ....");
        sQLiteDatabase.execSQL(Project.CREATE_TABLE);
        sQLiteDatabase.execSQL(Point.CREATE_TABLE);
    }

    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        onUpgrade(sQLiteDatabase, i, i2);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append("ATUALIZANDO BASE DE DADOS DA VERSÃO ");
        sb.append(i);
        sb.append(" PARA A ");
        sb.append(i2);
        Log.i("INFO", sb.toString());
        sQLiteDatabase.execSQL(Point.DROP_TABLE);
        sQLiteDatabase.execSQL(Project.DROP_TABLE);
        onCreate(sQLiteDatabase);
    }

    public Project getProjeto(long j) {
        Project projeto;
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor query = readableDatabase.query(Project.TABLE, new String[]{"id", Project.COLUMN_DESCRICAO, Project.COLUMN_MASCARA, Project.COLUMN_URI, Project.COLUMN_RESOLUCAO, Project.COLUMN_TEMPO}, "id=?", new String[]{String.valueOf(j)}, null, null, "id", null);
        if (query != null ? query.moveToFirst() : false) {
            projeto = new Project((long) Integer.parseInt(query.getString(0)), query.getString(1), null, Uri.parse(query.getString(3)), query.getInt(4), query.getInt(5));
            if (query.getBlob(2) != null) {
                projeto.setMask(Utils.bytesToBitmap(query.getBlob(2)).copy(Config.ARGB_8888, true));
            }
            projeto.carregarPontos(this);
        } else {
            projeto = null;
        }
        query.close();
        readableDatabase.close();
        return projeto;
    }

    public Project getUltimoProjeto() {
        Project projeto;
        SQLiteDatabase readableDatabase = getReadableDatabase();
        SQLiteDatabase sQLiteDatabase = readableDatabase;
        Cursor query = sQLiteDatabase.query(Project.TABLE, new String[]{"id", Project.COLUMN_DESCRICAO, Project.COLUMN_MASCARA, Project.COLUMN_URI, Project.COLUMN_RESOLUCAO, Project.COLUMN_TEMPO}, "id=(SELECT MAX(id)  FROM tb_projeto)", null, null, null, null, null);
        if (query.moveToFirst()) {
            projeto = new Project((long) Integer.parseInt(query.getString(0)), query.getString(1), null, Uri.parse(query.getString(3)), query.getInt(4), query.getInt(5));
            projeto.setResolucaoSave(query.getInt(4));
            projeto.setTempoSave(query.getInt(5));
            if (query.getBlob(2) != null) {
                projeto.setMask(Utils.bytesToBitmap(query.getBlob(2)).copy(Config.ARGB_8888, true));
            }
            projeto.carregarPontos(this);
        } else {
            projeto = null;
        }
        query.close();
        readableDatabase.close();
        return projeto;
    }


}
