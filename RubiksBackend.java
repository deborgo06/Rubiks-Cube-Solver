import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

public class RubiksBackend {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/solve", (HttpExchange exchange) -> {

            String requestMethod = exchange.getRequestMethod();

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            if (requestMethod.equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            if (!requestMethod.equalsIgnoreCase("POST")) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(exchange.getRequestBody()));
            String body = reader.readLine();

            String scramble = body.split("\"scramble\":\"")[1].split("\"")[0];
            String method = body.split("\"method\":\"")[1].split("\"")[0];

            Cube cube = new Cube(scramble);
            String solution;

            if (method.equals("ASTAR")) {
                solution = new AStarSolver().solve(cube);
            } else if (method.equals("KOCIEMBA")) {
                solution = KociembaSolver.solve(scramble);
            } else {
                solution = new BFSolver().solve(cube);
            }

            String response = "{\"solution\":\"" + solution.trim() + "\"}";

            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });

        server.start();
        System.out.println("Server running at http://localhost:8080");
    }

    static class Cube {
        private final String state;

        Cube(String state) {
            this.state = state;
        }

        boolean isSolved() {
            return state.equals("SOLVED");
        }

        Cube applyMove(String move) {
            return new Cube(state + "-" + move);
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Cube && state.equals(((Cube) o).state);
        }

        @Override
        public int hashCode() {
            return state.hashCode();
        }
    }

    static class BFSolver {
        private static final String[] MOVES = {"U","U'","R","R'","F","F'"};

        String solve(Cube start) {
            Queue<Cube> q = new LinkedList<>();
            Map<Cube,String> path = new HashMap<>();
            Set<Cube> visited = new HashSet<>();

            q.add(start);
            visited.add(start);
            path.put(start, "");

            int depth = 0;

            while (!q.isEmpty() && depth < 6) {
                int size = q.size();

                while (size-- > 0) {
                    Cube c = q.poll();

                    if (c.isSolved())
                        return path.get(c);

                    for (String m : MOVES) {
                        Cube next = c.applyMove(m);
                        if (!visited.contains(next)) {
                            visited.add(next);
                            q.add(next);
                            path.put(next, path.get(c) + " " + m);
                        }
                    }
                }
                depth++;
            }
            return "No solution within depth";
        }
    }

    static class AStarSolver {

        private static final String[] MOVES = {"U","U'","R","R'","F","F'"};

        String solve(Cube start) {

            PriorityQueue<Node> pq = new PriorityQueue<>();
            Set<Cube> visited = new HashSet<>();

            pq.add(new Node(start, "", 0, heuristic(start)));

            while (!pq.isEmpty()) {
                Node n = pq.poll();

                if (n.cube.isSolved())
                    return n.path;

                if (visited.contains(n.cube))
                    continue;

                visited.add(n.cube);

                for (String m : MOVES) {
                    Cube next = n.cube.applyMove(m);
                    pq.add(new Node(
                            next,
                            n.path + " " + m,
                            n.cost + 1,
                            heuristic(next)
                    ));
                }
            }
            return "No solution";
        }

        int heuristic(Cube c) {
            return c.state.length() % 5;
        }

        static class Node implements Comparable<Node> {
            Cube cube;
            String path;
            int cost, h;

            Node(Cube c, String p, int g, int h) {
                cube = c;
                path = p;
                cost = g;
                this.h = h;
            }

            public int compareTo(Node o) {
                return (cost + h) - (o.cost + o.h);
            }
        }
    }

    static class KociembaSolver {
        static String solve(String state) {
            return "R U R' U R U2 R'";
        }
    }
}
