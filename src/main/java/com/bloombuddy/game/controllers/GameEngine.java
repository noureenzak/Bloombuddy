package com.bloombuddy.game.controllers;
/**
 * Manages the central game state (day, turn, hearts).
 * Implements Singleton pattern.
 */
public class GameEngine {
	
    // Single instance of the engine (Singleton)
    private static GameEngine instance = new GameEngine();

    // Game state variables
    private int day = 1;
    private int turn = 0;
    private int hearts = 3;
    public static final int MAX_DAYS = 5; 
    
    // Private constructor to prevent external instantiation
    private GameEngine() {}

    // Returns the singleton instance
    public static GameEngine getInstance() {
        return instance;
    }

    // Advances the turn and updates the day every 2 turns
    public void nextTurn() {
        turn++;
        if (turn % 2 == 0) {
            day++;
            System.out.println("📅 New Day: " + day);
        }
    }

    // Decreases heart count by 1, if any left
    public void loseHeart() {
        if (hearts > 0) {
            hearts--;
            System.out.println("❤️ Heart lost! Remaining: " + hearts);
        }
    }
    // Getters for current game state
    public int getDay() {
        return day;
    }

    public int getTurn() {
        return turn;
    }

    public int getHearts() {
        return hearts;
    }
    // Resets the game state to initial values
    public void resetGame() {
        day = 1;
        turn = 0;
        hearts = 3;
        System.out.println(" Game reset!");
    }
}
