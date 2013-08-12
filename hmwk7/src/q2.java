import java.io.IOException;


public class q2 {
	void solve() throws IOException{
		String trainData = Utils.filteInFile("hmm-train.txt");
		int loops = 10;
		int numS = 2;
		int numSy =27;
		int numT = trainData.length();
		Integer[] numOfFactorA = new Integer[1];
		Integer[] numOfFactorB = new Integer[1];
		double factor = 100000;
		double logll;
		Double[][] lamdas = new Double[numS][numS];
		Double[] intial = new Double[numS];
		Double[][] bs = new Double[numS][numSy];
		Double[] end = new Double[numS];
		Integer[] factorsB = new Integer[numT];
		Integer[] factorsA = new Integer[numT];
		Utils.createMyParameters(trainData,lamdas, intial, bs, end);
		Double alphas[][] = Utils.alphaGen(factorsB,numOfFactorA, factor, trainData.toCharArray(), lamdas, bs, intial, end, numSy, numS, numT);
		Double betas[][] = Utils.betaGen(factorsA,numOfFactorB, factor, trainData.toCharArray(), lamdas, bs, intial, end, numSy, numS, numT);
		System.out.println();
		logll=0;
		int num = trainData.toCharArray()[0] - 'A';
		if (num < 0 || num > 25)
			num = 26;
		for(int i=0;i<numS;i++){
			logll+=alphas[i][numT-1];
		}
		System.out.println(numOfFactorA[0] +" alpha: "+1.0/numT*(Math.log(logll)-1.0*numOfFactorA[0]*Math.log(factor))/Math.log(2));
		logll=0;
		for(int i=0;i<numS;i++){
			logll+=betas[i][0]*intial[i]*bs[i][num];
		}
		System.out.println(numOfFactorB[0]+" beta: "+1.0/numT*(Math.log(logll)-1.0*(numOfFactorB[0])*Math.log(factor))/Math.log(2));
	}
}
