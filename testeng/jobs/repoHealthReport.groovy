package testeng

def pytest_repo_health_gitURL = 'https://github.com/jinder1s/pytest-repo-health'
def edx_repo_health_gitURL = 'https://github.com/jinder1s/edx-repo-health'


job('repo-health-report') {

    description('Generate a report listing repository structure standard compliance accross edX repos')
    parameters {
        stringParam('GITHUB_REPO_URL', 'https://github.com/edx/edx-platform',
                    'Github repo url on which to run pytest-repo-health checks')
    }
    concurrentBuild(false)
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
        git {
            remote {
                url('\${GITHUB_REPO_URL}')
            }
            branch('*/master')
            browser()
            extensions {
                cleanAfterCheckout()
                relativeTargetDirectory('target_repo')
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