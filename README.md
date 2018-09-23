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
  def versionHelper = cor.release(this)
  
  def version = versionHelper.determineNextVersion()
  
  // the actual build, for example using maven
  sh("mvn versions:set -DnewVersion=${version} versions:commit")
  sh("mvn clean verify")
  
  versionHelper.tag(true)
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

## Configuring the VersionHelper

You can adjust the behaviour of the VersionHelper, but most is 
only useful _before_ calling `determineNextVersion()`.

See [cor-jenkins](https://github.com/richard-voss/cor-jenkins)
for details on these features:

* `versionHelper.snapshot()` will switch to the snapshot strategy
* `versionHelper.release()` will switch to the release strategy
* `versionHelper.prefix("foo-")` will set the prefix used for git tag names
* `versionHelper.triggerMinorChange([/compatible update/])` will configure they keywords for minor version changes
* `versionHelper.triggerMajorChange([/breaking change/])` will configure they keywords for major version changes
