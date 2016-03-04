package ke_demo;

import java.util.List;

import com.example.viewpager.MActivity;
import com.example.viewpager.R;



import ke.phonefiletocomput.com.SDFileExplorer;
import ke_adapter.DiskAdapter;
import ke_base.AppManager;
import ke_base.BaseActivity;
import ke_bean.Disk;
import ke_fragment.CenterFragment;
import ke_net.SocketClient;




import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;

public class ke_MainActivity extends BaseActivity implements OnClickListener {

	private onKeyDownListener keyDownListener;

	// 抽屉布局
	private DrawerLayout mDrawerLayout;

	// 抽屉list
	private ListView mDrawerList;

	private LinearLayout left_drawer_LinearLayout;

	private LinearLayout open_llt;


	private DiskAdapter diskAdapter;


	private PopupWindow popupWindow;

	private View view;

	private List<Disk> diskList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
			view = View.inflate(this, R.layout.main_activity, null);


		setContentView(view);

		// 网络连接判断
		if (!application.isNetworkConnected()) {
	
			finish();
		} 


		initView();

		searchViewData();

	}

	/**
	 * 查询view的数据
	 */
	public void searchViewData() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {

				
					diskList = SocketClient.getDiskInfo(application);
			

				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);

				if (result) {

					diskAdapter.setList(diskList);

					diskAdapter.notifyDataSetChanged();

					if (diskAdapter.getSelectedPosition() == -1) {
						diskAdapter.setSelectedPosition(0);
						selectItem(0);
					}

				} else {

				}
			}
		});
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// 横竖屏拦截，我测试总是不好使。不拦截了 竖屏就一直竖屏， 横屏就一直横屏
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	
	}

	/**
	 * 初始化视图
	 * 
	 * @param view
	 */
	public void initView() {

		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		diskAdapter = new DiskAdapter(this);

		mDrawerList.setAdapter(diskAdapter);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

	}

	/**
	 * 初始化竖向视图
	 * 
	 * @param view
	 * @return
	 */
	public View initPortpaitView() {

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// 遮盖主要内容的阴影
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		left_drawer_LinearLayout = (LinearLayout) view
				.findViewById(R.id.left_drawer_LinearLayout);

		open_llt = (LinearLayout) findViewById(R.id.open_llt);

		open_llt.setOnClickListener(this);

		mDrawerLayout.setDrawerListener(new MyDrawerListener());

		return view;
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			diskAdapter.setSelectedPosition(position);
			selectItem(position);

		}
	}

	private void selectItem(int position) {

		Bundle args = new Bundle();

		args.putString(CenterFragment.DISK_PATH,
				diskAdapter.getList().get(position).getPath());

		Fragment fragment = new CenterFragment();

		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();

		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		mDrawerList.setItemChecked(position, true);

	}

	/**
	 * 抽屉监听
	 */
	private class MyDrawerListener implements DrawerListener {

		@Override
		public void onDrawerClosed(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onDrawerOpened(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onDrawerSlide(View arg0, float arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onDrawerStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		// 打开/关闭抽屉
		case R.id.open_llt:
			if (mDrawerLayout.isDrawerOpen(left_drawer_LinearLayout)) {
				mDrawerLayout.closeDrawer(left_drawer_LinearLayout);
			} else {
				mDrawerLayout.openDrawer(Gravity.LEFT);
			}
			break;
		}
	}

	private class MenuItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			keyDownListener.fragmentKeyDown(keyCode, event);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onAttachFragment(Fragment fragment) {

		keyDownListener = (onKeyDownListener) fragment;
		super.onAttachFragment(fragment);
	}

	/**
	 * fragment keyDown回调
	 * 
	 * @author 赵庆洋
	 * 
	 */
	public interface onKeyDownListener {
		public void fragmentKeyDown(int keyCode, KeyEvent event);
	}
	
	public void back(View view){
		Intent intent=new Intent(ke_MainActivity.this,MActivity.class);
		startActivity(intent);
		finish();
	}

}
