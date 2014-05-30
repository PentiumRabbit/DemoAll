package com.baofeng.mediaplay.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baofeng.mediaplay.bean.VideoInfo;

public class MediaInfoAdapter extends BaseAdapter {
	private Context context;
	private List<VideoInfo> result;

	public MediaInfoAdapter(Context context, List<VideoInfo> result) {
		this.context = context;
		this.result = result;
	}

	@Override
	public int getCount() {
		return result.size();
	}

	@Override
	public Object getItem(int position) {
		return result.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView = new TextView(context);
		textView.setText(result.get(position).getDisplayName());
		return textView;
	}

	class ViewHolder {
	}
}
