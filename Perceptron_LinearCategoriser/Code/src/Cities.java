import java.io.*;
import java.util.*;


public class Cities{

	private ArrayList<ArrayList<String>> cities = new ArrayList<>();
	private final double closestDistance;
	private final int dimensions;
	private double longestAxis;
	private boolean found;


	// constructor
	public Cities(String fileName){
		found			= fileReader(fileName);
		dimensions		= found? cities.get(0).size()-1: 0;
		closestDistance	= found? closestDistance(): 0;
		if(found) getArea();
	}



	//txt file reader
	private boolean fileReader(String fileName){
        Scanner scann = null;
        String delimiter = ",";

        try{
            scann = new Scanner(new File(fileName));
        }catch (IOException e){
            System.out.println("No file found!");
			return false;
        }
        
        while(scann.hasNextLine()){
            String line = scann.nextLine();
			ArrayList<String> tempRow = new ArrayList<>();
			ArrayList<String> row = new ArrayList<>(Arrays.asList(line.split(delimiter)));

			if(row.size()>2){
				int num = 0, notNum = 0;
				for(String s: row){
					if(Util.isNumeric(s)){
						tempRow.add(s);
						num++;
					}else if(notNum == 0){
						tempRow.add(0, s);
						notNum++;
					}
				}
				if(num>1 && tempRow.size()>2) cities.add(tempRow);
			}
        }

        scann.close();
		return true;
    }

	// calculating the smaller distance
	public double closestDistance(){
		double smallest = 0;

		ArrayList<ArrayList<ArrayList<String>>> groups = new ArrayList<>();
		ArrayList<ArrayList<String>> team = new ArrayList<>();
		String currentTeam = "";

		// splitting in groups by category
		for(int i=0; i<getSize(); i++){
			if(i != 0 && !currentTeam.equals(getName(i)) || i == getSize()-1){
				if(i == getSize()-1)	team.add(new ArrayList<String>(cities.get(i)));
				groups.add(new ArrayList<ArrayList<String>>(team));
				team.clear();
			}
			team.add(new ArrayList<String>(cities.get(i)));
			currentTeam = getName(i);
		}

		double[] groupsClosestDitance = new double[groups.size()-1*(groups.size()) / 2];

		// checking smallest distance between groups
		for(int group1=0; group1<groups.size()-1; group1++){
			for(int group2=group1+1; group2<groups.size(); group2++){

				double smallerDistance = 0;
				for(int point1=0; point1<groups.get(group1).size(); point1++){
					for(int point2=0; point2<groups.get(group2).size(); point2++){

						double sum = 0;
						for(int d=1; d<=getDimensions(); d++){
							sum += Math.pow(Double.parseDouble(groups.get(group1).get(point1).get(d)) - Double.parseDouble(groups.get(group2).get(point2).get(d)),2);
						}
						if(point1 == 0 && point2 == 0)	smallerDistance = Math.sqrt(sum);
						else	smallerDistance = smallerDistance < Math.sqrt(sum)? smallerDistance: Math.sqrt(sum);
					}
				}
				if(group1 == 0 && group2 <= 1) groupsClosestDitance[group1]  = smallerDistance;
				else groupsClosestDitance[group1]  = groupsClosestDitance[group1] < smallerDistance? groupsClosestDitance[group1]: smallerDistance;
			}
			if(group1 == 0)	smallest = groupsClosestDitance[group1];
			else smallest  = smallest < groupsClosestDitance[group1]? smallest: groupsClosestDitance[group1];
		}
		return smallest;
	}



	//.....	GETTER METHODs.....//

	// cityName getter
	public String getName(int index){
		return cities.get(index).get(0);
	}

	// X coordinate getter
	public double getVal(int index, int axis){
		index = index<0? 0: index;
		axis = axis<0? 0: axis;
		return Double.parseDouble(cities.get(index).get(axis+1));
	}

	// getting size
	public int getSize(){
		return cities.size();
	}

	// getting array axis
	public double[] getAxis(int axis){
		axis = axis<0? 0: axis;
		double axisArray[] = new double[getSize()];
		for(int i=0; i<getSize(); i++) axisArray[i] = Double.parseDouble(cities.get(i).get(axis+1));
		return axisArray;
	}

	// getting list of names
	public String[] getNameList(){
		String[] NamesList = new String[getSize()];
		for(int i=0; i<getSize(); i++)	NamesList[i] = cities.get(i).get(0);
		return NamesList;
	}

	// get state
	public boolean found(){
		return found;
	}

	// getting numbur of dimensions
	public int getDimensions(){
		return dimensions;
	}

	// getting greater axis
	public double getLongestAxis(){
		return longestAxis;
	}

	// getting area
	public double getArea(){
		double area = 0;

		for(int j=0; j<getDimensions(); j++){
			double min = 0, max = 0;
			for(int i=0; i<getSize(); i++){
				min = i==0? getVal(i,j): min>getVal(i,j)? getVal(i,j): min;
				max = i==0? getVal(i,j): max<getVal(i,j)? getVal(i,j): max;
			}
			longestAxis = longestAxis > (max - min)? longestAxis: (max - min);
			area += (max - min) * 2;
		}
		return area;
	}

	// getting the shortest distance
	public double getClosestDistance(){
		return closestDistance;
	}
	
	// dereferencing the arrayList
	public ArrayList<ArrayList<String>> getCities(){
		ArrayList<ArrayList<String>> newCities = new ArrayList<>();
		for(ArrayList<String> d: cities){
			ArrayList<String> tmp = new ArrayList<>();
			for(String s: d) tmp.add(s);
			newCities.add(tmp);
		}
		return newCities;
	}
}