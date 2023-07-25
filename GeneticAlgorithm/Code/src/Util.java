import java.util.Scanner;

public class Util{
	public static Scanner cin = new Scanner(System.in);

	//determining whether the text get colored or not
	private static boolean colorState = true;
	public static void setColor(boolean b){ colorState = b; }
	public static boolean colorState(){ return colorState;  }
    
	
	// generation string
	public static String StringRepeat(String s, long n){
		String str = "";
		for(long i=0; i<n; i++){
			str += s;
		}
		return str;
	}

	// type detector
	public static boolean isNumeric(String str){ 
		try{  
			Double.parseDouble(str);  
			return true;
		}catch(NumberFormatException e){  
			return false;
		}  
	}

	// sum values of array
	public static double arraySum(int[] array) { 
		double sum = 0;
		for (double value: array) sum += value;
		return sum; 
	}
	public static double arraySum(double[] array) {
		double sum = 0;
		for (double value: array) sum += value;
		return sum;
	}

	// avarage finder
	public static int arrayAvarage(double[] array){
		int index = 0;
		double difference = 0;
		double mean = arraySum(array) / array.length;

		for(int i=0; i<array.length; i++){
			double sub = mean <= array[i]? array[i] - mean: mean - array[i];

			if(i==0 || difference > sub){
				difference = sub;
				index = i;
			}
		}
		return index;
	}

	// rounding decimal numbers
	public static double round(double n, int d){
		double round = Math.pow(10, d>=0? d: 0);
		return Math.round(n*round)/round;
	}

	// getting ansi color
	public static String color(String color){
		if(colorState){
			String[] colors = {"black","red","green","yellow","blue","magenta","cyan","white"};
			if(color.equals("reset"))	return "\u001B[0m";

			for(int i=0; i<colors.length; i++){
				if(color.equals(colors[i]))		return "\u001B[3"+i+"m";
			}
		}

		return "";
	}

	//colored text
	public static String colorText(String text, String color){
		return color(color) + text + color("reset");
	}

	/**
     * @since checking whether a number exists already in the array
     * @param arr
     * @param b
     * @return boolean
     */
    public static boolean exists(int[] arr, int b){
        for(int a: arr){
            if(a==b) return true;
        }
        return false;
    }

	/**
     * @since generate an integer within a range
     * @param min
     * @param max
     * @return int
     */
    public static double rangeRandom(double min, double max){
		min = min<max?min:max;
		max = min<max?max:min;
        return Math.round((Math.random() * (max - min)) + min);
    }

	// append arrays
	public static <T> T[] append(T[] destination, T[] array) {
		for(int i=0; i<array.length; i++) destination[i] = array[i];
		return destination;
	}

	/**
	 * @since returns a string of numerate options
	 * @param opts
	 * @param minSize
	 * @param colors
	 * @param print
	 * @return String[]
	 */
	public static String[] navOptions(String[] opts, long minSize, String colors, boolean print){
		String[] options = new String[opts.length + 2];
		append(options, opts);

        final long oSize = opts.length;
        long iSize = 2, i = 0, longest = 7;

        // getting the longest string size
        for(String o: opts){
            final long strSize = o.length();
            longest = strSize > longest? strSize: longest;
            if(++i == oSize) iSize = Long.toString(i).length() > iSize? Long.toString(i).length(): iSize;
        }

        String cStart = color(colors);						//yellow corresponds to: "\033[1;35m"
        String cEnd = cStart==""? "": color("reset");	//reset  corresponds to: "\033[0m"

        longest = longest+3>=(double)minSize-iSize? longest+3: minSize-iSize;
        i = 0;
        for(String o: opts){
            final long indexSize = iSize - Long.toString(++i).length();
            final long gap = longest - o.length();
            String index = cStart + Long.toString(i) + cEnd;

            options[(int)i-1] = o + StringRepeat(".", gap+indexSize) + index;
        }
        options[opts.length]	= "Go back" + StringRepeat(".",longest-7+iSize-1) + cStart+"0"+cEnd;
        options[opts.length+1]	= "Exit"    + StringRepeat(".",longest-4+iSize-2) + cStart+"00"+cEnd;

        // printing
        if(print) for(String line: options) System.out.println(line);
        return options;
    }

	// getting console input
	public static String cinln(){
        String input = cin.nextLine();
        System.out.println();
        return input;
    }


    /**
     * @since getting user input
     * @param max
     * @return int
     */
    public static int getChoice(int options){
        options = options<2?1: options;
		
		//checking the choice
		String input ="";
		
		while(true){
			System.out.print("Enter a choice here :> ");
			input = cinln();
			
			if(!input.equals("0") && !input.equals("00")){
				for(int i=1; i<=options; i++){
					if(input.equals(Integer.toString(i))) return i;
				}
				System.out.println(colorText("WRONG SELECTION! Try again.", "yellow"));
			}else if(input.equals("0")) return 0;
			else return -1;
		}
	}

	/**
         * @since display options and return choice
         * @param options 
         * @param min
         * @return int 
         */
	public static int navChoice(long min, String ... options){
        
        //displaying options
		navOptions(options, min, "yellow", true);
		System.out.println();
		//getting the choice
		return getChoice(options.length);
    }
}