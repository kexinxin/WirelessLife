package com.example.viewpager;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.zip.Inflater;



import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MouseKey extends Activity {
	private Button d_sc ;
	private Button d_dc;
	private RelativeLayout d_mainboard;
	private String ip;
	private Socket socket1= null;
	private Socket socket2= null;
	private OutputStream out1 = null;
	private DataOutputStream out2 = null;
	
	private Keyboard k1,k2;
	private KeyboardView keyboardView;
	public boolean isnun = false;
	public boolean isupper = false;
	private boolean key_toggle = false;
	private boolean  isconnected = false;
	private int prex =0;
	private int prey =0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		setContentView(R.layout.activity_mouse_key);
		ip = this.getIntent().getStringExtra("ip");
		d_sc = (Button) findViewById(R.id.d_sc);
		d_dc = (Button) findViewById(R.id.d_dc);
		d_mainboard = (RelativeLayout) findViewById(R.id.d_mainboard);
		
		//////////keyboard////////////////////
		keyboardView = (KeyboardView) findViewById(R.id.keyboard_view);
		k1 = new Keyboard(this, R.xml.qwerty);
		k2 = new Keyboard(this, R.xml.symbols);
		keyboardView.setKeyboard(k1);
		keyboardView.setEnabled(true);
		keyboardView.setPreviewEnabled(true);
		keyboardView.setOnKeyboardActionListener(new OnKeyboardActionListener() {
			
			@Override
			public void swipeUp() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void swipeRight() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void swipeLeft() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void swipeDown() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onText(CharSequence text) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onRelease(int primaryCode) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPress(int primaryCode) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onKey(int primaryCode, int[] keyCodes) {
				// TODO Auto-generated method stub
				if (primaryCode == Keyboard.KEYCODE_CANCEL) {
					hideKeyboard();
					key_toggle=!key_toggle;
				}else if (primaryCode == 20) {
					changeKey();
					keyboardView.setKeyboard(k1);
					if(isconnected)
						try {
							out1.write(primaryCode);
							out1.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

				} else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {
					if (isnun) {
						isnun = false;
						keyboardView.setKeyboard(k1);
					} else {
						isnun = true;
						keyboardView.setKeyboard(k2);
					}
				}
				else{
					if(isconnected)
						try {
							out1.write(primaryCode);
							out1.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
			}
		});
		
		////////////////////////////////////////////////////
		
		final Bitmap mainbackground = new BitmapFactory().decodeResource(this.getResources(), R.drawable.mainboard);
		d_mainboard.setBackground(new Drawable() {
			
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
				canvas.drawColor(Color.BLACK);
				float mb_w = mainbackground.getWidth();
				float mb_h = mainbackground.getHeight();
				float layout_w = d_mainboard.getWidth();
				float layout_h = d_mainboard.getHeight();
				Matrix matrix = new Matrix();
				matrix.setScale(layout_w/mb_w, layout_h/mb_h);
				canvas.drawBitmap(mainbackground, matrix, new Paint());
			}
		});
		//////////////////////////////////net handler///////////////////////////////////////////////
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					socket1 = new Socket(ip, 9000);
					out1 = socket1.getOutputStream();
					socket2 = new Socket(ip,9001);
					out2= new DataOutputStream(socket2.getOutputStream());
					isconnected = true;
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		d_sc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isconnected)
					try {
						out1.write(205);
						out1.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			}
		});
		d_dc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isconnected)
					try {
						out1.write(206);
						out1.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		});
		d_mainboard.setOnLongClickListener(new  OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if(key_toggle){
					hideKeyboard();
					key_toggle=!key_toggle;
				}
				else {
					showKeyboard();
					
				}
				
				return true;
			}
		});
		d_mainboard.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int x =(int) event.getX();
				int y =(int) event.getY();
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					prex = x;
					prey = y;
					break;
				case MotionEvent.ACTION_MOVE:
					int xd = x-prex;
					int yd = y-prey;
					prex = x;
					prey = y;
					Log.e("my", "( "+xd+" , "+yd+" )");
					try {
						out2.writeInt(xd);
						out2.writeInt(yd);
						out2.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					break;
				}
				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mouse_key, menu);
		
		return true;
	}
	
	private void changeKey() {
		List<Key> keylist = k1.getKeys();
		if (isupper) {
			isupper = false;
			for(Key key:keylist){
				if (key.label!=null && isword(key.label.toString())) {
					key.label = key.label.toString().toLowerCase();
				}
			}
		} else {
			isupper = true;
			for(Key key:keylist){
				if (key.label!=null && isword(key.label.toString())) {
					key.label = key.label.toString().toUpperCase();
				}
			}
		}
	}

    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
            key_toggle=!key_toggle;
        }
    }
    
    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.INVISIBLE);
        }
    }
    
    private boolean isword(String str){
    	String wordstr = "abcdefghijklmnopqrstuvwxyz";
    	if (wordstr.indexOf(str.toLowerCase())>-1) {
			return true;
		}
    	return false;
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			try {
				socket1.close();
				socket2.close();
				out1.close();
				out2.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
		case KeyEvent.KEYCODE_HOME:
			
			break;
		case KeyEvent.KEYCODE_MENU:
			if(key_toggle){
				hideKeyboard();
				key_toggle=!key_toggle;
			}
			else {
				showKeyboard();
				
			}
			
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	
}
