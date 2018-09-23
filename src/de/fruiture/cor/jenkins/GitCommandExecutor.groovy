package de.fruiture.cor.jenkins

class GitCommandExecutor implements Serializable {
    private final steps

    def git = "git"

    def batch = false

    GitCommandExecutor(steps) {
        this.steps = steps
    }

    def git(String gitCommand) {
        this.git = git
        this
    }

    def batch() {
        this.batch = true
        this
    }

    def shell() {
        this.batch = false
        this
    }

    String getOutput(String command) {
        if (batch)
            steps.bat(script: "@" + cmd(command), returnStdout: true)
        else
            steps.sh(script: cmd(command), returnStdout: true)
    }

    def run(String command) {
        if (batch)
            steps.bat(script: cmd(command))
        else
            steps.sh(script: cmd(command))
    }

    private GString cmd(String command) {
        "${git} ${command}"
    }
}
