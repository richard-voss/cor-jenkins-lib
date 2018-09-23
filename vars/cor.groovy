import de.fruiture.cor.jenkins.BuildVersionHelper


def release(steps) {
    return new BuildVersionHelper(steps).release()
}

def snapshot(steps) {
    return new BuildVersionHelper(steps).snapshot()
}
