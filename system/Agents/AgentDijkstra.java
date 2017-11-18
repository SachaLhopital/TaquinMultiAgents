package system.Agents;

import sample.Main;
import system.Message;
import system.Position;

/**
 * Created by Sachouw on 09/10/2017.
 */
public class AgentDijkstra extends Agent {

    public AgentDijkstra(Position initialPosition, Position targetPosition, char img) {
        super(initialPosition, targetPosition, img);
    }

    /***
     * Approche Naive : Agent resolving the puzzle with dijkstra
     */
    public void run() {

        while(Main.grid.isNotComplete()) {

            if(isAtTargetPosition()) {
                //do nothing
            } else {
                //On récupère le point sur lequel on souhaite se déplacer
                Position p1 = getCurrentPosition();
                Position p2 = getTargetPosition();
                Position p = Main.grid.getNextPositionWithDijkstraFromTo(p1, p2);
                setCurrentPosition(p);
            }
        }
    }
}
