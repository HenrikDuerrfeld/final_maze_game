package de.tum.cit.ase.maze.pathfinding;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.ase.maze.game.CellType;
import de.tum.cit.ase.maze.game.Map;

import java.util.ArrayList;
import java.util.List;

public class PathFinding {
    List<Node> open_list;
    List<Node> close_list;
    Map map;

    Node startNode;
    Node endNode;

    int distanceType = 0;

    public PathFinding(Map map) {
        this.map = map;
    }

    public Map getMap(){
        return map;
    }

    Node isInOpenList(Node node) {
        for (Node n : open_list) {
            if (n.equal(node)) {
                return n;
            }
        }
        return null;
    }
    Node isInCloseList(int row, int col) {
        for (Node n : close_list) {
            if (n.getRow() == row && n.getCol() == col) {
                return n;
            }
        }
        return null;
    }

    int findMinFNode() {
        float min = 999999999;
        // Node* minF_node = nullptr;
        int index = 0;
        for (int i = 0; i < open_list.size(); i++) {
            if (open_list.get(i).getF() < min) {
                min = open_list.get(i).getF();
                // minF_node = open_list[i];
                index = i;
            }
        }
        return index;
    }

//    static float euclidean_distance(Node* node1, Node* node2) {
//        return sqrt((node2->getRow() - node1->getRow()) * (node2->getRow() - node1->getRow()) + (node2->getCol() - node2->getCol()) * (node2->getCol() - node2->getCol()));
//    }
//
//    static float manhattan_distance(Node* node1, Node* node2) {
//        return abs(node2->getRow() - node1->getRow()) + abs(node2->getCol() - node1->getCol());
//    }

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