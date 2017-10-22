package com.bjzt.uye.photo.view.touchanalizer;

import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;

public class LongClickBehavior extends TouchBehavior {
	
	private final static int CLICK_AREA = TouchAnalizer.CLICK_AREA;
	
	private final static long TRIG_TIME = 600;
	private boolean trigged = false;
	private float lastPosX = -1, lastPosY = -1;

	public LongClickBehavior(TouchAnalizer manager) {
		super(manager);
		type = TouchAnalizer.BehaviorType.LONG_CLICK;
	}
	
	private boolean checkOutside(MotionEvent curr) {
		if (curr == null || lastPosX == -1 || lastPosY == -1) {
			return true;
		}
		float dx = lastPosX - curr.getX();
		float dy = lastPosY - curr.getY();
		boolean ret = dx * dx + dy * dy > CLICK_AREA;
		return ret;
	}
	
	private Handler trigger = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			trigged = true;
			manager.pauseBehavior(TouchAnalizer.BehaviorType.SINGLE_CLICK);
			manager.pauseBehavior(TouchAnalizer.BehaviorType.DOUBLE_CLICK);
			manager.onBehavior(type, msg.arg1, msg.arg2);
		}
		
	};

	@Override
	public int analizeTouchEvent(MotionEvent event) {
		int ret = RET_CONTINUE;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastPosX = event.getX();
			lastPosY = event.getY();
			trigged = false;
			trigger.sendMessageDelayed(trigger.obtainMessage(0, (int)event.getX(), (int)event.getY()), TRIG_TIME);
			break;
		case MotionEvent.ACTION_MOVE:
			if (trigged) {
				ret = RET_CONTINUE;
			} else if (checkOutside(event)) {
				trigger.removeMessages(0);
				ret = RET_FAILED;
			} else {
				ret = RET_CONTINUE;
			}
			break;
		case MotionEvent.ACTION_UP:
			trigger.removeMessages(0);
			ret = RET_FAILED;
			break;
		default:
			trigger.removeMessages(0);
			ret = RET_FAILED;
			break;
		}
		if (ret == RET_FAILED || ret == RET_MATCHED) {
			lastPosX = lastPosY = -1;
		}
		return ret;
	}

}
