pipeline {
    agent any

    environment {
        // Repo GitHub
        GIT_REPO = 'https://github.com/Farrell354/calculator-apps.git'
        GIT_CREDENTIALS_ID = 'github-pat-credential' // Pastikan sudah dibuat di Jenkins

        // Android SDK dan Java
        ANDROID_HOME = 'C:\\Users\\Farrel\\AppData\\Local\\Android\\Sdk'
        JAVA_HOME = 'C:\\Program Files\\Android\\Android Studio\\jbr'
        PATH = "${env.ANDROID_HOME}\\cmdline-tools\\latest\\bin;${env.ANDROID_HOME}\\platform-tools;${env.JAVA_HOME}\\bin;${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Checkout source code dari GitHub..."
                git branch: 'main',
                    credentialsId: "${GIT_CREDENTIALS_ID}",
                    url: "${GIT_REPO}"
            }
        }

        stage('Build APK') {
            steps {
                echo "Mulai build APK..."
                bat '.\\gradlew.bat clean assembleDebug --stacktrace --info'
            }
        }

        stage('Archive APK') {
            steps {
                echo "Archive hasil APK..."
                archiveArtifacts artifacts: 'app\\build\\outputs\\apk\\debug\\*.apk', allowEmptyArchive: false
            }
        }
    }

    post {
        success {
            echo '✅ Build APK berhasil!'
        }
        failure {
            echo '❌ Build APK gagal. Periksa log di atas untuk detail.'
        }
    }
}

