package data.structure.tree;


public class SplayTree<T extends Comparable<T>> implements BST<T> {

    public static void main(String[] args) {
        SplayTree<Integer> splayTree = new SplayTree<>();
        splayTree.insert(1);
        splayTree.display();
        splayTree.insert(2);
        splayTree.display();
        splayTree.insert(3);
        splayTree.display();
        splayTree.insert(4);
        splayTree.display();
        splayTree.insert(5);
        splayTree.display();
        splayTree.insert(0);
        splayTree.display();
        splayTree.remove(1);
        splayTree.display();
        System.out.println(splayTree.find(3));
        splayTree.display();
        System.out.println(splayTree.find(0));
        splayTree.display();
        System.out.println(splayTree.find(6));
        splayTree.display();
    }


    private Node<T> root;

    @Override
    public void insert(T elem) {
        if (root == null) {
            root = new Node<>(elem);
            return;
        }
        Node<T> current = root;
        while (true) {
            int cmp = current.value.compareTo(elem);
            if (cmp == 0) {
                splay(current);
                return;
            } else if (cmp < 0) {
                if (current.right == null) {
                    Node<T> node = new Node<>(elem);
                    node.parent = current;
                    current.right = node;
                    splay(node);
                    return;
                } else {
                    current = current.right;
                }
            } else {
                if (current.left == null) {
                    Node<T> node = new Node<>(elem);
                    node.parent = current;
                    current.left = node;
                    splay(node);
                    return;
                } else {
                    current = current.left;
                }
            }
        }
    }

    @Override
    public boolean find(T elem) {
        if (root == null || elem == null) {
            return false;
        }
        Node<T> result = find(root, elem);
        splay(result);
        return result.value.compareTo(elem) == 0;
    }

    /**
     *
     * @param node, non null
     * @param elem, the element to search
     * @return node if hit the node else the last non null node during find
     */
    private Node<T> find(Node<T> node, T elem) {
        int cmp = node.value.compareTo(elem);
        if (cmp < 0) {
            if (node.right == null) {
                return node;
            } else {
                return find(node.right, elem);
            }
        } else if (cmp > 0) {
            if (node.left == null) {
                return node;
            } else {
                return find(node.left, elem);
            }
        } else {
            return node;
        }
    }

    private void splay(Node<T> node) {
        if (node.parent == null) {
            root = node;
            return;
        }
        Node<T> grandparent = node.parent.parent;
        Node<T> parent = node.parent;
        if (grandparent == null) {
            // single layer splay
            if (parent.left == node) {
                parent.left = node.right;
                if (node.right != null) {
                    node.right.parent = parent;
                }
                parent.parent = node;
                node.right = parent;
                node.parent = null;
            } else if (parent.right == node) {
                parent.right = node.left;
                if (node.left != null) {
                    node.left.parent = parent;
                }
                parent.parent = node;
                node.left = parent;
                node.parent = null;
            }
        } else {
            // double layer splay
            if (grandparent.parent != null) {
                if (grandparent.parent.left == grandparent) {
                    grandparent.parent.left = node;
                } else {
                    grandparent.parent.right = node;
                }
            }
            node.parent = grandparent.parent;
            parent.parent = node;
            if (grandparent.left == parent && parent.left == node) {
                grandparent.left = parent.right;
                if (parent.right != null) {
                    parent.right.parent = grandparent;
                }
                grandparent.parent = parent;
                parent.right = grandparent;
                parent.left = node.right;
                if (node.right != null) {
                    node.right.parent = parent;
                }
                node.right = parent;
            } else if (grandparent.left == parent && parent.right == node) {
                grandparent.parent = node;
                parent.right = node.left;
                grandparent.left = node.right;
                if (node.left != null) {
                    node.left.parent = parent;
                }
                if (node.right != null) {
                    node.right = grandparent;
                }
                node.left = parent;
                node.right = grandparent;
            } else if (grandparent.right == parent && parent.left == node) {
                grandparent.parent = node;
                parent.left = node.right;
                grandparent.right = node.left;
                if (node.left != null) {
                    node.left.parent = grandparent;
                }
                if (node.right != null) {
                    node.right = parent;
                }
                node.left = grandparent;
                node.right = grandparent;
            } else if (grandparent.right == parent && parent.right == node) {
                grandparent.right = parent.left;
                if (parent.left != null) {
                    parent.left.parent = grandparent;
                }
                grandparent.parent = parent;
                parent.left = grandparent;
                parent.right = node.left;
                if (node.left != null) {
                    node.left.parent = parent;
                }
                node.left = parent;
            }
        }
        splay(node);
    }

    /**
     *
     * @param elem, the element to remove, remove by find the element first, make it be root if hit, else last node
     *              during the find. split the tree to [Left - (element-to-remove) - Right], if Left is null, the root
     *              is Right part, if non null, by find max of left, splay to root, [Left' - (max-of-Left) - null], then
     *              connect to Right part.
     */
    public void remove(T elem) {
        boolean found = find(elem);
        if (found) {
            // remove root
            Node<T> right = root.right;
            if (root.left == null) {
                root = root.right;
                root.parent = null;
            } else {
                root = root.left;
                root.parent = null;
                findMax();
                root.right = right;
                right.parent = root;
            }
        }
    }

    /**
     * find the max of tree, splay the max node
     */
    private void findMax() {
        if (root == null) {
            return;
        }
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        splay(current);
    }

    private static class Node<T> {
        Node<T> left;
        Node<T> right;
        Node<T> parent;
        T value;
        Node(T value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "[" + left +
                    ", " + value +
                    ", " + right +
                    ']';
        }
    }

    @Override
    public String toString() {
        return root == null ? null : root.toString();
    }

    private int depth() {
        return depth(root);
    }

    private int depth(Node<T> root) {
        if (root == null) {
            return 0;
        }
        int leftDepth = depth(root.left);
        int rightDepth = depth(root.right);
        return leftDepth > rightDepth ? leftDepth + 1 : rightDepth + 1;
    }

    private void display() {
        display(root, 0, new boolean[depth()]);
    }

    private void display(Node<T> node, int ident, boolean[] vec) {
        if (ident > 0) {
            for (int i = 0; i < ident - 1; i++) {
                System.out.print(vec[i] ? "|   " : "    ");
            }
            System.out.print(vec[ident - 1] ? "├── " : "└── ");
        }
        if (node == null) {
            System.out.println("(null)");
            return;
        }
        System.out.println(node.value);
        if (node.left == null && node.right == null) {
            return;
        }
        vec[ident] = true;
        display(node.left, ident + 1, vec);
        vec[ident] = false;
        display(node.right, ident + 1, vec);
    }
}
