/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.game;

import java.awt.Graphics2D;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

public interface Guns{
    abstract void fire();
    abstract void reload();
    abstract int getAmmo();
    abstract void draw(Graphics2D G2);
}

class AK implements Guns{
    public URL imageUrl = getClass().getResource("/Image/AK.png");
    private Image gunimg;
    
    Player player;
    GamePanel GP;
    Input_Handler input;
    boolean isReload = false;
    
    int Damage = 65;
    int MaxAmmo = 30;
    int Ammo = 30;
    int recoil = 65;
    private final long firerate_Nano = 100_000_000L; //NANO SEC
    private long lastFireTimestamp = 0;
    private final int ReloadTime = 2500; //2.5
    
    public AK(Player player, GamePanel GP,Input_Handler input) {
        this.player = player;
        this.GP = GP;
        this.input = input;
        
        try {
            gunimg = ImageIO.read(imageUrl);
        } catch (Exception e) {
        }
    }

    @Override
    public void fire() {
        long currentTime = System.nanoTime();
        
        if (currentTime - lastFireTimestamp > firerate_Nano) {
            int startX = player.getX() + 15;
            int startY = player.getY() + 30;
            //create Delay
            Bullet b = new Bullet(startX,startY, GP, input, recoil);
            b.setDmg(Damage);
            GP.bullets.add(b);
            
            if(Ammo <= 0){
                reload();
            }else{
                Ammo-=1;
            }
            lastFireTimestamp = currentTime;
        }
        
    }

    @Override
    public void reload() {
        if(isReload == true){
            player.setInQueueArm(true);
            return;
        }
        
        isReload = true;
        new Thread(() -> {
            try {
                Thread.sleep(ReloadTime); // รอ 2.5 วิ
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Ammo = MaxAmmo;
            isReload = false;
            player.setInQueueArm(false);
        }).start();
    }

    @Override
    public void draw(Graphics2D g2) {
        double diffX = input.x - player.getX(); // Mouse X - Player X
        double diffY = input.y - player.getY(); // Mouse Y - Player Y
        double angleRadians = Math.atan2(diffY, diffX);
        
        if(gunimg != null){
            Graphics2D g2d = (Graphics2D)g2.create();
            
            int centerX = player.getX() + 15;
            int centerY = player.getY() + 30;
            g2d.rotate(angleRadians, centerX, centerY);
            
            int gunWidth = 70;
            int gunHeight = 70;
            g2d.drawImage(
                    gunimg,
                    centerX,
                    centerY - gunHeight / 2,
                    gunWidth,
                    gunHeight,
                    GP
            );
            g2d.dispose();
        } 
        
    }

    @Override
    public int getAmmo() {
        return Ammo;
    }
    
}