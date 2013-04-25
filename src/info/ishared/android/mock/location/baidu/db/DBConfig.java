package info.ishared.android.mock.location.baidu.db;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 11-12-7
 * Time: 下午4:47
 */
public class DBConfig {
    public static final String DB_NAME = "mock_location_baidu.db";
    public static final int DB_VERSION = 0x00000001;

    public static class MockLatLng {

        public static final String TABLE_NAME = "mock_lat_lng";

        public static final String ID = "id";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String LOCATION_TYPE = "location_type";
        public static final String FAV_NAME = "fav_name";

        public static final String CREATE_MOCK_LAT_LNG_SQL = "create table " + TABLE_NAME
                + "("
                + ID + " integer primary key autoincrement, "
                + LATITUDE + " REAL, "
                + LONGITUDE + " REAL, "
                + LOCATION_TYPE + " TEXT, "
                + FAV_NAME + " TEXT "
                + ");";
    }
}
