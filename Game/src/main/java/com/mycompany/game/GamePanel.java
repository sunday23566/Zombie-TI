package com.mycompany.game;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel{
    
    volatile boolean flag = true;
    int actionPoint = 12;
    int repairPoint = 0;
    int findresourcePoint = 0;
    int trap = 0;
    int mine = 0;
    
    String toggle = "";
    
    int day = 1;
    private Input_Handler input = new Input_Handler();
    private Thread rt;
    Player player = new Player(this,input);

    public GamePanel(Thread rt, Image backgroundImage, CopyOnWriteArrayList<Zombie> zombies, CopyOnWriteArrayList<Block> Blocks, CopyOnWriteArrayList<Bullet> bullets) {
        this.rt = rt;
        this.backgroundImage = backgroundImage;
        this.zombies = zombies;
        this.Blocks = Blocks;
        this.bullets = bullets;
    }

    private void spawnZombie() {
        Random rng = new Random();
        int spawnType;
        if (ZombieAmout < MaxZombie) {
            spawnType = rng.nextInt(100);
            if(spawnType < 70){
                zombies.add(new NomalZombie((int) (baseHp + bonusHp), 35 + day * 2, this));
            }else if(spawnType < 20){
                zombies.add(new SpeedZombie((int) (baseHp + bonusHp)-35, 25 + day * 2, this));
            }else if (spawnType < 10 && spaciel < MaxSpaciel) {
                //Tank!!!
                zombies.add(new NomalZombie((int) (baseHp + bonusHp), 35 + day * 2, this));
                spaciel++;
            }
            ZombieAmout++;
        }
    }

    private void preitem(String item) {
        toggle = item;
    }

    private void placeItem(int x,int y,int width,String item) {
        switch (item) {
            case "Mine":
                Blocks.add(new Mine(x, y, width, this));
                System.err.println("Add mind");
                break;
            case "Trap":
                Blocks.add(new Trap(x, y, width, this));
                break;
            default: 
                System.err.println("Not found" + item);
                break;
        }
    }
    
    public enum GameState {
        UPGRADE,
        PLANNING,
        FIGHTING,
        GAMEOVER
    }
    
    JButton btn = new JButton("Next");
    
    public GameState currentState = GameState.UPGRADE;
    
    int baseHp = 150;
    int MaxZombie = day * 5;
    int spawnRate = 4000;
    int ZombieAmout = 0;
    int spaciel = 0;
    int MaxSpaciel = day / 5;
    double bonusHp = day * 10;
    
    public URL imageUrlBG2 = getClass().getResource("/Image/background.jpg");
    public URL imageUrlBG = getClass().getResource("/Image/backgroundFullmoon.jpg");
    public URL imageUrlBGDay = getClass().getResource("/Image/backgroundDay.jpg");
    public URL imageUrlBullet = getClass().getResource("/Image/bullet.png");
    public URL imageUrlHouse = getClass().getResource("/Image/house.png");
    public URL imageUrlThink= getClass().getResource("/Image/think.jpg");
    public URL imageUrlNext= getClass().getResource("/Image/next.png");
    public URL imageUrlTrap= getClass().getResource("/Image/beartrap.png");
    public URL imageUrlMine= getClass().getResource("/Image/landmine.png");
    public URL imageUrlPlus= getClass().getResource("/Image/plus.png");
    public URL imageUrlMinus= getClass().getResource("/Image/minus.png");
    private Image backgroundImage;
    private Image nextImage;
    private Image trapImage;
    private Image mineImage;
    private Image plusImage,minusImage;
    
    private final Rectangle UPGRADE_NEXT_BUTTON_BOUNDS = new Rectangle(370, 340, 100, 50);
    private final Rectangle PLANNING_NEXT_BUTTON_BOUNDS = new Rectangle(120, 365, 100, 50);
    private final Rectangle plus_repair = new Rectangle(120, 180, 24, 24);
    private final Rectangle minus_repair = new Rectangle(170, 180, 24, 24);
    private final Rectangle plus_findre = new Rectangle(120, 220, 24, 24);
    private final Rectangle minus_findre = new Rectangle(170, 220, 24, 24);
    private final Rectangle mineIcon = new Rectangle(120, 100, 100, 100);
    private final Rectangle trapIcon = new Rectangle(120, 230, 100, 100);
    
    public CopyOnWriteArrayList<Zombie> zombies;
    public CopyOnWriteArrayList<Block> Blocks;
    public CopyOnWriteArrayList<Bullet> bullets;
    
    int FPS = 144;

    public GamePanel() {
        zombies = new CopyOnWriteArrayList<>();
        Blocks = new CopyOnWriteArrayList<>();
        bullets = new CopyOnWriteArrayList<>();
        
        
        setPreferredSize(new Dimension(1024,512));
        this.addKeyListener(input);
        this.addMouseListener(input);
        this.addMouseMotionListener(input);
        setFocusable(true);
        rt  = new Thread(new Runtime());
        
        Blocks.add(new House(0, 0, 100, this));
        zombies.add(new Tank(baseHp*10, 45, this));
        
        try {
            backgroundImage = ImageIO.read(imageUrlBG);
            nextImage = ImageIO.read(imageUrlNext);
            mineImage = ImageIO.read(imageUrlMine);
            trapImage = ImageIO.read(imageUrlTrap);
            plusImage = ImageIO.read(imageUrlPlus);
            minusImage = ImageIO.read(imageUrlMinus);
        } catch (Exception e) {
        }
        
        System.out.println(""+ currentState);
    }
    
    public void startThread(){
        rt.start();
    }
    
    class Runtime implements Runnable{
        double drawInterVal = 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterVal;

        @Override
        public void run() {
            while (flag) {   
                update();
                repaint();
                
                try {
                    double reaminTime = nextDrawTime  - System.nanoTime();
                    reaminTime = reaminTime / 1000000; //1 sec for nano time
                    if(reaminTime < 0) reaminTime = 0;
                    Thread.sleep((long) reaminTime);
                    nextDrawTime += drawInterVal;
                } catch (InterruptedException e) {
                }
            } 
        }
        
    }
    
    public void checkClick() {
        int mouseX = input.x;
        int mouseY = input.y;

        if (currentState == GameState.UPGRADE) {
            if (plus_findre.contains(mouseX, mouseY)) {
                input.click = false; //anti spam
                if(actionPoint > 0){
                    actionPoint--;
                    findresourcePoint++;
                }
            }
            if (minus_findre.contains(mouseX, mouseY)) {
                input.click = false;
                if(findresourcePoint > 0){
                    actionPoint++;
                    findresourcePoint--;
                }
            }
            if (plus_repair.contains(mouseX, mouseY)) {
                input.click = false;
                if(actionPoint > 0){
                    actionPoint--;
                    repairPoint++;
                }
            }
            if (minus_repair.contains(mouseX, mouseY)) {
                input.click = false;
                if(repairPoint > 0){
                    actionPoint++;
                    repairPoint--;
                }
            }
            if (UPGRADE_NEXT_BUTTON_BOUNDS.contains(mouseX, mouseY)) {
                double rng;
                for (int i = 0; i < findresourcePoint; i++) {
                    rng = (Math.random() * 10);
                    if((int) rng == 1){
                        trap++;
                    }
                    if((int) rng == 9){
                        mine++;
                    }
                }
                for (int i = 0; i < repairPoint; i++) {
                    for (Block b:Blocks) {
                        b.TakeHeal((int) (20 + bonusHp));
                    }
                }
                currentState = GameState.PLANNING;
                input.click = false; // anti spam
                System.out.println("State Changed: UPGRADE -> PLANNING");
            }
        } else if (currentState == GameState.PLANNING) {
            if (trapIcon.contains(mouseX, mouseY) && trap > 0) {
                input.click = false;
                preitem("Trap");
                trap--;
            }
            if (mineIcon.contains(mouseX, mouseY) && mine > 0) {
                input.click = false;
                preitem("Mine");
                mine--;
            }
            if (PLANNING_NEXT_BUTTON_BOUNDS.contains(mouseX, mouseY)) {
                currentState = GameState.FIGHTING;
                input.click = false; // anti spam
                System.out.println("State Changed: PLANNING -> FIGHTING");
            }
        }
    }
    private long lastSpawnTime = 0;
    void update(){
        if (input.click == true) {
            checkClick();
        }
        //new Day
        
        switch (currentState) {
            case FIGHTING:
                
                player.update();
                for (Zombie zom : zombies) {
                    zom.update();
                }
                
                long now = System.currentTimeMillis();
                if (now - lastSpawnTime >= spawnRate) {
                    spawnZombie();
                    lastSpawnTime = now;
                }
                
                if (day % 5 == 0) {
                    if (spawnRate > 1500) {
                        spawnRate -= 250;
                    }
                    baseHp += 25;
                    player.speed = 1;
                    actionPoint += 3;
                    try {
                        backgroundImage = ImageIO.read(imageUrlBG);
                    } catch (Exception e) {
                    }

                } else {
                    player.speed = 2;
                    try {
                        backgroundImage = ImageIO.read(imageUrlBG2);
                    } catch (Exception e) {
                    }
                }
                
                if (ZombieAmout >= MaxZombie && zombies.isEmpty()) {
                    ZombieAmout = 0;
                    findresourcePoint = 0;
                    repairPoint = 0;
                    spaciel = 0;
                    baseHp += 25;
                    day++;
                    actionPoint += 8;
                    input.click = false;

                    currentState = GameState.UPGRADE;
                }
                
                break;
            case UPGRADE:
                try {
                    backgroundImage = ImageIO.read(imageUrlThink);
                } catch (Exception e) {
                }

                break;
            case PLANNING:
                try {
                        backgroundImage = ImageIO.read(imageUrlBGDay);
                    } catch (Exception e) {
                    }
                
                break;
            case GAMEOVER:
                break;
            default:
                throw new AssertionError();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);   
        
        Graphics2D g2 = (Graphics2D)g;
        Font newFont2 = new Font("Arial", Font.BOLD, 50);
        Font newFont3 = new Font("Arial", Font.BOLD, 30);
        Font newFont = new Font("Arial", Font.BOLD, 20);
        
        
        switch (currentState) {
            case FIGHTING:
                if (backgroundImage != null) {
                    g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
                for (Block b : Blocks) {
                    b.draw(g2);
                }
                
                g2.setColor(Color.GRAY);
                g2.fillRect(0, 0, 100, 50);
                
                g2.setColor(Color.WHITE);
                
                g2.setFont(newFont);
                
                try {
                    g2.drawImage(ImageIO.read(imageUrlBullet), 0, 0, 50, 50, this);
                } catch (Exception e) {
                }
                g2.drawString(player.gun.getAmmo() + "", 60, 25);
                
                player.draw(g2);
                player.gun.draw(g2);

                for (Bullet bul : bullets) {
                    bul.draw(g2);
                }
                for (Zombie zom : zombies) {
                    zom.draw(g2);
                }
//                g2.setColor(Color.GREEN);
//                g2.drawRect(0, 400, 2000, 70);
                break;
            case UPGRADE:
                    g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    g2.drawImage(nextImage, 320, 260, 200, 200, this);
                    //g2.drawRect(370, 340, 100, 50);
                    
                    g2.setColor(Color.BLACK);
                    g2.setFont(newFont3);
                    g2.drawString("Day: " + day, 235, 120);
                    g2.drawString("Action Point: " + actionPoint, 235, 300);
                    
                    g2.drawString("Repair: " + repairPoint, 225, 200);
                    g2.drawString("Find Resource: " + findresourcePoint, 225, 235);
                    
                    //repair
                    g2.drawImage(plusImage, 120, 180, 24, 24, this);
                    g2.drawImage(minusImage, 170, 180, 24, 24, this);
                    
                    //find resource
                    g2.drawImage(plusImage, 120, 220, 24, 24, this);
                    g2.drawImage(minusImage, 170, 220, 24, 24, this);
                break;
            case PLANNING:
                
                g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                
                for (Block block : Blocks) {
                    block.draw(g2);
                }
                g2.setColor(Color.GRAY);
                g2.fillRect(0, 0, 300, 512);
                
                g2.drawImage(trapImage, 70, 180, 200, 200, this);
                g2.drawImage(mineImage, 70, 60, 200, 200, this);
                g2.drawImage(nextImage, 70, 290, 200, 200, this);
                
                g2.setFont(newFont3);
                g2.setColor(Color.WHITE);
                g2.drawString("x"+mine, 40, 180);
                g2.drawString("x"+trap, 40, 290);
//                g2.setColor(Color.red);
//                g2.drawRect(120, 365, 100, 50);
                
//                g2.setColor(Color.GREEN);
//                g2.drawRect(0, 400, 2000, 70);
                break;
            case GAMEOVER:
                g2.setColor(Color. BLACK);
                g2.fillRect(day, day, getWidth(), getHeight());
                
                g2.setColor(Color.red);
                
                g2.setFont(newFont2);
                g2.drawString("GAME OVER!", getWidth()/3, getHeight()/2);
                break;
            default:
                throw new AssertionError();
        }
        if(toggle != "" && currentState == GameState.PLANNING){
            if (input.click == true && input.x >300 && input.x<950 && input.y > 350 && input.y < 510) {
                input.click = false;
                placeItem(input.x-35,input.y-35, 70, toggle);
                toggle = "";
            }
            if(toggle == "Mine"){
                g2.drawImage(mineImage, input.x - 35, input.y - 35, 70, 70, this);
                g2.drawRect(input.x - 35, input.y - 35, 70, 70);
            }else if(toggle == "Trap"){
                g2.drawImage(trapImage, input.x - 35, input.y - 35, 70, 70, this);
                g2.drawRect(input.x - 35, input.y - 35, 70, 70);
            }
        }
        g2.dispose();
    }
}