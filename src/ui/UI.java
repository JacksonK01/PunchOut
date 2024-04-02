package ui;

import entity.Player;
import gamepanel.GamePanel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;

public class UI {
    private BufferedImage spriteSheet;
    private BufferedImage[] numberSprites = new BufferedImage[10];
    private BufferedImage[] roundSprites = new BufferedImage[3];
    GamePanel gp;

    public int textSmaller = 2;
    public int textGap =1;
    private Font customFont;

    public UI(GamePanel gp) {
        this.gp = gp;

        try {
            //numbers are in the same file as the sprite sheet for the boxing ring
            spriteSheet = ImageIO.read(getClass().getResource("/ui/ui.png"));
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Resource/ui/punch-out-nes.ttf")).deriveFont(20f);
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
        //this.roundSprites[i] = spriteSheet.getSubimage(x*7+xOffset, 12, 32, 6);

    }

        public void update() {

        }
        public void drawScore(Graphics2D g2){
            // made it without an image builder, but it works ig
            int score = gp.player.testScore;
            String scoreStr = Integer.toString(score);
            /*
            for (int i = scoreStr.length()-1; i >= 0; i--) {
                int digit = Character.getNumericValue(scoreStr.charAt(i));
                g2.drawImage(numberSprites[digit], 200* gp.scale - (Math.abs(i-scoreStr.length()-1)) * (numberSprites[digit].getWidth()-textGap) * gp.scale , 21 * gp.scale + 2, numberSprites[digit].getWidth()* gp.scale - textSmaller, numberSprites[digit].getHeight()* gp.scale - textSmaller,  null);
            }
            */
            g2.setFont(customFont);
            g2.setColor(Color.WHITE);
            // TODO: If keep, make font always keep last digit in the same place
            g2.drawString(scoreStr, 160* gp.scale, 27* gp.scale + 2);
        }
        public void drawStamina(Graphics2D g2){
            //int stamina = gp.player.;
            int stamina = 20;
            String scoreStr = Integer.toString(stamina);
            for (int i = scoreStr.length()-1; i >= 0; i--) {
                int digit = Character.getNumericValue(scoreStr.charAt(i));
                g2.drawImage(numberSprites[digit], (((78 * gp.scale) - (Math.abs(i-scoreStr.length()-1)) * (numberSprites[digit].getWidth()) * gp.scale)), 14* gp.scale + 2, numberSprites[digit].getWidth()* gp.scale - textSmaller, numberSprites[digit].getHeight()* gp.scale - textSmaller,  null);
            }
        }
        public void drawCharge(Graphics2D g2){
            //int charge = gp.player.;
            int charge = 10;
            String scoreStr = Integer.toString(charge);
            for (int i = scoreStr.length()-1; i >= 0; i--) {
                int digit = Character.getNumericValue(scoreStr.charAt(i));
                g2.drawImage(numberSprites[digit], 45 * gp.scale - (Math.abs(i-scoreStr.length()-1)) * (numberSprites[digit].getWidth()-textGap) * gp.scale , 14* gp.scale + 2, numberSprites[digit].getWidth()* gp.scale - textSmaller, numberSprites[digit].getHeight()* gp.scale - textSmaller,  null);
            }
        }
        public void drawHealth(Graphics2D g2){
            int maxHealthPlayer = 100; // Maximum health
            int maxHealthEnemy = 100; // Maximum health
            //int currentHealth = gp.player.health; // Current health
            //int currentHealthEnemy = gp.enemy.health; // Current health
            int currentHealthPlayer = 80;
            int currentHealthEnemy = 95;

            int barWidth = 49 * gp.scale - 2; // Width of the health bar
            int barHeight = 6 * gp.scale + 1; // Height of the health bar

            int x1 = 88 * gp.scale; // X position of the health bar
            int y1 = 14 * gp.scale - 1; // Y position of the health bar
            int x2 = 144 * gp.scale; // X position of the health bar
            int y2 = 14 * gp.scale - 1; // Y position of the health bar

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

            // Draw the border of the health bar
            //g2.setColor(Color.BLACK);
            //g2.drawRect(x, y, barWidth, barHeight);
}
        public void drawScoreboard(Graphics2D g2){
            // get round on sprite sheet
            // get timer, use font to display
            // x 0 to 32 y 12 to 17 on sprite sheet


        }
        public void draw (Graphics2D g2){
            drawScore(g2);
            drawStamina(g2);
            drawCharge(g2);
            drawHealth(g2);
        }
    }