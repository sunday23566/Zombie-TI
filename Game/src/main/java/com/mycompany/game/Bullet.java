package com.mycompany.game;

import java.awt.*;

public class Bullet {
    private int x,y,dmg=10; //setDefualt
    private int size = 10;
    private double dx, dy;
    GamePanel GP;
    Thread rb;
    Player player;
    double rng;
    private volatile boolean active = true;
    
    public Bullet(int x,int y, GamePanel GP, Input_Handler input, double recoil) {
        this.GP = GP;
        rb = new Thread(new RunBullet());
        
        this.x = x;
        this.y = y;
        rng = (Math.random() * recoil ) + 1;
        double diffX = input.x - x;
        double diffY = input.y - y + rng;
        double length = Math.sqrt(diffX * diffX + diffY * diffY);
        this.dx = diffX / length;
        this.dy = diffY / length;
        
        rb.start();
    }
    
    class RunBullet implements Runnable{

        @Override
        public void run() {
            while (active) {
                update();
                if (x > GP.getWidth() || y > GP.getHeight() || x < 0 || y < 0) {
                    active = false;
                    GP.bullets.remove(Bullet.this);
                }
                try {
                    Thread.sleep(15);
                } catch (InterruptedException ignored) {
                }
            }
            
        }
        
    }
    private void checkHitZombie() {
        for (Zombie z : GP.zombies) {
            if (intersects(z)) {
                z.TakeDamage(this.dmg);
                active = false;
                GP.bullets.remove(Bullet.this);
                //System.out.println("HP: "+z.hp);
                break;
            }
        }
    }

    private boolean intersects(Zombie z) {
        // (Bounding Box)
        Rectangle bulletRect = new Rectangle((int) x - 4, (int) y - 4, 8, 8);
        return bulletRect.intersects(z.getBounds());
    }
    
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void setDmg(int dmg){
        this.dmg = dmg;
    }
    public boolean isActive() {
        return active;
    }
    public void update(){
        x += dx * 50;
        y += dy * 50;
        checkHitZombie();
    }
    public void draw(Graphics2D g2){
        g2.setColor(Color.YELLOW);
        g2.fillOval(x, y, size*2, size/2);
    }
}
