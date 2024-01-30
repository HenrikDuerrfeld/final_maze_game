package de.tum.cit.ase.maze.pathfinding;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.ase.maze.game.CellType;
import de.tum.cit.ase.maze.game.Map;

import java.util.ArrayList;
import java.util.List;

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



    void exploreNeighbour(Node node) {
        int dir_row[] = { -1, 1, 0, 0 };
        int dir_col[] = { 0, 0, -1, 1 };
        //int dir_row[4] = { -1, 1, 0, 0 };
        //int dir_col[4] = { 0, 0, -1, 1 };

        for (int i = 0; i < 4; i++) {
            int new_row = dir_row[i] + node.getRow();
            int new_col = dir_col[i] + node.getCol();

            if (new_row > map.getRows() - 1 || new_row < 0 || new_col > map.getCols() - 1 || new_col < 0) {
                continue;
            }
            if (map.getCell(new_row,new_col).cellType == CellType.WALL) {
                continue;
            }
            if (isInCloseList(new_row, new_col) != null) {
                continue;
            }

            Node new_node = new Node(new_row, new_col, node);
            if (isInOpenList(new_node) != null) {
                new_node = isInOpenList(new_node);
            }

            float path_to_this_neightbour = node.getG() + 1;

            if (path_to_this_neightbour < new_node.getG() || isInOpenList(new_node) == null) {
                // is mud

                new_node.setG(path_to_this_neightbour);
                new_node.setH(grid_distance(new_node, endNode));

                //graphData->changeType(new_node->getRow(), new_node->getCol(), 1);
                if (isInOpenList(new_node) == null) {
                    open_list.add(new_node);
                }
            }
        }

    }

    List<Vector2> showPath(Node node) {
        Node tmp = node;
        List<Node> tracepath = new ArrayList<>();
        List<Vector2> path = new ArrayList<>();
        while (tmp != null)
        {
            tracepath.add(tmp);
            tmp = tmp.getParent();
        }
        for (int i = tracepath.size() - 1; i >= 0; i--) {
            Node n = tracepath.get(i);
            path.add(new Vector2(n.getCol(), n.getRow()));
        }

        if(path.size() > 0){
            path.remove(0);
        }

        return path;
    }


    public List<Vector2> findPath(int col,int row, int toCol,int toRow) {
        startNode = new Node(row,col,null);
        endNode = new Node(toRow,toCol,null);

        open_list = new ArrayList<>();
        close_list = new ArrayList<>();

        open_list.add(startNode);
        while (open_list.size() != 0) {

            //Sleep(1);
            int i_min = findMinFNode();
            Node currentNode = open_list.get(i_min);


            close_list.add(currentNode);
            if (currentNode.equal(endNode)) {
                return showPath(currentNode);
            }

            open_list.remove(i_min);
            exploreNeighbour(currentNode);
        }
        return new ArrayList<>();
    }

};