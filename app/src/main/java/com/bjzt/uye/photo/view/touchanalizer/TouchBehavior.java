package com.bjzt.uye.photo.view.touchanalizer;

import android.view.MotionEvent;


public abstract class TouchBehavior {
	
	public final static int RET_CONTINUE = 0;
	public final static int RET_CONTINUE_FORCE_MORE = 5;
	public final static int RET_FAILED = 1;
	public final static int RET_MATCHED = 2;
	public final static int RET_MATCHED_AND_CALLED_TRUE = 3;
	public final static int RET_MATCHED_AND_CALLED_FALSE = 4;
	
	protected TouchAnalizer manager;
	protected TouchAnalizer.BehaviorType type;
	private boolean paused = false;
	protected TouchBehaviorEventJudger judger;
	
	public TouchBehavior(TouchAnalizer manager) {
		this.manager = manager;
	}
	
	public void pause() {
		paused = true;
	}
	
	public void setJudger(TouchBehaviorEventJudger judger) {
		this.judger = judger;
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		if (paused) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				paused = false;
			} else {
				return false;
			}
		}

		int ret = RET_CONTINUE;

		try {
			ret = analizeTouchEvent(event);
		} catch (Exception e) {
			ret = RET_FAILED;
			e.printStackTrace();
		}

		if (ret == RET_CONTINUE) {
			return false;
		} else if (ret == RET_FAILED) {
			paused = true;
			return false;
		} else if (ret == RET_MATCHED) {
			return manager.onBehavior(type, event.getX(), event.getY());
		} else if (ret == RET_MATCHED_AND_CALLED_TRUE) {
			return true;
		} else if (ret == RET_MATCHED_AND_CALLED_FALSE) {
			return false;
		} else if (ret == RET_CONTINUE_FORCE_MORE) {
			return true;
		}

		// will not come to this case below.
		paused = true;
		return false;
	}
	
	public abstract int analizeTouchEvent(MotionEvent event);
	
	public interface TouchBehaviorEventJudger {
		public int judgeEvent(TouchAnalizer.BehaviorType type, float x, float y, int state);
	}
	
}
