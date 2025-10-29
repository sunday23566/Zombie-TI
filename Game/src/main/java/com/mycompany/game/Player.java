package com.mycompany.game;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Player extends Entity{
    private int x=80,y=400,hp = 100;
    double speed = 2;
    Input_Handler input;
    GamePanel GP;
    
    String path = "/Image/player_stand.png";
    URL imgURL = getClass().getResource(path);
    Image img;
    
    private boolean inAction = false;
    private boolean unMovable = false;
    private boolean inQueueArm = false;
    
    Guns gun;
    
    public Player(GamePanel GP,Input_Handler input) {
        this.GP = GP;
        this.input = input;
        gun = new AK(this, GP,input);
        try {
            img = ImageIO.read(imgURL);
        } catch (Exception e) {
            System.out.println("Not found" + path);
        }
    }
    
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void getX(int x){
        this.x = x;
    }
    public void getY(int y){
        this.y = y;
    }
    
    void update(){
        if (inAction == false ) {
            if(unMovable == false){
                if (input.up == true && y >= 320) {
                    y -= speed;
                }
                if (input.down == true && y <= 420) {
                    y += speed;
                }
                if (input.left == true && x >= 8) {
                    x -= speed;
                }
                if (input.right == true && x <= 80) { //before 900
                    x += speed;
                }
            }
            if(inQueueArm == false){
                if (input.click == true) {
                    gun.fire();
                    if(x >= 10){
                        x -= 1;
                    }
                }
                if (input.r == true) {
                    gun.reload();
                }
            }
        }
    }
    
    void setInAction(boolean b){
        this.inAction = b;
    }
    void setunMoveable(boolean b){
        this.unMovable = b;
    }
    void setInQueueArm(boolean b){
        this.inQueueArm = b;
    }
    void draw(Graphics2D g2){
        if(img != null){
            g2.drawImage(img, x-25, y-25, 120, 120, GP);
            g2.setColor(Color.red);
            //g2.drawRect(x, y, 40, 80);
        }else{
            g2.setColor(Color.red);
            g2.drawRect(x, y, 40, 80);
        }
    }
}
