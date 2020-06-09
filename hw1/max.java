public class max{
	public static int max(int[] m){
		int maxNumber = 0;
		for (int i = 0; i < m.length; i++){
			if (m[i] > maxNumber)
				maxNumber = m[i];
		}
		return maxNumber;
	}
	public static void main (String[] args){
		int[] m = new int[]{9, 10, 4, 15, 20, 0};
		System.out.println(max(m));
	}
}
