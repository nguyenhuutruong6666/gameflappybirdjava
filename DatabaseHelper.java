import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Tên cơ sở dữ liệu và bảng
    private static final String DATABASE_NAME = "flappybird.db";
    private static final String TABLE_NAME = "scores";
    
    // Cấu trúc bảng
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SCORE = "score";
    
    // Câu lệnh tạo bảng
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SCORE + " REAL);";
    
    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);  // Tạo bảng khi cơ sở dữ liệu được tạo
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Thêm điểm vào cơ sở dữ liệu
    public void addScore(float score) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COLUMN_SCORE + ") VALUES (" + score + ")");
    }

    // Lấy tổng điểm từ cơ sở dữ liệu
    public float getTotalScore() {
        SQLiteDatabase db = this.getReadableDatabase();
        float totalScore = 0;

        String query = "SELECT SUM(" + COLUMN_SCORE + ") FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            totalScore = cursor.getFloat(0);
        }
        cursor.close();
        return totalScore;
    }
}
