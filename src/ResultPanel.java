import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
public class ResultPanel extends FieldPanel implements KeyListener {
  public Hero hero;
  public ArrayList<Enemy> enemies = new ArrayList<Enemy>();
  public ResultPanel(MainFrame fr, Hero hero, ArrayList<Enemy> enemies, ArrayList<Integer> enemiesID, Map map) {
    super(fr, hero, enemies, enemiesID, map, null);
    this.hero = hero; this.enemies = enemies;
    
  }
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Font bold = new Font("Arial", Font.BOLD, 50), plain = new Font("Arial", Font.PLAIN, 15);
    Color dark = new Color(0, 0, 0, 50), red = new Color(200, 0, 0), blue = new Color(0, 0, 200);
    g.setColor(dark);
    g.fillRect(0, 0, MainFrame.WIDTH, MainFrame.HEIGHT);
    g.setFont(bold);
    if (!fr.clear) {
      g.setColor(red);
      g.drawString("Game Over", 150, 150);
    } else {
      g.setColor(blue);
      g.drawString("Congratulation!!", 150, 150);
    }
    g.setFont(plain);
    g.drawString("Press any key to end...", 250, 300);
  }
  @Override
  public void keyPressed(KeyEvent e) {
    fr.state = 5;
  }
  @Override
  public void keyReleased(KeyEvent e) {
  }
  @Override
  public void keyTyped(KeyEvent e) {
  }
}