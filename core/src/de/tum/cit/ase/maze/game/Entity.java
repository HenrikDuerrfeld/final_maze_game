package de.tum.cit.ase.maze.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.ase.maze.utils.Rectangle;

public class Entity {

    //Used to restrict accessing variables outside the package
    //While allowing classes of the same package and subclasses to access it
    protected Vector2 pos;

    Texture texture;

    public Entity(Vector2 pos, Texture texture) {
        this.pos = pos;
        this.texture = texture;
    }
    public void uodate(){

    }
    //gets the position of the player
    public Vector2 getPos() {
        return pos;
    }

    //sets the position of the player
    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    //This method returns a rectangle or a squaree that has a position and dimensions matching the position and
    // dimensions of the entity's texture which basically means it is creating a bounding box around the entity
    public Rectangle getRect(){
        return new Rectangle(pos.x,pos.y,texture.getWidth(),texture.getHeight());
    }

    //this method draws the player onto the batch using the coordinates x,y and the width, height of the texture of the player

    //the last two parameters control whether the texture should be flipped horizontally and vertically
    //in our case, both are set to false meaning no flipping

    //Despite this class not having any getters, getwidth and getheight is written as the methods getWidth() and getHeight()
    // are already a part of the Texture class in LibGDX
    public void draw(Batch batch){
        batch.begin();
        batch.draw(texture, pos.x, pos.y ,texture.getWidth(),texture.getHeight(),0,0,texture.getWidth(),texture.getHeight(),false,false);
        batch.end();
    }
}
