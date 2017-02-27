import java.util.ArrayList;

public class EndPointHandler{
	int latencyToDataCenter;
	ArrayList<Integer> arrayOfCacheIds = new ArrayList<>();
	ArrayList<Integer> arrayOfCacheLatencies = new ArrayList<>();
	ArrayList<Integer> arrayOfRequestNumbers = new ArrayList<>();
	ArrayList<Integer> arrayOfRequestedVideos = new ArrayList<>();
	
	public EndPointHandler(int latencyToDataCenter, ArrayList<Integer> arrayOfCacheIds,
			ArrayList<Integer> arrayOfCacheLatencies, ArrayList<Integer> arrayOfRequestNumbers,
			ArrayList<Integer> arrayOfRequestedVideos) {
		this.latencyToDataCenter = latencyToDataCenter;
		this.arrayOfCacheIds = arrayOfCacheIds;
		this.arrayOfCacheLatencies = arrayOfCacheLatencies;
		this.arrayOfRequestNumbers = arrayOfRequestNumbers;
		this.arrayOfRequestedVideos = arrayOfRequestedVideos;
	}
	
	
	
}
