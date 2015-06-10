import gnu.trove.list.array.TIntArrayList;
import gnu.trove.set.hash.TIntHashSet;

public class MinHash {

	HashFunction[] myHashes;
	
	public MinHash(int hashFuncCount)
	{
		myHashes = new HashFunction[hashFuncCount];
		for(int i=0;i<hashFuncCount;i++)
			myHashes[i] = new HashFunction(i);
	}
	
	public int[] FindMinHash(String[] vector)
	{
		int[] signatureMatrix = new int[myHashes.length];
		for(int index =0;index<signatureMatrix.length;index++)
			signatureMatrix[index] = Integer.MAX_VALUE;
		
		for(int i=0;i<vector.length;i++)
		{
			int value = Integer.parseInt(vector[i]);
			if (value !=0)
			{
				for(int hashFunctionID =0;hashFunctionID<myHashes.length;hashFunctionID++)
				{
					int hashValue = myHashes[hashFunctionID].HashValue(i);
					if (signatureMatrix[hashFunctionID] > hashValue)
						signatureMatrix[hashFunctionID] = hashValue;
				}
			}
		}
		
		return signatureMatrix;
	}
	
	public static int[] union(int[] a, int[] b) {
		TIntArrayList set = new TIntArrayList();
	    set.addAll(a);
	    set.addAll(b);
	    return set.toArray();
	  }
	
	public static int[] intersectionUnsorted(int[] arr, int[] arr2) {
	    TIntHashSet set = new TIntHashSet();
	    TIntHashSet toReturn = new TIntHashSet();
	    for (int a : arr) {
	      set.add(a);
	    }

	    for (int a : arr2) {
	      if (set.contains(a)) {
	        toReturn.add(a);
	      }
	    }

	    return toReturn.toArray();
	  }
	
	public double measureJaccardSimilarity(int[] left, int[] right) {
	    if (left.length != right.length)
	    	 return 0d;

	    if (left.length + right.length == 0)
	      return 0d;

	    int[] union = union(left, right);
	    int[] intersection = intersectionUnsorted(left, right);

	    return intersection.length / (double) union.length;
	  }
}
