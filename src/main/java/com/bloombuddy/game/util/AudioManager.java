package com.bloombuddy.game.util;

import javafx.scene.media.AudioClip;
/**
 * AudioManager handles playback of sound effects used in the game.
 * All sounds are preloaded and accessed via static methods.
 */

public class AudioManager {
    private static final AudioClip clickSound = load("click.wav");
    private static final AudioClip correctItem = load("correctitem.wav");
    private static final AudioClip wrongItem = load("wrongitem.wav");
    private static final AudioClip plantDied = load("plantdied.wav");
    private static final AudioClip winResult = load("winresult.wav");

    private static AudioClip load(String filename) {
        try {
            return new AudioClip(AudioManager.class.getResource("/assets/sounds/" + filename).toExternalForm());
        } catch (Exception e) {
            System.err.println("Failed to load sound: " + filename);
            return null;
        }
    }

    //click sound 
    public static void playClick() {
        if (clickSound != null) clickSound.play();
    }

    //plays when correct item dropped
    public static void playCorrectItem() {
        if (correctItem != null) correctItem.play();
    }

    //plays when wrong item dropped
    public static void playWrongItem() {
        if (wrongItem != null) wrongItem.play();
    }

    //when th eplan dies (lose 3 hearts)
    public static void playPlantDied() {
        if (plantDied != null) plantDied.play();
    }

    //when 6 attemps are all correct
    public static void playWinResult() {
        if (winResult != null) winResult.play();
    }
}
