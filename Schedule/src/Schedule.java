import java.util.ArrayList;
/**
 * Job Schedule
 * 
 * @author Harsh
 *
 */
public class Schedule {

	private ArrayList<Job> jobs = new ArrayList<>();
	private int minCompletionTime = 0;
	private boolean edge = false;

	/**
	 * Adds a new job to the schedule, where the time needed to complete the job is
	 * time.
	 * 
	 * @param time
	 * @return the job inserted
	 */
	public Job insert(int time) {
		Job job = new Job(time);
		jobs.add(job);

		if (minCompletionTime < job.time) {
			minCompletionTime = job.time;

		}
		return job;

	}

	/**
	 * returns the job by its number
	 * 
	 * @param index
	 * @return the job at index implicitly
	 */
	public Job get(int index) {
		return jobs.get(index);
	}

	/**
	 * Return the earliest possible completion time for the entire Schedule
	 * 
	 * @return minCompletionTime
	 */
	public int finish() {

		if (!edge)
			return minCompletionTime;
		edge = false;

		ArrayList<Job> temp = new ArrayList<>();

		for (Job j : jobs) {
			j.tempcounter = j.counter;
			j.loopFlag = true;
			if (j.tempcounter <= 0) {
				temp.add(j);
				j.loopFlag = false;
			}
		}

		int count = 0;
		while (!temp.isEmpty()) {
			Job current = temp.remove(0);
			count++;
			for (Job j : current.children) {
				int tempTime = current.startTime + current.time;
				if (j.startTime < tempTime)
					j.startTime = tempTime;
				tempTime = j.startTime + j.time;
				if (minCompletionTime < tempTime)
					minCompletionTime = tempTime;
				j.tempcounter--;
				if (j.tempcounter == 0) {
					temp.add(j);
					j.loopFlag = false;
				}
			}
		}

		if (jobs.size() != count) {
			minCompletionTime = -1;
		}

		return minCompletionTime;
	}

	/**
	 * Inner Class Job
	 * 
	 * @author Harsh
	 *
	 */
	public class Job {
		String name;
		int time;
		int startTime = 0;
		int counter = 0;
		boolean loopFlag = false;
		ArrayList<Job> prereqs = new ArrayList<>();

		ArrayList<Job> children = new ArrayList<>();
		private int tempcounter = 0;

		/**
		 * Private constructor
		 * 
		 * @param time
		 */
		private Job(int time) {
			this.time = time;
		}

		/**
		 * sets up the requirement that this job requires job j to be completed before
		 * it begins
		 * 
		 * @param j
		 */
		public void requires(Job j) {
			this.prereqs.add(j);
			j.children.add(this);
			counter++;
			edge = true;
		}

		/**
		 * return the earliest possible start time for the job.
		 * 
		 * @return startTime earliest possible time
		 */
		public int start() {
			if (edge)
				finish();
			if (loopFlag)
				return -1;
			return startTime;
		}

	}
}
