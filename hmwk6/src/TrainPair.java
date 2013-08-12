import java.util.ArrayList;
import java.util.List;


public class TrainPair {
	List<Boolean> answers;
	String[] h;
	int currentPoint;
	TrainPair(String[] h,int cp){
		this.h = h;
		this.currentPoint = cp;
		answers = new ArrayList<Boolean>();
	}
}
