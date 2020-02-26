package testeng

import static org.edx.jenkins.dsl.JenkinsPublicConstants.JENKINS_PUBLIC_LOG_ROTATOR
import static org.edx.jenkins.dsl.JenkinsPublicConstants.GENERAL_SLACK_STATUS

job('scan-plugin-updates') {

    description(
        'Scan the plugins installed on this Jenkins instance for security related updates'
    )
    authorization {
        blocksInheritance(true)
        permissionAll('edx*testeng')
    }

    logRotator JENKINS_PUBLIC_LOG_ROTATOR()
    label('master')
    concurrentBuild(false)

    scm {
        git {
            remote {
                url('https://github.com/edx/jenkins-configuration.git')
            }
            branch('*/master')
            extensions {
                relativeTargetDirectory('jenkins-configuration')
            }
        }
    }

    wrappers {
        credentialsBinding {
            string('DEVOPS_SUPPORT_EMAIL', 'DEVOPS_SUPPORT_EMAIL_ACCOUNT')
        }
        timestamps()
    }

    triggers {
        cron('H H * * *')
    }

    steps {
        systemGroovyScriptFile('jenkins-configuration/scripts/scanPluginUpgrades.groovy')
    }

    publishers {
        extendedEmail {
            recipientList('$DEVOPS_SUPPORT_EMAIL')
            triggers {
                failure {
                    attachBuildLog(false)  
                    compressBuildLog(false)
                    subject('Build Jenkins Security updates: ${JOB_NAME}')
                    content('Security updates for plugins are available on Build jenkins . \n\nSee ${BUILD_URL} for details.')
                    contentType('text/plain')
                    sendTo {
                        recipientList()
                    }
                }
            }
        }
    }
}
