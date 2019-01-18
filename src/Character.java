import java.awt.*;
/**
 * キャラクターの基本ステータス
 */
public class Character {
  public int id;
  public String name;
  public int level;
  public int MAX_HP;
  public int hp;
  public int MAX_FULLNESS;
  public int fullness;
  public int atk;
  public int def;
  public double atkRatio = 1., defRatio = 1.;
  public Point pos;
  public Dir dir = Dir.DOWN;
  public int moved = 0;
  public int attackX = 0, attackY = 0, attack[] = {8, 8, 1, -1, -8, -8};
  public boolean attacked = false, skilled = false, damaged = false;
  public Character(int id, String name, int level, int MAX_HP, int MAX_FULLNESS, int atk, int def, Point pos) {
    this.id = id; this.name = name; this.level = level; this.MAX_HP = MAX_HP; this.MAX_FULLNESS = MAX_FULLNESS;
    this.hp = MAX_HP; this.fullness = MAX_FULLNESS; this.atk = atk; this.def = def; this.pos = pos;
  }
  /**
   * 攻撃
   * @param ch 攻撃対象キャラ
   */
  public int attack(Character ch) {
    int val = 0;
    if (ch.hp - this.damageTo(ch) <= 0) val = this.damageTo(ch) - ch.hp;
    ch.hp -= this.damageTo(ch);
    return val;
  }
  // 移動
  public void move(Dir dir, MainFrame fr, Map map) {}
  /**
   * ダメージ計算
   * @param ch 攻撃対象キャラ
   * @return このキャラが攻撃対象キャラに与えるダメージ量
   */
  public int damageTo(Character ch) {
    int damage = (int)(this.atk * this.atkRatio - ch.def * ch.defRatio);
    return damage > 0 ? damage : 1;
  }
  @Override
  public String toString() {
    String chInfo = "ID: " + id + ", Name: " + name + ", Lv." + level + ", HP: " + hp + "/" + MAX_HP + ", FULLNESS: " + fullness + "/" + MAX_FULLNESS,
      status = "ATK: " + atk + ", DEF: " + def;
    return chInfo + ", " + status;
  }
  @Override
  public boolean equals(Object obj) {
    Character ch = (Character)obj;
    if (this.id == ch.id) {
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
