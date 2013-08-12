import java.awt.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class go {

	/**
	 * @param args
	 */
	public class CustomComparator implements Comparator<QandI> {
		@Override
		public int compare(QandI o1, QandI o2) {
			Double a = o1.i;
			Double b = o2.i;
			return a.compareTo(b);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * try { q1 Q1 = new q1("hw6-WSJ-1.tags.txt","hw6-WSJ-2.tags.txt");
		 * Q1.solve(); System.out.println("avgLogTest: "+Q1.avgLogTest);
		 * System.out.println("avgLogTrain: "+Q1.avgLogTrain);
		 * System.out.println("ptest "+Q1.ptest);
		 * System.out.println("ptrain "+Q1.ptrain); } catch (Exception e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); };
		 */
		try {
			long start = System.currentTimeMillis();
			q5_4 qq3 = new q5_4();
			qq3.solve();

			 
			long end = System.currentTimeMillis();
			System.out.println("runtime: " + 1.0 * (end - start) / 1000 + "s");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
		/*
		 * dt tree = new dt(); tree.createQs();
		 */
	}

}
