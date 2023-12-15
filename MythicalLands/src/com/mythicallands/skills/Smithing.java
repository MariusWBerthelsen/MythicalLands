package com.mythicallands.skills;

public class Smithing extends Skill {

    public Smithing() {
        super("Smithing");
    }
    
    public void smeltBar(String name) {
        int expGained = 0; // Example value, adjust as needed
    	switch(name) {
    	case "Bronze":
    		expGained = 25;
    		break;
    		default:
    			break;
    	}
        addExperience(expGained);
        System.out.println(name + " (" + expGained + " xp)");
    }
    
    public void smithItem(String name) {
        int expGained = 0; // Example value, adjust as needed
    	switch(name) {
    	case "Bronze Platebody":
    		expGained = 100;
    		break;
    		default:
    			break;
    	}
        addExperience(expGained);
        System.out.println(name + " (" + expGained + " xp)");
    }

    @Override
    protected void checkLevelUp() {
        while (experience >= experienceForNextLevel()) {
            level++;
            multiplyXP = (double) (multiplyXP * 1.10833);
            nextLevelXP = (int) (nextLevelXP + multiplyXP);
            System.out.println("Leveled up! New mining level: " + level);
            // Additional actions on level up, if necessary
        }
    }

    private int calculateExpGain() {
        // Calculate the amount of experience gained from a woodcutting action
        // This could be a fixed amount or vary based on certain conditions
        return 10; // Example value
    }

	@Override
	public void performAction() {
		// TODO Auto-generated method stub
		
	}
	
}
