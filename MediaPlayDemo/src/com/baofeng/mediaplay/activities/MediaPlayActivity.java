package com.baofeng.mediaplay.activities;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.baofeng.mediaplay.R;

public class MediaPlayActivity extends Activity implements OnClickListener {

	private static final String TAG = "MediaPlayActivity";
	protected static final int SETSIZE = 0;
	protected static final int NOTIFY_SB_CHANG = 1;
	protected static final int NOTIFY_MEDIA_CHANG = 2;
	protected boolean isSeekCompleted = true;
	private SurfaceView sv_mediaplay;
	private SurfaceHolder holder;
	private String path;
	private MediaPlayer mMediaPlayer;
	private int mVideoWidth;
	private int mVideoHeight;
	private int progress;
	Handler uiHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case SETSIZE:
				int mVideoWidth = mMediaPlayer.getVideoWidth();
				int mVideoHeight = mMediaPlayer.getVideoHeight();
				int width = sv_mediaplay.getWidth();
				int height = sv_mediaplay.getHeight();
				android.widget.RelativeLayout.LayoutParams sufaceviewParams = (android.widget.RelativeLayout.LayoutParams) sv_mediaplay
						.getLayoutParams();
				if (mVideoWidth * height > width * mVideoHeight) {
					// Log.i("@@@", "image too tall, correcting");
					sufaceviewParams.height = width * mVideoHeight / mVideoWidth;
				} else if (mVideoWidth * height < width * mVideoHeight) {
					// Log.i("@@@", "image too wide, correcting");
					sufaceviewParams.width = height * mVideoWidth / mVideoHeight;
				} else {
					sufaceviewParams.height = height;
					sufaceviewParams.width = width;
				}

				sv_mediaplay.setLayoutParams(sufaceviewParams);
				// setTimer();
				break;

			case NOTIFY_SB_CHANG:
				sb_progress.setProgress(msg.arg1);
				Log.i(TAG, "---------" + msg.arg1);

				break;
			case NOTIFY_MEDIA_CHANG:

				break;
			default:
				break;
			}

			return false;
		}
	});
	private ImageView iv_next;
	private ImageView iv_pre;
	private ImageView iv_play;
	private SeekBar sb_progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		setContentView(R.layout.activity_media_play);
		// path = getIntent().getStringExtra("local_url");
		path = "/storage/sdcard0/我的资料/爸爸去哪儿精彩分段_第2013-12-06期_1862199.mp4";

		initView();
	}

	private void initView() {
		sv_mediaplay = (SurfaceView) findViewById(R.id.sv_mediaplay);
		iv_next = (ImageView) findViewById(R.id.iv_next);
		iv_pre = (ImageView) findViewById(R.id.iv_pre);
		iv_play = (ImageView) findViewById(R.id.iv_play);
		sb_progress = (SeekBar) findViewById(R.id.sb_progress);
		sv_mediaplay.setOnClickListener(this);
		iv_next.setOnClickListener(this);
		iv_pre.setOnClickListener(this);
		iv_play.setOnClickListener(this);
		sb_progress.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			/**
			 * 拖动条停止拖动的时候调用
			 */
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if (isSeekCompleted) {
					progress = seekBar.getProgress();
					mMediaPlayer.seekTo(progress);
					isSeekCompleted = false;
				}
			}

			/**
			 * 拖动条开始拖动的时候调用
			 */
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			/**
			 * 拖动条进度改变的时候调用
			 */
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

			}
		});

		holder = sv_mediaplay.getHolder();

		holder.addCallback(new MediaCallback());

	}

	class MediaCallback implements Callback {

		/* 更改时出发的事件 */
		public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
			Log.d(TAG, "surfaceChanged called");

		}

		/* 销毁 */
		public void surfaceDestroyed(SurfaceHolder surfaceholder) {
			Log.d(TAG, "surfaceDestroyed called");
		}

		/* 当SurfaceHolder创建时触发 */
		public void surfaceCreated(SurfaceHolder holder) {
			Log.d(TAG, "surfaceCreated called");
			playVideo();
			setTimer();
		}
	}

	private void playVideo() {
		try {

			/* 构建MediaPlayer对象 */
			mMediaPlayer = new MediaPlayer();

			/* 设置媒体文件路径 */
			mMediaPlayer.setDataSource(path);

			/* 设置通过SurfaceView来显示画面 */
			mMediaPlayer.setDisplay(holder);

			mMediaPlayer.setScreenOnWhilePlaying(true);

			// 设置边播放变缓冲
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.prepareAsync();

			/* 准备 */
			// mMediaPlayer.prepare();

			mMediaPlayer.setOnSeekCompleteListener(new OnSeekCompleteListener() {

				@Override
				public void onSeekComplete(MediaPlayer mp) {
					isSeekCompleted = false;
				}
			});

			/**
			 * 播放器异常事件
			 */
			mMediaPlayer.setOnErrorListener(new OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					mMediaPlayer.release();
					return false;
				}
			});

			/* 设置事件监听 */
			mMediaPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

				@Override
				public void onBufferingUpdate(MediaPlayer mp, int percent) {

				}
			});
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					playVideo();

				}
			});
			mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					Log.d(TAG, "onPrepared called");
					mVideoWidth = mMediaPlayer.getVideoWidth();
					mVideoHeight = mMediaPlayer.getVideoHeight();
					sb_progress.setMax(mMediaPlayer.getDuration());
					if (mVideoWidth != 0 && mVideoHeight != 0) {
						// if (mVideoWidth > mVideoHeight) {
						// holder.setFixedSize(mVideoWidth, mVideoHeight);
						// } else {
						// /* 设置视频的宽度和高度 */
						// holder.setFixedSize(mVideoWidth, mVideoHeight);
						// }
						/* 开始播放 */
						mMediaPlayer.start();

						uiHandler.sendEmptyMessageDelayed(SETSIZE, 1000);

					}

				}
			});
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage(), e);
		}
	}

	private void setTimer() {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				Message message = Message.obtain();
				message.what = NOTIFY_SB_CHANG;
				if (mMediaPlayer.isPlaying()) {
					message.arg1 = mMediaPlayer.getCurrentPosition();
				}
				uiHandler.sendMessage(message);
			}
		};
		timer.schedule(task, 2000, 500);
	}

	/* 销毁 */
	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}

	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.sv_mediaplay:

			break;
		case R.id.iv_next:

			break;
		case R.id.iv_play:

			break;
		case R.id.iv_pre:

			break;

		default:
			break;
		}
	}

}
