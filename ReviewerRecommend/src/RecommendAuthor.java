import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

public class RecommendAuthor {
	HashMap<String,ArrayList<String>> dataFromAbstract;
	HashMap<String,ArrayList<String>> dataFromCitation;
	public RecommendAuthor()
	{
		dataFromAbstract = new HashMap<String,ArrayList<String>>();
		dataFromCitation  = new HashMap<String,ArrayList<String>>();
		try{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream("authorFromAbstract.txt");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String strLine;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				dataFromAbstract.put(strLine, new ArrayList<String>());
				String authors;
				while ((authors = br.readLine()) != null)
				{
					if (authors.equals(""))
						break;
					dataFromAbstract.get(strLine).add(authors);
				}
			}
			//Close the input stream
			in.close();
			}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
			}
		
		try{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream("authorFromCitation.txt");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String strLine;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				dataFromCitation.put(strLine, new ArrayList<String>());
				String authors;
				while ((authors = br.readLine()) != null)
				{
					if (authors.equals(""))
						break;
					dataFromCitation.get(strLine).add(authors);
				}
			}
			//Close the input stream
			in.close();
			}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
			}
	}
	
	public ArrayList<String> GetAuthorsFromAbs(String paperID)
	{
		if (dataFromAbstract.containsKey(paperID))
			return dataFromAbstract.get(paperID);
		return new ArrayList<String>();
	}
	
	public ArrayList<String> GetAuthorsFromCitation(String paperID)
	{
		if (dataFromCitation.containsKey(paperID))
			return dataFromCitation.get(paperID);
		return new ArrayList<String>();
	}
}
