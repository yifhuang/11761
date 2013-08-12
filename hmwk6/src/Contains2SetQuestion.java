import java.util.HashSet;


public class Contains2SetQuestion extends Question {
	HashSet set1 = null;
	HashSet set2 = null;
	@Override
	String desc() {
		// TODO Auto-generated method stub
		
		return "[type 3]ContainsSetQuestion with subsets: "+set1.toString()+" and subsets: "+set2.toString();
	}
	Contains2SetQuestion(HashSet set1,HashSet set2){
		this.type = 3;
		this.set1 = set1;
		this.set2 = set2;
	}
	@Override
	boolean isSatisfied(String[] history, int currentP) {
		boolean kg1 =false, kg2 =false;
		if (currentP==0)
			return false;
		for(int i=0;i<currentP;i++){
			if (set1.contains(history[i])){
				kg1 = true;
			}
			if (set2.contains(history[i])){
				kg2 = true;
			}
		}
		// TODO Auto-generated method stub
		return kg1&&kg2;
	}
}
