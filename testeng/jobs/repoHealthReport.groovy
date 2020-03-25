package testeng

def pytest_repo_health_gitURL = 'https://github.com/jinder1s/pytest-repo-health'
def edx_repo_health_gitURL = 'https://github.com/jinder1s/edx-repo-health'
def parameter =     buildParameters(String parameter = "blah")

job('repo-health-report') {

    description('Generate a report listing repository structure standard compliance accross edX repos')
    concurrentBuild(false)
    environmentVariables(
            PYTHON_VERSION: 3.5,
        )
    multiscm {
        git {
            remote {
                url(pytest_repo_health_gitURL)
            }
            branch('*/msingh/adding_code')
            browser()
            extensions {
                cleanAfterCheckout()
                relativeTargetDirectory('pytest-repo-health')
            }
        }
        git {
            remote {
                url(edx_repo_health_gitURL )
            }
            branch('*/msingh/chekcs')
            browser()
            extensions {
                cleanAfterCheckout()
                relativeTargetDirectory('edx-repo-health')
            }
        }
    }
    triggers {
        cron('@midnight')
    }
    steps{
        shell(readFileFromWorkspace('testeng/resources/create-repo-health-report.sh'))
    }

}