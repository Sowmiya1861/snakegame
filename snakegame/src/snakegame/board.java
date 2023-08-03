package snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class board extends JPanel implements ActionListener{
    private Image apple;
    private Image dot;
    private Image head;
    private final int all_dots = 900;
    private final int dot_size = 10;
    private final int random_pos = 25;
    private int apple_x;
    private int apple_y;
    private final int x[] = new int[all_dots];
    private final int y[] = new int[all_dots];
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean ingame = true;
    private int dots;
    private Timer timer;
    
    //for replay
    private boolean gameOver;
    private JButton replayB;
    
    //score
    private int score = 0;
    board(){
    	//creating object and calling the function
    	addKeyListener(new TAdapter());
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(300,300));
        setFocusable(true);
        
        loadImages();
        initGame();
    }   
    
    //loading the png images into the screen
    public void loadImages() {
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/apple.png"));
        apple = i1.getImage();
        
        ImageIcon i2 = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/dot.png"));
        dot = i2.getImage();
        
        ImageIcon i3 = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/head.png"));
        head = i3.getImage();
    }
    
    public void initGame(){
        dots = 3;
        for(int i=0; i<dots; i++){
            y[i] = 50;
            x[i] = 50 - i * dot_size;
        }
        locateApple();
        
        timer = new Timer(140, this);
        timer.start();
        
        //replay
        gameOver = false;

        replayB = new JButton("Replay");
        replayB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle the action when the "Replay" button is clicked
                replayGame();
            }
        });
    }
    
    //replay
    public void replayGame() {
        // Reset the game state
        dots = 3;
        left = false;
        right = true;
        up = false;
        down = false;
        ingame = true;

        for (int i = 0; i < dots; i++) {
            y[i] = 50;
            x[i] = 50 - i * dot_size;
        }

        locateApple();

        // Hide the "Replay" button
        remove(replayB);
        gameOver = false;

        // Start the game loop
        timer.start();

        // Repaint the board
        repaint();
    }

    
    public void locateApple() {
    	int p = (int)(Math.random() * random_pos);
    	apple_x = p * dot_size;
    	p = (int)(Math.random() * random_pos);
    	apple_y = p * dot_size;
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
        //score
        drawScore(g);
    }
    
    public void draw(Graphics g){
    	if(ingame) {
	    	g.drawImage(apple, apple_x, apple_y, this);
	    	
	        for(int i=0; i<dots; i++){
	            if(i == 0){
	                g.drawImage(head, x[i], y[i], this);
	            }
	            else{
	                g.drawImage(dot, x[i], y[i], this);
	            }
	        }
	        Toolkit.getDefaultToolkit().sync();
    	}
    	else {
    		gameOver(g);
    	}
    }
    
  //for score

  	public void drawScore(Graphics g) {
  	    String scoreStr = "Score: " + score;
  	    Font font = new Font("SAN_SERIF", Font.BOLD, 14);
  	    FontMetrics metrics = getFontMetrics(font);
  	    g.setColor(Color.WHITE);
  	    g.setFont(font);
  	    g.drawString(scoreStr, 10, 20); // Draw the score at position (10, 20)
  	}
  	
    public void gameOver(Graphics g) {
    	String msg = "Game Over!";
    	Font font = new Font("SAN_SERIF", Font.BOLD, 14);
    	FontMetrics metrics = getFontMetrics(font);
    	g.setColor(Color.WHITE);
    	g.setFont(font);
    	g.drawString(msg, (300 - metrics.stringWidth(msg))/2, 300/2);
    	
    	//replay
    	if (gameOver) {
            int buttonWidth = 80;
            int buttonHeight = 30;
            int buttonX = (300 - buttonWidth) / 2;
            int buttonY = 300 / 2 + 30;
            replayB.setBackground(Color.GRAY);
            replayB.setForeground(Color.WHITE);
            replayB.setFont(new Font("SAN_SERIF", Font.BOLD, 12));
            //score
            String scoreStr = "Your Score: " + score;
            g.drawString(scoreStr, (300 - metrics.stringWidth(scoreStr)) / 2, 300 / 2 + 20); // Display the score below the "Game Over" message
            replayB.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
            add(replayB);
        }
    }
    
    public void move() {
    	//>=0 is wrong because we are moving the head and then the body to the place of the head so when the head moves the the body follows so when it is >=0 then the head moves but the body will be out of bounds
    	for(int i=dots; i>0; i--) {
    		x[i] = x[i-1];
    		y[i] = y[i-1];
    	}
    	if(left) {
    		x[0] = x[0] - dot_size;
    	}
    	if(right) {
    		x[0] = x[0] + dot_size;
    	}
    	if(up) {
    		y[0] = y[0] - dot_size;
    	}
    	if(down) {
    		y[0] = y[0] + dot_size;
    	}
    	
    }
    
    //check and eat the apple
    public void checkApple() {
    	if((x[0] == apple_x) && (y[0] == apple_y)) {
    		dots++;
    		//score
    		score+=dots;
    		//locates the next location of the apple and that location passed on to the apple_x and y
    		locateApple();
    	}
    }
    
    //when the snake collides with each other
    public void checkCollision() {
    	for(int i=dots; i>0; i--) {
    		if(i > 4 && x[0] == x[i] && y[0] == y[i]) {
    			ingame = false;
    		}
    	}
    	if(y[0] >= 300) {
    		ingame = false;
    	}
    	if(x[0] >= 300) {
    		ingame = false;
    	}
    	if(y[0] < 0) {
    		ingame = false;
    	}
    	if(y[0] >= 300) {
    		ingame = false;
    	}
    	if(x[0] < 0) {
    		ingame = false;
    	}
    	
    	if(!ingame) {
    		timer.stop();
    		gameOver = true;
    		repaint();
    	}
    }

	public void actionPerformed(ActionEvent e) {
		if(ingame) {
			checkApple();
			checkCollision();
			move();	
			repaint();
		}
		
	}
	
	public class TAdapter extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			//here only the snake can take the left turn when the snake is not in the right direction so the not right have been used
			if((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A)&& (!right)) {
				left = true;
				up = false;
				down = false;
			}
			if((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D)&& (!left)) {
				right = true;
				up = false;
				down = false;
			}
			if((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && (!down)) {
				left = false;
				up = true;
				right = false;
			}
			if((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && (!up)) {
				left = false;
				down = true;
				right = false;
			}
			
		}
	}
	
	

}
