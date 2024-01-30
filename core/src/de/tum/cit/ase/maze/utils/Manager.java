package de.tum.cit.ase.maze.utils;

public class Manager {

    public SoundsManager soundsManager;

    private Manager()
    {
        soundsManager = new SoundsManager();
    }

    private static Manager instance = null;
    public static Manager getInstance()
    {
        if (instance == null)
            instance = new Manager();

        return instance;
    }

}
