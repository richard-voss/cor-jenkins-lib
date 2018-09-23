# Shared Library for Continuous Releasing with Jenkins Pipeline

Allows releasing a reasonable next version number in a Jenkins Pipeline with just a few lines of code.

This is a wrapper around [cor-jenkins](https://github.com/richard-voss/cor-jenkins)
that can be directly used as [shared library](https://jenkins.io/doc/book/pipeline/shared-libraries/)
in Jenkins.

All you need to prepare is add this repository as *Shared Library* to Jenkins
in order to use it in a pipeline script:

```groovy
library 'cor-jenkins-lib'

node {
  def vc = cor.release()
   
  vc.tags(
    sh(script: "git ${vc.gitFindTagsCommand}", returnStdout: true)
  )
  
  vc.messages(
    sh(script: "git ${vc.gitLogCommand}", returnStdout: true)
  )
  
  def version = vc.nextVersion
  
  // the actual build, for example using maven
  sh("mvn versions:set -DnewVersion=${version} versions:commit")
  sh("mvn clean verify")
  
  sh("git ${vc.gitNextTagCommand}")
  sh("git push --tags")
}
```