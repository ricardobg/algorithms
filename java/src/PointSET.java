import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {

	private TreeSet<Point2D> points;

	public PointSET()                               // construct an empty set of points 
	{
		points = new TreeSet<>();
	}

	public boolean isEmpty()                      // is the set empty?
	{
		return points.isEmpty();
	}

	public int size()                         // number of points in the set
	{
		return points.size();
	}

	public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
	{
		if (p == null)
			throw new NullPointerException();
		points.add(p);
	}

	public boolean contains(Point2D p)            // does the set contain point p?
	{
		if (p == null)
			throw new NullPointerException();
		return points.contains(p);
	}

	public void draw()                         // draw all points to standard draw
	{
		for (Point2D cmp : points) {
			cmp.draw();
		}
	}

	public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle
	{
		if (rect == null)
			throw new NullPointerException();
		Queue<Point2D> ret = new Queue<>();
		for (Point2D cmp : points) {
			if (rect.contains(cmp))
				ret.enqueue(cmp);
		}
		return ret;
	}

	public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
	{
		if (p == null)
			throw new NullPointerException();
		Point2D near = null;
		double lastDistance = Double.POSITIVE_INFINITY;
		for (Point2D cmp : points) {
			double dist = cmp.distanceSquaredTo(p);
			if (dist < lastDistance) {
				lastDistance = dist;
				near = cmp;
			}
		}
		return near;
	}

	public static void main(String[] args)                  // unit testing of the methods (optional)
	{

	}
}