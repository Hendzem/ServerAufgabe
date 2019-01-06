import java.io.*;
import java.util.*;
public class URL
{
        private String[] adress;
        private int position;

	public URL(String request)
	{	//Positionsmerker für "adress" Array wird auf 0 gesetzt
		position = 0;
		//Ist die URL / wird die index.html bereitgestellt
		if(request.equals("/"))
		{
			adress = new String[1];
			adress[0] = "index.html";
		}
		else
		{
			char[] toCount = request.toCharArray();
			char vgl = '/';
			int counter = 0;
			//Zähle wie viele Wörter in der Adresse vorkommen
			for(int i = 0; i < toCount.length; i++)
			{
				if(toCount[i] == vgl)
				{
					counter++;
				}
			}
			
			adress = new String[counter];
			StringTokenizer slash = new StringTokenizer(request,"/");
			counter = 0;
			//Befülle das Array mit den Wörtern ohne "/"
			while(slash.hasMoreTokens())
			{
				adress[counter] = slash.nextToken();
				counter++;
			}
		}
	}

	public String getWord(int i)
	{
		return adress[i];
	}

	public void setPosition(int i)
	{
		position = i;		
	}

	public int getPosition()
	{
		return position;
	}

	public boolean hasNext(int i)
	{
		if(i < adress.length)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}