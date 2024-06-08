package com.project.peoplescorner.data;

public class statusReply {
	
	private String status;
	private String reply;
	private String reply_datetime;
	
	public String getStatus(){
		return status;
	}

	public void setStatus(String newstatus)
	{
		status=newstatus;
	}

	public String getReply(){
		return reply;
	}

	public void setReply(String newReply)
	{
		reply=newReply;
	}

	public String getDate(){
		return reply_datetime;
	}

	public void setDate(String newDate)
	{
		reply_datetime=newDate;
	}

	
}
