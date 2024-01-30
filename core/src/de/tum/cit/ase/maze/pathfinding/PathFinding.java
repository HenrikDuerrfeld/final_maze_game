package de.tum.cit.ase.maze.pathfinding;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.ase.maze.game.CellType;
import de.tum.cit.ase.maze.game.Map;

import java.util.ArrayList;
import java.util.List;

// https://www.youtube.com/watch?v=Hd0D68guFKg tutorial on pathfinding for 2d java games

public class PathFinding {

    // A list to store open nodes during pathfinding
    List<Node> open_list;

    //A list to store closed nodes during pathfinding
    List<Node> close_list;

    //This represents the game map itself
    Map map;

    //starting and ending nodes for pathfinding
    Node startNode;
    Node endNode;
    int distanceType = 0;

    //constructor initializes the map while taking map as its parameter
    public PathFinding(Map map) {
        this.map = map;
    }

    public Map getMap(){
        return map;
    }

    //Helper methods below which checks if a given node/coordinates are present in the open or closed list

    Node isInOpenList(Node node) {
        for (Node n : open_list) {
            if (n.equal(node)) {
                return n;
            }
        }
        return null;
    }

    // Different to the above method closelist takes int row and int column as it's parameters
    // This is because it allows you to check for the presence of a node in the closelist based on
    // its position without necessarily having a reference to the exact Node object

    // essentially it's more flexable this way, given Passing row and column as parameters avoids the
    // creation of a new Node object just for the purpose of checking its presence in the closelist
    Node isInCloseList(int row, int col) {
        for (Node n : close_list) {
            if (n.getRow() == row && n.getCol() == col) {
                return n;
            }
        }
        return null;
    }

    // crucial method in pathfinding it's purpose is to find the index of the node in the openlist that has the minimum F value

    // float min: A variable to store the minimum F value found so far. It is initialized to a large value
    // int index: this is used to store the location/position of the node in openlist that has the minimum F value
    int findMinFNode() {
        float min = 999999999;
        int index = 0;

        // for loop which loops through the openlist with the nodes
        for (int i = 0; i < open_list.size(); i++) {

            // For each node in the loop, check if its F value is smaller than the current minimum
            if (open_list.get(i).getF() < min) {
                // if true, update the minimum and store the index of this node
                min = open_list.get(i).getF();
                index = i;
            }
        }
        return index;
    }

    // These are two static methods for calculating distances between two nodes in a grid-based environment

    // Euclidean distance

//    static float euclidean_distance(Node* node1, Node* node2) {
//        return sqrt((node2->getRow() - node1->getRow()) * (node2->getRow() - node1->getRow()) + (node2->getCol() - node2->getCol()) * (node2->getCol() - node2->getCol()));
//    }

    // This method calculates the Euclidean distance between two nodes in a grid
    // The Euclidean distance is the straight-line distance between two points in a two-dimensional space

    // Simply put what it does is obtains the difference in rows and difference in columns
    // obtains the sum of them and square roots it
    // resulting in the Euclidean distance btween two nodes

    // Manhattan distance

//    static float manhattan_distance(Node* node1, Node* node2) {
//        return abs(node2->getRow() - node1->getRow()) + abs(node2->getCol() - node1->getCol());
//        }

   // This method calculates the manhattan distance between two nodes in a grid
    // It does so by obtaining the difference in rows and difference in columns and adding them together
    // obtaining the absolute difference, reflecting the distance one would travel in a gridd
    // only moving along the grid lines as in horizontally and vertically


    // The difference between the Euclidean distance and manhatttan is that
    // it tends to be a more optimistic estimate of the actual cost or distance and considers
    // diagonal movement whereas the Manhattan distance is more conservative with its estimate and
    // does not consider diagonal movement
    // in our code we chose to use manhattan distance with a bit of a twist

    // In this method you can see that the manhattan distance was actually implemented but with a twist,
    // given it calculates the absolute difference of both columns and rows
    // and depending on whether the horizontal distance is greater than the vertical distance (dst_x > dst_y)
    // it either calculates the distance as 5 × dst_y + 3 × ( dst_x − dst_y )
    // or as 5 × dst_x + 3 × ( dst_y − dst_x )
    // this is because the algorithm when horizontal distance is greater deems vertical movement more favorable
    // thus multiplying it by 5 and the remaining horizontal distance by 3 and the other scenario is vice versa
    // this method basically calculates the heuristic distance from one node (a) to another node (b)
    // in a grid-based environment

    static float grid_distance(Node a, Node b)
    {
        int dst_x = Math.abs(a.getCol() - b.getCol());
        int dst_y = Math.abs(a.getRow() - b.getRow());

        if (dst_x > dst_y)
        {
            return 5 * dst_y + 3 * (dst_x - dst_y);
        }
        return 5 * dst_x + 3 * (dst_y - dst_x);
    }

// This function explores neighboring nodes of the current node and evaluates based on various
// conditions whether if those nodes are the nodes with least cost considering both the actual
// cost from the start and the heuristic estimate to the goal

