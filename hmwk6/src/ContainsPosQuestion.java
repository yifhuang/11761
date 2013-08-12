import java.util.HashSet;
import java.util.List;


public class ContainsPosQuestion extends Question {
	String pos = null;
	String desc() {
		// TODO Auto-generated method stub
		
		return "ContainsPosQuestion with subsets: "+pos;
	}
	ContainsPosQuestion(String pos){
		this.pos = pos;
	}
	@Override
	boolean isSatisfied(String[] history,int currentP) {
		
		if (currentP==0)
			return false;
		for(int i=0;i<currentP;i++){
			if(pos.equals(history[i]))
				return true;
		}
		// TODO Auto-generated method stub
		return false;
	}
}
