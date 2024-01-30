package de.tum.cit.ase.maze.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Key extends Entity {
    public Key(Vector2 pos) {
        super(pos, new Texture("key.png"));
    }
}