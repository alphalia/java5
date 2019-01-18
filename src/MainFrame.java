import java.awt.event.KeyListener;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class MainFrame extends JFrame implements Runnable {
  public boolean in_progress = true, turn = true; // heroのターンならtrue;
  public int state = 0;
  public int floor = 1;
  public int old_state = 0;
  public int[] menuCmd = null;
  public boolean clear = false;
  public BufferedReader br;
  public Container cp = this.getContentPane();
  public Thread th = null;
  public TitlePanel tpn;
  public FieldPanel fpn;
  public MenuPanel mpn;
  public ResultPanel rpn;
  public NewFloor nf;
  public int slimeType = 0;
  public Hero hero = new Hero(0, "Michel", new Point());
  public ArrayList<Enemy> enemies = new ArrayList<Enemy>();
  public ArrayList<Integer> enemiesID = new ArrayList<Integer>();
  public int enemyID = 1;
  public ArrayList<String> log = new ArrayList<String>();
  public Map map;
  public BufferedImage tiles[] = new BufferedImage[5], stair, world, duel,
    heroChip[][] = new BufferedImage[4][3], slimeChip[][][] = new BufferedImage[2][4][3], itemChip[] = new BufferedImage[15];
  public static final int WIDTH = 608, HEIGHT = 448;  // 19 * 14マス
  public static final int MAPCHIP_SIZE = 32, CHIP_XSIZE = 24, CHIP_YSIZE = 32;
  public MainFrame() {
    super("Slimes");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // CLOSEでプログラム終了
    setResizable(false);
    cp.setBackground(Color.black);
    cp.setPreferredSize(new Dimension(WIDTH, HEIGHT)); // コンテントペインの希望サイズ
    add(new TitlePanel(this));
  }
  public void change(int prev, int next) {
    cp.removeAll();
    switch (next) {
      case 0:
        tpn = new TitlePanel(this);
        cp.add(tpn);
        tpn.requestFocus();
        break;
      case 1:
        fpn = new FieldPanel(this, hero, enemies, enemiesID, map, menuCmd);
        cp.add(fpn);
        fpn.requestFocus();
        break;
      case 2:
        mpn = new MenuPanel(this,  hero, enemies, enemiesID, map);
        cp.add(mpn);
        mpn.requestFocus();
        break;
      case 4:
        rpn = new ResultPanel(this, hero, enemies, enemiesID, map);
        cp.add(rpn);
        rpn.requestFocus();
        break;
      default:
        break;
    }
    validate();
    repaint();
    // System.out.println("change " + prev + " to " + next);
  }
  @Override
  public void run() {
    while (in_progress) {
      try {
        Thread.sleep(20);
      } catch (Exception e) {}

      if (state == 5) {
        in_progress = false;
      } else if (state == 3) {
        new NewFloor(this);
        change(1, 1);
        state = 1;
      } else if (old_state != state) {
        change(old_state, state);
        old_state = state;
      }
    }

    System.exit(0);
  }
  public synchronized void gameStart() {
    th = new Thread(this);
    th.start();
  }
}