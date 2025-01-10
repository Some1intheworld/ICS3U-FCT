import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
import java.awt.event.*;

public class Main extends JPanel implements KeyListener, MouseListener, Runnable {
    // JFrame icon
	public static ImageIcon iconImg = new ImageIcon("Tanks/Mouse Tank.png");
	
	// ! Game States
	// 0 <- Home Screen
    // 1 <- Credits
    // 2 <- Tank & Player Selection
    // 3 <- PVP Mode & PVP Pause Screen
	// 4 <- PVP End Screen
	// 5 <- Solo Mode Leaderboard
    public static int gameState = 0;
    public static boolean isPaused;
    
    // ! Buffered Images
    //GS base images
    public static BufferedImage home;
    public static BufferedImage credits;
    public static BufferedImage tankSelection;
    public static BufferedImage PVPMode;
    public static BufferedImage PVPPause;
    public static BufferedImage PVPEnd;
    public static BufferedImage PVPLeaderboard;
    // tanks
    public static BufferedImage titanTank;
    public static BufferedImage ironcladTank;
    public static BufferedImage mouseTank;
    public static BufferedImage sentinelTank;

	// Objects Images
	public static BufferedImage crate;
	public static BufferedImage rock;
	public static BufferedImage wall;
    
    // ! Mouse/Keyboard Events
    public static int mouseX;
    public static int mouseY;
    
    public static boolean mouseReset = true;
    // ! Game Stats
    // Player 1 (P1)
    // Tank Selection GS & Leaderboard GS
    public static String P1Name = "";
    public static String P1Tank = "Titan"; // P1 default tank is Titan
    public static double P1Wins;
    public static double P1Battles;
    public static double P1Winrate;
    // PVP Mode GS
    public static int P1X = 0; 
    public static int P1Y = 685;
    public static boolean P1GoingRight = true;
    
    public static int P1HP = 100;
    public static int P1Damage;
    public static boolean P1Dead;
    
    // Player 2 (P2)
    // Tank Selection GS & Leaderboard GS
    public static String P2Name = "";
    public static String P2Tank = "Mouse"; // P2 default tank is Mouse
    public static double P2Wins;
    public static double P2Battles;
    public static double P2Winrate;
    // PVP Mode GS
    public static int P2X = 2320; 
    public static int P2Y = 685;
    public static boolean P2GoingRight;
    
    public static int P2HP = 100;
    public static int P2Damage;
    public static boolean P2Dead;
    
    // ! General stats
    // For Tank Selection GS
    public static int currentPlayer = 1; // Default is P1
	public static Map<String, Integer> tankSelectLocations = new HashMap<>();
    public static String[] tankArr = {"Titan", "Mouse", "Ironclad", "Sentinel"};
	
	public static boolean isTypingP1;
	public static boolean isTypingP2;

	// For PVP Mode GS
	public static Map<String, BufferedImage> tankImages = new HashMap<>();
	public static Map<String, Integer> playerCoordinates = new HashMap<>();
	public static int enemyPlayer = 2;

	public static int currentTurn = 1;
	public static int currentAbility = 1;
	public static String currentAbilityName = "";
	public static boolean aPressed;
	public static boolean dPressed;
	public static boolean fire = false;
	
	public static boolean gameOver;
	
	// ! abilities  
	public static BufferedImage missile;
	public static int missileX;
	public static int missileY;
	public static boolean missileStarted;
	
    // For PVP End Screen GS
    public static String winnerPlayer;
    
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
        	if(mouseReset) {
        		mouseX = 0;
        		mouseY = 0;
        		mouseReset = false;
        	}
        	// Detecting click for the menu buttons
        	// Tank selection button
        	if((mouseX>=40 && mouseX<=210) && (mouseY>=330 && mouseY<=500)) {
        		gameState = 2;
        		mouseReset = true;
        	}
        	// Credits button
        	if((mouseX>=40 && mouseX<=210) && (mouseY>=600 && mouseY<=770)) {
        		gameState = 1;
        		mouseReset = true;
        	}
        	// Leaderboard button
        	if((mouseX>=2275 && mouseX<=2460) && (mouseY>=330 && mouseY<=515)) {
        		gameState = 5;
        		mouseReset = true;
        	}
        	// Video tutorial button
        	if((mouseX>=2275 && mouseX<=2460) && (mouseY>=600 && mouseY<=785)) {
        		// TODO
        		System.out.println("welcome to the video tutorial (coming soon)");
        		mouseReset = true;
        	}
        	// START button
        	if((mouseX>=710 && mouseX<=1715) && (mouseY>=1015 && mouseY<=1170)) {
        		gameState = 3;
        		mouseReset = true;
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
        	}
        	// Player 2 selected
        	if((mouseX>=183 && mouseX<=440) && (mouseY>=523 && mouseY<=618)) {
        		currentPlayer = 2;
        	}
        	
