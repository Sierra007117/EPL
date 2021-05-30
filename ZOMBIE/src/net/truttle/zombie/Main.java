package net.truttle.zombie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
	public static ArrayList<Entity> entities = new ArrayList<Entity>();
	public static String code = "";
	public static void main(String[] args) throws IOException
	{
		File file = new File(args[0]); 
		  
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		String st; 
		while ((st = br.readLine()) != null) 
		{
			code = code + "\n" + st;
		}
		code = code.replaceAll("\t", "");
		generateEntities();
		runEntities();
	}
	public static void generateEntities()
	{
		int layer = 0;
		boolean nextMustBeSummon = false;
		Entity currentEntity = null;
		for(int i=0; i<code.split("\n").length;i++)
		{
			String a = code.split("\n")[i];
			if(layer == 0)
			{
				if(a.endsWith("is a zombie"))
				{
					currentEntity = new Zombie(a.split(" ")[0]);
					nextMustBeSummon = true;
					continue;
				}
				if(a.endsWith("is a ghost"))
				{
					currentEntity = new Ghost(a.split(" ")[0]);
					nextMustBeSummon = true;
					continue;
				}
				if(a.endsWith("is a vampire"))
				{
					currentEntity = new Vampire(a.split(" ")[0]);
					nextMustBeSummon = true;
					continue;
				}
				if(a.endsWith("is a djinn"))
				{
					currentEntity = new Djinn(a.split(" ")[0]);
					nextMustBeSummon = true;
					continue;
				}
				if(a.endsWith("is a demon"))
				{
					currentEntity = new Demon(a.split(" ")[0], null);
					nextMustBeSummon = true;
					continue;
				}
				if(nextMustBeSummon)
				{
					if(a.equals("summon"))
					{
						layer++;
						nextMustBeSummon = false;
						continue;
					}
					else
					{
						System.out.println("ERROR: An entity has been declared but not summoned");
						System.exit(2);
					}
				}
			}
			if(layer >= 1)
			{
				if(layer == 1)
				{
					if(a.startsWith("remember"))
					{
						currentEntity.rememberedValue = 0;
						currentEntity.rememberedValue = currentEntity.getMoanValue(a,1);
					}
					if(a.equals("bind"))
					{
						if(currentEntity instanceof Zombie || currentEntity instanceof Ghost)
						{
							currentEntity.activeEntity = false;
						}
						else
						{
							currentEntity.activeEntity = true;
						}
						entities.add(currentEntity);
						layer--;
					}
					if(a.equals("animate"))
					{
						if(currentEntity instanceof Zombie)
						{
							currentEntity.activeEntity = true;
							entities.add(currentEntity);
							layer--;
						}
						else
						{
							System.out.println("Only zombies may be animated.");
							System.exit(3);
						}
					}
					if(a.equals("disturb"))
					{
						if(currentEntity instanceof Ghost)
						{
							currentEntity.activeEntity = true;
							entities.add(currentEntity);
							layer--;
						}
						else
						{
							System.out.println("Only ghosts may be disturbed.");
							System.exit(3);
						}
					}
				}
				else
				{
					if(a.equals("bind"))
					{
						layer--;
					}
					if(a.equals("animate"))
					{
						layer--;
					}
				}
				if(a.startsWith("task"))
				{
					layer++;
				}
				currentEntity.entityCode = currentEntity.entityCode + a + "\n";
			}
		}
	}

	public static void runEntities()
	{
		for(int i=0; i<entities.size();i++)
		{
			entities.get(i).start();
		}
	}
}
