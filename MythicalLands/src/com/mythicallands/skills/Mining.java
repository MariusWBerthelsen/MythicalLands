package com.mythicallands.skills;

public class Mining extends Skill {

    public Mining() {
        super("Mining");
    }

    public void mineOre(String name) {
        int expGained = 0; // Example value, adjust as needed
    	switch(name) {
    	case "Copper":
    		expGained = 25;
    		break;
    	case "Tin":
    		expGained = 30;
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