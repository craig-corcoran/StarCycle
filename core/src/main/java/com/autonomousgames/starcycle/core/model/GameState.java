package com.autonomousgames.starcycle.core.model;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ccor on 2/25/14.
 */

class InputState implements Serializable {

    float ammo = 0f;
    boolean[] pressed = new boolean[3];
    Vector2 pointer = new Vector2();

    public void updateState(Player player) {
        ammo = player.ammo;
        pressed[0] = player.launchPad.orbButton.pressedDown;
        pressed[1] = player.launchPad.pw1Button.pressedDown;
        pressed[2] = player.launchPad.pw2Button.pressedDown;
        pointer.set(player.base.pointer);
    }
}

public class GameState implements Serializable {

    ArrayList<Vector2>[] orbPos;
    ArrayList<Vector2>[] orbVel;
    ArrayList<Vector2>[] voidPos;
    ArrayList<Vector2>[] voidVel;
    ArrayList<Vector2>[] novaPos;
    ArrayList<Vector2>[] novaVel;
    InputState[] inputStates;
    Vector2[] starPos;

    public GameState(int numPlayers, int numStars) {

        orbPos = new ArrayList[numPlayers];
        orbVel = new ArrayList[numPlayers];
        voidPos = new ArrayList[numPlayers];
        voidVel = new ArrayList[numPlayers];
        novaPos = new ArrayList[numPlayers];
        novaVel = new ArrayList[numPlayers];
        inputStates = new InputState[numPlayers];

        for (int i=0; i < numPlayers; i++) {
            orbPos[i] = new ArrayList<Vector2>();
            orbVel[i] = new ArrayList<Vector2>();
            voidPos[i] = new ArrayList<Vector2>();
            voidVel[i] = new ArrayList<Vector2>();
            novaPos[i] = new ArrayList<Vector2>();
            novaVel[i] = new ArrayList<Vector2>();
            inputStates[i] = new InputState();
        }
        starPos = new Vector2[numStars];
    }

    public void updateState(Player[] players, Model model) {
        starPos = model.starPositions;
        for (Player p: players) {

            inputStates[p.number].updateState(p);

            for (int i=0; i < p.orbs.size(); i++) {
                orbPos[p.number].add(i, p.orbs.get(i).position);
                orbVel[p.number].add(i, p.orbs.get(i).body.getLinearVelocity());
            }

            for (int i=0; i < p.voids.size(); i++) {
                voidPos[p.number].add(i, p.voids.get(i).position);
                voidVel[p.number].add(i, p.voids.get(i).body.getLinearVelocity());
            }

            for (int i=0; i < p.novas.size(); i++) {
                novaPos[p.number].add(i, p.novas.get(i).position);
                novaVel[p.number].add(i, p.novas.get(i).body.getLinearVelocity());
            }
        }
    }
}
