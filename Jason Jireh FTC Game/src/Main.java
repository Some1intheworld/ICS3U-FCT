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
    public static BufferedImage mouseTankMini;

	// Objects Images
	public static BufferedImage crate;
	public static BufferedImage rock;
	public static BufferedImage wall;
	public static BufferedImage shield;
    
    // ! Mouse/Keyboard Events
    public static int mouseX;
    public static int mouseY;
    
    public static boolean mouseReset = true;
    // ! Game Stats
    public static Map<String, Object> playerStats = new HashMap<>();
    // Player 1 (P1)
    // Tank Selection GS & Leaderboard GS
    public static String P1Name = "";
    public static double P1Wins;
    public static double P1Battles;
    public static double P1Winrate;
    public static String P1Size= "normal";
    // PVP Mode GS 
    public static int baseDamage = 10;
	/*
	 * public static int P1Health = 100; public static int P2Health = 100;
	 */

    
    
    // Player 2 (P2)
    // Tank Selection GS & Leaderboard GS
    public static String P2Name = "";
    public static double P2Wins;
    public static double P2Battles;
    public static double P2Winrate;
    public static String P2Size;
    // PVP Mode GS  
    
    // ! General stats
    // For Tank Selection GS
    public static int currentPlayer = 1; // Default is P1
	public static Map<String, Integer> tankSelectLocations = new HashMap<>();
    public static String[] tankArr = {"Titan", "Mouse", "Ironclad", "Sentinel"};
	
	public static boolean isTypingP1;
	public static boolean isTypingP2;

	// For PVP Mode GS
	public static Map<String, BufferedImage> tankImages = new HashMap<>();
	public static int enemyPlayer = 2;
	
	public static int currentTurn = 1;
	public static int currentAbility = 1;
	public static String currentAbilityName = "";
	public static boolean aPressed;
	public static boolean dPressed;
	public static boolean fire;
	public static boolean lighting;
	public static int affected;
	public static int speed = 6;
	
	public static boolean activated;
	
	public static boolean gameOver;
	
	public static int tankFlippedLow;
	public static int timer = 0;
	public static int frameCounter = 0;
	public static int deathFrameCounter;
	
	// ! abilities  
	public static Map<String, Object> weapon1Properties = new HashMap<>();
	public static BufferedImage bomb;
	public static int bombX;
	public static int bombY=10000;
	public static int bombXStart;
	public static int bombYStart;
	public static boolean bombDirectionRight = true;
	public static int powerBarHeight = 0;
	
	public static int electrogunLength = 0;
	public static int electrogunStartX;
	public static int electrogunStartY;
	public static boolean electrogunInitiated;
	
	public static boolean damageReductionDone;
	
	public static boolean bombStarted;
	public static int power = 0;
	public static int increment = 10;
	public static int fireIncrement = 10;
	public static int fireYIncrement = -4;
	public static int velocity = -50;
	public static int gravity = 10;

	
    // For PVP End Screen GS
    public static int winnerPlayer;
    public static int loserPlayer;
    public static int PVPEndScreenFrameCounter = 0;
    
    // For PVP Leaderboard Screen GS
    public static int previousGS;
    public static int leaderboardScreenFrameCounter = 0;
    
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
    		previousGS = 0;
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
        	Graphics2D g2 = (Graphics2D) g.create();
        	// Player Selection:
        	g2.setStroke(new BasicStroke(3));
        	// Player 1 selected
        	if((mouseX>=183 && mouseX<=440) && (mouseY>=260 && mouseY<=355)) {
        		currentPlayer = 1;
        	}
        	if(currentPlayer == 1) {
        		g2.setColor(new Color(194, 164, 0)); // gold color
        		g2.drawRect(178, 255, 268, 105);
        	}
        	// Player 2 selected
        	if((mouseX>=183 && mouseX<=440) && (mouseY>=523 && mouseY<=618)) {
        		currentPlayer = 2;
        	}
        	if(currentPlayer == 2) {
        		g2.setColor(new Color(225, 74, 61)); // red color
        		g2.drawRect(178, 518, 272, 105);
        	}
        	
        	// Selecting tanks:
        	for(int i = 0; i < 4; i++) 
        		if((mouseX>=tankSelectLocations.get(tankArr[i]+"X") && 
    				mouseX<=tankSelectLocations.get(tankArr[i]+"X")+278) && 
    				(mouseY>=tankSelectLocations.get(tankArr[i]+"Y") && 
    				mouseY<=tankSelectLocations.get(tankArr[i]+"Y")+333)) 
        		playerStats.put(currentPlayer+"Tank", tankArr[i]);	
        		
        	
        	// Tank Name next to Player #:
        	// P1
        	g.setColor(new Color(255, 255, 255)); // white
        	g.setFont(new Font("Arial", 1, 60));
        	g.drawString(playerStats.get("1Tank")+"".toUpperCase(), 525, 335);
        	// P2
        	g.drawString(playerStats.get("2Tank")+"".toUpperCase(), 525, 595);
        	
        	// Indicators/border for tank chosen:
        	g2.setStroke(new BasicStroke(15)); // Setting thickness of border
        	
        	// P1 tanks' markers
    		g2.setColor(new Color(194, 164, 0)); // gold color
    		g2.drawRect(tankSelectLocations.get(playerStats.get("1Tank")+"X")-30, // -30 for a bigger border
    					tankSelectLocations.get(playerStats.get("1Tank")+"Y")-30, 338, 393);
        	// P2 tanks' markers
			g2.setColor(new Color(225, 74, 61)); // red color
    		g2.drawRect(tankSelectLocations.get(playerStats.get("2Tank")+"X")-30, 
    					tankSelectLocations.get(playerStats.get("2Tank")+"Y")-30, 338, 393);
    		// Checks if both players' tanks are the same, then show special indicator
    		if(playerStats.get("1Tank") == playerStats.get("2Tank")) {
    			g2.setColor(new Color(228, 119, 47)); // Orange for both players
    			g2.drawRect(tankSelectLocations.get(playerStats.get("1Tank")+"X")-30,
        					tankSelectLocations.get(playerStats.get("1Tank")+"Y")-30, 338, 393);
    		}

        	// allow player to choose player name (TODO textfile streaming)
    		g2.setStroke(new BasicStroke(3));
    		if(mouseX >= 600 && mouseX <= 1143 && mouseY >= 400 && mouseY <= 485) {
				isTypingP1 = true;
				g2.setColor(new Color(194, 164, 0)); 
        		g2.drawRect(596, 395, 552, 93);
			} 
			if(mouseX >= 600 && mouseX <= 1143 && mouseY >= 665 && mouseY <= 750) {
				isTypingP2 = true;
				g2.setColor(new Color(225, 74, 61));
        		g2.drawRect(596, 661, 552, 93);
        		
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
        	
        	g.setColor(new Color(250, 250, 250));
        	g.fillRect(325, 160, 100, 340);    
        	g.setColor(new Color(255, 214, 0));
        	g.fillRect(325, 500, 100, powerBarHeight);     
        	g.setColor(new Color(0, 0, 0));
        	g.setFont(new Font("Arial", 1, 40));
        	g.drawString(power+"", 330, 350);
        	g.setColor(new Color(0, 0, 0));
        	g.setFont(new Font("Arial", 1, 50));
        	g.drawString("Time left: " + (15 - (timer /50))+"", 1130, 110);
        	
        	
        	// Timer
        	if(timer >= 750) {
        		PVPMethods.changeTurns();
        		timer = 0;
        	}
        	timer ++;
        	
        	// Check if dead
        	PVPMethods.deathCheck();
        	
        	// Player movement	
        	PVPMethods.playerMovement();
        	
			// Drawing P1 tank movement, deaths, & name
			g.drawImage(PVPMethods.drawPlayerMouse(1), (int)playerStats.get("1X"), (int)playerStats.get("1Y")+tankFlippedLow, null);
			// P1 Name
			g.setColor(PVPMethods.nameColor((Boolean)playerStats.get("1Dead"))); // Sets color
			if(!gameOver && currentTurn == 1) g.setColor(new Color(159, 27, 255));
			g.setFont(new Font("Arial", 1, 50));
			g.drawString(PVPMethods.displayName(P1Name, 1), (int)playerStats.get("1X"), (int)playerStats.get("1Y")-120);
			
			// Drawing P2 tank movement, deaths, & name
			g.drawImage(PVPMethods.drawPlayer(2), (int)playerStats.get("2X"), (int)playerStats.get("2Y")+tankFlippedLow, null);
			// P2 Name
			g.setColor(PVPMethods.nameColor((Boolean)playerStats.get("2Dead")));
			if(!gameOver && currentTurn == 2) g.setColor(new Color(159, 27, 255));
			g.drawString(PVPMethods.displayName(P2Name, 2), (int)playerStats.get("2X"), (int)playerStats.get("2Y")-120);
			
			
			// HP:
        	g.setColor(new Color(150, 0, 0));
			g.fillRect(500, 21, (int)playerStats.get("1HP")*6-51, 104);
        	g.fillRect(2060, 21, -(int)playerStats.get("2HP") *6+51, 104);
        	g.setColor(new Color(0, 0, 0));
			g.drawString((int)playerStats.get("1HP") +"", 683, 90);
			g.drawString((int)playerStats.get("2HP") + "", 1792, 90);
					
			// Fire button
			g.drawString(currentAbilityName, 1720, 1080);
			g.setColor(new Color(0, 0, 0));
			g.setFont(new Font("Arial", 1, 30));
			g.setFont(new Font("Courier", 1, 30));
			g.setColor(new Color(100, 242, 0));
			g.drawString(baseDamage +"", 500, 500);
			
			
			g.drawString(Main.bombYStart - (Main.power / 2) +"", 600, 500);
			/*if(!isPaused && mouseX >= 2078 && mouseX <= 2269 && mouseY >= 933 && mouseY <= 1124) {
				fire = true;
				bombStarted = true;
				mouseX = 0;
				mouseY = 0;
			}*/
			if(fire && (bombY < 50)) {
				fireYIncrement = 4;
			}
			if(Main.fire && (Main.bombY < Main.bombYStart - (Main.power / 2))) {
				Main.fireYIncrement = 4;
//				YIncrementMultiplier *= YIncrementMultiplier;
			}
			// ! ======== Bomb Ability =======
			enemyPlayer = (currentTurn == 1)? 2 : 1;
			if(!fire) {
				PVPMethods.powerRangeDeterminer();
				PVPMethods.velocityDeterminer();
			}
			if(bombStarted) { 
				PVPMethods.bombStartAndDirectionLocate();
				bombStarted = false;
			}


			
			if(currentAbility == 1) {
				currentAbilityName = "bomb";
				PVPMethods.bombDirection();
				if(fire) { // Opened fire
					g.drawImage(bomb, bombX, bombY, null);
					baseDamage = 10;
					bombX += fireIncrement;
					bombY += velocity;
					velocity += gravity;
					PVPMethods.bombPowerCheck();
					PVPMethods.enemyHitCheck();
				} 
				
			} else if(currentAbility == 2) {	
				// ------ Titan Lighting Strike -------JJ
				if(playerStats.get(currentTurn+"Tank") == "Titan") {
					currentAbilityName = "Lighting Strike";
					PointerInfo a = MouseInfo.getPointerInfo();
					Point b = a.getLocation();
					g.drawOval((int) b.getX()-100, 850, 200, 100);
					if(activated) { // lighting strike
						if(!((int) playerStats.get(enemyPlayer+"X") >=(int) b.getX()+100 ||
						   (int) playerStats.get(enemyPlayer+"X")+180 <=(int) b.getX()-100)){		
							PVPMethods.dealDamage(baseDamage);
							speed = 2;
							baseDamage = 6;
							affected = enemyPlayer;
						}
						g.fillRect((int) b.getX()-100, 0, 200, 1000);
						activated = false;
						PVPMethods.changeTurns();
						
					}
				}
				
				// ------ Ironclad Damage Reduction -------
				else if(playerStats.get(currentTurn+"Tank") == "Ironclad") {
					currentAbilityName = "Damage Reduction";
					if(activated) {
						frameCounter += 1;
						if(!damageReductionDone) {
							baseDamage /= 2;
							damageReductionDone = true;
						}
						affected = enemyPlayer;
						g.drawImage(shield, 700, 550, null);
						if(frameCounter == 30) {
							activated = false;
							frameCounter = 0;
							PVPMethods.changeTurns();
							damageReductionDone = false;
						}
						
					}
				}
				
				// ------- Mouse Shrink -------
				else if(playerStats.get(currentTurn+"Tank") == "Mouse") {
					currentAbilityName = "Shrink";
					if(activated) {
						affected = currentTurn;
						if(currentTurn == 1) {
							P1Size = "small";
							affected = 1;
						}
						else if(currentTurn == 2) {
							P2Size = "small";
							affected = 2;
						}
					}
				}
				
				// ------- Sentinel electrogun ----------JJ
				else if(playerStats.get(currentTurn+"Tank") == "Sentinel") {
					currentAbilityName = "Electrogun";
					PointerInfo a = MouseInfo.getPointerInfo();
					Point b = a.getLocation();
					g.setColor(new Color(255, 255, 0, 50));
					if(!activated && (Boolean)playerStats.get(currentTurn+"GoingRight")) 
						g.fillRect((int) playerStats.get(currentTurn+"X") + 180, (int) b.getY(), 3000, 20);
					else if (!activated) 
						g.fillRect((int) playerStats.get(currentTurn+"X")-3000, (int) b.getY(), 3000, 20);
					
					if(activated) { 
						if(!electrogunInitiated) {
						electrogunStartX = (int)playerStats.get(currentTurn+"X");
						electrogunStartY = (int)b.getY();
						electrogunInitiated = true;
						}
						g.setColor(new Color(255, 255, 255));
						// TODO add other direction
						g.fillRect(electrogunStartX + 180, electrogunStartY, electrogunLength, 20);
						if(!((int)b.getY()+20 <= (int) playerStats.get(enemyPlayer+"Y")||
							(int) b.getY() >= (int) playerStats.get(enemyPlayer+"Y")+134)){		
							PVPMethods.dealDamage(baseDamage/5);
						}
						if((Boolean)playerStats.get(currentTurn+"GoingRight")) electrogunLength += 800;
						else electrogunLength -= 800;
						if(electrogunLength >= 2500 || electrogunLength <= -2500) {
							activated = false;
							electrogunLength = 0;
							electrogunInitiated = false;
							PVPMethods.changeTurns();

						}
					}
				}
			} 
			
			}
		
			
			// PVP Pause screen
        	if(isPaused) { 
        		g.drawImage(PVPPause, 0, 0, null);
        	}
        
        else if(gameState == 4) { // PVP End Screen GS
        	g.drawImage(PVPEnd, 0, 0, null);
        	previousGS = 4;
        	// Tank resets
        	playerStats.put("1X", 10);
    		playerStats.put("1Y", 800);
    		playerStats.put("2X", 2310);
    		playerStats.put("2Y", 800);
    		playerStats.put("1GoingRight", true);
    		playerStats.put("2GoingRight", false);
    		playerStats.put("1Dead", false);
    		playerStats.put("2Dead", false);
    		playerStats.put("1HP", 100);
    		playerStats.put("2HP", 100);
        	// General game resets
    		speed = 6;
    		affected = 0; // no one is affected
    		baseDamage = 10;
			currentTurn = 1;
			currentAbility = 1;
			gameOver = false;
			fire = false;
			power = 0;
			powerBarHeight = 0;
			PVPEndScreenFrameCounter++;
        	//TODO display winner player name, tank visual & tank name
			
        }
        else if(gameState == 5){ // PVP Leaderboard GS
        	g.drawImage(PVPLeaderboard, 0, 0, null);
        	leaderboardScreenFrameCounter++;
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
    	PVPMode 		= ImageIO.read(new File("GameStates/GS3 - PVP V2.png"));
    	PVPPause 		= ImageIO.read(new File("PVP Pause Screen.png"));
    	PVPEnd 			= ImageIO.read(new File("GameStates/GS4 - PVP End Screen.png"));
    	PVPLeaderboard 	= ImageIO.read(new File("GameStates/GS5 - PVP Mode Leaderboard.png"));
    	// Tanks
    	titanTank 		= ImageIO.read(new File("Tanks/Titan Tank.png"));
		mouseTank		= ImageIO.read(new File("Tanks/Mouse Tank.png"));
		ironcladTank 	= ImageIO.read(new File("Tanks/Ironclad Tank.png"));
		sentinelTank 	= ImageIO.read(new File("Tanks/Sentinel Tank.png"));
		mouseTankMini	= ImageIO.read(new File("Tanks/Mouse Tank mini.png"));
		// Obstacles
		rock 			= ImageIO.read(new File("Obstacles/rock.png"));

		// Abilities
    	bomb 			= ImageIO.read(new File("Abilities/bomb.png"));
    	shield 			= ImageIO.read(new File("Abilities/shield.png"));
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
    	tankImages.put("Titan", titanTank);
		tankImages.put("Mouse", mouseTank);
		tankImages.put("Ironclad", ironcladTank);
		tankImages.put("Sentinel", sentinelTank);
		tankImages.put("Mouse Mini", mouseTankMini);
		

		playerStats.put("1X", 10);
		playerStats.put("1Y", 800);
		playerStats.put("2X", 2310);
		playerStats.put("2Y", 800);
		
		playerStats.put("1GoingRight", true);
		playerStats.put("2GoingRight", false);
		playerStats.put("1Dead", false);
		playerStats.put("2Dead", false);
		playerStats.put("1Tank", "Titan");
		playerStats.put("2Tank", "Mouse");
		playerStats.put("1HP", 100);
		playerStats.put("2HP", 100);
		
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
    	else if(gameState == 4 && PVPEndScreenFrameCounter >= 30) {
    		gameState = 5;
    		PVPEndScreenFrameCounter = 0;
    	}
    	else if(gameState == 5) {
    		if(previousGS == 4 && leaderboardScreenFrameCounter >= 30) {
    			gameState = 0;
    			leaderboardScreenFrameCounter = 0;
    		}
    		else if(previousGS != 4) gameState = 0;
    	}
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
			if(e.getKeyChar() == 32 && !isPaused) {
				if(currentAbility == 1) {
					fire = true;
					bombStarted = true;
					mouseX = 0;
					mouseY = 0;
				} else if(currentAbility == 2) {
					activated = true;
					mouseX = 0;
					mouseY = 0;
				}

			}
			// Dev keys
    		if(e.getKeyChar() == '=' && !isPaused) { // dev key for finishing in PVP mode
    			gameState = 4;
    		}
    		if(e.getKeyChar() == '/' && !isPaused) { // dev key for player turn switch
    			PVPMethods.changeTurns();
    		}
    		if(e.getKeyChar() == '0' && !isPaused) { // dev key for killing both tanks & reviving
    			playerStats.put("1Dead", !(Boolean)playerStats.get("1Dead"));
    			playerStats.put("2Dead", !(Boolean)playerStats.get("2Dead"));
    			gameOver = !gameOver;
    		}

			if(e.getKeyChar() == '1' && !isPaused){
				currentAbility = 1;
			}
			if(e.getKeyChar() == '2' && !isPaused){
				currentAbility = 2;
			}
			

    	}
    	else if(gameState == 4 && PVPEndScreenFrameCounter >= 30) {
    		gameState = 5;
    		PVPEndScreenFrameCounter = 0;
    	}
    	else if(gameState == 5) {
    		if(previousGS == 4 && leaderboardScreenFrameCounter >= 30) { 
    			gameState = 0;
    			leaderboardScreenFrameCounter = 0;
    		}
    		else if(previousGS != 4) gameState = 0;
    	}
    		
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