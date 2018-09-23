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

## Configure Shared Library

If you have a minute, read the [excellent documentation](https://jenkins.io/doc/book/pipeline/shared-libraries/),
if you don't:

0. Go to *Manage Jenkins* > *Configure System*
0. Look for *Global Pipeline Libraries* and press *Add*
0. Enter `cor-jenkins-lib` as name
0. Enter `master` (always up to date) or any specific version as default version
0. Tick *Modern SCM* and then *Git* as retrieval option
0. Enter `https://github.com/richard-voss/cor-jenkins-lib.git` as URL, no credentials
0. *Save*

Now the `library 'cor-jenkins-lib'` statement will work in every pipeline.
