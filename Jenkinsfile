pipeline {
    agent any

    environment {
        GIT_REPO = 'https://github.com/Farrell354/calculator-apps.git'
        GIT_CREDENTIALS_ID = 'github-pat-credential' // gunakan Personal Access Token GitHub
        ANDROID_HOME = 'C:\\Users\\Farrel\\AppData\\Local\\Android\\Sdk'
        JAVA_HOME = 'C:\\Program Files\\Android\\Android Studio\\jbr'
        PATH = "${env.ANDROID_HOME}\\cmdline-tools\\latest\\bin;${env.ANDROID_HOME}\\platform-tools;${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checkout kode dari GitHub...'
                git branch: 'main',
                    credentialsId: "${GIT_CREDENTIALS_ID}",
                    url: "${GIT_REPO}"
            }
        }

        stage('Accept SDK Licenses') {
            steps {
                echo 'Menerima Android SDK licenses...'
                bat """
                echo y | sdkmanager.bat --licenses
                """
            }
        }

        stage('Build APK') {
            steps {
                echo 'Mulai build APK...'
                bat """
                gradlew.bat clean assembleDebug --refresh-dependencies --stacktrace --info
                """
            }
        }

        stage('Archive APK') {
            steps {
                echo 'Mengarsipkan APK...'
                archiveArtifacts artifacts: 'app\\build\\outputs\\apk\\debug\\*.apk', allowEmptyArchive: true
            }
        }
    }

    post {
        success {
            echo 'Build berhasil!'
        }
        failure {
            echo 'Build gagal! Cek log Gradle untuk detail error.'
        }
    }
}
