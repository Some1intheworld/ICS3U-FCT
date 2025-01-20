import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.net.URI;
import java.net.URISyntaxException;
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

	// Objects Images
	public static BufferedImage crate;
	public static BufferedImage rock;
	public static BufferedImage wall;
	public static BufferedImage shield;
	public static BufferedImage asteroid;
	
    // ! Mouse/Keyboard Events
    public static int mouseX;
    public static int mouseY;
    
    public static boolean mouseReset = true;
    // ! Game Stats
    public static Map<String, Object> playerStats = new HashMap<>();
    // Player 1 (P1)
    // Tank Selection GS & Leaderboard GS
    public static double P1Wins;
    public static double P1Battles;
    public static double P1Winrate;
    public static String P1Size= "normal";
    // PVP Mode GS 
    public static int baseDamage = 10;

    
    
    // Player 2 (P2)
    // Tank Selection GS & Leaderboard GS
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
	public static int affected;
	public static int speed = 6;
	
	public static boolean activated;
	
	public static boolean gameOver;
	
	public static int tankFlippedLow;
	public static int timer;
	public static int frameCounter;
	public static int deathFrameCounter;
	
	// ! abilities  
	public static Map<String, Object> weapon1Properties = new HashMap<>();
	public static BufferedImage bomb;
	public static int bombX;
	public static int bombY=10000;
	public static int bombXStart;
	public static int bombYStart;
	public static boolean bombDirectionRight = true;
	public static boolean bombIsInAir;
	public static boolean enemyHit;
	public static BufferedImage scorchMark;
	public static int crateX = 1000;
	public static int crateY = 560;

	public static int asteroidX = 400;
	public static int asteroidY = 600;
	
	public static boolean asteroid1 = true;
	public static boolean asteroid2 = true;
	public static boolean asteroid3 = false;
	
	public static int fireFrameCounter;
	public static BufferedImage[] tankFireImageArr = new BufferedImage[5];
	public static boolean fireDrawn;
	public static int tankFireIndex;
	
	public static int powerBarHeight;
	
	public static int electrogunLength;
	public static int electrogunStartX;
	public static int electrogunStartY;
	public static boolean electrogunInitiated;
	
    public static BufferedImage explosion;
    public static boolean explode;
    public static BufferedImage aiming;
    public static boolean scorched;
    
    public static ArrayList<int[]> scorchLocations = new ArrayList<>();
	
	public static boolean damageReductionDone;
	
	public static boolean bombStarted;
	public static int power;
	public static int increment = 40;
	public static int fireIncrement = 10;
	public static int velocity;
	public static int gravity = 2;
	public static double reachTime; 
	
	public static boolean inCrate;
	public static boolean illegalUse;
	
    // For PVP End Screen GS
    public static int winnerPlayer;
    public static int loserPlayer;
    public static int PVPEndScreenFrameCounter = 0;
    public static Map<String, BufferedImage> tankImagesEndScreen = new HashMap<>();
    
    // For PVP Leaderboard Screen GS
    public static int previousGS;
    public static int leaderboardScreenFrameCounter = 0;
    public static String[] leaderboardPlayers = new String[4];
    
    public static boolean deathDevKeyUsed;
    public static boolean gameDevKeyUsed;
    public static boolean leaderboardStreamed;
    public static int playersShown;
	public static int displaySpacing;
    
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
    		leaderboardStreamed = false;
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
        		 try {
                     // Create a URI object from the URL
                     URI uri = new URI("https://www.youtube.com/watch?v=xvFZjo5PgG0");
                     // Use Desktop to open the link in the default browser
                     if (Desktop.isDesktopSupported()) {
                         Desktop desktop = Desktop.getDesktop();
                         desktop.browse(uri);  // Open the URL in the default browser
                     } else {
                         System.out.println("Desktop not supported");
                     }
                 } catch (URISyntaxException | java.io.IOException ex) {
                     ex.printStackTrace();
                 }
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
			g.drawString((playerStats.get("1Name")+""), 615, 460);
			g.drawString((playerStats.get("2Name")+""), 615, 725);
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

			g.setColor(new Color(255, 255, 255));
			g.setFont(new Font("Arial", 0, 30));
			g.drawString("Bomb Power", 290, 530);

        	g.setColor(new Color(0, 0, 0));
        	g.setFont(new Font("Arial", 1, 50));
        	g.drawString("Time left: " + (10 - (timer /50))+"", 1130, 110);
        	
        	// Showing scorch marks
			if(scorched) {
				scorchLocations.add(new int[]{bombX, bombY+70});
				scorched = false;
			}
			for (int[] array : scorchLocations) {
				g.drawImage(scorchMark, array[0], array[1], null);
	        }

        	// Timer
        	if(timer >= 500) {
        		PVPMethods.changeTurns();
        		timer = 0;
        	}
        	if(!isPaused && !gameOver) timer++;
        		
        	// Player movement	
        	PVPMethods.playerMovement();

        	// Obstacles
        	g.drawImage(crate, crateX, crateY, null);
        	if(asteroid1) g.drawImage(asteroid, 400, asteroidY, null);
        	if(asteroid2) g.drawImage(asteroid, 1650, asteroidY, null);
        	if(asteroid3) g.drawImage(asteroid, asteroidX, asteroidY, null);
        	

        	
			// Drawing P1 tank movement, deaths, & name
			g.drawImage(PVPMethods.drawPlayerMouse(1), (int)playerStats.get("1X"), (int)playerStats.get("1Y")+tankFlippedLow, null);
			// P1 Name
			g.setColor(PVPMethods.nameColor((Boolean)playerStats.get("1Dead"))); // Sets color
			if(!gameOver && currentTurn == 1) {
				g.setColor(new Color(255, 214, 0));
				g.setFont(new Font("Arial", 0, 30));
				g.drawString("Your Turn!", (int)playerStats.get("1X")+16, (int)playerStats.get("1Y")-190);
				g.setColor(new Color(159, 27, 255));
			}
			g.setFont(new Font("Arial", 1, 50));
			g.drawString(PVPMethods.displayName(playerStats.get("1Name")+"", 1), (int)playerStats.get("1X"), (int)playerStats.get("1Y")-120);
			
			// Drawing P2 tank movement, deaths, & name
			g.drawImage(PVPMethods.drawPlayer(2), (int)playerStats.get("2X"), (int)playerStats.get("2Y")+tankFlippedLow, null);
			// P2 Name
			g.setColor(PVPMethods.nameColor((Boolean)playerStats.get("2Dead")));
			if(!gameOver && currentTurn == 2) {
				g.setColor(new Color(255, 214, 0));
				g.setFont(new Font("Arial", 0, 30));
				g.drawString("Your Turn!", (int)playerStats.get("2X")+16, (int)playerStats.get("2Y")-190);
				g.setColor(new Color(159, 27, 255));
			}
			g.setFont(new Font("Arial", 1, 50));
			g.drawString(PVPMethods.displayName(playerStats.get("2Name")+"", 2), (int)playerStats.get("2X"), (int)playerStats.get("2Y")-120);
			
			
			// HP:
        	g.setColor(new Color(255, 255, 255));
        	g.fillRect(500, 21, 600, 104);
        	g.fillRect(2060, 21, -600, 104);
        	
			g.setColor(new Color(150, 0, 0));
			if(!(Boolean)playerStats.get("1Dead")) g.fillRect(500, 21, (int)playerStats.get("1HP")*6, 104);
			if(!(Boolean)playerStats.get("2Dead")) g.fillRect(2060, 21, -(int)playerStats.get("2HP")*6, 104);
			
        	g.setColor(new Color(0, 0, 0));
        	if(!(Boolean)playerStats.get("1Dead")) g.drawString((int)playerStats.get("1HP") +"", 683, 90);
        	else g.drawString("0", 683, 90);
			if(!(Boolean)playerStats.get("2Dead")) g.drawString((int)playerStats.get("2HP") + "", 1792, 90);
			else g.drawString("0", 1792, 90);
					
			// Fire button
			g.setColor(new Color(0, 0, 0));
			g.setFont(new Font("Arial", 1, 40));
			g.drawString(currentAbility+": "+currentAbilityName, 1700, 1090);
			g.setColor(new Color(0, 0, 0));
			g.setFont(new Font("Courier", 1, 30));
			g.setColor(new Color(100, 242, 0));
			g.drawString(baseDamage +"", 500, 500);
						
			g.drawString(Main.bombYStart - (Main.power / 2) +"", 600, 500);
			
			enemyPlayer = (currentTurn == 1)? 2 : 1;
			// ! ======== Bomb Ability =======
			if(currentAbility == 1 && !isPaused && !gameOver) {
				if(!fire && !isPaused && !gameOver) {
					PVPMethods.powerRangeDeterminer();
					PVPMethods.velocityDeterminer();
				}
				if(bombStarted) { 
					PVPMethods.bombStartAndDirectionLocate();
					bombStarted = false;
				}
				currentAbilityName = "Bomb";
				PVPMethods.bombDirection();
				if(!fire && (Boolean)playerStats.get(currentTurn+"GoingRight")){
					g.drawImage(aiming, (int)playerStats.get(currentTurn+"X")+power, 845, null);
				}
				else if(!fire && !(Boolean)playerStats.get(currentTurn+"GoingRight")){
					g.drawImage(aiming, (int)playerStats.get(currentTurn+"X")-power, 845, null);
				}
				if(fire) { // Opened fire
					bombIsInAir = true;
					g.drawImage(bomb, bombX, bombY, null);
					bombX += fireIncrement;
				    bombY += velocity; // Update the Y position based on velocity
				    velocity += gravity;
					PVPMethods.enemyHitCheck();
					
					if(bombY+115 >= 950 && !enemyHit) {
						explode = true;
						scorched = true;
						fire = false;
						bombIsInAir = false;
						PVPMethods.changeTurns();	
					}
					if(explode) { 
						g.drawImage(explosion, bombX, bombY-30, null);
						enemyHit = false;
						explode = false;
					}	
				}
			} 
			else if(currentAbility == 2  && !isPaused && !gameOver)
			{	
				// ------ Titan Lightning Strike -------
				if("Titan".equals(playerStats.get(currentTurn+"Tank")) &&  !inCrate) {
					currentAbilityName = "Lightning Strike";
					Graphics2D g2 = (Graphics2D) g.create();
		        	g2.setStroke(new BasicStroke(3));
					PointerInfo a = MouseInfo.getPointerInfo();
					Point b = a.getLocation();
					g2.drawOval((int) b.getX()-100, 860, 200, 100);
					if(activated) { // lightning strike
						if(!((int) playerStats.get(enemyPlayer+"X") >=(int) b.getX()+100 ||
						   (int) playerStats.get(enemyPlayer+"X")+180 <=(int) b.getX()-100)){		
							PVPMethods.dealDamage(100);
							//PVPMethods.dealDamage(baseDamage / 2);
							speed = 1;
							baseDamage = 6;
							affected = enemyPlayer;
						}
						g.fillRect((int) b.getX()-100, 0, 200, 1000);
						activated = false;
						PVPMethods.changeTurns();
						
					}
				}
				
				// ------ Ironclad Damage Reduce ------- 
				else if("Ironclad".equals(playerStats.get(currentTurn+"Tank"))) {
					currentAbilityName = "Damage Reduce";
					if(activated) {
						frameCounter++;
						if(!damageReductionDone) {
							baseDamage /= 2;
							damageReductionDone = true;
							affected = enemyPlayer;
						}
						g.drawImage(shield, (int)playerStats.get(currentTurn+"X")+180, 550, null);
						g.drawImage(shield, (int)playerStats.get(currentTurn+"X")-523, 550, null);
						if(frameCounter >= 30) {
							activated = false;
							frameCounter = 0;
							PVPMethods.changeTurns();
							damageReductionDone = false;
						}
						
					}
				}
				
				// ------- Mouse Obstacle Placer -------
				else if("Mouse".equals(playerStats.get(currentTurn+"Tank"))) {
					currentAbilityName = "Obstacle";
					Graphics2D g2 = (Graphics2D) g.create();
		        	g2.setStroke(new BasicStroke(3));
					PointerInfo a = MouseInfo.getPointerInfo();
					Point b = a.getLocation();
					g2.setColor(new Color(110, 110, 110));
					g2.fillOval((int) b.getX()-100, 860, 488, 100);

					if(activated) { 
						if(PVPMethods.checkObstaclePlacing()) {
							asteroidX = (int) b.getX()-100;
							asteroid3 = true;
							activated = false;
							PVPMethods.changeTurns();
						} else {
							activated = false;
						}	
					} 
				}
				
				// ------- Sentinel electrogun ----------
				else if("Sentinel".equals(playerStats.get(currentTurn+"Tank"))  && !inCrate) {
					currentAbilityName = "Electrogun";
					PointerInfo a = MouseInfo.getPointerInfo();
					Point b = a.getLocation();
					g.setColor(new Color(255, 255, 0, 50));
					if(!activated && (Boolean)playerStats.get(currentTurn+"GoingRight")) 
						g.fillRect((int) playerStats.get(currentTurn+"X") + 180, (int) b.getY(), 3000, 20);
					else if (!activated) 
						g.fillRect((int) playerStats.get(currentTurn+"X"), (int) b.getY(), -3000, 20);
					
					if(activated) { 
						if(!electrogunInitiated) {
						electrogunStartX = (int)playerStats.get(currentTurn+"X");
						electrogunStartY = (int)b.getY();
						electrogunInitiated = true;
						}
						g.setColor(new Color(255, 255, 255));
						if((Boolean)playerStats.get(currentTurn+"GoingRight")) g.fillRect(electrogunStartX + 180, electrogunStartY, electrogunLength, 20);
						else g.fillRect(electrogunStartX, electrogunStartY, electrogunLength, 20);
							
						if((Boolean)playerStats.get(currentTurn+"GoingRight")) electrogunLength += 800;
						else electrogunLength -= 800;
						
						if(!((int)b.getY()+20 <= (int) playerStats.get(enemyPlayer+"Y")||
								(int) b.getY() >= (int) playerStats.get(enemyPlayer+"Y")+134)
								&& (Boolean)playerStats.get(currentTurn+"GoingRight") &&
								(int)playerStats.get(enemyPlayer+"X")>=(int)playerStats.get(currentTurn+"X")){		
								PVPMethods.dealDamage(baseDamage/5);
						}
						else if(!((int)b.getY()+20 <= (int) playerStats.get(enemyPlayer+"Y")||
								(int) b.getY() >= (int) playerStats.get(enemyPlayer+"Y")+134) && !(Boolean)playerStats.get(currentTurn+"GoingRight")
								&& (int)playerStats.get(enemyPlayer+"X")<=(int)playerStats.get(currentTurn+"X")){		
								PVPMethods.dealDamage(baseDamage/5);
						}
						if(electrogunLength >= 2500 || electrogunLength <= -2500) {
							activated = false;
							electrogunLength = 0;
							electrogunInitiated = false;
							PVPMethods.changeTurns();

						}
					}
				}
			}
			// Check if dead
	    	PVPMethods.deathCheck();
	    	
	    	PVPMethods.inCrateCheck();
	    	if(illegalUse) {
	    		g.setColor(new Color(255, 49, 49));
				g.setFont(new Font("Arial", 0, 30));
				g.drawString("Can't use when in Crate!", (int)playerStats.get(currentTurn+"X")-70, (int)playerStats.get(currentTurn+"Y")-30);
	    	}
	    	if(gameOver && !fireDrawn) {
	    		fireFrameCounter++;
	    		if(!deathDevKeyUsed) {
	    			if(fireFrameCounter%4==0) {
		    			tankFireIndex = fireFrameCounter/4;
		    		}
	    			g.drawImage(tankFireImageArr[tankFireIndex], (int)playerStats.get(loserPlayer+"X"), (int)playerStats.get(loserPlayer+"Y")-32, null);
		    		if(fireFrameCounter >= 16) {
		    			fireDrawn = true;
		    			tankFireIndex = 0;
		    			fireFrameCounter = 0;
		    		}
	    		}
	    	}
			
			// PVP Pause screen
        	if(isPaused) { 
        		g.drawImage(PVPPause, 0, 0, null);
        	}
		}
	     
        
        else if(gameState == 4) { // PVP End Screen GS
        	previousGS = 4;
        	g.drawImage(PVPEnd, 0, 0, null);
        	// Displaying winner	
			g.setColor(new Color(255, 255, 255)); 
			g.setFont(new Font("Arial", 1, 70));
			g.drawString(PVPMethods.displayName((playerStats.get(winnerPlayer+"Name")+""), winnerPlayer), 1120, 310);
			g.setFont(new Font("Arial", 1, 60));
			g.drawString((playerStats.get(winnerPlayer+"Tank")+"").toUpperCase(), 1130, 1025);
			g.drawImage(tankImagesEndScreen.get(playerStats.get(winnerPlayer+"Tank")+""), 978, 500, null);
        	
        	// Tank resets
        	playerStats.put("1X", 10);
    		playerStats.put("1Y", 800);
    		playerStats.put("2X", 2310);
    		playerStats.put("2Y", 800);
    		playerStats.put("1GoingRight", true);
    		playerStats.put("2GoingRight", false);
    		playerStats.put("1HP", 100);
    		playerStats.put("2HP", 100);
        	// General game resets
    		timer = 0;
    		speed = 6;
    		affected = 0; // no one is affected
    		baseDamage = 10;
			currentTurn = 1;
			currentAbility = 1;
			gameOver = false;
			fire = false;
			power = 0;
			powerBarHeight = 0;
			electrogunLength = 0;
			fireIncrement = 10;
			increment = 40;
			gravity = 2;
			scorchLocations.clear();
    		asteroid1 = true;
    		asteroid2 = true;
    		asteroid3 = false;
    		fireDrawn = false;
    		inCrate = false;
    		illegalUse = false;

    		deathDevKeyUsed = false;
    		deathDevKeyUsed = false;
    		
			PVPEndScreenFrameCounter++;	
        }
        else if(gameState == 5){ // PVP Leaderboard GS
        	g.drawImage(PVPLeaderboard, 0, 0, null);
        	leaderboardScreenFrameCounter++;
        	
			// Textfile streaming for leaderboard
			if(!((playerStats.get("1Name")+"").equals("")) && !((playerStats.get("2Name")+"").equals(""))
					&& !gameDevKeyUsed && !leaderboardStreamed && previousGS==4) {
				readLeaderboard();
				// updating p1
				updateLeaderboard(playerStats.get("1Name")+"", !(Boolean)playerStats.get("1Dead"));
				// updating p2
				updateLeaderboard(playerStats.get("2Name")+"", !(Boolean)playerStats.get("2Dead"));
				sortLeaderboard();
				leaderboardStreamed = true;
			}
			g.setColor(new Color(255, 255, 255));
			g.setFont(new Font("Arial", 1, 70));
			try {
				Scanner inputFile = new Scanner(new File("players.txt"));
				if(inputFile.hasNextLine()) inputFile.nextLine();
				while(inputFile.hasNextLine() && playersShown < 3) {
					playersShown++;
					String line = inputFile.nextLine();
					String[] lineParts = line.split("\\s+");
					g.drawString(lineParts[0], 592, 670+displaySpacing);
					g.drawString(String.format("%.0f", Double.parseDouble(lineParts[1])), 1345, 670+displaySpacing);
					g.drawString(String.format("%.1f", Double.parseDouble(lineParts[2])*100)+"%", 1760, 670+displaySpacing);
					displaySpacing += 130;
				}
				inputFile.close();
				playersShown = 0;
				displaySpacing = 0;		
			} catch (IOException e) {
	        	e.printStackTrace();
	        }
			
			winnerPlayer = 0;
			loserPlayer = 0;
    		playerStats.put("1Dead", false);
    		playerStats.put("2Dead", false);
        }
	} 
	public static ArrayList<String> sortLine(ArrayList<String> lines){
		ArrayList<String> newLines = lines;
		int len = newLines.size();
		for(int i = 0;i<len-1;i++)
		{
			for(int j = 0;j<len -1- i;j++)
			{
				String str = newLines.get(j);
				String[] parts1 = str.split("\\s+");
				double num1 = Double.parseDouble(parts1[1]);
				String str2 = newLines.get(j+1);
				String[] parts2 = str2.split("\\s+");
				double num2 = Double.parseDouble(parts2[1]);
				if(num1<num2){
					String temp = newLines.get(j);
                    newLines.set(j, newLines.get(j+1));
                    newLines.set(j+1, temp);
				}
			}
		}
		return newLines;
	}
	// Leaderboard methods
    public static void sortLeaderboard() {
        ArrayList<String> lines = new ArrayList<>();
        
        try {
            Scanner inputFile = new Scanner(new File("players.txt"));
			inputFile.nextLine();
			while (inputFile.hasNextLine()) {
                lines.add(inputFile.nextLine());
            }
			inputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
		lines = sortLine(lines);
        
        try {
        	PrintWriter outputFile = new PrintWriter(new File("players.txt"));
            for (String line : lines) {
				outputFile.println();
                outputFile.print(line);
            }
			
            outputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static Map<String, double[]> readLeaderboard() {
        Map<String, double[]> leaderboard = new HashMap<>();
        try {
        	Scanner inputFile = new Scanner(new File("players.txt"));
        	while (inputFile.hasNextLine()) {
            	String name = inputFile.next();
                double wins = inputFile.nextDouble();
                double winrate = inputFile.nextDouble();
                double battles = inputFile.nextDouble();
                double[] gameStats = {wins, winrate, battles};
                leaderboard.put(name, gameStats);
            }
        	inputFile.close();
        } catch (IOException e) {
        	e.printStackTrace();
        }
        return leaderboard;
    }
    public static void updateLeaderboard(String playerName, boolean win) {
        Map<String, double[]> leaderboard = readLeaderboard();
        double[] gameStatsDefault = {0, 0, 0};
        // Update the player's wins or add a new player
        double[] gameStatsUpdate = leaderboard.getOrDefault(playerName, gameStatsDefault);
        gameStatsUpdate[2]++;
        if(win) gameStatsUpdate[0]++;
        gameStatsUpdate[1] = gameStatsUpdate[0]/gameStatsUpdate[2];
        leaderboard.put(playerName, gameStatsUpdate);
        // Write the updated leaderboard back to the file
        try {
        	PrintWriter outputFile = new PrintWriter(new FileWriter("players.txt"));
        	for (Map.Entry<String, double[]> entry : leaderboard.entrySet()) {
            	String name = entry.getKey();
            	double[] stats = entry.getValue();
            	outputFile.println();
            	outputFile.printf("%s %f %f %f", name, stats[0], stats[1], stats[2]);
            }
        	outputFile.close();
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
    
	public static void main(String[] args) throws IOException{
    	// Disable Java from automatically scaling content based on system’s DPI settings
    	System.setProperty("sun.java2d.uiScale", "1.0");
    	// JFrame and JPanel
    	JFrame frame = new JFrame("Juggernaut Assault");
        Main panel = new Main();
        frame.setIconImage(iconImg.getImage());
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	
    	// Image Importation:
    	// GS
    	home 			= ImageIO.read(new File("GameStates/GS0 - Menu Screen.png"));
    	credits 		= ImageIO.read(new File("GameStates/GS1 - Credit.png"));
    	tankSelection 	= ImageIO.read(new File("GameStates/GS2 - Tank Selection.png"));
    	PVPMode 		= ImageIO.read(new File("GameStates/GS3 - PVP V2.png"));
    	PVPPause 		= ImageIO.read(new File("PVP Pause Screen.png"));
    	PVPEnd 			= ImageIO.read(new File("GameStates/GS4 - PVP End Screen.png"));
    	PVPLeaderboard 	= ImageIO.read(new File("GameStates/GS5 - PVP Mode Leaderboard.png"));
    	
		// Obstacles
		crate = ImageIO.read(new File("Obstacles/crate.png"));
		asteroid = ImageIO.read(new File("Obstacles/asteroid.png"));
		explosion 		= ImageIO.read(new File("Abilities/explosion.png"));
		
		
		// Abilities
    	bomb 			= ImageIO.read(new File("Abilities/bomb.png"));
    	shield 			= ImageIO.read(new File("Abilities/shield.png"));
    	aiming 			= ImageIO.read(new File("Abilities/aim.png"));
    	scorchMark		= ImageIO.read(new File("Abilities/Bomb Scorch Mark.png"));
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
    	tankImages.put("Titan", ImageIO.read(new File("Tanks/Titan Tank.png")));
		tankImages.put("Mouse", ImageIO.read(new File("Tanks/Mouse Tank.png")));
		tankImages.put("Ironclad", ImageIO.read(new File("Tanks/Ironclad Tank.png")));
		tankImages.put("Sentinel", ImageIO.read(new File("Tanks/Sentinel Tank.png")));
		tankImages.put("Mouse Mini", ImageIO.read(new File("Tanks/Mouse Tank mini.png")));
		
		playerStats.put("1Name", "");
		playerStats.put("2Name", "");

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
		
		for(int i = 0; i < 5; i++) {
			tankFireImageArr[i] = ImageIO.read(new File("Tank Fires/fire_sprite_"+(i+1)+".png"));
		}
		
		
		// For PVP End Screen
		tankImagesEndScreen.put("Titan", ImageIO.read(new File("Tanks - End Screen Versions/Titan Tank.png")));
		tankImagesEndScreen.put("Mouse", ImageIO.read(new File("Tanks - End Screen Versions/Mouse Tank.png")));
		tankImagesEndScreen.put("Ironclad", ImageIO.read(new File("Tanks - End Screen Versions/Ironclad Tank.png")));
		tankImagesEndScreen.put("Sentinel", ImageIO.read(new File("Tanks - End Screen Versions/Sentinel Tank.png")));
    	
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
				if((playerStats.get("1Name")+"").length() <= 10 && Character.isLetterOrDigit(e.getKeyChar()))
					playerStats.put("1Name", (playerStats.get("1Name")+"")+ e.getKeyChar());
				if(e.getKeyCode() == 8 && (playerStats.get("1Name")+"").length()>0) { // Deleting letters
					playerStats.put("1Name", (playerStats.get("1Name")+"").substring(0, (playerStats.get("1Name")+"").length()-1));
				}	
			}
			else if(isTypingP2){
				if((playerStats.get("2Name")+"").length() <= 10 && Character.isLetterOrDigit(e.getKeyChar()))
					playerStats.put("2Name", (playerStats.get("2Name")+"")+ e.getKeyChar());
				if(e.getKeyCode() == 8 && (playerStats.get("2Name")+"").length()>0) { // Deleting letters
					playerStats.put("2Name", (playerStats.get("2Name")+"").substring(0, (playerStats.get("2Name")+"").length()-1));
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
			if(e.getKeyChar() == 32 && !isPaused && !gameOver) {
				if(currentAbility == 1 && !bombIsInAir) {
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
    		if(e.getKeyChar() == '=' && !isPaused && !gameOver) { // dev key for finishing in PVP mode
    			gameState = 4;
    			gameDevKeyUsed = true;
    		}
    		if(e.getKeyChar() == '/' && !isPaused && !gameOver) { // dev key for player turn switch
    			PVPMethods.changeTurns();
    		}
    		if(e.getKeyChar() == '0' && !isPaused) { // dev key for killing both tanks & reviving
    			playerStats.put("1Dead", !(Boolean)playerStats.get("1Dead"));
    			playerStats.put("2Dead", !(Boolean)playerStats.get("2Dead"));
    			gameOver = !gameOver;
    			deathDevKeyUsed = true;
    		}

			if(e.getKeyChar() == '1' && !isPaused && !gameOver){
				currentAbility = 1;
			}
			if(e.getKeyChar() == '2' && !isPaused && !gameOver){
				if(!inCrate) {
					currentAbility = 2;
				}
				else if(inCrate) {
					if (!"Titan".equals(playerStats.get(currentTurn+"Tank")) && !"Sentinel".equals(playerStats.get(currentTurn+"Tank"))){
						currentAbility = 2;
					}
					else {
						illegalUse = true;
					}
				}
				
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