import edu.princeton.cs.algs4.StdIn;

public class Subset {

    public static void main(String[] args) {
        
        int k = Integer.parseInt(args[0]);
        
        String[] strings = StdIn.readAllStrings();
        RandomizedQueue<String> randomQueue = new RandomizedQueue<>();
        for (String s : strings)
            randomQueue.enqueue(s);
        
        while (k-- > 0)
            System.out.println(randomQueue.dequeue());
        
        
    }
}
