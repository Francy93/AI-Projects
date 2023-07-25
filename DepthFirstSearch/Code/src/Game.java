import java.util.*;

public class Game {
    
	// Initializing 2d arrayList (left and right side of the river)
	private ArrayList<ArrayList<Integer>> solutionStates = new ArrayList<>(), nodesHistory = new ArrayList<>();
	private ArrayList<Integer> rootNode = new ArrayList<>();
	private boolean debugging = false, win	= false;
	private final int missionaries, cannibals, boatSeats, left = 0, right = 1;

	// Constructor
	public Game(){
		missionaries	= 3;
		cannibals		= 3;
		boatSeats		= 2;
		rootInit();
		setSolution();
	}
	public Game(int m, int c, int b){
		missionaries	= m;
		cannibals		= c;
		boatSeats		= b;
		rootInit();
		setSolution();
	}


	// initialising root node
	private void rootInit(){
		rootNode.add(missionaries);
		rootNode.add(cannibals);
		rootNode.add(left);
	}

	// detecting initial state
	private boolean isNodeRoot(int m, int c, int s){
		return isNodeRoot(new ArrayList<Integer>(Arrays.asList(m, c, s)));
	}
	private boolean isNodeRoot(ArrayList<Integer> node){
		if(node.equals(rootNode)) return true;
		else return false;
	}

	//switching river side
	private ArrayList<Integer> switchSide(int m, int c, int s){
		return switchSide(new ArrayList<Integer>(Arrays.asList(m, c, s)));
	}
	private ArrayList<Integer> switchSide(ArrayList<Integer> n){
		ArrayList<Integer> switched =  new ArrayList<>();
		
		switched.add(missionaries - n.get(0));
		switched.add(cannibals - n.get(1));
		switched.add(n.get(2)==right?left:right);
		return switched;
	}
	
	// winning state checker
	private boolean winningState(ArrayList<Integer> node){
		int result = 0;
		// checking if this node correspond to the winning state
		for(int team: node) result += team;
		return result == 0 && node.size() == 3;
	}

	// checking whether a node as been generated before
	private boolean isInHistory(ArrayList<Integer> n){
		for(ArrayList<Integer> node: nodesHistory){
			if(node.equals(n)) return true;
		}
		return false;
	}

	// (QuickSort) sorting nodes according to the riverside
	private ArrayList<ArrayList<Integer>> nodeSort(ArrayList<ArrayList<Integer>> nodes){
		return nodeSort(nodes, 0, nodes.size()-1, nodes.get(0).get(2));
	}
	private ArrayList<ArrayList<Integer>> nodeSort(ArrayList<ArrayList<Integer>> nodes, int left, int right, int side){
		int s = nodes.size();

		if(s > 0){
			left	= left	< 1? 0:	left	>= s? s-1: left;
			right	= right	< 1? 0:	right	>= s? s-1: right;
			int l	= left, r = right, way;

			// getting mid point
			int mid = (l + r) / 2;
			//getting the pivot (number of people off the boat)
			int pivot = nodes.get(mid).get(0)+nodes.get(mid).get(1);

			// partition 
			while (l <= r) {
				// loop left index if the current number of people is less than in the pivot
				do way = side==this.left? l++: r--;
				while (nodes.get(way).get(0)+nodes.get(way).get(1) < pivot);
				// loop right index if the current number of people is greater than in the pivot
				do way = side==this.left? r--: l++;
				while (nodes.get(way).get(0)+nodes.get(way).get(1) > pivot);

				if (--l <= ++r) {
					ArrayList<Integer> tmpNode = new ArrayList<>(nodes.get(l));
					nodes.set(l++, nodes.get(r));
					nodes.set(r--, tmpNode);
				}
			}

			// recursion
			if (left < r ) nodeSort(nodes, left, r, side );
			if (l < right) nodeSort(nodes, l, right, side);
		}
		return nodes;
	}

