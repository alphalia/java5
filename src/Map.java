import java.awt.*;
import java.util.*;

public class Map {
  int w, h;
  Point[][] prevNode;
  char[][] status;
  int[][] chara;
  int[][] object;
  int[][] minDistance;
  String[] objList = {"Portion", "Green Apple", "Ether", "Juicy Apple", "Taurine", "Protein", "Monster" , "Elixir", "Slime Crystal", "Dark Portion", 
    "Holy Staff", "Dark Staff", "Sword", "Lance", "Shield Sword"};
  
  ArrayList<Enemy> enemies;
  ArrayList<Integer> enemiesID;
  Hero hero = new Hero(0, "Michel", new Point());
  Point start = new Point(), goal = new Point();
  Point generalStart = new Point(), center = new Point(9, 7), edge = new Point(), frameEdge = new Point();
  Random rnd = new Random();

  PriorityQueue<Node> pq = new PriorityQueue<Node>(650, new Comparator<Node>(){
    @Override
    public int compare(Node o1, Node o2) {
      Node node1 = o1, node2 = o2;
      if (node1.distance < node2.distance) {
        return 1;
      } else if (node1.distance > node2.distance) {
        return -1;
      } else return 0;
    }
  });

  public Map(int w, int h) {
    this.w = w; this.h = h;
    prevNode = new Point[h][w];
    status = new char[h][w];
    chara = new int[h][w];
    object = new int[h][w];
    minDistance = new int[h][w];
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        chara[i][j] = -1;
        object[i][j] = -1;
      }
    }
  }

  public void search(Hero hero, Enemy enemy) {
    // 最小距離を初期化
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        minDistance[i][j] = Integer.MAX_VALUE;
      }
    }
    Node node = new Node(hero.pos, new Point(-1, -1), 0);
    pq.add(node);
    // 15 * 15の範囲を探索
    while (!pq.isEmpty()) {
      Node curr = pq.poll();
      // 最小距離がみつかった場合
      if (curr.distance < minDistance[curr.pos.y][curr.pos.x]) {
        // ヒーローからの最小距離を更新
        minDistance[curr.pos.y][curr.pos.x] = curr.distance;
        // 1個前のマスを代入
        prevNode[curr.pos.y][curr.pos.x] = new Point(curr.prevPos);
        // 次のマスへ
        if (curr.distance < 50) {
          if (status[curr.pos.y][curr.pos.x + 1] != 'B' && chara[curr.pos.y][curr.pos.x + 1] == -1) {
            Node next1 = new Node(new Point(curr.pos.x + 1, curr.pos.y), new Point(curr.pos.x, curr.pos.y), curr.distance + 1);
            pq.add(next1);
          }
          if (status[curr.pos.y][curr.pos.x - 1] != 'B' && chara[curr.pos.y][curr.pos.x - 1] == -1) {
            Node next2 = new Node(new Point(curr.pos.x - 1, curr.pos.y), new Point(curr.pos.x, curr.pos.y), curr.distance + 1);
            pq.add(next2);
          }
          if (status[curr.pos.y + 1][curr.pos.x] != 'B' && chara[curr.pos.y + 1][curr.pos.x] == -1) {
            Node next3 = new Node(new Point(curr.pos.x, curr.pos.y + 1), new Point(curr.pos.x, curr.pos.y), curr.distance + 1);
            pq.add(next3);
          }
          if (status[curr.pos.y - 1][curr.pos.x] != 'B' && chara[curr.pos.y - 1][curr.pos.x] == -1) {
            Node next4 = new Node(new Point(curr.pos.x, curr.pos.y - 1), new Point(curr.pos.x, curr.pos.y), curr.distance + 1);
            pq.add(next4);
          }
        }
      }
    }
  }
  // 敵を動かす
  public Dir towardHero(Enemy enemy) {
    if (prevNode[enemy.pos.y][enemy.pos.x] != null) {
      // 一回前の更新で通ったところに向かって，敵が動く
      return Dir.direction(enemy.pos, prevNode[enemy.pos.y][enemy.pos.x]);
    } else return Dir.getDir(rnd.nextInt(4));
  }
  // local -> general
  public Point getGeneral(Point p) {
    int dx = p.x - start.x, dy = p.y - start.y;
    return new Point(generalStart.x + dx, generalStart.y + dy);
  }
  // general -> local
  public Point getLocal(Point p) {
    int dx = p.x - generalStart.x, dy = p.y - generalStart.y;
    return new Point(start.x + dx, start.y + dy);
  }
  /**
   * 画面内にあるかどうか
   * @param p マップのローカル座標
   */
  public boolean isInView(Point p) {
    boolean viewX = false, viewY = false;
    viewX = frameEdge.x <= p.x && p.x < frameEdge.x + w;
    viewY = frameEdge.y <= p.y && p.y < frameEdge.y + h;
    return viewX && viewY;
  }
}

class Node {
  public Point pos, prevPos;
  public int distance;
  public Node(Point pos, Point prevPos, int distance) {
    this.pos = pos; this.prevPos = prevPos; this.distance = distance;
  }
}