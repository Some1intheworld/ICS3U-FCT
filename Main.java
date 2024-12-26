import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.event.*;

// NOTE: All GS images are currently placeholders TODO 
public class Main extends JPanel implements KeyListener, MouseListener, Runnable {
    // JFrame icon
	public static ImageIcon iconImg = new ImageIcon("Tank Icon.png");
	
	// Game States
	// 0 <- Home Screen
    // 1 <- Credits
    // 2 <- Tank & Player Selection
    // 3 <- PVP Mode & PVP Pause Screen
	// 4 <- PVP End Screen
	// 5 <- Solo Mode Leaderboard
    public static int gameState = 0;
    public static boolean isPaused = false;
    
    //public static BufferedImage[] GSArr = new BufferedImage[7];
    
    // Buffered Images
    //GS base images
    public static BufferedImage home;
    public static BufferedImage credits;
    public static BufferedImage tankselection;
    public static BufferedImage PVPMode;
    public static BufferedImage PVPPause;
    public static BufferedImage PVPEnd;
    public static BufferedImage PVPLeaderboard;
    // tanks TODO no images for tanks yet
    public static BufferedImage titanTank;
    public static BufferedImage ironcladTank;
    public static BufferedImage mouseTank;
    public static BufferedImage sentinelTank;
    // abilities TODO 
    
    
    // Mouse/Keyboard Events
    public static int mouseX;
    public static int mouseY;
    
    // Game Stats
    // Player stats, none are used yet
    // Player 1 (P1)
    public static String P1Name;
    public static String P1Tank;
    public static double P1Wins;
    public static double P1Battles;
    public static double P1Winrate;
    
    public static int P1X; 
    public static int P1Y;
    
    public static int P1HP;
    public static int P1Damage;
    // Player 2 (P2)
    public static String P2Name;
    public static String P2Tank;
    public static double P2Wins;
    public static double P2Battles;
    public static double P2Winrate;
    
    public static int P2X; 
    public static int P2Y;
    
    public static int P2HP;
    public static int P2Damage;

    // General stats
    //TODO
    public static String winnerPlayer;
    
    //TODO game methods
    
    // JPanel Settings
    public Main(){
        setPreferredSize(new Dimension(2500, 1250));
        // Adding KeyListener and MouseListener
        this.setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
        
        // Adding Timer
        Thread thread = new Thread(this);
        thread.start();
        
    }

    // Draw Screen
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(gameState == 0){
        	g.drawImage(home, 0, 0, null);
        	// Detecting click for the menu buttons
        	// Tank selection button
        	if((mouseX >= 40 && mouseX <= 210) && (mouseY >= 330 && mouseY <= 500)) {
        		gameState = 2;
        		mouseX = 0;
        		mouseY = 0;
        	}
        	// Credits button
        	if((mouseX >= 40 && mouseX <= 210) && (mouseY >= 600 && mouseY <= 770)) {
        		gameState = 1;
        		mouseX = 0;
        		mouseY = 0;
        	}
        	// Leaderboard button
        	if((mouseX >= 2275 && mouseX <= 2460) && (mouseY >= 330 && mouseY <= 515)) {
        		gameState = 5;
        		mouseX = 0;
        		mouseY = 0;
        	}
        	// Video tutorial button
        	if((mouseX >= 2275 && mouseX <= 2460) && (mouseY >= 600 && mouseY <= 785)) {
        		//TODO
        		System.out.println("welcome to the video tutorial (coming soon)");
        		mouseX = 0;
        		mouseY = 0;
        	}
        	
        	// START button
        	if((mouseX >= 710 && mouseX <= 1715) && (mouseY >= 1015 && mouseY <= 1170)) {
        		gameState = 3;
        		mouseX = 0;
        		mouseY = 0;
        	}
      
        }
        else if(gameState == 1){
        	g.drawImage(credits, 0, 0, null);
        	
        }
        else if(gameState == 2){
        	g.drawImage(tankselection, 0, 0, null);
        	//TODO allow player to choose tank
        	//TODO allow player to choose player name (textfile streaming)
        }
        else if(gameState == 3){
        	g.drawImage(PVPMode, 0, 0, null);
        	//TODO make actual game
        	
        	if(isPaused) {
        		g.drawImage(PVPPause, 0, 0, null);
        		// TODO lock controls during pause
        	}
        }
        else if(gameState == 4) {
        	g.drawImage(PVPEnd, 0, 0, null);
        	//TODO display winner player name, tank visual & tank name
        }
        else if(gameState == 5){
        	g.drawImage(PVPLeaderboard, 0, 0, null);
        	//TODO display top 3 players by wins & show their winrate (textfile streaming)
        }

    }
    
    // Mouse and Keyboard Methods
    public static void main(String[] args) throws IOException{
    	// Disable Java from automatically scaling content based on system’s DPI settings
    	System.setProperty("sun.java2d.uiScale", "1.0");
    	
    	// Image Importation
    	home 			= ImageIO.read(new File("GameStates/GS0 - Menu Screen (1).png"));
    	credits 		= ImageIO.read(new File("GameStates/GS1 - Credit.png"));
    	tankselection 	= ImageIO.read(new File("GameStates/GS2 - Tank Selection.png"));
    	PVPMode 		= ImageIO.read(new File("GameStates/GS3 - PVP.png"));
    	PVPPause 		= ImageIO.read(new File("PVP Pause Screen.png"));
    	PVPEnd 			= ImageIO.read(new File("GameStates/GS4 - PVP End Screen.png"));
    	PVPLeaderboard 	= ImageIO.read(new File("GameStates/GS5 - PVP Mode Leaderboard.png"));
    	
    	
    	// JFrame and JPanel
    	JFrame frame = new JFrame("Juggernaut Assault");
        Main panel = new Main();
        frame.setIconImage(iconImg.getImage());
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
       // frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    
    //Mandatory methods
    public void run() {
        while(true) {
            repaint();
            try {
                Thread.sleep(20); //50FPS
            }
            catch(Exception e) {}
        }
    }
    
    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {
    	mouseX = e.getX();
    	mouseY = e.getY();
    }
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {
    	if(gameState == 3) {
    		isPaused = false;
		}
    }
    public void mouseExited(MouseEvent e) {
    	if(gameState == 3) {
    		isPaused = true;
		}
    }

    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {
    	if(gameState == 2 && e.getKeyCode() == 27)
    		gameState = 0;
    	else if(gameState == 1)
    		gameState = 0;
    	else if(gameState == 5)
    		gameState = 0;
    	else if(gameState == 3) {
    		if(e.getKeyChar() == '=') { // dev key for finishing in PVP mode
    			gameState = 4;
    		}
    	}
    	else if(gameState == 4) {
    		gameState = 5;
    	}
    }
    public void keyReleased(KeyEvent e) {}
}