import java.awt.*;

public class Enemy extends Character implements Runnable {
  // Thread th = null;
  // Hero hero = null;
  // notice; Default(Future)
  // hp: 30(1000), mp: 20(100), ATK: 25(300), DEF: 15(100)
  // if hp is 0, Enemy dies.
  public Enemy(int id, String name, int level, Point pos) {
    super(id, name, level, 5 << level, 0, 7 << level, 7 << level, pos);
  }
  @Override
  public int attack(Character ch) {
    return super.attack(ch);
  }
  @Override
  public void run() {
    // search();
  }
  @Override
  public void move(Dir dir, MainFrame fr, Map map) {
    this.dir = dir;
    switch (dir) {
      case LEFT:
      case RIGHT:
        if (map.status[pos.y][pos.x + dir.getX()] != 'B' && map.chara[pos.y][pos.x + dir.getX()] == -1) {
          map.chara[pos.y][pos.x] = -1; map.chara[pos.y][pos.x + dir.getX()] = id;
          pos.x += dir.getX();
          moved++;
        }
        break;
      case DOWN:
      case UP:
        if (map.status[pos.y + dir.getY()][pos.x] != 'B' && map.chara[pos.y + dir.getY()][pos.x] == -1) {
          map.chara[pos.y][pos.x] = -1; map.chara[pos.y + dir.getY()][pos.x] = id;
          pos.y += dir.getY();
          moved++;
        }
        break;
      // otherwise
      default:
        break;
    }
  }
  @Override
  public boolean equals(Object obj) {
    Enemy e = (Enemy)obj;
    if (this.id == e.id) {
      return true;
    } else {
      return false;
    }
  }
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Integer.valueOf(id).hashCode();
    return result;
  }
}

