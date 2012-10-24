package ui;

import java.util.List;
import shared.Task;

public class UndatedTaskListRenderer {
	
	private List<Task> taskList;
	
	public UndatedTaskListRenderer(List<Task> taskList){
		this.taskList = taskList;
	}
	
	public String render(){
		
		StringBuffer sb = new StringBuffer();
		for(Task task : taskList){
			if (task.getType() == Task.TaskType.FLOATING){
				sb.append(renderTask(task));
			}
		}
		
		return sb.toString();
	}
	
	public String renderTask(Task t){
		StringBuffer sb = new StringBuffer();
		
		sb.append("<table cellpadding=0 cellspacing=0 class=\"taskbox\" width=100%>");
		sb.append("<tr><td rowspan=2><font size=5>"+t.getTaskName()+"</font></td><td width=1 align=center>" + (taskList.indexOf(t)+1) + "</td></tr>");
		sb.append("<tr><td valign=top align=center><input type=checkbox></td></tr>");
		sb.append("</table>");
		
		return sb.toString();
	}

}