package system;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Sachouw on 09/10/2017.
 */
public class Grid {

    private static int SIZE = 5;
    private static char CASE_VIDE = '-';

    private char[][] grid;
    private List<Agent> agents;

    public Grid(Agent... a) {

        grid = new char[SIZE][SIZE];

        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++) {
                grid[i][j] = CASE_VIDE;
            }
        }


        agents = new ArrayList<>();
        Agent temp;

        for(int k = 0; k < a.length; k++) {
            temp = a[k];
            agents.add(temp);
            grid[temp.getActualPositionX()][temp.getActualPositionY()] = temp.getImage();
        }

        System.out.println("Puzzle Initial : \n");
        printPuzzle();
    }


    /*************************************************
     * PUBLIC METHODS
     */


    /***
     * Start all agent threads
     */
    public void resolve() {
        for(int k = 0; k < agents.size(); k++) {
            agents.get(k).start();
        }
    }

    /***
     * check if the puzzle is complete
     * (check if all agents are in there target position)
     * @return
     */
    public boolean isNotComplete() {
        for(Agent a : agents) {
            if(! a.isAtTargetPosition()) {
                return true;
            }
        }
        return false;
    }

    /***
     * Display the puzzle on the console
     */
    public synchronized void printPuzzle() {
        System.out.println("\n");
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    /***
     * Get a hole next to an agent (randomly between all holes around him)
     * @param agent
     * @return
     */
    public synchronized Position getRandomHoleAroundMe(Agent agent) {

        int x = agent.getActualPositionX();
        int y = agent.getActualPositionY();
        List<Position> holes = new ArrayList<>();

        if((x + 1) < SIZE && (grid[x + 1][y] == CASE_VIDE)) {
            return new Position(x + 1, y);
        }
        if((x - 1) >= 0 && (grid[x - 1][y] == CASE_VIDE)) {
            return new Position(x - 1, y);
        }
        if((y - 1) >= 0 && (grid[x][y - 1] == CASE_VIDE)) {
            return new Position(x, y - 1);
        }
        if((y + 1) < SIZE && (grid[x][y + 1] == CASE_VIDE)) {
            return new Position(x, y + 1);
        }
        return agent.getCurrentPosition();
    }

    /***
     * Move selected agent from its current position to a target position
     * @param targetPosition
     * @param agent
     */
    public synchronized void moveAgentFromTo(Position targetPosition, Agent agent) {
        Position agentCurrentPosition = agent.getCurrentPosition();
        grid[agentCurrentPosition.getX()][agentCurrentPosition.getY()] = CASE_VIDE;
        grid[targetPosition.getX()][targetPosition.getY()] = agent.getImage();
        printPuzzle();
    }

    /***
     * Retourne la liste du chemin à emprunter pour aller d'un point "from" à un point "to" sans
     * entrer en collision avec d'autres agents
     * Algorithme de Dijkstra
     * @param from
     * @param to
     * @return
     */
    public Position getNextPositionWithDijkstraFromTo(Position from, Position to) {

        int x;
        int y;
        int alt;
        Position currentPoint;

        int[][] dist = new int[grid.length][grid.length];
        Position[][] prev = new Position[grid.length][grid.length];
        List<Position> pointsDeLaGrille = new ArrayList<>();

        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid.length; j++) {
                dist[i][j] = Integer.MAX_VALUE;//valeur "infini"
                prev[i][j] = null;
                pointsDeLaGrille.add(new Position(i, j));
            }
        }

        dist[from.getX()][from.getY()] = 0;

        //hack : agents.size() - 1 car on ne parcours pas les cases occupées par d'autres agents que l'agent qui effectue dijkstra
        while(pointsDeLaGrille.size() > agents.size() - 1) {

            currentPoint = getMinFromTab(pointsDeLaGrille, dist);
            pointsDeLaGrille = removeFromList(pointsDeLaGrille, currentPoint);

            x = currentPoint.getX();
            y = currentPoint.getY();
            alt = dist[x][y] + 1;

            //Mise à jour des voisins
            if ((x + 1) < SIZE && (grid[x + 1][y] == CASE_VIDE)
                    && alt < dist[x + 1][y]) {
                dist[x + 1][y] = alt;
                prev[x + 1][y] = currentPoint;
            }
            if ((x - 1) >= 0 && (grid[x - 1][y] == CASE_VIDE)
                    && alt < dist[x - 1][y]) {
                dist[x - 1][y] = alt;
                prev[x - 1][y] = currentPoint;
            }
            if ((y - 1) >= 0 && (grid[x][y - 1] == CASE_VIDE)
                    && alt < dist[x][y - 1]) {
                dist[x][y - 1] = alt;
                prev[x][y - 1] = currentPoint;
            }
            if ((y + 1) < SIZE && (grid[x][y + 1] == CASE_VIDE)
                    && alt < dist[x][y + 1]) {
                dist[x][y + 1] = alt;
                prev[x][y + 1] = currentPoint;
            }
        }
        /* FIN DIJKSTRA */

        currentPoint = to;

        //cas particulier si on ne peux pas atteindre to on reste où on est
        if(prev[currentPoint.getX()][currentPoint.getY()] == null) {
            return from;
        }

        while(! prev[currentPoint.getX()][currentPoint.getY()].equals(from)) {
            currentPoint = prev[currentPoint.getX()][currentPoint.getY()];
        }

        return currentPoint;
    }


    /***
     * Get the agent in the position specified.
     * If there is not agent at this position, return null
     * @param position
     * @return
     */
    public synchronized Agent getAgentAtPositionIfExist(Position position) {

        for(int k = 0; k < agents.size(); k++) {

            if(agents.get(k).getCurrentPosition().equals(position)) {
                return agents.get(k);
            }
        }
        return null;
    }

    /***
     * Get a neighbour of an agent (randomly choose between all it's neighbours)
     * @param agent
     * @return
     */
    public synchronized Agent getNeighbour(Agent agent) {

        int x = agent.getActualPositionX();
        int y = agent.getActualPositionY();
        List<Agent> neighbours = new ArrayList<>();

        if((x + 1) < SIZE) {
            return getAgentAtPositionIfExist(new Position(x + 1, y));
        }
        if((x - 1) >= 0) {
            return getAgentAtPositionIfExist(new Position(x - 1, y));
        }
        if((y - 1) >= 0) {
            return getAgentAtPositionIfExist(new Position(x, y - 1));
        }
        if((y + 1) < SIZE) {
            return getAgentAtPositionIfExist(new Position(x, y + 1));
        }
        return agent;
    }



    /*************************************************
     * PRIVATES METHODS : DIJSKTRA UTILITIES
     */

    private Position getMinFromTab(List<Position> pointsDeLaGrille, int[][] dist) {

        int value = Integer.MAX_VALUE;
        int x = 0;
        int y = 0;
        int tempX = 0;
        int tempY = 0;

        for(int i = 0; i < pointsDeLaGrille.size(); i++){
            tempX = pointsDeLaGrille.get(i).getX();
            tempY = pointsDeLaGrille.get(i).getY();

            if(dist[tempX][tempY] <  value) {
                value = dist[tempX][tempY];
                x = tempX;
                y = tempY;
            }
        }
        return new Position(x, y);
    }

    private List<Position> removeFromList(List<Position> pointsDeLaGrille, Position currentPoint) {
        for(int i = 0; i < pointsDeLaGrille.size(); i++){
            Position p = pointsDeLaGrille.get(i);
            if(currentPoint.equals(p)) {
                pointsDeLaGrille.remove(p);
            }
        }
        return pointsDeLaGrille;
    }
}