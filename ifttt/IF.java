package ifttt;

import java.util.ArrayList;

public class IF
{
	void judgeIF(ArrayList<aFile> Files_former,ArrayList<aFile> Files_latter,THEN then_task)
	{
		for(int i=0; i<Files_former.size() && (!Files_latter.isEmpty()); i++)
		{
			for(aFile file_updated : Files_latter)
			{
				switch(Files_former.get(i).IF_type)
				{
				case "size-changed" :
					if(Files_former.get(i).path.equals(file_updated.path) && Files_former.get(i).size != file_updated.size)
					{
						if(Files_former.get(i).file_real.isFile())
						{
							then_task.summaries.summary_size++;
						}
						Detail detail = new Detail();
						detail.IF_type = Files_former.get(i).IF_type;
						detail.THEN_type = Files_former.get(i).THEN_type;
						detail.old_size = Files_former.get(i).size;
						detail.new_size = file_updated.size;
						then_task.details.add(detail);
						if(!Files_former.get(i).THEN_type.equals("recover"))
						{
							Files_former.get(i).update(file_updated.path);
						}
					}
					break;
				case "modified" :
					if(Files_former.get(i).path.equals(file_updated.path) &&
							Files_former.get(i).time != file_updated.time)
					{
						if(Files_former.get(i).is_file)
						{
							then_task.summaries.summary_modified++;
						}
						Detail detail = new Detail();
						detail.IF_type = Files_former.get(i).IF_type;
						detail.THEN_type = Files_former.get(i).THEN_type;
						detail.old_time = Files_former.get(i).time;
						detail.new_time = file_updated.time;
						then_task.details.add(detail);
						//updateTarget
						if(!Files_former.get(i).THEN_type.equals("recover"))
						{
							Files_former.get(i).update(file_updated.path);
//							Files_former.get(i).IF_type = detail.IF_type;
//							Files_former.get(i).THEN_type = detail.THEN_type;
						}
					}
					break;
				case "path-changed" :
					if(Files_former.get(i).is_file &&
					   file_updated.is_file&&
					   Files_former.get(i).name.equals(file_updated.name) &&
					  (!Files_former.get(i).file_real.exists()) &&
			    	  (!Files_former.get(i).path.equals(file_updated.path)) &&
					   Files_former.get(i).size == file_updated.size)
					{
						then_task.summaries.summary_path++;
						Detail detail = new Detail();
						detail.IF_type = Files_former.get(i).IF_type;
						detail.THEN_type = Files_former.get(i).THEN_type;
						detail.old_path = Files_former.get(i).path;
						detail.new_path = file_updated.path;
						then_task.details.add(detail);
					//updateTarget
						if(!Files_former.get(i).THEN_type.equals("recover"))
						{
							Files_former.get(i).update(file_updated.path);
//									Files_former.get(i).IF_type = detail.IF_type;
//									Files_former.get(i).THEN_type = detail.THEN_type;
						}
					}
					break;
				case "renamed" :
					if(Files_former.get(i).is_file &&
							   file_updated.is_file &&
							   Files_former.get(i).parent.equals(file_updated.parent) &&
							   Files_former.get(i).size == file_updated.size &&
							   (!Files_former.get(i).name.equals(file_updated.name)))
					{
						then_task.summaries.summary_renamed++;
						Detail detail = new Detail();
						detail.IF_type = Files_former.get(i).IF_type;
						detail.THEN_type = Files_former.get(i).THEN_type;
						detail.old_name = Files_former.get(i).name;
						detail.new_name = file_updated.name;
						then_task.details.add(detail);
						//updateTarget
						if(!Files_former.get(i).THEN_type.equals("recover"))
						{
							Files_former.get(i).update(file_updated.path);
							Files_former.get(i).IF_type = detail.IF_type;
							Files_former.get(i).THEN_type = detail.THEN_type;
						}
					}
					break;
				}
			}
		}
	}
}
