package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 *
 * @author zikesjan
 * zikesjan@gmail.com
 * http://about.me/zikesjan
 * 
 */
public class Solution {

    private static int testCases;
    private static HashMap<Vote, Vote> pairs;
    private static HashMap<Vote, Integer> distance;
    private static HashSet<Vote> catPartition;
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            readFromStdInFast();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        
    }
    
    /*
     * just reading of the data from the system
     */
    static void readFromStdInFast() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        testCases = Integer.parseInt(st.nextToken());
        
        for (int i = 0; i < testCases; i++) {
            st = new StringTokenizer(br.readLine());
            Integer.parseInt(st.nextToken());
            Integer.parseInt(st.nextToken());
            
            int voters = Integer.parseInt(st.nextToken());
            
            Vote[] votes = new Vote[voters];
            for (int j = 0; j < voters; j++) {
                st = new StringTokenizer(br.readLine());
                String like = st.nextToken();
                String hate = st.nextToken();
                Vote v = new Vote(like, hate);
                votes[j] = v;
            }
            System.out.println(solveTestCase(voters, votes));
        }
    }
    
    /**
     * method for solving the test case using Hopcroft–Karp algorithm
     * graph must be bipartite so it's standard max cardinality matching
     * code is inspired by the pseudocode on wikipedia (just speeded up a bit)
     * @param voters
     * @param votes
     * @param p1
     * @param p2
     * @return 
     */
    static int solveTestCase(int voters, Vote[] votes){
        pairs = new HashMap<Vote, Vote>();
        distance = new HashMap<Vote, Integer>();
        addContradictions(votes);
        int result = 0;
        while(bredthFirstSearch()){
            for(Vote v : catPartition){
                if(pairs.get(v) == null){
                    if(depthFirstSearch(v)){
                        result++;
                    }
                }
            }
        }
        
        return voters - result;
    }
    
    
    /**
     * Just the helper method that builds the graph it puts there edge if there is a contradiction
     * @param votes 
     */
    static void addContradictions(Vote[] votes){
        catPartition = new HashSet<Vote>();
        
        for (int i = 0; i < votes.length; i++) {
            Vote vote1 = votes[i];
            for (int j = i+1; j < votes.length; j++) {
                Vote vote2 = votes[j];
                if(vote1.getKeep().equals(vote2.getThrowAway()) || vote2.getKeep().equals(vote1.getThrowAway())){
                    vote1.addContradiction(vote2);
                    vote2.addContradiction(vote1);
                    
                    //obvious from the cat or dog loving person
                    if(vote1.getKeep().charAt(0) == 'C'){
                        catPartition.add(vote1);
                    } else{
                        catPartition.add(vote2);
                    }
                    distance.put(vote1, Integer.MAX_VALUE);
                    distance.put(vote2, Integer.MAX_VALUE);
                }
            }
        }
    }
    
    
    /**
     * classical BFS modified to augumenting paths
     * @param p1
     * @param p2
     * @return 
     */
    static boolean bredthFirstSearch(){
        Queue<Vote> queue = new LinkedList<Vote>();
        for(Vote v : catPartition){
            if(pairs.get(v) == null){
                distance.put(v, 0);
                queue.add(v);
            }else{
                distance.put(v, Integer.MAX_VALUE);
            }
        }
        distance.put(null, Integer.MAX_VALUE); 
        
        while(!queue.isEmpty()){
            Vote v = queue.poll();
            if(v != null){
                for(Vote u : v.getContradiction()){
                    if(distance.get(pairs.get(u)) == Integer.MAX_VALUE){
                        distance.put(pairs.get(u), distance.get(v)+1);
                        queue.add(pairs.get(u));
                    }
                }
            }
        }
        
        return distance.get(null) != Integer.MAX_VALUE;
    }
    
    
    /**
     * classical DFS modified to augumenting path
     * @param v
     * @return 
     */
    static boolean depthFirstSearch(Vote v){
        if(v != null){
            for(Vote u : v.getContradiction()){                
                if(distance.get(pairs.get(u)) == distance.get(v)+1){
                    if(depthFirstSearch(pairs.get(u))){
                        pairs.put(v, u);
                        pairs.put(u, v);
                        return true;
                    }
                }
            }
            distance.put(v, Integer.MAX_VALUE);
            return false;
        }
        return true;
    }
}
