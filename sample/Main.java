package sample;

import system.Agents.Agent;
import system.Agents.AgentCognitif;
import system.Agents.AgentDijkstra;
import system.Grid;
import system.Message;
import system.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static Grid grid;
    public static String temp;
    public static Long lastTimeSomeoneMoved;
    public static HashMap<AgentCognitif, List<Message>> communications;

    public static AgentCognitif agentsCognitifs[] = {
        new AgentCognitif(new Position(0, 3), new Position(0,0), '*'),
        new AgentCognitif(new Position(0, 2), new Position(0,2), 'o'),
        new AgentCognitif(new Position(1,2),  new Position(1,2),'Y'),
        new AgentCognitif(new Position(2, 3), new Position(2,3), '+'),
        new AgentCognitif(new Position(2, 4), new Position(2,4), '&'),
        new AgentCognitif(new Position(0, 1), new Position(0,1), '#'),
        new AgentCognitif(new Position(2, 0), new Position(3,3), '@'),
        new AgentCognitif(new Position(3, 1), new Position(4,3), '='),
        new AgentCognitif(new Position(4, 0), new Position(4,2), '$'),
        new AgentCognitif(new Position(4, 4), new Position(0,3), '%'),
        new AgentCognitif(new Position(1, 1), new Position(1,4), 'µ'),
        new AgentCognitif(new Position(2, 1), new Position(3,0), '?'),
        new AgentCognitif(new Position(3, 2), new Position(3,4), '!'),
        new AgentCognitif(new Position(4, 1), new Position(1,0), 'Z'),
        new AgentCognitif(new Position(1, 4), new Position(2,1), 'R'),
        new AgentCognitif(new Position(3, 3), new Position(4,4), 'M'),
        new AgentCognitif(new Position(1,3), new Position(3,1), 'P'),
        new AgentCognitif(new Position(4,2), new Position(1,1), 'F'),
        new AgentCognitif(new Position(2,2), new Position(4,0), 'H'),
        new AgentCognitif(new Position(0,4), new Position(0,4), 'K'),
    };


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("-------------------------------------------\n"
                + "Taquin Multi-Agents :\n"
                + "Réalisé par S. LHOPITAL & T. PRIEUR-DREVON - Automne 2017\n"
                + "-------------------------------------------");

        System.out.println("\nSelectionner la partie avec la laquelle vous souhaitez jouer : " +
                "\n- 1 : Partie 1 - Agents résolvant le problème avec Dijkstra " +
                "\n- 2 : Partie 2 - Agents qui communiquent entre eux");

        temp = sc.nextLine();

        switch (temp) {

            case "1":
                AgentDijkstra ad1 = new AgentDijkstra(new Position(0, 3), new Position(0,0), '*');
                AgentDijkstra ad2 = new AgentDijkstra(new Position(0, 2), new Position(0,2), 'o');
                AgentDijkstra ad3 = new AgentDijkstra(new Position(1,2),  new Position(1,2),'Y');
                AgentDijkstra ad4 = new AgentDijkstra(new Position(2, 3), new Position(2,3), '+');
                grid = new Grid(ad1, ad2, ad3, ad4);
                grid.resolve();
                break;

            case "2":

                System.out.println("\nAvec combien d'agents Cognitifs voulez-vous résoudre le puzzle (pas plus de 20) :");

                int nbAgents = Integer.parseInt(sc.nextLine());
                List<Agent> agents = new ArrayList<>();
                communications = new HashMap<>();

                for(int i = 0; i < nbAgents; i++) {
                    agents.add(agentsCognitifs[i]);
                    communications.put(agentsCognitifs[i], new ArrayList<>());
                }

                grid = new Grid(agents);
                grid.resolve();
                break;

            default:
                System.out.println("Saisie non reconnue."
                        + "\nAssurez-vous d'avoir saisie un chiffre (1 ou 2)\n");
        }
    }

    public static synchronized void iMoved() {
        lastTimeSomeoneMoved = System.currentTimeMillis();
    }

    /***
     * Add message to the queue
     * @param message
     */
    public static void sendMessage(Message message) {

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
