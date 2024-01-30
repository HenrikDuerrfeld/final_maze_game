package de.tum.cit.ase.maze.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
// projectile class which is also a type of entity is the players bullets
public class PlayerProjectile extends Entity {
    Vector2 dir; //direction which the bullet will follow
    float speed = 5; // speed of the bullets
    public PlayerProjectile(Vector2 pos, Vector2 dir) {
        super(pos, new Texture("playerProjectile.png"));
        this.dir = dir;
    }

    @Override
    public void update() { // updates the position of the bullet based on the velocity
        super.update();
        Vector2 vel = new Vector2(dir.x * speed,dir.y * speed);
        pos = pos.add(vel);
    }

    public void draw(Batch batch){// as per usual draw method draws the projectile at its current location
        batch.begin();
        batch.draw(texture, pos.x, pos.y ,texture.getWidth(),texture.getHeight(),0,0,texture.getWidth(),texture.getHeight(),false,false);
        batch.end();
    }

}
