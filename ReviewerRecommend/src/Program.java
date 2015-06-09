import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import org.apache.hadoop.util.ToolRunner;
import org.unigram.likelike.lsh.*;

public class Program {
    private static String VectorFile = "vectors.txt";
    private static String MinHashFile = "minHash.txt";
	
	static HashMap<String,HashSet<String>> citationRelatoin;
	static TreeSet<String> docIDS;
	static void ReadCitation()
	{
		citationRelatoin = new HashMap<String,HashSet<String>>();
		docIDS = new TreeSet<String>();
		try{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream("Cit-HepTh.txt");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			//skip comment
			br.readLine();
			br.readLine();
			br.readLine();
			br.readLine();
			
			String strLine;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				String[] idAndCitation = strLine.split("\t");
				docIDS.add(idAndCitation[0]);
				docIDS.add(idAndCitation[1]);
				
				if (!citationRelatoin.containsKey(idAndCitation[0]))
					citationRelatoin.put(idAndCitation[0], new HashSet<String>());
				citationRelatoin.get(idAndCitation[0]).add(idAndCitation[1]);
			}
			//Close the input stream
			in.close();
			}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
			}
	}
	
	static void GenCitationVectors()
	{
		try {
			FileOutputStream out = new FileOutputStream(VectorFile);
			DataOutputStream on = new DataOutputStream(out);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(on));
			
			for (String id : citationRelatoin.keySet()){
				HashSet<String> citedNode = citationRelatoin.get(id);
				
				bw.write(id);
				for(String citationID : docIDS)
				{
					bw.write("\t");
					if (citedNode.contains(citationID))
						bw.write("1");
					else
						bw.write("0");
				}
				bw.write("\r\n");		
			}
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void GenMinHashTable()
	{
		MinHash hash = new MinHash();
		
		try {
			FileOutputStream out = new FileOutputStream(MinHashFile);
			DataOutputStream on = new DataOutputStream(out);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(on));
			
			FileInputStream fstream = new FileInputStream(VectorFile);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String strLine;
			while ((strLine = br.readLine()) != null) 
		    {
				String[] data = strLine.split("\t");
				String id = data[0];
				String[] vector = new String[data.length -1];
				for(int i=0;i<vector.length;i++)
				{
					vector[i] = data[i+1];
				}
				StringBuilder sb = new StringBuilder(id);
				sb.append("\t");
				
				int[] minhashTable = hash.FindMinHash(vector);
				for(int minhash : minhashTable)
				{
					sb.append(minhash+" ");
				}
				bw.write(sb.substring(0,sb.length()-1)+"\r\n");
		    }
			br.close();
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//ReadCitation();
		//GenCitationVectors();
		GenMinHashTable();
		try {	
			ToolRunner.run(new LSHRecommendations(), args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
