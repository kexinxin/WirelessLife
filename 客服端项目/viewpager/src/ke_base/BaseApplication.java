package ke_base;

import java.io.File;
import java.util.Properties;
import java.util.UUID;

import com.example.viewpager.MActivity;

import ke_common.FileUtil;
import ke_common.StringUtil;
import ke_net.Constant;
import ke_net.NetClient;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * 鍏ㄥ眬搴旂敤绫伙細鐢ㄤ簬淇濆瓨鍜岃皟鐢ㄥ叏灞�搴旂敤閰嶇疆
 * 
 * 
 */
public class BaseApplication extends Application {

	public void onCreate() {
		super.onCreate();

		// 娉ㄥ唽app寮傚父宕╂簝澶勭悊鍣�
	};

	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 鑾峰彇app瀹夎鍖呬俊鎭�
	 * 
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;

		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}

		if (info == null)
			info = new PackageInfo();

		return info;
	}

	/**
	 * 鑾峰彇ip鍦板潃
	 * 
	 * @return
	 */
	public String getIpAdress() {
		// String ip_adress = getProperty(AppConfig.CONF_IP_ADDRESS);
		//
		// if (StringUtil.isEmpty(ip_adress))
		// return "0.0.0.0";
		// else
		// return ip_adress;
		String ip_adress = MActivity.ip;
		return ip_adress;
	}

	/**
	 * 娓呴櫎缂撳瓨
	 */
	public void clearAppCache() {
		// 娓呴櫎webview缂撳瓨

		deleteDatabase("webview.db");
		deleteDatabase("webview.db-shm");
		deleteDatabase("webview.db-wal");
		deleteDatabase("webviewCache.db");
		deleteDatabase("webviewCache.db-shm");
		deleteDatabase("webviewCache.db-wal");

		// 娓呴櫎鏁版嵁缂撳瓨
		clearCacheFolder(getFilesDir(), System.currentTimeMillis());
		clearCacheFolder(getCacheDir(), System.currentTimeMillis());

		// 娓呮缂栬緫鍣ㄤ繚瀛樼殑涓存椂鏂囦欢

	}

	/**
	 * 娓呴櫎涓嬭浇缂撳瓨
	 */
	public void clearDownCache() {
		// 娓呴櫎涓嬭浇缂撳瓨
		File file = FileUtil.getDirectoryFile(Constant.DOWN_PATH);
		if (file != null && file.exists() && file.isDirectory()) {
			for (File item : file.listFiles()) {
				item.delete();
			}
			file.delete();
		}
	}

	/**
	 * 娓呴櫎缂撳瓨鐩綍
	 * 
	 * @param dir
	 *            鐩綍
	 * @param curTime
	 *            褰撳墠绯荤粺鏃堕棿
	 * @return
	 */
	private int clearCacheFolder(File dir, long curTime) {
		int deletedFiles = 0;

		if (dir != null && dir.isDirectory()) {
			try {
				for (File child : dir.listFiles()) {
					if (child.isDirectory()) {
						deletedFiles += clearCacheFolder(child, curTime);
					}
					// 濡傛灉鏂囦欢鐨勬渶鍚庝慨鏀规椂闂村皬浜庡綋鍓嶇郴缁熸椂闂�
					if (child.lastModified() < curTime) {
						// 鍒犻櫎
						if (child.delete()) {
							deletedFiles++;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return deletedFiles;
	}

}
