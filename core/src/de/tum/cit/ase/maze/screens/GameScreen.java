package de.tum.cit.ase.maze.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.maze.screens.MazeRunnerGame;

import javax.swing.text.html.parser.Entity;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.List;


/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    private float sinusInput = 0f;
    private Viewport viewport;

    // adding game element
    private int score;
    private float time;
    private int heartCount;
    private int key;
    private String mapPath;
    private int level;
    private Stage stage;
    Batch batch;
    Map map;
    boolean gamePause = false;

    List<Entity> elements; // we will use this to store our game elements(entities)

    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game, String mapPath, int score, int time) {
        this.game = game;
        this.score = score;

        this.time = time;
        this.heartCount = 5;
        this.key = 0;
        elements = new ArrayList<>();
        level = mapPath.split("-")[1].charAt(0) - '0'; // extracting the map level from the path provided
        stage = new Stage(); //creating a stage for ui elements such as buttons ect
        // Create and configure the camera for the game view
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


        font = game.getSkin().getFont("font"); // Get the font from the game's skin
        batch = game.getSpriteBatch(); //using sprite batch for renderig
        map = new Map(mapPath, elements);
        viewport = new ScreenViewport(camera);
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
        //map.draw(batch);
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
