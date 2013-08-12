import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class q4 {
	void solve() throws IOException{
		Dt tree = new Dt("data/hw6-WSJ-1.tags.txt", "data/hw6-WSJ-2.tags.txt");
		tree.getTrainPairs();
		tree.getTestPairs();
		// tree.getTestPairs();
		tree.createQs();
		tree.evaluateQs();
		 tree.buildTree(2);
		 System.out.println("avgTrainLog: "+tree.getTrainAvgLog());
		 System.out.println("avgTestLog: "+tree.getTestAvgLog());
	}
}
