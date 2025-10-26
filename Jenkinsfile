pipeline {
    agent any

    environment {
        ANDROID_SDK_DIR = 'C:\\Android\\Sdk'
        JAVA_HOME = 'C:\\Program Files\\Android\\Android Studio\\jbr'
        PATH = "${env.ANDROID_SDK_DIR}\\platform-tools;${env.PATH}"
    }

    stages {
        stage('Debug SDK') {
            steps {
                bat "dir \"${env.ANDROID_SDK_DIR}\" /s /b"
                bat "echo JAVA_HOME=%JAVA_HOME%"
                bat "echo PATH=%PATH%"
            }
        }
    }
}



