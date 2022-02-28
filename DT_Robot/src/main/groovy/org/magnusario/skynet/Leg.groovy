package org.magnusario.skynet

class Leg implements Runnable {

    final Robot robot
    final String identifier

    Leg(Robot robot, String identifier) {
        this.robot = robot
        this.identifier = identifier
    }

    @Override
    void run() {
        while(true) {
            try {
                while (robot.steps > 0) {
                    synchronized (robot) {
                        if (robot.lastStep == this)
                            continue
                        println "$identifier шагает"
                        robot.lastStep = this
                        robot.steps--
                    }
                }
                synchronized (robot) {
                    if (robot.steps <= 0)
                        robot.wait()
                }
            } catch(Exception exception) {
                println exception.getMessage()
                break
            }
        }
    }
}
