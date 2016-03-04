package com.example.viewpager;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import ke.phonefiletocomput.com.SDFileExplorer;
import ke_demo.ke_MainActivity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

public class MActivity extends Activity {

	View linear1;// 上传文件的LinearLayout
	LinearLayout linear2;// 下载文件的LinearLayout
	LinearLayout linear3;// 游戏手柄的LinearLayout
	LinearLayout linear4;// 重力感应的LinearLayout
	LinearLayout linear5;// 视频监视的LinearLayout
	LinearLayout linear6;// 鼠标控制的LinearLayout
	LinearLayout linear7;// 更多的LinearLayout
	
	public static String ip;
	
	
	
	public static String ke_FilePath;
	public static FileOutputStream ke_fos = null;

	// 自定义的弹出框类
	SelectPicPopupWindow menuWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// setContentView(R.layout.bangzhu);
		initViewPager();

		// linear1 = (LinearLayout) findViewById(R.id.show1);
		// linear1.setOnClickListener(new ClickListenr1());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {

		case 0:
			// 此处写点击这个按钮的事件
			return true;

		}
		return false;
	}

	private void initViewPager() {
		ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
		/**
		 * 获取子页面对象
		 */
		View view1 = LayoutInflater.from(this).inflate(R.layout.layout1, null);
		View view2 = LayoutInflater.from(this).inflate(R.layout.layout2, null);

		ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);

		MYViewPagerAdepter adepter = new MYViewPagerAdepter();
		adepter.setViews(views);
		viewPager.setAdapter(adepter);
	}

	public void dianji_connect(View v) {
		// 请在这里编写connect的点击事件
		final EditText e=new EditText(this);
		new AlertDialog.Builder(this).setTitle("请输入IP")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setView(e)
				.setPositiveButton("连接", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						ip=e.getText().toString();
						System.out.println(ip);
						ReciviFileFromComputer();
					}
				}).setNegativeButton("取消", null).show();


	}

	public void dianji_shangchuanwenjian(View v) {
		// 请在这里编写上传文件的点击事件
		Intent intent=new Intent(MActivity.this,SDFileExplorer.class);
		startActivity(intent);
		System.out.println("上传文件");
	}

	public void dianji_xiazaiwenjian(View v) {
		// 请在这里编写下载文件的点击事件
		Intent intent=new Intent(MActivity.this,ke_MainActivity.class);
		startActivity(intent);
		System.out.println("下载文件");
	}

	public void dianji_youxishoubing(View v) {
		// 请在这里编写游戏手柄的点击事件
		System.out.println("游戏手柄");
	}

	public void dianji_zhongliganying(View v) {
		// 请在这里编写游戏手柄的点击事件
		System.out.println("重力感应");
	}

	public void dianji_shipinjianshi(View v) {
		// 请在这里编写视频监视的点击事件
		System.out.println("关机");
			AlertDialog.Builder builder =  new AlertDialog.Builder(this);
			builder.setTitle("关机？");
			builder.setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								Socket socket = new  Socket(ip,8889);
							} catch (UnknownHostException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();
					
				}
			});
			builder.setNegativeButton("取消", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//do nothing!
				}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
		
	}

	public void dianji_shubiaokongzhi(View v) {
		// 请在这里编写鼠标控制的点击事件
		System.out.println("鼠标控制");
		Intent intent = new Intent();
		intent.putExtra("ip", ip);
		intent.setClass(MActivity.this, MouseKey.class);
		this.startActivity(intent);
		
	}

	public void dianji_gengduo(View v) {
		// 请在这里编写“更多”的点击事件
		System.out.println("更多");
		Intent intent = new Intent();
		intent.putExtra("ip", ip);
		intent.setClass(this, D_screen.class);
		
		this.startActivity(intent);
	}

	public void dianji_caidan(View v) {
		// 实例化SelectPicPopupWindow
		menuWindow = new SelectPicPopupWindow(MActivity.this, itemsOnClick);
		// 显示窗口
		menuWindow.showAtLocation(
				MActivity.this.findViewById(R.id.viewPager), Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
	}

	public void zizhuwomen(View v) {
		new AlertDialog.Builder(this).setTitle("资助我们")
				.setItems(new String[] { "请联系我们" }, null)
				.setNegativeButton("确定", null).show();
	}

	/*
	 * public void bangzhu(View v){ TextSwitcher switcher; String[] strs = new
	 * String[]{ "帮助啊帮助！" }; int curStr; switcher = (TextSwitcher)
	 * findViewById(R.id.text_bangzhu); switcher.setFactory(new ViewFactory() {
	 * 
	 * @Override public View makeView() { TextView tv = new
	 * TextView(MainActivity.this); tv.setTextSize(40);
	 * tv.setTextColor(Color.MAGENTA); return tv; } });
	 * 
	 * }
	 */

	public void gengxin(View v) {
		new AlertDialog.Builder(this).setTitle("更新")
				.setItems(new String[] { "当前已是最新版本" }, null)
				.setNegativeButton("确定", null).show();
	}

	// 为弹出窗口实现监听类
	private android.view.View.OnClickListener itemsOnClick = new android.view.View.OnClickListener() {

		public void onClick(View v) {
			menuWindow.dismiss();

			switch (v.getId()) {
			case R.id.btn_bangzhu:
				/*
				 * { Button btn_bangzhu = (Button)
				 * findViewById(R.id.btn_bangzhu); bangzhu(btn_bangzhu); }
				 */
				break;
			case R.id.btn_gengxin: {
				Button btn_gengxin = (Button) findViewById(R.id.btn_gengxin);
				gengxin(btn_gengxin);
			}
				break;
			case R.id.btn_tuanduijieshao:
				break;
			case R.id.btn_zizhuwomen: {
				Button btn_zizhu = (Button) findViewById(R.id.btn_zizhuwomen);
				zizhuwomen(btn_zizhu);
			}
				break;
			default:
				break;
			}

		}

	};
	
	public static void ReciviFileFromComputer(){
		new Thread() {
			@Override
			public void run() {
				try {
					// 建立连接到远程服务器的Socket
					Socket ke_socket = new Socket("192.168.191.1", 2777); // ①
					System.out.println("准备取数值");
					// 将Socket对应的输入流包装成BufferedReader
					BufferedReader ke_br = null;
					ke_br = new BufferedReader(new InputStreamReader(
							ke_socket.getInputStream(), "utf-8"));
					while ((ke_FilePath = ke_br.readLine()) != null) {

						ke_FilePath = ke_br.readLine();
						ke_fos = new FileOutputStream(new File(
								"/storage/sdcard0/从电脑传来的文件/" + ke_FilePath));
						System.out.println(ke_FilePath);
					}
					
					
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();

		new Thread() {
			@Override
			public void run() {
				try {
					ke_FilePath = "/storage/sdcard0/从电脑传来的文件/fasd.txt";

					Socket ke_socket = new Socket("192.168.191.1", 2732); // ①
					System.out.println("fasd");

					byte[] ke_inputByte = null;
					int ke_length = 0;
					DataInputStream dis = null;

					dis = new DataInputStream(ke_socket.getInputStream());
					ke_fos = new FileOutputStream(new File(
							"/storage/sdcard0/从电脑传来的文件/" + "SD.txt"));
					System.out.println("开端接管数据...");
					ke_inputByte = new byte[1024];
					while ((ke_length = dis.read(ke_inputByte, 0, ke_inputByte.length)) > 0) {
						System.out.println(ke_length);
						ke_fos.write(ke_inputByte, 0, ke_length);
	//					System.out.println(FilePath);
						ke_fos.flush();
					}
					System.out.println("完成接管");

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

}