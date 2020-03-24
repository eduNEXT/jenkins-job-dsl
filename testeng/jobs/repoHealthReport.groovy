package testeng

import static

def pytest_repo_health_gitURL = 'https://github.com/jinder1s/pytest-repo-health'
def edx_repo_health_gitURL = 'https://github.com/jinder1s/edx-repo-health'

job('repo-health-report') {

    description('Generate a report listing repository structure standard compliance accross edX repos')
    concurrentBuild(false)
    label('jenkins-worker')
    scm {
        git {
            remote {
                url(pytest_repo_health_gitURL)
            }
            branch('*/msingh/adding_code')
            browser()
        }
    }
    scm{
        git {
            remote {
                url(edx_repo_health_gitURL )
            }
            branch('*/msingh/chekcs')
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