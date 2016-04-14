import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private static class LineArray  {
        private class Line implements Comparable<Line> {
            private Point start;
            private Point end;
            public Line(Point start, Point end) {
                this.start = start;
                this.end = end;
            }
            @Override
            public int compareTo(Line o) {
                int cmp = this.start.compareTo(o.start);
                if (cmp == 0) {
                    return this.end.compareTo(o.end);
                }
                return cmp;
            }
           
        }
        private Line[] lines;
        private int size;
        public LineArray() {
            size = 0;
            lines = new Line[2]; 
        }
        public void add(Point start, Point end) {
            if (size == lines.length)
                resize(lines.length * 2);
            lines[size++] = new Line(start, end);
        }
        private void resize(int newSize) {
            Line[] tempLines = new Line[newSize];
            for (int i = 0; i < size; i++)
                tempLines[i] = lines[i];
            lines = tempLines;
        }
       
       
        
    }
    private LineSegment[] segments; 
    public FastCollinearPoints(Point[] inputPoints) {    // finds all line segments containing 4 or more points
        //Copy points
        Point[] points = new Point[inputPoints.length];
        for (int i = 0; i < inputPoints.length; i++)
            points[i] = inputPoints[i];
        //
        LineArray removeLines = new LineArray();
        LineArray addLines = new LineArray();
        //Sort by x,y
        Arrays.sort(points);
        //Find equal points
        for (int i = 0; i < points.length - 1; i++) 
            if (points[i].compareTo(points[i + 1]) == 0)
                throw new IllegalArgumentException();
        
        Point[] tempPoints = new Point[points.length - 1];
        
        for (int i = 0; i < points.length - 3; i++) {
            //Copy points
            int realLength = points.length - i - 1;
            for (int j = i+1; j < points.length; j++) {
                tempPoints[j-i-1] = points[j];
            }
            
            //Sort points to right based on slope of points[i]
            Arrays.sort(tempPoints, 0, realLength, points[i].slopeOrder());
            //Scan all points and try to find as many as possible with same slope
            //If finds 3 with same slope, we have a 4-point line
            //Stops at realLength-2,
            int j = 0;
            //This is the refSlope, reference slope to compare
            double refSlope = tempPoints[j].slopeTo(points[i]);
            //How much equal slopes, need at least 3 (to have 4 points = 3 + cmp point)
            int countEqual = 1;
            while (++j < realLength) {
                double cmpSlope = tempPoints[j].slopeTo(points[i]);
                //Another equal: increment count
                if (cmpSlope == refSlope)
                    countEqual++;
                //If we have 4 or more points and ended scanning or changed slope, add segment
                if ((cmpSlope != refSlope || j == realLength - 1) && countEqual >= 3) {
                    int lastIndex = j;
                    if (cmpSlope != refSlope)
                        lastIndex--;
                    addLines.add(points[i], tempPoints[lastIndex]);
                    if (countEqual > 3) {
                        //If size is bigger than 3, need to remove this segment afterwards
                        removeLines.add(tempPoints[lastIndex - countEqual + 1], tempPoints[lastIndex]);
                    }
                    //StdOut.println("[SEARCH] found path:");
                 
                    
                }
                //Changed slope
                if (cmpSlope != refSlope) {
                    refSlope = cmpSlope;
                    countEqual = 1;
                }
            }
        }
        
        //Remove duplicated
        segments = new LineSegment[addLines.size - removeLines.size];
        Arrays.sort(addLines.lines, 0, addLines.size);
        Arrays.sort(removeLines.lines, 0, removeLines.size);
        //Do merge
        int add = 0, rm = 0, k = 0;
        while (k < segments.length) {
            if (rm < removeLines.size && addLines.lines[add].compareTo(removeLines.lines[rm]) == 0) {
                rm++;
            }
            else
                segments[k++] = new LineSegment(addLines.lines[add].start, addLines.lines[add].end);
            add++;
        }
    }
    
    public int numberOfSegments() {       // the number of line segments
        return segments.length;
    }
    
    public LineSegment[] segments() {               // the line segments
        LineSegment[] segs = new LineSegment[segments.length];
        for (int i = 0; i < segs.length; i++) {
            segs[i] = segments[i];
        }
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
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
}
