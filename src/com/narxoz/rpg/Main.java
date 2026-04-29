package com.narxoz.rpg;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.floor.BossFloor;
import com.narxoz.rpg.floor.CombatFloor;
import com.narxoz.rpg.floor.RestFloor;
import com.narxoz.rpg.floor.TowerFloor;
import com.narxoz.rpg.floor.TrapFloor;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.tower.TowerRunResult;
import com.narxoz.rpg.tower.TowerRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("       The Haunted Tower — Ascending the Floors");
        System.out.println("============================================================");

        Hero aragorn = new Hero("Aragorn", 40, 9, 2);
        Hero lyra = new Hero("Lyra", 32, 8, 1, new PoisonedState(2, 3));

        List<Hero> party = new ArrayList<>(Arrays.asList(aragorn, lyra));

        System.out.println("\nParty roster:");
        for (Hero h : party) {
            System.out.println("  - " + h.getName() + " | HP=" + h.getHp()
                    + " | ATK=" + h.getAttackPower()
                    + " | DEF=" + h.getDefense()
                    + " | starting state=" + h.getState().getName());
        }

        List<TowerFloor> floors = new ArrayList<>();
        floors.add(new CombatFloor(
                "Floor 1 - Skeleton Crypt",
                Arrays.asList(
                        new Monster("Skeleton Grunt", 14, 5),
                        new Monster("Skeleton Archer", 10, 6)),
                false));
        floors.add(new TrapFloor("Floor 2 - Spike Hall", 6, true));
        floors.add(new RestFloor("Floor 3 - Quiet Alcove", 12));
        floors.add(new CombatFloor(
                "Floor 4 - Venom Pit",
                Arrays.asList(
                        new Monster("Cave Spider", 12, 5),
                        new Monster("Venom Drake", 16, 7)),
                true));
        floors.add(new BossFloor("Floor 5 - Throne of Ash",
                new Monster("Wraith Lord", 36, 9)));

        TowerRunner runner = new TowerRunner(floors);
        TowerRunResult result = runner.run(party);

        System.out.println();
        System.out.println("============================================================");
        System.out.println("                    Tower Run Result");
        System.out.println("============================================================");
        System.out.println("  Floors cleared : " + result.getFloorsCleared() + " / " + floors.size());
        System.out.println("  Heroes alive   : " + result.getHeroesSurviving() + " / " + party.size());
        System.out.println("  Reached top    : " + result.isReachedTop());
        System.out.println();
        System.out.println("  Final party state:");
        for (Hero h : party) {
            System.out.println("    - " + h.getName()
                    + " | HP=" + h.getHp() + "/" + h.getMaxHp()
                    + " | state=" + h.getState().getName()
                    + " | alive=" + h.isAlive());
        }
    }
}
