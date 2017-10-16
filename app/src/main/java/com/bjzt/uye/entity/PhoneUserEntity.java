package com.bjzt.uye.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PhoneUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public String FirstName = "";
	public String LastName = "";
	public List<String> Phones = new ArrayList<String>();
	public List<String> Email = new ArrayList<String>();

	public String getPhones(){
		String str = "";
		if(Phones!=null&&Phones.size()>0){
			for(int i = 0;i < Phones.size();i++){
				str += Phones.get(i) + " ";
			}
		}
		return str;
	}
}
