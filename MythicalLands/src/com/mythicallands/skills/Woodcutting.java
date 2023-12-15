package com.mythicallands.skills;

public class Woodcutting extends Skill {

    public Woodcutting() {
        super("Woodcutting");
    }

    @Override
    public void performAction() {
        // Example: Gain a fixed amount of experience per action
        int expGained = 25; // Example value, adjust as needed
        addExperience(expGained);
        System.out.println("Gained " + expGained + " woodcutting experience.");
    }

    @Override
    protected void checkLevelUp() {
        while (experience >= experienceForNextLevel()) {
            level++;
            multiplyXP = (double) (multiplyXP * 1.10833);
            nextLevelXP = (int) (nextLevelXP + multiplyXP);
            System.out.println("Leveled up! New woodcutting level: " + level);
            // Additional actions on level up, if necessary
        }
    }

    private int calculateExpGain() {
        // Calculate the amount of experience gained from a woodcutting action
        // This could be a fixed amount or vary based on certain conditions
        return 10; // Example value
    }
}