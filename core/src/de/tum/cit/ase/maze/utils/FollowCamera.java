package de.tum.cit.ase.maze.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.ase.maze.game.Map;
// we are using this class to follow our player
public class FollowCamera {
    OrthographicCamera camera;
    Map map;

    public FollowCamera(OrthographicCamera camera,  Map map){
        this.camera = camera;
        this.map =map;
        camera.zoom = 0.2f;
    }
    // our follow method will follow the charcter by using vector2 by setting the camera postion to the player position
    public void follow(Vector2 target){
        float centerX = camera.viewportWidth * camera.zoom * 0.5f;
        float centerY = camera.viewportHeight * camera.zoom * 0.5f;

        camera.position.x = target.x;
        camera.position.y = target.y;

        if(camera.position.y < centerY){
            camera.position.y = centerY;
        }
        else if(camera.position.y > map.getRows()* 16 - centerY ){
            camera.position.y = map.getRows()* 16 - centerY ;
        }
        if(camera.position.x < centerX){
            camera.position.x = centerX;
        }
        else if(camera.position.x > map.getCols()* 16 - centerX ){
            camera.position.x = map.getCols()* 16 - centerX;
        }

        if(map.getRows() * 16 < Gdx.graphics.getHeight() * camera.zoom){
            camera.position.y = map.getRows() * 16 /2;
        }
        if(map.getCols()* 16 < Gdx.graphics.getWidth() * camera.zoom){
            camera.position.x = map.getCols() * 16 /2;
        }

    }
}