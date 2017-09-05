package ifttt;

import java.io.IOException;

public interface file_operator
{
	public boolean newFile(String path) throws IOException;
	public boolean renameFile(String old_path,String new_path);
	public boolean moveFile(String oldpath, String newpath);
	public void writeFile(String path,String content) throws IOException;
	public void newDirectory(String path);
	public void deleteFile(String path);
	public void deleteDirectory(String path);


}
