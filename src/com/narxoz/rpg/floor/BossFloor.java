package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.state.BerserkState;
import com.narxoz.rpg.state.StunnedState;

import java.util.List;

public class BossFloor extends TowerFloor {

    private final String floorName;
    private final Monster bossTemplate;
    private Monster boss;

    public BossFloor(String floorName, Monster bossTemplate) {
        this.floorName = floorName;
        this.bossTemplate = bossTemplate;
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }

    @Override
    protected void announce() {
        System.out.println();
        System.out.println("##################################################");
        System.out.println("##  BOSS FLOOR: " + floorName);
        System.out.println("##  " + bossTemplate.getName() + " bars the way!");
        System.out.println("##################################################");
    }

    @Override
    protected void setup(List<Hero> party) {
        boss = new Monster(bossTemplate.getName(), bossTemplate.getHp(), bossTemplate.getAttackPower());
        System.out.println("  setup: " + boss.getName() + " enters with " + boss.getHp() + " HP.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("  resolveChallenge: the boss fight begins.");
        int damageTaken = 0;
        int round = 1;
        while (anyAlive(party) && boss.isAlive()) {
            System.out.println("  -- Boss Round " + round + " --");
            for (Hero hero : party) {
                if (!hero.isAlive()) continue;
                hero.onTurnStart();
                if (!hero.isAlive()) { hero.onTurnEnd(); continue; }
                if (hero.canAct() && boss.isAlive()) {
                    int dmg = hero.rollAttack();
                    boss.takeDamage(dmg);
                    System.out.println("    " + hero.getName() + " (" + hero.getState().getName()
                            + ") strikes " + boss.getName() + " for " + dmg
                            + " (HP=" + boss.getHp() + ").");
                }
                hero.onTurnEnd();
            }
            if (!boss.isAlive()) break;
            Hero target = firstAliveHero(party);
            if (target == null) break;
            int hpBefore = target.getHp();
            boss.attack(target);
            int dealt = hpBefore - target.getHp();
            damageTaken += dealt;
            System.out.println("    " + boss.getName() + " smashes " + target.getName()
                    + " for " + dealt + " (HP=" + target.getHp() + ").");
            if (target.isAlive() && round % 2 == 0
                    && !(target.getState() instanceof StunnedState)) {
                target.setState(new StunnedState(1));
            }
            if (target.isAlive() && BerserkState.shouldTrigger(target)) {
                target.setState(new BerserkState());
            }
            round++;
        }
        boolean cleared = anyAlive(party) && !boss.isAlive();
        return new FloorResult(cleared, damageTaken,
                cleared ? "Boss " + boss.getName() + " defeated!"
                        : "Party fell to " + boss.getName() + ".");
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        int healing = 8;
        System.out.println("  awardLoot: a legendary chest cracks open (+" + healing + " HP each).");
        for (Hero hero : party) {
            if (hero.isAlive()) hero.heal(healing);
        }
    }

    private static boolean anyAlive(List<Hero> party) {
        for (Hero h : party) if (h.isAlive()) return true;
        return false;
    }

    private static Hero firstAliveHero(List<Hero> party) {
        for (Hero h : party) if (h.isAlive()) return h;
        return null;
    }
}
