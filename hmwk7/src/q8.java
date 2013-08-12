import java.io.IOException;

public class q8 {
	void solve() throws IOException {
		String trainData = Utils.filteInFile("hmm-test.txt");
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
		//Utils.createMyParameters(trainData,lamdas, intial, bs, end);
		Double[] a = {0.00000250238577725080 ,0.02394769693071620000 ,
				0.03857922800442680000 ,0.07484049062393590000 ,0.00000000000113907636 
				,0.04286933401179260000 ,0.02400836557757210000 ,0.10271539335539600000 ,
				0.00000000000401631196 ,0.00162607818665420000 ,0.00657811711413534000 ,
				0.05297330519608520000 ,0.04715626741297200000 ,0.11426894860996800000 ,
				0.00000000000027015562 ,0.02114373406207730000 ,0.00236520463513337000 ,
				0.10406900373658700000 ,0.10739507230424300000 ,0.13608244293359500000 ,
				0.00147380091062472000 ,0.02091727849196060000 ,0.03954326499363600000 ,
				0.00155216554118674000 ,0.03517888527713570000 ,0.00007391264484791640 ,
				0.00063950705413966100 };
		Double[] b = {0.122670416523190000 ,0.000000000000008806 ,0.000074817790069179 ,
				0.000032047365405465 ,0.205315604240088000 ,0.000000000000000017 ,
				0.002021532594768050 ,0.007913736718927510 ,0.112199023417917000 ,
				0.000000000000000000 ,0.000000105092354061 ,0.011284357677135000 ,
				0.000000000000000000 ,0.000000000315355093 ,0.126905417156510000 ,
				0.001788883784178340 ,0.000000000000000000 ,0.000000000203125468 ,
				0.000000000640372848 ,0.001999686893647050 ,0.042043085561421900 ,
				0.000000000000000000 ,0.000000000000000005 ,0.000000000000601303 ,
				0.000003429727633667 ,0.000000000000000000 ,0.365747854297281000 };
		bs[0] = a;
		bs[1] = b;
		lamdas[0][0] =0.26016156044910000;
		lamdas[0][1] =0.73983843955092200 ;
		lamdas[1][0] =0.71802327197239700;
		lamdas[1][1] =0.28197672802758100 ;
		intial[0] = 0.0;
		intial[1] = 1.0;
		end[0] = 1.0;
		end[1] =1.0;
		
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
			double current = 1.0/numT*(Math.log(logll)-1.0*numOfFactorA[0]*Math.log(factor))/Math.log(2);;
			//System.out.println(Math.log(logll)-1.0*numOfFactorA[0]*Math.log(factor));
			System.out.println(current);
		}
		
	}
}
