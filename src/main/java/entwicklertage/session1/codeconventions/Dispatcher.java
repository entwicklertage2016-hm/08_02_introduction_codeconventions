package entwicklertage.session1.codeconventions;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Dispatcher<T> {
	
	protected static final Logger LOG = Logger.getLogger( Dispatcher.class.getName() );

	protected static int initialNumberOfTasks = 10; 
	
	private static int requiredNumberOfTasksForNotify = 10;
	
	private List<Worker<T>> workers;
	
	private Queue<T> tasks;
	
	private List<T> tasksDone;
	
	public Dispatcher() {
		workers = new LinkedList<>();
		tasks = new LinkedList<>();
		tasksDone = new LinkedList<>();
	}
	
	public void createWorker(){
		Worker<T> newWorker = new Worker<T>(tasks,tasksDone);
		workers.add(newWorker);
		newWorker.start();
		Dispatcher.LOG.log(Level.INFO, this + " added worker "+newWorker);
	}
	
	public void stopAllWorkers(){
		for(Worker<T> w: workers){
			synchronized(w){
				w.stopWorker(this);
				w.notify();
			}			
		}
		Dispatcher.LOG.log(Level.INFO, this + " stopped all workers");
	}
	
	public void addTask(T task){
		Dispatcher.LOG.log(Level.INFO, this + " task added: "+task);
		tasks.add(task);
		probablyNotifyWorkers();
	}
	
	public int getNumberOfTasksDone(){
		return tasksDone.size();
	}
	
	public List<T> getTasksDone(){
		return new LinkedList<>(tasksDone);
	}
	
	private void probablyNotifyWorkers(){
		if(tasks.size()>requiredNumberOfTasksForNotify){
			for(Worker<T> w: workers){
				synchronized(w){
					w.notify();
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return Thread.currentThread().toString();
	}
	
	public static void main(String[] args) throws InterruptedException {
		Dispatcher<Integer> dispatcher = new Dispatcher<>();
		int tasksIndex = 0;
		int numberOfTasks = Dispatcher.initialNumberOfTasks;
		for(; tasksIndex<numberOfTasks;tasksIndex++){
			dispatcher.addTask(tasksIndex);
		}
		dispatcher.createWorker();
		dispatcher.createWorker();
		dispatcher.createWorker();
		Thread.sleep(4000);
		numberOfTasks +=6;
		for(; tasksIndex<numberOfTasks;tasksIndex++){
			dispatcher.addTask(tasksIndex);
		}
		Thread.sleep(4000);
		numberOfTasks +=6;
		for(; tasksIndex<numberOfTasks;tasksIndex++){
			dispatcher.addTask(tasksIndex);
		}
		dispatcher.stopAllWorkers();
	}
	
}
