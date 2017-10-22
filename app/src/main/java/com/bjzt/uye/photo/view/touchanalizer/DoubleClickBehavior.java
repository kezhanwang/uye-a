package com.bjzt.uye.photo.view.touchanalizer;

import android.view.MotionEvent;

public class DoubleClickBehavior extends TouchBehavior {
	
	private final static int CLICK_AREA = 400;
	private final static int AREA_BETWEEN_CLICK = 900;
	private final static long TIME_BETWEEN_CLICK = 500;
	
	private long lastTime = -1;
	private float lastPosX = -1, lastPosY = -1;
	private int state = 0;
	DoubleClickBehavior(TouchAnalizer manager) {
		super(manager);
		type = TouchAnalizer.BehaviorType.DOUBLE_CLICK;
	}
	
	private boolean checkOutside(MotionEvent curr, int type) {
		if (curr == null || lastPosX == -1 || lastPosY == -1) {
			return true;
		}
		float dx = lastPosX - curr.getX();
		float dy = lastPosY - curr.getY();
		boolean ret = type == 0 ? (dx * dx + dy * dy > CLICK_AREA)
						: ((dx * dx + dy * dy > AREA_BETWEEN_CLICK) || (System.currentTimeMillis() - lastTime > TIME_BETWEEN_CLICK));
		return ret;
	}

	@Override
	public int analizeTouchEvent(MotionEvent event) {
		int ret = RET_CONTINUE;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (state == 0) {
				state = 1;
				ret = RET_CONTINUE;
			} else if (state == 2) {
				if (checkOutside(event, 1)) {
					ret = RET_FAILED;
				} else {
					state = 3;
					ret = RET_CONTINUE;
				}
			} else {
				ret = RET_FAILED;
			}
			lastTime = System.currentTimeMillis();
			lastPosX = event.getX();
			lastPosY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (checkOutside(event, 0)) {
				ret = RET_FAILED;
			} else {
				ret = RET_CONTINUE;
			}
			break;
		case MotionEvent.ACTION_UP:
			if (checkOutside(event, 0)) {
				ret = RET_FAILED;
			} else {
				if (state == 1) {
					state = 2;
					ret = RET_CONTINUE;
				} else if (state == 3) {
					ret = RET_MATCHED;
				} else {
					ret = RET_FAILED;
				}
				lastTime = System.currentTimeMillis();
				lastPosX = event.getX();
				lastPosY = event.getY();
			}
			break;
		default:
			ret = RET_FAILED;
			break;
		}
		if (ret == RET_FAILED || ret == RET_MATCHED) {
			state = 0;
			lastTime = -1;
			lastPosX = lastPosY = -1;
		}
		return ret;
	}

}
