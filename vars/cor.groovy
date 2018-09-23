@Grab('de.fruiture.cor:cor-jenkins:1.0.1')
import de.fruiture.cor.jenkins.VersionCalculator

def release() {
	return VersionCalculator.release("v")
}

def snapshot() {
	return VersionCalculator.snapshot("v")
}
