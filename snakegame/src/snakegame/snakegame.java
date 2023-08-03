package snakegame;

import javax.swing.*;

public class snakegame extends JFrame{
    snakegame(){
        super("Snake Game");
        add(new board());
        //similar to refresh
        pack();
        
        setResizable(false);
        setLocationRelativeTo(null); //automatically sets in the center of the screen
    }    
    public static void main(String[] args) {
        snakegame s = new snakegame();
        s.setVisible(true);
    }
}

