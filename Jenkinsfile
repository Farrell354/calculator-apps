pipeline {
    agent any

    environment {
        GIT_REPO = 'https://github.com/Farrell354/calculator-apps.git'
        GIT_CREDENTIALS_ID = 'github-pat-credential'
        ANDROID_HOME = 'C:\\Users\\Farrel\\AppData\\Local\\Android\\Sdk'
        JAVA_HOME = 'C:\\Program Files\\Android\\Android Studio\\jbr'
        PATH = "${env.ANDROID_HOME}\\cmdline-tools\\latest\\bin;${env.ANDROID_HOME}\\platform-tools;${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', credentialsId: "${GIT_CREDENTIALS_ID}", url: "${GIT_REPO}"
            }
        }

        stage('Accept SDK Licenses') {
            steps {
                bat """echo y | "%ANDROID_HOME%\\cmdline-tools\\latest\\bin\\sdkmanager.bat" --licenses"""
            }
        }

        stage('Build APK') {
            steps {
                bat "gradlew.bat clean assembleDebug --refresh-dependencies --stacktrace --info"
            }
        }

        stage('Archive APK') {
            steps {
                archiveArtifacts artifacts: 'app\\build\\outputs\\apk\\debug\\*.apk', allowEmptyArchive: false
            }
        }
    }

    post {
        success {
            echo 'Build APK berhasil!'
        }
        failure {
            echo 'Build APK gagal! Periksa log Gradle.'
        }
    }
}
