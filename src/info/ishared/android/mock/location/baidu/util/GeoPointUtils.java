package info.ishared.android.mock.location.baidu.util;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 13-4-25
 * Time: 下午7:47
 */
public class GeoPointUtils {

    public static GeoPoint convertLatLngToGeoPoint(LatLng latLng){
        return  new GeoPoint((int)(latLng.latitude* 1E6),(int)(latLng.longitude* 1E6));
    }

    public static LatLng convertGeoPointToLatLng(GeoPoint geoPoint){
        LatLng latLng = new LatLng();
        latLng.longitude = geoPoint.getLongitudeE6()/1E6;
        latLng.latitude = geoPoint.getLatitudeE6()/1E6;

        return  latLng;
    }
}
