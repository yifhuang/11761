import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class q1 {
	HashMap<String, Double> unigram = null;
	String trainPath = null;
	String testPath = null;
	double avgLogTrain = 0;
	double avgLogTest = 0;;
	double ptrain;
	double ptest;

	q1(String trainPath, String testPath) throws FileNotFoundException {
		unigram = new HashMap<String, Double>();
		this.trainPath = trainPath;
		this.testPath = testPath;
	}
	void solve() throws IOException{
		this.getUnigram();
		this.calculateAvgLog();
	}
	void getUnigram() throws IOException {
		int totalCount = 0;
		BufferedReader br = new BufferedReader(new FileReader(trainPath));
		String oneRow = null;

		while ((oneRow = br.readLine()) != null) {
			String[] posList = oneRow.split(" ");
			totalCount += posList.length;
			for (String pos : posList) {
				if (unigram.containsKey(pos)) {
					unigram.put(pos, unigram.get(pos) + 1);
				} else {
					unigram.put(pos, 1.0);
				}
			}
		}
		br.close();
		Set<String> keys = this.unigram.keySet();
		for (String key : keys) {
			unigram.put(key, unigram.get(key) / totalCount);
		}
		

	}

	void calculateAvgLog() throws IOException  {
		BufferedReader br = new BufferedReader(new FileReader(trainPath));
		String oneRow = null;
		double count =0;
		while ((oneRow = br.readLine()) != null) {
			String[] posList = oneRow.split(" ");
			count+=posList.length;
			for (String pos : posList) {
				avgLogTrain+=Math.log(unigram.get(pos))/Math.log(2);
			}
		}
		br.close();
		this.avgLogTrain = this.avgLogTrain/count;
		count = 0;
		br = new BufferedReader(new FileReader(testPath));
		while ((oneRow = br.readLine()) != null) {
			String[] posList = oneRow.split(" ");
			count+=posList.length;
			for (String pos : posList) {
				avgLogTest+=Math.log(unigram.get(pos))/Math.log(2);
			}
		}
		br.close();
		this.avgLogTest = this.avgLogTest/count;
		this.ptest = Math.pow(2,-avgLogTest);
		this.ptrain = Math.pow(2,-avgLogTrain);
	}
}
