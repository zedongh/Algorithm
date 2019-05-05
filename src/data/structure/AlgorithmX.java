package data.structure;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class AlgorithmX {

    public static void main(String[] args) {
        InputStream is = AlgorithmX.class.getClassLoader().getResourceAsStream("./matrix.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        List<String> strings = reader.lines().collect(Collectors.toList());
        Iterator<String> iterator = strings.iterator();
        int n = Integer.valueOf(iterator.next());
        int m = Integer.valueOf(iterator.next());
        System.out.println(n);
        System.out.println(m);
        int[][] matrix = new int[n][m];
        for (int i = 0; i < n; i++) {
            String[] splits = iterator.next().trim().split(" +");
            List<Integer> list = Arrays.stream(splits).map(Integer::valueOf).collect(Collectors.toList());
            for (int j = 0; j < m; j++) {
                matrix[i][j] = list.get(j);
            }
        }
        DanceLink dlx = new DanceLink(matrix);
        System.out.println(dlx);
        System.out.println("===============");
        dlx.search();
        dlx.printSolution();
        System.out.println("===============");
        System.out.println(dlx);
    }

    static class Cell {
        Cell up, down, left, right;
        Cell head;
        int row;
        int col;

        Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public String toString() {
            return "[" + row + ", " + col + "]";
        }
    }

    static class DanceLink {
        Cell root;
        Cell[] headers;
        private Stack<Cell> solution = new Stack<>();

        DanceLink(int[][] matrix) {
            int n = matrix.length;
            int m = matrix[0].length;
            initHeader(m);
            initMatrix(matrix, n, m);
        }

        void initHeader(int m) {
            root = new Cell(-1, -1);
            headers = new Cell[m];
            headers[0] = new Cell(-1, 0);
            root.right = headers[0];
            headers[0].left = root;
            for (int i = 1; i < m; i++) {
                headers[i] = new Cell(-1, i);
                headers[i].left = headers[i - 1];
                headers[i - 1].right = headers[i];
            }
            headers[m - 1].right = root;
            root.left = headers[m - 1];
        }

        void initMatrix(int[][] matrix, int n, int m) {
            Cell[] cells = new Cell[m];
            for (int i = 0; i < m; i++) {
                cells[i] = headers[i];
            }

            for (int i = 0; i < n; i++) {
                Cell row = new Cell(i, -1);
                Cell cur = row;
                for (int j = 0; j < m; j++) {
                    if (matrix[i][j] != 0) { // 1
                        Cell cell = new Cell(i, j);
                        cell.head = headers[j];
                        cell.up = cells[j];
                        cells[j].down = cell;
                        cur.right = cell;
                        cell.left = cur;
                        cur = cell;
                        cells[j] = cell;
                    }
                }
                if (cur != row) { // not all zero
                    row.right.left = cur;
                    cur.right = row.right;
                    // clear row
                    row.left = null;
                    row.right = null;
                }
            }

            for (int i = 0; i < m; i++) {
                cells[i].down = headers[i];
                headers[i].up = cells[i];
            }
        }

        public void printSolution() {
            for (Cell cell : solution) {
                System.out.print(cell.row + " ");
            }
            System.out.println();
        }

        public void search() {
            if (root.right == root) {
                printSolution();
            } else {
                // column selection
                Cell column = root.right;
                cover(column);
                for (Cell down = column.down; down != column; down = down.down) {
                    solution.push(down);

                    for (Cell right = down.right; right != down; right = right.right) {
                        cover(right.head);
                    }

                    search();

                    solution.pop();

                    for (Cell left = down.left; left != down; left = left.left) {
                        uncover(left.head);
                    }
                }
                uncover(column);
            }
        }

        public void cover(Cell column) {
            // unlink left & right
            column.right.left = column.left;
            column.left.right = column.right;

            for (Cell down = column.down; down != column; down = down.down) {
                for (Cell right = down.right; right != down; right = right.right) {
                    // unlink up & down
                    right.up.down = right.down;
                    right.down.up = right.up;
                }
            }
        }

        public void uncover(Cell column) {
            for (Cell up = column.up; up != column; up = up.up) {
                for (Cell left = up.left; left != up; left = left.left) {
                    // unlink up & down
                    left.up.down = left;
                    left.down.up = left;
                }
            }
            // unlink left & right
            column.right.left = column;
            column.left.right = column;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Cell header = root.right; header != root; header = header.right) {
                for (Cell cell = header.down; cell != header; cell = cell.down) {
                    sb.append(cell);
                }
                sb.append("\n");
            }
            return sb.toString();
        }
    }
}