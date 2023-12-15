package com.mythicallands.skills;

public abstract class Skill {
    protected String name;
    protected int level;
    protected int experience;
    protected int nextLevelXP = 83;
    protected double multiplyXP = 83;

    public Skill(String name) {
        this.name = name;
        this.level = 1;
        this.experience = 0;
    }

    public void addExperience(int exp) {
        this.experience += exp;
        System.out.println("XP: " + experience + "/" + nextLevelXP);
        // Check for level up and update level if necessary
        checkLevelUp();
    }

    public abstract void performAction();
    
    protected int experienceForNextLevel() {
        return nextLevelXP;
    }

    protected abstract void checkLevelUp();

    // Getters and setters
    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }
}