package de.tum.cit.ase.maze.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;
import games.spooky.gdx.nativefilechooser.NativeFileChooserCallback;
import games.spooky.gdx.nativefilechooser.NativeFileChooserConfiguration;

import java.io.File;
import java.io.FilenameFilter;

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
//this is our screen managing class which also starts the game
public class MazeRunnerGame extends Game {
    // Screens
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private GameOverScreen gameOverScreen;
    private WinScreen winScreen;

    // Sprite Batch for rendering
    private SpriteBatch spriteBatch;

    // UI Skin
    private Skin skin;

    // Character animation downwards
    private Animation<TextureRegion> characterDownAnimation;
    private Animation<TextureRegion> characterUpAnimation;
    private Animation<TextureRegion> characterLeftAnimation;
    private Animation<TextureRegion> characterRightAnimation;
    MazeRunnerGame that;

    /**
     * Constructor for MazeRunnerGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public MazeRunnerGame(NativeFileChooser fileChooser) {

        super();
        this.fileChooser = fileChooser;
        that = this;
    }

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     */
    @Override
    public void create() {
        spriteBatch = new SpriteBatch(); // Create SpriteBatch
        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin
        this.loadCharacterAnimation(); // Load character animation

        // Play some background music
        // Background sound
        Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();

        goToMenu(); // Navigate to the menu screen
    }

    /**
     * Switches to the menu screen.
     */
    public void goToMenu() {
        this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    /**
     * Switches to the game screen.
     */
    public void goToGame(String mapPath,int score,float time) {
        gameScreen = new GameScreen(this,mapPath,score,time);
        this.setScreen(gameScreen);
    }
    //load game method which uses native file chooser to load in a map from a.properties file
    // https://github.com/spookygames/gdx-nativefilechooser
    public void loadGame(){ //Your program must be able to read any maze from a properties file and play it.
        NativeFileChooserConfiguration conf = new NativeFileChooserConfiguration();
        conf.directory = Gdx.files.local("/maps"); //setting /maps as the map choosing point
        //namefilter to only accept .properties files
        conf.nameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".properties");
            }
        };
        //using nativefilechooser
        fileChooser.chooseFile(conf, new NativeFileChooserCallback() {
            @Override
            public void onFileChosen(FileHandle file) { //taking theinputted file as our new mapPath
            gameScreen = new GameScreen(that, file.path(), 0, 0);
            setScreen(gameScreen);
            }

            @Override
            public void onCancellation() {

            }

            @Override
            public void onError(Exception exception) {

            }
        });
    }
    public void goToVictoryScreen(int score,float time){
        if (winScreen == null) {
            winScreen = new WinScreen(this,score,time);
        }
        this.setScreen(winScreen);
    }
    public void goToGameOverScreen(int score,float time){
        if (gameOverScreen == null) {
            gameOverScreen = new GameOverScreen(this,score,time);
        }
        this.setScreen(gameOverScreen);
    }

    /**
     * Loads the character animation from the character.png file.
     */
    private void loadCharacterAnimation() {
        Texture walkSheet = new Texture(Gdx.files.internal("character.png"));

        int frameWidth = 16;
        int frameHeight = 32;
        int animationFrames = 4;

        // libGDX internal Array instead of ArrayList because of performance
        Array<TextureRegion> walkFramesDown = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkFramesUp = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkFramesLeft = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkFramesRight = new Array<>(TextureRegion.class);

        // Add all frames to the animation
        for (int col = 0; col < animationFrames; col++) {
            walkFramesDown.add(new TextureRegion(walkSheet, col * frameWidth, 0, frameWidth, frameHeight));
            walkFramesUp.add(new TextureRegion(walkSheet, col * frameWidth, frameHeight, frameWidth, frameHeight));
            walkFramesLeft.add(new TextureRegion(walkSheet, col * frameWidth, frameHeight * 2, frameWidth, frameHeight));
            walkFramesRight.add(new TextureRegion(walkSheet, col * frameWidth, frameHeight * 3, frameWidth, frameHeight));
        }

        characterDownAnimation = new Animation<>(0.1f, walkFramesDown);
        characterUpAnimation = new Animation<>(0.1f, walkFramesUp);
        characterLeftAnimation = new Animation<>(0.1f, walkFramesLeft);
        characterRightAnimation = new Animation<>(0.1f, walkFramesRight);
    }

    /**
     * Cleans up resources when the game is disposed.
     */
    @Override
    public void dispose() {
        getScreen().hide(); // Hide the current screen
        getScreen().dispose(); // Dispose the current screen
        spriteBatch.dispose(); // Dispose the spriteBatch
        skin.dispose(); // Dispose the skin
    }

    // Getter methods
    public Skin getSkin() {
        return skin;
    }

    public Animation<TextureRegion> getCharacterDownAnimation() {
        return characterDownAnimation;
    }

    public Animation<TextureRegion> getCharacterUpAnimation() {
        return characterUpAnimation;
    }

    public Animation<TextureRegion> getCharacterLeftAnimation() {
        return characterLeftAnimation;
    }

    public Animation<TextureRegion> getCharacterRightAnimation() {
        return characterRightAnimation;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    NativeFileChooser fileChooser;
}
