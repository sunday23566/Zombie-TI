package com.mycompany.game;

import java.awt.event.*;

public class Input_Handler implements KeyListener,MouseMotionListener,MouseListener{
    public static boolean up=false,down=false,left=false,right=false,click=false,r=false,sp=false;
    public int x=0,y=0;
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_W){
            up = true;
        }
        if(key == KeyEvent.VK_A){
            left = true;
        }
        if(key == KeyEvent.VK_S){
            down = true;
        }
        if(key == KeyEvent.VK_D){
            right = true;
        }
        if (key == KeyEvent.VK_R) {
            r = true;
        }
        if (key == KeyEvent.VK_SPACE) {
            sp = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            up = false;
        }
        if (key == KeyEvent.VK_A) {
            left = false;
        }
        if (key == KeyEvent.VK_S) {
            down = false;
        }
        if (key == KeyEvent.VK_D) {
            right = false;
        }
        if (key == KeyEvent.VK_R) {
            r = false;
        }
        if (key == KeyEvent.VK_SPACE) {
            sp = false;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.x = e.getX();
        this.y = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.x = e.getX();
        this.y = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        click = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        click = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
    
}
