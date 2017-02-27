import java.util.ArrayList;

public class ResultHandler {
	ArrayList<Integer> combinations = new ArrayList<>();
	ArrayList<Integer> cacheIds = new ArrayList<>();
	ArrayList<Integer> requestVideos = new ArrayList<>();
	
	public ResultHandler(ArrayList<Integer> combinations, ArrayList<Integer> cacheIds, ArrayList<Integer> requestVideos) {
		this.combinations = combinations;
		this.cacheIds = cacheIds;
		this.requestVideos = requestVideos;
	}
}
