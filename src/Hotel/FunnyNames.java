package Hotel;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class FunnyNames {
	public static String getRandom(){
		ArrayList<String> names = new ArrayList<String>();
		try {
			Scanner scan = new Scanner( new File("src/funnyNames.txt") );
			while(scan.hasNext()){
				String first = scan.next();
				if( first.startsWith("(") ){
					while(!first.endsWith(")") ){
						first = scan.next();
					}
					continue;
				}

				if( first.endsWith(",") ){
					first = first.substring(0,first.length()-1);
				}

				String second = "";
				if( scan.hasNext() ){
					second = scan.next();

					if( second.startsWith("(") ){
						while(!second.endsWith(")") ){
							second = scan.next();
						}

					}
					//System.out.println("=== " + second);
					if( second.endsWith(")") ){
						second = second.substring(0, second.length()-1);
					}
					//System.out.println("=== " + second);
				}

				if( second.endsWith(",") ){
					second = second.substring(0,second.length()-1);
				}

				names.add(first + " " + second);
			}

			// Show Names
			//for(String s : names){ System.out.println(s);}

			scan.close();

			Random rand = new Random();
			return names.get(rand.nextInt(names.size()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ("Joe Mamma");
	}

	public static void main(String[] args){
		System.out.println(FunnyNames.getRandom());
	}
}

