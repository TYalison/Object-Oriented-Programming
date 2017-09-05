package ifttt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


class Summary
{
	volatile int summary_size = 0;
	volatile int summary_path = 0;
	volatile int summary_modified = 0;
	volatile int summary_renamed = 0;
}
class Detail//路径移动和重命名仅针对文件
{
	volatile String IF_type = "";//size-changed || renamed || modified || path-changed
	volatile String THEN_type = "";//record-summary || record-detail || recover
	long old_size = 0;
	long new_size = 0;
	String old_name = "";
	String new_name = "";
	long old_time = 0;
	long new_time = 0;
	String old_path = "";
	String new_path = "";
}
class Task
{
	volatile String file_path = "";
	volatile String IF_type = "";
	volatile String THEN_type = "";
}
public class Test implements file_operator
{
	//file-operator
	public boolean newFile(String path) throws IOException
	{
		File newFile = new File(path);
		if(!newFile.exists())
		{
			newFile.createNewFile();
			return true;
		}
		else
		{
			System.out.println(path + " already exits!");
		}
		return false;
	}
	public boolean renameFile(String old_path,String new_path)
	{
		if(!old_path.equals(new_path))
		{
			File old_file=new File(old_path);
			File new_file=new File(new_path);
		    if((!new_file.exists()) && old_file.getParent().equals(new_file.getParent()))
		    {
		    	old_file.renameTo(new_file);
		    	return true;
		    }
		    else
		    {
		    	System.out.println(new_path + " already exists!");
		    }
		}
		return false;
	}
	public boolean moveFile(String oldpath, String newpath)
	{
		if(!oldpath.equals(newpath))
		{
			File oldfile = new File(oldpath);
	        File newfile = new File(newpath);
	        if(!newfile.exists() && oldfile.getName().equals(newfile.getName()))
	        {
	        	oldfile.renameTo(newfile);
	        	return true;
	        }
	    }
		else
		{
			System.out.println("No need moving " + oldpath + "!");
		}
		return false;
	}
	public void writeFile(String path,String content) throws IOException
	{
		File file =new File(path);
		if(!file.exists())
		{
			file.createNewFile();
		}
		//true = append file
		FileWriter file_writter = new FileWriter(path,true);
		BufferedWriter buffer_writter = new BufferedWriter(file_writter);
		buffer_writter.write(content);
		buffer_writter.close();
	}
	public void newDirectory(String path)
	{
        File dir=new File(path);
        if(!dir.exists())
        {
            dir.mkdir();
        }
    }
	public void deleteFile(String path)
	{
        File file=new File(path);
        if(file.exists() && file.isFile())
        {
            file.delete();
        }
    }
    public void deleteDirectory(String path)
    {
        File dir=new File(path);
        if(dir.exists())
        {
        	if(dir.isDirectory())
        	{
	            File[] tmp=dir.listFiles();
	            for(int i=0; i<tmp.length; i++)
	            {
	                if(tmp[i].isDirectory())
	                {
	                    deleteDirectory(path+"/"+tmp[i].getName());
	                }
	                else if(tmp[i].isFile())
	                {
	                    tmp[i].delete();
	                }
	            }
        	}
        	else if(dir.isFile())
        	{
        		dir.delete();
        	}
        }
    }
	public static void main(String[] args)
	{
		Test test_temp = new Test();
		Scanning scanner = new Scanning();
		try
		{
			test_temp.newFile("F:\\summary.txt");
			test_temp.newFile("F:\\detail.txt");
			//扫描线程
			Thread scanning = new Thread(scanner,"scanning");
			scanning.setDaemon(true);
			scanning.start();
			//测试线程
			/*--------------------------------*/
		}
		catch(Exception e)
		{
			System.out.println("Sorry to find abnormality!");;
		}
		
	}

}
