package de.tum.cit.ase.maze.game;

public enum CellType {
    // defining the types of cells we have
    TRAP,
    WALL,
    ENTRY_POINT,
    KEY,
    EXIT,
    ENEMY;

    // giving cell types the value which will be present in the .properties file
    public static CellType fromId(int x){
        switch(x) {
            case 0:
                return WALL;
            case 1:
                return ENTRY_POINT;
            case 2:
                return EXIT;
            case 3:
                return TRAP;
            case 4:
                return ENEMY;
            case 5:
                return KEY;
        }
        return null;
    }

}
