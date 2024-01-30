package de.tum.cit.ase.maze.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

// making class trap and defining it as a type entity
public class Trap extends Entity {
    public Trap(Vector2 pos) {
        super(pos, new Texture("trap.png"));
    }
}