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
import de.tum.cit.ase.maze.utils.Manager;
import de.tum.cit.ase.maze.game.*;
import de.tum.cit.ase.maze.game.Enemy;
import de.tum.cit.ase.maze.game.Key;
import de.tum.cit.ase.maze.game.Trap;

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

    List<Entity> objects;
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
    Label bananaLabel; // bullets
    Label missingKeyLabel;

    Table pauseMenu;
    boolean gamePause = false;
    int level = 1;

    public GameScreen(MazeRunnerGame game,String mapPath,int score,float time) {
        this.game = game;
        objects = new ArrayList();
        // extracting the map level from the path provided
        level =  mapPath.split("-")[1].charAt(0) - '0';

        // Create and configure the camera for the game view
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        viewport = new ScreenViewport(camera);
        stage = new Stage(); //creating a stage for ui elements such as buttons ect


        // Get the font from the game's skin
        font = game.getSkin().getFont("font");

        batch = game.getSpriteBatch();
        map = new Map(mapPath,objects);
        player = new Player(new Vector2(map.getEntryCell().col * 16,map.getEntryCell().row * 16));
        initInput();
        followCamera = new FollowCamera(camera,map);


        this.score = score;
        this.time = time;
        heart = 3; //The character has a limited number of lives.
        keyCount = 0;
        createUI();
        Manager.getInstance().soundsManager.play("begin",1);

    }
    //HUD: Display the amount of lives left and key collection status at all times.
    void createUI(){
        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage
        table.left().top();


        // heart
        Table heartTable = new Table();
        table.add(heartTable);
//        heartTable.setBackground(new TextureRegionDrawable(new Texture("uiBox.png")));

        Image hearthImg = new Image(new Texture("heart.png"));
        heartLabel = new Label("1",game.getSkin());
        heartLabel.setColor(Color.WHITE);
        heartTable.add(heartLabel).pad(10);
        heartTable.add(hearthImg);


        // score
        Table scoreTable = new Table();
        table.add(scoreTable);
//        scoreTable.setBackground(new TextureRegionDrawable(new Texture("uiBox.png")));

        Label label2 = new Label("Score:",game.getSkin());
        scoreLabel = new Label("3400",game.getSkin());
        label2.setColor(Color.WHITE);
        label2.setFontScale(1.0f);
        scoreLabel.setFontScale(1.0f);
        scoreLabel.setColor(Color.WHITE);
        scoreTable.add(label2).pad(10);
        scoreTable.add(scoreLabel);
        scoreTable.padLeft(100);
        scoreTable.padRight(100);

        // time
        Table timeTable = new Table();
        table.add(timeTable);
//        timeTable.setBackground(new TextureRegionDrawable(new Texture("uiBox.png")));


        // key
        Table keyTable = new Table();
        table.add(keyTable);
//        keyTable.setBackground(new TextureRegionDrawable(new Texture("uiBox.png")));

        keyLabel = new Label("0/" + map.getKeyCount(),game.getSkin());
        keyLabel.setFontScale(1.0f);
        keyLabel.setColor(Color.YELLOW);
        keyTable.add(keyLabel).pad(10);
        keyTable.add(new Image(new Texture("key.png")));

        missingKeyLabel = new Label("missing key!",game.getSkin());
        missingKeyLabel.setVisible(false);
        missingKeyLabel.setColor(Color.WHITE);
        missingKeyLabel.setPosition(Gdx.graphics.getWidth()/2 - missingKeyLabel.getWidth()/2,Gdx.graphics.getHeight()/2 - missingKeyLabel.getHeight()/2);
        stage.addActor(missingKeyLabel);


        pauseMenu = new Table();
        pauseMenu.setFillParent(true);

        pauseMenu.add(new Label("ESC: Continue!",game.getSkin())).padBottom(20).row();


        TextButton loadBtn = new TextButton("Load", game.getSkin());
        pauseMenu.add(loadBtn).width(200);
        loadBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.loadGame(); // Change to the game screen when button is pressed
            }
        });
        TextButton exitBtn = new TextButton("Exit", game.getSkin());
        pauseMenu.add(exitBtn).width(200).row();
        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit(); // Change to the game screen when button is pressed
            }
        });
        TextButton gotoMenuBtn = new TextButton("Menu", game.getSkin());
        pauseMenu.add(gotoMenuBtn).width(200).row();
        gotoMenuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToMenu(); // Change to the game screen when button is pressed
            }
        });

        pauseMenu.setVisible(false);

        stage.addActor(pauseMenu);
        Manager.getInstance().soundsManager.playGameMusic();
    }
    //handling inputs with InputMultiplexer from gdx for the keyup and down methods written in player
    void initInput(){
        InputMultiplexer mixInput = new InputMultiplexer();
        mixInput.addProcessor(stage);
        mixInput.addProcessor(new InputAdapter(){
            @Override
            public boolean keyDown(int keycode) {
                player.onKeyDown(keycode);
                return super.keyDown(keycode);
            }

            @Override
            public boolean keyUp(int keycode) {
                player.onKeyUp(keycode);
                return super.keyUp(keycode);
            }
        });
        Gdx.input.setInputProcessor(mixInput);
    }

    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
