import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

	/**
	 * Node in the tree
	 * @author ricardo
	 *
	 */
	private class Node implements Comparable<Point2D> {

		private Node right, left;
		private Point2D point;
		private final boolean even;
		private RectHV rectangle;

		public Node(Point2D point, Node parent, boolean right) {
			this.even = parent == null || (!parent.even);
			this.point = point;
			this.right = null;
			this.left = null;

			double xmin = 0, ymin = 0, xmax = 0, ymax = 0;
			if (parent != null && parent.even) {
				//Divide by X coordinate
				if (right) {
					xmin = parent.point.x();
					xmax = parent.rectangle.xmax();
				}
				else {
					xmin = parent.rectangle.xmin();
					xmax = parent.point.x();
				}

				ymin = parent.rectangle.ymin();
				ymax = parent.rectangle.ymax();
			}
			else if (parent != null) {
				if (right) {
					ymin = parent.point.y();
					ymax = parent.rectangle.ymax();
				}
				else {
					ymin = parent.rectangle.ymin();
					ymax = parent.point.y();
				}
				xmin = parent.rectangle.xmin();
				xmax = parent.rectangle.xmax();
			}
			this.rectangle = new RectHV(xmin, ymin, xmax, ymax);
		}
		public Node(Point2D point, Node parent, RectHV rectangle) {
			this.even = parent == null || (!parent.even);
			this.point = point;
			this.right = null;
			this.left = null;
			this.rectangle = rectangle;
		}

		public int compareTo(Point2D cmp) {
			if (point.equals(cmp)) return 0;
			double val;
			if (even) val = point.x() - cmp.x();
			else val = point.y() - cmp.y();

			if (val < 0) return -1;
			else if (val == 0) return 0;
			return 1;
		}

		public boolean isX() {
			return even;
		}

		public Point2D getLineStart() {
			Point2D start = null;
			if (even)
				start = new Point2D(point.x(), rectangle.ymin());
			else
				start = new Point2D(rectangle.xmin(), point.y());

			return start;
		}

		public Point2D getLineEnd() {
			Point2D end = null;
			if (even)
				end = new Point2D(point.x(), rectangle.ymax());
			else
				end = new Point2D(rectangle.xmax(), point.y());
			return end;
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
			root = new Node(p, null, new RectHV(0, 0, 1, 1));
			size++;
			return;
		}
		Node parent = root;
		Node visit = root;
		int cmp = 0;
		while (visit != null) {
			cmp = visit.compareTo(p);
			parent = visit;
			if (cmp < 0)
				visit = visit.right;
			else if (cmp > 0)
				visit = visit.left;
			else {
				//Equals
				if (visit.point.x() == p.x() && visit.point.y() == p.y())
					return;
				visit = visit.right;
			}
		}

		if (cmp <= 0)
			parent.right = new Node(p, parent, true);
		else
			parent.left = new Node(p, parent, false);
		size++;
	}

	public boolean contains(Point2D p)            // does the set contain point p?
	{
		if (p == null)
			throw new NullPointerException();
		Node visit = root;
		while (visit != null) {
			int cmp = visit.compareTo(p);
			if (cmp == 0 && p.x() == visit.point.x() && p.y() == visit.point.y())
				return true;
			if (cmp <= 0)
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
		Point2D start = node.getLineStart(), end = node.getLineEnd();
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

		if (node.left != null && rect.intersects(node.left.rectangle))
			visitNode(node.left, rect, pts);
		if (node.right != null && rect.intersects(node.right.rectangle))
			visitNode(node.right, rect, pts);

	}

	public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle
	{
		if (rect == null)
			throw new NullPointerException();
		Queue<Point2D> ret = new Queue<>();
		visitNode(root, rect, ret);
		return ret;
	}
	/**
	 * 
	 * @param node
	 * @param cmp The point to cmp
	 * @param near The nearest found until now
	 * @param minDist
	 */
	private void visitNode(Node node, Point2D cmp, NearPoint near) {
		if (node == null) return;

		double dist = cmp.distanceSquaredTo(node.point);

		if (dist < near.dist) {
			near.dist = dist;
			near.point = node.point;
		}

		double distLeft = Double.POSITIVE_INFINITY, distRight = Double.POSITIVE_INFINITY;
		if (node.left != null)
			distLeft = node.left.rectangle.distanceSquaredTo(cmp);
		if (node.right != null)
			distRight = node.right.rectangle.distanceSquaredTo(cmp);

		if (distLeft < distRight) {
			if (distLeft < near.dist)
				visitNode(node.left, cmp, near);
			if (distRight < near.dist)
				visitNode(node.right, cmp, near);
		}
		else {
			if (distRight < near.dist)
				visitNode(node.right, cmp, near);
			if (distLeft < near.dist)
				visitNode(node.left, cmp, near);
		}

	}

	private class NearPoint {
		private Point2D point = null;
		private double dist = Double.POSITIVE_INFINITY;
	}

	public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
	{
		if (p == null)
			throw new NullPointerException();
		NearPoint near = new NearPoint();
		visitNode(root, p, near);
		return near.point;
	}

	public static void main(String[] args)                  // unit testing of the methods (optional)
	{
		KdTree tree = new KdTree();
		System.out.println("Size = " + tree.size());
		tree.insert(new Point2D(0.5, 0.5));
		System.out.println("Size = " + tree.size());
		tree.insert(new Point2D(0.6, 0.7));
		System.out.println("Size = " + tree.size());
		tree.insert(new Point2D(0.7, 0.5));
		System.out.println("Size = " + tree.size());
		tree.insert(new Point2D(0.8, 0.5));
		System.out.println("Size = " + tree.size());
		tree.insert(new Point2D(0.9, 0.5));
		System.out.println("Size = " + tree.size());
		tree.insert(new Point2D(0.1, 0.5));
		System.out.println("Size = " + tree.size());
		tree.insert(new Point2D(0.2, 0.5));
		System.out.println("Size = " + tree.size());
		tree.insert(new Point2D(0.2, 0.5));
		System.out.println("Size = " + tree.size());
	}
}