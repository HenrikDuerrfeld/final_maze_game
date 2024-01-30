package de.tum.cit.ase.maze.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

public class SoundsManager {

    public HashMap<String, Sound> sounds = new HashMap();

    public SoundsManager(){
        sounds.put("menuMusic",Gdx.audio.newSound(Gdx.files.internal("sounds/background.mp3")));
        sounds.put("gameMusic",Gdx.audio.newSound(Gdx.files.internal(  "sounds/gameMusic.mp3")));
        sounds.put("death",Gdx.audio.newSound(Gdx.files.internal(  "sounds/death.wav")));
        sounds.put("item",Gdx.audio.newSound(Gdx.files.internal(  "sounds/item.wav")));
        sounds.put("shoot",Gdx.audio.newSound(Gdx.files.internal(  "sounds/shoot.wav")));
        sounds.put("begin",Gdx.audio.newSound(Gdx.files.internal(  "sounds/begin.mp3")));

    }


    public void play(String sound,float volume){
        sounds.get(sound).play(volume);
    }


    public void playMenuMusic(){
        sounds.get("gameMusic").stop();

        sounds.get("menuMusic").stop();
        sounds.get("menuMusic").loop(0.7f);
    }
    public void playGameMusic(){
        sounds.get("menuMusic").stop();

        sounds.get("gameMusic").stop();
        sounds.get("gameMusic").loop(0.7f);
    }

    public void stopMusic(){
        sounds.get("music").stop();
    }

    public void stop(String sound){
        sounds.get(sound).stop();
    }
}
