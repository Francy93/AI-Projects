abstract class GenAsset {

    protected int[][] baseGeneration, newGeneration;
    protected final int chromoLength, popuLength, geneSet;
	public int currentGeneration;
	protected final double mutationRate;
    
    public enum SelMethod {
        FitPropSel("Fitness Proportionate Selection"), RankSel("Rank Selection");
        private final String fullName;
        SelMethod(String s){ fullName = s; }

        @Override
        public String toString(){ return fullName; }

        public static String[] fullValues(){
            String[] enumValues = new String[GenAsset.SelMethod.values().length];
            SelMethod[] list = GenAsset.SelMethod.values();

            for(int i=0; i<list.length; i++){
                enumValues[i] = list[i].toString();
            }
            return enumValues;
        }
    }
    protected enum SortMode{
        Increasing(true), Decreasing(false);
        public final boolean toBool;
        SortMode(boolean b){ toBool=b; }
    }
    final SelMethod selectorM;
	final SortMode sortM;

    // constructorr
    protected GenAsset(int x, int y, SelMethod s) {
		currentGeneration	= 0;
        chromoLength		= x;
		popuLength			= y;
		geneSet				= x*2;
		mutationRate		= (double) 1/x;
		selectorM   		= s;
		sortM    			= setSortMode();

        baseGeneration		= new int[y][x];
        newGeneration		= new int[y][x];

        populInit();
		makeNewGen();
    }





    // make new generation
	abstract protected void makeNewGen();


    /**
    * @since setting the sorting mode
    * @return SortMode (enum)
    */
    private SortMode setSortMode(){
        if(selectorM==SelMethod.FitPropSel || selectorM==SelMethod.RankSel){
            return SortMode.Increasing;
        }else return SortMode.Decreasing;
    }


    /**
     * @since summing the array content
     * @param arr
     * @return double
     */
    protected double geneFitness(int[] arr){
        return Util.arraySum(arr);
    }


    /**
     * @since (QuickSort) sorting genes according to theire sum
     * @see https://bit.ly/3GGQU4W
     * @param arr
     * @param mode (SortMode)
     * @return int[][]
     */
	protected int[][] populSort(int[][] arr, SortMode mode){
		return populSort(arr, 0, arr.length-1, mode.toBool);
	}
	protected int[][] populSort(int[][] popul, int left, int right, boolean mode){
		int s = popul.length;

		if(s > 0){
			left	= left	< 1? 0:	left	>= s? s-1: left;
			right	= right	< 1? 0:	right	>= s? s-1: right;
			int l	= left, r = right, way;

			// getting mid point
			int mid = (l + r) / 2;
			//getting the pivot (number of people off the boat)
			double pivot = geneFitness(popul[mid]);

			// partition 
			while (l <= r) {
				// loop left index if the current number of people is less than in the pivot
				do way = mode? l++: r--;
				while (geneFitness(popul[way]) < pivot);
				// loop right index if the current number of people is greater than in the pivot
				do way = mode? r--: l++;
				while (geneFitness(popul[way]) > pivot);

				if (--l <= ++r) {
					int[] tmpNode = popul[l];
					popul[l++] = popul[r];
					popul[r--] = tmpNode;
				}
			}

			// recursion
			if (left < r ) populSort(popul, left, r, mode );
			if (l < right) populSort(popul, l, right, mode);
		}
		return popul;
	}

	/**
     * @since (binarySearch) searching in a sorted array
     * @see https://bit.ly/3025DXi
     * @param arr (array to cycle)
     * @param pivot (value to search)
     * @return int (array index)
     */
    protected int binarySearch(double[] arr, double pivot){ 
        int s = arr.length, mid = 0;

		if(s > 0){
			int left = 0, right	= s - 1;
			int l	 = left,  r = right;

			// partition 
			while (l <= r) {
                // getting mid point ("Math.floor" determines the search be ended in the index at the right hand side)
			    mid = (int) Math.floor((l + r) / 2);

                if(arr[mid] == pivot) break;
                
				if(l < r){
					if(arr[mid] > pivot) r = mid - 1;
					else l = mid + 1;
				}else break;
            }
        }
		
		return mid;
    }


    // initialising the matrix
    private void populInit() {

        for (int i = 0; i < popuLength; i++) {
            for (int j = 0; j < chromoLength; j++) {
                int chromosome;

                do chromosome = (int)Util.rangeRandom(0, geneSet);
                while(Util.exists(newGeneration[i], chromosome));

                newGeneration[i][j] = chromosome;
            }
        }
		populSort(newGeneration, sortM);
    }


    // checking for the final state
    private  boolean finalState(){
        int prevGen = newGeneration[0][0];
		boolean goal = false, full = true;

		for(int i=0; i<popuLength; i++){
			int counter = 0;
			for(int j=0; j<chromoLength; j++){
				counter += newGeneration[i][j];

				if(newGeneration[i][j] != prevGen){
					full = false;
				}
			}
			if(counter == geneSet*chromoLength){
				goal = true;
				break;
			}
		}
        return goal || full? true: false;
    }

    
    /**
	 * @since recursive creation of new generations
	 * @param g
	 * @return Genetic
	 */
	public void train(int g){
		while(currentGeneration < g) makeNewGen();
	}
	public void train(){
		while(!finalState()) makeNewGen();
	}


    /**
     * @since printing matrix content
     * @param arr
     */
    private void populPrint(int[][] arr){
        String outcome = "";

        for (int row = 0; row < arr.length; row++) {
            outcome += "[";
            for (int column = 0; column < arr[row].length; column++) {
                outcome += (arr[row][column]) + (column<arr[row].length-1? ", ": "");
            }
            outcome += "] 	Fitness: " + geneFitness(arr[row]) + "\n";
        }

        System.out.println(outcome);
    }


    // printing outcome
    public void outputBase() {

        System.out.println("\r\nBase generation: ");
        populPrint(baseGeneration);
        System.out.println("<<<<< "+currentGeneration+"° GENERATION OF: "+selectorM+" >>>>>");
    }
    public void outputNew() {

        System.out.println("\r\nNew generation: ");
        populPrint(newGeneration);
        System.out.println(Util.colorText("<<<<< "+currentGeneration+"° GENERATION OF: "+selectorM+" >>>>>\r\n", "green"));  
    }
}