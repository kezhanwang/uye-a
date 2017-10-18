package com.bjzt.uye.comparator;

import java.io.File;
import java.util.Comparator;

public class FileComparator implements Comparator<File> {

	@Override
	public int compare(File file1, File file2) {
		// TODO Auto-generated method stub
		String nameLeft = file1.getName();
		String nameRight = file2.getName();
		return nameLeft.compareTo(nameRight);
	}
}
