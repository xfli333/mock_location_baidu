package info.ishared.android.mock.location.baidu;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.google.android.gms.maps.model.LatLng;
import info.ishared.android.mock.location.baidu.bean.MockLatLng;
import info.ishared.android.mock.location.baidu.util.AlertDialogUtils;
import info.ishared.android.mock.location.baidu.util.FormatUtils;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Seven
 * Date: 13-4-22
 * Time: PM1:16
 */
public class FavDialog extends Dialog {
    private static final int MOVE = 0;
    private static final int DELETE = 1;
    private MainActivity mainActivity;
    private ListView mFavListView;
    protected SimpleAdapter adapter;
    protected List<Map<String, String>> favLocationData = new ArrayList<Map<String, String>>();
    private Handler mHandler;


    public FavDialog(Context context, MainActivity mainActivity) {
        super(context);
        this.mainActivity = mainActivity;
        mHandler = new Handler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_fav_list_view);
        this.setCancelable(true);
        this.setTitle("收藏列表");
        mFavListView = (ListView) this.findViewById(R.id.fav_location_list_view);

        mFavListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Double latitude = Double.valueOf(favLocationData.get(position).get("latitude"));
                Double longitude = Double.valueOf(favLocationData.get(position).get("longitude"));
                LatLng latLng = new LatLng(latitude, longitude);
                mainActivity.moveToLocation(latLng);
            }
        });

        mFavListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, MOVE, 0, "移动到该位置");
                menu.add(0, DELETE, 0, "删除该位置");
            }
        });

        initListViewData();
        initListViewGUI();
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Long id = Long.valueOf(favLocationData.get(menuInfo.position).get("id"));
        Double latitude = Double.valueOf(favLocationData.get(menuInfo.position).get("latitude"));
        Double longitude = Double.valueOf(favLocationData.get(menuInfo.position).get("longitude"));
        LatLng latLng = new LatLng(latitude, longitude);
        switch (item.getItemId()) {
            case MOVE:
                mainActivity.moveToLocation(latLng);
                break;
            case DELETE:
                AlertDialogUtils.showYesNoDiaLog(mainActivity, "删除收藏?", new AlertDialogUtils.Executor() {
                    @Override
                    public void execute() {
                        mainActivity.mainController.deleteFavLocation(id);
                        adapter.notifyDataSetChanged();
                        FavDialog.this.dismiss();
                    }
                });

                break;
            default:
                break;
        }

        return false;
    }

//    @Override
//    public boolean onContextItemSelected(android.view.MenuItem item) {
//
//    }


    private void initListViewGUI() {
        adapter = new SimpleAdapter(mainActivity, favLocationData, R.layout.fav_location_list_item, new String[]{"name", "location"}, new int[]{R.id.fav_name, R.id.fav_location}) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (position % 2 != 0)
                    view.setBackgroundResource(R.drawable.table_background_selector);
                else
                    view.setBackgroundResource(R.drawable.table_background_alternate_selector);
                return view;
            }
        };
        mFavListView.setAdapter(adapter);

    }

    private void initListViewData() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                favLocationData.clear();
                List<MockLatLng> favLocationList = mainActivity.mainController.getAllFavLocation();
                Collections.sort(favLocationList, new Comparator<MockLatLng>() {
                    @Override
                    public int compare(MockLatLng lhs, MockLatLng rhs) {
                        return lhs.getFavName().compareTo(rhs.getFavName());
                    }
                });
                for (MockLatLng mockLatLng : favLocationList) {
                    Map<String, String> map = new HashMap<String, String>(2);
                    map.put("id", mockLatLng.getId() + "");
                    map.put("name", mockLatLng.getFavName());
                    map.put("location", FormatUtils.formatLatLngNumber(mockLatLng.getLatitude()) + "," + FormatUtils.formatLatLngNumber(mockLatLng.getLongitude()));
                    map.put("latitude", mockLatLng.getLatitude().toString());
                    map.put("longitude", mockLatLng.getLongitude().toString());
                    favLocationData.add(map);
                }
                adapter.notifyDataSetChanged();
            }
        });

    }

}
