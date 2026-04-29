package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.NormalState;

import java.util.List;

public class RestFloor extends TowerFloor {

    private final String floorName;
    private final int healAmount;

    public RestFloor(String floorName, int healAmount) {
        this.floorName = floorName;
        this.healAmount = Math.max(0, healAmount);
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("  setup: a quiet alcove with a fountain — the party makes camp.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("  resolveChallenge: the heroes rest and cleanse their afflictions.");
        for (Hero hero : party) {
            if (!hero.isAlive()) continue;
            hero.heal(healAmount);
            if (!(hero.getState() instanceof NormalState)) {
                hero.setState(new NormalState());
            }
            System.out.println("    " + hero.getName() + " recovers (HP=" + hero.getHp() + ").");
        }
        return new FloorResult(true, 0, "Rested at " + floorName + ".");
    }

    @Override
    protected boolean shouldAwardLoot(FloorResult result) {
        return false;
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        throw new IllegalStateException("RestFloor.awardLoot should not be reached when shouldAwardLoot is false.");
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("  cleanup: the fire dies down; the climb resumes.");
    }
}
