
public class LinearRegr{
	
	private double[][] line = new double[2][2];
	private final Cities cities;


	// constructor
	public LinearRegr(Cities c){
		cities = c;
		lineCalc();
	}



	//calculating the line
	private void lineCalc(){
		double sumXsquared = 0, sum_dXdY = 0;

		// 1° step: calculating the mean point
		double meanX = Util.arraySum(cities.getAxis(0)) / cities.getSize();
		double meanY = Util.arraySum(cities.getAxis(1)) / cities.getSize();

		// 2°-3°-4° steps
		for(int i=0; i<cities.getSize(); i++){
			//2° step: deducting
			double deductX = cities.getVal(i,0) - meanX;
			double deductY = cities.getVal(i,1) - meanY;

			//3° step: squaring
			sumXsquared += Math.pow(deductX, 2);
			//4° step: suming deducted X and dedudcted Y
			sum_dXdY += deductX*deductY;
		}


		// 5° step: calculting the offset
		double slope =  sum_dXdY / sumXsquared;
		double offset = meanY - (slope * meanX);

		// setting the two points coordinates
		line[0][0] = 0;
		line[0][1] = offset;
		line[1][0] = meanX;
		line[1][1] = meanY;
	}

	

	// GETTER METHODs //

	public double offsetX(){
 		return line[0][0];
	}

	public double offsetY(){
		return line[0][1];
	}

	public double meanX(){
		return line[1][0];
	}

	public double meanY(){
		return line[1][1];
	}

	// euclidean calculation
	public double width(){
		return Math.sqrt( Math.pow((offsetX() - meanX()),2) + Math.pow((offsetY() - meanY()),2) );
	}

	// print detailes
	public void output(){
		System.out.println(Util.colorText("LINEAR REGRESSION"	, "magenta"));
		System.out.println(Util.colorText("Offset:"				, "blue") + "		X= " + String.format("%.5f",offsetX()) + "	|	Y= " + String.format("%.5f",offsetY()));
		System.out.println(Util.colorText("Mean:"				, "blue") + "		X= " + String.format("%.5f",meanX())   + "	|	Y= " + String.format("%.5f",meanY()));
		System.out.println(Util.colorText("Line Width:	"		, "blue") + width());
	}
}