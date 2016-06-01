import java.awt.Color;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {

	private static final double BORDER_ENERGY = 1000;

	private class PicturePoint {
		private int x;
		private int y;

		public PicturePoint(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	private class PictureDigraph {
		private Picture picture;

		PictureDigraph(Picture picture) {
			this.picture = new Picture(picture);
		}
	}

	private PictureDigraph picture;

	public SeamCarver(Picture picture)
	{
		if (picture == null)
			throw new NullPointerException();
		this.picture = new PictureDigraph(picture);
	}

	private Picture pic()
	{
		return picture.picture;
	}

	public Picture picture()
	{
		return new Picture(pic());
	}

	public int width()
	{
		return pic().width();
	}

	public int height()
	{
		return pic().height();
	}


	private double energyDifference(int x1, int y1, int x2, int y2) {
		Color c1 = pic().get(x1, y1);
		Color c2 = pic().get(x2, y2);

		int blue = c1.getBlue() - c2.getBlue();
		int red = c1.getRed() - c2.getRed();
		int green = c1.getGreen() - c2.getGreen();

		return blue*blue + red*red + green*green;

	}

	public double energy(int x, int y)
	{
		if (x < 0 || x >= pic().width() || y < 0 || y >= pic().height())
			throw new IndexOutOfBoundsException();
		if (x == 0 || x == pic().width() - 1 || y == 0 || y == pic().height() - 1)
			return BORDER_ENERGY;
		return Math.sqrt(energyDifference(x - 1, y, x + 1, y) + energyDifference(x, y - 1, x, y + 1));
	}


	private void relaxEdges(boolean horizontal, int x, int y, double[][] distances, PicturePoint[][] from) {
		double cmpEnergy = distances[x][y] + energy(x, y);
		if (horizontal) {
			for (int j = y - 1; j <= y + 1 && j < height(); j++) {
				if (distances[x + 1][j] > cmpEnergy) {
					distances[x + 1][j] = cmpEnergy;
					from[x + 1][j] = new PicturePoint(x, y);
				}
			}
		}
		else {
			for (int i = x - 1;  i <= x + 1 && i < width(); i++) {
				if (distances[i][y + 1] > cmpEnergy) {
					distances[i][y + 1] = cmpEnergy;
					from[i][y + 1] = new PicturePoint(x, y);
				}
			}
		}
	}

	private int[] findSeam(boolean horizontal) {
		double[][] distances = new double[width()][height()];
		PicturePoint[][] from = new PicturePoint[width()][height()];
		int[] result;
		for (int i = 0; i < distances.length; i++)
			for (int j = 0; j < distances[i].length; j++) {
				if ((horizontal && i == 0) || (!horizontal && j == 0))
					distances[i][j] = 0;
				else
					distances[i][j] = Double.MAX_VALUE;
				from[i][j] = new PicturePoint(i, j);
			}
		if (horizontal) {
			//Consider vertices in in topological order
			for (int x = 0; x < width() - 1; x++)
				for (int y = 1; y < height() - 1; y++)
					relaxEdges(horizontal, x, y, distances, from);

			//Done!
			//Find smaller distance
			int y = 0;
			double minDist = Double.MAX_VALUE;
			for (int i = 1; i < height() - 1; i++) {
				if (distances[width() - 1][i] < minDist) {
					minDist = distances[width() - 1][i];
					y = i; 
				}
			}
			result = new int[width()];
			int x = width() - 1;
			while (x >= 0) {
				result[x] = y;
				y = from[x][y].y;
				x--;
			}
		}
		else  {
			//Consider vertices in in topological order
			for (int y = 0; y < height() - 1; y++)
				for (int x = 1; x < width() - 1; x++)
					relaxEdges(horizontal, x, y, distances, from);

			//Done!
			//Find smaller distance
			int x = 0;
			double minDist = Double.MAX_VALUE;
			for (int i = 1; i < width() - 1; i++) {
				if (distances[i][height() - 1]  < minDist) {
					minDist = distances[i][height() - 1];
					x = i; 
				}
			}
			result = new int[height()];
			int y = height() - 1;
			while (y >= 0) {
				result[y] = x;
				x = from[x][y].x;
				y--;
			}
		}
		return result;
	}

	public int[] findHorizontalSeam()
	{
		return findSeam(true);
	}

	public int[] findVerticalSeam()
	{
		return findSeam(false);
	}

	public void removeHorizontalSeam(int[] seam)
	{
		if (seam == null)
			throw new NullPointerException();
		if (seam.length != width())
			throw new IllegalArgumentException();
		int last = seam[0];
		for (int i = 0; i < seam.length; i++) {
			if (seam[i] > last + 1 || seam[i] < last - 1 || seam[i] < 0 || seam[i] >= height())
				throw new IllegalArgumentException();
			last = seam[i];
		}
		Picture p = new Picture(width(), height() - 1);
		for (int x = 0; x < width(); x++) {
			for (int y = 0; y < height(); y++) {
				if (y < seam[x])
					p.set(x, y, pic().get(x, y)); 
				else if (y > seam[x])
					p.set(x, y - 1, pic().get(x, y));
			}
		}
		this.picture.picture = p;

	}
	public void removeVerticalSeam(int[] seam)
	{
		if (seam == null)
			throw new NullPointerException();
		if (seam.length != height())
			throw new IllegalArgumentException();
		int last = seam[0];
		for (int i = 0; i < seam.length; i++) {
			if (seam[i] > last + 1 || seam[i] < last - 1 || seam[i] < 0 || seam[i] >= width())
				throw new IllegalArgumentException();
			last = seam[i];
		}
		Picture p = new Picture(width() - 1, height());
		for (int y = 0; y < height(); y++) {
			for (int x = 0; x < width(); x++) {
				if (x < seam[y])
					p.set(x, y, pic().get(x, y)); 
				else if (x > seam[y])
					p.set(x - 1, y, pic().get(x, y));
			}
		}
		this.picture.picture = p;
	}

	private void print() {
		StdOut.println("Image " + width() + "x" + height());
		for (int y = 0; y < height(); y++) {
			for (int x = 0; x < width(); x++) {
				StdOut.printf("%9.2f", energy(x, y));
			}
			StdOut.println();
		}
	}
	private void printPixels() {
		StdOut.println("Image " + width() + "x" + height());
		for (int y = 0; y < height(); y++) {
			for (int x = 0; x < width(); x++) {
				StdOut.printf("(%3d, %3d, %3d)  ", 
						pic().get(x, y).getRed(), pic().get(x, y).getGreen(), pic().get(x, y).getBlue());
			}
			StdOut.println();
		}
	}

	private void printHorizontalSeam(int[] seam) {
		StdOut.println("Image " + width() + "x" + height());
		for (int y = 0; y < height(); y++) {
			for (int x = 0; x < width(); x++) {
				if (seam[x] == y)
					StdOut.printf("%8.2f-", energy(x, y));
				else
					StdOut.printf("%9.2f", energy(x, y));
			}
			StdOut.println();
		}
	}

	private void printVerticalSeam(int[] seam) {
		StdOut.println("Image " + width() + "x" + height());
		for (int y = 0; y < height(); y++) {
			for (int x = 0; x < width(); x++) {
				if (seam[y] == x)
					StdOut.printf("%8.2f-", energy(x, y));
				else
					StdOut.printf("%9.2f", energy(x, y));
			}
			StdOut.println();
		}
	}

	public static void main(String[] args) {
		Picture p = new Picture(args[0]);
		SeamCarver carver = new SeamCarver(p);
		carver.print();
		int[] hor = carver.findHorizontalSeam();
		carver.printHorizontalSeam(hor);
		carver.printPixels();
		carver.removeHorizontalSeam(hor);
		carver.printPixels();
		int[] ver = carver.findVerticalSeam();
		carver.printVerticalSeam(ver);
		carver.printPixels();
		carver.removeVerticalSeam(ver);
		carver.printPixels();
	}
}