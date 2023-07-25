public class Perceptron{

	private final Cities cities;
    private String[] predicted;
	private double[] weight;
	private double[] bestWeight;
	private int bestMatch = 0;
	private int trainings = 0;
	private final int epochs;
	private final double threshold;
	private final double learningCoeff;



    // constructor
	public Perceptron(Cities c){
        cities = c;
        threshold = 1;
		learningCoeff = c.getClosestDistance()/Math.pow(c.getSize(),3);
		epochs = (int) Math.ceil(c.getArea()/learningCoeff);
        predicted = new String[c.getSize()];
		initWeights();
		training();
		performing();
    }
	public Perceptron(Cities c, double l){
        cities = c;
        threshold = 1;
		learningCoeff = l;
		epochs = (int) Math.ceil(c.getArea()/learningCoeff);
        predicted = new String[c.getSize()];
		initWeights();
		training();
		performing();
    }
	public Perceptron(Cities c, double l, double t){
        cities = c;
        threshold = t;
		learningCoeff = l;
		epochs = (int) Math.ceil(c.getArea()/learningCoeff);
        predicted = new String[c.getSize()];
		initWeights();
		training();
		performing();
    }




	// initialising Weights
	public void initWeights(){
		weight = new double[cities.getDimensions()];

		// initialising with random values
		for (int i=0; i<weight.length; i++){
			weight[i] = (Math.random()-.5)/10.0;
		}
		bestWeight = new double[weight.length];
		System.arraycopy(weight, 0, bestWeight, 0, bestWeight.length);
	}

    // calculating
    private double neuron(int index){
        double sum = 0;

        for(int axis=0; axis<cities.getDimensions(); axis++){
            sum += cities.getVal(index, axis) * weight[axis];
        }
        return sum;
    }

    // prediction
    private int estimation(int index){
        return neuron(index) < threshold? -1: 1;
    }

	// testing
	private boolean testMatch(){
		boolean completed = true;
		int matches = 0;

		for(int i=0; i<cities.getSize(); i++){
			if (estimation(i) != (cities.getName(i).equals("A")? -1: 1))		completed = false;
			else matches++;
		}

		if(matches >= bestMatch){
			System.arraycopy(weight, 0, bestWeight, 0, bestWeight.length);
			bestMatch = matches;
		}
		return completed;
	}

	// training
	private void training(){
		for(trainings = 0; trainings < epochs && !testMatch(); trainings++){

			for(int i=0; i<cities.getSize(); i++){
				int outcome	= (cities.getName(i).equals("A")? -1: 1) - estimation(i);
				if (outcome != 0){
					for(int axis=0; axis<weight.length; axis++){
						weight[axis] += learningCoeff * cities.getVal(i,axis) * outcome;
					}
				}
			}
		}
		if(!testMatch())	System.arraycopy(bestWeight, 0, weight, 0, weight.length);
	}

	// performing the prediction trained
	private void performing(){
		for(int i=0; i<cities.getSize(); i++){
			predicted[i] = (estimation(i) == -1? "A": "B");
		}
	}



	// GETTER METHODS //

	// cityName getter
	public String getName(int index){
		index = index<0? 0: index;
		return predicted[index];
	}

	// getting weight
	public double getWeight(int axis){
		axis = axis<0? 0: axis;
		return weight[axis];
	}

	// getting number of trainings performed
	public int getIteractions(){
		return trainings;
	}

	// print detailes
	public void output(){
		System.out.println(Util.colorText("PERCEPTRON", "magenta"));
		for(int i=0; i<cities.getDimensions(); i++){
			System.out.println(Util.colorText("Weight axis "+(i+1)+": 	", "blue") + getWeight(i));
		}
		System.out.println(Util.colorText("No. trainings:	", "blue") + getIteractions() + " / " + epochs);

		String proofList = "";
		double precision = 0;
		for(int i=0; i<cities.getSize(); i++){
			precision = cities.getName(i).equals(getName(i))? precision+1: precision;
			String coloredName = cities.getName(i).equals(getName(i))? Util.colorText(getName(i),"green"):Util.colorText(getName(i),"red");
			proofList += "Estimated: " + cities.getName(i) + Util.colorText(" ---> ","yellow") + "Predicted: " + coloredName + "\r\n";
		}

		precision = Util.round(precision * 100 / cities.getSize(), 1);
		System.out.println(Util.colorText("\r\nPerceptron precision "+ precision +"% ..", "yellow"));
		System.out.println(proofList);
	}
	
}