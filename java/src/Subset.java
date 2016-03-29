import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Subset {

    public static void main(String[] args) {
        
        int k = Integer.parseInt(args[0]);
        
        String[] strings = StdIn.readAllStrings();
        
        Deque<String> deque = new Deque<>();
        for (int i = 0; i < k; i++) {
            int position = StdRandom.uniform(strings.length - i);
            deque.addFirst(strings[position]);
            strings[position] = strings[strings.length - 1 - i];
            strings[strings.length - 1 - i] = null;
               
        }
        
        while (k-- > 0)
            System.out.println(deque.removeFirst());
        
        
    }
}
