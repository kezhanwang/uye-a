package com.bjzt.uye.photo.view.touchanalizer;

import android.view.MotionEvent;

import java.util.ArrayList;

public class MultiSlashBehavior extends TouchBehavior {

	private final static int TIME_BETWEEN_TOUCHDOWN = 300;
	
	private long lastTime = -1;
	private ArrayList<Float> pathOne, pathTwo;
	
	public MultiSlashBehavior(TouchAnalizer manager) {
		super(manager);
		type = TouchAnalizer.BehaviorType.MULTI_SLASH;
	}

	@Override
	public int analizeTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (pathOne == null) {
				pathOne = new ArrayList<Float>();
			}
			pathOne.clear();
			pathOne.add(event.getX());
			pathOne.add(event.getY());
			lastTime = System.currentTimeMillis();
			break;
		case MotionEvent.ACTION_UP:
			pathOne.add(event.getX());
			pathOne.add(event.getY());
			break;
		case MotionEvent.ACTION_POINTER_1_DOWN:
			if (pathTwo == null) {
				pathTwo = new ArrayList<Float>();
			}
			pathTwo.clear();
			pathTwo.add(event.getX());
			pathTwo.add(event.getY());
			break;
		case MotionEvent.ACTION_POINTER_1_UP:
			pathTwo.add(event.getX());
			pathTwo.add(event.getY());
			break;
		case MotionEvent.ACTION_POINTER_2_DOWN:
			manager.pauseBehavior(TouchAnalizer.BehaviorType.MULTI_SLASH);
			break;
		case MotionEvent.ACTION_MOVE:
			pathOne.add(event.getX(0));
			pathOne.add(event.getY(0));
			if (pathTwo != null) {
				pathTwo.add(event.getX(1));
				pathTwo.add(event.getY(1));
			}
			break;
		}
		return 0;
	}

}
