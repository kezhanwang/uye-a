package com.bjzt.uye.photo.view.touchanalizer;


public interface TouchBehaviorListener {
	public boolean onInvoke(TouchAnalizer.BehaviorType type, float x, float y, int state);
}

