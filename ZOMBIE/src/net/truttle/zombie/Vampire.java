package net.truttle.zombie;
import java.util.ArrayList;
import java.util.Collections;

public class Vampire extends Entity{

	public Vampire(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void performTasks() {
		ArrayList<Task> tempList = new ArrayList<Task>();
		for(int i=0; i<this.numOfTasks;i++)
		{
			tempList.add(tasks[i]);
		}
		Collections.shuffle(tempList);
		for(int i=0; i<this.numOfTasks;i++)
		{
			tasks[i] = tempList.get(i);
		}
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
