package system;

/**
 * Created by Sachouw on 09/10/2017.
 */
public class Position {

    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Position p) {
        return (p.x == x && p.y == y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
