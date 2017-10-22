package com.bjzt.uye.photo.view.touchanalizer;

import android.view.MotionEvent;

public class RotateBehavior extends TouchBehavior {
	
//	private final static long TIME_BETWEEN_CLICK = 800;
	
	private float centerX, centerY;
	private float pointerX1, pointerY1;
	private float pointerX2, pointerY2;
	private float degree = 0;
	
	public final static int ROTATE_START = 0;
	public final static int ROTATE_MOVE = 1;
	public final static int ROTATE_END = 2;

	public RotateBehavior(TouchAnalizer manager) {
		super(manager);
		type = TouchAnalizer.BehaviorType.ROTATE;
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
			degree = getDegree();
			break;
		case MotionEvent.ACTION_POINTER_2_DOWN:
			pointerX2 = event.getX(1);
			pointerY2 = event.getY(1);
			centerX = (pointerX2 + pointerX1) / 2;
			centerY = (pointerY1 + pointerY2) / 2;
			degree = getDegree();
			boolean b = manager.onBehavior(TouchAnalizer.BehaviorType.ROTATE, centerX, centerY, ROTATE_START);
			ret = b ? RET_MATCHED_AND_CALLED_TRUE : RET_MATCHED_AND_CALLED_FALSE;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_1_UP:
		case MotionEvent.ACTION_POINTER_2_UP:
			b = manager.onBehavior(TouchAnalizer.BehaviorType.ROTATE, 0, 0, ROTATE_END);
			ret = b ? RET_MATCHED_AND_CALLED_TRUE : RET_MATCHED_AND_CALLED_FALSE;
			break;
		case MotionEvent.ACTION_MOVE:
			pointerX1 = event.getX();
			pointerY1 = event.getY();
			if (event.getPointerCount() == 2) {
				pointerX2 = event.getX(1);
				pointerY2 = event.getY(1);
				float d = getDegree();
				b = manager.onBehavior(TouchAnalizer.BehaviorType.ROTATE, degree, d, ROTATE_MOVE);
//				degree = d;
				ret = b ? RET_MATCHED_AND_CALLED_TRUE : RET_MATCHED_AND_CALLED_FALSE;
			}
			break;
		}
		return ret;
	}

	private float getDegree() {
		float dx = (pointerX2 - pointerX1);
		if (dx == 0) {
			dx = 0.0000001f;
		}
		float ret = (float) Math.atan((pointerY2 - pointerY1) / Math.abs(pointerX2 - pointerX1));
		if (dx < 0) {
			ret = (float) (Math.PI - ret);
		}
		return ret;
	}
	
}
