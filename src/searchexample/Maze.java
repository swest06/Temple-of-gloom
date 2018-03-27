package searchexample;/* Finding the solution out of a maze.

   This problem illustrates searching using stacks (depth-first search)
   and queues (breadth-first search).
*/


import java.util.LinkedList;
import java.util.Stack;


public class Maze {

    final static char C = ' ', X = 'x', S = 's', E = 'e', V = '.';

    final static int START_I = 1, START_J = 1;
    final static int END_I = 2, END_J = 9;
    private static char[][] maze = {
            {X, X, X, X, X, X, X, X, X, X},
            {X, S, C, C, C, C, C, C, C, X},
            {X, C, C, C, X, C, X, X, C, E},
            {X, C, X, X, X, C, X, X, C, X},
            {X, C, C, C, C, X, X, X, C, X},
            {X, X, X, X, C, X, X, X, C, X},
            {X, X, X, X, C, X, C, C, C, X},
            {X, X, C, X, C, X, X, C, C, X},
            {X, X, C, C, C, C, C, C, C, X},
            {X, X, X, X, X, X, C, X, X, X}
    };

    public static void main(String[] args) {

        Maze maze = new Maze();
        maze.print();

        System.out.println("\n\nFind a path using a stack: ");
        maze.solveStack();

        System.out.println("\n\nFind a path using a queue: ");
        maze.solveQueue();

        System.out.println("\n\nFind a path recursively: ");
        maze.solveRec();

    }

    public int size() {
        return maze.length;
    }

