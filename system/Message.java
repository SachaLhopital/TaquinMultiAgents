package system;

/**
 * Created by Sachouw on 09/10/2017.
 */
public class Message {

    private Agent sender;
    private Position senderPosition;
    private Agent receiver;
    private Position positionToFree;

    public Message(Agent s, Agent r, Position p) {
        sender = s;
        senderPosition = s.getCurrentPosition();
        receiver = r;
        positionToFree = p;
    }

    public Position getPositionToFree() {
        return positionToFree;
    }

    public Position getSenderPosition() {
        return senderPosition;
    }

    public Agent getReceiver() {
        return receiver;
    }

    public Agent getSender() {
        return sender;
    }
}