        	// Selecting tanks:
        	for(int i = 0; i < 4; i++) {
        		if((mouseX>=tankSelectLocations.get(tankArr[i]+"X") &&
    				mouseX<=tankSelectLocations.get(tankArr[i]+"X")+278) && 
    				(mouseY>=tankSelectLocations.get(tankArr[i]+"Y") && 
    				mouseY<=tankSelectLocations.get(tankArr[i]+"Y")+333)) 
        		{ // using tankArr, checks for if mouseX and mouseY is within each 
        		  // tank's X and Y rectangle areas drawn from their top left corner
        	      // (hence the +270 (width) and +285 (height)), with those values from
        		  // the tankSelectLocations hashmap. Saves the need to write 4 if statements
        			if(currentPlayer == 1)
            			P1Tank = tankArr[i];	
            		else
            			P2Tank = tankArr[i];
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
    					tankSelectLocations.get(P1Tank+"Y")-30, 338, 393);
        	// P2 tanks' markers
			g2.setColor(new Color(225, 74, 61)); // red color
    		g2.drawRect(tankSelectLocations.get(P2Tank+"X")-30, 
    					tankSelectLocations.get(P2Tank+"Y")-30, 338, 393);
    		// Checks if both players' tanks are the same, then show special indicator
    		if(P1Tank == P2Tank) {
    			g2.setColor(new Color(228, 119, 47)); // Orange for both players
    			g2.drawRect(tankSelectLocations.get(P1Tank+"X")-30,
        					tankSelectLocations.get(P1Tank+"Y")-30, 338, 393);
    		}

        	// allow player to choose player name (TODO textfile streaming)
			if(mouseX >= 600 && mouseX <= 1143 && mouseY >= 400 && mouseY <= 485) {
				isTypingP1 = true;
			} 
			if(mouseX >= 600 && mouseX <= 1143 && mouseY >= 665 && mouseY <= 750) {
				isTypingP2 = true;
			}
			// Exiting
			if(!(mouseX >= 600 && mouseX <= 1143 && mouseY >= 400 && mouseY <= 485)) {
				isTypingP1 = false;
			} 
			if(!(mouseX >= 600 && mouseX <= 1143 && mouseY >= 665 && mouseY <= 750)) {
				isTypingP2 = false;
			}
			// Draw names
			g.setColor(new Color(0, 0, 0));
			g.setFont(new Font("Arial", 1, 50));
			g.drawString(P1Name, 615, 460);
			g.drawString(P2Name, 615, 725);
        }
				// ! -------------------------------------------------------------------- PVP Mode GS   --------------------------------------------------------------------
        else if(gameState == 3){ // ! PVP Mode GS
        	g.drawImage(PVPMode, 0, 0, null);
			if(currentTurn == 1) {
				if(aPressed) {
					P1X -= 5;
					P1GoingRight = false;
				}
				if(dPressed) {
					P1X += 5;
					P1GoingRight = true;
				}
			} 
			else if (currentTurn == 2) {
				if(aPressed) {
					P2X -= 5;
					P2GoingRight = false;
				}
				if(dPressed) {
					P2X += 5;
					P2GoingRight = true;
				}
			}

			// P1 tank movement & deaths
			if(P1Dead && P1GoingRight) { // P1 death flip facing right
				g.drawImage(PVPMethods.flipImageVertical(
					PVPMethods.flipImageHorizontal(tankImages.get(P1Tank+"Tank"))), P1X, P1Y+25, null);
			}
			else if (P1Dead) { // death facing left
				g.drawImage(PVPMethods.flipImageVertical(tankImages.get(P1Tank+"Tank")), P1X, P1Y+25, null);
			}
			else if(P1GoingRight) {
				g.drawImage(PVPMethods.flipImageHorizontal(tankImages.get(P1Tank+"Tank")), P1X, P1Y, null);
			}
			else {
				g.drawImage(tankImages.get(P1Tank+"Tank"), P1X, P1Y, null);
			}
			// P2 tank movement & deaths
			if(P2Dead && P2GoingRight) { // P2 death flip facing right
				g.drawImage(PVPMethods.flipImageVertical(
					PVPMethods.flipImageHorizontal(tankImages.get(P2Tank+"Tank"))), P2X, P2Y+25, null);
			}
			else if (P2Dead) { // death facing left
				g.drawImage(PVPMethods.flipImageVertical(tankImages.get(P2Tank+"Tank")), P2X, P2Y+25, null);
			}
			else if(P2GoingRight) {
				g.drawImage(PVPMethods.flipImageHorizontal(tankImages.get(P2Tank+"Tank")), P2X, P2Y, null);
			}
			else {
				g.drawImage(tankImages.get(P2Tank+"Tank"), P2X, P2Y, null);
			}
			
			// Player deaths
			if(P1Dead || P2Dead) {
				gameOver = true;
			}
			g.setColor(new Color(0, 0, 0));
			g.setFont(new Font("Arial", 1, 50));
			g.drawString(currentAbilityName, 1795, 105);
			if(mouseX >= 2078 && mouseX <= 2269 && mouseY >= 933 && mouseY <= 1124) {
						g.drawString(currentAbility + "", 100, 100);
						fire = true;
						missileStarted = true;
						mouseX = 0;
						mouseY = 0;
				}
			// ! ======== Shooting / Abilities mechanics =======
			enemyPlayer = (currentTurn == 1)? 2 : 1;
			playerCoordinates.put("1X", P1X);
			playerCoordinates.put("1Y", P1Y);
			playerCoordinates.put("2X", P2X);
			playerCoordinates.put("2Y", P2Y);
			if(missileStarted) {
				missileX = playerCoordinates.get(currentTurn+"X");
				missileY = playerCoordinates.get(currentTurn+"Y")-256;
				missileStarted = false;
			}
			
			if(currentAbility == 1) {
				currentAbilityName = "Missile";
				if(fire) {
					g.drawImage(missile, missileX, missileY, null);
				}

				if(missileX < playerCoordinates.get(enemyPlayer+"X")) {
					missileX += 10;
				}
				if(missileX > playerCoordinates.get(enemyPlayer+"X")) {
					missileX -= 10;
				}
				// Impact on enemy tank
				if(missileX+128 >= playerCoordinates.get(enemyPlayer+"X") &&
						missileX+128 <= playerCoordinates.get(enemyPlayer+"X")+180) {
					fire = false;
					System.out.println("hit");
				}
			}
			
			//System.out.println(playerCoordinates.get(currentTurn));
			} else if(currentAbility == 2) {	
				if(P1Tank == "Titan" || P2Tank == "Titan") {
					currentAbilityName = "Lighting Strike";
				}
				else if(P1Tank == "Mouse" || P2Tank == "Mouse") {
					currentAbilityName = "Shrink";
				}
				else if(P1Tank == "Ironclad" || P2Tank == "Ironclad") {
					currentAbilityName = "Damage Reduction";
				}
				else if(P1Tank == "Sentinel" || P2Tank == "Sentinel") {
					currentAbilityName = "Electrogun";
				}
			
			}
			
			
			// PVP Pause screen
        	if(isPaused) { 
        		g.drawImage(PVPPause, 0, 0, null);
        		// TODO lock controls during pause
        	}
        