    public void print() {
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                System.out.print(maze[i][j]);
                System.out.print(' ');
            }
            System.out.println();
        }
    }

    public char mark(int i, int j, char value) {
        assert (isInMaze(i, j));
        char tmp = maze[i][j];
        maze[i][j] = value;
        return tmp;
    }

    public char mark(MazePos pos, char value) {
        return mark(pos.i(), pos.j(), value);
    }

    public boolean isMarked(int i, int j) {
        assert (isInMaze(i, j));
        return (maze[i][j] == V);
    }

    public boolean isMarked(MazePos pos) {
        return isMarked(pos.i(), pos.j());
    }

    public boolean isClear(int i, int j) {
        assert (isInMaze(i, j));
        return (maze[i][j] != X && maze[i][j] != V);
    }

    public boolean isClear(MazePos pos) {
        return isClear(pos.i(), pos.j());
    }

    //true if cell is within maze
    public boolean isInMaze(int i, int j) {
        if (i >= 0 && i < size() && j >= 0 && j < size()) return true;
        else return false;
    }

    //true if cell is within maze
    public boolean isInMaze(MazePos pos) {
        return isInMaze(pos.i(), pos.j());
    }

    public boolean isFinal(int i, int j) {
        return (i == Maze.END_I && j == Maze.END_J);
    }

    public boolean isFinal(MazePos pos) {
        return isFinal(pos.i(), pos.j());
    }

    public char[][] clone() {
        char[][] mazeCopy = new char[size()][size()];
        for (int i = 0; i < size(); i++)
            for (int j = 0; j < size(); j++)
                mazeCopy[i][j] = maze[i][j];
        return mazeCopy;
    }

    public void restore(char[][] savedMaze) {
        for (int i = 0; i < size(); i++)
            for (int j = 0; j < size(); j++)
                maze[i][j] = savedMaze[i][j];
    }


    //THE GOAL IS TO FIND A PATH FROM START TO END

    //**************************************************
    //this solution uses a stack to keep track of possible
    //states/positions to explore; it marks the maze to remember the
    //positions that it's already explored.
    public void solveStack() {

        //save the maze
        char[][] savedMaze = clone();

        //declare the locations stack
        Stack<MazePos> candidates = new Stack<MazePos>();

        //insert the start
        candidates.push(new MazePos(START_I, START_J));

        MazePos crt, next;
        while (!candidates.empty()) {

            //get current position
            crt = candidates.pop();

            if (isFinal(crt)) break;

            //mark the current position
            mark(crt, V);

            //put its neighbors in the queue
            next = crt.north();
            if (isInMaze(next) && isClear(next)) candidates.push(next);
            next = crt.east();
            if (isInMaze(next) && isClear(next)) candidates.push(next);
            next = crt.west();
            if (isInMaze(next) && isClear(next)) candidates.push(next);
            next = crt.south();
            if (isInMaze(next) && isClear(next)) candidates.push(next);
        }

        if (!candidates.empty()) {
            System.out.println("Found a solution: ");
        } else {
            System.out.println("You're stuck in the maze!");
        }
        print();


        //restore the maze
        restore(savedMaze);
    }


    //**************************************************
    //this solution uses a QUEUE to keep track of possible
    //states/positions to explore; it marks the maze to remember the
    //positions that it's already explored.
    public void solveQueue() {

        //save the maze
        char[][] savedMaze = clone();

        //declare the locations stack
        LinkedList<MazePos> candidates = new LinkedList<>();

        //insert the start
        candidates.add(new MazePos(START_I, START_J));

        MazePos crt, next;
        while (!candidates.isEmpty()) {

            //get current position
            crt = candidates.removeFirst();

            if (isFinal(crt)) break;

            //mark the current position
            mark(crt, V);

            //put its neighbors in the queue
            next = crt.north();
            if (isInMaze(next) && isClear(next)) candidates.add(next);
            next = crt.east();
            if (isInMaze(next) && isClear(next)) candidates.add(next);
            next = crt.west();
            if (isInMaze(next) && isClear(next)) candidates.add(next);
            next = crt.south();
            if (isInMaze(next) && isClear(next)) candidates.add(next);
        }

        if (!candidates.isEmpty()) {
            System.out.println("Found a solution: ");
        } else {
            System.out.println("You're stuck in the maze!");
        }
        print();

        //restore the maze
        restore(savedMaze);
    }


    //************************************************** solve using
    //recursion. Note: this solution unmarks the path upon reaching
    //dead ends, so in the end only the path is left marked. It is
    //possible to write a solution that does not un-mark its traces,
    //but instead it clones and restores the maze.
    public void solveRec() {

        if (solve(new MazePos(START_I, START_J))) {
            System.out.println("Found a solution: ");
        } else {
            System.out.println("You're stuck in the maze.");
        }
        print();
    }


    //find a path to exit the maze from this position. Works
    //recursively, by advancing to a neighbor and continuing from
    //there. If a path is found, return true. Otherwise return false.
    public boolean solve(MazePos pos) {

        //base case
        if (!isInMaze(pos)) return false;
        if (isFinal(pos)) return true;
        if (!isClear(pos)) return false;

        //current position must be clear
        assert (isClear(pos));

        //recurse

        //first mark this  location
        mark(pos, V);

        //try to go south
        if (solve(pos.south())) {
            //we found a solution going south: if we want to leave the
            //maze clean, then unmark current cell and return; if we
            //want to mark the path in the maze, then don't unmark
            //mark(pos, C);
            return true;
        }

        //else west
        if (solve(pos.west())) {
            //we found a solution going west: if we want to leave the
            //maze clean, then unmark current cell and return; if we
            //want to mark the path in the maze, then don't unmark
            //return
            //mark(pos, C);
            return true;
        }

        //else north
        if (solve(pos.north())) {
            //we found a solution going north: if we want to leave the
            //maze clean, then unmark current cell and return; if we
            //want to mark the path in the maze, then don't unmark
            //return
            // mark(pos, C);
            return true;
        }

        //else east
        if (solve(pos.east())) {
            //we found a solution going east: if we want to leave the
            //maze clean, then unmark current cell and return; if we
            //want to mark the path in the maze, then don't unmark
            //return
            //mark(pos, C);
            return true;
        }

        //unmark all dead ends; since it was marked, the position must
        //have been clear
        mark(pos, C);

        //if none of the above returned, then there is no solution
        return false;
    }
}


class MazePos {
    int i, j;

    public MazePos(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int i() {
        return i;
    }

    public int j() {
        return j;
    }

    public void print() {
        System.out.println("(" + i + "," + j + ")");
    }

    public MazePos north() {
        return new MazePos(i - 1, j);
    }

    public MazePos south() {
        return new MazePos(i + 1, j);
    }

    public MazePos east() {
        return new MazePos(i, j + 1);
    }

    public MazePos west() {
        return new MazePos(i, j - 1);
    }
}
