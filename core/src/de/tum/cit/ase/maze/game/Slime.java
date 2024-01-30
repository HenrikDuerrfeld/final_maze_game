package de.tum.cit.ase.maze.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.ase.maze.pathfinding.PathFinding;
import de.tum.cit.ase.maze.utils.Rectangle;
import de.tum.cit.ase.maze.utils.SpriteSheet;

import java.util.ArrayList;
import java.util.List;

public class Slime extends Enemy{

    SpriteSheet sheet; //using spritesheet once again for slime animations

    //Connect the pathfinding class to the slime class to use its methods
    PathFinding pathFinding;
    List<Vector2> path; //nodes the slime will follow
    Vector2 target; // target which slime is following( player )
    Vector2 vel; //velocity of our slime
    float speed; //just the speed of slime
    int currentTargetIndex; // index of where player target is located on the path
    int scaleX = 1; // scaling the slime sprite on x axis
    Player player;

    public Slime(Vector2 pos, Map map) { //vector2 is the original position of the slime
        super(pos, null);
        sheet = new SpriteSheet(new Texture("slime.png"),1,4); // sprite sheet initialization using slime.png file for our animation
        sheet.setPlay(0, 3, 0.02f, true); // from spritesheet
        pathFinding = new PathFinding(map);

        currentTargetIndex = 0;
        speed = 1.0F;
        target = new Vector2(pos.x ,pos.y);
        vel = new Vector2(0,0);
        this.player = null;
        this.path = new ArrayList<>();
    }
    //The setPlayer method assigns a player to the slime if it doesn't already have one
    // and if the player is within a certain distance by using the code
    // (Vector2.dst(pos.x, pos.y, player.pos.x, player.pos.y) > 150).

    public void setPlayer(Player player){
        if(this.player != null){ //does slime already have a player assigned
            return;
        }

        // This line calculates the cell in the map corresponding to the player's position (destCell)
        // and then it uses the PathFinding instance to find a path from the current position of the slime to the player
        if(Vector2.dst(pos.x,pos.y,player.pos.x,player.pos.y) > 150){ // how far is slime from player
            return;
        }
        this.player = player;
        // we use pathfinding to calculate the new path to the player
        Cell destCell = pathFinding.getMap().getCell((int) player.getPos().y/16,(int) player.getPos().x/16);
        path = pathFinding.findPath((int)pos.x/16,(int)pos.y/16,destCell.col,destCell.row );
    }
    //this represents the collision box of slime, same as the methjod we used for the player
    @Override
    public Rectangle getRect(){
        return new Rectangle(pos.x + 2,pos.y + 2,sheet.getWidth() - 4,sheet.getHeight()/2 - 4);
    }
    // updates the slimes animation and position
    @Override
    public void update() {
        super.update();
        sheet.play(); //slime animation is being played


        if(Vector2.dst(pos.x,pos.y,target.x,target.y) <= 1.0){ //has slime reached target position
            // Update the target and velocity when reaching the current target position
            currentTargetIndex += 1;
            if(currentTargetIndex >= path.size()){

                if(this.player != null){
                    Cell destCell = pathFinding.getMap().getCell((int) player.getPos().y/16,(int) player.getPos().x/16);
                    path = pathFinding.findPath((int)pos.x/16,(int)pos.y/16,destCell.col,destCell.row );
                    currentTargetIndex = 0;
                }
                return;
            }

            target = path.get(currentTargetIndex);
            target = new Vector2(target.x * 16,target.y * 16);
            Vector2 dir = new Vector2(target.x - pos.x,target.y - pos.y).nor();
            vel = new Vector2(dir.x *speed,dir.y * speed);

        }
//        System.out.println("vel: " + target.x + ";" + target.y);
        pos = pos.add(vel);

        if(vel.x < 0){
            scaleX = -1;
        }
        else {
            scaleX = 1;
        }

    }
    // normal draw method which will justdraw our slime on screen
    @Override
    public void draw(Batch batch) {
        // draw currentFrame of player at position x y
        batch.begin();
        TextureRegion t = sheet.getCurrentFrame();
        batch.draw(t, pos.x ,pos.y ,sheet.getWidth()/2,sheet.getHeight()/2,sheet.getWidth(),sheet.getHeight(),scaleX,1,0);
        batch.end();
    }
}
