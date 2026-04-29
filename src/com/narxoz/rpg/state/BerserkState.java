package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class BerserkState implements HeroState {

    private static final double HP_THRESHOLD = 0.5;

    @Override
    public String getName() {
        return "Berserk";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return (int) Math.round(basePower * 1.6);
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return (int) Math.round(rawDamage * 1.4);
    }

    @Override
    public void onTurnStart(Hero hero) {
        System.out.println("    " + hero.getName() + " roars in berserk fury!");
    }

    @Override
    public void onTurnEnd(Hero hero) {
        double ratio = hero.getMaxHp() == 0 ? 0.0 : (double) hero.getHp() / hero.getMaxHp();
        if (ratio > HP_THRESHOLD) {
            hero.setState(new NormalState());
        }
    }

    @Override
    public boolean canAct() {
        return true;
    }

    public static boolean shouldTrigger(Hero hero) {
        if (hero.getMaxHp() == 0) {
            return false;
        }
        double ratio = (double) hero.getHp() / hero.getMaxHp();
        return ratio <= HP_THRESHOLD && !(hero.getState() instanceof BerserkState);
    }
}
