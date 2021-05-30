package net.truttle.zombie;

public class Zombie extends Entity{

	public Zombie(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void performTasks() {
		if(this.activeEntity)
		{
			if(this.numOfTasks >= 1)
			{
				for(int i=0; i<this.numOfTasks;i++)
				{
					if(tasks[i].active)
					{
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
