
public class test {
	void run(){
		String trainData = "AAB";
		/*for(int i=0;i<10;i++){
			trainData+="AB";
		}*/
		int loops = 10;
		int numS = 2;
		int numSy =2;
		int numT = trainData.length();
		Integer[] numOfFactor = new Integer[1];
		numOfFactor[0]=0;
		double factor = 1;
		double logll;
		Double[][] lamdas = new Double[numS][numS];
		Double[] intial = new Double[numS];
		Double[][] bs = new Double[numS][numSy];
		Double[] end = new Double[numS];
		lamdas[0][0] = 0.6;
		lamdas[0][1] = 0.4;
	    lamdas[1][0] = 0.0;
		lamdas[1][1] = 1.0;
		bs[0][0] = 0.8;
		bs[0][1] = 0.2;
		bs[1][0] = 0.3;
		bs[1][1] = 0.7;
		intial[0] = 0.6;
		intial[1] =0.4;
		end[0] = 1.0;
		end[1] =1.0;
		Integer[] numOfFactorA = new Integer[1];
		Integer[] numOfFactorB = new Integer[1];
		Integer[] factorsB = new Integer[numT];
		Integer[] factorsA = new Integer[numT];
		Double alphas[][] = Utils.alphaGen(factorsA,numOfFactorA, factor, trainData.toCharArray(), lamdas, bs, intial, end, numSy, numS, numT);
		Double betas[][] = Utils.betaGen(factorsB,numOfFactorB, factor, trainData.toCharArray(), lamdas, bs, intial, end, numSy, numS, numT);
		System.out.println(alphas[0][numT-1]+alphas[1][numT-1]);
		System.out.println(numOfFactorA[0] +" alpha: "+(Math.log10(alphas[0][numT-1]+alphas[1][numT-1])-1.0*numOfFactorA[0]*Math.log10(factor)));
		for(int t=0;t<loops;t++){
			Utils.EM(factorsA,factorsB,alphas,betas,trainData.toCharArray(),lamdas,bs, intial, end, numSy, numS,
					numT,factor);
			//System.out.println(lamdas[0][0]+" "+lamdas[0][1]);
			alphas = Utils.alphaGen(factorsA,numOfFactorA, factor, trainData.toCharArray(), lamdas, bs, intial, end, numSy, numS, numT);
			betas = Utils.betaGen(factorsB,numOfFactorB, factor, trainData.toCharArray(), lamdas, bs, intial, end, numSy, numS, numT);
			
			System.out.println(numOfFactorA[0] +" alpha: "+(Math.log(alphas[0][numT-1]+alphas[1][numT-1])-1.0*numOfFactorA[0]*Math.log(factor)));
		}
		
		
	}
}
