import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Array;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DES {
	
	static final int[][] s_1 = { 
			{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
			{0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
			{4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
			{15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
	};
	static final int[][] s_2 = {
			{15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10}, 
			{3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
			{0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
		    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9},
	};
	static final int[][] s_3 = {
			{10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
			{13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
			{13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
			{1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}			
	};
	static final int[][] s_4 = {
			{7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
			{13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
			{10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
			{3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}			
	};
	static final int[][] s_5 = {
			{2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
			{14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
			{4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
			{11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}			
	};
	static final int[][] s_6 = {
			{12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
			{10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
			{9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
			{4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}			
	};
	static final int[][] s_7 = {
			{4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
			{13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
			{1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
			{6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}			
	};
	static final int[][] s_8 = {
			{13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
			{1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
			{7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
			{2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}			
	};
	
	public static void main(String args[]) {
		//System.out.println("Welcome to what is probably the slowest and most poorly written implementation of DES");
		String key 		= "3b3898371520f75e";//"133457799BBCDFF1";
		String plain 	= "0123456789ABCDEF";
		String ciph		= "";
		int blocksize = 64;
		boolean[] KEY = hexStringToBinary(key);
		boolean[] PLAIN = hexStringToBinary(plain);
		boolean[] CIPH = doDES(PLAIN, KEY, false);
		printBlockAsLine(PLAIN, 0);
		printBlockAsLine(CIPH, 0);
		CIPH = doDES(CIPH, KEY, true);
		printBlockAsLine(CIPH, 0);

		
		
		//printBlock(hexStringToBinary("FFFFFFFFFFFFFFFF"), 3);
		boolean[] res = {false, false, true, false, true, true};
//		System.out.println(s_8[1][5]);
//		String s = "";
//		boolean[] derp = sSelection(res, s_8);
//		for (boolean b : derp)
//			s+= (b?1:0) +"";
//		System.out.println(s);
//		
//		
//		System.out.println(binaryToInt("0101"));
		
//		tests();
//		boolean[] keyArr = hexStringToBinary(key);
//		boolean[] plainArr = hexStringToBinary(key);
//		boolean[] ciphArr = doDES(plainArr, keyArr);
		
	}
	
	//helpers
	public static boolean[] hexStringToBinary(String s) {
		boolean[] res = new boolean[s.length() * 4];
		int counter = 0;
		String K = "";
		for (int i = 0; i < s.length(); i++) {
			int t = Integer.parseInt(s.charAt(i)+"", 16);
			String k = Integer.toBinaryString(Integer.parseInt(s.charAt(i)+"", 16));
			for (int j = 0; j < 4 - k.length(); j++) {
				K += "0";
			}
			K += k;
		}
		for (int j = 0; j < K.length(); j++) {
			res[j] = (K.charAt(j)=='1'?true:false);
		}
		
		return res;
	}
	
	public static boolean[] doDES(boolean[] data, boolean[] key, boolean decrypt) {
		int r = 16; //number of rounds to perform
		data = initialPermutation(data);
		boolean[] Ln = new boolean[32];
		boolean[] Rn = new boolean[32];
		for (int i = 0; i < data.length/2; i++) {
			Ln[i] = data[i];
			Rn[i] = data[32+i];
		}
		boolean[][] keyPerms = generateKeyPerms(key);
//		if (decrypt) { //reverse keyorder
//			boolean[][] temp = keyPerms;
//			for (int i = 0; i < 16; i++) {
//				temp[i] = keyPerms[15-i];
//			}
//			for (int i = 0; i <16; i++) {
//				if (!temp[i].equals(keyPerms[15-i]))
//					System.out.println("KEY BLOCK ERROR ON KEY#"+i);
//			}
//		keyPerms = temp;
//		}
		boolean[] prevL = Ln;
		boolean[] prevR = Rn;
		boolean[][][] what = new boolean[17][2][32];
		what[0][1] = Ln;
		what[0][0] = Rn;
		//ERROR IS BELOW
		for (int i = 1; i < r+1; i++) {
//			System.out.println("ROUND#"+i+":");
			//Code below does not save state, but still works.
			/*Rn = XOR(Ln, function(Rn, keyPerms[(decrypt?(16-i):(i-1))]));
			Ln = prevR;
			prevR = Rn;*/
			what[i][1] = what[i-1][0];
			boolean[] temp = function(what[i-1][0], keyPerms[(decrypt?(16-i):(i-1))]);
			what[i][0] = XOR(what[i-1][1],
					temp);
			
//			System.out.print("R"+i+":");
//			printBlockAsLine(what[i][0], 4);
//			System.out.print("L"+i+":");
//			printBlockAsLine(what[i][1], 4);
		}
		//data = concatinate(new boolean[][] { Rn, Ln});
		//data = invInitialPermutation(data);
		data = concatinate(what[16]);
//		System.out.print("PREO:");
//		printBlockAsLine(data, 8);
		data = invInitialPermutation(data);
//		System.out.print("OUTP:");
//		printBlockAsLine(data, 8);
		return data;
	}

	public static boolean[] function(boolean[] in, boolean[] key) {
		boolean[] data = expandFunction(in);
		data = XOR(data, key);
		
//		System.out.print("ExKS:");
//		printBlockAsLine(data, 6);

		int k = 0;
		boolean[][] holder = new boolean[8][6];
		boolean[] res = { };
		for (int i = 0; i < 8; i++) {
			boolean[] hold = new boolean[6];
			for (int j = 0; j < 6; j++) {
				hold[j] = data[k];
				k++;
			}
			res = concatinate(new boolean[][] {res, sSelection(hold, getSBoxN(i+1))});
		}
//		System.out.print("SBOX:");
//		printBlockAsLine(res, 4);
		res = permute(res);
//		System.out.print("FUNC:");
//		printBlockAsLine(res, 8);

		return res;
	}

	public static boolean[][] generateKeyPerms(boolean[] key) {
		boolean[][] res = new boolean[16][48];
		boolean[] b = permutedKeyChoice1(key);
		int magic = b.length/2;
		boolean[] C = new boolean[magic];
		boolean[] D = new boolean[magic];
		for (int i = 0; i < magic; i++) {
			C[i] = b[i];
			D[i] = b[i+magic];
		}
		//we now have C_0 and D_0.
		for (int i = 0; i < 16; i++) {
			if (i == 0 || i == 1 || i == 8 || i == 15) {
				C = bitShift(C, -1);
				D = bitShift(D, -1);
			} else {
				C = bitShift(C, -2);
				D = bitShift(D, -2);
			}
			res[i] = permutedKeyChoice2(concatinate(new boolean[][] {C, D}));
		}
		return res;
	}
	public static boolean[] getKeyPerm(int n, boolean[] b) {
		boolean[] res = null;
		if (n == 0) { //for n=0 we need to perform the initial key permutation
			b = permutedKeyChoice1(b);
			//OK
		}
		if (b.length%2 != 0)
			throw new IllegalArgumentException();
		
		int magic = b.length/2;
		boolean[] C = new boolean[magic];
		boolean[] D = new boolean[magic];
		for (int i = 0; i < magic; i++) {
			C[i] = b[i];
			D[i] = b[i+magic];
		}
		//OK
		if (n == 0 || n == 1 || n == 8 || n == 15) {
			C = bitShift(C, -1);
			D = bitShift(D, -1);
		} else {
			C = bitShift(C, -2);
			D = bitShift(D, -2);
		}
		res = concatinate(new boolean[][] {C, D});
		res = permutedKeyChoice2(res);
		return res;
	}
	public static boolean[] bitShift(boolean[] b, int n) {
		//shifts the bits in the array by n to the right (negative n means left shift)
		int magic = b.length;
		boolean[] res = new boolean[magic];
		int k = n;
		for (int i = 0; i < magic; i++) {
			//System.out.print(i+":"+k+";");
			if (k < 0)
				k += magic;
			if (k >= magic)
				k -= magic;
			//System.out.println(k);
			//if k is less than 0, loop around to the end of the array
			//if k is greater than the size of the array, loop around to the beginning of it.
			res[k] = b[i];
			k++;
		}
		return res;
	}
	public static boolean[] permutedKeyChoice1(boolean[] b) {
		try{
			return new boolean[] { 
					b[56], b[48], b[40], b[32], b[24], b[16], b[8],
					b[0],  b[57], b[49], b[41], b[33], b[25], b[17],
					b[9],  b[1],  b[58], b[50], b[42], b[34], b[26],
					b[18], b[10], b[2],  b[59], b[51], b[43], b[35],
					
					b[62], b[54], b[46], b[38], b[30], b[22], b[14],
					b[6],  b[61], b[53], b[45], b[37], b[29], b[21],
					b[13], b[5],  b[60], b[52], b[44], b[36], b[28], 
					b[20], b[12], b[4],  b[27], b[19], b[11], b[3]
			};
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static boolean[] permutedKeyChoice2(boolean[] b) {
		try {
			return new boolean[] {
					b[13], b[16], b[10], b[23], b[0],  b[4],
					b[2],  b[27], b[14], b[5],  b[20], b[9],
					b[22], b[18], b[11], b[3],  b[25], b[7],
					b[15], b[6],  b[26], b[19], b[12], b[1],
					b[40], b[51], b[30], b[36], b[46], b[54],
					b[29], b[39], b[50], b[44], b[32], b[47],
					b[43], b[48], b[38], b[55], b[33], b[52],
					b[45], b[41], b[49], b[35], b[28], b[31]
			};
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return null;
	}
	//part of the jig
	public static void printBlock(boolean[] b, int s) {
		for (int i = 1; i < b.length+1; i++) {
			System.out.print(b[i-1]?1:0);
			if (s != 0 && i%s == 0)
				System.out.print("\n");
		}
		System.out.print("\n");
	}
	public static void printBlockAsLine(boolean[] b, int s) {
		for (int i = 1; i < b.length+1; i++) {
			System.out.print(b[i-1]?1:0);
			if (s != 0 && i%s ==0)
				System.out.print(" ");
		}
		System.out.print("\n");
	}
	public static boolean[] expandFunction(boolean[] b) {
		boolean[] res = null;
		try {
			boolean[] IP = {
					b[31], b[0], b[1], b[2], b[3], b[4],
					b[3],  b[4], b[5], b[6], b[7], b[8],
					b[7],  b[8], b[9], b[10], b[11], b[12],
					b[11], b[12], b[13], b[14], b[15], b[16],
					b[15], b[16], b[17], b[18], b[19], b[20],
					b[19], b[20], b[21], b[22], b[23], b[24],
					b[23], b[24], b[25], b[26], b[27], b[28],
					b[27], b[28], b[29], b[30], b[31], b[0]
			};
			res = IP;
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static boolean[] XOR(boolean[] a, boolean[] b) throws ArrayIndexOutOfBoundsException {
//		System.out.println("a"+a);
//		System.out.println("b"+b);
		if (a.length != b.length)
			throw new ArrayIndexOutOfBoundsException();
		int j = (a.length <= b.length ? b.length : a.length);
		boolean[] res = new boolean[j];
		for (int i = 0; i < j; i++) {
			res[i] = a[i] ^ b[i];
		}
		
		return res;
	}
	public static boolean[] permute(boolean[] b) { //also known as "primitive function"
		boolean[] res = null;

		try {
			boolean[] B = {
					b[15], b[6],  b[19], b[20],
					b[28], b[11], b[27], b[16],
					b[0],  b[14], b[22], b[25],
					b[4],  b[17], b[30], b[9],
					b[1],  b[7],  b[23], b[13],
					b[31], b[26], b[2],  b[8], 
					b[18], b[12], b[29], b[5],
					b[21], b[10], b[3],  b[24]
			};
			res = B;
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		
		return res;
	}
	public static boolean[] initialPermutation(boolean[] b) {
		boolean [] res = null;
		try {
			boolean[] IP = {
					b[57], b[49], b[41], b[33], b[25], b[17], b[9],  b[1],
					b[59], b[51], b[43], b[35], b[27], b[19], b[11], b[3], 
					b[61], b[53], b[45], b[37], b[29], b[21], b[13], b[5],
					b[63], b[55], b[47], b[39], b[31], b[23], b[15], b[7],
					b[56], b[48], b[40], b[32], b[24], b[16], b[8],  b[0],
					b[58], b[50], b[42], b[34], b[26], b[18], b[10], b[2],
					b[60], b[52], b[44], b[36], b[28], b[20], b[12], b[4], 
					b[62], b[54], b[46], b[38], b[30], b[22], b[14], b[6]
			};
			res = IP;
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static boolean[] invInitialPermutation(boolean[] b) {
		boolean[] res = null;
		try {
			return new boolean[] {
					b[39], b[7], b[47], b[15], b[55], b[23], b[63], b[31],
					b[38], b[6], b[46], b[14], b[54], b[22], b[62], b[30],
					b[37], b[5], b[45], b[13], b[53], b[21], b[61], b[29],
					b[36], b[4], b[44], b[12], b[52], b[20], b[60], b[28],
					b[35], b[3], b[43], b[11], b[51], b[19], b[59], b[27],
					b[34], b[2], b[42], b[10], b[50], b[18], b[58], b[26],
					b[33], b[1], b[41], b[9],  b[49], b[17], b[57], b[25],
					b[32], b[0], b[40], b[8],  b[48], b[16], b[56], b[24],
			};
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return res;
	}
	public static boolean[] concatinate(boolean[][] b) {
		StringBuilder sb = new StringBuilder();
		for (boolean[] B : b) {
			for (int i = 0; i < B.length; i++)
				sb.append(B[i]?1:0);
		}
		boolean[] res = new boolean[sb.length()];
		String s = sb.toString();
		for (int i = 0; i < s.length(); i++)
			res[i] = (s.charAt(i) == '1'?true:false);
		return res;
	}
	public static boolean[] sSelection(boolean[] b, int[][] sbox) {
		boolean[] res = new boolean[4];
		int row = binaryToInt((b[0]?1:0)+""+(b[5]?1:0));
		int column = binaryToInt((b[1]?1:0)+""+(b[2]?1:0)+""+(b[3]?1:0)+""+(b[4]?1:0));
		//System.out.println("ROW:"+row+"::COLUMN:"+column);
		int r = sbox[row][column];
		String s = Integer.toBinaryString(r);
		if (s.length() < 4) {
			String k = "";
			for (int i = 0; i < (4 - s.length()); i++) {
				k+="0";
			}
			s = k+s;
		}
		for (int i = 0; i < s.length(); i++)
			res[i] = (s.charAt(i) == '1' ? true:false);
		
		return res;
	}
	public static int binaryToInt(String s) {
		int r = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '1')
				r+= Math.pow(2, s.length()-i-1);
		}
		return r;
	}
	public static int[][] getSBoxN(int n) {
		switch (n) {
		case 1: return s_1;
		case 2: return s_2;
		case 3: return s_3;
		case 4: return s_4;
		case 5: return s_5;
		case 6: return s_6;
		case 7: return s_7;
		case 8: return s_8;
		}
		return null;
	}
	//tests
	public static boolean tests() {
		
		//bitShift test
		boolean[] derp = { true, false, false, false, false, false, false, false, false };
		int shiftval = -5;
		System.out.println("bitShift test with shiftval="+shiftval);
		for(boolean b : derp)
			System.out.print(b?1:0);
		derp = bitShift(derp, shiftval);
		System.out.print("-->");
		for(boolean b : derp)
			System.out.print(b? '1' : '0');
		
		//blockPrint test
		System.out.println("\n \n" +"printBlock test with splitAt = 3");
		printBlock(derp, 3);
		
		
		System.out.println("concat test");
		//concatinate test
		boolean[] test2 = concatinate(new boolean[][] { {false}, {true} });
		for (boolean b : test2)
			System.out.print(b?1:0);
		
		return true;
	}
}

//GRAVEYARD
/*
 * 		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s = null;
		while (true) {
		try {
			s = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Pattern rp = Pattern.compile("\\s+");
		Matcher m = rp.matcher(s);
		
		System.out.println(m.replaceAll(", "));
		}
 * 
 */
