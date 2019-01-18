public class Main {
  public static void main(String[] args) {
    MainFrame fr = new MainFrame();
    fr.pack(); // Window側で推奨サイズに合わせる
    fr.setLocationRelativeTo(null); // 初期画面表示位置を中央に
    fr.setVisible(true);
    fr.gameStart();
  }
}