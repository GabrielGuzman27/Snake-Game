import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private final int TILE_SIZE = 20;
    private final int WIDTH = 20, HEIGHT = 20;
    private LinkedList<Point> snake = new LinkedList<>();
    private Point food;
    private char direction = 'R';
    private boolean running = true;
    private Timer timer;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        snake.add(new Point(5, 5));
        spawnFood();
        timer = new Timer(100, this);
        timer.start();
    }

    private void spawnFood() {
        Random rand = new Random();
        food = new Point(rand.nextInt(WIDTH), rand.nextInt(HEIGHT));
        while (snake.contains(food)) {
            food = new Point(rand.nextInt(WIDTH), rand.nextInt(HEIGHT));
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
        g.setColor(Color.RED);
        g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        if (!running) {
            g.setColor(Color.WHITE);
            g.drawString("Game Over!", WIDTH * TILE_SIZE / 2 - 30, HEIGHT * TILE_SIZE / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!running) return;
        Point head = snake.getFirst();
        Point newHead = new Point(head.x, head.y);
        switch (direction) {
            case 'U': newHead.y--; break;
            case 'D': newHead.y++; break;
            case 'L': newHead.x--; break;
            case 'R': newHead.x++; break;
        }
        if (newHead.x < 0 || newHead.x >= WIDTH || newHead.y < 0 || newHead.y >= HEIGHT || snake.contains(newHead)) {
            running = false;
            timer.stop();
        } else {
            snake.addFirst(newHead);
            if (newHead.equals(food)) {
                spawnFood();
            } else {
                snake.removeLast();
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        char newDir = direction;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:    newDir = 'U'; break;
            case KeyEvent.VK_DOWN:  newDir = 'D'; break;
            case KeyEvent.VK_LEFT:  newDir = 'L'; break;
            case KeyEvent.VK_RIGHT: newDir = 'R'; break;
        }
        // Prevent reversing direction
        if ((direction == 'U' && newDir != 'D') ||
            (direction == 'D' && newDir != 'U') ||
            (direction == 'L' && newDir != 'R') ||
            (direction == 'R' && newDir != 'L')) {
            direction = newDir;
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new SnakeGame());
        frame.pack();
        frame.setVisible(true);
    }
}