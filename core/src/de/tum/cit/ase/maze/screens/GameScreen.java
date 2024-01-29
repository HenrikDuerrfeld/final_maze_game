package de.tum.cit.ase.maze.screens;

import  com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;
import de.tum.cit.ase.maze.utils.FollowCamera;
//import de.tum.cit.ase.maze.utils.Manager;
import de.tum.cit.ase.maze.game.*;
//import de.tum.cit.ase.maze.game.Enemy;
//import de.tum.cit.ase.maze.game.Key;
//import de.tum.cit.ase.maze.game.Trap;

import java.util.ArrayList;
import java.util.List;


/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    private final Stage stage;

    private Viewport viewport;

    List<Entity> objects; // we will use this to store our game elements(entities);
    Batch batch;
    Map map;
    Player player;
    FollowCamera followCamera;

    int score ;
    float time ;
    int heart;
    int keyCount;

    Label scoreLabel;
    Label heartLabel;
    Label keyLabel;
    Label bananaLabel;
    Label missingKeyLabel;

    Table pauseMenu;
    boolean gamePause = false;
    int level = 1;

    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game, String mapPath, int score, int time) {
        this.game = game;
        objects = new ArrayList<>();


        level = mapPath.split("-")[1].charAt(0) - '0'; // extracting the map level from the path provided
        stage = new Stage(); //creating a stage for ui elements such as buttons ect
        // Create and configure the camera for the game view
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        viewport = new ScreenViewport(camera);
        font = game.getSkin().getFont("font"); // Get the font from the game's skin
        batch = game.getSpriteBatch(); //using sprite batch for rendering
        map = new Map(mapPath, objects);
        player = new Player(new Vector2(map.getEntryCell().col * 16,map.getEntryCell().row * 16));
        this.score = score;
        this.time = time;
        heart = 3;
        keyCount = 0;
    }


    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
            gamePause = !gamePause;
        }
        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen

        camera.update(); // Update the camera
        batch.setProjectionMatrix(viewport.getCamera().combined);


        // Move text in a circular path to have an example of a moving object
        //sinusInput += delta;
        //float textX = (float) (camera.position.x + Math.sin(sinusInput) * 100);
        //float textY = (float) (camera.position.y + Math.cos(sinusInput) * 100);

        // Set up and begin drawing with the sprite batch
        game.getSpriteBatch().setProjectionMatrix(camera.combined);


        //movment controls

        //last key pressed
        String direction = "";

        game.getSpriteBatch().begin(); // Important to call this before drawing anything
        //rendering the actual map
        map.draw(batch, player);
        // Draw the character next to the text :) / We can reuse sinusInput here
        // looping true makes our character have the leg walking animation
        // Time variables for each direction
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
    // Additional methods and logic can be added as needed for the gamescreen
}
