package homework1;

public class MyHashMap<K, V> {
    private int capacity;
    private int size = 0;
    private static final double LOAD_FACTOR = 0.75;
    private Node[] table;

    public MyHashMap() {
        this.capacity = 16;
        this.table = new Node[capacity];
    }

    public MyHashMap(int capacity) {
        this.capacity = capacity;
        this.table = new Node[capacity];
    }

    public MyHashMap(MyHashMap<K, V> another) {
        this.capacity = another.capacity;
        this.table = new Node[capacity];
        for (int i = 0; i < another.capacity; i++) {
            Node<K, V> current = another.table[i];
            while (current != null) {
                this.put(current.key, current.value);
                current = current.next;
            }
        }
    }

    public int size() {
        return size;
    }

    public void put(K key, V value) {
        if (key == null) {
            return;
        }

        if (size >= capacity * LOAD_FACTOR) resize();

        int bucketIndex = getBucketIndex(key);
        if (this.table[bucketIndex] == null) {
            this.table[bucketIndex] = new Node<K, V>(key, value);
            size++;
            return;
        }

        Node<K, V> current = table[bucketIndex];
        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return;
            }
            if (current.next == null) break;
            current = current.next;
        }

        current.next = new Node<>(key, value);
        size++;
    }

    public void remove(K key) {

        int bucketIndex = getBucketIndex(key);
        Node<K, V> current = table[bucketIndex];

        if (current != null && current.key.equals(key)) {
            table[bucketIndex] = current.next;
            size--;
            return;
        }

        Node<K, V> prev = null;
        while (current != null) {
            if (current.key.equals(key)) {
                prev.next = current.next;  // Перелинковываем
                size--;
                return;
            }
            prev = current;
            current = current.next;
        }
    }

    public V get(K key) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> current = table[bucketIndex];
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;

        for (int i = 0; i < table.length; i++) {
            Node<K, V> current = table[i];
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.key).append("=").append(current.value);
                first = false;
                current = current.next;
            }
        }

        sb.append("}");
        return sb.toString();
    }

    private void resize() {
        capacity = capacity << 1;
        Node<K, V>[] newTable = new Node[capacity];

        for (int i = 0; i < table.length; i++) {
            Node<K, V> current = table[i];
            while (current != null) {
                int newIndex = getBucketIndex(current.key);
                Node<K, V> next = current.next;

                current.next = newTable[newIndex];
                newTable[newIndex] = current;

                current = next;
            }
        }
        table = newTable;
    }

    private int getBucketIndex(K key) {
        if (key == null) return 0;
        return Math.abs(key.hashCode()) % capacity;
    }

    static class Node<K, V> {

        final K key;
        V value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
