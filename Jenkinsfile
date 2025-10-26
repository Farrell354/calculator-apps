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
                echo 'Mulai checkout repository...'
                git branch: 'main',
                    credentialsId: "${GIT_CREDENTIALS_ID}",
                    url: "${GIT_REPO}"
            }
        }

        stage('Build APK') {
            steps {
                echo 'Mulai build APK...'
                // Jalankan gradlew.bat dari workspace penuh
                bat "\"${WORKSPACE}\\gradlew.bat\" clean assembleDebug --refresh-dependencies --stacktrace --info"
            }
        }

        stage('Archive APK') {
            steps {
                echo 'Archive APK hasil build...'
                archiveArtifacts artifacts: 'app\\build\\outputs\\apk\\debug\\*.apk', allowEmptyArchive: false
            }
        }
    }

    post {
        success {
            echo 'Build APK berhasil!'
        }
        failure {
            echo 'Build APK gagal. Periksa log Gradle untuk detail.'
        }
    }
}
