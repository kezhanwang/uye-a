package com.bjzt.uye.photo.view.touchanalizer;

import android.view.MotionEvent;

public class PinchBehavior extends TouchBehavior {
	
//	private final static long TIME_BETWEEN_CLICK = 800;
	
	private float centerX, centerY;
	private float pointerX1, pointerY1;
	private float pointerX2, pointerY2;
	private float distance = 0;
	
	public final static int PINCH_START = 0;
	public final static int PINCH_MOVE = 1;
	public final static int PINCH_END = 2;

	public PinchBehavior(TouchAnalizer manager) {
		super(manager);
		type = TouchAnalizer.BehaviorType.PINCH;
	}

	@Override
	public int analizeTouchEvent(MotionEvent event) {
		int ret = RET_CONTINUE;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			pointerX1 = event.getX();
			pointerY1 = event.getY();
			break;
		case MotionEvent.ACTION_POINTER_1_DOWN:
			pointerX1 = event.getX();
			pointerY1 = event.getY();
			distance = getDistance();
			break;
		case MotionEvent.ACTION_POINTER_2_DOWN:
			pointerX2 = event.getX(1);
			pointerY2 = event.getY(1);
			centerX = (pointerX2 + pointerX1) / 2;
			centerY = (pointerY1 + pointerY2) / 2;
			distance = getDistance();
			boolean b = manager.onBehavior(TouchAnalizer.BehaviorType.PINCH, centerX, centerY, PINCH_START);
			ret = b ? RET_MATCHED_AND_CALLED_TRUE : RET_MATCHED_AND_CALLED_FALSE;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_1_UP:
		case MotionEvent.ACTION_POINTER_2_UP:
			b = manager.onBehavior(TouchAnalizer.BehaviorType.PINCH, 0, 0, PINCH_END);
			ret = b ? RET_MATCHED_AND_CALLED_TRUE : RET_MATCHED_AND_CALLED_FALSE;
			break;
		case MotionEvent.ACTION_MOVE:
			pointerX1 = event.getX();
			pointerY1 = event.getY();
			if (event.getPointerCount() == 2) {
				pointerX2 = event.getX(1);
				pointerY2 = event.getY(1);
				float d = getDistance();
				b = manager.onBehavior(TouchAnalizer.BehaviorType.PINCH, d, distance, PINCH_MOVE);
				distance = d;
				ret = b ? RET_MATCHED_AND_CALLED_TRUE : RET_MATCHED_AND_CALLED_FALSE;
			}
			break;
		}
		return ret;
	}

	private float getDistance() {
		float ret = (float) Math.sqrt((pointerX2 - pointerX1) * (pointerX2 - pointerX1)
				+ (pointerY2 - pointerY1) * (pointerY2 - pointerY1));
		return ret;
	}

}
