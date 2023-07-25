
public class Main {
	public static Genetic gen;

	public static int envSet(){
		System.out.println(Util.colorText("Environment setting", "magenta"));

		switch(Util.navChoice(5,"Colors ON  (VScode)","Colors OFF (Eclipse)")){
			case -1: return -1;
			case  0: return  0;
			case  1: Util.setColor(true);
				break;
			case  2: Util.setColor(false);
		}
		return 1;
	}

	public static int setMatrix(Genetic.SelMethod SM){
		System.out.println(Util.colorText("Set the matrix", "magenta"));
		int nav = Util.navChoice(5, "Standard Matrix","Custom Matrix");

		switch (nav) {
			case -1: case 0:	return nav;
			case  1: gen = new Genetic(SM);
				break;
			case  2: 
				String[] text = {"Set chromosomes length :>", "Set population length :>"};
				int[] values = new int[text.length];

				for(int i=0; i< values.length; i++){
					while(true){
						System.out.print(text[i]);
						String input = Util.cinln();

						if(Util.isNumeric(input) && Integer.parseInt(input) > 0){
							values[i] = Integer.parseInt(input);
							break;
						}else System.out.println(Util.colorText("Wrong value! Try again", "yellow"));
					}
				}
				gen = new Genetic(values[0], values[1], SM);
		}
		return 1;
	}

	// instantiate two object for two different selection method
	public static int makeGen(){

		for(boolean loop = false; !loop; loop = !loop){
			System.out.println(Util.colorText("Choose a selection Method", "magenta"));
			int nav = Util.navChoice(5, Genetic.SelMethod.fullValues());

			switch (nav){
				case -1: case 0:	return	nav;
				default:	
					nav = setMatrix(Genetic.SelMethod.values()[nav-1]);
					if (nav == 0) loop = true;
					else if(nav == -1) return -1;
			}
		}
		return 1;
	}




	public static void main(String[] args) {
		Util.setColor(false);
		System.out.println(Util.colorText("\r\n\r\nWelcome to the Genetic Algorithm trial\r\n", "cyan"));

		for(int nav = envSet(); nav != -1; nav = nav!=0? nav: envSet()){
			nav = nav==0? nav: makeGen();

			// getting result before and after cycling the generations
			if(nav == 1){
				//gen.outputBase();
				gen.train();
				gen.outputNew();
			}
		}

		// exiting the main loop
		System.out.println(Util.colorText("Successfully exited!", "green"));
	}

}