    void exploreNeighbour(Node node) {

        // These arrays represent the possible directions (up, down, left, right)
        // that can be explored from the current node
        // dir_row contains the changes in row indices, and dir_col contains the changes in column indices
        int dir_row[] = { -1, 1, 0, 0 };
        int dir_col[] = { 0, 0, -1, 1 };

        // This loop iterates through the 4 possible directions
        // For each direction, it calculates the new row and column indices
        // based on the current node's position

        for (int i = 0; i < 4; i++) {
            int new_row = dir_row[i] + node.getRow();
            int new_col = dir_col[i] + node.getCol();

            // This checks if the calculated indices are within the bounds of the map
            // if not it skips the current iteration ensuring that the algorithm does not explore outside the map

            // new_row > map.getRows() - 1 says if the calculated new row is greater than the maximum allowable row index
            // it means the position goes beyond the last row of the map upwards
            //new_row < 0 says if the row is less than 0 it goes below the first row of the map
            //column works the same way, more than the maximum allowed column or less than 0 both means it's outside the map

            if (new_row > map.getRows() - 1 || new_row < 0 || new_col > map.getCols() - 1 || new_col < 0) {
                continue;
            }

            // This checks if the cell at the new indices is a wall
            // if it is, the algorithm skips the current iteration, avoiding exploration of walls

            if (map.getCell(new_row,new_col).cellType == CellType.WALL) {
                continue;
            }

            // This checks if the neighboring node (or the row and column coordinates) is in the closed list
            // meaning it has already been explored and if it is in the closed list the iteration is skipped

            if (isInCloseList(new_row, new_col) != null) {
                continue;
            }

            // initially creates a new node to represent the neighbouring node
            // and if that node is within the openlist it returns a non-null value
            // and the line reassigns new_node to be the existing node in the open list
            // simply put the code is saying "If this node is already in the open list, let's use the existing one"
            // which is an attempt to avoid adding duplicate nodes to the open list

            Node new_node = new Node(new_row, new_col, node);
            if (isInOpenList(new_node) != null) {
                new_node = isInOpenList(new_node);
            }

            // Calculates the cost from the starting node to the current neighboring node
            // which then updates the G value of the neighbor with this cost

            float path_to_this_neightbour = node.getG() + 1;

            // This checks if the calculated cost for the new neightboring node is less than the existing cost of
            // the neighboring node or if the neighboring node is not in the open list

            if (path_to_this_neightbour < new_node.getG() || isInOpenList(new_node) == null) {

                // This updates the G (accumulated cost from the start node)
                // and H (heuristic estimate to the end node) values of the neighboring node

                new_node.setG(path_to_this_neightbour);
                new_node.setH(grid_distance(new_node, endNode));

                //if the newly created neighboring node is not in the openlist then
                // it is added to the open list for further exploration

                if (isInOpenList(new_node) == null) {
                    open_list.add(new_node);
                }
            }
        }
    }

    // This method generates and returns a list of Vector2 points representing the path from
    // a given end node to the starting node. It does so by traversing backward from the end node
    // to the start node, collecting each node in the tracepath list, then it converts the nodes
    // in tracepath to Vector2 points and adds them to the path list. It also removes the first
    // element from the path list first element being the starting position/node and finally returns
    // the final path list containing Vector2 points representing the path from the start to the end node

    List<Vector2> showPath(Node node) {

        // This initializes a temporary node tmp with the input of a node
        // which is used to traverse backward from the end node to the start node

        Node tmp = node;

        // Intializes a list called tracepath to store the nodes encountered
        // while traversing backwards

        List<Node> tracepath = new ArrayList<>();

        // Another initialization of a list called path to store the final path (the nodes)
        // as Vector2 objects

        List<Vector2> path = new ArrayList<>();

        // This loop traverses backward from the end node to the start node by adding each
        // node to the tracepath list initialized above and updates tmp to its parent in each iteration
        // by using the getParent node getter
        while (tmp != null)
        {
            tracepath.add(tmp);
            tmp = tmp.getParent();
        }

        // this code converts the nodes found while traversing backwards into vector2 coordinates
        // and adds them to the path list. The loop also iterates in reverse order to maintain
        // the correct order of the path

        for (int i = tracepath.size() - 1; i >= 0; i--) {
            Node n = tracepath.get(i);
            path.add(new Vector2(n.getCol(), n.getRow()));
        }

        // This code checks if the path list has elements beyond the starting node
        // and if so also removes the first element

        if(path.size() > 0){
            path.remove(0);
        }

        return path;
    }

    // This is the main method of the pathfinding class which starts from a specified location (col, row)
    // and searches for a path to another location (toCol, toRow). It uses the A* algorithm with open and
    // closed lists to find the most efficient path.
    // If a path is found it returns a list of Vector2 points representing the path
    // and returns an empty arraylist when the while loop stops looping as it indicates exploration of the nodes is done

    public List<Vector2> findPath(int col,int row, int toCol,int toRow) {

        // Initializes the start and end nodes with the provided row and column indices
        // however the startNode has no parent initially, as it's the starting point
        startNode = new Node(row,col,null);
        endNode = new Node(toRow,toCol,null);

        // Initializes two lists the open_list for nodes to be explored and
        // close_list for nodes that have been explored and won't be revisited

        open_list = new ArrayList<>();
        close_list = new ArrayList<>();

        // Adds the startNode to the open_list to initiate the pathfinding process

        open_list.add(startNode);

        // This is a While Loop which continues as long as there are nodes in the open_list to explore
        while (open_list.size() != 0) {

            // Finds the node in the open_list with the minimum F value (combination of G and H values) using the
            // findMinFNode() method mentioned above

            int i_min = findMinFNode();

            // This line of code then replaces the current node with that node
            Node currentNode = open_list.get(i_min);

            // Adds the current node to the close_list since it is being explored
            close_list.add(currentNode);

            // Checks if the current node is the end/goal node
            // if it is it returns the path from the start to
            // the goal node using the showPath method

            if (currentNode.equal(endNode)) {
                return showPath(currentNode);
            }

            //Removes the current node from the open_list since it has been explored
            // and then explores its neighbors by calling the exploreNeighbour method from above

            open_list.remove(i_min);
            exploreNeighbour(currentNode);
        }

        // Finally it returns an empty list when the open_list becomes empty and no path is found
        return new ArrayList<>();
    }

};