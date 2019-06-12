package data.structure.tree;

public class BinarySearchTree<T extends Comparable<T>> implements BST<T> {
    protected Node<T> root;

    public static void main(String[] args) {
        BinarySearchTree<Integer> binarySearchTree = new BinarySearchTree<>();
        binarySearchTree.insert(1);
        binarySearchTree.insert(3);
        binarySearchTree.insert(2);
        binarySearchTree.insert(0);
        System.out.println(binarySearchTree);
        binarySearchTree.remove(1);
        System.out.println(binarySearchTree);
        System.out.println(binarySearchTree.find(3));
        System.out.println(binarySearchTree);
    }

    public void insert(T elem) {
        root = insert(root, elem);
    }

    @Override
    public boolean find(T elem) {
        if (elem == null) {
            return false;
        }
        Node<T> result = find(root, elem);
        return result != null;
    }

    private Node<T> find(Node<T> node, T elem) {
        if (node == null) {
            return null;
        }
        int cmp = node.value.compareTo(elem);
        if (cmp < 0) {
            return find(node.right, elem);
        } else if (cmp > 0) {
            return find(node.left, elem);
        } else {
            return node;
        }
    }

    private Node<T> insert(Node<T> node, T elem) {
        if (node == null) {
            return new Node<>(elem);
        }
        int cmp = node.value.compareTo(elem);
        if (cmp < 0) {
            node.right = insert(node.right, elem);
        } else if (cmp > 0) {
            node.left = insert(node.left, elem);
        } else {
            node.value = elem;
        }
        return node;
    }

    public void remove(T elem) {
        root = remove(root, elem);
    }

    private Node<T> remove(Node<T> node, T elem) {
        if (node == null) {
            return null;
        }
        int cmp = node.value.compareTo(elem);
        if (cmp < 0) {
            node.right = remove(node.right, elem);
            return node;
        } else if (cmp > 0) {
            node.left = remove(node.left, elem);
            return node;
        } else {
            if (node.right == null) {
                return node.left;
            } else if (node.left == null) {
                return node.right;
            } else if (node.right.left == null) {
                node.right.left = node.left;
                return node.right;
            } else {
                Node<T> parent = node.right;
                while (parent.left.left != null) {
                    parent = parent.left;
                }
                node.value = parent.left.value;
                parent.left = parent.left.right;
                return node;
            }
        }
    }

    public Node<T> rotateLeft(Node<T> node) {
        if (node.right != null) {
            Node<T> q = node.right;
            node.right = q.left;
            q.left = node;
            return q;
        } else {
            return node;
        }
    }

    public Node<T> rotateRight(Node<T> node) {
        if (node.left != null) {
            Node<T> p = node.left;
            node.left = p.right;
            p.right = node;
            return p;
        } else {
            return node;
        }
    }

    @Override
    public String toString() {
        if (root == null) {
            return "[]";
        }
        return root.toString();
    }

    private static class Node<T> {
        Node<T> left;
        Node<T> right;
        T value;

        Node(T value) {
            if (value == null) {
                throw new IllegalArgumentException("Node init with null value.");
            }
            this.value = value;
        }

        @Override
        public String toString() {
            String l = left == null ? "" : left.toString();
            String r = right == null ? "" : right.toString();
            return "[" + l + ", " + value + ", " + r + "]";
        }
    }
}
