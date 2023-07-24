import java.util.Arrays;
import java.util.Iterator;

public class MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 8;
    private int size;
    private int numBuckets;
    private SList<Entry<K, V>>[] buckets;

    // Entry class to hold key-value pairs
    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    // Singly-linked list class to implement separate chaining
    // Update SList class to implement Iterable
    private static class SList<E> implements Iterable<E> {
        SListNode<E> head;
        int size;

        SList() {
            head = null;
            size = 0;
        }

        void addFirst(E data) {
            head = new SListNode<>(data, head);
            size++;
        }

        int size() {
            return size;
        }

        E remove(E target) {
            SListNode<E> prev = null;
            SListNode<E> current = head;

            while (current != null) {
                if (current.data.equals(target)) {
                    if (prev == null) {
                        head = current.next;
                    } else {
                        prev.next = current.next;
                    }
                    size--;
                    return current.data;
                }
                prev = current;
                current = current.next;
            }

            return null;
        }

        @Override
        public Iterator<E> iterator() {
            return new SListIterator<>(head);
        }
    }

    // Iterator for SList class
    private static class SListIterator<E> implements Iterator<E> {
        private SListNode<E> current;

        SListIterator(SListNode<E> head) {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            E data = current.data;
            current = current.next;
            return data;
        }
    }

    // Node class for singly-linked list
    private static class SListNode<E> {
        E data;
        SListNode<E> next;

        SListNode(E data, SListNode<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    public MyMap() {
        size = 0;
        numBuckets = INITIAL_CAPACITY;
        buckets = new SList[numBuckets];
        Arrays.fill(buckets, new SList<>());
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % numBuckets;
    }


    public void put(K key, V value) {
        int index = hash(key);
        SList<Entry<K, V>> bucket = buckets[index];
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
        bucket.addFirst(new Entry<>(key, value));
        size++;

        // Check if resize is needed
        if ((double) size / numBuckets > 0.75) {
            resize();
        }
    }

    public boolean contains(K key) {
        int index = hash(key);
        SList<Entry<K, V>> bucket = buckets[index];
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public V remove(K key) {
        int index = hash(key);
        SList<Entry<K, V>> bucket = buckets[index];

        // Iterate through the bucket to find the entry with the given key
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                // If the key is found, remove the entry from the bucket and update size
                bucket.remove(entry);
                size--;
                return entry.value;
            }
        }

        return null;
    }

    public V get(K key) {
        int index = hash(key);
        SList<Entry<K, V>> bucket = buckets[index];
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    public void resize() {
        numBuckets *= 2;
        SList<Entry<K, V>>[] newBuckets = new SList[numBuckets];
        Arrays.fill(newBuckets, new SList<>());

        for (SList<Entry<K, V>> bucket : buckets) {
            for (Entry<K, V> entry : bucket) {
                int newIndex = Math.abs(entry.key.hashCode()) % numBuckets;
                newBuckets[newIndex].addFirst(new Entry<>(entry.key, entry.value));
            }
        }

        buckets = newBuckets;
    }

}
