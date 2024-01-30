package de.tum.cit.ase.maze.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class SpriteSheet {
    Texture texture; // this the texture of our sprite sheet
    int rows, cols; // number of rows and columns in our sprite sheet grid
    TextureRegion[] frames; // we use this to choose and store the pictures from our selected file
    int width,height;
    int from;
    int to;
    int current;
    float time;
    boolean loop;
    int countTime = 0;


    public SpriteSheet(Texture texture, int rows, int cols) {
        this.texture = texture;
        this.rows = rows;
        this.cols = cols;

        frames = new TextureRegion[rows * cols];
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / cols, texture.getHeight() / rows);  // split the sprite sheet
        width = texture.getWidth() / cols;
        height =  texture.getHeight() / rows;
        int ide = 0;
        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                frames[ide] = tmp[i][j];
                ide++;
            }
        }
    }
    public TextureRegion getTexture(int frame) {
        return frames[frame];
    } // we use this to get the frame we want from jpeg file
    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }

    // Will come to play when initializing character animation in class Player
    public void setPlay(int from, int to, float time, boolean loop) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.loop = loop;
        current = from;
    }
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
    public TextureRegion getCurrentFrame() {
        return frames[current];
    } //returns current frames used for player
}
