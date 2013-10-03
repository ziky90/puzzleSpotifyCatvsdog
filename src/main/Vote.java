package main;

import java.util.ArrayList;

/**
 *
 * @author zikesjan
 */
public class Vote {

    private String keep;
    private String throwAway;
    private ArrayList<Vote> contradictions;

    public Vote(String keep, String throwAway) {
        this.keep = keep;
        this.throwAway = throwAway;
        this.contradictions = new ArrayList<Vote>();
    }

    public String getKeep() {
        return keep;
    }

    public void setKeep(String keep) {
        this.keep = keep;
    }

    public String getThrowAway() {
        return throwAway;
    }

    public void setThrowAway(String throwAway) {
        this.throwAway = throwAway;
    } 
    
    public void addContradiction(Vote vote){
        this.contradictions.add(vote);
    }    
    
    public ArrayList<Vote> getContradiction(){
        return contradictions;
    }
}
