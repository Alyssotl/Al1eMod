package codes.alyssotl.al1e.commons;

import java.util.Timer;

public class PlayerData {
    /*
     * The name of the player
     */
    public String name = "";

    /*
     * If the player is currently dead
     */
    public boolean dead = false;

    /*
     * The duration the player has been dead for
     */
    public int deadFor = 0; // Seconds

    /*
     * If the player is currently eliminated
     */
    public boolean eliminated = false;

    /*
     * The dead timer of the player
     */
    public Timer deadTimer = new java.util.Timer();

    /*
     * Kill the player
     */
    public void respawn() {
        if (dead) return; // Already dead
        dead = true;
        deadTimer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                deadFor++;
                System.out.println("Player " + name + " " + deadFor + " seconds dead");

                if (deadFor >= 5) {
                    dead = false;
                    deadFor = 0;
                    System.out.println("Player " + name + " has respawned");
                    deadTimer.cancel();
                    deadTimer = new java.util.Timer();
                }
            }
        }, 1000, 1000);
    }

    /*
     * Constructs a new player data
     */
    public PlayerData(String name) {
        this.name = name;
    }
}
