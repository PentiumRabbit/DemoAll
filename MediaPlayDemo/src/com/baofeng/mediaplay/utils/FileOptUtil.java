package com.baofeng.mediaplay.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import com.baofeng.mediaplay.bean.VideoInfo;

public class FileOptUtil {
	private DealFileInfo dealFileInfo;

	public void getVideoFile(final List<VideoInfo> list, File file) {// 获得视频文件

		file.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				// sdCard找到视频名称
				String name = file.getName();

				int i = name.indexOf('.');
				if (i != -1) {
					name = name.substring(i);
					if (name.equalsIgnoreCase(".mp4") || name.equalsIgnoreCase(".3gp") //
							|| name.equalsIgnoreCase(".wmv") //
							|| name.equalsIgnoreCase(".ts") //
							|| name.equalsIgnoreCase(".rmvb") //
							|| name.equalsIgnoreCase(".mov") //
							|| name.equalsIgnoreCase(".m4v") //
							|| name.equalsIgnoreCase(".avi") //
							|| name.equalsIgnoreCase(".m3u8") //
							|| name.equalsIgnoreCase(".3gpp") //
							|| name.equalsIgnoreCase(".3gpp2") //
							|| name.equalsIgnoreCase(".mkv") //
							|| name.equalsIgnoreCase(".flv") //
							|| name.equalsIgnoreCase(".divx") //
							|| name.equalsIgnoreCase(".f4v") //
							|| name.equalsIgnoreCase(".rm") //
							|| name.equalsIgnoreCase(".asf") //
							|| name.equalsIgnoreCase(".ram") //
							|| name.equalsIgnoreCase(".mpg") //
							|| name.equalsIgnoreCase(".v8") //
							|| name.equalsIgnoreCase(".swf") //
							|| name.equalsIgnoreCase(".m2v") //
							|| name.equalsIgnoreCase(".asx") //
							|| name.equalsIgnoreCase(".ra") //
							|| name.equalsIgnoreCase(".ndivx") //
							|| name.equalsIgnoreCase(".xvid")) { //
						VideoInfo vi = new VideoInfo();
						vi.setDisplayName(file.getName());
						vi.setPath(file.getAbsolutePath());
						list.add(vi);
						return true;
					}
				} else if (file.isDirectory()) {
					getVideoFile(list, file);
				}
				return false;
			}
		});
	}

	public void setScanMediaFileFinishlistener(DealFileInfo dealFileInfo) {
		this.dealFileInfo = dealFileInfo;
	}

	interface DealFileInfo {
		void scanMediaFileFinish();
	}
}
