package de.tum.cit.ase.maze.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.ase.maze.utils.SpriteSheet;

public class Player extends Entity {
    float speed;
    SpriteSheet sheet;
    SpriteSheet walkSheet_down;
    SpriteSheet walkSheet_up;
    SpriteSheet walkSheet_left;
    SpriteSheet walkSheet_right;

    //represents the position of the player in the 2D game world
    //part of the LibGDX framework
    //used to represent 2D vectors, where x and y components denote the position in the horizontal and vertical directions
    Vector2 dir;
    Vector2 facingDir;

    public Player(Vector2 pos) {
        super(pos, null);

        // Initializing sheet with walkSheet_right initially makes the character start by looking to the right
        sheet = walkSheet_right;

        // player walk png was added to our asset folder as we didn't want to use all animations included in character png
        // player walk png is 4 x 4 grid png that only contains animations of the player moving up, down, left and right

        // rows 4 and columns 4 tells the program that our png is a 4 x 4 grid
        // using the setplay method from the spritesheet class we can tell the program to choose the animations from 0 to 3
        // which in this case are the walking down animations

        //0.1f sets the speed of the animation, each frame will be displayed for 0.1 seconds
        //true will allow the animation to loop repeating indefinitely.

        // the rest walksheet right, up and left follows the same logic

        walkSheet_down = new SpriteSheet(new Texture("player_walk.png"), 4, 4);
        walkSheet_down.setPlay(0, 3, 0.1f, true);

        walkSheet_right = new SpriteSheet(new Texture("player_walk.png"), 4, 4);
        walkSheet_right.setPlay(4, 7, 0.1f, true);

        walkSheet_up = new SpriteSheet(new Texture("player_walk.png"), 4, 4);
        walkSheet_up.setPlay(8, 11, 0.1f, true);

        walkSheet_left = new SpriteSheet(new Texture("player_walk.png"), 4, 4);
        walkSheet_left.setPlay(12, 15, 0.1f, true);

        speed = 2;

        // character direction/position is initialized as 0,0 coordinates
        dir = new Vector2(0, 0);

        // character facing direction is initialized as 1,0 coordinates initially starting into the game looking to the right
        facingDir = new Vector2(1, 0);
    }

    // onKeyUp is part 1 of the character movement, it is used for when the movement key is no longer pressed
    public void onKeyUp(int keycode) {

        // Here we use values of -1, 1 and 0, -1 being leftwards movement, 1 rightward movement and 0 meaning no horizontal movement
        // In the first case of the switch statements if (dir.x == -1) is the condition of releasing the left arrow key
        // Input.keys.Left indicates the character was moving leftwards initially however it is then set to dir.x = 0
        // stopping horizontal movement as in the character becomes stasis

        // the rest follows the same logic

        switch (keycode) {
            case Input.Keys.LEFT:
                if (dir.x == -1) {
                    dir.x = 0;
                }
            case Input.Keys.RIGHT:
                if (dir.x == 1) {
                    dir.x = 0;
                }
                break;
            case Input.Keys.UP:
                if (dir.y == 1) {
                    dir.y = 0;
                }
                break;
            case Input.Keys.DOWN:
                if (dir.y == -1) {
                    dir.y = 0;
                }
        }
    }


    // OnKeyDown is part 2 of the character movement, it is whn the movement keys are actually pressed

    // The code is quite simple when left arrow key is pressed indicated by the Input.keys.Left
    // the direction of the character is updated as a new Vector2 (-1, 0) once again -1 is leftward movement and 0 is no movement
    // the sheet which brings the animation of the character facing some direction is updated to walkSheet_left making the player
    // turn left
    // facingDir vector is set to the current direction which preserves the information about the facing direction
    public void onKeyDown(int keycode) {
        switch(keycode) {
            case Input.Keys.LEFT:
                dir = new Vector2(-1,0);
                sheet = walkSheet_left;
                facingDir = new Vector2(dir.x,dir.y);
                break;
            case Input.Keys.RIGHT:
                dir = new Vector2(1,0);
                sheet = walkSheet_right;
                facingDir = new Vector2(dir.x,dir.y);
                break;
            case Input.Keys.UP:
                dir = new Vector2(0,1);
                sheet = walkSheet_up;
                facingDir = new Vector2(dir.x,dir.y);
                break;
            case Input.Keys.DOWN:
                dir = new Vector2(0,-1);
                sheet = walkSheet_down;
                facingDir = new Vector2(dir.x,dir.y);
                break;
        }
    }

}




