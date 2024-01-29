package de.tum.cit.ase.maze.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.ase.maze.utils.SpriteSheet;

import java.io.FileInputStream;
import java.util.*;
import static de.tum.cit.ase.maze.game.CellType.*;

public class Map {
    private int rows; // rows and columns for the size of our map grid
    private int cols; // rows and columns for the size of our map grid
    private SpriteSheet mapSheet; // creating spite sheet which will be used to choose tiles for our game
    int cellSize = 16; //defining our cell size
    Cell entryCell; //our entry and exit points
    Cell exitCell;
    Cell[][] grid; //this will be our game map base
    List<Cell> baseCells; //these are cells which do not contain anything, which are the floor
    Random random; // random number generator
    int keyCount = 0;
    Vector2 position; //we use this to locate the cells or entities positions

    public Map(String path, List<Entity> entities) {
        mapSheet = new SpriteSheet(new Texture("basictiles.png"), 15, 8); //making basic tiles our sprite sheet
        baseCells = new ArrayList<>();
        cols = 0;
        rows = 0;
        random = new Random();
        Properties properties = new Properties();

        try {
            //loading the properties file which contains the map information from the class path
            properties.load(new FileInputStream(path));
            for (java.util.Map.Entry<Object, Object> entry : properties.entrySet()) { // iterating through the properties file
                String key = (String) entry.getKey(); //retrieving the entry value
                System.out.println(key);
                // in the first part we determine the number of rows and columns will have
                try {
                    //parsing the input from properties file by splitting where the , is
                    Integer.parseInt(key.split(",")[0]);
                } catch (Exception exception) {
                    continue;
                }
                int col = Integer.parseInt(key.split(",")[0]); //parsing for the column
                int row = Integer.parseInt(key.split(",")[1]); // parsing for the row
                if (col > cols) {
                    cols = col; // update the calue of columns and rows if the value is greater that way we have the correct number of columns and rows
                }
                if (row > rows) {
                    rows = row;
                }
            }
            cols += 1;
            rows += 1;
            grid = new Cell[rows][cols]; // initializing the grid for our map based on the previously calculated row and column dimensions
            // using a nested for loop to go over the number of rows and columns to add them to the grid, currently our cell type is still null
            for (int row = 0; row < rows; row++) {
                grid[row] = new Cell[cols];
                for (int col = 0; col < cols; col++) {
                    grid[row][col] = new Cell(row, col, null);
                }
            }
            boolean c = false;
            // in this part we iterate through .properties and also take the value of the xy coordinates and assign the cell type
            for (java.util.Map.Entry<Object, Object> entry : properties.entrySet()) { // iterating through the properties file
                String key = (String) entry.getKey(); //retrieving the entry value
                System.out.println(key);
                int value = Integer.valueOf((String) entry.getValue()); //getting the key value of the cell so cell type
                // in the first part we determine the number of rows and columns will have
                try {
                    //parsing the input from properties file by splitting where the , is
                    Integer.parseInt(key.split(",")[0]);
                } catch (Exception exception) {
                    continue;
                }
                int col = Integer.parseInt(key.split(",")[0]); //parsing for the column
                int row = Integer.parseInt(key.split(",")[1]); // parsing for the row
                Cell cell = grid[row][col]; //initlaizing cell from cell class
                cell.cellType = CellType.getValue(value);
                //setting the current cells to either entry or exit point
                switch (cell.cellType) {
                    case ENTRY_POINT:
                        entryCell = cell;
                        break;
                    case EXIT:
                        exitCell = cell;
                        break;
                    case ENEMY:
                        break;
                    case KEY:
                        break;
                    case TRAP:
                        break;
                }
                System.out.println(cell.cellType);
            }
            // if the current cell type is either null or enemy we make it a base cell so a floor piece
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    if (grid[row][col].cellType == null || grid[row][col].cellType == CellType.ENEMY) {
                        baseCells.add(grid[row][col]);
                    }
                }
            }


        } catch (Exception exception) {
            exception.printStackTrace();
        }
        position = new Vector2(0, 0); //character position
    }

        // this method will be used to choose a cell and draw it so add its texture
    // batch is used for rendering
    private void texturize(Batch batch, Cell cell, TextureRegion textureRegion){
        batch.draw(textureRegion, position.x + cell.col * cellSize,position.y + cell.row * cellSize,mapSheet.getWidth()/2,mapSheet.getHeight()/2,mapSheet.getWidth(),mapSheet.getHeight(),1,1,0);
    }
    //we will now use the texturize function to draw the elemets of our map, finally
    public void draw(Batch batch, Player player){
        batch.begin();
        for(int row = 0; row < rows;row++) {
            for (int col = 0; col < cols; col++) {
                Cell cell = grid[row][col];
                if(Vector2.dst(player.pos.x,player.pos.y,cell.col*16,cell.row*16) > 1500){
                    continue;
                }
                //rendering the cell type based on the frame from basic tiles
                if(cell.cellType == null){
                    texturize(batch,cell,mapSheet.getTexture(11));
                }
                else {
                    switch (cell.cellType) {
                        case WALL:
                            texturize(batch, cell, mapSheet.getTexture(8));
                            break;
                        case ENTRY_POINT:
                            texturize(batch, cell, mapSheet.getTexture(11));
                            break;
                        case EXIT:
                            texturize(batch, cell, mapSheet.getTexture(36));
                            break;
                        case TRAP:
                        case ENEMY:
                        case KEY:
                            texturize(batch, cell, mapSheet.getTexture(11));
                            break;
                    }
                }
            }
        }
        batch.end();
    }

    //getters and setters
    public Cell getCell(int row, int column){
        return grid[row][column]; // returns a certain cell on the grid
    }
    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int columns) {
        this.cols = columns;
    }

    public SpriteSheet getMapSheet() {
        return mapSheet;
    }

    public void setMapSheet(SpriteSheet mapSheet) {
        this.mapSheet = mapSheet;
    }

    public int getCellSize() {
        return cellSize;
    }

    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }

    public Cell getEntryCell() {
        return entryCell;
    }

    public void setEntryCell(Cell entryCell) {
        this.entryCell = entryCell;
    }

    public Cell getExitCell() {
        return exitCell;
    }

    public void setExitCell(Cell exitCell) {
        this.exitCell = exitCell;
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public void setGrid(Cell[][] grid) {
        this.grid = grid;
    }

    public List<Cell> getBaseCells() {
        return baseCells;
    }

    public void setBaseCells(List<Cell> baseCells) {
        this.baseCells = baseCells;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public int getKeyCount() {
        return keyCount;
    }

    public void setKeyCount(int keyCount) {
        this.keyCount = keyCount;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }
}
