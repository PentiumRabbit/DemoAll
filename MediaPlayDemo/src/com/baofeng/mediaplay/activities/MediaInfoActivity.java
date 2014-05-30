package com.baofeng.mediaplay.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.baofeng.mediaplay.R;
import com.baofeng.mediaplay.adapter.MediaInfoAdapter;
import com.baofeng.mediaplay.bean.VideoInfo;
import com.baofeng.mediaplay.utils.FileOptUtil;

/**
 * 
 * MediaInfoActivity
 * 
 * @author ----zhaoruyang----
 * @version: V1.0
 * @data: 2014年5月30日 上午10:06:28
 * 
 */
public class MediaInfoActivity extends Activity {

	protected static final String TAG = "MediaInfoActivity";
	private ListView lv_mediainfo;
	private MediaInfoAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_media_info);
		initView();
		initData();
	}

	private void initData() {
		new AsyncTask<Void, Void, List<VideoInfo>>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(List<VideoInfo> result) {
				if (result != null && result.size() != 0) {

					adapter = new MediaInfoAdapter(MediaInfoActivity.this.getApplicationContext(), result);
					lv_mediainfo.setAdapter(adapter);

				}
			}

			@Override
			protected List<VideoInfo> doInBackground(Void... params) {
				FileOptUtil fileOptUtil = new FileOptUtil();
				File file = Environment.getExternalStorageDirectory();

				List<VideoInfo> list = new ArrayList<VideoInfo>();
				fileOptUtil.getVideoFile(list, file);
				Log.i(TAG, "-------------");
				return list;
			}
		}.execute();
	}

	private void initView() {
		lv_mediainfo = (ListView) findViewById(R.id.lv_mediainfo);
		lv_mediainfo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				VideoInfo item = (VideoInfo) adapter.getItem(position);
				Intent intent = new Intent(getApplicationContext(), MediaPlayActivity.class);
				intent.putExtra("local_url", item.getPath());
				Log.i(TAG, "-------------" + item.getPath());
				startActivity(intent);
			}
		});
	}

}
