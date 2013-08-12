import java.util.List;


public abstract class Question {
	int type;
	String desc(){
		return null;
	}
	boolean isSatisfied(String[] history,int currentP){
		
		return true;
	}
}
