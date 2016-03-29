import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    
    private Node first, last;
    private int size;
    /**
     * Node for linked list
     * @author ricardo
     *
     */
    private class Node {
        private Item value;
        private Node next;
        private Node previous;
    }
    
    public Deque()                           // construct an empty deque
    {
        first = last = null;
        size = 0;
    }
    
    public boolean isEmpty()                 // is the deque empty?
    {
        return size  == 0;
    }
    
    public int size()                        // return the number of items on the deque
    {
        return size;
    }
    
    public void addFirst(Item item)          // add the item to the front
    {
        if (item == null)
            throw new NullPointerException();
        Node node = new Node();
        node.value = item;
        node.next = null;
        node.previous = null;
        if (first == null) {
            first = last = node;
        }
        else {
            node.previous = first;
            first.next = node;
            first = node;
        }
        size++;
    }
    
    public void addLast(Item item)           // add the item to the end
    {
        if (item == null)
            throw new NullPointerException();
        Node node = new Node();
        node.value = item;
        node.next = null;
        node.previous = null;
        if (last == null) {
            last = first = node;
        }
        else {
            node.next = last;
            last.previous = node;
            last = node;
        }
        size++;
    }
    
    public Item removeFirst()                // remove and return the item from the front
    {
        if (first == null)
            throw new NoSuchElementException();
        
        Node oldFirst = first;
        first = oldFirst.previous;
        if (first != null)
            first.next = null;
        else
            last = null;
        
        size--;
        return oldFirst.value;
        
    }
    
    public Item removeLast()                 // remove and return the item from the end
    {
        if (last == null)
            throw new NoSuchElementException();
        
        Node oldLast = last;
        last = oldLast.next;
        if (last != null)
            last.previous = null;
        else
            first = null;
        
        size--;
        return oldLast.value;
    }
    
    private class DequeIterator implements Iterator<Item> {
        
        private Node current = first;
        
        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (current == null)
                throw new NoSuchElementException();
            Node oldCurrent = current;
            current = oldCurrent.previous;
            return oldCurrent.value;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    public Iterator<Item> iterator()         // return an iterator over items in order from front to end
    {
        return new DequeIterator();
    }
    
    public static void main(String[] args)   // unit testing
    {
        Deque<Integer> deque = new Deque<Integer>();
        for (int i = 0; i < 10; i++) {
            deque.addFirst(i);
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(deque.removeFirst());
        }
        for (int i = 0; i < 10; i++) {
            deque.addLast(i);
        }
        System.out.println(deque.size());
        for (int i : deque) {
            System.out.println(i);
        }
    }
}
