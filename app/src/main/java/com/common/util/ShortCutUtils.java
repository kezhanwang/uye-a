package com.common.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.database.Cursor;
import android.net.Uri;

import com.bjzt.uye.R;
import com.common.file.SharePreShortCut;

/***
 * 快捷方式工具类
 * @date 2015/08/03
 */
public class ShortCutUtils {

	/***
	 * 创建快捷方式
	 * @param context
	 */
	public static final void createShortCut(Activity context){
		boolean isFirst = SharePreShortCut.isFirstOpen(context);
		if(isFirst){
			boolean isHaveShortCut = hasShortcut(context);
			if(isHaveShortCut){
				delShortcut(context);
			}
			addShortcut(context);
			SharePreShortCut.saveFristOpenFlag(context,false);
		}
	}
	
	/**
	 * 判断是否有快捷方式
	 * @param context
	 * @return
	 */
	private static final boolean hasShortcut(Context context){
	        boolean isInstallShortcut = false;
	        final ContentResolver cr = context.getContentResolver();
	        final String AUTHORITY ="com.android.launcher.settings";
	        final Uri CONTENT_URI = Uri.parse("content://" +AUTHORITY + "/favorites?notify=true");
	        Cursor c = cr.query(CONTENT_URI,new String[] {"title","iconResource" },"title=?",
	        new String[] {context.getString(R.string.app_name).trim()}, null);
	        if(c!=null && c.getCount()>0){
	            isInstallShortcut = true ;
	        }
	        return isInstallShortcut ;
	}

	/** 
     * 为程序创建桌面快捷方式 
     */ 
	private static final void addShortcut(Activity context){
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources().getString(R.string.app_name));
        shortcut.putExtra("duplicate", false); //不允许重复创建
        //这个方法已经失效
        //ComponentName comp = new ComponentName(this.getPackageName(), "."+this.getLocalClassName());
        //shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));
        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.setClassName(context, context.getClass().getName());
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		//快捷方式的图标
        ShortcutIconResource iconRes = ShortcutIconResource.fromContext(context, R.drawable.icon);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
		shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        context.sendBroadcast(shortcut);
    }
    
    /**
     * 删除快捷方式
     * @param context
     */
	private static final void delShortcut(Activity context){
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        //快捷方式的名称  
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources().getString(R.string.app_name));
        String appClass = context.getPackageName() + "." +context.getLocalClassName();
        ComponentName comp = new ComponentName(context.getPackageName(), appClass);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));
        context.sendBroadcast(shortcut);  
    }  
}
