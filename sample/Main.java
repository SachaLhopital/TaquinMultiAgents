package sample;

import system.Agent;
import system.Grid;
import system.Message;
import system.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static Grid grid;
    public static Long lastTimeSomeoneMoved;
    public static HashMap<Agent, List<Message>> communications;

    public static void main(String[] args) {

        Agent a1 = new Agent(new Position(0, 3), new Position(0,0), '*');
        Agent a2 = new Agent(new Position(0, 2), new Position(0,2), 'o');
        Agent a3 = new Agent(new Position(1,2),  new Position(1,2),'Y');
        Agent a4 = new Agent(new Position(2, 3), new Position(2,3), '+');
        Agent a5 = new Agent(new Position(2, 4), new Position(2,4), '&');
        Agent a6 = new Agent(new Position(0, 1), new Position(0,1), '#');
        Agent a7 = new Agent(new Position(2, 0), new Position(2,2), '@');
        Agent a8 = new Agent(new Position(3, 1), new Position(3,3), '=');
        Agent a9 = new Agent(new Position(4, 0), new Position(4,2), '$');
        Agent a10 = new Agent(new Position(4, 4), new Position(0,4), '%');
        Agent a11 = new Agent(new Position(1, 1), new Position(1,4), 'µ');
        Agent a12 = new Agent(new Position(2, 1), new Position(3,0), '?');
        Agent a13 = new Agent(new Position(2, 3), new Position(3,4), '!');
        Agent a14 = new Agent(new Position(4, 1), new Position(1,0), 'Z');
        /*Agent a15 = new Agent(new Position(, ), new Position(,), 'R');
        Agent a16 = new Agent(new Position(, ), new Position(,), 'M');
        Agent a17 = new Agent(new Position(,), new Position(,), 'P');
        Agent a18 = new Agent(new Position(,), new Position(,), 'F');
        Agent a19 = new Agent(new Position(,), new Position(,), 'H');
        Agent a20 = new Agent(new Position(,), new Position(,), 'K');*/

        communications = new HashMap<>();
        communications.put(a1, new ArrayList<>());
        communications.put(a2, new ArrayList<>());
        communications.put(a3, new ArrayList<>());
        communications.put(a4, new ArrayList<>());
        communications.put(a5, new ArrayList<>());
        communications.put(a6, new ArrayList<>());
        communications.put(a7, new ArrayList<>());
        communications.put(a8, new ArrayList<>());
        communications.put(a9, new ArrayList<>());
        communications.put(a10, new ArrayList<>());
        communications.put(a11, new ArrayList<>());
        communications.put(a12, new ArrayList<>());
        communications.put(a13, new ArrayList<>());
        communications.put(a14, new ArrayList<>());

        grid = new Grid(a1, a2, a3, a4, a5, a6, a7, a8, a9,
                a10, /*a11,*/ a12, a13);

        grid.resolve();
    }

    public static synchronized void iMoved() {
        lastTimeSomeoneMoved = System.currentTimeMillis();
    }

    /***
     * Add message to the queue
     * @param message
     */
    public static void sendMessage(Message message) {

        /*List<Message> listMessages = communications.get(message.getSender());
        if ((listMessages.size() > 0)) {
            for (int i = 0; i < listMessages.size(); i++) {
                if (listMessages.get(i).getPositionToFree().equals(message.getPositionToFree())) {
                    System.out.println("cant add !");
                    return;
                }
            }
        }*/

        communications.get(message.getReceiver()).add(message);

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /***
     * Get the next message for specific agent
     * @param agent
     * @return
     */
    public static Message readNextMessageFor(Agent agent) {
        List<Message> listMessages = communications.get(agent);
        return (listMessages.size() == 0) ? null : listMessages.remove(0);
    }
}
