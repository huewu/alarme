package com.huewu.alarme.model;

public class UserInfo {
	
	public String uid;	//user id
	public String uname;	//user name
	public String rid;	//GCM registeration id
	public String cid;	//clock id.
	public String msg; //message
	
	@Override
	public String toString() {
		return String.format("uname=%s&rid=%s&cid=%s", uname, rid, cid);
	}

}//end of class
