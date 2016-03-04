package ke_common;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.example.viewpager.R;



import ke_base.AppManager;
import ke_base.BaseApplication;
import ke_demo.DownLoadActivity;
import ke_demo.ke_MainActivity;
import ke_thread.DownLoadThread;


//import com.example.qingyangdemo.ui.CustomDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 下载管理
 * 
 * 
 */
public class DownLoadManager {
	// 文件重命名出错
	public final static int DOWNLOAD_RENAME = -2;
	// 下载失败状态
	public final static int DOWNLOAD_FAILL = -1;
	// 下载成功状态
	public final static int DOWNLOAD_SUCCESS = 1;
	// 下载更新状态
	public final static int DOWNLOAD_UPDATE = 2;
	// 已有文件
	public final static int DOWNLOAD_EXITS = 3;

	private DownLoadActivity context;

	private static DownLoadManager downManager;

	private DownLoadThread runnable;

	private Handler handler;

	// 是否是突然暂停的下载
	private boolean isStop = true;

	public static DownLoadManager getDownManager() {

		if (downManager == null) {
			downManager = new DownLoadManager();
		}
		return downManager;
	}

	/**
	 * 下载方法
	 * 
	 * @param context
	 * @param application
	 * @param progressBar
	 * @param progressText
	 */
	public void oneDownLoad(final DownLoadActivity context,
			final BaseApplication application, final String filePath,
			final String fileName, final ProgressBar progressBar,
			final TextView progressText) {

		this.context = context;

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == DOWNLOAD_FAILL) {
	

					isStop = false;
				} else if (msg.what == DOWNLOAD_RENAME) {


					isStop = false;
				} else if (msg.what == DOWNLOAD_SUCCESS) {

					String localFilePath = (String) msg.obj;

					progressText.setText(R.string.download_success);

					// 打开文件
					File file = new File(localFilePath);

	
					AppManager.getAppManager().finishActivity();

					isStop = false;

				} else if (msg.what == DOWNLOAD_UPDATE) {

					Map<String, Object> map = (HashMap<String, Object>) msg.obj;

					progressBar.setProgress((Integer) map.get("progress"));

					progressText.setText(map.get("currentSize") + "/"
							+ map.get("fileSize"));

					isStop = true;

				} else if (msg.what == DOWNLOAD_EXITS) {
					String localFilePath = (String) msg.obj;

					progressText.setText(R.string.download_exits);

					showDialog(application, filePath, localFilePath, fileName);

					isStop = false;
				}

			}
		};

		runnable = new DownLoadThread(application, handler, filePath, fileName);

		runnable.start();
	}

	/**
	 * 停止下载线程
	 */
	public void stopDownTread() {

		if (isStop) {
			runnable.setStart(false);

		}
	}

	/**
	 * 显示是否重新下载的dialog
	 * 
	 * @param handler
	 * @param application
	 */
	public void showDialog(final BaseApplication application,
			final String filePath, final String localFilePath,
			final String fileName) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setTitle("提示");
		builder.setMessage("该文件已下载，是否重新下载?");
		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 删除文件
				File file = new File(localFilePath);

				file.delete();

				runnable = new DownLoadThread(application, handler, filePath,
						fileName);

				runnable.start();

			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 打开文件
				File file = new File(localFilePath);

		

				dialog.dismiss();

				// 关闭activity
				AppManager.getAppManager().finishActivity();

			}
		});
		builder.create().show();
	}
}
