package com.lialzm.android.third.location.route;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.route.WalkPath;
import com.lialzm.android.R;
import com.lialzm.android.third.location.LocationUtils;

public class WalkRouteDetailActivity extends Activity {
	private WalkPath mWalkPath;
	private TextView mTitle,mTitleWalkRoute;
	private ListView mWalkSegmentList;
	private WalkSegmentListAdapter mWalkSegmentListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_detail);
		getIntentData();
		mTitle = (TextView) findViewById(R.id.title_center);
		mTitle.setText("步行路线详情");
		mTitleWalkRoute = (TextView) findViewById(R.id.firstline);
		String dur = LocationUtils.getFriendlyTime((int) mWalkPath.getDuration());
		String dis = LocationUtils
				.getFriendlyLength((int) mWalkPath.getDistance());
		mTitleWalkRoute.setText(dur + "(" + dis + ")");
		mWalkSegmentList = (ListView) findViewById(R.id.bus_segment_list);
		mWalkSegmentListAdapter = new WalkSegmentListAdapter(
				this.getApplicationContext(), mWalkPath.getSteps());
		mWalkSegmentList.setAdapter(mWalkSegmentListAdapter);

	}

	private void getIntentData() {
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		mWalkPath = intent.getParcelableExtra("walk_path");
	}

	public void onBackClick(View view) {
		this.finish();
	}

}
