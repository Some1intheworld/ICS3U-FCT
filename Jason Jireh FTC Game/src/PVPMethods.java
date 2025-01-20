import java.awt.Color;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.image.BufferedImage;


public class PVPMethods {
	
	public static BufferedImage flipImageHorizontal(BufferedImage image) {
		// Flips the image horizontally by flipping each pixel horizontally in the rows
		// Goes through all rows (in original image) from top to bottom
		int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage flippedImage = new BufferedImage(width, height, image.getType()); 
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                flippedImage.setRGB(width-1-x, y, image.getRGB(x, y));
            }
        } 
        return flippedImage;
    }
	
	public static BufferedImage flipImageVertical(BufferedImage image) {
		// Flips the image vertically by flipping each pixel vertically in the columns
		// Goes through all rows (in original image) from top to bottom
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage flippedImage = new BufferedImage(width, height, image.getType());
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                flippedImage.setRGB(x, height-1-y, image.getRGB(x, y));
            }
        }
        return flippedImage;
    }
	
	public static void playerMovement() {
		// Moves players using their coordinates and direction from playerStats map
		// based on which turn it is 
		if(Main.aPressed && (int)Main.playerStats.get(Main.currentTurn+"X") >= -50 && movementRestrictionLeft()) {
			Main.playerStats.put(Main.currentTurn+"X", (int)Main.playerStats.get(Main.currentTurn+"X") - Main.speed);
    		Main.playerStats.put(Main.currentTurn+"GoingRight", false);
    	}
    	if(Main.dPressed && (int)Main.playerStats.get(Main.currentTurn+"X")+180 <= 2550 && movementRestrictionRight()) {
    		Main.playerStats.put(Main.currentTurn+"X", (int)Main.playerStats.get(Main.currentTurn+"X") + Main.speed);
    		Main.playerStats.put(Main.currentTurn+"GoingRight", true);
		}
	}
	
	public static BufferedImage drawPlayer(int generatePlayer) {
		// Generates players' tanks, whether facing right, left, or flipped in those orientations
		BufferedImage drawnImage = new BufferedImage(180, 134, BufferedImage.TYPE_INT_RGB);
		
		if((Boolean)Main.playerStats.get(generatePlayer+"Dead") && (Boolean)Main.playerStats.get(generatePlayer+"GoingRight")) { // P1 death flip facing right
			drawnImage = flipImageVertical(flipImageHorizontal(Main.tankImages.get(Main.playerStats.get(generatePlayer+"Tank"))));
			Main.tankFlippedLow = 25;
		}
		else if ((Boolean)Main.playerStats.get(generatePlayer+"Dead")) { // death facing left
			drawnImage = flipImageVertical(Main.tankImages.get(Main.playerStats.get(generatePlayer+"Tank")));
			Main.tankFlippedLow = 25;
		}
		else if((Boolean)Main.playerStats.get(generatePlayer+"GoingRight")) { // Alive facing right
			drawnImage = flipImageHorizontal(Main.tankImages.get(Main.playerStats.get(generatePlayer+"Tank")));
			Main.tankFlippedLow = 0;
		}
		else { // Alive facing left
			drawnImage = Main.tankImages.get(Main.playerStats.get(generatePlayer+"Tank"));
			Main.tankFlippedLow = 0;
		}
		return drawnImage;	
	}

		public static BufferedImage drawPlayerMouse(int generatePlayer) {
		// Generates players' tanks, whether facing right, left, or flipped in those orientations
			BufferedImage drawnImage = new BufferedImage(180, 134, BufferedImage.TYPE_INT_RGB);
			if((Boolean)Main.playerStats.get(generatePlayer+"Dead") && (Boolean)Main.playerStats.get(generatePlayer+"GoingRight")) { // P1 death flip facing right
				drawnImage = flipImageVertical(flipImageHorizontal(Main.tankImages.get(Main.playerStats.get(generatePlayer+"Tank"))));
				Main.tankFlippedLow = 25;
			}
			else if ((Boolean)Main.playerStats.get(generatePlayer+"Dead")) { // death facing left
				drawnImage = flipImageVertical(Main.tankImages.get(Main.playerStats.get(generatePlayer+"Tank")));
				Main.tankFlippedLow = 25;
			}
			else if((Boolean)Main.playerStats.get(generatePlayer+"GoingRight")) { // Alive facing right
				drawnImage = flipImageHorizontal(Main.tankImages.get(Main.playerStats.get(generatePlayer+"Tank")));
				Main.tankFlippedLow = 0;
			}
			else { // Alive facing left
				drawnImage = Main.tankImages.get(Main.playerStats.get(generatePlayer+"Tank"));
				Main.tankFlippedLow = 0;
		}
		return drawnImage;	
	}
	
	public static void powerRangeDeterminer() {
		// Determines power of shot. Constantly increases by 10 pixels until 1700 pixels (max range), then decrease by 10 until 0
		Main.power += Main.increment;
		Main.powerBarHeight -= 0.2 * (Main.increment);
		if(Main.power >= 1700) {
			Main.increment = -40;
		} else if(Main.power <= 0) Main.increment = 40;

	}
	public static void bombStartAndDirectionLocate() {
		// locates start of bomb (where the player tank is), keeps that as the start
		// even if player moves, and keeps the same fire direction even if player turns.
		// Gets direction
		Main.bombX = (int)Main.playerStats.get(Main.currentTurn+"X");
		Main.bombY = (int)Main.playerStats.get(Main.currentTurn+"Y")-256;
		Main.bombXStart = Main.bombX;
		Main.bombYStart = Main.bombY;
		Main.bombDirectionRight = (Boolean)Main.playerStats.get(Main.currentTurn+"GoingRight");
	}

	public static void enemyHitCheck() {
		// Bomb collision
		if(Main.fire && !(Main.bombX>=(int)Main.playerStats.get(Main.enemyPlayer+"X")+160 ||
			Main.bombX+120<=(int)Main.playerStats.get(Main.enemyPlayer+"X")+20 ||
			Main.bombY>=(int)Main.playerStats.get(Main.enemyPlayer+"Y")+114 ||
			Main.bombY+115<=(int)Main.playerStats.get(Main.enemyPlayer+"Y")+20))
		{
			Main.explode = true;
			if(Main.baseDamage == 5) {
				dealDamage(10);
			} else dealDamage(Main.baseDamage + 10);
			Main.enemyHit = true;
			Main.fire = false;
			Main.bombIsInAir = false;
			changeTurns();
	
		}
		// !------------ Bomb collision with obstacles
		// Crate
		if(Main.fire && !(Main.bombX>=Main.crateX +350 ||
				Main.bombX+100<= Main.crateX ||
				Main.bombY>= Main.crateY + 350||
				Main.bombY+80<=Main.crateY))
			{
				Main.enemyHit = true;
				Main.explode = true;
				Main.fire = false;
				Main.bombIsInAir = false;
				changeTurns();
		
			}
		// Asteroid 1
		if(Main.asteroid1 && (Main.fire && !(Main.bombX>=400 +362 ||
				Main.bombX+100<= 400 ||
				Main.bombY>= Main.asteroidY + 363||
				Main.bombY+80<=Main.asteroidY)))
			{
				Main.enemyHit = true;
				Main.explode = true;
				Main.fire = false;
				Main.bombIsInAir = false;
				Main.asteroid1 = false;
				changeTurns();
		
			}
		if(Main.asteroid2 && (Main.fire && !(Main.bombX>=1650 +362 ||
				Main.bombX+100<= 1650 ||
				Main.bombY>= Main.asteroidY + 363||
				Main.bombY+80<=Main.asteroidY)))
			{
				Main.enemyHit = true;
				Main.explode = true;
				Main.fire = false;
				Main.bombIsInAir = false;
				Main.asteroid2 = false;
				changeTurns();
		
			}

		if(Main.asteroid3 && (Main.fire && !(Main.bombX>=Main.asteroidX +362 ||
			Main.bombX+100<= Main.asteroidX ||
			Main.bombY>= Main.asteroidY + 363||
			Main.bombY+80<=Main.asteroidY)))
		{
			Main.enemyHit = true;
			Main.explode = true;
			Main.fire = false;
			Main.bombIsInAir = false;
			Main.asteroid3 = false;
			changeTurns();
	
		}
		
	}
	public static void inCrateCheck() {
		// Disables 2nd ability when in crate
		if((int)Main.playerStats.get(Main.currentTurn+"X")>=Main.crateX &&
		(int)Main.playerStats.get(Main.currentTurn+"X")+180<= Main.crateX+350)
		{
			if(("Titan".equals(Main.playerStats.get(Main.currentTurn+"Tank"))
					||"Sentinel".equals(Main.playerStats.get(Main.currentTurn+"Tank")))
					&& Main.currentAbility == 2){
				Main.inCrate = true;
			}
					
		}
		if(!((int)Main.playerStats.get(Main.currentTurn+"X")>=Main.crateX &&
		(int)Main.playerStats.get(Main.currentTurn+"X")+100<= Main.crateX+350))
		{
			Main.inCrate = false;
		}
	}
	public static void deathCheck() {
		// Checks if any player is dead and changes the necessary variables
		for(int targetPlayer = 1; targetPlayer <= 2; targetPlayer++) {
			if((int)Main.playerStats.get(targetPlayer+"HP") <= 0) 
			{
				Main.playerStats.put(targetPlayer+"Dead", true);
				Main.winnerPlayer = (targetPlayer==1)? 2:1;
				Main.loserPlayer = targetPlayer;
				Main.gameOver = true;
				Main.deathFrameCounter++;
				if(Main.deathFrameCounter>=50) {
					Main.gameState = 4;
					Main.deathFrameCounter = 0;
				}
				
			}
		}
	}
	
	public static String displayName(String name, int player) {
		// displays player name. if name is empty, display "Player #"
		if(name.equals("")) return "Player " + player;
		return name;
	}
	public static Color nameColor(boolean dead) {
		// returns red color for name if player is dead, white otherwise
		if(dead) return new Color(200, 0, 0);
		return new Color(255, 255, 255);
	}
   public static void velocityDeterminer(){
        Main.reachTime = Math.sqrt((2 * Main.power) / Main.gravity); // Time to reach the target
        if(Main.power > 500) {
           Main.velocity = (int) ((Main.gravity * Main.reachTime / 2 -10) * -1); 
        } else if(Main.power < 500) {
            Main.velocity = (int) ((Main.gravity * Main.reachTime / 2 -25) * -1); 
        }
        Main.gravity = 2; 
    }

    public static void bombDirection() {
        // Determines fireIncrement for direction of bomb (moving left or right),
        // Executes direction through fireIncrement
        if(Main.bombDirectionRight) { // Tank facing right when fired
            Main.fireIncrement = (int) (Main.power / Main.reachTime); // Adjust to cover desired distance
        } else { // Tank facing left when fired
            Main.fireIncrement = -(int) (Main.power / Main.reachTime); // Adjust to cover desired distance
        }
    }
	
	public static void changeTurns() {
		Main.currentAbility = 1;
		Main.timer = 0;
		Main.power = 0;
		Main.powerBarHeight = 0;
		if(Main.currentTurn == 1) {
			if(Main.affected == 1) {
				Main.speed = 6;
				Main.baseDamage = 10;
				Main.P1Size = "normal";
			}
		} else if(Main.currentTurn == 2) {
			if(Main.affected == 2) {
				Main.speed = 6;
				Main.baseDamage = 10;
				Main.P1Size = "normal";
			}
		}
		Main.currentTurn = (Main.currentTurn == 1)? 2 : 1;
		Main.inCrate = false;
	}
	public static void dealDamage(int damage) {
		Main.playerStats.put(Main.enemyPlayer+"HP", (int)Main.playerStats.get(Main.enemyPlayer+"HP")-damage);
	}
	
	public static boolean movementRestrictionRight() {
	    int playerX = (int)Main.playerStats.get(Main.currentTurn + "X");
	    
	    // Asteroid 1
	    if (playerX + 180 > 450 && playerX + 180 < 888 && Main.asteroid1) { 
	        return false;
	    }
	    
	    // Asteroid 2
	    if (playerX + 180 > 1710 && playerX + 180 < 2138 && Main.asteroid2) { 
	        return false;
	    }
	    
	    // Asteroid 3
	    if (playerX + 180 > Main.asteroidX + 50 && playerX + 180 < Main.asteroidX + 488 && Main.asteroid3) { 
	        return false;
	    }

	    return true; 
	}
	
	public static boolean movementRestrictionLeft() {
	    int playerX = (int)Main.playerStats.get(Main.currentTurn + "X");
	    
	    // Asteroid 1
	    if (playerX < 740 && playerX > 400 && Main.asteroid1) { 
	        return false;
	    }
	    
	    // Asteroid 2
	    if (playerX < 2000 && playerX > 1650 && Main.asteroid2) { 
	        return false;
	    }
	    
	    // Asteroid 3
	    if (playerX < Main.asteroidX + 468 - 118 && playerX > Main.asteroidX && Main.asteroid3) { 
	        return false;
	    }

	    return true;
	}

	public static boolean checkObstaclePlacing() {
			PointerInfo a = MouseInfo.getPointerInfo();
			Point b = a.getLocation();
			int playerX = (int)Main.playerStats.get(Main.currentTurn + "X");
			int enemyX = (int)Main.playerStats.get(Main.enemyPlayer + "X");
			int mouseX = (int) b.getX()-100;
			if((playerX >= mouseX && playerX <= mouseX + 488) || (playerX + 180 <= mouseX + 488 && playerX + 180 >= mouseX)) {
				return false;
			}
			if((enemyX >= mouseX && enemyX <= mouseX + 488) || (enemyX + 180 <= mouseX + 488 && enemyX + 180 >= mouseX)) {
				return false;
			}
			return true;
	}
}