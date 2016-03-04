package com.example.viewpager;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class MYViewPagerAdepter extends PagerAdapter{

	
private ArrayList<View> views;
	
	
	public void setViews(ArrayList<View> views) {
		this.views = views;
	}

	
	public int getCount() {
		return views.size();
	}

	
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	
	public void destroyItem(View container, int position, Object object) {
		
		((ViewPager)container).removeView(views.get(position));
	}

	
	public Object instantiateItem(View container, int position) {
		((ViewPager)container).addView(views.get(position));
		return views.get(position);
	}
}
