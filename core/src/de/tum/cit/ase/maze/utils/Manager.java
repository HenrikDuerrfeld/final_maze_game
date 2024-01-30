package de.tum.cit.ase.maze.utils;
//we use this class to manage our soundmanager because of having various sounds in different locations this makes it easier
public class Manager {

    public SoundsManager soundsManager;

    private Manager()
    {
        soundsManager = new SoundsManager();
    }

    private static Manager instance = null;
    public static Manager getInstance() {
        if (instance == null)
            instance = new Manager();

        return instance;
    }

}
