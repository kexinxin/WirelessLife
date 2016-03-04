package ke_fragment;

import java.util.List;
import java.util.Stack;

import com.example.viewpager.R;



import ke_adapter.FileAdapter;
import ke_base.AppManager;
import ke_base.BaseFragment;
import ke_bean.PcFile;
import ke_demo.DownLoadActivity;
import ke_demo.ke_MainActivity;
import ke_demo.ke_MainActivity.onKeyDownListener;
import ke_net.SocketClient;





//import com.example.qingyangdemo.ui.CustomDialog;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 中间的布局
 * 
 * 
 */
public class CenterFragment extends BaseFragment implements OnClickListener,
		onKeyDownListener {

	public static final String DISK_PATH = "disk_path";

	private ListView listView;

	private ImageButton returnBtn, upBtn;

	private TextView titleView;

	private FileAdapter fileAdapter;

	private List<PcFile> fileList;

	private String diskPath;

	private ke_MainActivity activity;

	// path的堆栈
	private static Stack<String> pathStack;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		diskPath = getArguments().getString(DISK_PATH);
		activity = (ke_MainActivity) getActivity();
		addPath(diskPath);
	}

	/**
	 * 添加路径到堆栈
	 * 
	 * @param path
	 */
	public void addPath(String path) {

		if (pathStack == null) {
			pathStack = new Stack<String>();
		}

		pathStack.add(path);
	}

	/**
	 * 获取堆栈最上层的路径
	 * 
	 * @return
	 */
	public String getLastPath() {
		return pathStack.lastElement();
	}

	/**
	 * 移除堆栈最上层路径
	 */
	public void removeLastPath() {
		pathStack.remove(getLastPath());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.center_fragment, container,
				false);

		listView = (ListView) rootView.findViewById(R.id.center_drawer);

		fileAdapter = new FileAdapter(getActivity());

		listView.setAdapter(fileAdapter);

		listView.setOnItemClickListener(new DrawerItemClickListener());

		returnBtn = (ImageButton) rootView.findViewById(R.id.center_return_btn);

		upBtn = (ImageButton) rootView.findViewById(R.id.center_up_btn);

		returnBtn.setOnClickListener(this);

		upBtn.setOnClickListener(this);

		titleView = (TextView) rootView.findViewById(R.id.center_title);

		titleView.setText(diskPath);

		searchViewData(diskPath);

		return rootView;
	}

	/**
	 * 查询调用方法
	 */
	public void searchData(String path) {
		searchViewData(path);
		titleView.setText(path);
	}

	/**
	 * 查询view的数据
	 */
	public void searchViewData(final String path) {

		activity.putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {

				fileList = SocketClient.getFileInfo(getMyApplication(), path);

				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);

				if (result) {

					fileAdapter.setList(fileList);

					fileAdapter.setSelectedPosition(-1);

					fileAdapter.notifyDataSetChanged();

				}
			}
		});
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			fileAdapter.setSelectedPosition(position);

			selectItem(position);
		}
	}

	private void selectItem(int position) {

		String filePath = fileAdapter.getList().get(position).getFilePath();

		String fileName = fileAdapter.getList().get(position).getFileName();

		if (fileAdapter.getList().get(position).isDirectory()) {
			searchData(filePath);
			addPath(filePath);
		} else if (fileAdapter.getList().get(position).isFile()) {

			Bundle args = new Bundle();

			args.putString(DownLoadActivity.FILE_NAME, fileName);

			args.putString(DownLoadActivity.FILE_PATH, filePath);

			Intent intent = new Intent(getActivity(), DownLoadActivity.class);

			intent.putExtras(args);

			getActivity().startActivity(intent);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.center_return_btn:
			if (getLastPath().equals(diskPath)) {
				return;
			}
			removeLastPath();

			searchData(getLastPath());

			break;

		}
	}

	@Override
	public void fragmentKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

	}

}
