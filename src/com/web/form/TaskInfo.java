package com.web.form;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.web.common.THREADSTATUS;;

public class TaskInfo {

	private String taskType;
	private int taskStatus;
	private String taskName;
	private String starttime;
	private String finishtime;
	private int Progress;
	private String markinfo; //备注信息
	private long taskId;
	
	public TaskInfo()
	{
		taskId=-1;
		taskType="";
		taskStatus=THREADSTATUS.THREAD_STATUS_UNKNOWN.ordinal();
		starttime="";
		finishtime="";
		Progress=0;
		markinfo="";
		taskName="";
	}
	public TaskInfo(String taskType,int taskStatus,String taskName,
	 Date starttime,Date finishtime,int iprogress,String markinfo )
	{
		this.taskType=taskType;
		this.taskStatus=taskStatus;
		this.Progress=iprogress;
		this.markinfo=markinfo;
		this.taskName=taskName;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(starttime ==null ) this.starttime="";
		else this.starttime=sdf.format(starttime);
		
		if(finishtime ==null ) this.finishtime="";
		else  this.finishtime=sdf.format(finishtime); 
	}
	
	public String getStartTime() throws ParseException {
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		//return sdf.parse(starttime);
		return starttime;
	}
	public void setStartTime(Date starttime) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(starttime ==null ) this.starttime="";
		else this.starttime=sdf.format(starttime);
	}
	public void setStartTime(String  starttime) {
		this.starttime=starttime;
	}
	public String getfinishTime() throws ParseException {
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		//return sdf.parse(finishtime);
		return finishtime;
	}
	public void setTaskEndTime(Date finishtime) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(finishtime ==null ) this.finishtime="";
		else  this.finishtime=sdf.format(finishtime); 
	}
	public String getTaskEndTime() {
		return finishtime;
	}
	public void setfinishTime(String finishtime) {
		this.finishtime=finishtime;
	}
	public int getProgress() {
		return Progress;
	}
	public void setProgress(int iprogress) {
		this.Progress = iprogress;
	}

	public long getTaskId() {
		return taskId;
	}
	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public int getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(int taskStatus) {
		this.taskStatus = taskStatus;
	}
	
	public String getMarkinfo() {
		return markinfo;
	}
	public void setMarkinfo(String markinfo) {
		this.markinfo = markinfo;
	}

	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
}
