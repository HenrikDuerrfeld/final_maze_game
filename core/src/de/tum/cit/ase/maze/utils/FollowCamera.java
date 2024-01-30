package de.tum.cit.ase.maze.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.ase.maze.game.Map;
// we are using this class to follow our player
//Camera Movement: Implement a camera movement mechanism that ensures that the player character is always visible within at least the middle 80% of the screen horizontally and vertically during gameplay.
public class FollowCamera {
    OrthographicCamera camera; //https://libgdx.com/wiki/graphics/2d/orthographic-camera this is the camera that will follow our player
    Map map;

    public FollowCamera(OrthographicCamera camera,  Map map){
        this.camera = camera;
        this.map =map;
        camera.zoom = 0.2f;//setting base camera zoom, bigger means further away
    }
    // our follow method will follow the charcter by using vector2 by setting the camera postion to the player position
    public void follow(Vector2 target){
        //center x and y represent the x y coordinates of the camera, as in where its center is located
        // in the following code we will adjust this
        float centerX = camera.viewportWidth * camera.zoom * 0.5f; //viewport is the part of the map that is visible to use based on Orthographic camera
        float centerY = camera.viewportHeight * camera.zoom * 0.5f;

        camera.position.x = target.x; //player positions x and y
        camera.position.y = target.y;
        // we are checking if the camera is well centered here, if center y is bigger the camera is too close to the top and too far to the right for x, then the values must be recentered on the player
        //second condition checkes for the opposit if y is smaller or x then the camera is respectively closer to the bottom or left, in thatcase we adjust the camera again
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
        //here we are checking that the camera is covering less area then the entire map, if it is greater we must resize
        if(map.getRows() * 16 < Gdx.graphics.getHeight() * camera.zoom){
            camera.position.y = map.getRows() * 16 /2;
        }
        if(map.getCols()* 16 < Gdx.graphics.getWidth() * camera.zoom){
            camera.position.x = map.getCols() * 16 /2;
        }

    }
}