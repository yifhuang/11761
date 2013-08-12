import java.io.IOException;

public class q5 {
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
		Double[][] maxLamdas = null;
		Double[] maxIntial = null;
		Double[][] maxBs = null;
		double maxLogLL = -Double.MAX_VALUE;
		//Utils.createMyParameters(trainData,lamdas, intial, bs, end);
		
		for(int t=0;t<10;t++){
			Utils.createParameters(lamdas, intial, bs, end);
			Double alphas[][] = Utils.alphaGen(factorsA,numOfFactorA, factor, trainData.toCharArray(), lamdas, bs, intial, end, numSy, numS, numT);
			Double betas[][] = Utils.betaGen(factorsB,numOfFactorB, factor, trainData.toCharArray(), lamdas, bs, intial, end, numSy, numS, numT);
			logll=0;
			for(int i=0;i<numS;i++){
				logll+=alphas[i][numT-1];
			}
			double current = (Math.log10(logll)-1.0*numOfFactorA[0]*Math.log10(factor));			
			for(int l=0;l<loops;l++){
				Utils.EM(factorsA,factorsB,alphas,betas,trainData.toCharArray(),lamdas,bs, intial, end, numSy, numS,
						numT,factor);
				alphas= Utils.alphaGen(factorsA,numOfFactorA, factor, trainData.toCharArray(), lamdas, bs, intial, end, numSy, numS, numT);
				betas = Utils.betaGen(factorsB,numOfFactorB, factor, trainData.toCharArray(), lamdas, bs, intial, end, numSy, numS, numT);
				logll=0;
				for(int i=0;i<numS;i++){
					logll+=alphas[i][numT-1];
				}
				double now = 1.0/numT*(Math.log(logll)-1.0*numOfFactorA[0]*Math.log(factor))/Math.log(2);
				//System.out.println("After "+(l+1)+"th's iteration "+now);
				double gap = now-current;
				current = now;
				if(gap<delta){
					System.out.println(current);
					if(maxLogLL<current){
						maxLogLL = current;
						maxLamdas = lamdas.clone();
						maxIntial = intial.clone();
						maxBs = bs.clone();
						
					}
					break;
				}
			}		
		}
		System.out.println("max: "+maxLogLL);
		Utils.printMat(maxLamdas);
		Utils.printMat(maxIntial);
		Utils.printMat(maxBs);
		
	}
}
