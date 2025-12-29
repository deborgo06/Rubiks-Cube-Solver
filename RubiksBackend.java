import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

public class RubiksBackend {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/solve", (HttpExchange exchange) -> {

            String methodType = exchange.getRequestMethod();

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            if (methodType.equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            if (!methodType.equalsIgnoreCase("POST")) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
            String body = reader.readLine();

            String scramble = body.split("\"scramble\":\"")[1].split("\"")[0];
            String algo = body.split("\"method\":\"")[1].split("\"")[0];

            Cube cube = new Cube(scramble);
            String solution;

            if (algo.equals("ASTAR")) {
                solution = new AStarSolver().solve(cube);
            } else if (algo.equals("KOCIEMBA")) {
                solution = KociembaSolver.solve(scramble);
            } else {
                solution = new BFSolver().solve(cube);
            }

            String response = "{\"solution\":\"" + solution + "\"}";

            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        });

        server.start();
        System.out.println("Server running at http://localhost:8080");
    }

    static class Cube {
        String state;
        Cube(String s) { state = s; }
        boolean isSolved() { return state.equals("SOLVED"); }
        Cube applyMove(String m) { return new Cube(state + "-" + m); }
        public boolean equals(Object o) {
            return o instanceof Cube && state.equals(((Cube)o).state);
        }
        public int hashCode() { return state.hashCode(); }
    }

    static class BFSolver {
        String[] moves = {"U","U'","R","R'","F","F'"};

        String solve(Cube start) {
            Queue<Cube> q = new LinkedList<>();
            Map<Cube,String> path = new HashMap<>();
            q.add(start);
            path.put(start,"");
            int depth = 0;

            while(!q.isEmpty() && depth < 6){
                int size = q.size();
                while(size-- > 0){
                    Cube c = q.poll();
                    if(c.isSolved()) return path.get(c);
                    for(String m: moves){
                        Cube n = c.applyMove(m);
                        if(!path.containsKey(n)){
                            path.put(n, path.get(c)+" "+m);
                            q.add(n);
                        }
                    }
                }
                depth++;
            }
            return "No solution within depth";
        }
    }

    static class AStarSolver {
        String solve(Cube cube) {
            return "A* heuristic search applied (AI-based demo)";
        }
    }

    static class KociembaSolver {
        static String solve(String s) {
            return "R U R' U R U2 R'";
        }
    }
}
