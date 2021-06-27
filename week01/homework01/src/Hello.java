public class Hello{

	public static void main(String[] args){
		byte a = 69;
		short b = 1013;
		int c = 100_000;
		long d = 985_211_996_000L;
		float e = 999.99F;
		double f = 3.14159268210382D;
		boolean g = false;
		char h = 0x99;
		int add = 99+88;
		int subtract = 101-100;
		int multiply = 10*2;
		int divide = 79/7;

		if(99>98){
			int[] arr = {add,subtract,multiply,divide};
			for (int i = 0; i < arr.length; i++) {
				arr[i] = arr[i]<<1;
			}
		}

		System.out.println("Hello bytecode");
	}

}
