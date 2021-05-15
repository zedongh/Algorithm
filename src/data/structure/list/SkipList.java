package data.structure.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class SkipList<K extends Comparable<K>, V> {

    private static final Random random = new Random();

    /**
     * placeholder node
     */
    private final Node<K, V> header;

    private int size;

    private int getMaxLevel() {
        return header == null ? 0 : header.next.length;
    }

    @SuppressWarnings("unchecked")
    public SkipList() {
        header = new Node<>(null, null);
        header.next = (Node<K, V>[]) new Node<?, ?>[1];
    }

    public V search(K key) {
        Node<K, V> current = header;
        for (int i = getMaxLevel() - 1; i >= 0; i--) {
            while (current.next[i] != null && key.compareTo(current.next[i].key) > 0) {
                current = current.next[i];
            }
        }
        if (current.next[0] == null || !Objects.equals(current.next[0].key, key)) {
            return null;
        } else {
            return current.next[0].value;
        }
    }

    @SuppressWarnings("unchecked")
    public void insert(K key, V value) {

        Node<K, V>[] updated = (Node<K, V>[]) new Node<?, ?>[getMaxLevel()];

        Node<K, V> current = header;
        for (int level = getMaxLevel() - 1; level >= 0; level--) {
            while (current.next[level] != null && key.compareTo(current.next[level].key) > 0) {
                current = current.next[level];
            }
            updated[level] = current;
        }

        if (Objects.nonNull(current.next[0]) && Objects.equals(current.next[0].key, key)) {
            // found key, replace & done
            current.value = value;
            return;
        }

        int k = randomLevel();
        Node<K, V> node = new Node<>(key, value);
        node.next = (Node<K, V>[]) new Node<?, ?>[k];
        if (updated.length < k) {
            Node<K, V>[] next = (Node<K, V>[]) new Node<?, ?>[k];
            System.arraycopy(header.next, 0, next, 0, header.next.length);
            header.next = next;

            Node<K, V>[] temp = (Node<K, V>[]) new Node<?, ?>[k];
            for (int i = updated.length; i < k; i++) {
                temp[i] = header;
            }
            System.arraycopy(updated, 0, temp, 0, updated.length);
            updated = temp;
        }
        for (int level = k - 1; level >= 0; level--) {
            // [level] -> ...
            // prev
            // [level] -> [level] -> ...
            // prev       node
            Node<K, V> prev = updated[level];
            node.next[level] = prev.next[level];
            prev.next[level] = node;
        }
        size++;
    }

    @SuppressWarnings("unchecked")
    public V delete(K key) {
        Node<K, V>[] updated = (Node<K, V>[]) new Node<?, ?>[getMaxLevel()];

        Node<K, V> current = header;
        for (int level = getMaxLevel() - 1; level >= 0; level--) {
            while (current.next[level] != null && key.compareTo(current.next[level].key) > 0) {
                current = current.next[level];
            }
            updated[level] = current;
        }

        if (Objects.isNull(current.next[0]) || !Objects.equals(current.next[0].key, key)) {
            // not found key, done
            return null;
        }
        Node<K, V> deletedNode = current.next[0];
        for (int i = 0; i < deletedNode.next.length; i++) {
            updated[i].next[i] = deletedNode.next[i];
        }
        int level;
        for (level = header.next.length - 1; level > 0; level--) {
            if (Objects.nonNull(header.next[level])) {
                break;
            }
        }
        Node<K, V>[] next = (Node<K, V>[]) new Node<?, ?>[level + 1];
        System.arraycopy(header.next, 0, next, 0, next.length);
        header.next = next;
        size--;
        return current.value;
    }

    public int size() {
        return size;
    }

    private int randomLevel() {
        int level = 1;
        while (random.nextDouble() < 0.5) {
            level++;
            if (level > size() / 2) {
                return size() / 2;
            }
        }
        return level;
    }

    private static class Node<K, V> {
        Node<K, V>[] next;
        K key;
        V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "[" + "next=" + Arrays.toString(next) + ", (" + key + ", " + value + ")]";
        }
    }

    @Override
    public String toString() {
        if (this.header == null) {
            return "";
        }

        Node<K, V> current = this.header;
        List<String[]> matrix = new ArrayList<>();
        while (current != null) {
            String[] strings = new String[getMaxLevel()];
            for (int j = 0; j < current.next.length; j++) {
                strings[j] = String.format("[%4s]", current.key);
            }
            for (int j = current.next.length; j < getMaxLevel(); j++) {
                strings[j] = "";
            }
            matrix.add(strings);
            current = current.next[0];
        }
        StringBuilder sb = new StringBuilder();
        for (int j = matrix.get(0).length - 1; j >= 0; j--) {
            for (int i = 0; i < matrix.size(); i++) {
                sb.append(String.format("%6s", matrix.get(i)[j])).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        SkipList<Integer, Integer> sk = new SkipList<>();
        Set<Integer> iSet = new HashSet<>();
        for (int i = 0; i < 30; i++) {
            sk.insert(i, i);
            iSet.add(i);
        }
        System.out.println(sk);
        for (Integer i : iSet) {
            System.out.printf("==================delete %s====================%n", i);
            sk.delete(i);
            System.out.println(sk);
        }
    }
}

