pipeline {
    agent any

    environment {
        GIT_REPO = 'https://github.com/Farrell354/calculator-apps.git'
        GIT_CREDENTIALS_ID = 'github-pat-credential'
        ANDROID_HOME = 'C:\\Users\\Farrel\\AppData\\Local\\Android\\Sdk'
        JAVA_HOME = 'C:\\Program Files\\Android\\Android Studio\\jbr'
        PATH = "${env.ANDROID_HOME}\\cmdline-tools\\latest\\bin;${env.ANDROID_HOME}\\platform-tools;${env.PATH}"
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    credentialsId: "${GIT_CREDENTIALS_ID}",
                    url: "${GIT_REPO}"
            }
        }

        stage('Accept SDK Licenses') {
            steps {
                echo "Memastikan semua lisensi SDK diterima..."
                bat "\"${env.ANDROID_HOME}\\cmdline-tools\\latest\\bin\\sdkmanager.bat\" --licenses --quiet || exit 0"
            }
        }

        stage('Build APK') {
            steps {
                echo "Mulai build APK..."
                bat 'gradlew.bat clean assembleDebug'
            }
        }

        stage('Archive APK') {
            steps {
                echo "Archiving APK..."
                archiveArtifacts artifacts: 'app\\build\\outputs\\apk\\debug\\*.apk', allowEmptyArchive: false
            }
        }
    }

    post {
        success {
            echo '✅ Build APK berhasil! APK bisa di-download di Jenkins artifacts.'
        }
        failure {
            echo '❌ Build APK gagal! Cek log untuk detail error.'
        }
    }
}


