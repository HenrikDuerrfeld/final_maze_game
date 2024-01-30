package de.tum.cit.ase.maze.pathfinding;

public class Node {
    // Reference to the parent node in the path
    Node parent;

    // Row and column of the node in the maze
    int col;
    int row;

    // Cost values for pathfinding algorithms (g: cost from start, h: heuristic cost, distance: total cost)
    // heuristic cost = optimistic estimate of the cost from the current node to the goal/destination
    float g;
    float h;
    float distance;

    // Constructor to initialize a node with its position and parent node
    Node(int row, int col, Node parent) {
        this.row = row;
        this.col = col;
        this.parent = parent;

        // Initialize cost values to default values
        g = 0;
        h = 0;

        // Initialized as Max value as the total cost/distance should essentilly be unreachable or infinite
        distance = Integer.MAX_VALUE;
    }

    // Calculate and retrieve the total cost (f) of the node, total cost is the cost of distance and heuristic cost
    public float getF() {
        return g + h;
    }

    // Retrieve the cost from the start node (g)
    public float getG() {
        return g;
    }

    // Set the cost from the start node (g)
    public void setG(float g) {
        this.g = g;
    }

    // Retrieve the heuristic cost (h)
    public float getH() {
        return h;
    }

    // Set the heuristic cost (h)
    public void setH(float h) {
        this.h = h;
    }

    // Retrieve the parent node
    public Node getParent() {
        return parent;
    }

    // Set the parent node
    public void setParent(Node parent) {
        this.parent = parent;
    }

    //In the context of a pathfinding algorithm which works on a grid
    //these getters and setters manipulate the node positions within the grid
    //As in updating the position of nodes during exploration, checking the position of nodes in relation to the grid

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    // Retrieve the total cost of the node
    public float getDistance() {
        return distance;
    }

    // Set the total cost of the node
    public void setDistance(float distance) {
        this.distance = distance;
    }

    // Check if two nodes are equal based on their row and column indices
    public boolean equal(Node n) {
        return n.row == row && n.col == col;
    }
};