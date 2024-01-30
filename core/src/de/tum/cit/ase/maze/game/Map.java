package de.tum.cit.ase.maze.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.ase.maze.utils.SpriteSheet;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class Map {

    int rows; // rows and cols for the size of our map grid
    int cols; // rows and cols for the size of our map grid
    SpriteSheet mapSheet; // creating spite sheet which will be used to choose tiles for our game
    int cellSize = 16; //defining our cell size

    Vector2 pos; //we use this to locate the cells or entities positions

    Cell entryCell; //our entry and exit points
    Cell exitCell;
    Cell[][] grid; //this will be our game map base
    List<Cell> emptyCells; //these are cells which do not contain anything, which are the floor
    Random random; // random number generator
    int keyCount = 0;

    public Map(String path,List<Entity> entities){
        mapSheet = new SpriteSheet(new Texture("basictiles.png"),15,8); //making basic tiles our sprite sheet

        emptyCells = new ArrayList<>();
        Properties prop = new Properties();
        cols = 0;
        rows = 0;
        random = new Random();

        try {
            //loading the properties file which contains the map information from the class path
            prop.load(new FileInputStream(path));

            // determining number of rows and cols based on the previously loaded file to be able to create the grid of game map
            for (java.util.Map.Entry<Object, Object> entry : prop.entrySet()) { // iterating through the properties file
                String key = (String) entry.getKey(); //retrieving the entry value
                try {
                    //parsing first part (row) if an error occurs the code moves onto the next interation
                    //parsing the input from properties file by splitting where the , is
                    Integer.parseInt(key.split(",")[0]);
                }catch (Exception e){
                    continue;
                }
                int col = Integer.parseInt(key.split(",")[0]);
                int row = Integer.parseInt(key.split(",")[1]);
                if(col > cols){
                    cols = col; // update the calue of columns and rows if the value is greater that way we have the correct number of columns and rows
                }
                if(row > rows){
                    rows = row;
                }
            }
            cols += 1;
            rows += 1;

            grid = new Cell[rows][cols]; // initializing the grid for our map based on the previously calculated row and column dimensions
            // using a nested for loop to go over the number of rows and columns to add them to the grid, currently our cell type is still null
            for(int row = 0; row < rows;row++){
                grid[row] = new Cell[cols];
                for (int col = 0; col < cols;col++){
                    grid[row][col] = new Cell(row,col,null);
                }
            }

            boolean c = false;
            for (java.util.Map.Entry<Object, Object> entry : prop.entrySet()) {
                String key = (String) entry.getKey();
                int value = Integer.valueOf((String) entry.getValue()); //getting the key value of the cell so cell type
                // in the first part we determine the number of rows and columns will have
                try {
                    // skip error format
                    Integer.parseInt(key.split(",")[0]);
                }catch (Exception e){
                    continue;
                }
                int col = Integer.parseInt(key.split(",")[0]);
                int row = Integer.parseInt(key.split(",")[1]);

                Cell cell = grid[row][col]; //initlaizing cell from cell class
                cell.cellType = CellType.fromId(value);
                //setting the current cells to either entry or exit point
                switch (cell.cellType){
                    case ENTRY_POINT:
                        entryCell = cell;
                        break;
                    case EXIT:
                        exitCell = cell;
                        break;
                    case ENEMY:
                        break;
                }
            }
            // if the current cell type is either null or enemy we make it a base cell so a floor piece
            for(int row = 0; row < rows;row++){
                for(int col = 0; col < cols;col++){
                    if(grid[row][col].cellType == null || grid[row][col].cellType == CellType.ENEMY){
                        emptyCells.add(grid[row][col]);
                    }
                }
            }

            for (java.util.Map.Entry<Object, Object> entry : prop.entrySet()) {
                String key = (String) entry.getKey();
                int value = Integer.valueOf((String) entry.getValue());
                try {
                    // skip error format
                    Integer.parseInt(key.split(",")[0]);
                }catch (Exception e){
                    continue;
                }
                int col = Integer.parseInt(key.split(",")[0]);
                int row = Integer.parseInt(key.split(",")[1]);

                Cell cell = grid[row][col];
                cell.cellType = CellType.fromId(value);
                switch (cell.cellType){
                    case ENEMY:
                        entities.add(new Slime(new Vector2(cell.col*16,cell.row*16),this));
                        break;
                    case KEY:
                        entities.add(new Key(new Vector2(cell.col*16,cell.row*16)));
                        keyCount++;
                        break;
                    case TRAP:
                        entities.add(new Trap(new Vector2(cell.col*16,cell.row*16)));
                        break;
                }
            }

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }


        pos = new Vector2( 0,0); //character position
    }
    // this method will be used to choose a cell and draw it so add its texture
    // batch is used for rendering
    private void drawCell( Batch batch,Cell cell, TextureRegion textureRegion){
        batch.draw(textureRegion, pos.x + cell.col * cellSize,pos.y + cell.row * cellSize,mapSheet.getWidth()/2,mapSheet.getHeight()/2,mapSheet.getWidth(),mapSheet.getHeight(),1,1,0);
    }

    public Cell getCell(int row,int col){
        return grid[row][col];
    } // returns a certain cell on the grid

    public int getKeyCount(){
        return keyCount;
    }
    //we will now use the texturize function to draw the elemets of our map, finally
    public void draw(Batch batch,Player player){

        batch.begin();

        for(int row = 0; row < rows;row++){
            for(int col = 0; col < cols;col++){
                Cell cell = grid[row][col];
                if(Vector2.dst(player.pos.x,player.pos.y,cell.col*16,cell.row*16) > 1500){
                    continue;
                }
                //rendering the cell type based on the frame from basic tiles
                if(cell.cellType == null){
                    drawCell(batch,cell,mapSheet.getTexture(11));
                }
                else {
                    switch (cell.cellType){
                        case WALL:
                            drawCell(batch,cell,mapSheet.getTexture(8));
                            break;
                        case ENTRY_POINT:
                            drawCell(batch,cell,mapSheet.getTexture(11));
                            break;
                        case EXIT:
                            drawCell(batch,cell,mapSheet.getTexture(36));
                            break;
                        case TRAP:
                        case ENEMY:
                        case KEY:
                            drawCell(batch,cell,mapSheet.getTexture(11));
                            break;
                    }
                }

            }
        }

        batch.end();
    }
    public Cell getEntryCell(){
        return entryCell;
    }
    public Cell getExitCell(){
        return exitCell;
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

    public void setCols(int cols) {
        this.cols = cols;
    }

}