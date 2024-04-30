package ui;

import game.GamePanel;
import game.GamePhase;
import game.GamePhaseManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;

/**
 * Represents the user interface for the Punch Out game.
 * Handles rendering of the game's UI elements.
 */
public class UI {
    private BufferedImage spriteSheet;
    private BufferedImage[] numberSprites = new BufferedImage[10];
    private BufferedImage[] roundSprites = new BufferedImage[3];
    GamePanel gp;

    public int textSmaller = 2;
    public int textGap =1;
    private Font customFont;

    //Max amount of digits a scoreboard can display
    public final int MAX_DIGITS = 6;

    /**
     * Constructs a new UI with the specified game panel.
     * @param gp
     */

    public UI(GamePanel gp) {
        this.gp = gp;

        try {
            //numbers are in the same file as the sprite sheet for the boxing ring
            spriteSheet = ImageIO.read(getClass().getResource("/textures/ui/ui.png"));
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Resource/textures/ui/punch-out-nes.ttf")).deriveFont(20f);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        int i = 0;
        int xOffset = 1;
        for(int x = 0; x < numberSprites.length; x++){
            this.numberSprites[i] = spriteSheet.getSubimage(x*7+xOffset, 1, 7, 7);

            xOffset += 1;
            i++;
        }
        // x 0 to 32 y 12 to 17 on sprite sheet
        // 115 total pixels, 35 pixels per round
        for(int x = 0; x < roundSprites.length; x++) {
            this.roundSprites[x] = spriteSheet.getSubimage((x*36)+1, 12, 32, 7);
        }
    }

    /**
     * Draws the player's score on the provided graphics context.
      * @param g2
     */
    public void drawScore(Graphics2D g2){
            // made it without an image builder, but it works ig
            int score = gp.getGameEngine().getPlayer().score;

            String scoreStr = Integer.toString(score);

            int shift = (MAX_DIGITS - scoreStr.length()) * 20;
            g2.setFont(customFont);
            g2.setColor(Color.WHITE);
            // TODO: If keep, make font always keep last digit in the same place
            g2.drawString(scoreStr, 150* gp.scale + shift, 27* gp.scale + 2);
        }
        /**
         * Draws the player's stamina on the provided graphics context.
         * @param g2
         */
        public void drawStamina(Graphics2D g2){
            int stamina = gp.getGameEngine().getPlayer().getStamina();

            if (stamina < 0) {
                stamina = 0;
            }
            String scoreStr = Integer.toString(stamina);
            for (int i = scoreStr.length()-1; i >= 0; i--) {
                int digit = Character.getNumericValue(scoreStr.charAt(i));
                g2.drawImage(numberSprites[digit], (((78 * gp.scale) - (Math.abs(i-scoreStr.length()-1)) * (numberSprites[digit].getWidth()) * gp.scale)), 14* gp.scale + 2, numberSprites[digit].getWidth()* gp.scale - textSmaller, numberSprites[digit].getHeight()* gp.scale - textSmaller,  null);
            }
        }
        /**
         * Draws the player's charge on the provided graphics context.
         * @param g2
         */
        public void drawCharge(Graphics2D g2){
            //int charge = gp.player.;
            int charge = 0;
            String scoreStr = Integer.toString(charge);
            for (int i = scoreStr.length()-1; i >= 0; i--) {
                int digit = Character.getNumericValue(scoreStr.charAt(i));
                g2.drawImage(numberSprites[digit], 45 * gp.scale - (Math.abs(i-scoreStr.length()-1)) * (numberSprites[digit].getWidth()-textGap) * gp.scale , 14* gp.scale + 2, numberSprites[digit].getWidth()* gp.scale - textSmaller, numberSprites[digit].getHeight()* gp.scale - textSmaller,  null);
            }
        }
        /**
         * Draws the player's health on the provided graphics context.
         * @param g2
         */
        public void drawHealth(Graphics2D g2){
            int maxHealthPlayer = 50; // Maximum health
            int maxHealthEnemy = 50; // Maximum health
            //int currentHealth = gp.player.health; // Current health
            //int currentHealthEnemy = gp.enemy.health; // Current health
            int currentHealthPlayer = gp.getGameEngine().getPlayer().getHealth();
            int currentHealthEnemy = gp.getGameEngine().getOpponent().getHealth();

            int barWidth = 49 * gp.scale - 2;
            int barHeight = 6 * gp.scale + 1;

            int x1 = 88 * gp.scale;
            int y1 = 14 * gp.scale - 1;
            int x2 = 144 * gp.scale;
            int y2 = 14 * gp.scale - 1;

            // Draw the background of the health bar
            g2.setColor(Color.BLACK);
            g2.fillRect(x1, y1, barWidth, barHeight);
            g2.fillRect(x2, y2, barWidth, barHeight);

            // Calculate the width of the filled part of the health bar
            int filledWidth = (int) ((currentHealthPlayer / (double) maxHealthPlayer) * barWidth);

            // Draw the filled part of the health bar
            g2.setColor(Color.WHITE);
            g2.fillRect(x1, y1, filledWidth, barHeight);
            // Draw filled part of bar for enemy
            filledWidth = (int) ((currentHealthEnemy / (double) maxHealthEnemy) * barWidth);
            g2.fillRect(x2, y2, filledWidth, barHeight);


}
        /**
         * Draws the scoreboard on the provided graphics context.
         * @param g2
         */
        public void drawScoreboard(Graphics2D g2){
            // get round on sprite sheet
            // get timer, use font to display
            // x 0 to 32 y 12 to 17 on sprite sheet
            // TODO: Make Round Print on screen
            int tempRound = 1;
            g2.drawImage(roundSprites[tempRound-1], 207*gp.scale, 22*gp.scale, 32* gp.scale, 6* gp.scale, null);


        }
        public int counter = 0;
        /**
         * Draws the timer on the provided graphics context.
         * @param g2
         */
        public void drawTimer(Graphics2D g2){
            // get timer, use font to display
            if(GamePhaseManager.getGlobalEventState() == GamePhase.FIGHT) {
                if(counter >= 60){
                    counter = 0;
                    GamePanel.timeElapsed++;
                }
                counter++;
            }
            // get time in min:seconds format
            int minutes = GamePanel.timeElapsed / 60;
            int seconds = GamePanel.timeElapsed % 60;
            String time = String.format("%01d:%02d", minutes, seconds);
            g2.setFont(customFont);
            g2.setColor(Color.WHITE);
            //make font size bigger
            g2.setFont(customFont.deriveFont(22f));
            g2.drawString(time, 209* gp.scale, 20* gp.scale+1);
        }
        /**
         * Draws the UI elements.
         * @param g2
         */
        public void draw (Graphics2D g2){
            drawScore(g2);
            drawStamina(g2);
            drawCharge(g2);
            drawHealth(g2);
            drawScoreboard(g2);
            drawTimer(g2);
            if(GamePhaseManager.getGlobalEventState() == GamePhase.END){
                g2.setFont(customFont.deriveFont(40f));
                g2.setColor(Color.WHITE);
                if(gp.getGameEngine().getPlayer().getHealth() <= 0){
                    g2.drawString("YOU LOSE", 80* gp.scale, 80* gp.scale);
                } else {
                    g2.drawString("YOU WIN", 80* gp.scale, 80* gp.scale);
                }
            }

        }
    }