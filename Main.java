import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

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
    public static BufferedImage tankSelection;
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
    public static String P1Tank = "Titan"; // P1 default tank is Titan
    public static double P1Wins;
    public static double P1Battles;
    public static double P1Winrate;
    
    public static int P1X; 
    public static int P1Y;
    
    public static int P1HP;
    public static int P1Damage;
    // Player 2 (P2)
    public static String P2Name;
    public static String P2Tank = "Mouse"; // P2 default tank is Mouse
    public static double P2Wins;
    public static double P2Battles;
    public static double P2Winrate;
    
    public static int P2X; 
    public static int P2Y;
    
    public static int P2HP;
    public static int P2Damage;

    // General stats
    //TODO
    // For Tank Selection GS
    public static int currentPlayer = 1; // Default is P1
    public static Map<String, Integer> tankSelectLocations = new HashMap<>();
    public static String[] tankArr = {"Titan", "Mouse", "Ironclad", "Sentinel"};
    
    // For PVP End Screen GS
    public static String winnerPlayer;
    
    
    //TODO game methods, maybe in separate class file?
    
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
        if(gameState == 0){ // Menu GS
        	g.drawImage(home, 0, 0, null);
        	// Detecting click for the menu buttons
        	// Tank selection button
        	if((mouseX>=40 && mouseX<=210) && (mouseY>=330 && mouseY<=500)) {
        		gameState = 2;
        		mouseX = 0;
        		mouseY = 0;
        	}
        	// Credits button
        	if((mouseX>=40 && mouseX<=210) && (mouseY>=600 && mouseY<=770)) {
        		gameState = 1;
        		mouseX = 0;
        		mouseY = 0;
        	}
        	// Leaderboard button
        	if((mouseX>=2275 && mouseX<=2460) && (mouseY>=330 && mouseY<=515)) {
        		gameState = 5;
        		mouseX = 0;
        		mouseY = 0;
        	}
        	// Video tutorial button
        	if((mouseX>=2275 && mouseX<=2460) && (mouseY>=600 && mouseY<=785)) {
        		//TODO
        		System.out.println("welcome to the video tutorial (coming soon)");
        		mouseX = 0;
        		mouseY = 0;
        	}
        	// START button
        	if((mouseX>=710 && mouseX<=1715) && (mouseY>=1015 && mouseY<=1170)) {
        		gameState = 3;
        		mouseX = 0;
        		mouseY = 0;
        	}
        }
        else if(gameState == 1){ // Credits GS
        	g.drawImage(credits, 0, 0, null);
        	
        }
        else if(gameState == 2){ // Tank Selection GS
        	g.drawImage(tankSelection, 0, 0, null);
        	// Player Selection:
        	// Player 1 selected
        	if((mouseX>=183 && mouseX<=440) && (mouseY>=260 && mouseY<=355)) {
        		currentPlayer = 1;
        		mouseX = 0;
        		mouseY = 0;
        	}
        	// Player 2 selected
        	else if((mouseX>=183 && mouseX<=440) && (mouseY>=523 && mouseY<=618)) {
        		currentPlayer = 2;
        		mouseX = 0;
        		mouseY = 0;
        	}
        	
        	// Selecting tanks:
        	for(int i = 0; i < 4; i++) {
        		if((mouseX>=tankSelectLocations.get(tankArr[i]+"X") &&
    				mouseX<=tankSelectLocations.get(tankArr[i]+"X")+270) && 
    				(mouseY>=tankSelectLocations.get(tankArr[i]+"Y") && 
    				mouseY<=tankSelectLocations.get(tankArr[i]+"Y")+285)) 
        		{ // using tankArr, checks for if mouseX and mouseY is within each 
        		  // tank's X and Y rectangle areas drawn from their top left corner
        	      // (hence the +270 (width) and +285 (height)), with those values from
        		  // the tankSelectLocations hashmap. Saves the need to write 4 if statements
        			if(currentPlayer == 1) {
            			P1Tank = tankArr[i];
            			System.out.printf("P1 Tank: %s%n", P1Tank); // Message for devs
            			// to confirm tank was selected
            			// TODO remove all console messages later
            		}	
            		else {
            			P2Tank = tankArr[i];
            			System.out.printf("P2 Tank: %s%n", P2Tank);
            		}	
            		mouseX = 0;
            		mouseY = 0;
            	}
        	}	
        	
        	// Markers/border for tank chosen:
        	Graphics2D g2 = (Graphics2D) g.create();
        	g2.setStroke(new BasicStroke(15)); // Setting thickness of border
        	
        	// P1 tanks' markers
    		g2.setColor(new Color(194, 164, 0)); // gold color
    		g2.drawRect(tankSelectLocations.get(P1Tank+"X")-30, // -30 for a bigger border
    					tankSelectLocations.get(P1Tank+"Y")-30, 330, 345);
        	// P2 tanks' markers
			g2.setColor(new Color(225, 74, 61)); // red color
    		g2.drawRect(tankSelectLocations.get(P2Tank+"X")-30, 
    					tankSelectLocations.get(P2Tank+"Y")-30, 330, 345);
        	
    		// Checks if both players' tanks are the same, then show special marker
    		if(P1Tank == P2Tank) {
    			g2.setColor(new Color(228, 119, 47)); // Orange for both players
    			g2.drawRect(tankSelectLocations.get(P1Tank+"X")-30,
        					tankSelectLocations.get(P1Tank+"Y")-30, 330, 345);
    		}

    		
        	//TODO allow player to choose player name (textfile streaming)
        }
        else if(gameState == 3){ // PVP Mode GS
        	g.drawImage(PVPMode, 0, 0, null);
        	//TODO make actual game
        	
        	if(isPaused) { // PVP Pause screen
        		g.drawImage(PVPPause, 0, 0, null);
        		// TODO lock controls during pause
        	}
        }
        else if(gameState == 4) { // PVP End Screen GS
        	g.drawImage(PVPEnd, 0, 0, null);
        	//TODO display winner player name, tank visual & tank name
        }
        else if(gameState == 5){ // PVP Leaderboard GS
        	g.drawImage(PVPLeaderboard, 0, 0, null);
        	//TODO display top 3 players by wins & show their winrate (textfile streaming)
        }

    }
    
    // Mouse and Keyboard Methods
    public static void main(String[] args) throws IOException{
    	// Disable Java from automatically scaling content based on system’s DPI settings
    	System.setProperty("sun.java2d.uiScale", "1.0");
    	
    	// Image Importation
    	home 			= ImageIO.read(new File("GameStates/GS0 - Menu Screen.png"));
    	credits 		= ImageIO.read(new File("GameStates/GS1 - Credit.png"));
    	tankSelection 	= ImageIO.read(new File("GameStates/GS2 - Tank Selection.png"));
    	PVPMode 		= ImageIO.read(new File("GameStates/GS3 - PVP.png"));
    	PVPPause 		= ImageIO.read(new File("PVP Pause Screen.png"));
    	PVPEnd 			= ImageIO.read(new File("GameStates/GS4 - PVP End Screen.png"));
    	PVPLeaderboard 	= ImageIO.read(new File("GameStates/GS5 - PVP Mode Leaderboard.png"));
    	// For Tank Selection GS
    	tankSelectLocations.put("TitanX", 1525);
    	tankSelectLocations.put("TitanY", 365);
    	tankSelectLocations.put("MouseX", 1525);
    	tankSelectLocations.put("MouseY", 815);
    	tankSelectLocations.put("IroncladX", 2045);
    	tankSelectLocations.put("IroncladY", 365);
    	tankSelectLocations.put("SentinelX", 2045);
    	tankSelectLocations.put("SentinelY", 815);
    	
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