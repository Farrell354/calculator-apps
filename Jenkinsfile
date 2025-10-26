pipeline {
    agent any

    environment {
        APK_PATH = "app/build/outputs/apk/debug/app-debug.apk"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Farrell354/calculator-apps.git'
            }
        }

        stage('Build APK') {
            steps {
                echo "Building APK..."
                sh './gradlew clean assembleDebug'
            }
        }

        stage('Archive APK') {
            steps {
                archiveArtifacts artifacts: APK_PATH, allowEmptyArchive: false
                echo "APK archived successfully!"
            }
        }
    }
}
