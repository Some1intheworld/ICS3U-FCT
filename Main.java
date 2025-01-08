import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
import java.awt.event.*;

public class Main extends JPanel implements KeyListener, MouseListener, Runnable {
    // JFrame icon
	public static ImageIcon iconImg = new ImageIcon("Tank Icon.png");
	
	// Game States
	// 0 <- Home Screen
    // 1 <- Credits
    // 2 <- Tank & Player Selection
    // 3 <- PVP Mode & PVP Pause Screen
	// 4 <- PVP End Scree
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
    public static BufferedImage titanTank1;
    public static BufferedImage ironcladTank1;
    public static BufferedImage mouseTank1;
    public static BufferedImage sentinelTank1;
    public static BufferedImage titanTank2;
    public static BufferedImage ironcladTank2;
    public static BufferedImage mouseTank2;
    public static BufferedImage sentinelTank2;
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
    public static String P1Name = "";
    public static String P1Tank = "Titan"; // P1 default tank is Titan
    public static double P1Wins;
    public static double P1Battles;
    public static double P1Winrate;
    
    public static int P1X = 0; 
    public static int P1Y = 685;
    
    public static int P1HP = 100;
    public static int P1Damage;
    // Player 2 (P2)
    public static String P2Name = "";
    public static String P2Tank = "Mouse"; // P2 default tank is Mouse
    public static double P2Wins;
    public static double P2Battles;
    public static double P2Winrate;
    
    public static int P2X = 2320; 
    public static int P2Y = 685;

    public static int P2HP = 100;
    public static int P2Damage;

    // General stats
    // For Tank Selection GS
    public static int currentPlayer = 1; // Default is P1
		public static int currentTurn = 1;
    public static Map<String, Integer> tankSelectLocations = new HashMap<>();
    public static String[] tankArr = {"Titan", "Mouse", "Ironclad", "Sentinel"};
	public static boolean aPressed = false;
	public static boolean dPressed = false;
	public static boolean isTypingP1 = false;
	public static boolean isTypingP2 = false;

	// For PVP Mode GS
	public static Map<String, BufferedImage> tankImages = new HashMap<>();
	
