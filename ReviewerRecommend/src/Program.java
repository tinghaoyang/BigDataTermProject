import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import org.apache.hadoop.util.ToolRunner;
import org.unigram.likelike.lsh.*;

public class Program {
    private static String VectorFile = "vectors.txt";
    private static String MinHashFile = "minHash_1000.txt";
	private static int NumofHashFunctions = 1000;
    
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
		MinHash hash = new MinHash(NumofHashFunctions);
		
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
	
	public static void LSH(String[] args)
	{
		try {	
			ToolRunner.run(new LSHRecommendations(), args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void Eval(int exampleID)
	{
		RecommendAuthor authorFromAbstract = new RecommendAuthor();
		ArrayList<String> authorsFromA;
		ArrayList<String> authorsFromC;
		switch (exampleID) {
        case 1: 
        System.out.print("論文編號："+"0301019\r\n");
        authorsFromA = authorFromAbstract.GetAuthorsFromAbs("0301019");
        authorsFromC = authorFromAbstract.GetAuthorsFromCitation("0301019");
             break;
        case 2:  
        System.out.print("論文編號："+"0302077\r\n");
    	authorsFromA = authorFromAbstract.GetAuthorsFromAbs("0302077");
    	authorsFromC = authorFromAbstract.GetAuthorsFromCitation("0302077");
             break;
        case 3:  
        System.out.print("論文編號："+"0303040\r\n");
    	authorsFromA = authorFromAbstract.GetAuthorsFromAbs("0303040");
    	authorsFromC = authorFromAbstract.GetAuthorsFromCitation("0303040");
             break;
        case 4:  
        System.out.print("論文編號："+"9201001\r\n");
    	authorsFromA = authorFromAbstract.GetAuthorsFromAbs("9201001");
    	authorsFromC = authorFromAbstract.GetAuthorsFromCitation("9201001");
             break;
        case 5:  
        System.out.print("論文編號："+"9301001\r\n");
    	authorsFromA = authorFromAbstract.GetAuthorsFromAbs("9301001");
    	authorsFromC = authorFromAbstract.GetAuthorsFromCitation("9301001");
             break;
        case 6:  
        System.out.print("論文編號："+"9401001\r\n");
    	authorsFromA = authorFromAbstract.GetAuthorsFromAbs("9401001");
    	authorsFromC = authorFromAbstract.GetAuthorsFromCitation("9401001");
             break;
        default:
            System.out.print("論文編號："+"0301019\r\n");
        	authorsFromA = authorFromAbstract.GetAuthorsFromAbs("0301019");
        	authorsFromC = authorFromAbstract.GetAuthorsFromCitation("0301019");
            break;
		}
		
		HashSet<String> result = new HashSet<String>();
		if (authorsFromA.size()< 3)
		{
			for (String author : authorsFromA){
				result.add(author);
			}
			
			for (String author : authorsFromC){
				if (result.size()<5)
					result.add(author);
			}
		}
		else if (authorsFromC.size()<2)
		{
			for (String author : authorsFromC){
				result.add(author);
			}
			
			for (String author : authorsFromA){
				if (result.size()<5)
					result.add(author);
			}
		}
		else
		{
			int counter = 3;
			for (String author : authorsFromA){
				result.add(author);
				counter--;
				if (counter == 0)
					break;
			}
			
			counter = 2;
			for (String author : authorsFromC){
				result.add(author);
				counter--;
				if (counter == 0)
					break;
			}
		}
		
		System.out.print("推薦作者："+"\r\n");
		for (String author : result){
			System.out.print(author+"\r\n");
		}
	}
	
	public static void main(String[] args) {
		Boolean encodeCitationToVectors = false;
		Boolean genMinHash = false;
		Boolean runLSH = false;
		
		int exampleID = 1;
		
		for (int i = 0; i < args.length; ++i) 
		{
			if ("-mode".equals(args[i])) {
               String mode = args[++i];
               if (mode.indexOf('v')!=-1)
            	   encodeCitationToVectors = true;
               if (mode.indexOf('m')!=-1)
            	   genMinHash = true;      
            } else if ("-input".equals(args[i])) {
                if (args[++i].indexOf("minHash")!= -1)
                	runLSH = true;
             }else if ("-example".equals(args[i])) {
               exampleID = Integer.parseInt(args[++i]);
            }
		}
		
		if (encodeCitationToVectors){
			ReadCitation();
			GenCitationVectors();
		}
		if (genMinHash){
			GenMinHashTable();
		}
		if (runLSH){
			LSH(args);
		}
		Eval(exampleID);
		
		//System.out.println("End.");
	}

}
