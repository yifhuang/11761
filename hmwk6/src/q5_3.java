import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class q5_3 {
	
	void solve() throws IOException{
		Dt tree = new Dt("data/hw6-WSJ-1.tags.txt", "data/hw6-WSJ-2.tags.txt");
		tree.getTrainPairs();
		tree.getTestPairs();
		tree.createQs();
		tree.evaluateQs();
		ArrayList<Integer> qi = new ArrayList<Integer>();
		for (int i = 0; i < tree.qList.size(); i++) {
			qi.add(i);
		}
		ArrayList<TrainPair> yes = new ArrayList<TrainPair>();
		ArrayList<TrainPair> no = new ArrayList<TrainPair>();
		tree.getMaxI(qi,(ArrayList)tree.trainPairs, yes, no);
		
		ArrayList<QandI> is = (ArrayList<QandI>) tree.getLeveloneI(yes,qi);
		this.evaluate(is, tree);
		System.out.println("----------------------------------------------------------");
		
		is = (ArrayList<QandI>) tree.getLeveloneI(no,qi);
		this.evaluate(is, tree);
		
		
	}
	void evaluate(ArrayList<QandI> is,Dt tree){
		Collections.sort(is, new Comparator<QandI>() {
			public int compare(QandI a, QandI b) {
				Double A = a.i;
				Double B = b.i;
				return A.compareTo(B);
			}
		});
		System.out.println("bot:------------------");
		for (int i = 0; i < 100; i++) {
			System.out.println(tree.qList.get(is.get(i).q).desc() + " "
					+ is.get(i).i);
		}
		System.out.println("top:------------------");
		for (int i = 0; i < 100; i++) {
			System.out.println(tree.qList.get(is.get(is.size() - i - 1).q)
					.desc() + " " + is.get(is.size() - i - 1).i);
		}
		double[] typeMeanI = new double[8];
		double[] typeCount = new double[8];
		int [] typeMap = new int[26];
		typeMap[1] = 0;typeMap[3] = 1;typeMap[4]=2;
		typeMap[21] = 3; typeMap[22] = 4;typeMap[23] =5;
		typeMap[24] = 6; typeMap[25] = 7;
		double total = 0;
		for (int i = 0; i < 4; i++) {
			typeMeanI[i] = 0;
			typeCount[i] = 0;
		}
		for (QandI ai : is) {
			total+=ai.i;
			typeMeanI[typeMap[tree.qList.get(ai.q).type]] += ai.i;
			typeCount[typeMap[tree.qList.get(ai.q).type]]++;

		}
		for(int i=0;i<8;i++){
			System.out.println("Cat "+i+" question:"+1.0*typeMeanI[i]/typeCount[i]);
		}
		System.out.println("All avg: "+total/is.size());
		
	}
}
