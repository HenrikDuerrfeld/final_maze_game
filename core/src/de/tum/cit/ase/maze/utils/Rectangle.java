package de.tum.cit.ase.maze.utils;

public class Rectangle {
    // this is the square which our cell will be in
    public float x,y,w,h;

    public Rectangle(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    //collide function to detect player and wall collisions
    public boolean collide(Rectangle b) {
        Rectangle rectangle = this;
        Rectangle r = b;
        if (r.x < rectangle.x + rectangle.w &&
                r.x + r.w > rectangle.x &&
                r.y < rectangle.y + rectangle.h &&
                r.h + r.y > rectangle.y) {
            return true;
        }
        return false;
    }
}
