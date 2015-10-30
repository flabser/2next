package task.dao;

import task.model.Task;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;


public class TaskDAO extends DAO <Task, Long> {

	public TaskDAO(_Session session) {
		super(Task.class, session);
	}
}
