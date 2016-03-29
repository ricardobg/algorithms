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
        
        boolean sentinel() {
            return false;
        }
    }
    
    private class SentinelNode extends Node {
        @Override
        boolean sentinel() {
            return true;
        }
    }
    
    public Deque()                           // construct an empty deque
    {
        //Creates sentinel node
        first = new SentinelNode();
        last = first;
        first.next = first;
        first.previous = first;
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
        node.next = first.next;
        node.previous = first;
        first.next.previous = node;
        first.next = node;
        if (first.sentinel())
            last = node;
        first = node;
        
        size++;
    }
    
    public void addLast(Item item)           // add the item to the end
    {
        if (item == null)
            throw new NullPointerException();
        Node node = new Node();
        node.value = item;
        node.previous = last.previous;
        node.next = last;
        last.previous.next = node;
        last.previous = node;
        if (last.sentinel())
            first = node;
        last = node;
        
        size++;
    }
    
    public Item removeFirst()                // remove and return the item from the front
    {
        if (first.sentinel())
            throw new NoSuchElementException();
        
        Node oldFirst = first;        
        first.previous.next = first.next;
        first.next.previous = first.previous;
        first = first.previous;
        size--;
        if (size == 0)
            last = first;
        return oldFirst.value;
        
    }
    
    public Item removeLast()                 // remove and return the item from the end
    {
        if (last.sentinel())
            throw new NoSuchElementException();
        
        Node oldLast = last;
        last.next.previous = last.previous;
        last.previous.next = last.next;
        last = oldLast.next;                
        size--;
        if (size == 0)
            first = last;
        return oldLast.value;
    }
    
    private class DequeIterator implements Iterator<Item> {
        
        private Node current = first;
        
        @Override
        public boolean hasNext() {
            return !current.sentinel();
        }

        @Override
        public Item next() {
            if (current.sentinel())
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
        System.out.println("Size:");
        System.out.println(deque.size());
        for (int i : deque) {
            System.out.println(deque.removeFirst());
        }
    }
}
