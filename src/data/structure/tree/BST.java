package data.structure.tree;

public interface BST<T extends Comparable<T>> {
    void insert(T elem);
    boolean find(T elem);
    void remove(T elem);
}
