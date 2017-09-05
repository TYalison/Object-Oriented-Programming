package ifttt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Scanning implements Runnable
{
	Scanner in = new Scanner(System.in);
	IF if_trigger = new IF();
	THEN then_task = new THEN();
	ArrayList<aFile> Files_former = new ArrayList<aFile>();
	ArrayList<aFile> Files_latter = new ArrayList<aFile>();
	ArrayList<Task> Tasks = new ArrayList<Task>();
	ArrayList<String> workspace = new ArrayList<String>();
	boolean judgeTask(String str)
	{
		String[] tag = new String[6];
		tag = str.split("\\ ");
		if(tag.length == 5)
		{
			if(tag[0].equals("IF") &&
			  (tag[1].charAt(0) == '[' && tag[1].charAt(tag[1].length()-1) == ']') &&
			  (tag[2].equals("size-changed") || tag[2].equals("renamed") || tag[2].equals("modified") || tag[2].equals("path-changed")) &&
			   tag[3].equals("THEN") &&
			  (tag[4].equals("record-summary") || tag[4].equals("record-detail") || tag[4].equals("recover")))
			{
				return true;
			}	
		}
		return false;
	}
	void judgePath()
	{
		for(int i=0; i<Tasks.size(); i++)
		{
			File file_temp = new File(Tasks.get(i).file_path);
			if(!file_temp.exists())
			{
				System.out.println(Tasks.get(i).file_path + " is not existent!");
				Tasks.remove(i);
				i--;
			}
			else
			{
				if(Tasks.get(i).THEN_type.equals("recover") && (!Tasks.get(i).IF_type.equals("renamed")) && (!Tasks.get(i).IF_type.equals("path-changed")))
				{
					Tasks.remove(i);
					i--;
				}
			}
		}
		if(Tasks.isEmpty())
		{
			System.out.println("No valid task!");
			System.exit(0);
		}
	}
	void initFile(Task task_temp)//获得工作区所有文件
	{
		File file_temp = new File(task_temp.file_path);
		if(file_temp.exists())
		{
			if(file_temp.isFile())
			{
				aFile new_target = new aFile(task_temp.file_path);
				new_target.IF_type = task_temp.IF_type;
				new_target.THEN_type = task_temp.THEN_type;
				Files_former.add(new_target);
			}
			else
			{
				if(file_temp.isDirectory())
				{
					aFile new_target = new aFile(task_temp.file_path);
					new_target.IF_type = task_temp.IF_type;
					new_target.THEN_type = task_temp.THEN_type;
					Files_former.add(new_target);
					File[] files = file_temp.listFiles();
					for(int i=0; i<files.length; i++)
					{
						Task new_task = new Task();
						new_task.file_path = files[i].getAbsolutePath();
						new_task.IF_type = task_temp.IF_type;
						new_task.THEN_type = task_temp.THEN_type;
						initFile(new_task);
					}	
				}
			}
		}
	}
	void determineWorkspace(String path_temp)//if not exists,return false!
	{
		File file_temp = new File(path_temp);
		if(file_temp.exists())
		{
			if(file_temp.isFile())
			{
				if(workspace.isEmpty())
				{
					workspace.add(new File(file_temp.getParent()).getAbsolutePath());
				}
				else
				{
					boolean found = false;
					for(String path : workspace)
					{
						String new_path = new File(file_temp.getParent()).getAbsolutePath();
						if(path.equals(new_path))
						{
							found = true;
							break;
						}
					}
					if(!found)
					{
						workspace.add(new File(file_temp.getParent()).getAbsolutePath());
					}
				}
				
			}
			else if(file_temp.isDirectory())
			{
				if(workspace.isEmpty())
				{
					workspace.add(new File(path_temp).getAbsolutePath());
				}
				else
				{
					boolean found = false;
					for(String path : workspace)
					{
						if(path.equals(file_temp.getAbsolutePath()))
						{
							found = true;
							break;
						}
					}
					if(!found)
					{
						workspace.add(new File(path_temp).getAbsolutePath());
					}
				}
			}
		}
	}
	void updateFile(String workspace_temp)
	{
		File file_temp = new File(workspace_temp);
		if(file_temp.exists())
		{
			if(file_temp.isFile())
			{
				aFile new_target = new aFile(workspace_temp);
				Files_latter.add(new_target);
			}
			else
			{
				if(file_temp.isDirectory())
				{
					aFile new_target = new aFile(workspace_temp);
					Files_latter.add(new_target);
					File[] files = file_temp.listFiles();
					for(int i=0; i<files.length; i++)
					{
						String new_workspace = files[i].getAbsolutePath();
						updateFile(new_workspace);
					}	
				}
			}
		}
	}
	@Override
	public void run()
	{
		while(true)
		{
			String str = in.nextLine();
			if(str.equals("END"))
			{
				break;
			}
			else
			{
				if(judgeTask(str))
				{
					Task task_temp = new Task();
					String[] tag = str.split(" ");
					task_temp.file_path = tag[1].substring(1,tag[1].length()-1);
					task_temp.IF_type = tag[2];
					task_temp.THEN_type = tag[4];
					Tasks.add(task_temp);
				}
			}
		}
		in.close();
		judgePath();
		for(int i=0; i<Tasks.size(); i++)
		{
			initFile(Tasks.get(i));
		}
		for(int i=0; i<Tasks.size(); i++)
		{
			determineWorkspace(Tasks.get(i).file_path);
		}
		while(true)
		{
			for(int i=0; i<workspace.size(); i++)
			{
				updateFile(workspace.get(i));
			}
			if_trigger.judgeIF(Files_former, Files_latter, then_task);
			Files_latter.clear();
			try
			{
				then_task.dealTask();
			}
			catch (IOException e) {}
			then_task.details.clear();
			try
			{
				Thread.sleep(10000);
			}
			catch (InterruptedException e)
			{
				System.out.println("Sorry to Interrupt!");
			}
		}	
	}
}