//Game Menu: Available on startup and through the Esc button; must allow to continue playing, choosing a new map, or exiting.
            gamePause = !gamePause;
            pauseMenu.setVisible(gamePause);
        }
        ScreenUtils.clear(0, 0, 0, 1); //clear screen


        camera.update();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        if(!gamePause){
            followCamera.follow(player.getPos()); //camera follows player

            // update time,score,heart
            time += delta;
            scoreLabel.setText(score);
            heartLabel.setText(heart);
            keyLabel.setText(keyCount + "/" + map.getKeyCount());

            List<Entity> addedObjects = new ArrayList<>();
            player.update(map);
            player.shoot(addedObjects);


            for(Entity obj : objects){
                obj.update();
            }
            objects.addAll(addedObjects);
            //level completion checks, when our player passes a level score increases and he is instantly brought to next level to be able to conitnue the game
            missingKeyLabel.setVisible(false);
            for(int row = 0; row < map.getRows();row++) {
                for (int col = 0; col < map.getCols(); col++) {
                    Cell cell = map.getCell(row,col);
                    if(cell.cellType == CellType.EXIT && cell.getRect().collide(player.getRect())){  // checking for player collision with the exit box
                        if(keyCount == map.getKeyCount()){//checking if player has the key id so continue else not
                            // The character must collect a key from the maze and reach the exit before losing all lives.
                            score += 100;
                            System.out.println("level: pass " + level);
                            if(level == 5){
                                game.goToVictoryScreen(score,time);
                                //win
                            }
                            else {
                                String mapPath = "maps/level-"+ String.valueOf(level + 1) +".properties";
                                //assuming that the file has the same formatas all others, we also keep score and time
                                game.goToGame(mapPath,score,time);
                            }
                            return;
                        }
                        else {
                            missingKeyLabel.setVisible(true);
                            //if key is missing player can not pass
                        }
                    }
                }
            }

            // collision player with other objects
            for(Entity obj : objects){
                if(obj instanceof Slime){
                    ((Slime)obj).setPlayer(player);
                }
                if(obj instanceof Enemy && obj.getRect().collide(player.getRect())){
                    //Static traps and dynamically moving enemies are obstacles. On contact with any of them, the player loses one life.
                    playerDie(); //decrease heart if it is the last one gameover
                }
                else if(obj instanceof Key && obj.getRect().collide(player.getRect())){
                    obj.destroyFlag = true; // when our player touches the key he gets it and the key is removed from the screen
                    keyCount++;
                    score += 50; //score also increase
                    Manager.getInstance().soundsManager.play("item",1.0f); // play key retrieval sound
                }
                else if(obj instanceof Trap && obj.getRect().collide(player.getRect())){
                    playerDie(); // if player touches a trap he also dies
                }
            }

            // collision between objects
            for(Entity obj : objects){
                for(Entity otherObj: objects){
                    if(obj == otherObj){
                        continue; // if normal entititys interact thea are set to continue
                    }
                    //when bullets touch either traps or slimes those die and score increases for our player
                    if(obj instanceof PlayerProjectile && (otherObj instanceof Enemy || otherObj instanceof Trap)){
                        if(obj.getRect().collide(otherObj.getRect())){
                            score += 5;
                            obj.destroyFlag = true;
                            otherObj.destroyFlag = true;
                        }
                    }

                }
            }
            // when destroyflag is kalled objects are removed
            for(int i = 0; i < objects.size();i++){
                if(objects.get(i).destroyFlag){
                    objects.remove(i);
                }
            }
        }

        //draw
        map.draw(batch,player); // loading map
        // objects are only drawn when they are within a certain range of the player
        for(Entity obj : objects){
            if(Vector2.dst(player.getPos().x,player.getPos().y,obj.getPos().x,obj.getPos().y) > 2000){ // fog of war
                continue;
            }
            obj.draw(batch); // draw when within distance
        }

        player.draw(batch); // load player

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage

        viewport.apply(false);
    }

    void playerDie(){ // when player dies his heart count decreases if it is 0 then the game is over otherwise the player is spawned back to the begining of the level
        heart--;
        Cell entryCell = map.getEntryCell();
        player.setPos(new Vector2(entryCell.col * 16,entryCell.row * 16));
        Manager.getInstance().soundsManager.play("death",1.0f);
        if(heart <= 0){
            game.goToGameOverScreen(score,time);
        }
    }

    // resizing game window for changign of screen sizes
    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false);
        viewport.update(width,height,false);
//        stage.getViewport().update(width, height, false); // Update the stage viewport on resize
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

    // Additional methods and logic can be added as needed for the game screen
}
