package com.bjzt.uye.photo.view.touchanalizer;

import android.view.MotionEvent;

public class TouchAnalizer {

	public static enum BehaviorType {
		SINGLE_CLICK,
		DOUBLE_CLICK,
		DRAG,
		SINGLE_DRAG,	// for API level 3-
		SLASH,
		MULTI_SLASH,
		LONG_CLICK,
		//TRIPLE_CLICK,
		PINCH,
		ROTATE, BehaviorType,
	};

	public static int CLICK_AREA = 400;
	public static int CLICK_AREA_SEC = 900;

	private TouchBehaviorListener[] listeners = new TouchBehaviorListener[BehaviorType.values().length];
	private TouchBehavior[] analizers = new TouchBehavior[BehaviorType.values().length];


	public void setListener(BehaviorType behavior, TouchBehaviorListener l) {
		setListener(behavior, l, null);
	}
	public void setListener(BehaviorType behavior, TouchBehaviorListener l, TouchBehavior.TouchBehaviorEventJudger judger) {
		int seq = behavior.ordinal();
		listeners[seq] = l;
		if (l == null) {
			analizers[seq] = null;
		} else {
			if (analizers[seq] == null) {
				analizers[seq] = createTouchBehavior(behavior);
			}
		}
		if (analizers[seq] != null && judger != null) {
			analizers[seq].setJudger(judger);
		}
	}

	private TouchBehavior createTouchBehavior(BehaviorType behavior) {
		switch (behavior) {
		case SINGLE_CLICK:
			return new SingleClickBehavior(this);
		case DOUBLE_CLICK:
			return new DoubleClickBehavior(this);
		case DRAG:
			return new DragBehavior(this);
		case SINGLE_DRAG:
			return new DragBehaviorSinglePoint(this);
		case SLASH:
			return new SlashBehavior(this);
		case MULTI_SLASH:
			return new MultiSlashBehavior(this);
		case LONG_CLICK:
			return new LongClickBehavior(this);
		case PINCH:
			return new PinchBehavior(this);
		case ROTATE:
			return new RotateBehavior(this);
		}
		return null;
	}

	public boolean inputTouchEvent(MotionEvent event) {
		boolean ret = false;
		for (TouchBehavior analizer : analizers) {
			if (analizer != null) {
				if (analizer.onTouchEvent(event)) {
					ret = true;
				}
			}
		}
		return ret;
	}

	public void pauseBehavior(BehaviorType type) {
		if (analizers[type.ordinal()] != null) {
			analizers[type.ordinal()].pause();
		}
	}

	public boolean onBehavior(BehaviorType type, float x, float y) {
		return onBehavior(type, x, y, -1);
	}

	public boolean onBehavior(BehaviorType type, float x, float y, int state) {
		if (listeners[type.ordinal()] != null) {
			return listeners[type.ordinal()].onInvoke(type, x, y, state);
		} else {
			return false;
		}
	}


}
