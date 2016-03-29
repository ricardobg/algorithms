import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    
    private int size;
    private Item[] itens;
    
    public RandomizedQueue()                 // construct an empty randomized queue
    {
        size = 0;
        itens = (Item[]) new Object[2];
    }
    
    public boolean isEmpty()                 // is the queue empty?
    {
        return size == 0;
    }
    
    public int size()                        // return the number of items on the queue
    {
        return size;
    }
    
    private void resize(int capacity) {
        Item[] tempItens = (Item[]) new Object[capacity];
        
        for (int i = 0; i < size; i++)
            tempItens[i] = itens[i];
        
        itens = tempItens;
    }
    
    public void enqueue(Item item)           // add the item
    {
        if (item == null)
            throw new NullPointerException();
        
        if (size == itens.length)
            resize(itens.length * 2);
        
        itens[size++] = item;
    }
    
    public Item dequeue()                    // remove and return a random item
    {
        if (size == 0)
            throw new NoSuchElementException();
        
        int position = StdRandom.uniform(size);
        Item returnItem = itens[position];
        
        itens[position] = itens[--size];
        itens[size] = null;
        
        if (size == itens.length / 4 && itens.length > 2)
            resize(itens.length / 2);
        
        return returnItem;
        
    }
    
    public Item sample()                     // return (but do not remove) a random item
    {
        if (size == 0)
            throw new NoSuchElementException();
        return itens[StdRandom.uniform(size)];
    }
    
    private class RandomIterator implements Iterator<Item>
    {
        private Item[] itens;
        private int size;
        
        private RandomIterator() {
            size = RandomizedQueue.this.size;
            itens = (Item[]) new Object[size];
            for (int i = 0; i < size; i++)
                itens[i] = RandomizedQueue.this.itens[i];
        }
        @Override
        public boolean hasNext() {
            return size != 0;
        }

        @Override
        public Item next() {
            if (size == 0)
                throw new NoSuchElementException();
            
            int position = StdRandom.uniform(size);
            Item returnItem = itens[position];
            
            itens[position] = itens[--size];
            itens[size] = null;
            
            return returnItem;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    public Iterator<Item> iterator()         // return an independent iterator over items in random order
    {
        return new RandomIterator();
    }
    
    public static void main(String[] args)   // unit testing
    {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        for (int i = 0; i < 10; i++)
            queue.enqueue(i);
        for (int item : queue)
            System.out.println(item);
        for (int i = 0; i < 10; i++)
            System.out.println(queue.dequeue());
    }
}