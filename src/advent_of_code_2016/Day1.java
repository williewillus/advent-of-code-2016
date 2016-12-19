package advent_of_code_2016;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day1 {

    private static final boolean PART_TWO = true;

    public enum Direction {
        NORTH, EAST, SOUTH, WEST;

        public Direction ccw() {
            switch (this) {
                case NORTH: return WEST;
                case EAST: return NORTH;
                case SOUTH: return EAST;
                case WEST: return SOUTH;
                default: throw new IllegalStateException("wut");
            }
        }

        public Direction cw() {
            switch (this) {
                case NORTH: return EAST;
                case EAST: return SOUTH;
                case SOUTH: return WEST;
                case WEST: return NORTH;
                default: throw new IllegalStateException("wut");
            }
        }
    }

    public static class Pos {
        private final int x;
        private final int y;

        public Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Pos move(Direction d, int count) {
            switch (d) {
                case NORTH: return new Pos(x, y + count);
                case SOUTH: return new Pos(x, y - count);
                case WEST: return new Pos(x - count, y);
                case EAST: return new Pos(x + count, y);
                default: throw new IllegalStateException("wut");
            }
        }

        public int dist() {
            return Math.abs(x) + Math.abs(y);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Pos && ((Pos) o).x == x && ((Pos) o).y == y;
        }

        @Override
        public String toString() {
            return "[" + x + ", " + y + "]";
        }
    }

    public static void main(String[] args) throws Exception {
        String input = String.join("", Files.readAllLines(Paths.get("d1_input.txt"))).replaceAll("\\R+", "");
        Matcher m = Pattern.compile("[LR]\\d+").matcher(input);

        Set<Pos> seen = new HashSet<>();

        Direction currentDir = Direction.NORTH;
        Pos currentPos = new Pos(0, 0);

        outer: while (m.find()) {
            char moveDir = m.group().charAt(0);
            int moveAmt = Integer.parseInt(m.group().substring(1));

            switch (moveDir) {
                case 'L': currentDir = currentDir.ccw(); break;
                case 'R': currentDir = currentDir.cw(); break;
            }

            for (int i = 0; i < moveAmt; i++) {
                currentPos = currentPos.move(currentDir, 1);
                if (PART_TWO && !seen.add(currentPos)) {
                    System.out.println("ALREADY SEEN");
                    break outer;
                }
            }
        }

        System.out.println(currentPos.dist());
    }

}
