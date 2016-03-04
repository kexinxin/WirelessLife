package com.example.viewpager;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
  
public class SelectPicPopupWindow extends PopupWindow {   
  
  
    public Button btn_bangzhu, btn_tuanduijieshao, btn_gengxin,btn_zizhuwomen,btn_cancel;   
    public View mMenuView;   
  
    public SelectPicPopupWindow(Activity context,OnClickListener itemsOnClick) {   
        super(context);   
        LayoutInflater inflater = (LayoutInflater) context   
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);   
        mMenuView = inflater.inflate(R.layout.huachu, null);   
        btn_bangzhu = (Button) mMenuView.findViewById(R.id.btn_bangzhu);   
        btn_tuanduijieshao = (Button) mMenuView.findViewById(R.id.btn_tuanduijieshao);   
        btn_gengxin = (Button) mMenuView.findViewById(R.id.btn_gengxin);  
        btn_zizhuwomen=(Button) mMenuView.findViewById(R.id.btn_zizhuwomen);
        btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
        
        //取消按钮   
        btn_cancel.setOnClickListener(new OnClickListener() {   
  
            public void onClick(View v) {   
                //销毁弹出框   
                dismiss();   
            }   
        });   
        //设置按钮监听   
        btn_tuanduijieshao.setOnClickListener(itemsOnClick);   
        btn_bangzhu.setOnClickListener(itemsOnClick); 
        btn_gengxin.setOnClickListener(itemsOnClick); 
        btn_zizhuwomen.setOnClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View   
        this.setContentView(mMenuView);   
        //设置SelectPicPopupWindow弹出窗体的宽   
        this.setWidth(LayoutParams.FILL_PARENT);   
        //设置SelectPicPopupWindow弹出窗体的高   
        this.setHeight(LayoutParams.WRAP_CONTENT);   
        //设置SelectPicPopupWindow弹出窗体可点击   
        this.setFocusable(true);   
           
        //实例化一个ColorDrawable颜色为半透明   
        ColorDrawable dw = new ColorDrawable(0xb0000000);   
        //设置SelectPicPopupWindow弹出窗体的背景   
        this.setBackgroundDrawable(dw);   
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框   
        mMenuView.setOnTouchListener(new OnTouchListener() {   
               
            public boolean onTouch(View v, MotionEvent event) {   
                   
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();   
                int y=(int) event.getY();   
                if(event.getAction()==MotionEvent.ACTION_UP){   
                    if(y<height){   
                        dismiss();   
                    }   
                }                  
                return true;   
            }   
        });   
  
    }   
  
}  