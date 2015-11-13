import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TsProc {

	public List<String> rawInput;
	public Boolean db2;
	public HashMap<String, ArrayList<String>> graph;
	public HashMap<String, Integer> inDegree;
	public ArrayList<String> order;
	public ArrayList<String> cyclic;

	//
	// Constructor
	// 
	// Input is a list of strings
	// 	List<String> lines = Files.readAllLines(Paths.get("input.txt"), StandardCharsets.UTF_8);
	// This parses the input into words on each line and converts it into a graph.
	//
	public TsProc(List<String> ii) {
		this.db2 = false;
		this.graph = new HashMap<String, ArrayList<String>>();	// the empty graph
		this.inDegree = new HashMap<String, Integer>();	// the number of edges each graph node has pointing at it
		this.order = new ArrayList<String>();
		this.cyclic = new ArrayList<String>();
		this.rawInput = ii;	// don't have to save input but makes it simpler to test 
		for (int i = 0; i < rawInput.size(); i++) {
			String[] words = rawInput.get(i).split("\\s+");	// Split into words

			// Assumption is that the 0th word on each line is the generated and the 1..n'th are the dependencies
			if ( words.length > 0 ) {	// Allow for empty input lines - comments in input might be nice but not implemented.
				String lib = words[0];
				ArrayList<String> tmp = this.graph.get(lib);
				if ( tmp == null ) {
					tmp = new ArrayList<String>();	// Create empty arraylist 
					this.graph.put ( lib, tmp );
				}
				for (int j = 1; j < words.length; j++ ) {
					String dep = words[j];
					Integer y = this.inDegree.get(dep);
					if ( y == null ) {
						this.inDegree.put(dep,0);
					}
					boolean found = false;

					if ( ! lib.equals(dep) ) {		// Disregard dependencies on self
						ArrayList<String> successors = this.graph.get(dep);
						if ( successors == null ) {
							found = false;
							successors = new ArrayList<String>();	// Create empty arraylist 
							this.graph.put ( dep, successors );
						} else {
							for (String succ : successors) {
								if (succ.equals(dep)) {
									found = true;
									break; 
								} 
							}
						}
						if ( ! found ) { // add it
							successors.add ( lib );
							this.graph.put ( dep, successors );
							Integer x = this.inDegree.get(lib);
							if ( x == null ) {
								this.inDegree.put(lib,1);
							} else {
								this.inDegree.put(lib,x+1);
							}
						}
					}
				}
			}
		}
	}

	// This is a fairly direct implementation from the Wikipedia
	// algorithm that you sent the link to.

	public void topologicalSort() {
		ArrayList<String> L = new ArrayList<String>();
		ArrayList<String> S = new ArrayList<String>();
		
		HashMap<String, Integer> rem = new HashMap<String, Integer>();	// Temporary Copy
		for (String key : this.inDegree.keySet() ) {
			int x = this.inDegree.get(key);

			if ( x == 0 ) {
				S.add(key);		// S is list of nodes with no incoming edges.
			} else {
				rem.put(key,x);
			}
		}

		// Loop until S is empty
		for ( int i = 0; S.size() > 0; i++ ) {
			int last = S.size() - 1;
			String n = S.get(last);

			S.remove(last);
			L.add ( n );		// Append 'n' to tail of 'L'
			ArrayList<String> mm = this.graph.get(n);
			if ( mm != null ) {
				for ( int j = 0; j < mm.size(); j++ ) {
					String m = mm.get(j);
					Integer rem_m = rem.get(m);
					if ( rem_m == null ) {
					} else if ( rem_m > 0 ) {
						rem_m = rem_m - 1;
						rem.put(m, rem_m);
						if ( rem_m == 0 ) {
							S.add(m);
						}
					}
				}
			}
		}

		// Could check for cycles at this point.  
		//		public ArrayList<String> cyclic; 
		// has not been set.

		this.order = L;
	}

	// Output resulting topological sort
	public void outputSortedResults() {
		System.out.println("");
		System.out.println("**** outputSortedResults ****");
		System.out.println("");
		ArrayList<String> t = this.order;
		for ( int i = t.size()-1; i >= 0; i-- ) {		// I prefer this in the reverse order but this matches with what you requested.
			String item = t.get(i);
			System.out.print(item);
			System.out.println("");
		}
	}

	// Dump input -have to change debug flag before it will print-
	public void dumpGraphAndInDegree() {
		if ( this.db2 ) {
			System.out.println("");
			System.out.println("**** dumpGraphAndInDegree ****");
			System.out.println("");

			System.out.println("graph:");
			for (String key : this.graph.keySet() ) {
				System.out.print("    [");
				System.out.print(key);
				System.out.print("]=");
				ArrayList<String> t = this.graph.get(key);
				for ( int i = 0; i < t.size(); i++ ) {
					String item = t.get(i);
					System.out.print(" ");
					System.out.print(item);
				}
				System.out.println("");
			}

			System.out.println("");
			System.out.println("inDegree:");
			for (String key : this.inDegree.keySet() ) {
				System.out.print("    [");
				System.out.print(key);
				System.out.print("]=");
				System.out.print(this.inDegree.get(key));
				System.out.println("");
			}

		}
	}

}
