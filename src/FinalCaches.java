import java.util.ArrayList;

public class FinalCaches {
	int id;
	int memoryLeft;
	ArrayList<Integer> videos = new ArrayList<>();
	
	public FinalCaches(int id, int memoryLeft, ArrayList<Integer> videos) {
		this.id = id;
		this.memoryLeft = memoryLeft;
		this.videos = videos;
	}

	public FinalCaches(int id, int memoryLeft) {
		this.id = id;
		this.memoryLeft = memoryLeft;
	}	
}
