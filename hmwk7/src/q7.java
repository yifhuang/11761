import java.io.IOException;

public class q7 {
	void solve() throws IOException {
		String trainData = Utils.filteInFile("hmm-train.txt");
		int loops = 1000;
		double delta = 0.00001;
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
		
		for(int t=0;t<1;t++){
			//Utils.createParameters(lamdas, intial, bs, end);
			//System.out.println(intial[0]);
			Double alphas[][] = Utils.alphaGen(factorsA,numOfFactorA, factor, trainData.toCharArray(), lamdas, bs, intial, end, numSy, numS, numT);
			Double betas[][] = Utils.betaGen(factorsB,numOfFactorB, factor, trainData.toCharArray(), lamdas, bs, intial, end, numSy, numS, numT);
			//System.out.println(numOfFactor[0]);
			logll=0;
			for(int i=0;i<numS;i++){
				logll+=alphas[i][numT-1];
			}
			double current = (Math.log10(logll)-1.0*numOfFactorA[0]*Math.log10(factor));
			//System.out.println(Math.log(logll)-1.0*numOfFactorA[0]*Math.log(factor));
			
			for(int l=0;l<loops;l++){
				Utils.EM(factorsA,factorsB,alphas,betas,trainData.toCharArray(),lamdas,bs, intial, end, numSy, numS,
						numT,factor);
				alphas= Utils.alphaGen(factorsA,numOfFactorA, factor, trainData.toCharArray(), lamdas, bs, intial, end, numSy, numS, numT);
				betas = Utils.betaGen(factorsB,numOfFactorB, factor, trainData.toCharArray(), lamdas, bs, intial, end, numSy, numS, numT);
				//System.out.println(Math.log(logll)-1.0*numOfFactorA[0]*Math.log(factor));
				logll=0;
				for(int i=0;i<numS;i++){
					logll+=alphas[i][numT-1];
				}
				double now = 1.0/numT*(Math.log(logll)-1.0*numOfFactorA[0]*Math.log(factor))/Math.log(2);
				System.out.println("After "+(l+1)+"th's iteration "+now);
				double gap = now-current;
				current = now;
				//System.out.println(gap);
				if(gap<delta){
					System.out.println(current);
					break;
				}
			}		
		}
		Utils.printMat(lamdas);
		Utils.printMat(bs);
		Utils.printMat(intial);
		
	}
}
