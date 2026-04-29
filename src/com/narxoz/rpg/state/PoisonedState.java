package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class PoisonedState implements HeroState {

    private final int poisonDamagePerTurn;
    private int turnsRemaining;

    public PoisonedState(int poisonDamagePerTurn, int duration) {
        this.poisonDamagePerTurn = Math.max(1, poisonDamagePerTurn);
        this.turnsRemaining = Math.max(1, duration);
    }

    @Override
    public String getName() {
        return "Poisoned(" + turnsRemaining + ")";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return (int) Math.round(basePower * 0.7);
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return rawDamage;
    }

    @Override
    public void onTurnStart(Hero hero) {
        hero.takeRawDamage(poisonDamagePerTurn);
        System.out.println("    " + hero.getName() + " suffers " + poisonDamagePerTurn
                + " poison damage (HP=" + hero.getHp() + ").");
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsRemaining--;
        if (turnsRemaining <= 0) {
            hero.setState(new NormalState());
        }
    }

    @Override
    public boolean canAct() {
        return true;
    }
}
