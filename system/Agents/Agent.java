package system.Agents;

import sample.Main;
import system.Message;
import system.Position;

public abstract class Agent extends Thread {

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

    public abstract void run();

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

    public synchronized void setCurrentPosition(Position position) {

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
}
