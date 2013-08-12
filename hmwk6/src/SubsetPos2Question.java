import java.util.Set;


public class SubsetPos2Question extends Question {
	@Override
	String desc() {
		// TODO Auto-generated method stub
		
		return "[type 4]SubsetQuestion in pos: "+pos+" with subsets: "+set1.toString()+"and in pos: "+(pos+1)+" with subsets: "+set2.toString();
	}
	private Set set1;
	private Set set2;
	int pos;
	SubsetPos2Question(Set set1,Set set2,int position){
		this.type = 4;
		this.set1 = set1;
		this.set2 = set2;
		this.pos = position;
	}
	@Override
	boolean isSatisfied(String[] history,int currentP) {
		// TODO Auto-generated method stub
		if(currentP<pos+1)
			return false;
		else return set1.contains(history[currentP-pos])&&set2.contains(history[currentP-pos-1]);
	}

}
