package de.tum.cit.ase.maze.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Slime extends Entity {
    public Slime(Vector2 pos) {
        super(pos, new Texture("slime.png"));
    }
}
