package com.mycompany.game;

import java.awt.*;
import java.awt.Rectangle;
import java.net.URL;
import javax.imageio.ImageIO;
import java.io.IOException;

public abstract class Zombie extends Entity{
    protected Image img;
    private float dx, dy;
    GamePanel GP;
    int resis = 0;
    double speed = 1;
    int MinY=320,MaxY=410;
    int width,height;
    int dmg = 30;
    
    private long lastAttackTime = 0;
    private final long attackCooldown = 1000;
    
    double rng;
    
    public Zombie(int hp,int dmg,GamePanel GP) {
        rng = (Math.random()*(MaxY-MinY) ) + MinY;
        this.y = (int) rng;
        this.x = 1000;
        this.Hp = hp;
        this.dmg = dmg;
        this.GP = GP;
    }
    public void update() {
        float diffX = GP.player.getX() - x;
        float diffY = GP.player.getY() - y;
        float length = (float) Math.sqrt(diffX * diffX + diffY * diffY);
        
        if (length != 0) { //anti / 0
            this.dx = diffX / length;
            this.dy = diffY / length;
        }
        
        //nextMove
        float nextX = (float) (x + dx * speed);
        float nextY = (float) (y + dy * speed);

        Block blockCollision = null;
        for (Block b : GP.Blocks) {
            if (isColliding(nextX, nextY, b)) {
                blockCollision = b;

                if (b instanceof Barricade || b instanceof House) {
                    attack(b);
                } else {
                    b.active(this);
                }
                break;
            }
        }

        if (blockCollision == null) {
            x = (int) nextX;
            y = (int) nextY;
        }
    }
    private boolean isColliding(float nextX, float nextY, Block b) {
        Rectangle zombieNextRect = new Rectangle((int) nextX, (int) nextY, width, height);
        return zombieNextRect.intersects(b.getBounds());
    }
    
    public void loadSetup(String path){
        try {
            URL imgURL = getClass().getResource(path);
            img = ImageIO.read(imgURL);
            
        } catch (Exception e) {
        }
    }

    public int getWidth() {
        return width;
    }
    public void Destroy(){
        GP.zombies.remove(Zombie.this);
    }
    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void TakeDamage(int dmg) {
        int actualDamage = Math.max(0, dmg - resis);
        Hp-= actualDamage;
        //System.out.println("Zom takes damage: " + actualDamage + ". New HP: " + Hp);
        if(Hp <= 0) Destroy();
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    public void attack(Block targetBlock) {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastAttackTime >= attackCooldown) {

            targetBlock.TakeDamage(this.dmg);

            System.out.println("Zombie attack Block! HP Block: " + targetBlock.Hp);

            lastAttackTime = currentTime;
        }
    }
    abstract void draw(Graphics2D g2);
}
class NomalZombie extends Zombie{
    String path = "/Image/zb_normal.png";
    

    public NomalZombie(int hp, int dmg,GamePanel GP) {
        super(hp, dmg, GP);
        setHeight(70);
        setWidth(70);
        loadSetup(path);
        this.speed = 1.5;
    }

    @Override
    void draw(Graphics2D g2) {
        if(img != null){
            g2.drawImage(img, x-20, y, 70, 70, GP);
            g2.setColor(Color.GREEN);
            //g2.drawRect(x, y, 30, 70);
        }else{
            g2.setColor(Color.GREEN);
            g2.drawRect(x, y, 30, 70);
        }
    }   
}
class SpeedZombie extends Zombie {

    String path = "/Image/zb_normal.png";

    public SpeedZombie(int hp, int dmg, GamePanel GP) {
        super(hp, dmg, GP);
        setHeight(70);
        setWidth(70);
        loadSetup(path);
        this.speed = 2.5;
    }

    @Override
    void draw(Graphics2D g2) {
        if (img != null) {
            g2.drawImage(img, x - 20, y, 70, 70, GP);
            g2.setColor(Color.GREEN);
            //g2.drawRect(x, y, 30, 70);
        } else {
            g2.setColor(Color.GREEN);
            g2.drawRect(x, y, 30, 70);
        }
    }
}
class Tank extends Zombie {
    int resistance = 25;
    String path = "/Image/zb_tank.png";

    public Tank(int hp, int dmg, GamePanel GP) {
        super(hp, dmg, GP);
        setHeight(120);
        setWidth(120);
        loadSetup(path);
        this.resis = resistance;
    }

    @Override
    void draw(Graphics2D g2) {
        if (img != null) {
            g2.drawImage(img, x, y, 120, 120, GP);
            g2.setColor(Color.GREEN);
            //g2.drawRect(x, y, width, height);
        } else {
            g2.setColor(Color.GREEN);
            g2.drawRect(x, y, width, height);
        }
    }

}
