package com.cxt.gps.UI.activity.navigate;

import android.os.Bundle;

import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.cxt.gps.R;
import com.cxt.gps.navigate.BaseActivity;


public class WalkRouteCalculateActivity extends BaseActivity {
    protected NaviLatLng mEndLatlng ;
    protected NaviLatLng mStartLatlng ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_basic_navi);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
        mAMapNaviView.setNaviMode(AMapNaviView.NORTH_UP_MODE);

    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        mStartLatlng = new NaviLatLng(getIntent().getDoubleExtra("start_lat",0.0), getIntent().getDoubleExtra("start_lng",0.0));
        mEndLatlng = new NaviLatLng(getIntent().getDoubleExtra("end_lat",0.0), getIntent().getDoubleExtra("end_lng",0.0));
        mAMapNavi.calculateWalkRoute(mStartLatlng, mEndLatlng);

    }

    @Override
    public void onCalculateRouteSuccess(int[] ids) {
        super.onCalculateRouteSuccess(ids);
        mAMapNavi.startNavi(NaviType.GPS);
    }
}
