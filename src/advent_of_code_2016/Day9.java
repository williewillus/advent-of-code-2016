package advent_of_code_2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day9 {
    private static final boolean PART_TWO = true;

    interface Node {
        long getLength();
        void show(int depth);
    }

    private static class SimpleNode implements Node {
        private final long length;

        public SimpleNode(long len) {
            this.length = len;
        }

        @Override
        public long getLength() {
            return length;
        }

        @Override
        public void show(int depth) {
            System.out.println(String.join("", Collections.nCopies(depth, " ")) + Long.toString(length));
        }
    }

    private static class ExpanderNode implements Node {
        List<Node> children = new ArrayList<>();
        final int multiplier;

        public ExpanderNode(int multiplier) {
            this.multiplier = multiplier;
        }

        public ExpanderNode() {
            this(1);
        }

        @Override
        public long getLength() {
            return multiplier * children.stream().mapToLong(Node::getLength).sum();
        }

        @Override
        public void show(int depth) {
            String padding = String.join("", Collections.nCopies(depth, " "));
            System.out.printf(padding + "Expander (multiplier %d)%n", multiplier);
            for (Node n : children) {
                n.show(depth + 1);
            }
        }
    }

    private static void parse(ExpanderNode root, String input) {
        // Eat all non-expand-type letters
        int i = 0;
        while (i < input.length() && input.charAt(i) != '(') {
            i++;
        }

        if (i > 0) {
            root.children.add(new SimpleNode(i));
        }

        input = input.substring(i);

        if (!input.isEmpty()) {
            Matcher m = Pattern.compile("\\((\\d+)x(\\d+)\\)").matcher(input);
            m.find();
            int markerSize = m.group(0).length();
            int chars = Integer.parseInt(m.group(1));
            int multiplier = Integer.parseInt(m.group(2));

            // Parse the group we just found
            ExpanderNode child = new ExpanderNode(multiplier);
            root.children.add(child);

            if (PART_TWO) {
                parse(child, input.substring(markerSize, markerSize + chars));
            } else {
                child.children.add(new SimpleNode(chars));
            }

            // Eat the group we just parsed then keep going
            parse(root, input.substring(markerSize + chars));
        }
    }

    public static void main(String[] args) throws IOException {
        String input = String.join("", Files.readAllLines(Paths.get("d9_input.txt")));
        ExpanderNode root = new ExpanderNode(1);

        parse(root, input);

        root.show(0);
        System.out.println(root.getLength());
    }
}

