package net.truttle.zombie;

import java.util.Stack;

public abstract class Entity extends Thread{
	public int rememberedValue;
	public String name = "";
	public String entityCode;
	/*
	public String[] tasks = new String[1000];
	public boolean[] activeTask = new boolean[1000];*/
	public Task[] tasks = new Task[1000];
	public boolean activeEntity;
	public abstract void performTasks();
	public int numOfTasks = 0;
	private int layer = 0;
	String regex = "[0-9]";
	public Stack<Integer> tempStack = new Stack<Integer>();
	boolean done;
	public Entity(String s)
	{
		for(int i=0; i<1000; i++)
		{
			tasks[i] = new Task();
		}
		this.name = s;
	}
	@SuppressWarnings("deprecation")
	public void runTask(int task)
	{
		String a[] = tasks[task].code.split("\n");
		for(int i=0; i<a.length;i++)
		{
			//System.out.println(i + "::" + name + "::" + rememberedValue);
			if(a[i].startsWith("null"))
			{
				a[i] = a[i].replace("null", "");
			}
			if(a[i].startsWith("around"))
			{
				int loopStartLayer = this.layer;
				while(true)
				{
					
					i--;
					this.calculateLayersLoop(a[i]);
					if(loopStartLayer-1 == this.layer && a[i].startsWith("shamble"))
					{
						break;
					}
				}
				continue;
			}
			if(a[i].startsWith("until remembering") && this.isNumber(a[i].split(" ")[2]))
			{
				int loopStartLayer = this.layer;
				while(this.rememberedValue != this.getMoanValue(a[i], 2))
				{
					
					i--;
					this.calculateLayersLoop(a[i]);
					if(loopStartLayer-1 == this.layer && a[i].startsWith("shamble"))
					{
						break;
					}
				}
				if(this.rememberedValue == this.getMoanValue(a[i], 2))
				{
					i += 1;
				}
				continue;
			}
			else if(a[i].startsWith("until remembering"))
			{
				String b[] = a[i].split(" ");
				Entity e = this.getEntityWithName(b[2]);
				int loopStartLayer = this.layer;
				while(e.rememberedValue != this.getMoanValue(a[i], 3))
				{
					
					i--;
					this.calculateLayersLoop(a[i]);
					if(loopStartLayer-1 == this.layer && a[i].startsWith("shamble"))
					{
						break;
					}
				}
				if(e.rememberedValue == this.getMoanValue(a[i], 3))
				{
					i += 1;
				}
				continue;
			}
			else if(a[i].startsWith("taste"))
			{
				if(!a[i+1].equals("good"))
				{
					System.out.println("ERROR: taste must be followed with good");
					System.exit(9);
				}
				layer++;
				String b[] = a[i].split(" ");
				if(b[1].equals("remembering"))
				{

					if(this.rememberedValue != this.getMoanValue(a[i], 3))
					{
						i = jumpToBad(a, i);
					}
				}
				else if(b[2].equals("remembering"))
				{

					Entity e = this.getEntityWithName(b[1]);
					if(e.rememberedValue != this.getMoanValue(a[i], 3))
					{
						i = jumpToBad(a, i);
					}
				}
				else
				{
					System.out.println("ERROR: What is being tasted exactly?");
					System.exit(8);
				}
				continue;
			}
			if(a[i].startsWith("bad"))
			{
				i = jumpToSpit(a,i);
			}
			if(a[i].startsWith("shamble"))
			{
				layer++;
			}
			if(a[i].startsWith("remember"))
			{
				if(a[i].split(" ")[1].equals("moan"))
				{
					this.rememberedValue = this.getMoanValue(a[i], 1);
					continue;
				}
				else if(isNumber(a[i].split(" ")[1]))
				{
					this.rememberedValue = this.getMoanValue(a[i], 1);
					continue;
				}
				else
				{
					Entity e = getEntityWithName(a[i].split(" ")[1]);
					e.rememberedValue = this.getMoanValue(a[i], 1);
					continue;
				}
				
			}
			if(a[i].startsWith("forget"))
			{
				String b[] = a[i].split(" ");
				try
				{
					Entity e = this.getEntityWithName(b[1]);
					e.rememberedValue = 0;
				}
				catch(Exception e)
				{
					this.rememberedValue = 0;
				}
			}
			if(a[i].startsWith("invoke"))
			{
				String b[] = a[i].split(" ");
				try
				{
					Entity e = this.getEntityWithName(b[1]);
					e.activeEntity = true;
				}
				catch(Exception e)
				{
					this.rememberedValue = 0;
					this.activeEntity = true;
				}
			}
			if(a[i].startsWith("banish"))
			{
				try
				{
					String entityToBanish = a[i].split(" ")[1];
					for(int j=0; j<Main.entities.size(); j++)
					{
						Entity temp = Main.entities.get(j);
						if(temp.name.equals(entityToBanish))
						{
							temp.activeEntity = false;
						}
						temp.done = true;
						this.checkIfAllDone();
						temp.stop();
					}
				}
				catch(Exception e)
				{
					this.done = true;
					this.checkIfAllDone();
					this.stop();
				}
			}
			if(a[i].startsWith("say"))
			{
				if(a[i].contains("\""))
				{
					int quote0 = 0;
					int quote1 = 0;
					for(int j=0; j<a[i].length();j++)
					{
						if(a[i].charAt(j) == '"')
						{
							quote0 = j+1;
							break;
						}
					}for(int j=quote0+1; j<a[i].length();j++)
					{
						if(a[i].charAt(j) == '"')
						{
							quote1 = j;
							break;
						}
					}
					System.out.print(a[i].substring(quote0,quote1).replace("\\n", "\n"));
				}
				else
				{

					if(a[i].contains("moan"))
					{
						System.out.print(this.getMoanValue(a[i], 1));
					}
				}
			}
		}

		done = true;
		this.activeEntity = false;
	}

