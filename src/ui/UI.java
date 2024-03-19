package ui;

import entity.Player;
import gamepanel.GamePanel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;

public class UI {
    private BufferedImage spriteSheet;
    private BufferedImage[] numberSprites = new BufferedImage[10];
    private BufferedImage displayNumImage;
    GamePanel gp;


    public UI(GamePanel gp) {
        this.gp = gp;
        try {
            //numbers are in the same file as the sprite sheet for the boxing ring
            spriteSheet = ImageIO.read(getClass().getResource("/ui/ui.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int totalWidth = spriteSheet.getWidth();
        int totalHeight = spriteSheet.getHeight();

        int i = 0;
        int xOffset = 1;
        for(int x = 0; x < numberSprites.length; x++){
            this.numberSprites[i] = spriteSheet.getSubimage(x*7+xOffset, 1, 7, 7);
            xOffset += 1;
            i++;
        }
        displayNumImage = numberSprites[0];
    }

        public void update() {
            /*
            int score = gp.player.score;
            String scoreStr = Integer.toString(score);
            for (int i = scoreStr.length()-1; i > 0; i--) {
                int digit = Character.getNumericValue(scoreStr.charAt(i));
                displayNumImage = numberSprites[digit];


            }
            */
        }
        public void drawScore(Graphics2D g2){
            // made it without an image builder, but it works ig
            int score = gp.player.testScore;
            String scoreStr = Integer.toString(score);
            for (int i = scoreStr.length()-1; i >= 0; i--) {
                int digit = Character.getNumericValue(scoreStr.charAt(i));
                g2.drawImage(numberSprites[digit], 200* gp.scale - (Math.abs(i-scoreStr.length()-1)) * numberSprites[digit].getWidth() * gp.scale , 21* gp.scale, numberSprites[digit].getWidth()* gp.scale, numberSprites[digit].getHeight()* gp.scale,  null);
            }
        }
        public void drawHealth(Graphics2D g2){
            //int health = gp.player.;
            int health = 100;
            String scoreStr = Integer.toString(health);
            for (int i = scoreStr.length()-1; i >= 0; i--) {
                int digit = Character.getNumericValue(scoreStr.charAt(i));
                g2.drawImage(numberSprites[digit], 80 * gp.scale - (Math.abs(i-scoreStr.length()-1)) * numberSprites[digit].getWidth() * gp.scale , 14* gp.scale, numberSprites[digit].getWidth()* gp.scale, numberSprites[digit].getHeight()* gp.scale,  null);
            }
        }
        public void drawCharge(Graphics2D g2){
            //int charge = gp.player.;
            int charge = 10;
            String scoreStr = Integer.toString(charge);
            for (int i = scoreStr.length()-1; i >= 0; i--) {
                int digit = Character.getNumericValue(scoreStr.charAt(i));
                g2.drawImage(numberSprites[digit], 47 * gp.scale - (Math.abs(i-scoreStr.length()-1)) * numberSprites[digit].getWidth() * gp.scale , 14* gp.scale, numberSprites[digit].getWidth()* gp.scale, numberSprites[digit].getHeight()* gp.scale,  null);
            }
        }

        public void draw (Graphics2D g2){
            drawScore(g2);
            drawHealth(g2);
            drawCharge(g2);
        }
    }