import java.util.Random;
import java.math.*;

public class HashFunction {
	int Seed;
	int xValue;
	int addValue;
	int modValue;
	
	public HashFunction(int seed)
	{
		Seed = seed;
		Random r = new Random(Seed);
		xValue = r.nextInt(100);
		modValue = Math.abs(r.nextInt());
		addValue = r.nextInt(modValue);
	}
	
	public int HashValue(String x)
	{
		int value = Integer.parseInt(x);
		
		return HashValue(value);
	}
	
//Test version
//	public int HashValue(int value)
//	{
//		if (Seed == 1)
//			return ((2 * value)+5) % 7;  //2x+5 mod 7
//		if (Seed == 2)
//			return ((3 * value)+4) % 11;  //3x+4 mod 11
//		if (Seed == 3)
//			return ((5 * value)+3) % 23;  //5x+3 mod 23
//		
//		return 0;
//	}
	
	public int HashValue(int value)
	{	
		return ((xValue * value)+addValue) % modValue;
	}
}
