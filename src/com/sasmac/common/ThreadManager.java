package com.sasmac.common;

import java.util.ArrayList;
import java.util.List;

import com.web.form.TaskInfo;
import com.web.thread.BaseThread;

public class ThreadManager {

	// static ExecutorService TreadPool = Executors.newCachedThreadPool();
	private static List<BaseThread> ThreadMap = new ArrayList<BaseThread>();

	// 正在运行

	/*
	 * public ExecutorService getThreadPool(){ return TreadPool; }
	 */
	public int getnumThreads() {
		int threadCount = ThreadMap.size();
		return threadCount;
	}

	public void submmitJob(BaseThread task) {
		// new Thread(task).start();
		task.start();
		ThreadMap.add(task);
	}

	public void removeJob(BaseThread task) {
		ThreadMap.remove(task);
	}

	public void StopThread(BaseThread task) {
		task.StopThread();
	}

	protected void finalize() {
		// ThreadMap.shutdown();
	}

	public BaseThread findThread(long threadId) {

		int n = ThreadMap.size();
		if (n <= 0)
			return null;

		BaseThread pThread;
		for (int i = 0; i < n; i++) {
			pThread = ThreadMap.get(i);
			if (pThread != null && pThread.getId() == threadId) {
				return pThread;
			}
		}

		return null;
	}

	public List<TaskInfo> getAllTaskInfoByTaskType(String TaskType) {
		List<TaskInfo> pTaskinfoList = new ArrayList<TaskInfo>();
		int n = ThreadMap.size();
		String tasktype = "";
		BaseThread pThread;
		for (int i = 0; i < n; i++) {
			pThread = ThreadMap.get(i);
			if (pThread != null) {
				tasktype = pThread.getTaskInfo().getTaskType();
				if (tasktype.compareToIgnoreCase(TaskType) == 0) {
					pTaskinfoList.add(pThread.getTaskInfo());
				}
			}
		}

		return pTaskinfoList;
	}

	public List<TaskInfo> getAllTaskInfo() {
		List<TaskInfo> pTaskinfoList = new ArrayList<TaskInfo>();
		int n = ThreadMap.size();

		BaseThread pThread;
		for (int i = 0; i < n; i++) {
			pThread = ThreadMap.get(i);
			if (pThread != null) {
				pTaskinfoList.add(pThread.getTaskInfo());
			}
		}

		return pTaskinfoList;
	}

}
