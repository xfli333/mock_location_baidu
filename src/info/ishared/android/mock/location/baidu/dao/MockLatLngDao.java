package info.ishared.android.mock.location.baidu.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import info.ishared.android.mock.location.baidu.bean.LocationType;
import info.ishared.android.mock.location.baidu.bean.MockLatLng;
import info.ishared.android.mock.location.baidu.db.DBConfig;
import info.ishared.android.mock.location.baidu.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 13-4-25
 * Time: 下午7:28
 */
public class MockLatLngDao {
    private DBHelper mDBHelper;

    public MockLatLngDao(Context context) {
        this.mDBHelper = DBHelper.getInstance(context);
    }

    public void insertCurrentMockLocation(MockLatLng mockLatLng) {
        this.mDBHelper.open();
        ContentValues values = new ContentValues();
        values.put(DBConfig.MockLatLng.LATITUDE, mockLatLng.getLatitude());
        values.put(DBConfig.MockLatLng.LONGITUDE, mockLatLng.getLongitude());
        values.put(DBConfig.MockLatLng.LOCATION_TYPE, mockLatLng.getLocationType());
        values.put(DBConfig.MockLatLng.FAV_NAME, mockLatLng.getFavName());

        this.mDBHelper.getMDB().insertOrThrow(DBConfig.MockLatLng.TABLE_NAME, null, values);
        this.mDBHelper.close();
    }

    public MockLatLng getMockLatLngById(Long id) {
        String whereClause = DBConfig.MockLatLng.ID + " = " + id;
        List<MockLatLng> transferNumberList = this.queryMockLatLng(whereClause);
        if (!transferNumberList.isEmpty()) {
            return transferNumberList.get(0);
        }
        return null;
    }

    public void updateCurrentMockLocation(MockLatLng mockLatLng) {
        MockLatLng existMock = this.getMockLatLngById(mockLatLng.getId());
        if (existMock != null) {
            this.mDBHelper.open();
            ContentValues values = new ContentValues();
            values.put(DBConfig.MockLatLng.LATITUDE, mockLatLng.getLatitude());
            values.put(DBConfig.MockLatLng.LONGITUDE, mockLatLng.getLongitude());
            StringBuilder sb = new StringBuilder();
            sb.append(DBConfig.MockLatLng.ID + " = " + mockLatLng.getId());
            this.mDBHelper.getMDB().update(DBConfig.MockLatLng.TABLE_NAME, values, sb.toString(), null);
            this.mDBHelper.close();
        }
    }

    public List<MockLatLng> queryMockLatLngByType(LocationType type) {
        String whereClause = DBConfig.MockLatLng.LOCATION_TYPE + " = '" + type.name() + "'";
        List<MockLatLng> transferNumberList = this.queryMockLatLng(whereClause);
        return transferNumberList;
    }

    private List<MockLatLng> queryMockLatLng(String whereClause) {
        this.mDBHelper.open();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.appendWhere(whereClause);
        builder.setTables(DBConfig.MockLatLng.TABLE_NAME);
        String arrColumn[] = {
                DBConfig.MockLatLng.ID, DBConfig.MockLatLng.LATITUDE, DBConfig.MockLatLng.LONGITUDE, DBConfig.MockLatLng.LOCATION_TYPE, DBConfig.MockLatLng.FAV_NAME
        };
        Cursor c = builder.query(this.mDBHelper.getMDB(), arrColumn, null, null, null, null, null);
        c.moveToFirst();
        List<MockLatLng> data = new ArrayList<MockLatLng>();
        while (!c.isAfterLast()) {
            MockLatLng item = new MockLatLng();
            item.setId(c.getLong(c.getColumnIndex(arrColumn[0])));
            item.setLatitude(c.getDouble(c.getColumnIndex(arrColumn[1])));
            item.setLongitude(c.getDouble(c.getColumnIndex(arrColumn[2])));
            item.setLocationType(c.getString(c.getColumnIndex(arrColumn[3])));
            item.setFavName(c.getString(c.getColumnIndex(arrColumn[4])));
            data.add(item);
            c.moveToNext();
        }
        c.close();
        this.mDBHelper.close();
        return data;

    }


    public void deleteMockLocationById(Long id){
        this.mDBHelper.open();
        String whereClause=DBConfig.MockLatLng.ID +" = "+id+"";
        this.mDBHelper.getMDB().delete(DBConfig.MockLatLng.TABLE_NAME,whereClause,null);
        this.mDBHelper.close();
    }
}
