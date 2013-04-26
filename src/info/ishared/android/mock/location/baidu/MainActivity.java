package info.ishared.android.mock.location.baidu;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.Projection;
import com.google.android.gms.maps.model.LatLng;
import info.ishared.android.mock.location.baidu.util.AlertDialogUtils;
import info.ishared.android.mock.location.baidu.util.FormatUtils;
import info.ishared.android.mock.location.baidu.util.GeoPointUtils;
import info.ishared.android.mock.location.baidu.util.ToastUtils;

public class MainActivity extends Activity implements View.OnClickListener {
    private ImageButton mRunBtn;
    private ImageButton mMenuBtn;

    BMapManager mBMapMan = null;
    MapView mMapView = null;
    MapController mMapController;
    private Handler mHandler;

    PopupWindow mPopupWindow;

    private ListView listView;

    private String title[] = {"停止模拟", "收藏位置", "查看收藏", "帮助说明", "退出程序"};
    LayoutInflater layoutInflater;

    MainController mainController;

    private LatLng defaultLatLng;

    private FavDialog mFavDialog;
    private HelpDialog mHelpDialog;
    private LatLng currentLocation;
    private ItemizedOverlay<OverlayItem> itemItemizedOverlay;
    private OverlayItem currentOverlayItem;



    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if ((int) event.getY() > 110) {
                    Projection projection = mMapView.getProjection();
                    GeoPoint loc = projection.fromPixels((int) event.getX(), (int) event.getY() - 100);
                    itemItemizedOverlay.removeAll();
                    mMapView.getOverlays().remove(itemItemizedOverlay);
                    currentOverlayItem = new OverlayItem(loc, "title", "location");
                    itemItemizedOverlay.addItem(currentOverlayItem);
                    currentLocation = GeoPointUtils.convertGeoPointToLatLng(loc);
                    mMapView.getOverlays().add(itemItemizedOverlay);
                    mMapView.refresh();
                }
            }
        });

        return false;    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBMapMan = new BMapManager(getApplication());
        mBMapMan.init("95ECA0FFB3FEF3DB9ACBCEC8AF5DE12ADC4F4CDE", null);
        setContentView(R.layout.main);

        mRunBtn = (ImageButton) findViewById(R.id.run_btn);
        mMenuBtn = (ImageButton) findViewById(R.id.menu_btn);

        mRunBtn.setOnClickListener(this);
        mMenuBtn.setOnClickListener(this);

        mainController = new MainController(this);
        mHandler = new Handler();
        defaultLatLng = this.mainController.getLastMockLocation();

        mMapView = (MapView) findViewById(R.id.bmapsView);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setDoubleClickZooming(false);

//        mMapView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                return false;
//            }
//        });


        Drawable marker = this.getResources().getDrawable(R.drawable.map_marker);
        marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
        itemItemizedOverlay=new ItemizedOverlay<OverlayItem>(marker,mMapView);

        mMapController = mMapView.getController();
        mMapView.getController().enableClick(true);
        if (defaultLatLng == null) {
            defaultLatLng = new LatLng(30.66, 104.07);
        }
        currentOverlayItem=new OverlayItem(GeoPointUtils.convertLatLngToGeoPoint(defaultLatLng),"title","lcoation");
        itemItemizedOverlay.addItem(currentOverlayItem);
        currentLocation = defaultLatLng;
        mMapView.getOverlays().add(itemItemizedOverlay);

        mMapController.setCenter(GeoPointUtils.convertLatLngToGeoPoint(defaultLatLng));//设置地图中心点
        mMapController.setZoom(9);//设置地图zoom级别
        mMapView.refresh();

    }

    @Override
    protected void onDestroy() {
        mMapView.destroy();
        if (mBMapMan != null) {
            mBMapMan.destroy();
            mBMapMan = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        if (mBMapMan != null) {
            mBMapMan.stop();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        if (mBMapMan != null) {
            mBMapMan.start();
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.run_btn:
                if (currentLocation == null) {
                    AlertDialogUtils.showConfirmDiaLog(this, "请先设置一个要模拟的位置.");
                } else {
                    mainController.startMockLocation(currentLocation);
                }
                break;
            case R.id.menu_btn:
                showPopupWindow(this.findViewById(R.id.menu_btn));
                break;
        }
    }

    private void showPopupWindow(View view) {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        } else {
            layoutInflater = getLayoutInflater();
            View menu_view = layoutInflater.inflate(R.layout.pop_menu, null);
            mPopupWindow = new PopupWindow(menu_view, 200, 285);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setFocusable(true);
            listView = (ListView) menu_view.findViewById(R.id.lv_dialog);
            listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.text, R.id.tv_text, title));
            listView.setOnItemClickListener(new MyOnItemClickListener());
            mPopupWindow.showAsDropDown(view, 10, 0);

        }
    }

    private void showFavLocation() {
        mFavDialog = new FavDialog(this, this);
        mFavDialog.show();
    }

    private void showHelpDialog(){
        mHelpDialog = new HelpDialog(this);
        mHelpDialog.show();
    }


    public void moveToLocation(LatLng latLng) {
        this.currentLocation = latLng;
        itemItemizedOverlay.removeAll();
        mMapView.getOverlays().remove(itemItemizedOverlay);

        mMapController.setCenter(GeoPointUtils.convertLatLngToGeoPoint(currentLocation));//设置地图中心点
        currentOverlayItem=new OverlayItem(GeoPointUtils.convertLatLngToGeoPoint(currentLocation),"title","location");
        itemItemizedOverlay.addItem(currentOverlayItem);
        mMapView.getOverlays().add(itemItemizedOverlay);
        mMapView.refresh();
        mFavDialog.dismiss();
    }


    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
            switch (position) {
                case 0:
                    mainController.stopMockLocationService();
                    ToastUtils.showMessage(MainActivity.this, "停止模拟位置");
                    break;
                case 1:
                    if (currentLocation != null) {
                        String info=FormatUtils.formatLatLngNumber(currentLocation.latitude)+","+FormatUtils.formatLatLngNumber(currentLocation.longitude);
                        AlertDialogUtils.showInputDialog(MainActivity.this,info , new AlertDialogUtils.CallBack() {
                            @Override
                            public void execute(Object... obj) {
                                mainController.favCurrentLocation(obj[0].toString(), currentLocation);
                                ToastUtils.showMessage(MainActivity.this, "收藏成功");
                            }
                        });
                    }
                    break;
                case 2:
                    showFavLocation();
                    break;
                case 3:
                    showHelpDialog();
                    break;
                case 4:
                    AlertDialogUtils.showYesNoDiaLog(MainActivity.this,"停止模拟并退出程序?",new AlertDialogUtils.Executor() {
                        @Override
                        public void execute() {
                            mainController.stopMockLocationService();
                            MainActivity.this.finish();
                        }
                    });
                    break;
            }

        }
    }
}
