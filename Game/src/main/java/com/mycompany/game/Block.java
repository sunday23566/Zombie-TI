
package com.mycompany.game;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.net.URL;
import javax.imageio.ImageIO;

public abstract class Block extends Entity{
    protected int MaxHp = 500;
    protected int Hp = 500;
    protected int x,y,width;
    protected GamePanel GP;
    public Block(int x, int y,int width,GamePanel GP) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.GP = GP;
    }
    abstract void active();
    abstract void active(Zombie z);
    abstract void Destroy();
    abstract void draw(Graphics2D g2);
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, width);
    }

    public int getWidth() {
        return width;
    }
    
}

class Barricade extends Block{
    String path;
    int restance = 12;
    public Barricade(int x, int y, int width,GamePanel GP) {
        super(x, y, width,GP);
    }
    @Override
    void active() {
    }
    @Override
    public void TakeDamage(int dmg){
        Hp-= dmg - restance;
    }
    @Override
    void draw(Graphics2D g2) {
    }

    @Override
    void Destroy() {
    }

    @Override
    void active(Zombie z) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
class House extends Block{
    //house dosn't require x,y becuse it fix
    String path = "/Image/house.png";
    URL imgUrl = getClass().getResource(path);
    Image image;
    
    int restance = 20;
    public House(int x, int y, int width,GamePanel GP) {
        super(x, y, width,GP);
        this.x = 0;
        this.y = 350;
        this.width = 120;
        try {
            image = ImageIO.read(imgUrl);
        } catch (Exception e) {
        }
    }
    @Override
    void active() {
    }
    @Override
    public void TakeDamage(int dmg){
        Hp-= dmg - restance;
        active();
        if (Hp<=0) {
            GP.currentState = GamePanel.GameState.GAMEOVER;
            this.Destroy();
        }
    }
    public void Destroy(){
        GP.Blocks.remove(this);
    }
    @Override
    public void TakeHeal(int amount){
        Hp += amount;
        if(Hp > MaxHp){
            this.MaxHp=Hp;
        }
    }
    @Override
    void draw(Graphics2D g2) {
        try {
            g2.drawImage(image, -200, 100, 400, 400, GP);
            g2.setColor(Color.GRAY);
            g2.fillRect(95, 350,  20, 1200);
            g2.fillRect(5, 220, this.MaxHp/2, 7);
            g2.setColor(Color.GREEN);
            g2.fillRect(5, 222, this.Hp/2, 5);
            //g2.drawRect(x, y, width*4, 1024);
            //g2.drawRect(x, y, width, width);
        } catch (Exception e) {
        }
    }

    @Override
    void active(Zombie z) {
    }
}

class Mine extends Block{
    String path = "/Image/landmine.png";
    Image image;
    URL imgUrl = getClass().getResource(path);
    
    int restance = 9999;
    int damage = 500;
    public Mine(int x, int y, int width,GamePanel GP) {
        super(x, y, width,GP);
        this.x = x;
        this.y = y;
        this.width = width;
        try {
            image = ImageIO.read(imgUrl);
        } catch (Exception e) {
        }
    }
    @Override
    void active() {
    }
    @Override
    public void TakeDamage(int dmg){
        Hp-= dmg - restance;
        active();
    }
    public void Destroy(){
        GP.Blocks.remove(this);
    }
    @Override
    public void TakeHeal(int amount){
        Hp += amount;
        if(Hp + amount > MaxHp){
            this.MaxHp+=amount;
            this.Hp = MaxHp;
        }
    }
    @Override
    void draw(Graphics2D g2) {
        if (image != null) {
            g2.drawImage(image, x, y, width, width, GP);
            g2.drawRect(x, y, width, width);
        } else {
            g2.drawRect(x, y, width, width);
        }
    }

    @Override
    void active(Zombie z) {
        z.TakeDamage(damage);
        this.Destroy();
    }
}

class Trap extends Block {

    String path = "/Image/beartrap.png";
    Image image;
    URL imgUrl = getClass().getResource(path);

    int restance = 9999;
    int damage = 200;
    int radius = 35;
    
    private long trapStopTime = 0; // 
    private final long TRAP_DURATION = 2500; // 2500  = 2.5 sec

    public Trap(int x, int y, int width, GamePanel GP) {
        super(x, y, width, GP);
        this.x = x;
        this.y = y;
        this.width = width;
        try {
            image = ImageIO.read(imgUrl);
        } catch (Exception e) {
        }
    }

    @Override
    void active() {
    }

    @Override
    public void TakeDamage(int dmg) {
        Hp -= dmg - restance;
        active();
    }

    public void Destroy() {
        GP.Blocks.remove(this);
    }

    @Override
    public void TakeHeal(int amount) {
        Hp += amount;
        if (Hp + amount > MaxHp) {
            this.MaxHp += amount;
            this.Hp = MaxHp;
        }
    }
    @Override
    public Rectangle getBounds() {
        int yStart = 320;
        int height = 410 - 320 + 70; 
        return new Rectangle(x, yStart, width, height);
    }

    @Override
    void draw(Graphics2D g2) {
        if (image!=null) {
            g2.drawImage(image, x, y, width, width, GP);
            g2.drawRect(x, y, width, width);
        }else{
            g2.drawRect(x, y, width, 1024);
        }
    }
    
    @Override
    void active(Zombie z) {
        z.TakeDamage(damage);
        z.speed=0.5;
        this.Destroy();
    }
}
