package de.tum.cit.ase.maze.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class PlayerProjectile extends Entity { //instance of entity as it is a moving object
    Vector2 dir; //dir defines the direction of the projectile using vector 2
    float speed = 6; // bullet movement speed
    public PlayerProjectile(Vector2 pos, Vector2 dir) {
        super(pos, new Texture("playerProjectile.png"));
        this.dir = dir;
    }

    @Override
    public void update() { //this will update the location of the bullet based on calculating the velocity as a product of direction and speed
        super.update();
        Vector2 vel = new Vector2(dir.x * speed,dir.y * speed);
        pos = pos.add(vel);
    }

    public void draw(Batch batch){
        batch.begin();
        batch.draw(texture, pos.x, pos.y ,texture.getWidth(),texture.getHeight(),0,0,texture.getWidth(),texture.getHeight(),false,false);
        batch.end();
    }

}
