import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class TitlePanel extends JPanel implements Runnable, KeyListener {
  private MainFrame fr;
  private BufferedImage tile;

  Thread th;
  int offset = 0, r = 0;
  boolean flag = true;
  Random rnd = new Random();
  public TitlePanel(MainFrame fr) {
    this.fr = fr;
    addKeyListener(this);
    setFocusable(true);
    setOpaque(false);
    try {
      // 画像の読み込み
      tile = ImageIO.read(new File("../img/skyroop.png"));
      fr.tiles[0] = ImageIO.read(new File("../img/chara_hero.png"));
      fr.tiles[1] = ImageIO.read(new File("../img/object_stairs.png"));
      fr.tiles[2] = ImageIO.read(new File("../img/chara_slime.png"));
      fr.tiles[3] = ImageIO.read(new File("../img/item1.png"));
      fr.tiles[4] = ImageIO.read(new File("../img/weapon1.png"));
      fr.world = ImageIO.read(new File("../img/map1.png"));
      fr.duel = ImageIO.read(new File("../img/map2.png"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    fr.stair = fr.tiles[1].getSubimage(1 * MainFrame.CHIP_XSIZE, 2 * MainFrame.CHIP_YSIZE, MainFrame.CHIP_XSIZE, MainFrame.CHIP_YSIZE);
    // [0]: 上向き, [1]: 右向き, [2]: 下向き, [3]: 左向き
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 3; j++) {
        fr.heroChip[i][j] = fr.tiles[0].getSubimage((6 + j) * MainFrame.CHIP_XSIZE, (4 + i) * MainFrame.CHIP_YSIZE, MainFrame.CHIP_XSIZE, MainFrame.CHIP_YSIZE);
        fr.slimeChip[0][i][j] = fr.tiles[2].getSubimage((6 + j) * MainFrame.CHIP_XSIZE, i * MainFrame.CHIP_YSIZE, MainFrame.CHIP_XSIZE, MainFrame.CHIP_YSIZE);
        fr.slimeChip[1][i][j] = fr.tiles[2].getSubimage((9 + j) * MainFrame.CHIP_XSIZE, i * MainFrame.CHIP_YSIZE, MainFrame.CHIP_XSIZE, MainFrame.CHIP_YSIZE);
      }
    }
    fr.itemChip[0] = fr.tiles[3].getSubimage(3 * MainFrame.CHIP_XSIZE, 3 * MainFrame.CHIP_YSIZE, MainFrame.CHIP_XSIZE, MainFrame.CHIP_YSIZE); // portion
    fr.itemChip[1] = fr.tiles[3].getSubimage(9 * MainFrame.CHIP_XSIZE, 1 * MainFrame.CHIP_YSIZE, MainFrame.CHIP_XSIZE, MainFrame.CHIP_YSIZE); // green apple
    fr.itemChip[2] = fr.tiles[3].getSubimage(5 * MainFrame.CHIP_XSIZE, 3 * MainFrame.CHIP_YSIZE, MainFrame.CHIP_XSIZE, MainFrame.CHIP_YSIZE); // ether
    fr.itemChip[3] = fr.tiles[3].getSubimage(10 * MainFrame.CHIP_XSIZE, 1 * MainFrame.CHIP_YSIZE, MainFrame.CHIP_XSIZE, MainFrame.CHIP_YSIZE); // juicy apple
    fr.itemChip[4] = fr.tiles[3].getSubimage(2 * MainFrame.CHIP_XSIZE, 0 * MainFrame.CHIP_YSIZE, MainFrame.CHIP_XSIZE, MainFrame.CHIP_YSIZE); // taurine
    fr.itemChip[5] = fr.tiles[3].getSubimage(2 * MainFrame.CHIP_XSIZE, 1 * MainFrame.CHIP_YSIZE, MainFrame.CHIP_XSIZE, MainFrame.CHIP_YSIZE); // protein
    fr.itemChip[6] = fr.tiles[3].getSubimage(2 * MainFrame.CHIP_XSIZE, 2 * MainFrame.CHIP_YSIZE, MainFrame.CHIP_XSIZE, MainFrame.CHIP_YSIZE); // monster
    fr.itemChip[7] = fr.tiles[3].getSubimage(4 * MainFrame.CHIP_XSIZE, 3 * MainFrame.CHIP_YSIZE, MainFrame.CHIP_XSIZE, MainFrame.CHIP_YSIZE); // elixir
    fr.itemChip[8] = fr.tiles[2].getSubimage(4 * MainFrame.CHIP_XSIZE, 0 * MainFrame.CHIP_YSIZE, MainFrame.CHIP_XSIZE, MainFrame.CHIP_YSIZE); // crystal
    fr.itemChip[9] = fr.tiles[3].getSubimage(5 * MainFrame.CHIP_XSIZE, 2 * MainFrame.CHIP_YSIZE, MainFrame.CHIP_XSIZE, MainFrame.CHIP_YSIZE); // dark portion
    fr.itemChip[10] = fr.tiles[4].getSubimage(4 * MainFrame.CHIP_XSIZE, 4 * MainFrame.CHIP_YSIZE, MainFrame.CHIP_XSIZE, MainFrame.CHIP_YSIZE); // holy staff
    fr.itemChip[11] = fr.tiles[4].getSubimage(5 * MainFrame.CHIP_XSIZE, 4 * MainFrame.CHIP_YSIZE, MainFrame.CHIP_XSIZE, MainFrame.CHIP_YSIZE); // dark staff
    fr.itemChip[12] = fr.tiles[4].getSubimage(3 * MainFrame.CHIP_XSIZE, 0 * MainFrame.CHIP_YSIZE, MainFrame.CHIP_XSIZE, MainFrame.CHIP_YSIZE); // sword
    fr.itemChip[13] = fr.tiles[4].getSubimage(3 * MainFrame.CHIP_XSIZE, 1 * MainFrame.CHIP_YSIZE, MainFrame.CHIP_XSIZE, MainFrame.CHIP_YSIZE); // lance
    fr.itemChip[14] = fr.tiles[4].getSubimage(1 * MainFrame.CHIP_XSIZE, 2 * MainFrame.CHIP_YSIZE, MainFrame.CHIP_XSIZE, MainFrame.CHIP_YSIZE); // shield sword
    new NewFloor(fr);
    bgloop();
  }
  public void bgloop() {
    th = new Thread(this);
    th.start();
  }
  
  @Override
  public void run() {
    while (true) {
      offset = (++offset) % 320;
      if (flag) {
        r += 17;
        if (r == 255) flag = false;
      } else {
        r -= 17;
        if (r == 0) flag = true;
      }

      repaint();
      try {
        Thread.sleep(15);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    for (int i = 0; i < 3; i++) for (int j = 0; j < 2; j++) {
      g.drawImage(tile, 320 * i - offset, 240 * j, this);
    }
    Font font1 = new Font("Arial", Font.BOLD, 70), font2 = new Font("Arial", Font.PLAIN, 18);
    g.setFont(font1);
    g.drawString("Slimes", 180, 130);
    g.setColor(new Color(r, 0, 0));
    g.setFont(font2);
    g.drawString("Press any key", 260, 330);
    g.setColor(new Color(0, 0, 0));
    g.drawString("素材・背景: 白螺子屋(http://hi79.web.fc2.com/)", 180, 430);
  }
  @Override
  public void keyPressed(KeyEvent e) {
    fr.state = 1;
  }
  @Override
  public void keyReleased(KeyEvent e) {}
  @Override
  public void keyTyped(KeyEvent e) {}
}