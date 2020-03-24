package testeng

import static org.edx.jenkins.dsl.JenkinsPublicConstants.JENKINS_PUBLIC_LOG_ROTATOR

job('repo-health-report') {

    description('Generate a report listing repository structure standard compliance accross edX repos')
    logRotator JENKINS_PUBLIC_LOG_ROTATOR()
    concurrentBuild(false)
    label('jenkins-worker')
    scm {
        git {
            remote {
                url('https://github.com/jinder1s/pytest-repo-health')
            }
            branch('*/master')
            browser()
        }
    }
    scm{
        git {
            remote {
                url('hhttps://github.com/jinder1s/edx-repo-health')
            }
            branch('*/master')
            browser()
        }
    }
    triggers {
        cron('@midnight')
    }
    wrappers {
        timestamps()
        sshAgent('jenkins')
        credentialsBinding {
           string('OEP_REPORT_TOKEN', 'GITHUB_STATUS_OAUTH_TOKEN')
        }
    }
    steps {
        virtualenv {
            name('repo-health-venv')
            nature('shell')
            clear(true)
            command(readFileFromWorkspace('testeng/resources/create-oep-report.sh'))
        }
    }
    publishers {
        archiveJunit('oep2-report.xml') {
            retainLongStdout(false)
        }
        publishHtml {
            report('.') {
                allowMissing(true)
                alwaysLinkToLastBuild(false)
                keepAll(true)
                reportFiles('oep2.report.html')
                reportName('OEP 2 Report')
            }
        }
    }

}