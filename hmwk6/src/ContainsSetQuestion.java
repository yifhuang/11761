import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ContainsSetQuestion extends Question {
	HashSet subset = null;
	@Override
	String desc() {
		// TODO Auto-generated method stub
		
		return "[type 1]ContainsSetQuestion with subsets: "+subset.toString();
	}
	ContainsSetQuestion(HashSet subset){
		this.type =1;
		this.subset = subset;
	}
	@Override
	boolean isSatisfied(String[] history, int currentP) {
		
		if (currentP==0)
			return false;
		for(int i=0;i<currentP;i++){
			if (subset.contains(history[i])){
				return true;
			}
		}
		// TODO Auto-generated method stub
		return false;
	}

}
