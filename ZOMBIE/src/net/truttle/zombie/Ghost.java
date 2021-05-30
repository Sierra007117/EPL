package net.truttle.zombie;

public class Ghost extends Entity{

	public Ghost(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("static-access")
	@Override
	public void performTasks() {
		Thread t;
		t = Thread.currentThread();
		if(this.activeEntity)
		{
			if(this.numOfTasks >= 1)
			{
				for(int i=0; i<this.numOfTasks;i++)
				{
					if(tasks[i].active)
					{
						long sleepTime = (long)(Math.random()*30000);
						try {
							t.sleep(sleepTime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						runTask(i);
					}
				}
			}
			else
			{
				System.out.println("ERROR: No task given to entity " + name);
				System.exit(5);
			}
		}
	}

}
