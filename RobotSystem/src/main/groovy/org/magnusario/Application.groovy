package org.magnusario

import org.magnusario.skynet.Robot

class Application {

    static void main(String[] args) {
        Robot robot = new Robot()
        robot.start()
        robot.walk(10)
        Thread.sleep(3000)
        robot.walk(15)
    }
}
