import java.util.Scanner;

public class Main{

	// making scanner object for the terminal input
	public static Scanner scan = new Scanner(System.in);


	// first start setting
	public static boolean firstSetting(){
		System.out.println("\r\nWelcome to the LINEAR CATEGORISER\r\n");
		System.out.println("First start setting:");
		System.out.println("Escape Ansi color ON  (VScode)... 1");
		System.out.println("Escape Ansi color OFF (Eclipse).. 2");
		System.out.println("To exit.......................... 0\r\n");

		boolean kill = false;
		for(boolean exit=false; !exit;){
			System.out.print("Please, enter your choice :>");

			String choice = scan.nextLine();
			System.out.println("");
			
			switch(choice){
				case "0":
					kill = true;
					exit = true;
					break;
				case "1":
					Util.setColor(true);
					exit = true;
					break;
				case "2":
					Util.setColor(false);
					exit = true;
					break;
				default: 
					System.out.print("Wrong selection! Try again.");
					break;
			}
		}
		return kill;
	}

	// printing outcome
	public static void output(Cities cities){
		LinearRegr line		= new LinearRegr(cities);
		Perceptron percept	= new Perceptron(cities);
		
		System.out.println(Util.colorText("CALCULATING... ↓","green"));
		System.out.println("\r\n");
		line.output();
		System.out.println("\r\n");
		percept.output();
	}
	



	public static void main(String[] args){
		boolean kill = firstSetting();

		// starting the main loop
		while(!kill){
			//getting user choice
			System.out.println("\r\n");
			System.out.print("Please, enter a file name or press '0' to exit :>");
			String fileName = scan.nextLine();
			System.out.println("");

			if(fileName.equals("0")) break;

			Cities cities = new Cities(fileName);
			if(cities.found()) output(cities);
			else System.out.println("Wrong input! Try again");
		}
		
		System.out.println("Successfully exited!");
	}
}