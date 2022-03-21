package org.firstinspires.ftc.teamcode;

public interface Holonomic {
    /**
     * Sets the direction you want the robot to move along.
     * @param course The course in radians, where 0 is forwards and {@link Math#PI}/2 is directly to the left.
     */
    void setCourse(double course);

    /**
     * Gets the direction the robot is supposed to be moving along.
     * @return The course in radians, where 0 is forwards and {@link Math#PI}/2 is directly to the left.
     */
    double getCourse();
}
