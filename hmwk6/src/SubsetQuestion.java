import java.util.List;
import java.util.Set;


public class SubsetQuestion extends Question {
	@Override
	String desc() {
		// TODO Auto-generated method stub
		
		return "[type 2]SubsetQuestion in pos: "+pos+" with subsets: "+subset.toString();
	}
	private Set subset;
	int pos;
	SubsetQuestion(Set subset,int position){
		this.type = 20+position;
		this.subset = subset;
		this.pos = position;
	}
	@Override
	boolean isSatisfied(String[] history,int currentP) {
		// TODO Auto-generated method stub
		if(currentP<pos)
			return false;
		else return subset.contains(history[currentP-pos]);
	}


}
