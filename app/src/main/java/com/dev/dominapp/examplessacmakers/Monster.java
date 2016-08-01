package com.dev.dominapp.examplessacmakers;

import java.util.Random;

/**
 * Created by Calvert on 7/20/2016.
 */
enum BODY_PART{HEAD, LEFT_ARM, RIGHT_ARM, TORSO, LEFT_LEG,RIGHT_LEG,TAIL};
enum SPECIAL_POWER{LASER_EYES, RAZOR_TAIL, BREATHING_FIRE, FURY}
public class Monster {
    Random rand;
    String currentMessage;
    String name;
    BodyPart head, left_arm, right_arm, torso, left_leg, right_leg, tail;
    SpecialPower specialPower;
    public Monster() {
        name = "default name";
        rand = new Random();
        specialPower = new SpecialPower(SPECIAL_POWER.values()[rand.nextInt(SPECIAL_POWER.values().length)]);
        head = new BodyPart(BODY_PART.HEAD,rand);
        left_arm = new BodyPart(BODY_PART.LEFT_ARM,rand);
        right_arm = new BodyPart(BODY_PART.RIGHT_ARM,rand);
        torso = new BodyPart(BODY_PART.TORSO,rand);
        tail = new BodyPart(BODY_PART.TAIL,rand);
        left_leg = new BodyPart(BODY_PART.LEFT_LEG,rand);
        right_leg = new BodyPart(BODY_PART.RIGHT_LEG,rand);
    }
    public void attack(Monster defender){
        BodyPart target = chooseTargetArea();
        String targetName = target.getNameAsString();
        int damage = rand.nextInt(10)+5;
        currentMessage = name + " attacks " + defender.name + ". ";
        if (rand.nextGaussian()<-.5){
            currentMessage += " The attack hits his " + targetName;
            currentMessage += " and deals " + damage + " damage. ";
            target.hp-=damage;
            if (target.hp < 0 && !target.isBroken) {
                target.isMissing = true;
                currentMessage += defender.name + "'s " + targetName + " is broken. ";
            }
            if (target.hp < 0) {
                target.isBroken = true;
                if (target.bodyPart != BODY_PART.TORSO) currentMessage += defender.name + "'s " + targetName + " is ripped off. ";
                else currentMessage += defender.name + "'s " + targetName + " is ripped open and his guts spill on the city below them. ";
            }
            currentMessage += defender.name + "'s " + targetName + " is broken. ";
        }
    }
    public void act(Monster defender){
        if (specialPower.ticksTillReady==0){
            specialPower.enact(this,defender,rand);
            specialPower.ticksTillReady = specialPower.coolDown;
        }
        else{
            attack(defender);
        }
    }
    public BodyPart chooseTargetArea(){
        int r = 0;
        BodyPart ret = null;
        boolean foundTarget = false;
        while (!foundTarget) {
            r = rand.nextInt(7);
            if (r == 0) ret = head;
            if (r == 1) ret = torso;
            if (r == 2) ret = left_arm;
            if (r == 3) ret = right_arm;
            if (r == 4) ret = left_leg;
            if (r == 5) ret = right_leg;
            if (r == 6) ret = tail;
            if (ret == null){
                System.out.print("error in choose target area");
            }
            if (!ret.isMissing){
                foundTarget = true;
            }
        }
        return  ret;
    }
}
class BodyPart{
    int hp;
    int strength;
    boolean isBleeding = false, isBroken = false, isMissing = false;
    BODY_PART bodyPart;
    BodyPart(BODY_PART bodyPart, Random rand){
        this.bodyPart = bodyPart;

        switch (bodyPart) {
            case HEAD:
                strength = (int)(Math.abs(rand.nextGaussian()*2));
                hp = (int)(Math.abs(rand.nextGaussian()*20))+50;
                break;
            case LEFT_ARM:
                strength = (int)(Math.abs(rand.nextGaussian()*3));
                hp = (int)(Math.abs(rand.nextGaussian()*10))+strength*10;
                break;
            case RIGHT_ARM:
                strength = (int)(Math.abs(rand.nextGaussian()*3));
                hp = (int)(Math.abs(rand.nextGaussian()*10))+strength*10;
                break;
            case TORSO:
                strength = (int)(Math.abs(rand.nextGaussian()*2));
                hp = (int)(Math.abs(rand.nextGaussian()*10))+strength*15;
                break;
            case LEFT_LEG:
                strength = (int)(Math.abs(rand.nextGaussian()));
                hp = (int)(Math.abs(rand.nextGaussian()*10))+strength*10;
                break;
            case RIGHT_LEG:
                strength = (int)(Math.abs(rand.nextGaussian()));
                hp = (int)(Math.abs(rand.nextGaussian()*10))+strength*10;
                break;
            case TAIL:
                strength = (int)(Math.abs(rand.nextGaussian()*2));
                hp = (int)(Math.abs(rand.nextGaussian()*10))+strength*10;
        }
    }
    String getNameAsString() {
        String ret = "";
        switch (bodyPart) {

            case HEAD:
                ret = "head";
                break;
            case LEFT_ARM:
                ret = "left arm";
                break;
            case RIGHT_ARM:
                ret = "right arm";
                break;
            case TORSO:
                ret = "torso";
                break;
            case LEFT_LEG:
                ret = "left leg";
                break;
            case RIGHT_LEG:
                ret = "right leg";
                break;
            case TAIL:
                ret = "tail";
                break;
        }
        return ret;
    }
}
class SpecialPower {
    int coolDown;
    int ticksTillReady;
    int power;
    SPECIAL_POWER special_power;

