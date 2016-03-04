package ke.phonefiletocomput.com;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.viewpager.MActivity;
import com.example.viewpager.R;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SDFileExplorer extends Activity {
	ListView listView;
	TextView textView;
	// 记录当前的父文件夹
	File currentParent;
	// 记录当前路径下的所有文件的文件数组
	File[] currentFiles;

	EditText show;
	int length = 0;
	byte[] sendBytes = null;
	Socket socket = null;
	DataOutputStream dos = null;
	FileInputStream fis = null;

	String path;
	String fileName;
	
	String ke_ip=MActivity.ip;

	static int MAX_PROGRESS = 100;

	// 记录进度对话框的完成百分比
	int progressStatus = 0;
	int hasData = 0;
	ProgressDialog bar;
	// 定义一个负责更新的进度的Handler
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ke_file_main);
		// 获取列出全部文件的ListView
		listView = (ListView) findViewById(R.id.list);
		textView = (TextView) findViewById(R.id.path);
		// 获取系统的SD卡的目录
		File root = new File("/mnt/sdcard/");
		// 如果 SD卡存在
		if (root.exists()) {
			currentParent = root;
			currentFiles = root.listFiles();
			// 使用当前目录下的全部文件、文件夹来填充ListView
			inflateListView(currentFiles);
		}
		// 为ListView的列表项的单击事件绑定监听器
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				try {
					path = currentParent.getCanonicalPath() + "/"
							+ currentFiles[position].getName();
					fileName = currentFiles[position].getName();

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// 用户单击了文件
				if (currentFiles[position].isFile()) {
					MAX_PROGRESS = ((int) currentFiles[position].length())-20000;
					new Thread() {
						public void run() {
							try {
								Socket socketPath = new Socket(ke_ip,
										36666);
								OutputStream os = socketPath.getOutputStream();

								os.write(fileName.getBytes());
								// 关闭输出流，关闭Socket
								os.close();

							} catch (UnknownHostException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}.start();

					new Thread() {
						@Override
						public void run() {
							try {
								try {
									socket = new Socket();
									socket.connect(new InetSocketAddress(
											ke_ip, 33456), 10 * 1000);
									dos = new DataOutputStream(socket
											.getOutputStream());

									String local_file = "/storage/sdcard0/recovery.img";

									File file = new File(path);

									fis = new FileInputStream(file);
									sendBytes = new byte[1024];
									while ((length = fis.read(sendBytes, 0,
											sendBytes.length)) > 0) {
										dos.write(sendBytes, 0, length);
								
										if (progressStatus < MAX_PROGRESS) {
											// 获取耗时操作的完成百分比
											progressStatus = (progressStatus + length);
											bar.setProgress(progressStatus);
										}
										// 如果任务已经完成
										if (progressStatus >= MAX_PROGRESS) {
											// 关闭对话框
											bar.dismiss();
										}

										dos.flush();
									}
								} finally {
									if (dos != null)
										dos.close();
									if (fis != null)
										fis.close();
									if (socket != null)
										socket.close();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}.start();

					// 将进度条的完成进度重设为0
					progressStatus = 0;
					// 重新开始填充数组。
					hasData = 0;
					bar = new ProgressDialog(SDFileExplorer.this);
					bar.setMax(MAX_PROGRESS);
					// 设置对话框的标题
					bar.setTitle("正在传输" + currentFiles[position].getName());
					// 设置对话框 显示的内容
					bar.setMessage("任务完成的百分比");
					// 设置对话框不能用“取消”按钮关闭
					bar.setCancelable(false);
					// 设置对话框的进度条风格
					bar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					// 设置对话框的进度条是否显示进度
					bar.setIndeterminate(false);
					bar.show(); // ③

					return;
				}
				// 获取用户点击的文件夹下的所有文件
				File[] tmp = currentFiles[position].listFiles();
				if (tmp == null || tmp.length == 0) {
					Toast.makeText(SDFileExplorer.this, "当前路径不可访问或该路径下没有文件",
							Toast.LENGTH_SHORT).show();
				} else {
					// 获取用户单击的列表项对应的文件夹，设为当前的父文件夹
					currentParent = currentFiles[position]; // ②
					// 保存当前的父文件夹内的全部文件和文件夹
					currentFiles = tmp;
					// 再次更新ListView
					inflateListView(currentFiles);
				}
			}
		});
		// 获取上一级目录的按钮
		Button parent = (Button) findViewById(R.id.parent);
		parent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View source) {
				try {
					if (!currentParent.getCanonicalPath().equals("/mnt/sdcard")) {
						// 获取上一级目录
						currentParent = currentParent.getParentFile();
						// 列出当前目录下所有文件
						currentFiles = currentParent.listFiles();
						// 再次更新ListView
						inflateListView(currentFiles);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void inflateListView(File[] files) // ①
	{
		// 创建一个List集合，List集合的元素是Map
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < files.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			// 如果当前File是文件夹，使用folder图标；否则使用file图标
			if (files[i].isDirectory()) {

				listItem.put("icon", R.drawable.folder);

				listItem.put("fileImage", R.drawable.file_folder);

			} else {
				long size = 0;
				size = size + files[i].length();
				String m = FormetFileSize(size);
				listItem.put("textSize", m);
				listItem.put("fileImage", R.drawable.file_down);

				String filename = files[i].getName();
				int index = filename.indexOf(".");
				String tail = filename.substring(index + 1);
				if (tail.equals("mp3")) {
					listItem.put("icon", R.drawable.file_icon_mp3);
				} else if (tail.equals("doc") || tail.equals("xls")
						|| tail.equals("docx") || tail.equals("ppt")
						|| tail.equals("pptx")) {
					listItem.put("icon", R.drawable.file_icon_office);
				} else if (tail.equals("pdf")) {
					listItem.put("icon", R.drawable.file_icon_pdf);
				} else if (tail.equals("png") || tail.equals("jpeg")
						|| tail.equals("gif") || tail.equals("pict")
						|| tail.equals("img") || tail.equals("jpg")) {
					listItem.put("icon", R.drawable.file_icon_picture);
				} else if (tail.equals("rar")) {
					listItem.put("icon", R.drawable.file_icon_rar);
				} else if (tail.equals("txt")) {
					listItem.put("icon", R.drawable.file_icon_txt);
				} else if (tail.equals("zip")) {
					listItem.put("icon", R.drawable.file_icon_zip);
				} else if (tail.equals("mp4") || tail.equals("mkv")
						|| tail.equals("rmvb")) {
					listItem.put("icon", R.drawable.file_icon_video);
				} else {
					listItem.put("icon", R.drawable.file_icon_default);
				}

			}
			listItem.put("fileName", files[i].getName());
			// 添加List项
			listItems.add(listItem);
		}
		// 创建一个SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.ke_file_line, new String[] { "icon", "fileName", "textSize",
						"fileImage" }, new int[] { R.id.file_icon,
						R.id.file_text, R.id.file_text_size, R.id.file_image });
		// 为ListView设置Adapter
		listView.setAdapter(simpleAdapter);
		try {
			textView.setText("：" + currentParent.getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public String FormetFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}
	
	public void back(View view){
		Intent intent=new Intent(SDFileExplorer.this,MActivity.class);
		startActivity(intent);
		finish();
	}
}

