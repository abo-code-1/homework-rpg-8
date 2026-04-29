package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.BerserkState;
import com.narxoz.rpg.state.StunnedState;

import java.util.List;

public class TrapFloor extends TowerFloor {

    private final String floorName;
    private final int trapDamage;
    private final boolean stunsOnTrigger;
    private boolean armed;

    public TrapFloor(String floorName, int trapDamage, boolean stunsOnTrigger) {
        this.floorName = floorName;
        this.trapDamage = Math.max(1, trapDamage);
        this.stunsOnTrigger = stunsOnTrigger;
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }

    @Override
    protected void setup(List<Hero> party) {
        armed = true;
        System.out.println("  setup: pressure plates click into place — the trap is armed.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("  resolveChallenge: the floor erupts!");
        if (!armed) {
            return new FloorResult(true, 0, "Trap was disarmed before triggering.");
        }
        int total = 0;
        for (Hero hero : party) {
            if (!hero.isAlive()) continue;
            int hpBefore = hero.getHp();
            hero.takeDamage(trapDamage);
            int dealt = hpBefore - hero.getHp();
            total += dealt;
            System.out.println("    " + hero.getName() + " takes " + dealt
                    + " trap damage (HP=" + hero.getHp() + ").");
            if (stunsOnTrigger && hero.isAlive() && !(hero.getState() instanceof StunnedState)) {
                hero.setState(new StunnedState(1));
            }
            if (hero.isAlive() && BerserkState.shouldTrigger(hero)) {
                hero.setState(new BerserkState());
            }
        }
        armed = false;
        boolean cleared = anyAlive(party);
        return new FloorResult(cleared, total,
                cleared ? "Trap survived; party walked it off."
                        : "Party wiped on " + floorName + ".");
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("  awardLoot: rummaging the rubble yields a small token (+2 HP each).");
        for (Hero hero : party) {
            if (hero.isAlive()) hero.heal(2);
        }
    }

    private static boolean anyAlive(List<Hero> party) {
        for (Hero h : party) if (h.isAlive()) return true;
        return false;
    }
}
