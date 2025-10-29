package com.mycompany.game;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Image;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends JFrame{
    GamePanel GP = new GamePanel();
    public URL imageUrlIcon = getClass().getResource("/Image/icon64x64.png");
    public static Image IconImage;
    public Game() throws HeadlessException {
        add(GP);
        GP.startThread();
        try {
            IconImage = ImageIO.read(imageUrlIcon);
        } catch (Exception e) {
        }
    }
    
    public static void main(String[] args) {
        
        JFrame f = new Game();
        if(IconImage != null){
            f.setIconImage(IconImage);
        }
        f.setTitle("Zombie TI");
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        f.setSize(1024, 512);
        f.setResizable(false);
        f.setBackground(Color.BLACK);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
