package ifttt;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class THEN
{ 
	Summary summaries = new Summary();
	ArrayList<Detail> details = new ArrayList<Detail>();
	public void writeFile(String path,String content) throws IOException
	{
		//true = append file
		FileWriter file_writter = new FileWriter(path,true);
		BufferedWriter buffer_writter = new BufferedWriter(file_writter);
		buffer_writter.write(content);
		buffer_writter.close();
	}
	void recordSummary() throws IOException
	{
		for(int i=0; i<details.size(); i++)
		{
			if(details.get(i).THEN_type.equals("record-summary"))
			{
				String content = "";
				switch(details.get(i).IF_type)
				{
				case "renamed" :
					content = "summary_renamed " + summaries.summary_renamed;
					System.out.println(content);
					writeFile("F:\\summary.txt", content + "\n");
					break;
				case "size-changed" :
					content = "summary_size " + summaries.summary_size;
					System.out.println(content);
					writeFile("F:\\summary.txt", content + "\n");
					break;
				case "path-changed" :
					content = "summary_path " + summaries.summary_path;
					System.out.println(content);
					writeFile("F:\\summary.txt", content + "\n");
					break;
				case "modified" :
					content = "summary_modified " + summaries.summary_modified;
					System.out.println(content);
					writeFile("F:\\summary.txt", content + "\n");
					break;
				}
			}
		}
	}
	void recordDetail() throws IOException
	{
		for(int i=0; i<details.size(); i++)
		{
			if(details.get(i).THEN_type.equals("record-detail"))
			{
				String content_old = "";
				String content_new = "";
				switch(details.get(i).IF_type)
				{
				case "renamed" :
					content_old = details.get(i).old_name;
					content_new = details.get(i).new_name;
					break;
				case "size-changed" :
					content_old = "" + details.get(i).old_size;
					content_new = "" + details.get(i).new_size;
					break;
				case "path-changed" :
					content_old = details.get(i).old_path;
					content_new = details.get(i).new_path;
					break;
				case "modified" :
					content_old = "" + details.get(i).old_time;
					content_new = "" + details.get(i).new_time;
					break;
				}
				String content = details.get(i).IF_type + " from " + content_old + " to " + content_new;
				System.out.println(content);
				writeFile("F:\\detail.txt", content + "\n");
				
			}
		}
	}
	void recoverFile()//complete?
	{
		for(int i=0; i<details.size(); i++)
		{
			if(details.get(i).THEN_type.equals("recover"))
			{
				aFile old_file = new aFile(details.get(i).old_path);
				aFile new_file = new aFile(details.get(i).new_path);
				if(details.get(i).IF_type.equals("renamed") || details.get(i).IF_type.equals("path-changed"))
				{
					new_file.file_real.renameTo(old_file.file_real);
					System.out.println(details.get(i).old_path + " is Recovered!");
				}
			}
		}
	}
	void dealTask() throws IOException
	{
			recordSummary();
			recordDetail();
			recoverFile();
	}
}
