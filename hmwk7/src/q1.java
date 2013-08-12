import java.io.IOException;


public class q1 {
	void solve() throws IOException{
		String trainData = Utils.filteInFile("hmm-train.txt");
		int loops = 10000;
		int numS = 2;
		int numSy =27;
		int numT = trainData.length();
		Integer[] numOfFactor = new Integer[1];
		numOfFactor[0]=0;
		double factor = 5;
		double logll;
		Double[][] lamdas = new Double[numS][numS];
		Double[] intial = new Double[numS];
		Double[][] bs = new Double[numS][numSy];
		Double[] end = new Double[numS];
		Utils.createMyParameters(trainData,lamdas, intial, bs, end);
		Utils.printMat(lamdas);
		Utils.printMat(bs);
		Utils.printMat(intial);
	}
}
