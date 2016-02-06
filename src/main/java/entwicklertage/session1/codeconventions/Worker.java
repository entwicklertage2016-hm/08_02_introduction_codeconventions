package entwicklertage.session1.codeconventions;

import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Worker<T> extends Thread{
	
	private Queue<T> tasks;
	
	private List<T> tasksDone;
	
	private boolean running = true;
	
	public Worker(Queue<T> tasks, List<T> tasksDone) {
		this.tasks = tasks;
		this.tasksDone = tasksDone;
	}
	
	@Override
	public void run() {
		while(running){
			try {
				synchronized(this){
					wait();
				}
			} catch (InterruptedException e) {
				Dispatcher.LOG.log(Level.SEVERE, this + " error while trying to wait for new tasks ",e);
			}
			while(!tasks.isEmpty()){
				T task = tasks.poll();
				Dispatcher.LOG.log(Level.INFO, this + " works on task "+task);
				tasksDone.add(task);
			}			
		}
	}

	public void stopWorker(Object initiater){
		this.running = false;
		Dispatcher.LOG.log(Level.INFO, this + " stop triggered by "+initiater);
	}
	
}
