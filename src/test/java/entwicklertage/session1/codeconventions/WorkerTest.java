package entwicklertage.session1.codeconventions;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.junit.Test;

public class WorkerTest {

	@Test
	public void test() throws InterruptedException {
		Dispatcher.LOG.setLevel(Level.SEVERE);
		Dispatcher<Integer> dispatcher = new Dispatcher<>();
		int tasksIndex = 0;
		int numberOfTasks = Dispatcher.initialNumberOfTasks;
		for(; tasksIndex<numberOfTasks;tasksIndex++){
			dispatcher.addTask(tasksIndex);
		}
		dispatcher.createWorker();
		dispatcher.createWorker();
		//dispatcher.createWorker();
		
		numberOfTasks +=10;
		for(; tasksIndex<numberOfTasks;tasksIndex++){
			dispatcher.addTask(tasksIndex);
		}
		numberOfTasks +=10;
		for(; tasksIndex<numberOfTasks;tasksIndex++){
			dispatcher.addTask(tasksIndex);
		}
		dispatcher.stopAllWorkers();
		assertEquals(tasksIndex,dispatcher.getNumberOfTasksDone());
		List<Integer> tasksDone = dispatcher.getTasksDone();
		Set<Integer> noDuplicates = new HashSet<>(tasksDone);
		assertEquals(tasksDone.size(),noDuplicates.size());
	}

}
