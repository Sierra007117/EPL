package net.truttle.zombie;

import java.util.ArrayList;
import java.util.Collections;

public class Demon extends Entity{

	public Demon parentDemon = null;
	public Demon(String s, Demon parent) {
		super(s);
		this.parentDemon = parent;
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
						for(int j=0;j<(int)(Math.random()*5);j++)
						{
							if(Math.random()<0.5 && parentDemon == null)
							{
								Demon d = new Demon(this.name,this);
								d.entityCode = this.entityCode;
								d.activeEntity = true;
								d.seperateTasks();
								d.start();
							}
							runTask(i);
						}
						if(parentDemon != null)
						{
							parentDemon.rememberedValue += this.rememberedValue;
						}
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
