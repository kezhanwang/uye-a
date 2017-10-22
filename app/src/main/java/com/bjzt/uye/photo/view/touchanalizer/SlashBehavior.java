package com.bjzt.uye.photo.view.touchanalizer;

import android.view.MotionEvent;

import java.util.ArrayList;

public class SlashBehavior extends TouchBehavior {

	private ArrayList<Float> path;
	private ArrayList<Integer> dirPath;
	
	public final static int ACTION_MASK = 0xf0;
	public final static int ACTION_DOWN = 0x10;
	public final static int ACTION_MOVE = 0x20;
	public final static int ACTION_UP = 0x40;
	public final static int DIRECTION_MASK = 0x0f;
	
	public static int caculatePointDirection(float x1, float y1, float x2, float y2) {
		float dx = x2 - x1;
		float dy = y2 - y1;
		int ret = 5;
		float tan = Math.abs(dx / dy);
		if (tan > 2.414) {
			ret = dx > 0 ? 6 : 4;
		} else if (tan < 0.414) {
			ret = dy > 0 ? 2 : 8;
		} else {
			ret = (dx > 0 ? 3 : 1) + (dy > 0 ? 0 : 6);
		}
		return ret;
	}
	
	// public static int caculatePathDirection(ArrayList<Float> p) {
	// int ret = 5;
	// if (p == null || p.size() <= 6) {
	//
	// } else {
	// int point = p.size() / 2;
	// int[] pathDir = new int[point - 1];
	// int[] tmpCnt = new int[9];
	// for (int i = 0; i <= point - 2; i++) {
	// pathDir[i] = caculatePointDirection(p.get(i * 2), p.get(i * 2 + 3), p.get(i * 2 + 2), p.get(i * 2 + 1));
	// tmpCnt[pathDir[i] - 1]++;
	// }
	// for (int d = 0; d < 9; d++) {
	// if (tmpCnt[d] > point * 2 / 3) {
	// ret = d + 1;
	// break;
	// }
	// }
	// }
	// return ret;
	// }
			
	public static int caculateDirection(ArrayList<Integer> p) {
		if (p == null || p.size() < 3) {
			return 5;
		}
		int count = p.size();
		int[] tmpCnt = new int[9]; 
		int ret = 5;
		for (int d : p) {
			tmpCnt[d - 1]++;
		}
		for (int d = 0; d < 9; d++) {
			if (tmpCnt[d] > count * 2 / 3) {
				ret = d + 1;
				break;
			}
		}
		return ret;
	}
	
	public SlashBehavior(TouchAnalizer manager) {
		super(manager);
		type = TouchAnalizer.BehaviorType.SLASH;
	}

	@Override
	public int analizeTouchEvent(MotionEvent event) {
		int ret = RET_CONTINUE;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (path == null) {
				path = new ArrayList<Float>();
			}
			path.clear();
			if (dirPath == null) {
				dirPath = new ArrayList<Integer>();
			}
			dirPath.clear();
			path.add(event.getX());
			path.add(event.getY());
			manager.onBehavior(TouchAnalizer.BehaviorType.SLASH, event.getX(), event.getY(), 5 | ACTION_DOWN);
			break;
		case MotionEvent.ACTION_MOVE:
			path.add(event.getX());
			path.add(event.getY());
			int count = path.size();
				dirPath.add(caculatePointDirection(	path.get(count - 4), path.get(count - 3), path.get(count - 2),
													path.get(count - 1)));
			int dir = caculateDirection(dirPath);
			if (dir != 5) {
					boolean b = manager.onBehavior(	TouchAnalizer.BehaviorType.SLASH, path.get(path.size() - 2) - path.get(0),
													path.get(path.size() - 1) - path.get(1), dir | ACTION_MOVE);
				ret = b ? RET_MATCHED_AND_CALLED_TRUE : RET_MATCHED_AND_CALLED_FALSE;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_OUTSIDE:
			path.add(event.getX());
			path.add(event.getY());
			count = path.size();
				dirPath.add(caculatePointDirection(	path.get(count - 4), path.get(count - 3), path.get(count - 2),
													path.get(count - 1)));
			dir = caculateDirection(dirPath);
			if (dir != 5) {
					boolean b = manager.onBehavior(TouchAnalizer.BehaviorType.SLASH, path.get(count - 2) - path.get(0), path.get(count - 1)
							- path.get(1), dir | ACTION_UP);
				ret = b ? RET_MATCHED_AND_CALLED_TRUE : RET_MATCHED_AND_CALLED_FALSE;
			}
			break;
		case MotionEvent.ACTION_POINTER_2_DOWN:
			manager.pauseBehavior(TouchAnalizer.BehaviorType.SLASH);
		default:
			break;
		}
		return ret;
	}

}
