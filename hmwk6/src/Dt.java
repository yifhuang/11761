import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Dt {
	String trainPath = null;
	String testPath = null;
	List<TrainPair> trainPairs;
	List<TrainPair> testPairs;
	List<Question> qList;
	double trainAvgLog = 1;
	double testAvgLog = 1;
	Node root;

	Dt(String train, String test) {
		this.trainPath = train;
		this.testPath = test;
	}

	double getTrainAvgLog() {
		if (this.trainAvgLog != 1)
			return this.trainAvgLog;
		double avgLog = 0;
		for (TrainPair t : trainPairs) {
			avgLog += 1.0 * Math.log(this.getP(t)) / Math.log(2);
		}
		this.trainAvgLog = 1.0 * avgLog / trainPairs.size();
		System.out.println(trainPairs.size());
		return this.trainAvgLog;
	}

	double getTestAvgLog() {
		if (this.testAvgLog != 1)
			return this.testAvgLog;
		double avgLog = 0;
		for (TrainPair t : testPairs) {
			avgLog += 1.0 * Math.log(this.getP(t)) / Math.log(2);
		}
		this.testAvgLog = 1.0 * avgLog / testPairs.size();
		System.out.println(testPairs.size());
		return this.testAvgLog;
	}

	void buildTree(int level) {
		this.root = new Node();
		this.root.trainPairs = (ArrayList<TrainPair>) this.trainPairs;
		ArrayList<Integer> qi = new ArrayList<Integer>();
		for (int i = 0; i < qList.size(); i++) {
			qi.add(i);
		}
		buildTree(root, level, qi);
	}

	void buildTree(Node node, int level, ArrayList<Integer> qi) {
		if (level == 1 || node.trainPairs.size() < 100) {
			node.getDistribution();
			return;
		}
		ArrayList<TrainPair> yes = new ArrayList<TrainPair>();
		ArrayList<TrainPair> no = new ArrayList<TrainPair>();
		System.out.println("Current node:" + level + "to the leaf");
		node.question = this.getMaxI(qi, node.trainPairs, yes, no);
		if (node.question == -1) {
			node.getDistribution();
			return;
		}
		Node yesNode = new Node();
		yesNode.trainPairs = yes;
		Node noNode = new Node();
		noNode.trainPairs = no;
		node.yesNode = yesNode;
		node.noNode = noNode;
		ArrayList<Integer> q2 = (ArrayList<Integer>) qi.clone();
		buildTree(yesNode, level - 1, qi);
		buildTree(noNode, level - 1, q2);

	}

	double getP(TrainPair t) {
		return getP(root, t);
	}

	double getP(Node node, TrainPair t) {
		if (node.distribution != null)
			return node.distribution.get(t.h[t.currentPoint]);
		if (t.answers.get(node.question))
			return getP(node.yesNode, t);
		else
			return getP(node.noNode, t);
	}

	void getTrainPairs() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(trainPath));
		List<TrainPair> trainList = new ArrayList<TrainPair>();
		String oneRow = null;
		while ((oneRow = br.readLine()) != null) {
			String[] posList = oneRow.split(" ");
			for (int i = 0; i < posList.length; i++) {
				trainList.add(new TrainPair(posList, i));
			}
		}
		br.close();
		this.trainPairs = trainList;
	}

	void getTestPairs() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(testPath));
		List<TrainPair> testList = new ArrayList<TrainPair>();
		String oneRow = null;
		while ((oneRow = br.readLine()) != null) {
			String[] posList = oneRow.split(" ");
			for (int i = 0; i < posList.length; i++) {
				testList.add(new TrainPair(posList, i));
			}
		}
		br.close();
		this.testPairs = testList;
	}

	void evaluateQs() {
		int gap = trainPairs.size() / 20;
		int c = 0;
		System.out.println("Evaluating Traindata...");
		for (TrainPair tr : trainPairs) {
			if (c % gap == 0){
				System.out.print(100 * c / trainPairs.size()  + "% ");
			}
			c++;
			for (Question q : this.qList) {
				tr.answers.add(q.isSatisfied(tr.h, tr.currentPoint));
			}
		}
		System.out.println("");
		System.out.println("Evaluating Testdata");
		c = 0;
		gap = testPairs.size() / 100;
		for (TrainPair tr : testPairs) {
			if (c % gap == 0)
				System.out.print(100 * c / testPairs.size() + " %");
			c++;
			for (Question q : this.qList) {
				tr.answers.add(q.isSatisfied(tr.h, tr.currentPoint));
			}
		}
		System.out.println("");

	}

	void createQs() {
		int maxH = 5;
		List<HashSet> sets = new ArrayList<HashSet>();
		addSubset("VB VBD VBG VBN VBP VBZ", sets);
		addSubset("WDT WP WRB", sets);
		addSubset("RB RBR", sets);
		addSubset("NN NNS NNP NNPS", sets);
		addSubset("JJ JJR JJS", sets);
		addSubset(", . ( ) :", sets);
		addSubset("( )", sets);
		addSubsets("TO RP POS PRP PRP$ MD IN EX DT CD CC", sets);
		addSubsets("VB VBD VBG VBN VBP VBZ", sets);
		addSubsets("WDT WP WRB", sets);
		addSubsets("RB RBR", sets);
		addSubsets("NN NNS NNP NNPS", sets);
		addSubsets("JJ JJR JJS", sets);
		addSubsets(", . ( ) :", sets);
		List<Question> qList = new ArrayList<Question>();
		for (HashSet s : sets) {
			qList.add(new ContainsSetQuestion(s));
			for (int i = 1; i <= maxH; i++) {
				qList.add(new SubsetQuestion(s, i));
			}
		}
		sets.clear();
		addSubset("VB VBD VBG VBN VBP VBZ", sets);
		addSubset("WDT WP WRB", sets);
		addSubset("RB RBR", sets);
		addSubset("NN NNS NNP NNPS", sets);
		addSubset("JJ JJR JJS", sets);
		addSubset(", . ( ) :", sets);
		addSubset("( )", sets);
		addSubsets("TO RP POS PRP PRP$ MD IN EX DT CD CC", sets);
		for (HashSet s1 : sets) {
			for (HashSet s2 : sets) {
				for (int i = 1; i <= 1; i++) {
					qList.add(new SubsetPos2Question(s1, s2, i));
				}
			}
		}
		sets.clear();
		addSubset("VB VBD VBG VBN VBP VBZ", sets);
		addSubset("WDT WP WRB", sets);
		addSubset("RB RBR", sets);
		addSubset("NN NNS NNP NNPS", sets);
		addSubset("JJ JJR JJS", sets);
		addSubset(", . ( ) :", sets);
		addSubset("( )", sets);
		for (HashSet s1 : sets) {
			for (HashSet s2 : sets) {
				for (int i = 1; i <= 1; i++) {
					qList.add(new Contains2SetQuestion(s1, s2));
				}
			}
		}
		
		System.out.println("inTotal " + qList.size() + " quesionts generated.");
		/*
		 * for(Question q:qList){ System.out.println(q.desc()); }
		 */

		this.qList = qList;
	}

	void addSubset(String s, List<HashSet> sets) {
		HashSet tmp = null;
		tmp = new HashSet<String>();
		String[] ss = s.split(" ");
		for (String sss : ss) {
			tmp.add(sss);
		}
		sets.add(tmp);
	}

	void addSubsets(String s, List<HashSet> sets) {
		HashSet tmp = null;

		String[] ss = s.split(" ");
		for (String sss : ss) {
			tmp = new HashSet<String>();
			tmp.add(sss);
			sets.add(tmp);
		}

	}

	List<QandI> getLeveloneI(List<TrainPair> trainPairs,ArrayList<Integer> qList) {
		HashMap<String, Integer> positive = new HashMap<String, Integer>();
		HashMap<String, Integer> negative = new HashMap<String, Integer>();
		HashMap<String, Integer> total = new HashMap<String, Integer>();
		double hT = 0;
		double hP;
		double hN;
		int countP;
		int countN;
		int currentMAX = 0;
		ArrayList<QandI> iList = new ArrayList<QandI>();
		;
		double maxI = 0;
		for (TrainPair tr : trainPairs) {
			if (total.containsKey((tr.h[tr.currentPoint]))) {
				total.put(tr.h[tr.currentPoint],
						total.get(tr.h[tr.currentPoint]) + 1);
			} else {
				total.put(tr.h[tr.currentPoint], 1);
			}
		}
		Set<String> keys = total.keySet();
		for (String key : keys) {
			hT += 1.0 * total.get(key) / trainPairs.size()
					* Math.log(1.0 * trainPairs.size() / total.get(key));
		}
		for (int i:qList) {
			System.out.println(i);
			countP = 0;
			hP = 0;
			hN = 0;
			positive.clear();
			negative.clear();
			for (TrainPair tr : trainPairs) {

				if (tr.answers.get(i)) {
					countP++;
					if (positive.containsKey((tr.h[tr.currentPoint]))) {
						positive.put(tr.h[tr.currentPoint],
								positive.get(tr.h[tr.currentPoint]) + 1);
					} else {
						positive.put(tr.h[tr.currentPoint], 1);
					}
				} else {
					if (negative.containsKey((tr.h[tr.currentPoint]))) {
						negative.put(tr.h[tr.currentPoint],
								negative.get(tr.h[tr.currentPoint]) + 1);
					} else {
						negative.put(tr.h[tr.currentPoint], 1);
					}
				}

			}
			keys = positive.keySet();
			for (String key : keys) {
				hP += 1.0 * positive.get(key) / countP
						* Math.log(1.0 * countP / positive.get(key));
			}
			countN = trainPairs.size() - countP;
			keys = negative.keySet();
			for (String key : keys) {
				hN += 1.0 * negative.get(key) / countN
						* Math.log(1.0 * countN / negative.get(key));
			}
			double I = hT - 1.0 * countP / trainPairs.size() * hP - 1.0
					* countN / trainPairs.size() * hN;
			iList.add(new QandI(i,I));
		}
		return iList;
	}

	int getMaxI(ArrayList<Integer> qi, ArrayList<TrainPair> trainPairs,
			ArrayList<TrainPair> yes, ArrayList<TrainPair> no) {
		HashMap<String, Integer> positive = new HashMap<String, Integer>();
		HashMap<String, Integer> negative = new HashMap<String, Integer>();
		HashMap<String, Integer> total = new HashMap<String, Integer>();

		double hT = 0;
		double hP;
		double hN;
		int countP;
		int countN;
		int currentMAX = 0;
		int maxIndex = 0;
		;
		double maxI = 0;
		for (TrainPair tr : trainPairs) {
			if (total.containsKey((tr.h[tr.currentPoint]))) {
				total.put(tr.h[tr.currentPoint],
						total.get(tr.h[tr.currentPoint]) + 1);
			} else {
				total.put(tr.h[tr.currentPoint], 1);
			}
		}
		Set<String> keys = total.keySet();
		for (String key : keys) {
			hT += 1.0 * total.get(key) / trainPairs.size()
					* Math.log(1.0 * trainPairs.size() / total.get(key));
		}
		int gap = qi.size() / 10;
		int c = 0;
		for (int i : qi) {
			if (c % gap == 0)
				System.out.print(1 + 100 * c / qi.size() + "% ");
			c++;
			countP = 0;
			hP = 0;
			hN = 0;
			positive.clear();
			negative.clear();
			for (TrainPair tr : trainPairs) {

				if (tr.answers.get(i)) {
					countP++;
					if (positive.containsKey((tr.h[tr.currentPoint]))) {
						positive.put(tr.h[tr.currentPoint],
								positive.get(tr.h[tr.currentPoint]) + 1);
					} else {
						positive.put(tr.h[tr.currentPoint], 1);
					}
				} else {
					if (negative.containsKey((tr.h[tr.currentPoint]))) {
						negative.put(tr.h[tr.currentPoint],
								negative.get(tr.h[tr.currentPoint]) + 1);
					} else {
						negative.put(tr.h[tr.currentPoint], 1);
					}
				}

			}
			keys = positive.keySet();
			for (String key : keys) {
				hP += 1.0 * positive.get(key) / countP
						* Math.log(1.0 * countP / positive.get(key));
			}
			countN = trainPairs.size() - countP;
			keys = negative.keySet();
			for (String key : keys) {
				hN += 1.0 * negative.get(key) / countN
						* Math.log(1.0 * countN / negative.get(key));
			}
			double I = hT - 1.0 * countP / trainPairs.size() * hP - 1.0
					* countN / trainPairs.size() * hN;
			if (I > maxI) {
				/*
				 * System.out.println(I); System.out.println("t: " + hT);
				 * System.out.println("p: " + hP); System.out.println("b: " +
				 * hN);
				 */
				// System.out.println(positive.keySet());
				maxI = I;
				currentMAX = i;
				maxIndex = c;
			}
		}

		for (TrainPair tr : trainPairs) {
			if (tr.answers.get(currentMAX)) {
				yes.add(tr);
			} else {
				no.add(tr);
			}
		}

		qi.remove(maxIndex);
		System.out.println("");
		System.out.println(this.qList.get(currentMAX).desc());
		System.out.println(maxI);
		if (maxI < 0.01)
			return -1;
		return currentMAX;
	}

}
