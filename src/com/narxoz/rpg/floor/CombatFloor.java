package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.state.BerserkState;
import com.narxoz.rpg.state.PoisonedState;

import java.util.ArrayList;
import java.util.List;

public class CombatFloor extends TowerFloor {

    private final String floorName;
    private final List<Monster> monsterRoster;
    private final boolean appliesPoisonOnHit;
    private List<Monster> activeMonsters;

    public CombatFloor(String floorName, List<Monster> monsterRoster, boolean appliesPoisonOnHit) {
        this.floorName = floorName;
        this.monsterRoster = monsterRoster;
        this.appliesPoisonOnHit = appliesPoisonOnHit;
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }

    @Override
    protected void setup(List<Hero> party) {
        activeMonsters = new ArrayList<>();
        for (Monster m : monsterRoster) {
            activeMonsters.add(new Monster(m.getName(), m.getHp(), m.getAttackPower()));
        }
        System.out.println("  setup: " + activeMonsters.size() + " enemies emerge from the shadows.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("  resolveChallenge: combat begins.");
        int damageTaken = 0;
        int round = 1;
        while (anyAlive(party) && anyAlive(activeMonsters)) {
            System.out.println("  -- Round " + round + " --");
            for (Hero hero : party) {
                if (!hero.isAlive()) continue;
                hero.onTurnStart();
                if (!hero.isAlive()) { hero.onTurnEnd(); continue; }
                if (hero.canAct()) {
                    Monster target = firstAliveMonster(activeMonsters);
                    if (target != null) {
                        int dmg = hero.rollAttack();
                        target.takeDamage(dmg);
                        System.out.println("    " + hero.getName() + " (" + hero.getState().getName()
                                + ") strikes " + target.getName() + " for " + dmg
                                + " (HP=" + target.getHp() + ").");
                    }
                }
                hero.onTurnEnd();
            }
            for (Monster monster : activeMonsters) {
                if (!monster.isAlive()) continue;
                Hero target = firstAliveHero(party);
                if (target == null) break;
                int hpBefore = target.getHp();
                monster.attack(target);
                int dealt = hpBefore - target.getHp();
                System.out.println("    " + monster.getName() + " hits " + target.getName()
                        + " for " + dealt + " (HP=" + target.getHp() + ").");
                damageTaken += dealt;
                if (appliesPoisonOnHit && target.isAlive()
                        && !(target.getState() instanceof PoisonedState)) {
                    target.setState(new PoisonedState(3, 2));
                }
                if (target.isAlive() && BerserkState.shouldTrigger(target)) {
                    target.setState(new BerserkState());
                }
            }
            round++;
        }
        boolean cleared = anyAlive(party) && !anyAlive(activeMonsters);
        String summary = cleared
                ? "Cleared " + floorName + " in " + (round - 1) + " round(s)."
                : "Party fell on " + floorName + ".";
        return new FloorResult(cleared, damageTaken, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        int healing = 4;
        System.out.println("  awardLoot: each hero recovers " + healing + " HP.");
        for (Hero hero : party) {
            if (hero.isAlive()) hero.heal(healing);
        }
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("  cleanup: party regroups after combat.");
    }

    private static boolean anyAlive(List<? extends Object> entities) {
        for (Object e : entities) {
            if (e instanceof Hero h && h.isAlive()) return true;
            if (e instanceof Monster m && m.isAlive()) return true;
        }
        return false;
    }

    private static Hero firstAliveHero(List<Hero> party) {
        for (Hero h : party) if (h.isAlive()) return h;
        return null;
    }

    private static Monster firstAliveMonster(List<Monster> monsters) {
        for (Monster m : monsters) if (m.isAlive()) return m;
        return null;
    }
}
