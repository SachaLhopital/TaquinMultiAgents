package system;

import sample.Main;

/**
 * Created by Sachouw on 09/10/2017.
 */
public class Agent extends Thread {

    private final int T_MAX = 1;

    private Position previousPosition;
    private Position currentPosition;
    private Position targetPosition;
    private char image;

    public Agent(Position initialPosition, Position targetPosition, char img) {
        this.targetPosition = targetPosition;
        this.currentPosition = initialPosition;
        this.previousPosition = initialPosition;
        this.image = img;
    }

    public void run() {

        while(Main.grid.isNotComplete()) {

            /* Q1 : Approche Naive */

            /*if(isAtTargetPosition()) {
                //do nothing
            } else {
                //On récupère le point sur lequel on souhaite se déplacer
                Position p1 = getCurrentPosition();
                Position p2 = getTargetPosition();
                Position p = Main.grid.getNextPositionWithDijkstraFromTo(p1, p2);
                setCurrentPosition(p);
            }*/


            /* Q2 : Approche cognitive */

            Message message = Main.readNextMessageFor(this);

            if(message != null
                    && message.getPositionToFree().equals(getCurrentPosition())
                    && message.getSenderPosition().equals(message.getSender().getCurrentPosition())) {
                //Si on doit changer de place pour aider un autre agent

                Position hole = Main.grid.getRandomHoleAroundMe(this);

                if(hole == getCurrentPosition()) {
                    //il n'y a pas de trou autour de nous : sendMessageToNeighbour
                    sendMessageToNeighbour();
                } else {
                    System.out.println(getImage() + " moves for " + message.getSender().getImage());
                    setCurrentPosition(hole);
                    waitUntilAgentMoves(message.getSender());
                }
            } else {

                if(isAtTargetPosition()) {
                    //on ne fait rien
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
     * Check if the agent is at it's target position
     * @return
     */
    public boolean isAtTargetPosition() {
        return getCurrentPosition().equals(getTargetPosition());
    }


    /***
     * GETTERS & SETTERS
     ****/

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public Position getTargetPosition() {
        return targetPosition;
    }

    public void setCurrentPosition(Position position) {

        if(previousPosition.equals(position)) {
            //Si la position où on était précédement est la même que celle que l'on essaye d'atteindre
            //on tourne en rond, donc on se déplace aléatoirement ailleurs
            position = Main.grid.getRandomHoleAroundMe(this);
            System.out.println(getImage() + " moves randomly");
        }

        previousPosition = currentPosition;
        Agent neighbour = Main.grid.getAgentAtPositionIfExist(position);

        if (neighbour == null) {
            //on se déplace
            Main.grid.moveAgentFromTo(position, this);
            currentPosition = position;
            System.out.println(getImage() + " set time");
            Main.iMoved();
        } else {
            //on envoi un message
            Message message = new Message(this, neighbour, position);
            Main.sendMessage(message);
        }
    }

    public int getActualPositionY() {
        return currentPosition.getY();
    }

    public int getActualPositionX() {
        return currentPosition.getX();
    }

    public int getTargetPositionY() {
        return targetPosition.getY();
    }

    public int getTargetPositionX() {
        return targetPosition.getX();
    }

    public char getImage() {
        return image;
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
     * Wait until a specific agent moves
     * @param a
     */
    private void waitUntilAgentMoves(Agent a) {
        System.out.println(getImage() + " waiting for " + a.getImage());

        /*if(a.getImage() == getImage()) {

            for(Message m : Main.communications.get(a)) {
                System.out.println(m.toString());
            }
        }*//*if(a.getImage() == getImage()) {

            for(Message m : Main.communications.get(a)) {
                System.out.println(m.toString());
            }
        }*/

        Position p = a.getCurrentPosition();
        //Ou si personne n'a bougé depuis 1s
        while(p == a.getCurrentPosition() || (System.currentTimeMillis() - Main.lastTimeSomeoneMoved > 1000)) {
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(getImage() + " proceed");
    }
}
