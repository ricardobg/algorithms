import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    
    private LinkedQueue<LineSegment> segments;
    
    public BruteCollinearPoints(Point[] points) {    // finds all line segments containing 4 points
        if (points == null)
            throw new NullPointerException();
        segments = new LinkedQueue<>();
        Point[] tempPoints = new Point[4];
        Point[] sortPoints = new Point[4];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new NullPointerException();
            for (int j = i + 1; j < points.length; j++)
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException();
        }
        for (int i = 0; i < points.length; i++) {
            tempPoints[0] = points[i];
            for (int j = i+1; j < points.length; j++) {
                tempPoints[1] = points[j];
                for (int k = j+1; k < points.length; k++) {
                    tempPoints[2] = points[k];
                    sortPoints[0] = tempPoints[0];
                    sortPoints[1] = tempPoints[1];
                    sortPoints[2] = tempPoints[2];
                    Arrays.sort(sortPoints, 0, 3);
                    double slope1 = sortPoints[1].slopeTo(sortPoints[0]);
                    double slope2 = sortPoints[2].slopeTo(sortPoints[0]);
                    if (slope1 != slope2)
                        continue;
                    for (int l = k+1; l < points.length; l++) {
                        tempPoints[3] = points[l];
                        sortPoints[0] = tempPoints[0];
                        sortPoints[1] = tempPoints[1];
                        sortPoints[2] = tempPoints[2];
                        sortPoints[3] = tempPoints[3];
                        Arrays.sort(sortPoints);
                        
                        slope1 = sortPoints[1].slopeTo(sortPoints[0]);
                        slope2 = sortPoints[2].slopeTo(sortPoints[0]);
                        double slope3 = sortPoints[3].slopeTo(sortPoints[0]);
                        if (slope1 == slope2 && slope3 == slope1) {
                           //Gets smallest segment
                           segments.enqueue(new LineSegment(sortPoints[0], sortPoints[3]));
                        } 
                    }
                }
            }
        }
      
    }
    
    public int numberOfSegments() {        // the number of line segments
        return segments.size();
    }
    
    public LineSegment[] segments() {
        LineSegment[] segs = new LineSegment[segments.size()];
        for (int i = 0; i < segs.length; i++) {
            segs[i] = segments.dequeue();
        }
        for (int i = 0; i < segs.length; i++)
            segments.enqueue(segs[i]);
        return segs;
    }
    
    public static void main(String[] args) {

        // read the N points from a file
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show();
        
        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
}