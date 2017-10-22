package com.bjzt.uye.photo.view.touchanalizer;

import android.view.MotionEvent;

public class DragBehaviorSinglePoint extends TouchBehavior {

	public final static int DRAG_START = 0;
	public final static int DRAG_MOVE = 1;
	public final static int DRAG_END = 2;
	
	private float lastX, lastY;
	private float deltaX, deltaY;
	private float oriX, oriY;
	
	private int dragPoint = -1;
	
	public DragBehaviorSinglePoint(TouchAnalizer manager) {
		super(manager);
		type = TouchAnalizer.BehaviorType.SINGLE_DRAG;
	}

	@Override
	public int analizeTouchEvent(MotionEvent event) {
		int ret = RET_CONTINUE;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			dragPoint = 0;
			oriX = lastX = event.getX();
			oriY = lastY = event.getY();
			deltaX = deltaY = 0;
			ret = manager.onBehavior(TouchAnalizer.BehaviorType.DRAG, oriX, oriY, DRAG_START) ? RET_MATCHED_AND_CALLED_TRUE : RET_CONTINUE;
			break;
		case MotionEvent.ACTION_MOVE:
			deltaX += event.getX() - lastX;
			deltaY += event.getY() - lastY;
			lastX = event.getX();
			lastY = event.getY();
			ret = manager.onBehavior(TouchAnalizer.BehaviorType.DRAG, oriX + deltaX, oriY + deltaY, DRAG_MOVE) ? RET_MATCHED_AND_CALLED_TRUE : RET_CONTINUE;
			break;
		case MotionEvent.ACTION_UP:
			ret = manager.onBehavior(TouchAnalizer.BehaviorType.DRAG, oriX + deltaX, oriY + deltaY, DRAG_END) ? RET_MATCHED_AND_CALLED_TRUE : RET_CONTINUE;
			deltaX = deltaY = 0;
			manager.pauseBehavior(TouchAnalizer.BehaviorType.DRAG);
			break;
		default:
			ret = RET_CONTINUE;
			break;
		}
		return ret;
	}
	

}
