//Shkruani nje program qe kontrollon nese abc eshte ne mes te stringes

import java.util.Scanner;

public class ushtrim {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String string;
		System.out.println("Jep nje string: ");
		string = sc.nextLine();
		String new_string = "";
		for(int i = 1; i < string.length()-1; i++) {
			new_string += string.charAt(i);
		}
		
		if(new_string.contains("abc")) {
			System.out.println("Stringa e dhene e permban abc ne mes!");
		} else {
			System.out.println("Stringa e dhene nuk e permban abc ne mes!");
		}
	}

}
