package com.narxoz.rpg.combatant;

import com.narxoz.rpg.state.HeroState;
import com.narxoz.rpg.state.NormalState;

public class Hero {

    private final String name;
    private int hp;
    private final int maxHp;
    private final int attackPower;
    private final int defense;
    private HeroState state;

    public Hero(String name, int hp, int attackPower, int defense) {
        this(name, hp, attackPower, defense, new NormalState());
    }

    public Hero(String name, int hp, int attackPower, int defense, HeroState initialState) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.state = initialState;
    }

    public String getName()        { return name; }
    public int getHp()             { return hp; }
    public int getMaxHp()          { return maxHp; }
    public int getAttackPower()    { return attackPower; }
    public int getDefense()        { return defense; }
    public boolean isAlive()       { return hp > 0; }

    public HeroState getState()    { return state; }

    public void setState(HeroState newState) {
        if (newState == null) {
            throw new IllegalArgumentException("Hero state cannot be null");
        }
        if (this.state != newState) {
            System.out.println("  * " + name + " is now " + newState.getName() + " (was " + this.state.getName() + ")");
            this.state = newState;
        }
    }

    public void takeDamage(int amount) {
        int adjusted = state.modifyIncomingDamage(amount);
        adjusted = Math.max(0, adjusted - defense);
        hp = Math.max(0, hp - adjusted);
    }

    public void takeRawDamage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    public int rollAttack() {
        return state.modifyOutgoingDamage(attackPower);
    }

    public void onTurnStart() {
        state.onTurnStart(this);
    }

    public void onTurnEnd() {
        state.onTurnEnd(this);
    }

    public boolean canAct() {
        return state.canAct();
    }
}
