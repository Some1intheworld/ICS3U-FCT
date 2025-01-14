import java.awt.Color;
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
		// Moves players using the respective arrays of their coordinates and direction
		// based on which turn it is (-1 due to array index starting at 0)
		if(Main.aPressed) {
    		Main.playerCoordinatesArr[Main.currentTurn-1] -= 5;
    		Main.playerDirectionArr[Main.currentTurn-1] = false;
    	}
    	if(Main.dPressed) {
    		Main.playerCoordinatesArr[Main.currentTurn-1] += 5;
    		Main.playerDirectionArr[Main.currentTurn-1] = true;
		}
	}
	
	public static BufferedImage drawPlayer(int generatePlayer) {
		// Generates players' tanks, whether facing right, left, or flipped in those orientations
		BufferedImage drawnImage = new BufferedImage(180, 134, BufferedImage.TYPE_INT_RGB);
		
		if(Main.playerDeathArr[generatePlayer-1] && Main.playerDirectionArr[generatePlayer-1]) { // P1 death flip facing right
			drawnImage = flipImageVertical(flipImageHorizontal(Main.tankImages.get(Main.playerTankArr[generatePlayer-1]+"Tank")));
			Main.tankFlippedLow = 25;
		}
		else if (Main.playerDeathArr[generatePlayer-1]) { // death facing left
			drawnImage = flipImageVertical(Main.tankImages.get(Main.playerTankArr[generatePlayer-1]+"Tank"));
			Main.tankFlippedLow = 25;
		}
		else if(Main.playerDirectionArr[generatePlayer-1]) { // Alive facing right
			drawnImage = flipImageHorizontal(Main.tankImages.get(Main.playerTankArr[generatePlayer-1]+"Tank"));
			Main.tankFlippedLow = 0;
		}
		else { // Alive facing left
			drawnImage = Main.tankImages.get(Main.playerTankArr[generatePlayer-1]+"Tank");
			Main.tankFlippedLow = 0;
		}
		return drawnImage;	
	}
	
	public static void powerRangeDeterminer() {// gg
		// Determines power of shot. Constantly increases by 10 pixels until 1700 pixels (max range), then decrease by 10 until 0
		Main.power += Main.increment;
		Main.powerBarHeight -= 0.2 * (Main.increment);
		Main.velocity = Main.power / 2;
		if(Main.power >= 1700) {
			Main.increment = -10;
		} else if(Main.power <= 0) Main.increment = 10;

	}
	public static void bombStartAndDirectionLocate() {
		// locates start of bomb (where the player tank is), keeps that as the start
		// even if player moves, and keeps the same fire direction even if player turns.
		// Gets direction
		Main.bombX = Main.playerCoordinatesArr[Main.currentTurn-1];
		Main.bombY = Main.playerCoordinatesArr[Main.currentTurn+1]-256;
		Main.bombXStart = Main.bombX;
		Main.bombYStart = Main.bombY;
		Main.bombDirectionRight = Main.playerDirectionArr[Main.currentTurn-1];
	}
	public static void bombDirection() {
		// Determines fireIncrement for direction of bomb (moving left or right),
		// direction obtained from bombStartAndDirectionLocate().
		// Executes direction through fireIncrement
		if(Main.bombDirectionRight) { // tank facing right when fired
			Main.fireIncrement = 10;
		}
		else{ // Tank facing left when fired
			Main.fireIncrement = -10;
		}
	}
	public static void bombPowerCheck() {
		// Checks if the bomb is out of power from the power determined in powerRangeDeterminer()
		// Checks both the forward distance and backwards distance as bomb can be shot
		// both ways
		if((Main.fire && Main.bombX+128 > Main.bombXStart + Main.power) ||
				(Main.fire && Main.bombX+128 < Main.bombXStart - Main.power)) {
			Main.fire = false;
			Main.fireYIncrement = -4;
			Main.velocity = -40;
		}

	}
	public static void enemyHitCheck() {
		// Checks if the middle of bomb along its x is within the enemy tank's x range
		if((Main.fire && Main.bombX+128 >= Main.playerCoordinatesArr[Main.enemyPlayer-1] &&
				Main.bombX+128 <= Main.playerCoordinatesArr[Main.enemyPlayer-1]+180)) {
			Main.fire = false;
			Main.fireYIncrement = -4;
			Main.velocity = -50;
	
		}
	}
	public static boolean deathCheck() {
		// Checks if any player is dead
		return Main.playerDeathArr[0] || Main.playerDeathArr[1];
	}
	
	public static String displayName(String name, int player) {
		// displays player name. if name is empty, display "Player #"
		if(name == "") return "Player " + player;
		return name;
	}
	public static Color nameColor(boolean dead) {
		// returns red color for name if player is dead, white otherwise
		if(dead) return new Color(216, 75, 61);
		return new Color(255, 255, 255);
	}
}