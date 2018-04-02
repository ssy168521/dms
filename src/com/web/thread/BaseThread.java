package com.web.thread;
import java.sql.Connection;
import java.text.ParseException;
import java.util.Date;

import net.sf.json.JSONArray;

import com.sasmac.common.ThreadManager;
import com.web.common.THREADSTATUS;
import com.web.form.*;
import com.web.util.DataUtils;

public class BaseThread extends Thread  {
   private TaskInfo pTaskinfo;
   protected boolean bStopThread;
   public BaseThread()
   {
	   pTaskinfo = new TaskInfo();
	   bStopThread=false;
   }
   
   public BaseThread(String taskType,int taskStatus,String taskName,
			 Date starttime,Date finishtime,int iprogress,String markinfo )
			{
	            pTaskinfo = new TaskInfo(taskType, taskStatus, taskName,
				  starttime, finishtime, iprogress, markinfo);
	             bStopThread=false;
	             pTaskinfo.setTaskId(getId());
			}
   
   public void StopThread()
   {
	   bStopThread=true;
	   pTaskinfo.setTaskStatus(THREADSTATUS.THREAD_STATUS_STOPED.ordinal());
	   ThreadManager pPool =new ThreadManager();
	   pPool.removeJob(this);
   }
   public void FinishThread()
   {
	   pTaskinfo.setTaskStatus(THREADSTATUS.THREAD_STATUS_FINISHED.ordinal());
	   ThreadManager pPool =new ThreadManager();
	   pPool.removeJob(this);
   }
   public String getTaskStartTime() throws ParseException {
		return pTaskinfo.getStartTime();
	}
	public void setTaskStartTime(Date starttime) {
		pTaskinfo.setStartTime(starttime);
	}
	public String getTaskEndTime() throws ParseException {
		return pTaskinfo.getfinishTime();
	}
	public void setTaskEndTime(Date finishtime) {
		pTaskinfo.setTaskEndTime(finishtime);;
	}
	
	public int getTaskProgress() {
		return pTaskinfo.getProgress();
	}
	public void setTaskProgress(int TaskProgress) {
		pTaskinfo.setProgress(TaskProgress);
	}
	/*public int getTaskId() {
		return pTaskinfo.getTaskId();
	}
	public void setTaskId(int taskId) {
		pTaskinfo.setTaskId(taskId);;
	}*/
	public String getTaskType() {
		return pTaskinfo.getTaskType();
	}
	public void setTaskType(String taskType) {
		pTaskinfo.setTaskType(taskType);;
	}
	public int getTaskStatus() {
		return pTaskinfo.getTaskStatus();
	}
	public void setTaskStatus(int taskStatus) {
		pTaskinfo.setTaskStatus(taskStatus);
	}
	
	public String getTaskMarkinfo() {
		return pTaskinfo.getMarkinfo();
	}
	public void setTaskMarkinfo(String markinfo) {
		pTaskinfo.setMarkinfo(markinfo);
	}
	public String getTaskName() {
		return pTaskinfo.getTaskName();
	}
	public void setTaskName(String taskName) {
		pTaskinfo.setTaskName(taskName);
	}
	
	public String PrintTaskInfo2JSON()
	{		
		JSONArray jsArray=JSONArray.fromObject(pTaskinfo);
		String str=jsArray.toString();
		return str;
	}
	public TaskInfo getTaskInfo()
	{
		return this.pTaskinfo;
	}
	
	public boolean PrintTaskInfo(Connection conn)
	{
		if(conn == null) return false;

        String sql = "insert into  taskinfo(taskId,taskName,taskType,taskStatus,starttime,finishtime,Progress,markinfo) values(?,?,?,?,?,?,?,?)";
		
    	try {	
    		Object[] params={getId(), getTaskName(),getTaskType(),getTaskStatus(),getTaskStartTime(),getTaskEndTime(),getTaskProgress(),getTaskMarkinfo()};
			int c= new DataUtils(conn).update(sql, params);
			if(c<=0) return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
