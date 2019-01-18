import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class NewFloor {
  private Random rnd = new Random();
  public NewFloor(MainFrame fr) {
    fr.map = new Map(94, 90);
    try {
      // マップの読み込み
      if (fr.floor < 5) fr.br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("map/map1.txt"))));
      else fr.br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("map/map2.txt"))));
      int k = 0; String str;
      while ((str = fr.br.readLine()) != null) {
        fr.map.status[k] = str.toCharArray();
        k++;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    // SとGをランダムに決定
    if (fr.floor < 5) {
      int s = rnd.nextInt(4), g = rnd.nextInt(3), scnt = 0, gcnt = 0;
      for (int i = 0; i < fr.map.h; i++) {
        for (int j = 0; j < fr.map.w; j++) {
          if (fr.map.status[i][j] == 'S') {
            if (scnt == s) {
              fr.map.start = new Point(j, i);
            }
            scnt++;
          }
          if (fr.map.status[i][j] == 'G') {
            if (gcnt == g) {
              fr.map.goal = new Point(j, i);
            }
            gcnt++;
          }
        }
      }
      // オブジェクトの配置
      for (int i = 0; i < fr.map.h; i++) {
        for (int j = 0; j < fr.map.w; j++) {
          if (fr.map.status[i][j] == 'F') {
            if (Math.random() < 0.004) {
              fr.map.object[i][j] = rnd.nextInt(6);
              if (Math.random() < 0.05) fr.map.object[i][j] = 6 + rnd.nextInt(4);
            }
            if (Math.random() < 0.002) {
              fr.map.object[i][j] = 10 + rnd.nextInt(4);
              if (Math.random() < 0.05) fr.map.object[i][j] = 14;
            }
          }
        }
      }
      fr.enemies = new ArrayList<Enemy>();
      fr.enemiesID = new ArrayList<Integer>();
      fr.enemyID = 1;
      fr.hero.dir = Dir.DOWN;
    } else {
      fr.map.start = new Point(45, 22);
      fr.map.goal = new Point(0, 0);
      fr.enemies = new ArrayList<Enemy>();
      fr.enemiesID = new ArrayList<Integer>();
      Enemy ene = new Enemy(1, "SlimeKing", 7, new Point(45, 15));
      ene.atkRatio = 1.5; ene.defRatio = 1.5;
      fr.enemies.add(ene);
      fr.enemiesID.add(1);
      fr.map.enemies = fr.enemies;
      fr.map.enemiesID = fr.enemiesID;
      fr.hero.dir = Dir.UP;
      fr.slimeType = 1;
    }
    fr.hero.pos = new Point(fr.map.start);
    fr.map.hero = fr.hero;
    fr.map.generalStart = new Point(9, 7);
    fr.map.chara[fr.map.start.y][fr.map.start.x] = 0;
  }
}