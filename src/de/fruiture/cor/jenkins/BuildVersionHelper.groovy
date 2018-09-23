package de.fruiture.cor.jenkins

@Grab('de.fruiture.cor:cor-jenkins:1.1.0')
class BuildVersionHelper implements Serializable {
    final GitCommandExecutor git

    boolean snapshot = false
    String prefix = "v"

    def triggerMinorChange
    def triggerMajorChange

    VersionCalculator calculator


    BuildVersionHelper(steps) {
        git = new GitCommandExecutor(steps)
    }

    def prefix(String prefix) {
        this.prefix = prefix
        this
    }

    def snapshot() {
        snapshot = true
        this
    }

    def release() {
        snapshot = false
        this
    }

    def triggerMinorChange(list) {
        triggerMinorChange = list
        this
    }

    def triggerMajorChange(list) {
        triggerMajorChange = list
        this
    }

    def newCalculator() {
        def c = snapshot ?
                VersionCalculator.snapshot(prefix) :
                VersionCalculator.release(prefix)

        if (triggerMinorChange)
            c.setTriggerMinorChange(triggerMinorChange)
        if (triggerMajorChange)
            c.setTriggerMajorChange(triggerMajorChange)

        return c
    }

    String determineNextVersion() {
        this.calculator = newCalculator()

        calculator.tags(git.getOutput(calculator.gitFindTagsCommand))
        calculator.messages(git.getOutput(calculator.gitLogCommand))

        return calculator.nextVersion
    }

    def getNextVersion() {
        return calculator.nextVersion
    }

    void tag(push = false) {
        git.run(calculator.gitNextTagCommand)
        if (push) {
            pushTag()
        }
    }

    void pushTag() {
        git.run(calculator.gitPushTagCommand)
    }
}
