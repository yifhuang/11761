import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class Node {
	Node yesNode;
	Node noNode;
	int question;
	ArrayList<TrainPair>  trainPairs;
	HashMap<String,Double> distribution;
	void getDistribution(){
		distribution = new HashMap<String,Double>();
		for (TrainPair tr : trainPairs) {
			if (distribution.containsKey((tr.h[tr.currentPoint]))) {
				distribution.put(tr.h[tr.currentPoint],
						distribution.get(tr.h[tr.currentPoint]) + 1.0);
			} else {
				distribution.put(tr.h[tr.currentPoint], 1.0);
			}
		}
		Set<String> keys = distribution.keySet();
		for (String key : keys) {
			distribution.put(key, 1.0 * distribution.get(key) / trainPairs.size());
		}
		
		
	}
}