    // For PVP End Screen GS
    public static String winnerPlayer;
    
    
    // TODO game methods, maybe in separate class file?
    
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
        	}
        	// Credits button
        	else if((mouseX>=40 && mouseX<=210) && (mouseY>=600 && mouseY<=770)) {
        		gameState = 1;
        	}
        	// Leaderboard button
        	else if((mouseX>=2275 && mouseX<=2460) && (mouseY>=330 && mouseY<=515)) {
        		gameState = 5;
        	}
        	// Video tutorial button
        	else if((mouseX>=2275 && mouseX<=2460) && (mouseY>=600 && mouseY<=785)) {
        		// TODO
        		System.out.println("welcome to the video tutorial (coming soon)");
        	}
        	// START button
        	else if((mouseX>=710 && mouseX<=1715) && (mouseY>=1015 && mouseY<=1170)) {
        		gameState = 3;
        	}
        	mouseX = 0; // resets mouse position
    		mouseY = 0;
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
        			if(currentPlayer == 1)
            			P1Tank = tankArr[i];	
            		else
            			P2Tank = tankArr[i];
            		mouseX = 0;
            		mouseY = 0;
            	}
        	}	
        	
        	// Tank Name next to Player #:
        	// P1
        	g.setColor(new Color(255, 255, 255)); // white
        	g.setFont(new Font("Arial", 1, 60));
        	g.drawString(P1Tank.toUpperCase(), 525, 335);
        	// P2
        	g.drawString(P2Tank.toUpperCase(), 525, 595);
        	
        	// Indicators/border for tank chosen:
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
    		// Checks if both players' tanks are the same, then show special indicator
    		if(P1Tank == P2Tank) {
    			g2.setColor(new Color(228, 119, 47)); // Orange for both players
    			g2.drawRect(tankSelectLocations.get(P1Tank+"X")-30,
        					tankSelectLocations.get(P1Tank+"Y")-30, 330, 345);
    		}

        	// allow player to choose player name (TODO textfile streaming)
			if(mouseX >= 600 && mouseX <= 925 && mouseY >= 400 && mouseY <= 485) {
				isTypingP1 = true;
			} 
			else if(mouseX >= 600 && mouseX <= 925 && mouseY >= 665 && mouseY <= 750) {
				isTypingP2 = true;
			}
			// Exiting
			if(!(mouseX >= 600 && mouseX <= 925 && mouseY >= 400 && mouseY <= 485)) {
				isTypingP1 = false;
			} 
			else if(!(mouseX >= 600 && mouseX <= 925 && mouseY >= 665 && mouseY <= 750)) {
				isTypingP2 = false;
			}
			// Draw names
			g.setColor(new Color(0, 0, 0));
			g.setFont(new Font("Arial", 1, 50));
			g.drawString(P1Name, 615, 460);
			g.drawString(P2Name, 615, 725);
        }
        else if(gameState == 3){ // !PVP Mode GS
        	g.drawImage(PVPMode, 0, 0, null);
        	//TODO make actual game
			if(currentTurn == 1) {
				if(aPressed) P1X -= 5;
				if(dPressed) P1X += 5;
			} 
			else if (currentTurn == 2) {
				if(aPressed) P2X -= 5;
				if(dPressed) P2X += 5;
			}		
			g.drawImage(tankImages.get(P1Tank+"Tank"), P1X, P1Y, null);
			g.drawImage(tankImages.get(P2Tank+"Tank2"), P2X, P2Y, null);

        	if(isPaused) { // PVP Pause screen
        		g.drawImage(PVPPause, 0, 0, null);
        		// TODO lock controls during pause
        	}
        }
        else if(gameState == 4) { // PVP End Screen GS
        	g.drawImage(PVPEnd, 0, 0, null);
        	P2X = 2320; 
            P2Y = 685;
            P1X = 0; 
            P1Y = 685;
        	//TODO display winner player name, tank visual & tank name
        }
        else if(gameState == 5){ // PVP Leaderboard GS
        	g.drawImage(PVPLeaderboard, 0, 0, null);
        	mouseX = 0; // resets mouse position
    		mouseY = 0;
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
		
		titanTank1 		= ImageIO.read(new File("Tanks/Titan Tank1.png"));
		mouseTank1		= ImageIO.read(new File("Tanks/Mouse Tank1.png"));
		ironcladTank1 	= ImageIO.read(new File("Tanks/Ironclad Tank1.png"));
		sentinelTank1 	= ImageIO.read(new File("Tanks/Sentinel Tank1.png"));
		
		titanTank2 		= ImageIO.read(new File("Tanks/Titan Tank2.png"));
		mouseTank2		= ImageIO.read(new File("Tanks/Mouse Tank2.png"));
		ironcladTank2 	= ImageIO.read(new File("Tanks/Ironclad Tank2.png"));
		sentinelTank2 	= ImageIO.read(new File("Tanks/Sentinel Tank2.png"));
		
		titanTank 		= ImageIO.read(new File("Tanks/Titan Tank2.png"));
		mouseTank		= ImageIO.read(new File("Tanks/Mouse Tank2.png"));
		ironcladTank 	= ImageIO.read(new File("Tanks/Ironclad Tank2.png"));
		sentinelTank 	= ImageIO.read(new File("Tanks/Sentinel Tank2.png"));

    	// For Tank Selection GS
    	tankSelectLocations.put("TitanX", 1525);
    	tankSelectLocations.put("TitanY", 365);
    	tankSelectLocations.put("MouseX", 1525);
    	tankSelectLocations.put("MouseY", 815);
    	tankSelectLocations.put("IroncladX", 2045);
    	tankSelectLocations.put("IroncladY", 365);
    	tankSelectLocations.put("SentinelX", 2045);
    	tankSelectLocations.put("SentinelY", 815);
    	
		// For PVP Mode GS
		tankImages.put("TitanTank", titanTank1);
		tankImages.put("MouseTank", mouseTank1);
		tankImages.put("IroncladTank", ironcladTank1);
		tankImages.put("SentinelTank", sentinelTank1);
		tankImages.put("TitanTank2", titanTank2);
		tankImages.put("MouseTank2", mouseTank2);
		tankImages.put("IroncladTank2", ironcladTank2);
		tankImages.put("SentinelTank2", sentinelTank2);

    	// JFrame and JPanel
    	JFrame frame = new JFrame("Juggernaut Assault");
        Main panel = new Main();
        frame.setIconImage(iconImg.getImage());
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
	//  frame.setResizable(false);
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
    
    public void mouseClicked(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}
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
    	if(gameState == 1)
    		gameState = 0;
    	else if(gameState == 2 ){
			// Entering P1 name
			if(isTypingP1){
				if(P1Name.length() <= 11 && Character.isLetterOrDigit(e.getKeyChar()))
					// also checks so name only includes letters or digits. esc, shift etc 
					// won't be registered
					P1Name += e.getKeyChar();
				else if(e.getKeyCode() == 8 && P1Name.length()>0) { // Deleting letters
					P1Name = P1Name.substring(0, P1Name.length()-1);
				}	
			}
			else if(isTypingP2){
				if(P2Name.length() <= 11 && Character.isLetterOrDigit(e.getKeyChar()))
					P2Name += e.getKeyChar();
				else if(e.getKeyCode() == 8 && P2Name.length()>0) { 
					P2Name = P2Name.substring(0, P2Name.length()-1);
				}
			}

			// Exiting GS
			if(e.getKeyCode() == 27){
				gameState = 0;
			}
		}
    	else if(gameState == 3) {
	
				if(e.getKeyChar() == 'a') {
					aPressed = true;
				}
				if(e.getKeyChar() == 'd') {
					dPressed = true;
				}
    		if(e.getKeyChar() == '=') { // dev key for finishing in PVP mode
    			gameState = 4;
    		}
    	}
    	else if(gameState == 4) 
    		gameState = 5;
    	else if(gameState == 5)
    		gameState = 0;
    }
    public void keyReleased(KeyEvent e) {
			if(gameState == 3) {
				if(e.getKeyChar() == 'a') {
					aPressed = false;
				}
				if(e.getKeyChar() == 'd') {
					dPressed = false;
				}
			}
		}
}