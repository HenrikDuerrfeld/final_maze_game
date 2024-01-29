package de.tum.cit.ase.maze.game;

import de.tum.cit.ase.maze.utils.Rectangle;
public class Cell {
    public int row; //this will be the index of the row a certain cell will be in same for column
    public int col;


    public CellType cellType; //this is the cell type the cell will have defined in cell type enum class

    public Cell(int row, int column, CellType cellType) {
        this.row = row;
        this.col = column;
        this.cellType = cellType;
    }
    public Rectangle getSquare(){
        return new Rectangle(col * 16,row * 16,16,16); //defining cell size based on the square class
    }
}
