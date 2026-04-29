# Homework 8 — Implementation Task Tracker

10 steps split across 3 feature branches. Each step ends with a commit and a push to `origin`.

## Branch 1 — `feature/state-pattern` (steps 1–4)
- [x] **Step 1** — Add `NormalState` (baseline) and extend `Hero.java` with a `HeroState` field, `getState`/`setState`, and turn lifecycle methods.
- [ ] **Step 2** — Add `StunnedState` (`canAct() == false`, self-decrements, transitions back to `NormalState`).
- [ ] **Step 3** — Add `PoisonedState` (self-transitioning, ticks passive damage, weakens outgoing).
- [ ] **Step 4** — Add `BerserkState` (modifies both outgoing and incoming damage, triggers via low-HP threshold).

## Branch 2 — `feature/template-method` (steps 5–8)
- [ ] **Step 5** — `CombatFloor` (monster fight, can apply states to heroes).
- [ ] **Step 6** — `TrapFloor` (automatic damage, may stun).
- [ ] **Step 7** — `RestFloor` (overrides `shouldAwardLoot()` → `false`, heals party).
- [ ] **Step 8** — `BossFloor` (overrides `announce()` hook, hard combat).

## Branch 3 — `feature/runner-and-demo` (steps 9–10)
- [ ] **Step 9** — `TowerRunner` (executes floors in sequence, halts on wipe, builds `TowerRunResult`).
- [ ] **Step 10** — Populate `Main.java` with 2 heroes / ≥4 floors / visible state transitions; verify compile + run.
