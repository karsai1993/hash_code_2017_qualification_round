import java.util.ArrayList;

public class SortedResultHandler {
	ArrayList<Integer> combinations = new ArrayList<>();
	ArrayList<Integer> cacheIds = new ArrayList<>();
	ArrayList<Integer> requestVideos = new ArrayList<>();
	
	public SortedResultHandler(ArrayList<Integer> combinations, ArrayList<Integer> cacheIds, ArrayList<Integer> requestVideos) {
		this.combinations = combinations;
		this.cacheIds = cacheIds;
		this.requestVideos = requestVideos;
	}

}
