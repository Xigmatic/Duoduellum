package xigmatic.me.dogfight.camera;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import xigmatic.me.dogfight.Dogfight;

import java.util.ArrayList;

public class CameraFunctions {
    private static int taskID = -1;
    private static int timeLeft;
    public CameraFunctions() {

    }


    /**
     * Forces the player to travel across a smooth path from one point to another within a certain amount of time (camera track)
     * 0 degrees yaw is facing toward the positive-z direction
     * -90 degrees yaw is facing toward the positive-x direction
     * @param players Player to see the camera track
     * @param startLoc Starting location
     * @param endLoc Ending Location
     * @param duration Duration of camera movement (in seconds)
     */
    public static void linearPan(ArrayList<Player> players, Location startLoc, Location endLoc, int duration) {
        // Checks if a task is already running (taskID of -1 means no task is running)
        if(taskID != -1)
            return;

        // The change in position/head tilt for each tick
        Vector positionVelocity = endLoc.toVector().subtract(startLoc.toVector()).multiply(1 / (duration * 20.0));
        float pitchVelocity = (endLoc.getPitch() - startLoc.getPitch()) / (duration * 20.0f);
        float yawVelocity = (endLoc.getYaw() - startLoc.getYaw()) / (duration * 20.0f);

        // Temporary location where the player teleports to that changes every tick
        Location teleportLoc = startLoc.clone();

        // Sets the ticks left to the amount of time specified (in seconds) and converts it into ticks
        timeLeft = 20 * duration;

        // Prevents the players from falling after every teleport
        for(Player player : players) {
            player.setGravity(false);
        }

        // Creates task that teleports the players every tick
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Dogfight.getPlugin(Dogfight.class), () -> {
            if(timeLeft == 0) {
                for(Player player : players) {
                    player.teleport(endLoc);
                }

                // Cancels task
                Bukkit.getScheduler().cancelTask(taskID);
                taskID = -1;
                return;
            }

            // Slightly changes position, pitch, and yaw by their respective velocities
            teleportLoc.add(positionVelocity);
            teleportLoc.setPitch(teleportLoc.getPitch() + pitchVelocity);
            teleportLoc.setYaw(teleportLoc.getYaw() + yawVelocity);

            // Teleports players to newly-moved location
            for(Player player : players) {
                player.teleport(teleportLoc);
            }

            // Decrements tick number
            timeLeft--;
        }, 1,0);
    }


    /**
     * Pans a camera around a point HORIZONTALLY
     * @param players Players to see the camera track
     * @param centerLoc Center of circle track
     * @param radius Size of circle track
     * @param startAngle Where the camera starts on the circle track (0 degrees is positive X axis)
     * @param endAngle Where the camera ends on the circle track (0 degrees is positive X axis)
     * @param faceCenter Player will face the center point if true
     * @param startPitch Starting pitch
     * @param endPitch Ending pitch
     * @param startYaw Starting yaw
     * @param endYaw Ending yaw
     * @param duration Duration of camera movement (in seconds)
     */
    public static void radialPan(ArrayList<Player> players, Location centerLoc, double radius, float startAngle, float endAngle, boolean faceCenter, float startPitch, float endPitch, float startYaw, float endYaw, int duration) {
        // Checks if a task is already running (taskID of -1 means no task is running)
        if(taskID != -1)
            return;

        // The change in position/head tilt for each tick
        float angleVelocity = (endAngle - startAngle) / (duration * 20.0f);
        float pitchVelocity = (endPitch - startPitch) / (duration * 20.0f);
        float yawVelocity = (endYaw - startYaw) / (duration * 20.0f);

        // Temporary location where the player teleports to that changes every tick
        Location teleportLoc = centerLoc.add(radius * Math.cos(Math.toRadians(startAngle)), 0, radius * Math.sin(Math.toRadians(startAngle)));
        teleportLoc.setPitch(startPitch);
        teleportLoc.setYaw(startAngle + startPitch + 90);

        // Sets the ticks left to the amount of time specified (in seconds) and converts it into ticks
        timeLeft = 20 * duration;

        // Sets the max number of ticks the camera movement will take
        float maxTicks = 20.0f * duration;

        // Prevents the player from falling after every teleport
        for(Player player : players) {
            player.setGravity(false);
        }

        // Creates task that teleports the player every tick
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Dogfight.getPlugin(Dogfight.class), () -> {
            if(timeLeft == 0) {
                // Cancels task
                Bukkit.getScheduler().cancelTask(taskID);
                taskID = -1;
                return;
            }

            // Radially moves the teleportLoc
            teleportLoc.setX(radius * Math.cos(Math.toRadians(startAngle + (angleVelocity * (maxTicks - timeLeft)))));
            teleportLoc.setZ(radius * Math.sin(Math.toRadians(startAngle + (angleVelocity * (maxTicks - timeLeft)))));

            // Sets player rotation depending on whether they face the center or not
            if(faceCenter) {
                teleportLoc.setPitch(teleportLoc.getPitch() + pitchVelocity);
                teleportLoc.setYaw(teleportLoc.getYaw() + angleVelocity + yawVelocity);
            } else {
                teleportLoc.setPitch(teleportLoc.getPitch() + pitchVelocity);
                teleportLoc.setYaw(teleportLoc.getYaw() + yawVelocity);
            }

            // Teleports players to newly-moved location
            for(Player player : players) {
                player.teleport(teleportLoc);
            }

            // Decrements tick number
            timeLeft--;
        }, 1,0);
    }
}
