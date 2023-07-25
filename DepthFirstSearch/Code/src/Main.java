import java.util.Scanner;

public class Main {
	
	public static Scanner scan = new Scanner(System.in);


	// selecting the challenge mode
	public static String modeChoice(){
		String mode = "";
		while(true){
			System.out.println("Start a standard challenge...1");
			System.out.println("Start a custom challenge.....2");
	
			System.out.print("\r\nEnter your choice here :> ");
			mode = scan.nextLine();
			
			if(!(mode.equals("1") || mode.equals("2"))) System.out.println("\r\nWrong choice! Try again\r\n");
			else break;
		}
		return mode;
	}

	// making the challenge chosen
	public static Game makeChallenge(String mode){
		if(mode.equals("2")){
			String[] require = {"Enter the Missionaries quantity :> ", "Enter the Cannibals quantity :> ", "Enter the boat seats quantity :> "};
			int[] choices = new int[require.length];

			// cycling over the options
			for(int i=0; i<require.length; i++){
				System.out.print(require[i]);
				String choice = scan.nextLine();
				choices[i] = Integer.parseInt(choice);
			}

			// starting the game challenge
			return new Game(choices[0], choices[1], choices[2]);
		}else {
			// starting the game challenge
			return new Game();
		}
	}






	public static void main(String[] args) {
		Game solution;
		
		// welcome message
		System.out.println("\r\nWelcome to the Missionaries & Cannibals challenge\r\n");

		// selecting the challenge mode
		String mode = modeChoice();
		System.out.print("");

		// running the challenge
		solution = makeChallenge(mode);
		System.out.println("\r\n");

		// showing the challenge result
		solution.output();
	}
}