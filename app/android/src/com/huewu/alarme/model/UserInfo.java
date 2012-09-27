package com.huewu.alarme.model;

public class UserInfo extends JsonModel {
	
	public String uid;	//user id
	public String uname;	//user name
	public String rid;	//GCM registeration id
	public String cid;	//clock id.

	public UserInfo(){
		
	}
	
	public UserInfo(String uname, String rid, String cid) {
		this.uname = uname;
		this.rid = rid;
		this.cid = cid;
	}

	@Override
	public String toPostData() {
		return String.format("uname=%s&rid=%s&cid=%s", uname, rid, cid);
	}

}//end of class
