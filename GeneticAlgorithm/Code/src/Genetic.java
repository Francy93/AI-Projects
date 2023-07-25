public class Genetic extends GenAsset{

	// constructors
	public Genetic()							{ this(5, 20, 	SelMethod.RankSel); }
	public Genetic(SelMethod selector)			{ this(5, 20, 	selector); }
	public Genetic(int genes, int population)	{ this(genes,	population,	SelMethod.RankSel); }
	public Genetic(int x, int y, SelMethod s)	{ super(x, y, s); }



	/**
	 * @since mutating gene (Swap Mutation)
	 * @see https://bit.ly/3GyrfeE
	 * @param chromosomes
	 */ 
	private void mutation(int[]... chromosomes){

		for(int[] chromo: chromosomes){
			for(int i=0; i<chromoLength; i++){
				if(Math.random() <= mutationRate/chromoLength*(chromoLength-1)){

					// Swap Mutation
					int a,b;
					do{ a = (int)Util.rangeRandom(0, chromoLength-1);
						b = (int)Util.rangeRandom(0, chromoLength-1);
					}while(a==b);

					final int holder = chromo[a];
					chromo[a] = chromo[b];
					chromo[b] = holder;
				}else if(Math.random() <= mutationRate/chromoLength){

					// Random Resetting Mutation
					chromo[(int)Util.rangeRandom(0, chromoLength-1)] = (int)Util.rangeRandom(0, geneSet);
				}
			}
		}
	}


	/**
	 * @since blending a certain genes range of all the chromosomes
	 * @see https://bit.ly/3biBWDt
	 * @param father
	 * @param mather
	 */
	private void crossover(int father, int mather){
		// setting the crossover combination
		double max = chromoLength-1;
		double min = Util.rangeRandom(0, max);
		max = Util.rangeRandom(0, max);
		min = min<max?min:max;
		max = max>min?max:min;

		newGeneration[mather] = baseGeneration[mather].clone();
		newGeneration[father] = baseGeneration[father].clone();

		// starting crossover
		for(int i = (int)min; i <= max; i++){
			newGeneration[mather][i]	= baseGeneration[father][i];
			newGeneration[father][i]	= baseGeneration[mather][i];
		}
	}


	/**
	 * @since Roulette Wheel Selection
	 * @see https://bit.ly/3moKqzj
	 * @param selector
	 */
	protected void selection(SelMethod selector){

		switch(selector){
			case FitPropSel:
			
				// making the Wheel (Fitness Proportioned Selection)
				double[] ranking = new double[popuLength];
				for(int i=0; i<popuLength; i++){
					ranking[i] = (geneFitness(baseGeneration[i]) + (i<1?0: ranking[i-1]));
				}

				// selecting parents
				for(int mother = 0; mother<popuLength; mother++){
					//spinning the "wheel"
					int father = binarySearch(ranking, Math.ceil(Util.rangeRandom(0, ranking[ranking.length-1])));

					// running the crossover
					crossover(father, mother);
					// running the mutation
					mutation(newGeneration[father],newGeneration[mother]);
				}
				break;
			case RankSel:

				// Rank Selection
				for(int mother=0; mother<popuLength-1; mother++){
					final int father = mother+1;
					// running the crossover
					crossover(father, mother);
					// running the mutation
					mutation(newGeneration[father],newGeneration[mother]);
				}
				break;
			default: System.out.println(Util.colorText("\r\nSelection method not contemplated!\r\n", "red"));
		}
	}


	// make new generation
	protected void makeNewGen(){
		currentGeneration++;
		baseGeneration = newGeneration.clone();

		// selecting parents
		selection(selectorM);
		// sorting newGeneration
		populSort(newGeneration, sortM);
	} 
}