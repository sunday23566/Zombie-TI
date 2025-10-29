/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.game;

/**
 *
 * @author sun23
 */
public abstract class Entity {
    public int MaxHp = 100;
    public int Hp = 100;
    public int speed = 1;
    public int x,y;
    
    public void TakeDamage(int dmg){
        Hp -= dmg;
    }
    public void TakeHeal(int amount){
        Hp+=amount;
        if(Hp >= MaxHp) Hp = MaxHp;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
}
