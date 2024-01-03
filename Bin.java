/* This class create to be a subclass for the windValues class since
the windValues class need three parameters in a single Bin, and it will
be more clean and reusable for the windValues class if the Bin class is
not suitable for the problem.
 */
public class Bin {
    Double interval;
    Integer count;
    Double cumProbability;

    public Bin(Double newInterval, Integer newCount, Double newCumProbability){
        this.interval = newInterval;
        this.count = newCount;
        this.cumProbability = newCumProbability;
    }
    public double getInterval(){
        return interval;
    }
    public String toString(){
        return "Interval:" + interval + " Count: " + count + " Cumulative Probability: " + cumProbability;
    }

}
