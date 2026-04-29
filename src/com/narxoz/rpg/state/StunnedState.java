package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class StunnedState implements HeroState {

    private int turnsRemaining;

    public StunnedState(int turns) {
        this.turnsRemaining = Math.max(1, turns);
    }

    @Override
    public String getName() {
        return "Stunned(" + turnsRemaining + ")";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return 0;
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return (int) Math.round(rawDamage * 1.25);
    }

    @Override
    public void onTurnStart(Hero hero) {
        System.out.println("    " + hero.getName() + " is stunned and reels in place.");
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
        return false;
    }
}
