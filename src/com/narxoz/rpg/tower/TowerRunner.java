package com.narxoz.rpg.tower;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.floor.FloorResult;
import com.narxoz.rpg.floor.TowerFloor;

import java.util.List;

public class TowerRunner {

    private final List<TowerFloor> floors;

    public TowerRunner(List<TowerFloor> floors) {
        this.floors = floors;
    }

    public TowerRunResult run(List<Hero> party) {
        int floorsCleared = 0;
        for (int i = 0; i < floors.size(); i++) {
            TowerFloor floor = floors.get(i);
            if (!anyAlive(party)) {
                System.out.println("\n[!] Party wiped before reaching floor " + (i + 1) + ".");
                break;
            }
            FloorResult result = floor.explore(party);
            System.out.println("  >> " + result.getSummary()
                    + " (cleared=" + result.isCleared()
                    + ", damageTaken=" + result.getDamageTaken() + ")");
            if (result.isCleared()) {
                floorsCleared++;
            } else {
                break;
            }
        }
        int survivors = countAlive(party);
        boolean reachedTop = floorsCleared == floors.size() && survivors > 0;
        return new TowerRunResult(floorsCleared, survivors, reachedTop);
    }

    private static boolean anyAlive(List<Hero> party) {
        for (Hero h : party) if (h.isAlive()) return true;
        return false;
    }

    private static int countAlive(List<Hero> party) {
        int n = 0;
        for (Hero h : party) if (h.isAlive()) n++;
        return n;
    }
}