    SpecialPower(SPECIAL_POWER special_power) {
        this.special_power = special_power;
        switch (special_power) {
            case LASER_EYES:
                coolDown = 5;
                power = 10;
                break;
            case RAZOR_TAIL:
                coolDown = 3;
                power = 7;
                break;
            case BREATHING_FIRE:
                coolDown = 20;
                power = 20;
                break;
            case FURY:
                coolDown = 2;
                power = 5;
                break;
        }
    }

    void enact(Monster attacker, Monster defender, Random rand) {
        ticksTillReady = coolDown;
        BodyPart target = attacker.chooseTargetArea();
        switch (special_power) {
            case LASER_EYES:
                if (!attacker.head.isBroken && !attacker.head.isBleeding) {
                    attacker.currentMessage = attacker.name + " shoots lasers from his eyes. At " + defender.name + "'s" + target.getNameAsString() + ". ";
                    if (rand.nextGaussian() < .5) {
                        target.hp -= rand.nextInt(power) * 10 + 1;
                        if (target.hp < 0) {
                            target.isMissing = true;
                            attacker.currentMessage += " The lasers totally melted " + defender.name + "'s" + target.getNameAsString() + ". ";
                        } else {
                            if (rand.nextGaussian() < -.2) {
                                target.isMissing = true;
                                attacker.currentMessage += " The lasers cut " + defender.name + "'s" + target.getNameAsString() + " right off. ";
                            }
                        }
                    } else {
                        attacker.currentMessage += defender.name + " dodges the laser beam of doom... or " + attacker.name + " is just blind";
                    }
                } else {
                    attacker.currentMessage += attacker.name + " tries to shoot lasers from his damaged head. It simply does not work. ";
                }
                break;
            case RAZOR_TAIL:
                if (!attacker.tail.isMissing) {
                    attacker.currentMessage = attacker.name + " whips his spiked tail with a mighty vengeance at " + defender.name + "'s" + target.getNameAsString() + ". ";
                    if (rand.nextGaussian() < .1) {
                        target.hp -= rand.nextInt(power) * 10 + 1;
                        if (target.hp < 0) {
                            target.isMissing = true;
                            attacker.currentMessage += " the tail completely destroys " + defender.name + "'s" + target.getNameAsString() + ". ";
                        } else {
                            if (rand.nextGaussian() < .7) {
                                target.isBleeding = true;
                                attacker.currentMessage += " the tail completely gouges into " + defender.name + "'s" + target.getNameAsString() + ". It is gushing blood everywhere. ";
                            }
                        }
                    } else {
                        attacker.currentMessage += defender.name + " steps back from " + attacker.name + " tail attack. ";
                    }
                } else {
                    attacker.currentMessage = attacker.name + " tried a tail attack but he is missing his tail. Monsters are not that bright. ";
                }
                break;
            case BREATHING_FIRE:
                if (!attacker.head.isBroken) {
                    attacker.currentMessage = attacker.name + " breathes fire at " + defender.name + "'s" + target.getNameAsString() + ". ";
                    if (rand.nextGaussian() < .142) {
                        target.hp -= rand.nextInt(power) * 10 + 1;
                        if (target.hp < 0) {
                            target.isMissing = true;
                            attacker.currentMessage += " the fire completely eradicates " + defender.name + "'s" + target.getNameAsString() + ". ";
                        } else {
                            if (rand.nextGaussian() < -1) {
                                target.isBleeding = true;
                                attacker.currentMessage += defender.name + " ignites and burns until there is nothing left but ash. ";
                            }
                        }
                    } else {
                        attacker.currentMessage += defender.name + " ignores " + attacker.name + " flames. ";
                    }
                } else {
                    attacker.currentMessage = attacker.name + " coughes and a little smoke escapes from his gnarly face. ";
                }
                break;
            case FURY:
                attacker.currentMessage = attacker.name + " breathes fire at " + defender.name + "'s" + target.getNameAsString() + ". ";
                if (rand.nextGaussian() < .6) {
                    target.hp -= rand.nextInt(power) * 5 + 1;
                    attacker.currentMessage = attacker.name + " ferociously attacks " + defender.name + "'s" + target.getNameAsString() + ". ";
                    break;
                }
        }
    }
}