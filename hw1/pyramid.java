public class pyramid{

	public static void drawTriangle (int N){
		int count = 1;
		String stars = "*";
		while (count <= N){
			for (int i = 0; i < count; i++){
				System.out.print(stars);
			}
			System.out.println();
			count += 1;
		}
	}
	public static void main (String[] args){
		drawTriangle(10);
	}
}