	// nodes generator
	private ArrayList<ArrayList<Integer>> nodesGen(int m0, int c0, int s0){
		ArrayList<ArrayList<Integer>> nodes = new ArrayList<>();
		int m1=m0, c1=c0, s1=s0;

		// switching river side
		if(!isNodeRoot(m0, c0, s0)){
			// valid for all the nodes except the root one
			ArrayList<Integer> newNode = switchSide(m0, c0, s0);
			m1 = newNode.get(0);
			c1 = newNode.get(1);
			s1 = newNode.get(2);
		}
		
		if(debugging)System.out.print("\r\nnon filtered nodes: [");
		// generating combinations
		for(int c3=0; c3<=c1 && c3<=boatSeats; c3++){
			for(int m3=0; m3<=m1 && m3<=boatSeats; m3++){
				
				// Considering just valid combinations
				if(m3+c3 <= boatSeats && m3+c3 > 0){
					ArrayList<Integer> node = new ArrayList<>(Arrays.asList(m1-m3, c1-c3, s1));
					if(debugging) System.out.print(node + ", ");
					int m4=node.get(0), c4=node.get(1);
					
					// filtering working nodes only
					if((m4==c4 || m4==0 || c4==0) && !isInHistory(node)){
						nodes.add(node);
					}
				}
			}
		}
		
		if(debugging) System.out.println("]\r\nfiltered nodes: " + nodes);
		return nodeSort(nodes);
	}

	// depth first search
	private ArrayList<ArrayList<Integer>> dfs(int m, int c, int s){
		ArrayList<ArrayList<Integer>> rightPath = new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(m, c, s))));
		ArrayList<ArrayList<Integer>> nodes = new ArrayList<>(nodesGen(m,c,s));
		if(debugging) System.out.println("sorted nodes: " + nodes);

		// Cycling over next nodes
		for(ArrayList<Integer>node: nodes){
			nodesHistory.add(node);
			if(debugging) System.out.println("Trying node: " + node);

			if(winningState(node)){
				if(debugging) System.out.println("Winning");
				win = true;
				rightPath.add(node);
				break;
			}else if(node.size() == 3){
				if(debugging) System.out.println("Not winning");
				ArrayList<ArrayList<Integer>> tempPath = dfs(node.get(0), node.get(1), node.get(2));
				
				if(tempPath.size()>0 && winningState(tempPath.get(tempPath.size()-1))){
					rightPath.addAll(tempPath);
					break;
				}
			}
		}
		return rightPath;
	}

	// depth first search algorithm
	private void setSolution(){
		solutionStates = dfs(missionaries, cannibals, left);
	}

	// setting the boat value as it was supposed to
	private ArrayList<Integer> boatFix(ArrayList<Integer> node, int s){
		ArrayList<Integer> side;

		if(s==left){
			side = new ArrayList<Integer>(node.get(2)==left	||  isNodeRoot(node)? node: switchSide(node));
			side.set(2, isNodeRoot(node)? right: node.get(2));
		}else{
			side = new ArrayList<Integer>(node.get(2)==right && !isNodeRoot(node)? node: switchSide(node));
			side.set(2, node.get(2)==right || isNodeRoot(node)?left:right);
		}
		return side;
	}

	// printing result
	public void output(){
		if(win){
			System.out.println("\r\nCorrect Path completed in " + solutionStates.size() + " states: ");

			// printing the graphical representation
			for(ArrayList<Integer> node: solutionStates){
				ArrayList<Integer> leftSide		= boatFix(node, left);
				ArrayList<Integer> rightSide	= boatFix(node, right);
				
				String arrowL	= leftSide.get(2) ==1? "<":"";
				String arrowR	= rightSide.get(2)==1? ">":"";
				String arrow	= arrowL+"---"+arrowR;
				
				System.out.println(leftSide + "   L	"+arrow+"	R   " + rightSide);
			}

			// printing the nodes path
			System.out.println("\r\n" + solutionStates.size() + " nodes on probation: ");
			System.out.println(solutionStates);
			System.out.println("\r\nTotal nodes inspected = " + nodesHistory.size() + "\r\n");

		}else System.out.println("No solution found!");
	}
}