import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.lang.Math ;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    private double []openedNum;
    private int tNum;
    public PercolationStats(int n, int trials){   // perform trials independent experiments on an n-by-n grid
        if(trials<=0)throw new IllegalArgumentException();;
        openedNum = new double[trials];
        tNum=trials;
        int num;
        for(int i=0;i<trials;i++){        
            Percolation p = new Percolation(n);
            while(!p.percolates()){
                num = StdRandom.uniform(n*n);
                p.open(num /n,num %n);
            }
            openedNum[i] = (double)p.numberOfOpenSites()/(n*n);
        }
    }
    public double mean(){                         // sample mean of percolation threshold
        return StdStats.mean(openedNum);
    }
    public double stddev(){                       // sample standard deviation of percolation threshold
        return StdStats.stddev(openedNum);
    }
    public double confidenceLow(){                // low  endpoint of 95% confidence interval
        return mean() -( (stddev()*1.96)/Math.sqrt(tNum));
    }
    public double confidenceHigh(){               // high endpoint of 95% confidence interval
        return mean() +( (stddev()*1.96)/Math.sqrt(tNum));
    }
    public static void main(String[] args){// unit testing (required)
        int size = StdIn.readInt();
        int t = StdIn.readInt();
        Stopwatch time = new Stopwatch();
        PercolationStats p = new PercolationStats(size,t);
        StdOut.print(time.elapsedTime());
        StdOut.print("\n");
        StdOut.print(p.mean());
        StdOut.print("\n");
        StdOut.print(p.stddev());
        StdOut.print("\n");
        StdOut.print(p.confidenceLow());
        StdOut.print("\n");
        StdOut.print(p.confidenceHigh());
    }
}