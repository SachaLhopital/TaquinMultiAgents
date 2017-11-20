package system.Agents;

import sample.Main;
import system.Message;
import system.Position;

/**
 * Created by Sachouw on 09/10/2017.
 */
public class AgentCognitif extends Agent {

    public AgentCognitif(Position initialPosition, Position targetPosition, char img) {
        super(initialPosition, targetPosition, img);
    }

    /***
     * Approche cognitive : Agents communicate with each other to resolve the puzzle
     */
    public void run() {

        while(Main.grid.isNotComplete()) {

            Message message = Main.readNextMessageFor(this);

            if(message != null
                    && message.getPositionToFree().equals(getCurrentPosition())
                    && message.getSenderPosition().equals(message.getSender().getCurrentPosition())) {
                //Si on doit changer de place pour aider un autre agent

                Position hole = Main.grid.getRandomHoleAroundMe(this);

                if (hole == getCurrentPosition()) {
                    //il n'y a pas de trou autour de nous : sendMessageToNeighbour
                    sendMessageToNeighbour();
                } else {
                    System.out.println(getImage() + " try to moves for " + message.getSender().getImage());
                    setCurrentPosition(hole);
                    waitUntilAgentMoves(message);
                }
            } else {

                if(isAtTargetPosition()) {
                    //on ne fait rien
                    Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
                } else {
                    //On essaye d'atteindre notre position cible.
                    if(getActualPositionY() != getTargetPositionY()) {

                        //on bouge en Y
                        if(getActualPositionY() > getTargetPositionY()) {
                            setCurrentPosition(new Position(getActualPositionX(), getActualPositionY() - 1));
                        } else {
                            setCurrentPosition(new Position(getActualPositionX(), getActualPositionY() + 1));
                        }
                    } else {
                        //on bouge en X
                        if(getActualPositionX() > getTargetPositionX()) {
                            setCurrentPosition(new Position(getActualPositionX() - 1, getActualPositionY()));
                        } else {
                            setCurrentPosition(new Position(getActualPositionX() + 1, getActualPositionY()));
                        }
                    }
                }
            }
        }
    }

    /***
     * Send Message to all neighbour to move
     */
    private void sendMessageToNeighbour() {
        Agent neighbour = Main.grid.getNeighbour(this);
        Message message = new Message(this, neighbour, neighbour.getCurrentPosition());
        Main.sendMessage(message);
    }

    /***
     * Wait until a specific agent moves based on a message he send to us
     * @param m
     */
    private void waitUntilAgentMoves(Message m) {

        Agent agentWaitingFor = m.getSender();
        Position p = m.getSenderPosition();

        //todo remove
        /*if(agentWaitingFor.getImage() == getImage()) {
            System.out.println("ERREUR : ");
            for(Message temp : Main.communications.get(this)) {
                System.out.println(temp.getSender().getImage()
                        + " send " + temp.getPositionToFree().getX() + " | " + temp.getPositionToFree().getY()
                        + " from " + temp.getSenderPosition().getX() + " | " + temp.getSenderPosition().getY());
            }
        }*/

        int priority = getPriority() == MIN_PRIORITY ? getPriority() : getPriority() - 1;
        setPriority(priority);
        System.out.println(getImage() + " waiting for " + agentWaitingFor.getImage() + " (set prio : " + getPriority() + ")");

        //Tant que l'agent qui nous a demandé de bouger est à la position qui lui a fait envoyer le message
        //et qu'il ne s'est pas passé 1s
        while(p == agentWaitingFor.getCurrentPosition()
                && (System.currentTimeMillis() - Main.lastTimeSomeoneMoved < 1000)) {
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(getImage() + " proceed");
    }
}
