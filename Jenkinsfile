pipeline {
    agent any

    environment {
        GIT_REPO = 'https://github.com/Farrell354/calculator-apps.git'
        GIT_CREDENTIALS_ID = 'github-pat-credential'
        ANDROID_SDK_DIR = 'C:\\Android\\Sdk'
        JAVA_HOME = 'C:\\Program Files\\Android\\Android Studio\\jbr'
        PATH = "${env.ANDROID_SDK_DIR}\\platform-tools;${env.PATH}"
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', credentialsId: "${GIT_CREDENTIALS_ID}", url: "${GIT_REPO}"
            }
        }

        stage('Accept SDK Licenses') {
            steps {
                bat "\"${ANDROID_SDK_DIR}\\cmdline-tools\\latest\\tools\\bin\\sdkmanager.bat\" --licenses --verbose"
            }
        }

        stage('Build APK Debug') {
            steps {
                bat "gradlew.bat assembleDebug --stacktrace"
            }
        }

        stage('Archive APK Debug') {
            steps {
                archiveArtifacts artifacts: 'app\\build\\outputs\\apk\\debug\\*.apk', allowEmptyArchive: false
            }
        }
    }

    post {
        success {
            echo 'Build APK debug berhasil!'
        }
        failure {
            echo 'Build gagal. Cek log Jenkins.'
        }
    }
}





