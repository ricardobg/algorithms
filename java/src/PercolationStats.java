

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {

    private double mean;
    private double stdDev;
    private double lowConf;
    private double hiConf;

    public PercolationStats(int N, int T)     // perform T independent experiments on an N-by-N grid
    {
        if (T <= 0)
            throw new IllegalArgumentException();
        //Array with counter 
        double[] openSitesCount = new double[T];
        for (int t = 0; t < T; t++) {
            Percolation perc = new Percolation(N);
            openSitesCount[t] = 0;
            do {
                int i, j; 
                do {
                    i = StdRandom.uniform(N) + 1;
                    j = StdRandom.uniform(N) + 1;
                }
                while (perc.isOpen(i, j));
                
                perc.open(i, j);
                openSitesCount[t]++;
            }
            while (!perc.percolates());
            openSitesCount[t] /= N*N;

        }
        //Calculate stats
        mean = StdStats.mean(openSitesCount);
        stdDev = StdStats.stddev(openSitesCount);

        double temp = 1.96*(stdDev) / Math.sqrt(T);
        lowConf = mean - temp;
        hiConf = mean + temp;
    }

    public double mean()                      // sample mean of percolation threshold
    {
        return mean;
    }

    public double stddev()                    // sample standard deviation of percolation threshold
    {
        return stdDev;
    }

    public double confidenceLo()              // low  endpoint of 95% confidence interval
    {
        return lowConf;
    }

    public double confidenceHi()              // high endpoint of 95% confidence interval
    {
        return hiConf;
    }

    public static void main(String[] args) {
        int N = 200;
        int T = 100;
        if (args.length == 2) { 
            N = Integer.parseInt(args[0]);
            T = Integer.parseInt(args[1]);
        }
        Stopwatch timer = new Stopwatch();
        PercolationStats stats = new PercolationStats(N, T);
        double time = timer.elapsedTime();
        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = " + stats.confidenceLo() + ", " +  stats.confidenceHi());
        StdOut.println("running time (s)        = " + time);
    }

}