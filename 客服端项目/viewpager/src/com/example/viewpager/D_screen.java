package com.example.viewpager;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;
import android.widget.RelativeLayout;

public class D_screen extends Activity {
	
	private RelativeLayout layout = null;
	private Handler handler;
	private String ip;
	private Socket socket;
	private ObjectInputStream in=null;
	private OutputStream out=null;
	private byte[] bitmapflu = null;
	private Bitmap bitmap = null;
	private Drawable background;
	private boolean isconnected = false;
	private ByteArrayInputStream bytein;
	private BitmapFactory bm;
	private float screen_height =0;
	private float screen_width = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		setContentView(R.layout.activity_d_screen);
		screen_height = this.getWindowManager().getDefaultDisplay().getHeight();
		screen_width = this.getWindowManager().getDefaultDisplay().getWidth();
		layout = (RelativeLayout) findViewById(R.id.d_screen);
		Intent intent = this.getIntent();
		ip = intent.getStringExtra("ip");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					socket = new Socket(ip,8888);
					in = new ObjectInputStream(socket.getInputStream());
					out = socket.getOutputStream();
					bm = new BitmapFactory();
					isconnected = true;
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("my", "Socket Unconnecting !");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("my", "Socket Unconnecting !");
				}
			}
		}).start();
		
		handler = new  Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what==1)
					layout.setBackground(background);
					Log.e("my", "handler");
					
			}
			
		};
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true)
				if(isconnected){
					try {
						bitmapflu = (byte[]) in.readObject();
						bytein = new ByteArrayInputStream(bitmapflu);
					
						
						bitmap = bm.decodeStream(bytein);
						background = new Drawable() {
					
						@Override
						public void setColorFilter(ColorFilter cf) {
						// TODO Auto-generated method stub
						
						}
					
						@Override
						public void setAlpha(int alpha) {
							// TODO Auto-generated method stub
						
						}
					
						@Override
						public int getOpacity() {
							// TODO Auto-generated method stub
							return 0;
						}
					
						@Override
						public void draw(Canvas canvas) {
							// TODO Auto-generated method stub
							canvas.drawColor(Color.WHITE);
							Matrix matrix = new Matrix();
							matrix.setScale(screen_width/bitmap.getWidth(), screen_height/bitmap.getHeight());
							canvas.drawBitmap(bitmap, matrix, new Paint());
						}
						};
				
					handler.sendEmptyMessage(1);
					} catch (OptionalDataException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ClassNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch (IOException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}catch(Exception e4){
						e4.printStackTrace();
					}
				}
			}
		}).start();
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			try {
				out.write(80);
				out.flush();
				socket.close();
				in.close();
				out.close();
				bytein.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(Exception e){
				
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.d_screen, menu);
		
		return true;
	}
	
}
