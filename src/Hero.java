import java.util.*;
import java.awt.*;

// notice; Default(Future)
// level: 1(10), hp: 100(1000), mp: 30(300), ATK: 20(200), DEF: 20(200), AGI: 15(150)
// items: leaf(hp+20) * 3, experience: 0(1000)
// if hp is 0, Hero dies.
/**
 * 主人公
 */
public class Hero extends Character {
  /** 累計経験値量 */
  public int exp = 0;
  /** 次のレベルに到達するのに必要な経験値量 */
  public int levelCost = 10;
  public int next = 10;
  /**
   * アイテム(MAX10コ)<br>
   * 0: portion(hp+20), 1: ether(mp+5), 2: leaf(cure poison)
   */
  public ArrayList<Integer> items = new ArrayList<Integer>(), weapons = new ArrayList<Integer>();
  /**
   * itemList
   * 0: hp+50, 1: fullness+30, 2: hp+150, 3: fullness + 100, 4: atk+10, 5: def+10, 6: atk+20 & def+20, 7: maxHP+50, 8: hp+full, 9: level up
   * weaponList(追加効果)
   * 0: hp+30, 1: fullness+20, 2: atk*1.5, 3: tmp*1.5, 4: atk*2, def*2
   */
  public String[] 
    itemList = {"Portion", "Green Apple", "Ether", "Juicy Apple", "Taurine", "Protein", "Monster" , "Elixir", "Slime Crystal", "Dark Portion"},
    weaponList = {"Holy Staff", "Dark Staff", "Sword", "Lance", "Shield Sword"}, 
    introduction = {"HP+50", "Fullness+30", "HP+150", "Fullness+100", "ATK+30", "DEF+30", "ATK+50, DEF+50", 
      "MAXHP+100 & full restoration", "full restoration & replenishment", "reaching next level", "Additional: HP+30", "Additional: Fullness+20", 
      "Additional: ATK*1.5", "Additional: DEF*1.5", "Additional: ATK*2.0 & DEF*2.0"};
  public int hand = -1, equipment = -1;
  public Hero(int id, String name, Point pos) {
    super(id, name, 1, 25, 100, 20, 10, pos);
  }
  
  @Override
  public int attack(Character ch) {
    hunger(2);
    return super.attack(ch);
  }
  @Override
  public void move(Dir dir, MainFrame fr, Map map) {
    switch (dir) {
      case LEFT:
      case RIGHT:
        if (map.status[pos.y][pos.x + dir.getX()] != 'B' && map.chara[pos.y][pos.x + dir.getX()] == -1) {
          map.chara[pos.y][pos.x] = -1; map.chara[pos.y][pos.x + dir.getX()] = 0;
          if (map.object[pos.y][pos.x + dir.getX()] != -1) {
            pickUp(map.object[pos.y][pos.x + dir.getX()], fr);
            map.object[pos.y][pos.x + dir.getX()] = -1;
          }
          pos.x += dir.getX();
          map.generalStart.x -= dir.getX();
          moved++;
          fr.turn = false;
        }
        break;
      case DOWN:
      case UP:
        if (map.status[pos.y + dir.getY()][pos.x] != 'B' && map.chara[pos.y + dir.getY()][pos.x] == -1) {
          map.chara[pos.y][pos.x] = -1; map.chara[pos.y + dir.getY()][pos.x] = 0;
          if (map.object[pos.y + dir.getY()][pos.x] != -1) {
            pickUp(map.object[pos.y + dir.getY()][pos.x], fr);
            map.object[pos.y + dir.getY()][pos.x] = -1;
          }
          pos.y += dir.getY();
          map.generalStart.y -= dir.getY();
          moved++;
          fr.turn = false;
        }
        break;
      // otherwise
      default:
        break;
    }
  }
  public void useItem(int id, MainFrame fr) {
    switch (id) {
      // portion
      case 0:
        fr.log.add(name + "'s HP restored by " + restore(50) + ".");
        replenish(5);
        break;
      // apple
      case 1:
        fr.log.add(name + " replenished fullness by " + replenish(30) + ".");
        break;
      // ether
      case 2:
        fr.log.add(name + "'s HP restored by " + restore(150) + ".");
        replenish(5);
        break;
      // juicy apple
      case 3:
        fr.log.add(name + " replenished fullness by " + replenish(100) + ".");
        break;
      // taurine
      case 4:
        fr.log.add(name + "'s ATK increased by 30.");
        this.atk += 30;
        replenish(5);
        break;
      // protein
      case 5:
        fr.log.add(name + "'s DEF increased by 30.");
        this.def += 30;
        replenish(5);
        break;
      // monster
      case 6:
        fr.log.add(name + "'s ATK increased by 50.");
        fr.log.add(name + "'s DEF increased by 50.");
        this.atk += 50;
        this.def += 50;
        replenish(5);
        break;
      // elixir
      case 7:
        fr.log.add(name + "'s max HP increased by 100.");
        fr.log.add(name + " was fully restored.");
        this.MAX_HP += 100;
        this.hp = this.MAX_HP;
        replenish(5);
        break;
      // crystal
      case 8:
        this.hp = this.MAX_HP;
        this.fullness = this.MAX_FULLNESS;
        fr.log.add(name + " was fully restored and replenished.");
        break;
      // dark portion
      case 9:
        gainExp(this.levelCost - this.exp);
        fr.log.add(name + " reached level " + level + "!");
        replenish(5);
        break;
      // otherwise
      default:
        break;
    }
    items.remove(Integer.valueOf(id));
  }
  
