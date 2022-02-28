package org.magnusario.skynet

class Robot {
    Leg leftLeg = new Leg(this, "Левая нога")
    Leg rightLeg = new Leg(this, "Правая нога")
    Leg lastStep = null

    volatile int steps = 0

    void start() {
        new Thread(leftLeg).start()
        new Thread(rightLeg).start()
    }

    void walk(int steps) {
        this.steps = steps
        synchronized (this) {
            this.notifyAll()
        }
    }
}