        else if(gameState == 4) { // PVP End Screen GS
        	g.drawImage(PVPEnd, 0, 0, null);
        	// Resetting tanks
        	P2X = 2320; 
            P2Y = 685;
            P1X = 0; 
            P1Y = 685;
            P1GoingRight = true;
            P2GoingRight = false;
        		
			currentTurn = 1;
			gameOver = false;
			fire = false;
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
    	
    	// Image Importation:
    	// GS
    	home 			= ImageIO.read(new File("GameStates/GS0 - Menu Screen.png"));
    	credits 		= ImageIO.read(new File("GameStates/GS1 - Credit.png"));
    	tankSelection 	= ImageIO.read(new File("GameStates/GS2 - Tank Selection.png"));
    	PVPMode 		= ImageIO.read(new File("map1.png"));
    	PVPPause 		= ImageIO.read(new File("PVP Pause Screen.png"));
    	PVPEnd 			= ImageIO.read(new File("GameStates/GS4 - PVP End Screen.png"));
    	PVPLeaderboard 	= ImageIO.read(new File("GameStates/GS5 - PVP Mode Leaderboard.png"));
    	// Tanks
    	titanTank 		= ImageIO.read(new File("Tanks/Titan Tank.png"));
		mouseTank		= ImageIO.read(new File("Tanks/Mouse Tank.png"));
		ironcladTank 	= ImageIO.read(new File("Tanks/Ironclad Tank.png"));
		sentinelTank 	= ImageIO.read(new File("Tanks/Sentinel Tank.png"));
		// Obstacles
		rock 			= ImageIO.read(new File("Obstacles/rock.png"));

		// Abilities
		missile 		= ImageIO.read(new File("Abilities/missile.png"));
		
		
    	
		// Maps:
		// For Tank Selection GS
    	tankSelectLocations.put("TitanX", 1535);
    	tankSelectLocations.put("TitanY", 323);
    	tankSelectLocations.put("MouseX", 1535);
    	tankSelectLocations.put("MouseY", 766);
    	tankSelectLocations.put("IroncladX", 2025);
    	tankSelectLocations.put("IroncladY", 323);
    	tankSelectLocations.put("SentinelX", 2025);
    	tankSelectLocations.put("SentinelY", 766);
    	
		// For PVP Mode GS
    	tankImages.put("TitanTank", titanTank);
		tankImages.put("MouseTank", mouseTank);
		tankImages.put("IroncladTank", ironcladTank);
		tankImages.put("SentinelTank", sentinelTank);

		
		
		
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
    
    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {
    	mouseX = e.getX();
    	mouseY = e.getY();
    	
    	if(gameState == 1)
    		gameState = 0;
    	else if(gameState == 4) 
    		gameState = 5;
    	else if(gameState == 5)
    		gameState = 0;
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
				if(P1Name.length() <= 10 && Character.isLetterOrDigit(e.getKeyChar()))
					// also checks so name only includes letters or digits. esc, shift etc 
					// won't be registered. Allows for 11 characters
					P1Name += e.getKeyChar();
				if(e.getKeyCode() == 8 && P1Name.length()>0) { // Deleting letters
					P1Name = P1Name.substring(0, P1Name.length()-1);
				}	
			}
			else if(isTypingP2){
				if(P2Name.length() <= 10 && Character.isLetterOrDigit(e.getKeyChar()))
					P2Name += e.getKeyChar();
				if(e.getKeyCode() == 8 && P2Name.length()>0) { 
					P2Name = P2Name.substring(0, P2Name.length()-1);
				}
			}

			// Exiting GS
			if(e.getKeyCode() == 27){
				gameState = 0;
			}
		}
    	else if(gameState == 3) {
			if((e.getKeyChar() == 'a' || e.getKeyCode() == KeyEvent.VK_LEFT) && !isPaused && !gameOver) {
				aPressed = true;
			}
			if((e.getKeyChar() == 'd' || e.getKeyCode() == KeyEvent.VK_RIGHT) && !isPaused && !gameOver) {
				dPressed = true;
			}
    		if(e.getKeyChar() == '=') { // dev key for finishing in PVP mode
    			gameState = 4;
    		}
    		if(e.getKeyChar() == '/') { // dev key for player turn switch
    			currentTurn = (currentTurn == 1)? 2 : 1;
    		}
    		if(e.getKeyChar() == '0') { // dev key for killing both tanks & reviving
    			P1Dead = !P1Dead;
    			P2Dead = !P2Dead;
    			gameOver = !gameOver;
    		}

			if(e.getKeyChar() == '1'){
				currentAbility = 1;
			}
			if(e.getKeyChar() == '2'){
				currentAbility = 2;
			}
    	}
    	else if(gameState == 4) 
    		gameState = 5;
    	else if(gameState == 5)
    		gameState = 0;
    }
    public void keyReleased(KeyEvent e) {
			if(gameState == 3) {
				if(e.getKeyChar() == 'a' || e.getKeyCode() == KeyEvent.VK_LEFT) {
					aPressed = false;
				}
				if(e.getKeyChar() == 'd' || e.getKeyCode() == KeyEvent.VK_RIGHT) {
					dPressed = false;
				}
			}
		}
}