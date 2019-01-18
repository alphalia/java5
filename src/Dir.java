import java.awt.*;

public enum Dir {
  UP(0, -1), RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0), STAY(0, 0);
  private int dx, dy;

  private Dir(int dx, int dy) {
    this.dx = dx; this.dy = dy;
  }

  public int getX() {
    return this.dx;
  }
  public int getY() {
    return this.dy;
  }
  public static Dir getDir(int ordinal) {
    if (ordinal == 0) {
      return UP;
    } else if (ordinal == 1) {
      return RIGHT;
    } else if (ordinal == 2) {
      return DOWN;
    } else if (ordinal == 3) {
      return LEFT;
    } else return STAY;
  }
  public static Dir direction(Point from, Point to) {
    int dx = to.x - from.x, dy = to.y - from.y;
    if (dx == 1 && dy == 0) {
      return RIGHT;
    } else if (dx == -1 && dy == 0) {
      return LEFT;
    } else if (dx == 0 && dy == 1) {
      return DOWN;
    } else if (dx == 0 && dy == -1) {
      return UP;
    } else return STAY;
  }
}