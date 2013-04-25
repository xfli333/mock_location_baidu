package info.ishared.android.mock.location.baidu;

import android.content.Intent;
import android.provider.Settings;
import com.google.android.gms.maps.model.LatLng;
import info.ishared.android.mock.location.baidu.bean.LocationType;
import info.ishared.android.mock.location.baidu.bean.MockLatLng;
import info.ishared.android.mock.location.baidu.dao.MockLatLngDao;
import info.ishared.android.mock.location.baidu.service.MockLocationService;
import info.ishared.android.mock.location.baidu.util.AlertDialogUtils;
import info.ishared.android.mock.location.baidu.util.FormatUtils;
import info.ishared.android.mock.location.baidu.util.ToastUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Seven
 * Date: 13-4-11
 * Time: AM11:53
 */
public class MainController {

    private MainActivity mainActivity;


    private MockLatLngDao mockLatLngDao;

    public MainController(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        mockLatLngDao = new MockLatLngDao(mainActivity);
    }

    public void startMockLocation(LatLng latLng) {
        saveOrUpdateCurrentMockLocation(latLng);
        checkMockLocationAndStartMock(latLng);
    }

    private void checkMockLocationAndStartMock(LatLng latLng) {
        try {
            int i = Settings.Secure.getInt(mainActivity.getContentResolver(), "mock_location");
            if (i == 0) {
                AlertDialogUtils.showYesNoDiaLog(mainActivity, "需要打开允许模拟选项,是否去设置？", new AlertDialogUtils.Executor() {
                    @Override
                    public void execute() {
                        mainActivity.startActivity(new Intent().setClassName("com.android.settings", "com.android.settings.DevelopmentSettings"));
                    }
                });
            } else {
                mainActivity.startService(new Intent(mainActivity, MockLocationService.class).putExtra("latitude", latLng.latitude).putExtra("longitude", latLng.longitude));
                ToastUtils.showMessage(mainActivity, "正在模拟位置:" + FormatUtils.formatLatLngNumber(latLng.latitude) + "," + FormatUtils.formatLatLngNumber(latLng.longitude));
                mainActivity.finish();
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void stopMockLocationService() {
        Intent intent = new Intent(mainActivity, MockLocationService.class);
        mainActivity.stopService(intent);
    }

    public void deleteFavLocation(Long id){
        this.mockLatLngDao.deleteMockLocationById(id);
    }

    public void favCurrentLocation(String favName,LatLng latLng){
        MockLatLng mockLatLng = new MockLatLng();
        mockLatLng.setLatitude(latLng.latitude);
        mockLatLng.setLongitude(latLng.longitude);
        mockLatLng.setLocationType(LocationType.FAV.name());
        mockLatLng.setFavName(favName);
        this.mockLatLngDao.insertCurrentMockLocation(mockLatLng);
    }

    public List<MockLatLng> getAllFavLocation(){
        List<MockLatLng> mockLatLngList = this.mockLatLngDao.queryMockLatLngByType(LocationType.FAV);
        return mockLatLngList;
    }

    public LatLng getLastMockLocation() {
        List<MockLatLng> mockLatLngList = this.mockLatLngDao.queryMockLatLngByType(LocationType.LAST);
        if (!mockLatLngList.isEmpty()) {
            return new LatLng(mockLatLngList.get(0).getLatitude(), mockLatLngList.get(0).getLongitude());
        }
        return null;
    }


    private void saveOrUpdateCurrentMockLocation(LatLng latLng) {
        List<MockLatLng> mockLatLngList = this.mockLatLngDao.queryMockLatLngByType(LocationType.LAST);
        if (mockLatLngList.isEmpty()) {
            MockLatLng mockLatLng = new MockLatLng();
            mockLatLng.setLatitude(latLng.latitude);
            mockLatLng.setLongitude(latLng.longitude);
            mockLatLng.setLocationType(LocationType.LAST.name());
            mockLatLng.setFavName("");
            this.mockLatLngDao.insertCurrentMockLocation(mockLatLng);
        } else {
            MockLatLng mockLatLng = mockLatLngList.get(0);
            mockLatLng.setLatitude(latLng.latitude);
            mockLatLng.setLongitude(latLng.longitude);
            this.mockLatLngDao.updateCurrentMockLocation(mockLatLng);
        }

    }
}
