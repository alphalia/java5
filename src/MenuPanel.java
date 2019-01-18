import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class MenuPanel extends FieldPanel implements KeyListener {
  private int list = 0, target = 0, cmd = 0, phaze = 0, menuSize = 0, columnSize = 0, page = 0;
  Hero hero;
  Map map;
  private String[] menuList = {"Weapon", "Backpack", "Strength", "Feet", "Guide", "Quit"};
  public MenuPanel(MainFrame fr, Hero hero, ArrayList<Enemy> enemies, ArrayList<Integer> enemiesID, Map map) {
    super(fr, hero, enemies, enemiesID, map, null);
    this.hero = hero; this.map = map;
  }
  @Override
  public void paintComponent(Graphics g) {
    Color blue = new Color(0, 0, 255, 100), white = new Color(255, 255, 255, 200);
    Font font = new Font("Arial", Font.PLAIN, 20);
    super.paintComponent(g);
    g.setFont(font);
    if (phaze == 0) {
      menuSize = menuList.length; columnSize = 1;
      // メニューリスト
      // 枠
      g.setColor(blue);
      g.fillRect(70, 100, 190, 200);
      g.fillRect(290, 100, 250, 45);
      // 項目
      g.setColor(white);
      g.drawString("→", 80, 130 + list * 30);
      g.drawString("Forest of Slime  " + fr.floor + "F", 340, 130);
      for (int i = 0; i < menuSize; i++) {
        g.drawString(menuList[i], 110, 130 + i * 30);
      }
    } else if (phaze > 0) {
      // メニュー
      // 枠
      g.setColor(blue);
      g.fillRect(70, 100, 350, 290);
      // g.fillRect(70, 390, 350, 50);
      // メニュー名
      g.setColor(white);
      g.drawString(menuList[list], 110, 122);
      g.drawLine(75, 127, 415, 127);
      switch (list) {
        // 武器
        case 0:
          g.setColor(blue);
          g.fillRect(70, 400, 350, 40);
          g.setColor(white);
          menuSize = hero.weapons.size();
          if (menuSize != 0) {
            columnSize = ((menuSize - 1) / 8) + 1;
            g.drawString("(" + (page + 1) + "/" + columnSize + ")", 250, 122);
            g.drawString("→", 80, 155 + target * 30);
            g.drawString(hero.introduction[10 + hero.weapons.get(page * 8 + target)], 110, 430);
            for (int i = page * 8; i < (page < menuSize / 8 ? (page + 1) * 8 : page * 8 + menuSize % 8); i++) {
              g.drawString(hero.weaponList[hero.weapons.get(i)], 110, 155 + (i % 8) * 30);
            }
            // 武器
            if (phaze == 2) {
              menuSize = 2; columnSize = 0;
              // メニュー
              // 枠
              g.setColor(blue);
              g.fillRect(450, 100, 100, 60);
              // 項目
              g.setColor(white);
              g.drawString("→", 460, 122 + cmd * 30);
              g.drawString("Equip", 490, 122);
              g.drawString("Drop", 490, 152);
            }
          } else {
            g.drawString("(1/1)", 250, 122);
          }
          break;
        // アイテム
        case 1:
          g.setColor(blue);
          g.fillRect(70, 400, 350, 40);
          g.setColor(white);
          menuSize = hero.items.size();
          if (menuSize != 0) {
            columnSize = ((menuSize - 1) / 8) + 1;
            g.drawString("(" + (page + 1) + "/" + columnSize + ")", 250, 122);
            g.drawString("→", 80, 155 + target * 30);
            g.drawString(hero.introduction[hero.items.get(page * 8 + target)], 110, 430);
            for (int i = page * 8; i < (page < menuSize / 8 ? (page + 1) * 8 : page * 8 + menuSize % 8); i++) {
              g.drawString(hero.itemList[hero.items.get(i)], 110, 155 + (i % 8) * 30);
            }
            if (phaze == 2) {
              menuSize = 3; columnSize = 1;
              // メニュー
              // 枠
              g.setColor(blue);
              g.fillRect(450, 100, 140, 90);
              // 項目
              g.setColor(white);
              g.drawString("→", 460, 122 + cmd * 30);
              g.drawString("Use", 490, 122);
              g.drawString("Drop", 490, 152);
              g.drawString("Grab", 490, 182);
            }
          } else {
            g.drawString("(1/1)", 250, 122);
          }
          
          break;
        // ステータス
        case 2:
          g.setColor(blue);
          g.fillRect(70, 390, 350, 50);
          g.fillRect(420, 100, 170, 340);
          g.setColor(white);
          g.drawString("Name: " + hero.name, 110, 155);
          g.drawString("Level: " + hero.level, 110, 185);
          g.drawString("EXP: " + hero.exp, 110, 215);
          g.drawString("To the next level: " + (hero.levelCost - hero.exp), 110, 245);
          g.drawString("HP:  " + hero.hp + " / " + hero.MAX_HP, 110, 275);
          g.drawString("Fullness:  " + hero.fullness + " / " + hero.MAX_FULLNESS, 110, 305);
          g.drawString("ATK: " + hero.atk, 110, 335);
          g.drawString("DEF: " + hero.def, 110, 365);
          g.drawString("Hand: " + (hero.hand != -1 ? hero.itemList[hero.hand] : "Nothing's grabbed"), 110, 395);
          g.drawString("Equipment: " + (hero.equipment != -1 ? hero.weaponList[hero.equipment] : "Nothing's equipped"), 110, 425);
          break;
        // 足元
        case 3:
          menuSize = map.object[hero.pos.y][hero.pos.x] != -1 ? 1 : 0; columnSize = 1;
          g.drawString("(" + (page + 1) + "/" + columnSize + ")", 250, 122);
          if (menuSize != 0) {
            g.drawString("→", 80, 155 + target * 30);
            g.drawString(map.objList[map.object[hero.pos.y][hero.pos.x]], 110, 155);
            if (phaze == 2) {
              menuSize = 1; columnSize = 1;
              // メニュー
              // 枠
              g.setColor(blue);
              g.fillRect(450, 100, 90, 30);
              g.fillRect(70, 380, 350, 10);
              // 項目
              g.setColor(white);
              g.drawString("→", 460, 122);
              g.drawString("Pick up", 490, 122);
            }
          }
          break;
        // マニュアル
        case 4:
          g.setColor(blue);
          g.fillRect(70, 390, 350, 50);
          g.fillRect(420, 100, 170, 340);
          g.setColor(white);
          g.drawString("Move: DIR key (CROSS key, WASD key)", 110, 155);
          g.drawString("Attack: SPACE key", 110, 185);
          g.drawString("Done: Z key", 110, 215);
          g.drawString("Return(Step): X key", 110, 245);
          g.drawString("Menu: ESC KEY", 110, 275);
          g.drawString("Turn: COMMAND (C, SHIFT) + DIR key", 110, 305);
          g.drawString("Throw: V key; COMMAND + V key to cancel", 110, 335);
          g.drawString("To the next floor: Z key on the stair icon", 110, 365);
          g.drawString("[ Notice ]: Weapons are disposable.", 110, 395);
          g.drawString("[ Notice ]: There exists 'boss' in 5F.", 110, 425);
          break;

        default:
          break;
      }
    }
  }
  @Override
  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    Dir dir = Dir.STAY;
    switch (key) {
      // down
      case KeyEvent.VK_DOWN:
      case KeyEvent.VK_S:
        dir = Dir.DOWN;
        break;
      // up
      case KeyEvent.VK_UP:
      case KeyEvent.VK_W:
        dir = Dir.UP;
        break;
        // right
      case KeyEvent.VK_RIGHT:
      case KeyEvent.VK_D:
        dir = Dir.RIGHT;
        break;
        // left
      case KeyEvent.VK_LEFT:
      case KeyEvent.VK_A:
        dir = Dir.LEFT;
        break;
      // ESC
      case KeyEvent.VK_ESCAPE:
        // go back Field
        fr.state = 1;
        break;
      // done
      case KeyEvent.VK_Z:
        if (list == 2 || list == 4) {
          if (phaze < 1) phaze++;
        } else if (list == 5) {
          fr.state = 5;
        } else {
          if (phaze == 2) {
            fr.menuCmd = new int[3];
            fr.menuCmd[0] = list;
            fr.menuCmd[1] = page * 8 + target;
            fr.menuCmd[2] = cmd;
            fr.state = 1;
          }
          if (phaze < 2) phaze++;
          if (menuSize == 0) phaze--;
          
        }
        break;
      // back
      case KeyEvent.VK_X:
        if (phaze == 2) {
          cmd = 0; phaze--;
        } else if (phaze == 1) {
          target = 0; page = 0; phaze--;
        }
        break;
      // command key
      case KeyEvent.VK_SHIFT:
      case KeyEvent.VK_C:
        break;
      // otherwise
      default:
        break;
    }
    
    if (dir != Dir.STAY) cursor(dir);
    repaint();
  }
  @Override
  public void keyReleased(KeyEvent e) {}
  @Override
  public void keyTyped(KeyEvent e) {}
  // カーソル移動
  public void cursor(Dir dir) {
    switch (dir) {
      case DOWN:
      case UP:
        if (phaze == 0) {
          if (list + dir.getY() >= 0 && list + dir.getY() <= menuSize - 1) {
            list += dir.getY();
          }
        } else if (phaze == 1) {
          if (target + dir.getY() >= 0 && target + dir.getY() <= (page < menuSize / 8 ? 7 : menuSize % 8 - 1) ) {
            target += dir.getY();
          }
        } else if (phaze == 2) {
          if (cmd + dir.getY() >= 0 && cmd + dir.getY() <= menuSize - 1) {
            cmd += dir.getY();
          }
        }
        break;
      case RIGHT:
      case LEFT:
        if (phaze == 1) {
          if (page + dir.getX() >= 0 && page + dir.getX() <= columnSize - 1) {
            page += dir.getX(); target = 0;
          }
        }
        break;
      // otherwise
      default:
        break;
    }
  }
}