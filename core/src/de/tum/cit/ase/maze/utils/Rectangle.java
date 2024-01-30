package de.tum.cit.ase.maze.utils;

// This class represents a rectangle in a 2D space using the coordinates x, y, w, h
// which is commonly used in game development for collision detection

public class Rectangle {

    public float x,y,w,h;

    //Initialization of the coordinates
    public Rectangle(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    // This method checks whether the current rectangle collides with another rectangle (b)
    // it returns a boolean value indicating whether there is a collision
    public boolean collide(Rectangle b) {
        Rectangle rectangle = this;
        //This creates a reference to the provided rectangle b
        // the following conditions check if the two rectangles intersect

        // r.x < rectangle.x + rectangle.w: Checks if the right edge of b is to the left of the right edge of the current rectangle
        // r.x + r.w > rectangle.x: Checks if the left edge of b is to the right of the left edge of the current rectangle
        //r.y < rectangle.y + rectangle.h: Checks if the bottom edge of b is above the top edge of the current rectangle
        //r.h + r.y > rectangle.y: Checks if the top edge of b is below the bottom edge of the current rectangle
        // The conditions are essentially checking if the two rectangles overlap along both the x-axis and y-axis

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
