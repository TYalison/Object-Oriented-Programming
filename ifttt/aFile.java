package ifttt;

import java.io.File;


public class aFile {
	volatile String IF_type = "";
	volatile String THEN_type = "";
	volatile String name = "";
	volatile String path = "";
	String parent = "";
	volatile long size = 0;
	volatile long time = 0;
	volatile File file_real = new File("");
	boolean is_file = false;
	boolean is_directory = false;
	aFile(String file_path)
	{
		file_real = new File(file_path);
		name = file_real.getName();
		path = file_real.getAbsolutePath();
		time  = file_real.lastModified();
		parent = file_real.getParent();
		size = setSize();
		is_file = file_real.isFile();
		is_directory = file_real.isDirectory();
	}
	void update(String file_path)
	{
		file_real = new File(file_path);
		name = file_real.getName();
		path = file_real.getAbsolutePath();
		time  = file_real.lastModified();
		parent = file_real.getParent();
		size = setSize();
	}
	private long setSize()
	{
		long size_temp = 0;
		if(file_real.isFile())
		{
			size_temp = file_real.length();
		}
		else if(file_real.isDirectory())
		{
			File[] files_lower = file_real.listFiles();
			for(int i=0; i<files_lower.length; i++)
			{
				if(files_lower[i].isFile())
				{
					size_temp += files_lower[i].length();
				}
			}
		}
		return size_temp;
	}
}
