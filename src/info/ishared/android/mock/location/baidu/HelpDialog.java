package info.ishared.android.mock.location.baidu;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Seven
 * Date: 13-4-22
 * Time: PM1:56
 */
public class HelpDialog extends Dialog {

    private ViewPager mViewPager;
    private View layout1 = null;
    private View layout2 = null;
    private View layout3 = null;
    private List<View> mListViews;
    private LayoutInflater mInflater;
    private MyPagerAdapter myAdapter;

    public HelpDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.help_dialog_layout);
        this.setCancelable(true);
        this.setTitle("帮助和说明");
        mViewPager = (ViewPager)this.findViewById(R.id.help_dialog_view_pager);

        myAdapter = new MyPagerAdapter();
        mViewPager.setAdapter(myAdapter);

        mListViews = new ArrayList<View>();
        mInflater = getLayoutInflater();
        layout1 = mInflater.inflate(R.layout.help_layout1, null);
        layout2 = mInflater.inflate(R.layout.help_layout2, null);
        layout3 = mInflater.inflate(R.layout.help_layout3, null);

        mListViews.add(layout1);
        mListViews.add(layout2);
        mListViews.add(layout3);

        //初始化当前显示的view
        mViewPager.setCurrentItem(0);

    }



    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            Log.d("k", "destroyItem");
            ((ViewPager) arg0).removeView(mListViews.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
            Log.d("k", "finishUpdate");
        }

        @Override
        public int getCount() {
//            Log.d("k", "getCount");
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            Log.d("k", "instantiateItem");
            ((ViewPager) arg0).addView(mListViews.get(arg1),0);
            return mListViews.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            Log.d("k", "isViewFromObject "+ (arg0==(arg1)));
            return arg0==(arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            Log.d("k", "restoreState");
        }

        @Override
        public Parcelable saveState() {
            Log.d("k", "saveState");
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            Log.d("k", "startUpdate");
        }

    }
}
