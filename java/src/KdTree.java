import java.util.TreeSet;

import com.sun.xml.internal.stream.util.ThreadLocalBufferAllocator;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
	
	private class Node implements Comparable<Point2D> {
		
		private Node right, left, parent;
		private Point2D point;
		private final boolean even;
		
		public int compareTo(Point2D cmp) {
			if (point.equals(cmp)) return 0;
			double val;
			if (even) val = point.x() - cmp.x();
			else val = point.y() - cmp.y();
			
			if (val < 0) return -1;
			else if (val == 0) return 0;
			return 1;
		}
		
		public boolean equals(Point2D cmp) {
			return cmp.equals(point);
		}
		
		public boolean isX() {
			return even;
		}
		
		public double getCmpKey() {
			if (even)
				return point.x();
			else
				return point.y();
		}
		
		public Point2D getLine(Point2D end) {
			Point2D start = null;
			if (even) {
				start = new Point2D(point.x(), 0);
				end = new Point2D(point.x(), 1);
			}
			else {
				start = new Point2D(0, point.y());
				end = new Point2D(1, point.y());
			}
			return start;
		}
		
		public boolean intersectsTree(RectHV rect) {
			if (parent == null)
				return rect.contains(point);
			double cmpMinCoordinate, cmpMaxCoordinate;
			if (parent.isX()) {
				cmpMinCoordinate = rect.xmin();
				cmpMaxCoordinate = rect.xmax();
			}
			else {
				cmpMinCoordinate = rect.ymin();
				cmpMaxCoordinate = rect.ymax();
			}
			//Right child
			if (parent.right != null && point.equals(parent.right.point)) {
				if (parent.getCmpKey() < cmpMaxCoordinate)
					return true;
				return false;
			}
			//Left child
			else {
				if (parent.getCmpKey() > cmpMinCoordinate)
					return true;
				return false;
			}
		}
		
		public Node(Point2D point, Node parent) {
			this.even = parent == null || (!parent.even);
			this.parent = parent;
			this.point = point;
			this.right = null;
			this.left = null;
		}
	}
	
	private Node root;
	private int size;
	
	public KdTree()                               // construct an empty set of points 
	{
		root = null;
		size = 0;
	}
	
	public boolean isEmpty()                      // is the set empty?
	{
		return root == null;
	}
	
	public int size()                         // number of points in the set
	{
		return size;
	}
	
	public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
	{
		if (p == null)
			throw new NullPointerException();
		if (root == null) {
			root = new Node(p, null);
			size++;
			return;
		}
		Node parent = root;
		Node visit = root;
		int cmp = 0;
		while (visit != null) {
			cmp = visit.compareTo(p);
			if (cmp == 0) return;
			parent = visit;
			if (cmp < 0)
				visit = visit.right;
			else
				visit = visit.left;
		}
		
		if (cmp < 0)
			parent.right = new Node(p, parent);
		else
			parent.left = new Node(p, parent);
		size++;
	}
	
	public boolean contains(Point2D p)            // does the set contain point p?
	{
		if (p == null)
			throw new NullPointerException();
		Node visit = root;
		while (visit != null) {
			int cmp = visit.compareTo(p);
			if (cmp == 0) return true;
			if (cmp < 0)
				visit = visit.right;
			else
				visit = visit.left;
		}
		return false;
	}
	
	private void drawPoint(Node node) {
		if (node == null) return;
		StdDraw.setPenColor();
		node.point.draw();

		//Draw line
		if (node.isX())
			StdDraw.setPenColor(StdDraw.RED);
		else
			StdDraw.setPenColor(StdDraw.BLUE);
		Point2D start = null, end = null;
		start = node.getLine(end);
		start.drawTo(end);
		
		drawPoint(node.left);
		drawPoint(node.right);
	}
	
	public void draw()                         // draw all points to standard draw
	{
		drawPoint(root);
	}
	
	
	private void visitNode(Node node, RectHV rect, Queue<Point2D> pts) {
		if (node == null) return;
		
		if (rect.contains(node.point))
			pts.enqueue(node.point);
		
		if (node.right != null && node.right.intersectsTree(rect))
			visitNode(node.right, rect, pts);
		else if (node.left != null && node.left.intersectsTree(rect))
			visitNode(node.left, rect, pts);
	}
	
	public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle
	{
		if (rect == null)
			throw new NullPointerException();
		Queue<Point2D> ret = new Queue<>();
		visitNode(root, rect, ret);
		return ret;
	}
	
	private void visitNode(Node node, Point2D cmp, Point2D near, double minDist) {
		if (node == null) return;
		double dist = cmp.distanceSquaredTo(node.point);
		if (dist < minDist) {
			minDist = dist;
			near = node.point;
		}
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