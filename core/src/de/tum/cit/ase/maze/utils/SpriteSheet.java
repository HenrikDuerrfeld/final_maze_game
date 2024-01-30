package de.tum.cit.ase.maze.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

//https://www.youtube.com/watch?v=ONe91ro51cQ
//https://libgdx.com/wiki/graphics/2d/2d-animation
// we are using spritesheet from libgdx to splice our jpeg files into different frames anf for animation purposes
public class SpriteSheet {
    // https://libgdx.com/wiki/graphics/2d/spritebatch-textureregions-and-sprites
    Texture texture; // this decodes our image files so that we can use them in our game

    int rows,cols;

    TextureRegion[] frames;
    //animation control variables
    float countTime = 0;
    int from;
    int to;
    int current;
    float time;
    boolean loop;
    int width,height;


    public SpriteSheet(Texture texture,int rows,int cols){
        this.texture = texture; // where the animations frames are contained
        this.rows = rows; // number of rows and cols in the sprite sheet
        this.cols = cols;

        frames = new TextureRegion[rows * cols]; // creating new array to store all the frames of the sprite sheet
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / cols, texture.getHeight() / rows);  // split the sprite sheet
        width = texture.getWidth() / cols; // width and height calculates the values of a single frame
        height =  texture.getHeight() / rows;
        int ide = 0;
        for(int i = 0; i < rows; i++) { // nested loop iterates over rows and columns to assign Tecture region to the frames array
            for (int j = 0; j < cols; j++) {
                frames[ide] = tmp[i][j];
                ide++;
            }
        }
    }
    public TextureRegion getTexture(int frame) {
        return frames[frame];
    }
    public TextureRegion getCurrentFrame() {
        return frames[current];
    } //
    //setPlay method is used to setup animations, eg. from frame x to frame y for time long and if loop true
    public void setPlay(int from, int to, float time, boolean loop) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.loop = loop;
        current = from;
    }
    //play method updates the animation frame based on the elapsed time, if the elapsed time is grater then the time variable then the frame is incremented
    //if loop is set to true then the animation is set to start again from the beginning
    public void play() {
        countTime += Gdx.graphics.getDeltaTime();
        if(countTime >= time) {
            countTime = 0;
            current++;
            if(current > to && loop) {
                current = from;
            }
            else if(current > to && !loop){
                current = to;
            }
        }

    }

    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }

    void dispose()
    {
        texture.dispose();
    }

}