	public void seperateTasks()
	{
		boolean insideTask = false;
		int currentTask = 0;
		String a[] = entityCode.split("\n");
		for(int i=0; i<a.length;i++)
		{
			if(a[i].startsWith("null"))
			{
				a[i] = a[i].replace("null", "");
			}
			if(a[i].startsWith("task") && !insideTask)
			{
				insideTask = true;
				currentTask = numOfTasks;
				numOfTasks++;
				continue;
			}
			if(insideTask)
			{
				if(a[i].startsWith("bind"))
				{
					insideTask = false;
					tasks[currentTask].active = false;
				}
				else if(a[i].startsWith("animate"))
				{
					insideTask = false;
					tasks[currentTask].active = true;
				}
				else if(a[i].startsWith("task"))
				{
					System.out.println("ERROR: Cannot put tasks inside of tasks!");
					System.exit(1);
				}
				else
				{
					tasks[currentTask].code = tasks[currentTask].code + a[i] + "\n";
				}
			}
		}
	}

	@Override
	public void run() {
		seperateTasks();
		performTasks();
		while(true)
		{
			if(done && activeEntity)
			{
				done = false;
				seperateTasks();
				performTasks();
			}
			checkIfAllDone();
		}
	}
	public void checkIfAllDone()
	{
		boolean allDone = true;
		for(int i=0; i<Main.entities.size(); i++)
		{
			if(Main.entities.get(i) != null && !Main.entities.get(i).done)
			{
				allDone = false;
				break;
			}
		}
		if(allDone)
		{
			System.exit(666);
		}
	}
	public Entity getEntityWithName(String name)
	{
		for(int i=0; i<Main.entities.size();i++)
		{
			if(Main.entities.get(i).name.equals(name))
			{
				return Main.entities.get(i);
			}
		}
		System.out.println("ERROR! Entity of name " + name + " does not exist!");
		System.exit(4);
		return null;
	}
	
	public int getMoanValue(String line, int start)
	{
		int val = 0;
		boolean done = false;
		String[] a = line.split(" ");
		int lookingAt = a.length-1;
		int whenToDivide = -99;
		this.tempStack.clear();
		while(!done)
		{

			//System.out.println(tempStack.size() + " , " + lookingAt);
			try
			{
				boolean finished = false;
				if(a[lookingAt].contains("moan"))
				{
					String name = null;
					try
					{
						name = a[lookingAt+1];
					}
					catch(Exception e)
					{
						//e.printStackTrace();
						tempStack.add(rememberedValue);
						lookingAt -= 1;
						continue;
					}
					for(int i=0; i<Main.entities.size(); i++)
					{
						if(Main.entities.get(i).name.equals(name))
						{
							tempStack.add(Main.entities.get(i).rememberedValue);
							lookingAt -= 1;
							finished = true;
						}
					}
					if(!finished)
					{
						tempStack.add(rememberedValue);
					}
					continue;
					
				}
				else if(isNumber(a[lookingAt]))
				{
					tempStack.add(Integer.parseInt(a[lookingAt]));
					whenToDivide -= 1;
				}
				else if(a[lookingAt].contains("turn"))
				{
					tempStack.add(-tempStack.pop());
				}
				else if(a[lookingAt].contains("rend"))
				{
					int val1 = tempStack.pop();
					int val2 = tempStack.pop();
					tempStack.add(val2/val1);
					whenToDivide = lookingAt+4;
				}
				if(lookingAt == whenToDivide)
				{

					//int val1 = tempStack.pop();
					//int val2 = tempStack.pop();
					//tempStack.add(val1/val2);
				}
				lookingAt -= 1;
				
				if(lookingAt<start-1)
				{
					done = true;
					continue;
				}
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				e.printStackTrace();
				done = true;
			}
		}
		
		while(tempStack.size() > 0)
		{
			val += tempStack.pop();
		}
		return val;
	}
	
	private boolean isNumber(String s)
	{
		try
		{
			@SuppressWarnings("unused")
			int d = Integer.parseInt(s);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	@SuppressWarnings("unused")
	private void printTasks()
	{
		for(int i = 0; i<this.numOfTasks; i++)
		{
			System.out.println(this.tasks[i]);
		}
	}
	private void calculateLayersLoop(String line)
	{
		if(line.startsWith("around"))
		{
			this.layer++;
		}
		if(line.startsWith("until"))
		{
			this.layer++;
		}
		if(line.startsWith("shamble"))
		{
			this.layer--;
		}
	}
	private int jumpToBad(String[] a, int i)
	{
		int newI = i;
		int lay = 0;
		while(i<a.length)
		{
			if(a[i].startsWith("good"))
			{
				lay++;
			}
			if(a[i].startsWith("bad"))
			{
				lay--;
				if(lay == 0)
				{
					newI = i;
					break;
				}
			}
			i++;
		}
		return newI;
	}
	private int jumpToSpit(String[] a, int i)
	{
		int newI = i;
		int lay = 0;
		while(i<a.length)
		{
			if(a[i].startsWith("bad"))
			{
				lay++;
			}
			if(a[i].startsWith("spit"))
			{
				lay--;
				if(lay == 0)
				{
					newI = i;
					break;
				}
			}
			i++;
		}
		return newI;
	}
}