  public int itemDamageTo(Character ch) {
    if (this.hand >= 6 || this.hand <= 9) {
      damageTo(100, ch); return 100;
    } else {
      damageTo(30, ch); return 30;
    }
    
  }
  
  public void damageTo(int damage, Character ch) {
    ch.hp -= damage;
  }
  public void grab(int id) {
    hand = id;
    items.remove(Integer.valueOf(id));
  }
  public void equip(int id) {
    equipment = id;
    switch (id) {
      // holy staff
      case 0:
        this.atkRatio = 1.2;
        break;
      // dark staff
      case 1:
        this.defRatio = 1.2;
        break;
      // sword
      case 2:
        this.atkRatio = 1.5;
        break;
      // lance
      case 3:
        this.defRatio = 1.5;
        break;
      // shield sword
      case 4:
        this.atkRatio = 2.;
        this.defRatio = 2.;
        break;
    
      default:
        break;
    }
    weapons.remove(Integer.valueOf(id));
  }
  
  public void gainExp(int exp) {
    this.exp += exp;
    while (this.exp >= this.levelCost) levelUp();
  }
  public void levelUp() {
    level++;
    next = (int)(1.3 * next);
    levelCost += next;
    // 各ステータス上昇
    MAX_HP += 50; MAX_FULLNESS += 20; hp = MAX_HP;
    atk += 30; def += 30;
  }
  // hp+
  public int restore(int val) {
    if (this.hp + val <= this.MAX_HP) {
      this.hp += val;
      return val;
    } else {
      int tmp = this.MAX_HP - this.hp;
      this.hp = this.MAX_HP;
      return tmp;
    }
  }
  // fullness+
  public int replenish(int val) {
    if (this.fullness + val <= this.MAX_FULLNESS) {
      this.fullness += val;
      return val;
    } else {
      int tmp = this.MAX_FULLNESS - this.fullness;
      this.fullness = this.MAX_FULLNESS;
      return tmp;
    }
  }
  // fullness-
  public void hunger(int val) {
    if (this.fullness - val >= 0) {
      this.fullness -= val;
    } else {
      this.fullness = 0;
    }
  }
  public void pickUp(int id, MainFrame fr) {
    if (id < 10) {
      fr.log.add(itemList[id] + " was picked up.");
      items.add(id);
      Collections.sort(items);
    } else {
      fr.log.add(weaponList[id % 10] + " was picked up.");
      weapons.add(id % 10);
      Collections.sort(weapons);
    }
  }

  @Override
  public String toString() {
    return super.toString() + ", exp: " + this.exp + ", items: " + this.items.toString();
  }
}