import java.awt.event.KeyListener;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class FieldPanel extends JPanel implements KeyListener, Runnable {
  private int val = 0, motion = 1;
  private int turnType = 0, circ = 0, thrCh = 0;
  private Map map;
  protected MainFrame fr;
  public Hero hero;
  private ArrayList<Enemy> enemies;
  private ArrayList<Integer> enemiesID;
  private int index = 0, id = 0;
  long time1, time2;
  private boolean fade = false, command = false, thr = false;
  Random rnd = new Random();
  Thread th = null;
  public FieldPanel(MainFrame fr, Hero hero, ArrayList<Enemy> enemies, ArrayList<Integer> enemiesID, Map map, int[] menuCmd) {
    this.hero = hero; this.enemies = enemies; this.enemiesID = enemiesID; this.map = map; this.fr = fr;
    addKeyListener(this);
    setFocusable(true);
    setOpaque(false);
    th = new Thread(this);
    th.start();
    heroTurn(menuCmd);
  }
  @Override
  public void run() {
    while (hero.hp > 0) {
      if (fr.clear) {
        map.goal = new Point(45, 15);
        fr.map = map;
      }
      repaint();
    }
    
  }
  
  @Override
  public void paintComponent(Graphics g) {
    try {
      g.setColor(Color.black);
      // マップスクロール
      // マップの先端座標(ジェネラル)
      map.edge = map.getGeneral(new Point(0, 0));
      // 画面の先端座標(ローカル)
      map.frameEdge = map.getLocal(new Point(0, 0));

      if (fr.floor < 5) g.drawImage(fr.world, (map.edge.x + 7) * MainFrame.MAPCHIP_SIZE, (map.edge.y + 5) * MainFrame.MAPCHIP_SIZE, this);
      else g.drawImage(fr.duel, (map.edge.x + 7) * MainFrame.MAPCHIP_SIZE, (map.edge.y + 5) * MainFrame.MAPCHIP_SIZE, this);

      // オブジェクトの配置
      for (int i = map.frameEdge.y; i < map.h; i++) {
        for (int j = map.frameEdge.x; j < map.w; j++) {
          if (map.object[i][j] != -1) {
            g.drawImage(fr.itemChip[map.object[i][j]], 4 + (map.edge.x + j) * MainFrame.MAPCHIP_SIZE, (map.edge.y + i) * MainFrame.MAPCHIP_SIZE, this);
          }
        }
      }
      // ゴールの配置
      if (map.isInView(map.goal)) {
        g.drawImage(fr.stair, 4 + (map.edge.x + map.goal.x) * MainFrame.MAPCHIP_SIZE, (map.edge.y + map.goal.y) * MainFrame.MAPCHIP_SIZE, this);
      }
      if (fr.turn) {
        // 主人公を敵より前面に出す(主人公の行動)
        // 敵
        for (int i = 0; i < enemies.size(); i++) {
          if (map.isInView(enemies.get(i).pos)) {
            // System.out.println("drawing: " + enemies.size());
            motion = (int)(1.1 - Math.sin(enemies.get(i).moved * Math.PI / 2));
            g.drawImage(fr.slimeChip[fr.slimeType][enemies.get(i).dir.ordinal()][motion], 4 + (map.edge.x + enemies.get(i).pos.x) * MainFrame.MAPCHIP_SIZE, (map.edge.y + enemies.get(i).pos.y) * MainFrame.MAPCHIP_SIZE, this);
          }
        }
        // 主人公
        motion = (int)(1.1 - Math.sin(hero.moved * Math.PI / 2));
        if (hero.hp > 0) {
          // アイテムの移動(主人公の下, 敵より上)
          if (thr) {
            g.drawImage(fr.itemChip[hero.hand], 4 + thrCh * hero.dir.getX() + map.center.x * MainFrame.MAPCHIP_SIZE, thrCh * hero.dir.getY() + map.center.y * MainFrame.MAPCHIP_SIZE, this);
          }
          // 回転および攻撃アニメーション
          g.drawImage(fr.heroChip[(hero.dir.ordinal() + circ) % 4][motion], hero.attackX + 4 + map.center.x * MainFrame.MAPCHIP_SIZE, hero.attackY + map.center.y * MainFrame.MAPCHIP_SIZE, this);
        }
        // ダメージ表示
        if (enemies.size() != 0 && enemies.get(index).damaged) {
          Font font = new Font("Arial", Font.PLAIN, 10);
          g.setFont(font);
          g.setColor(Color.white);
          g.drawString("-" + (turnType != 1  ? hero.damageTo(enemies.get(index)) : hero.itemDamageTo(enemies.get(index))), 10 + (map.edge.x + enemies.get(index).pos.x) * MainFrame.MAPCHIP_SIZE, 4 + (map.edge.y + enemies.get(index).pos.y) * MainFrame.MAPCHIP_SIZE);
        }

      } else {
        // 敵を主人公より前面に出す(敵の行動)
        // 主人公
        motion = (int)(1.1 - Math.sin(hero.moved * Math.PI / 2));
        if (hero.hp > 0) g.drawImage(fr.heroChip[hero.dir.ordinal()][motion], 4 + map.center.x * MainFrame.MAPCHIP_SIZE, map.center.y * MainFrame.MAPCHIP_SIZE, this);
        // 敵
        for (int i = 0; i < enemies.size(); i++) {
          if (map.isInView(enemies.get(i).pos)) {
            motion = (int)(1.1 - Math.sin(enemies.get(i).moved * Math.PI / 2));

            if (i == index) {
              g.drawImage(fr.slimeChip[fr.slimeType][enemies.get(i).dir.ordinal()][motion], enemies.get(i).attackX + 4 + (map.edge.x + enemies.get(i).pos.x) * MainFrame.MAPCHIP_SIZE, enemies.get(i).attackY + (map.edge.y + enemies.get(i).pos.y) * MainFrame.MAPCHIP_SIZE, this);
            } else {
              g.drawImage(fr.slimeChip[fr.slimeType][enemies.get(i).dir.ordinal()][motion], 4 + (map.edge.x + enemies.get(i).pos.x) * MainFrame.MAPCHIP_SIZE, (map.edge.y + enemies.get(i).pos.y) * MainFrame.MAPCHIP_SIZE, this);
            }
          }
          
        }
        // ダメージ表示
        if (hero.damaged) {
          Font font = new Font("Arial", Font.PLAIN, 10);
          g.setFont(font);
          g.setColor(Color.white);
          g.drawString("-" + enemies.get(index).damageTo(hero), 10 + (map.edge.x + hero.pos.x) * MainFrame.MAPCHIP_SIZE, 4 + (map.edge.y + hero.pos.y) * MainFrame.MAPCHIP_SIZE);
        }
      }

      Color white = new Color(200, 200, 200), alphaWhite = new Color(200, 200, 200, 150), alphaBlack = new Color(0, 0, 0, 150);
      Font font = new Font("Arial", Font.PLAIN, 15);
      g.setFont(font);
      // ログ
      if (fr.state != 2) {
        g.setColor(alphaWhite);
        g.fillRect(20, 293, 300, 137);
        g.setColor(alphaBlack);
        while (fr.log.size() > 9) fr.log.remove(0);
        for (int i = 0; i < fr.log.size(); i++) {
          g.drawString(fr.log.get(i), 30, 307 + 15 * i);
        }
      }
      
      // 上部のステータス
      g.setColor(white);
      g.drawString(fr.floor + "F", 20, 15);
      g.drawString("Lv." + hero.level, 90, 15);
      g.drawString("HP: " + hero.hp + " / " + hero.MAX_HP, 170, 15);
      if (fr.state != 2) {
        g.drawString("ESC to open Menu", 20, 35);
        if (hero.hand != -1) {
          g.drawString("V to throw(COMMAND + V to cancel)", 20, 55);
        }
      }
      
      g.fillRect(300, 2, 300, 16);
      g.setColor(Color.red);
      g.fillRect(300, 3, 300, 14);
      g.setColor(Color.green);
      g.fillRect(300, 3, (int)(300 * ((double)hero.hp / hero.MAX_HP)), 14);
      g.setColor(white);
      g.fillRect(300, 22, 300, 9);
      g.setColor(Color.red);
      g.fillRect(300, 23, 300, 7);
      g.setColor(Color.blue);
      g.fillRect(300, 23, (int)(300 * ((double)hero.fullness / hero.MAX_FULLNESS)), 7);
      // fade-out
      if (fade) {
        g.setColor(new Color(0, 0, 0, val));
        g.fillRect(0, 0, MainFrame.WIDTH, MainFrame.HEIGHT);
      }
      if (hero.hp <= 0) fr.state = 4;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  // メニューコマンドからの行動
  public void heroTurn(int[] menuCmd) {
    // cmdType = list * 100 + target * 10 + cmd;
    // list; 0:スキル, 1:アイテム, 3: 足元
    // target; ArrayListのインデックス
    // cmd; 0: 使う(拾う), 1: 置く, 2: 投げる, 3: 捨てる
    if (menuCmd != null) {
      int list = menuCmd[0], target = menuCmd[1], cmd = menuCmd[2];
      boolean action = false;
      switch (list) {
        // 武器
        case 0:
          switch (cmd) {
            // 装備
            case 0:
              fr.log.add(hero.name + " equipped " + hero.weaponList[hero.weapons.get(target)] + ".");
              hero.equip(hero.weapons.get(target));
              break;
            // 置く
            case 1:
              fr.log.add(hero.weaponList[hero.weapons.get(target)] + " was put on the ground.");
              map.object[hero.pos.y][hero.pos.x] = 10 + hero.weapons.get(target);
              hero.weapons.remove(Integer.valueOf(hero.weapons.get(target)));
            // otherwise
            default:
              break;
          }
          break;
        // アイテム
        case 1:
          switch (cmd) {
            // 使用
            case 0:
              fr.log.add(hero.name + " used " + hero.itemList[hero.items.get(target)] + ".");
              hero.useItem(hero.items.get(target), fr);
              action = true;
              break;
            // アイテムを置く
            case 1:
              fr.log.add(hero.itemList[hero.items.get(target)] + " was put on the ground.");
              map.object[hero.pos.y][hero.pos.x] = hero.items.get(target);
              hero.items.remove(Integer.valueOf(hero.items.get(target)));
              break;
            // アイテムを掴む
            case 2:
              fr.log.add(hero.itemList[hero.items.get(target)] + " was grabbed.");
              hero.grab(hero.items.get(target));
              break;
            // アイテムを捨てる
            case 3:
              hero.items.remove(Integer.valueOf(hero.items.get(target)));
              break;
            //otherwise
            default:
              break;
          }
          break;
        // 足元
        case 3:
          fr.log.add(hero.itemList[hero.items.get(target)] + " was picked up.");
          hero.pickUp(map.object[hero.pos.y][hero.pos.x], fr);
          map.object[hero.pos.y][hero.pos.x] = -1;
          break;
        // otherwise
        default:
          break;
      }
      fr.menuCmd = null;
      if (action) enemyTurn();
    }
    
  }
  // アイテムを投げる
  public void heroThrow() {
    turnType = 1;
    try {
      for (int i = 0; i < 4; i++) {
        circ++;
        paintImmediately(0, 0, MainFrame.WIDTH, MainFrame.HEIGHT);
        Thread.sleep(1);
      }
      thr = true;
      int j = 0; char stat; boolean strike = false;
      while (true) {
        if (map.chara[hero.pos.y + hero.dir.getY() * j][hero.pos.x + hero.dir.getX() * j] >= 1) {
          strike = true; break;
        }
        stat = map.status[hero.pos.y + hero.dir.getY() * (j + 1)][hero.pos.x + hero.dir.getX() * (j + 1)];
        if (stat != 'B') {
          thrCh += 16; j = thrCh / MainFrame.MAPCHIP_SIZE;
          paintImmediately(0, 0, MainFrame.WIDTH, MainFrame.HEIGHT);
          Thread.sleep(1);
        } else break;
      }
      if (strike) {
        id = map.chara[hero.pos.y + hero.dir.getY() * j][hero.pos.x + hero.dir.getX() * j];
        for (int i = 0; i < enemies.size(); i++) {
          if (enemies.get(i).id == id) {
            index = i; break;
          }
        }
        fr.log.add(hero.name + " did " + hero.itemDamageTo(enemies.get(index)) + " damage to " + enemies.get(index).name + ".");
        enemies.get(index).damaged = true;
        paintImmediately(0, 0, MainFrame.WIDTH, MainFrame.HEIGHT);
        Thread.sleep(100);
        enemies.get(index).damaged = false;
        if (enemies.get(index).hp < 0) {
          int prev = hero.level;
          fr.log.add(enemies.get(index).name + " collapsed.");
          map.chara[enemies.get(index).pos.y][enemies.get(index).pos.x] = -1;
          if (Math.random() < 0.05) map.object[enemies.get(index).pos.y][enemies.get(index).pos.x] = 8;
          hero.gainExp(enemies.get(index).level * 2);
          fr.log.add(hero.name + " gained " + (enemies.get(index).MAX_HP * 2) + " exp.");
          if (hero.level > prev) fr.log.add(hero.name + " reached level " + hero.level + "!");
          if (enemies.remove(index).name == "SlimeKing") fr.clear = true;
        }
        index = 0; id = 0;
      } else {
        // 落下先にすでに別のアイテムがある場合，消滅
        if (map.object[hero.pos.y + hero.dir.getY() * j][hero.pos.x + hero.dir.getX() * j] == -1) {
          map.object[hero.pos.y + hero.dir.getY() * j][hero.pos.x + hero.dir.getX() * j] = hero.hand;
        }
      }
      repaint();
    } catch (Exception e) {
      e.printStackTrace();
    }
    thr = false;
    turnType = 0;
    hero.hand = -1;
  }
  // 主人公の攻撃
  public void heroAttack() {
    try {
      fr.log.add(hero.name + " attacked!");
      for (int i = 0; i < hero.attack.length; i++) {
        hero.attackX += hero.attack[i] * hero.dir.getX();
        hero.attackY += hero.attack[i] * hero.dir.getY();
        paintImmediately(0, 0, MainFrame.WIDTH, MainFrame.HEIGHT);
        Thread.sleep(1);
      }
      
      id = map.chara[hero.pos.y + hero.dir.getY()][hero.pos.x + hero.dir.getX()];
      if (id != -1) {
        for (int i = 0; i < enemies.size(); i++) {
          if (enemies.get(i).id == id) {
            index = i; break;
          }
        }
        // System.out.println("attacked to: " + index + ", " + enemies.get(index));
        int overkill = hero.attack(enemies.get(index));
        fr.log.add(hero.name + " did " + hero.damageTo(enemies.get(index)) + " damage to " + enemies.get(index).name + ".");
        enemies.get(index).damaged = true;
        paintImmediately(0, 0, MainFrame.WIDTH, MainFrame.HEIGHT);
        Thread.sleep(100);
        enemies.get(index).damaged = false;
        if (hero.equipment == 0) fr.log.add(hero.name + "' HP was restored by " + hero.restore(30) + "."); 
        if (hero.equipment == 1) fr.log.add(hero.name + " was replenished by " + hero.replenish(20) + ".");
        if (enemies.get(index).hp < 0) {
          int prev = hero.level;
          fr.log.add(enemies.get(index).name + " collapsed.");
          map.chara[enemies.get(index).pos.y][enemies.get(index).pos.x] = -1;
          if (Math.random() < 0.05) map.object[enemies.get(index).pos.y][enemies.get(index).pos.x] = 8;
          hero.gainExp(enemies.get(index).level * 2 + overkill);
          fr.log.add(hero.name + " gained " + (enemies.get(index).level * 2 + overkill) + " exp.");
          if (hero.level > prev) fr.log.add(hero.name + " reached level " + hero.level + "!");
          if (enemies.remove(index).name == "SlimeKing") fr.clear = true;
        }
      }
      index = 0; id = 0;
      repaint();
    } catch (Exception e) {
      e.printStackTrace();
    }
    hero.equipment = -1;
    hero.atkRatio = 1;
    hero.defRatio = 1;
  }
  // 敵の行動
  public void enemyTurn() {
    // 敵移動 or 攻撃
    for (int i = 0; i < enemies.size(); i++) {
      if (Math.abs(hero.pos.x - enemies.get(i).pos.x) + Math.abs(hero.pos.y - enemies.get(i).pos.y) == 1) {
        try {
          // 敵の攻撃
          fr.log.add(enemies.get(i).name + " attacked!");
          enemies.get(i).dir = Dir.direction(enemies.get(i).pos, hero.pos);
          index = i;
          for (int j = 0; j < enemies.get(i).attack.length; j++) {
            enemies.get(i).attackX += enemies.get(i).attack[j] * enemies.get(i).dir.getX();
            enemies.get(i).attackY += enemies.get(i).attack[j] * enemies.get(i).dir.getY();
            paintImmediately(0, 0, MainFrame.WIDTH, MainFrame.HEIGHT);
            Thread.sleep(1);
          }
          
          enemies.get(i).dir = Dir.direction(enemies.get(i).pos, hero.pos);
          enemies.get(i).attack(hero);
          fr.log.add(enemies.get(i).name + " did " + enemies.get(i).damageTo(hero) + " damage to " + hero.name + ".");
          hero.damaged = true;
          paintImmediately(0, 0, MainFrame.WIDTH, MainFrame.HEIGHT);
          Thread.sleep(100);
          hero.damaged = false;
          index = 0; id = 0;
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else if (!enemies.get(i).attacked) {
        // 敵の移動
        map.search(this.hero, enemies.get(i));
        enemies.get(i).move(map.towardHero(enemies.get(i)), fr, map);
      }
    }
    // 敵の出現
    if (fr.floor < 5 && enemies.size() < 6 && Math.random() < 0.1) {
      int p = rnd.nextInt(19), cnt = 0;
      Point tmp = new Point();
      for (int i = 0; i < map.h; i++) {
        for (int j = 0; j < map.w; j++) {
          if (map.status[i][j] == 'E') {
            if (cnt == p) {
              tmp = new Point(j, i);
              map.chara[i][j] = fr.enemyID;
            }
            cnt++;
          }
        }
      }
      enemies.add(new Enemy(fr.enemyID, "Slime" + fr.enemyID, (int)(fr.floor * 1.7), tmp));
      enemiesID.add(fr.enemyID);
      map.enemies = enemies;
      map.enemiesID = enemiesID;
      fr.enemyID++;
    }
    repaint();
    fr.turn = true;
  }
  @Override
  public void keyPressed(KeyEvent e) {
    // キー操作
    // vk_up or vk_w: 'up', vk_down or vk_s: 'down', vk_right or vk_d: 'right', vk_left or vk_a: 'left'
    // vk_esc: 'toggle Menu'(we can go back to Title in Menu), vk_space or vk_z: 'done', vk_backspace or vk_x: 'back'
    // vk_shift or vk_c: sub key
    // Notice: we cannot go outside of map or go through the Rock. we should be careful.
    // encounter in 10%, translate to Battle.
    int key = e.getKeyCode();
    Dir dir = Dir.STAY;
    turnType = 0;
    if (fr.turn) {
      switch (key) {
        // up
        case KeyEvent.VK_UP:
        case KeyEvent.VK_W:
          if (!command) {
            dir = Dir.UP;
          }
          hero.dir = Dir.UP;
          break;
        // right
        case KeyEvent.VK_RIGHT:
        case KeyEvent.VK_D:
          if (!command) {
            dir = Dir.RIGHT;
          }
          hero.dir = Dir.RIGHT;
          break;
        // down
        case KeyEvent.VK_DOWN:
        case KeyEvent.VK_S:
          if (!command) {
            dir = Dir.DOWN;
          }
          hero.dir = Dir.DOWN;
          break;
          // left
        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_A:
          if (!command) {
            dir = Dir.LEFT;
          }
          hero.dir = Dir.LEFT;
          break;
        // ESC
        case KeyEvent.VK_ESCAPE:
          // go menu
          fr.hero = hero; fr.enemies = enemies; fr.map = map; fr.enemiesID = enemiesID;
          fr.state = 2;
          break;
        // done
        case KeyEvent.VK_Z:
          if (hero.pos.equals(map.goal)) {
            if (fr.floor < 5) {
              fr.hero = hero; fr.floor++;
              fr.state = 3;
            } else {
              fr.hero = hero;
              fr.state = 4;
            }
          }
          break;
        // attack
        case KeyEvent.VK_SPACE:
          if (!hero.attacked) {
            hero.attacked = true;
            heroAttack();
            fr.turn = false;
          }
          break;
        // (足踏み)back
        case KeyEvent.VK_X:
          hero.moved++;
          fr.turn = false;
          break;
        // 投げる
        case KeyEvent.VK_V:
          if (!command) {
            if (hero.hand != -1) {
              heroThrow();
              hero.hunger(1);
              fr.turn = false;
            }
          } else {
            hero.hand = -1;
          }
          break;
        // command key
        case KeyEvent.VK_SHIFT:
        case KeyEvent.VK_C:
          command = true;
          break;
        // otherwise
        default:
          break;
      }
    }
    // 主人公移動
    if (dir != Dir.STAY) {
      hero.move(dir, fr, map);
    }
    if (hero.moved % 10 == 0) {
      hero.restore(1);
      hero.replenish(-1);
    }
    if (hero.fullness <= 0) hero.hp -= 1;
    if (!fr.turn) enemyTurn();
    if (!fade) repaint();
  }
  @Override
  public void keyReleased(KeyEvent e) {
    int key = e.getKeyCode();
    switch (key) {
      // motion key
      case KeyEvent.VK_D:
      case KeyEvent.VK_W:
      case KeyEvent.VK_S:
      case KeyEvent.VK_A:
      case KeyEvent.VK_UP:
      case KeyEvent.VK_RIGHT:
      case KeyEvent.VK_DOWN:
      case KeyEvent.VK_LEFT:
      case KeyEvent.VK_BACK_SPACE:
      case KeyEvent.VK_X:
        break;
      // attack
      case KeyEvent.VK_SPACE:
        if (hero.attacked) hero.attacked = false;
        break;
      // done
      case KeyEvent.VK_Z:
        break;
      // command key
      case KeyEvent.VK_SHIFT:
      case KeyEvent.VK_C:
        command = false;
        break;
      // otherwise
      default:
        break;
    }
  }
  @Override
  public void keyTyped(KeyEvent e) {}
}