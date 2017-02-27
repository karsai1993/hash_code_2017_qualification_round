import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class Main {
	
	static int videoNum;
	static int endPointNum;
	static int endPointLeft;
	static int requestNum;
	static int cacheNum;
	static int cacheSize;
	static int [] videoSizes;
	static int [] latenciesBetweenEndPointDataCenter;
	static int [] numberOfCachesRelatedToEndPoints;
	static int [] limitOfEndPoints;
	static ArrayList<String> cacheFeatures = new ArrayList<>();//corresp. endpoint, id, latency
	static ArrayList<String> requestFeatures = new ArrayList<>();//corresp. video, endp, number
	
	public static void loadFile(String file) {
        try (BufferedReader br=new BufferedReader(new FileReader(file))){
            String currentLine;
            int counter = 0;
            Boolean isNewEndPoint = true;
            while((currentLine = br.readLine()) != null){
            	counter++;
            	if(counter == 1){
                    String [] mainFeatures = currentLine.split(" ");
                    videoNum = Integer.parseInt(mainFeatures[0]);
                    endPointNum = Integer.parseInt(mainFeatures[1]);
                    requestNum = Integer.parseInt(mainFeatures[2]);
                    cacheNum = Integer.parseInt(mainFeatures[3]);
                    cacheSize = Integer.parseInt(mainFeatures[4]);
                    endPointLeft = endPointNum;
                    videoSizes = new int[videoNum];
                	latenciesBetweenEndPointDataCenter = new int[endPointNum];
                	numberOfCachesRelatedToEndPoints = new int[endPointNum];
                	limitOfEndPoints = new int[endPointNum];
                }
                else if(counter == 2){
                    String [] videoSizesString = currentLine.split(" ");
                    for(int i=0; i<videoSizes.length; i++){
                    	if(Integer.parseInt(videoSizesString[i]) > cacheSize){
                    		videoSizes[i] = 0;
                    	}
                    	else{
                    		videoSizes[i] = Integer.parseInt(videoSizesString[i]);
                    	}
                    }
                }
                else if (currentLine.split(" ").length != 3){
                	if(isNewEndPoint){
                		String [] endPointFeatures = currentLine.split(" ");
                    	latenciesBetweenEndPointDataCenter[endPointNum-endPointLeft] = Integer.parseInt(endPointFeatures[0]);                	
                    	numberOfCachesRelatedToEndPoints[endPointNum-endPointLeft] = Integer.parseInt(endPointFeatures[1]);
                    	limitOfEndPoints[endPointNum-endPointLeft] = counter;
                    	isNewEndPoint = false;
                	}
                	else{
                		int counterLimit = numberOfCachesRelatedToEndPoints[endPointNum-endPointLeft] + limitOfEndPoints[endPointNum-endPointLeft];
                		String [] cacheLine = currentLine.split(" ");
                		if(counter <= counterLimit){
                			cacheFeatures.add(endPointNum-endPointLeft+","+cacheLine[0]+","+cacheLine[1]);
                		}
                		if(counter == counterLimit){
                			endPointLeft--;
                    		isNewEndPoint = true;
                		}
                	}
                }
                else{
                	String [] requestLine = currentLine.split(" ");
                	requestFeatures.add(requestLine[0]+";"+requestLine[1]+";"+requestLine[2]);
                }
            }
        }catch (Exception e){}
    }

    public static void main(String[] args) {
    	String fileName = "trending_today";
    	//String fileName = "videos_worth_spreading";
    	//String fileName = "kittens";
    	//String fileName = "me_at_the_zoo";
    	System.out.println("File reading...");
    	loadFile(fileName+".in");
    	System.out.println("Collecting data corresponds to the same endpoint...");
        ArrayList<EndPointHandler> endPointData = new ArrayList<EndPointHandler>();
    	for(int i=0; i<endPointNum; i++){
    		ArrayList<Integer> cacheIdRelatedToThisEndPoint = new ArrayList<>();
    		ArrayList<Integer> cacheLatencyRelatedToThisEndPoint = new ArrayList<>();
    		ArrayList<Integer> requestNumberRelatedToThisEndPoint = new ArrayList<>();
    		ArrayList<Integer> requestVideoRelatedToThisEndPoint = new ArrayList<>();
    		for(int j=0; j<cacheFeatures.size(); j++){
    			String [] cacheData = cacheFeatures.get(j).split(",");
    			if(Integer.parseInt(cacheData[0]) == i){
    				cacheIdRelatedToThisEndPoint.add(Integer.parseInt(cacheData[1]));
    				cacheLatencyRelatedToThisEndPoint.add(Integer.parseInt(cacheData[2]));
    			}
    		}
    		for(int j=0; j<requestFeatures.size(); j++){
    			String [] requestData = requestFeatures.get(j).split(";");
    			if(Integer.parseInt(requestData[1]) == i){
    				requestVideoRelatedToThisEndPoint.add(Integer.parseInt(requestData[0]));
    				requestNumberRelatedToThisEndPoint.add(Integer.parseInt(requestData[2]));
    			}
    		}
        	endPointData.add(new EndPointHandler(latenciesBetweenEndPointDataCenter[i],
        			cacheIdRelatedToThisEndPoint,cacheLatencyRelatedToThisEndPoint,
        			requestNumberRelatedToThisEndPoint,requestVideoRelatedToThisEndPoint));
    	}
    	//we have to indentify the biggest combination(latency difference * requestnumber)
    	//combination, corresp. cacheid, corresp. videonum
    	System.out.println("Calculating combination and storing corresponding ids...");
    	ArrayList<ResultHandler> resultHandler = new ArrayList<ResultHandler>();
    	for(int i=0; i<endPointData.size(); i++){
    		int currentEPRequestNum = endPointData.get(i).arrayOfRequestNumbers.size();
			int currentEPCacheNum = endPointData.get(i).arrayOfCacheIds.size();
    		if(currentEPCacheNum != 0 && currentEPRequestNum != 0){
    	    	ArrayList<Integer> latencyDifferences = new ArrayList<>();
    	    	ArrayList<Integer> sortedLatencyDifferences = new ArrayList<>();
    	    	ArrayList<Integer> sortedCacheIds = new ArrayList<>();
    	    	ArrayList<Integer> sortedRequestNumbers = new ArrayList<>();
    	    	ArrayList<Integer> sortedRequestVideos = new ArrayList<>();
    	    	ArrayList<Integer> resultEPCombinations = new ArrayList<>();
    	    	ArrayList<Integer> resultEPCacheIds = new ArrayList<>();
    	    	ArrayList<Integer> resultEPVideos = new ArrayList<>();
    			int latencyToDataCenter = endPointData.get(i).latencyToDataCenter;
    			int diff;
    			for(int j=0; j<currentEPCacheNum; j++){
    				diff = latencyToDataCenter - endPointData.get(i).arrayOfCacheLatencies.get(j);
    				diff = diff > 0 ? diff : 0;
    				latencyDifferences.add(diff);//current pos equals cache id on current pos
        		}
    			for(int j=0; j<currentEPCacheNum; j++){
    				sortedLatencyDifferences.add(latencyDifferences.get(j));
        		}
    			Collections.sort(sortedLatencyDifferences);
    			Collections.reverse(sortedLatencyDifferences);
    			for(int j=0; j<currentEPCacheNum; j++){
    				for(int k=0; k<currentEPCacheNum; k++){
            			if(sortedLatencyDifferences.get(j).equals(latencyDifferences.get(k))){
            				sortedCacheIds.add(endPointData.get(i).arrayOfCacheIds.get(k));
            			}
            		}
        		}
        		//let's sort numbers
        		for(int j=0; j<currentEPRequestNum; j++){
        			sortedRequestNumbers.add(endPointData.get(i).arrayOfRequestNumbers.get(j));
        		}
    			Collections.sort(sortedRequestNumbers);
    			Collections.reverse(sortedRequestNumbers);
    			for(int j=0; j<currentEPRequestNum; j++){
    				for(int k=0; k<currentEPRequestNum; k++){
            			if(sortedRequestNumbers.get(j).equals(endPointData.get(i).arrayOfRequestNumbers.get(k))){
            				sortedRequestVideos.add(endPointData.get(i).arrayOfRequestedVideos.get(k));
            			}
            		}
        		}
    			int combinationNum = currentEPCacheNum < currentEPRequestNum ? currentEPCacheNum : currentEPRequestNum;
    			for(int j=0; j<combinationNum; j++){
        			resultEPCombinations.add((sortedLatencyDifferences.get(j)*sortedRequestNumbers.get(j)));
        			resultEPCacheIds.add(sortedCacheIds.get(j));
        			resultEPVideos.add(sortedRequestVideos.get(j));
        		}
    			resultHandler.add(new ResultHandler(resultEPCombinations, resultEPCacheIds, resultEPVideos));
    		}
    	}
    	//we have to sort the endpoint according to how much its biggest combination is
    	ArrayList<Integer> combinationToDecideWhichIsTheBiggest = new ArrayList<>();
    	for(int i=0; i<resultHandler.size(); i++){
    		combinationToDecideWhichIsTheBiggest.add(resultHandler.get(i).combinations.size());
    	}
    	Collections.sort(combinationToDecideWhichIsTheBiggest);
    	Collections.reverse(combinationToDecideWhichIsTheBiggest);
    	ArrayList<SortedResultHandler> sortedResultHandler = new ArrayList<SortedResultHandler>();
    	for(int i=0; i<resultHandler.size(); i++){
    		for(int j=0; j<combinationToDecideWhichIsTheBiggest.size(); j++){
    			if(resultHandler.get(i).combinations.size() == combinationToDecideWhichIsTheBiggest.get(j)){
        			sortedResultHandler.add(new SortedResultHandler(resultHandler.get(i).combinations, resultHandler.get(i).cacheIds, resultHandler.get(i).requestVideos));
    			}
    		}
    	}
    	ArrayList<Integer> occuredIds = new ArrayList<Integer>();
    	for(int i=0; i<sortedResultHandler.size(); i++){
    		for(int j=0; j<sortedResultHandler.get(i).cacheIds.size(); j++){
    			if(!occuredIds.contains(sortedResultHandler.get(i).cacheIds.get(j))){
    				occuredIds.add(sortedResultHandler.get(i).cacheIds.get(j));
    			}
        	}
    	}
    	System.out.println("Creating final values...");
    	ArrayList<FinalCaches> finalCaches = new ArrayList<FinalCaches>();
    	int cacheSizeLeft = cacheSize;
    	for(int i=0; i<occuredIds.size(); i++){
    		finalCaches.add(new FinalCaches(occuredIds.get(i), cacheSizeLeft, new ArrayList<Integer>()));
    	}
    	for(int i=0; i<sortedResultHandler.size(); i++){
    		for(int j=0; j<sortedResultHandler.get(i).cacheIds.size(); j++){
    			for(int k=0; k<finalCaches.size(); k++){
    				if(sortedResultHandler.get(i).cacheIds.get(j) == finalCaches.get(k).id && videoSizes[sortedResultHandler.get(i).requestVideos.get(j)] != 0 && finalCaches.get(k).memoryLeft > videoSizes[sortedResultHandler.get(i).requestVideos.get(j)]){
    					ArrayList<Integer> innerVideosOrder = new ArrayList<Integer>();
    					int innerMemory;
    					int innerId;
    					innerVideosOrder = finalCaches.get(k).videos;
    					if(!innerVideosOrder.contains(sortedResultHandler.get(i).requestVideos.get(j))){
    						innerVideosOrder.add(sortedResultHandler.get(i).requestVideos.get(j));
    					}
    					innerMemory = finalCaches.get(k).memoryLeft - videoSizes[sortedResultHandler.get(i).requestVideos.get(j)];
    					innerId = finalCaches.get(k).id;
    					finalCaches.remove(k);
    					finalCaches.add(new FinalCaches(innerId, innerMemory, innerVideosOrder));
    					k=finalCaches.size()-1;
    				}
    			}
    		}
    	}
    	for(int i=0; i<finalCaches.size(); i++){
    		if(finalCaches.get(i).videos.size() == 0){
    			finalCaches.remove(i);
    		}
    	}
    	generateSubmissionFile(finalCaches,fileName);
    	System.out.println("Program finishes.");
    }
    public static void generateSubmissionFile(ArrayList<FinalCaches> arrayOfResults,String nameOfFile) {
        //result_output
        String file = "submission_"+nameOfFile+".out";
        try (PrintWriter writer = new PrintWriter(file, "UTF-8")){
            writer.println(arrayOfResults.size());
            for(int i=0; i<arrayOfResults.size(); i++){
            	writer.print(arrayOfResults.get(i).id+" ");
        		for(int j=0; j<arrayOfResults.get(i).videos.size(); j++){
        			writer.print(arrayOfResults.get(i).videos.get(j)+" ");
        		}
        		writer.println();            
            }
            writer.close();
        }catch (Exception e) {
            System.out.println("ERROR");
        }
	}
}