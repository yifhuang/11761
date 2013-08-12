import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Utils {

	public static String filteInFile(String trainPath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(trainPath));
		String oneLine = br.readLine();
		String all = "";
		while (oneLine != null) {
			oneLine = oneLine.replaceAll("[^a-zA-Z ]", " ");
			// System.out.println(oneLine);
			oneLine = oneLine.replaceAll("[ ]+", " ");
			// System.out.println(oneLine);
			oneLine = oneLine.toUpperCase();
			// System.out.println(oneLine);
			all += oneLine + " ";
			oneLine = br.readLine();
		}
		all = all.replaceAll("[ ]+", " ");
		return all.substring(0, all.length() - 1);
	}

	public static Double[][] alphaGen(Integer[] factorsA,
			Integer[] numOfFactor, double factor, char[] trainData,
			Double[][] lamdas, Double[][] bs, Double[] intial, Double[] end,
			int numSy, int numS, int numT) {
		numOfFactor[0] = 0;
		// System.out.println(numS);
		Double[][] alphas = new Double[numS][numT];
		Integer[][] alphaFactors = new Integer[numS][numT];
		factorsA[0] = 0;
		for (int i = 0; i < numS; i++) {
			int num = trainData[0] - 'A';
			if (num < 0 || num > 25)
				num = 26;
			// System.out.println(num+" "+i);
			alphas[i][0] = intial[i] * bs[i][num];
		}
		for (int i = 1; i < numT; i++) {
			// System.out.println(alphas[0][i-1]+" "+alphas[1][i-1]);
			int symbol = trainData[i] - 'A';
			if (symbol < 0 || symbol > 25)
				symbol = 26;
			boolean needToFactor = false;
			for (int ii = 0; ii < numS; ii++) {
				alphas[ii][i] = 0.0;
				for (int iii = 0; iii < numS; iii++) {
					// System.out.println(alphas[iii][i-1]+"*"+lamdas[iii][ii]+"*"+bs[ii][symbol]);
					alphas[ii][i] += alphas[iii][i - 1] * lamdas[iii][ii]
							* bs[ii][symbol];
				}
				if (alphas[ii][i] < 1 / factor && alphas[ii][i] > 0)
					needToFactor = true;
			}
			if (needToFactor) {
				for (int ii = 0; ii < numS; ii++) {
					alphas[ii][i] *= factor;
				}
				numOfFactor[0]++;
			}
			factorsA[i] = numOfFactor[0];
		}
		return alphas;
	}

	public static Double[][] betaGen(Integer[] factorsB, Integer[] numOfFactor,
			double factor, char[] trainData, Double[][] lamdas, Double[][] bs,
			Double[] intial, Double[] end, int numSy, int numS, int numT) {
		numOfFactor[0] = 0;
		factorsB[numT - 1] = 0;
		Double[][] beta = new Double[numS][numT];
		for (int i = 0; i < numS; i++) {
			int num = trainData[0] - 'A';
			if (num < 0 || num > 25)
				num = 26;
			// System.out.println(i);
			beta[i][numT - 1] = end[i];
		}
		for (int i = numT - 2; i >= 0; i--) {
			// System.out.println(beta[0][i+1]+" "+beta[1][i+1]);
			int symbol = trainData[i + 1] - 'A';
			if (symbol < 0 || symbol > 25)
				symbol = 26;
			boolean needToFactor = false;
			for (int ii = 0; ii < numS; ii++) {
				beta[ii][i] = 0.0;
				for (int iii = 0; iii < numS; iii++) {
					// System.out.println(beta[iii][i+1]+"x"+lamdas[ii][iii]
					// +"x"+bs[iii][symbol]);
					beta[ii][i] += beta[iii][i + 1] * lamdas[ii][iii]
							* bs[iii][symbol];
				}
				if (beta[ii][i] < 1 / factor && beta[ii][i] > 0)
					needToFactor = true;
			}
			if (needToFactor) {
				for (int ii = 0; ii < numS; ii++) {
					beta[ii][i] *= factor;
				}
				numOfFactor[0]++;

			}
			factorsB[i] = numOfFactor[0];

		}
		return beta;
	}

	public static Double[][] EM(Integer[] factorsA, Integer[] factorsB,
			Double[][] alphas, Double[][] betas, char[] trainData,
			Double[][] lamdas, Double[][] bs, Double[] intial, Double[] end,
			int numSy, int numS, int numT, double factor) {
		Double[][][] z = new Double[numS][numS][numT - 1];
		int factConst = factorsA[0] + factorsB[0];
		for (int t = 0; t < numT - 1; t++) {
			int symbol = trainData[t + 1] - 'A';
			if (symbol < 0 || symbol > 25)
				symbol = 26;

			for (int i = 0; i < numS; i++) {
				for (int j = 0; j < numS; j++) {
					int con = -factorsA[t] - factorsB[t + 1] + factConst;
					z[i][j][t] = alphas[i][t] * lamdas[i][j] * bs[j][symbol]
							* betas[j][t + 1] * Math.pow(factor, con);
					// System.out.println(i+" "+j+" "+t+ " "+z[i][j][t]);
				}
			}
		}
		Double r[][] = new Double[numS][numT];
		for (int t = 0; t < numT; t++) {
			for (int i = 0; i < numS; i++) {
				int con = -factorsA[t] - factorsB[t] + factConst;
				r[i][t] = alphas[i][t] * betas[i][t] * Math.pow(factor, con);
				// System.out.println(factorsA[t]+" "+factorsB[t]+" r:"+i+" "+t+" "+r[i][t]);
			}
		}
		Double r2[][] = new Double[numS][numT - 1];
		for (int t = 0; t < numT - 1; t++) {
			for (int i = 0; i < numS; i++) {
				r2[i][t] = 0.0;
				for (int j = 0; j < numS; j++) {
					r2[i][t] += z[i][j][t];
				}

			}
		}
		double avgll = 0;
		for (int i = 0; i < numS; i++) {
			avgll += r[i][0];
		}
		for (int i = 0; i < numS; i++) {
			intial[i] = r[i][0] / avgll;
		}
		for (int i = 0; i < numS; i++) {
			for (int j = 0; j < numS; j++) {
				double up = 0;
				double bot = 0;
				for (int t = 0; t < numT - 1; t++) {
					up += z[i][j][t];
					bot += r2[i][t];
				}
				lamdas[i][j] = up / bot;
				// System.out.println(i+" "+j+" "+lamdas[i][j]);
			}
		}
		for (int i = 0; i < numS; i++) {
			for (int j = 0; j < numSy; j++) {
				double up = 0;
				double bot = 0;
				for (int t = 0; t < numT; t++) {
					int symbol = trainData[t] - 'A';
					if (symbol < 0 || symbol > 25)
						symbol = 26;
					if (symbol == j)
						up += r[i][t];
					bot += r[i][t];
				}
				bs[i][j] = up / bot;
				// System.out.println(i+" "+j+" "+bs[i][j]);
			}
		}

		return null;
	}

	public static void createParameters(Double[][] lamdas, Double[] intial,
			Double[][] bs, Double[] end) {
		createRandomMatrix(lamdas, lamdas[0].length, lamdas.length);
		createRandomMatrix(intial, intial.length);
		createRandomMatrix(bs, bs.length, bs[0].length);
		end[0] = 1.0;
		end[1] = 1.0;
		// -----

	}

	public static void createRandomMatrix(Double[][] m, int row, int col) {
		for (int i = 0; i < row; i++) {
			double sum = 0;
			for (int j = 0; j < col; j++) {
				m[i][j] = Math.random() * 10;
				sum += m[i][j];
			}
			for (int j = 0; j < col; j++) {
				m[i][j] /= sum;
			}
		}
	}

	public static void createRandomMatrix(Double[] m, int row) {

		double sum = 0;
		for (int j = 0; j < row; j++) {
			m[j] = Math.random() * 10;
			sum += m[j];
		}
		for (int j = 0; j < row; j++) {
			m[j] /= sum;
		}
	}

	public static void createMyParameters(String trainData, Double[][] lamdas,
			Double[] intial, Double[][] bs, Double[] end) {
		String oneClass = "AEIOU ";
		int s1 = 0, s11 = 0, s21 = 0, s2=0, s12, s22;
		int space = 0;
		for (int i = 0; i < trainData.length() - 1; i++) {
			// System.out.println(trainData.substring(i, i+1));
			if (oneClass.contains(trainData.substring(i, i + 1))) {
				s1++;
				if (oneClass.contains(trainData.substring(i + 1, i + 2))) {
					s11++;
				}
			} else{
				s2++;
				if (oneClass.contains(trainData.substring(i + 1, i + 2))) {
					s21++;
				}
			}
		}
		
		s2 = trainData.length() - 1 - s1;
		s12 = s1 - s11;
		s22 = s2 - s21;
		lamdas[0][0] = 1.0 * s11 / s1;
		lamdas[0][1] = 1.0 * s12 / s1;
		lamdas[1][0] = 1.0 * s21 / s2;
		lamdas[1][1] = 1.0 * s22 / s2;
		for (int i = 0; i < bs.length; i++) {
			for (int j = 0; j < bs[0].length; j++) {
				bs[i][j] = 0.0;
			}
		}
		int[] st = new int[2];
		st[0] = 0;
		st[1] = 0;
		for (char s : trainData.toCharArray()) {
			int num = s - 'A';
			if (num < 0 || num > 25){
				num = 26;
				space++;
			}
			if (oneClass.contains(String.valueOf(s))) {
				bs[0][num]++;
				st[0]++;
			} else {
				bs[1][num]++;
				st[1]++;
			}
		}
		double[] tmp = new double[2];
		tmp[0]=0;
		tmp[1]=0;
		for(int i=0;i<25;i++){
			tmp[0]+=bs[0][i];
			tmp[1]+=bs[1][i];
		}
		tmp[0]/=5;
		tmp[1]/=20;
		System.out.println(tmp[0]+" "+tmp[1]);
		for (int i = 0; i < bs.length; i++) {
			for (int j = 0; j < bs[0].length; j++) {
				bs[i][j] = (1.0 * bs[i][j]+1) / (st[i]+27);
			}
		}
		

		// createRandomMatrix(bs,bs.length,bs[0].length);
		intial[0] = 0.5;
		intial[1] = 0.5;
		end[0] = 1.0;
		end[1] = 1.0;
		//bs[0][26] = 1.0*space/trainData.length();
		//bs[1][26] = 1.0*space/trainData.length();
	}

	

	static void printMat(Double[][] m) {
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				System.out.print(m[i][j] + " ");
			}
			System.out.println();
		}
	}

	static void printMat(Double[] m) {
		for (int i = 0; i < m.length; i++) {
			System.out.println(m[i]);
		}
	}
	
	public static String vertabi(Double[] logll, Integer[] factorsA,
			Integer[] numOfFactor, double factor, char[] trainData,
			Double[][] lamdas, Double[][] bs, Double[] intial, Double[] end,
			int numSy, int numS, int numT) {
		numOfFactor[0] = 0;
		// System.out.println(numS);
		Integer [][] record = new Integer[numS][numT];
		Double[][] alphas = new Double[numS][numT];
		Integer[][] alphaFactors = new Integer[numS][numT];
		factorsA[0] = 0;
		for (int i = 0; i < numS; i++) {
			int num = trainData[0] - 'A';
			if (num < 0 || num > 25)
				num = 26;
			// System.out.println(num+" "+i);
			alphas[i][0] = intial[i] * bs[i][num];
		}
		for (int i = 1; i < numT; i++) {
			// System.out.println(alphas[0][i-1]+" "+alphas[1][i-1]);
			int symbol = trainData[i] - 'A';
			if (symbol < 0 || symbol > 25)
				symbol = 26;
			boolean needToFactor = false;
			for (int ii = 0; ii < numS; ii++) {
				alphas[ii][i] = 0.0;
				for (int iii = 0; iii < numS; iii++) {
					// System.out.println(alphas[iii][i-1]+"*"+lamdas[iii][ii]+"*"+bs[ii][symbol]);
					double tmp = alphas[iii][i - 1] * lamdas[iii][ii]
							* bs[ii][symbol];
					if(tmp>alphas[ii][i]){
						alphas[ii][i] = tmp;
						record[ii][i] = iii;
					}
				}
				if (alphas[ii][i] < 1 / factor && alphas[ii][i] > 0)
					needToFactor = true;
			}
			if (needToFactor) {
				for (int ii = 0; ii < numS; ii++) {
					alphas[ii][i] *= factor;
				}
				numOfFactor[0]++;
			}
			factorsA[i] = numOfFactor[0];
		}
		double max = 0;
		int maxI = 0;
		for(int i=0;i<numS;i++){
			if(alphas[i][numT-1]>max){
				maxI = i;
				max = alphas[i][numT-1];
			}
		}
		logll[0] = alphas[maxI][numT-1];
		String Path = String.valueOf(maxI);
		int I = maxI;
		for(int i=numT-1;i>0;i--){
			I = record[I][i];
			Path+=String.valueOf(I);
		}
		logll[0] = 1.0/numT*(Math.log(logll[0])-1.0*numOfFactor[0]*Math.log(factor))/Math.log(2);
		return Path;
	}
	
	public static String naive(Double[] P,Integer[] factorsB, Integer[] numOfFactor,
			double factor, char[] trainData, Double[][] lamdas, Double[][] bs,
			Double[] intial, Double[] end, int numSy, int numS, int numT) {
		numOfFactor[0] = 0;
		String path = "";
		factorsB[numT - 1] = 0;
		Double[][] beta = new Double[numS][numT];
		double p = 1;
		int c = 0;
		int num = trainData[0] - 'A';
		if (num < 0 || num > 25)
			num = 26;
		if (bs[0][num]>bs[1][num]){
			p = p*intial[0]*bs[0][num];
			c = 0;path+="0";
		}else{
			p = p*intial[1]*bs[1][num];
			c = 1;path+="1";
		}		
		for(int i=1;i<trainData.length;i++){
			int n = trainData[i] - 'A';
			if (n < 0 || num > 25)
				n = 26;
			if (bs[0][n]>bs[1][n]){
				p = p*lamdas[c][0]*bs[0][n];
				c = 0;path+="0";
			}else{
				p = p*lamdas[c][1]*bs[1][n];
				c = 1;path+="1";
			}
			if (p < 1 / factor && p > 0) {
					p *= factor;
				numOfFactor[0]++;
			}
			
		}
		
		P[0]=p;
		return path;
	}
	
	
}
