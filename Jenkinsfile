pipeline {
    agent any

    environment {
        GIT_REPO = 'https://github.com/Farrell354/calculator-apps.git'
        GIT_CREDENTIALS_ID = 'github-pat-credential'
        ANDROID_SDK_DIR = 'C:\\Android\\Sdk'
        JAVA_HOME = 'C:\\Program Files\\Android\\Android Studio\\jbr'
        PATH = "${env.ANDROID_SDK_DIR}\\cmdline-tools\\latest\\bin;${env.ANDROID_SDK_DIR}\\platform-tools;${env.PATH}"
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo 'Checkout kode dari GitHub...'
                git branch: 'main',
                    credentialsId: "${GIT_CREDENTIALS_ID}",
                    url: "${GIT_REPO}"
            }
        }

        stage('Install SDK Command-line Tools') {
            steps {
                script {
                    def sdkToolsZip = "${env.WORKSPACE}\\commandlinetools.zip"
                    // Cek apakah SDK folder ada
                    if (!fileExists("${env.ANDROID_SDK_DIR}\\cmdline-tools\\latest")) {
                        echo 'SDK command-line tools tidak ditemukan. Mengunduh...'
                        bat """
                        powershell -Command "Invoke-WebRequest -Uri https://dl.google.com/android/repository/commandlinetools-win-9477386_latest.zip -OutFile ${sdkToolsZip}"
                        mkdir "${env.ANDROID_SDK_DIR}\\cmdline-tools\\latest"
                        powershell -Command "Expand-Archive -Path ${sdkToolsZip} -DestinationPath ${env.ANDROID_SDK_DIR}\\cmdline-tools\\latest"
                        """
                    } else {
                        echo 'SDK command-line tools sudah ada.'
                    }
                }
            }
        }

        stage('Accept SDK Licenses') {
            steps {
                bat """
                echo y | "%ANDROID_SDK_DIR%\\cmdline-tools\\latest\\bin\\sdkmanager.bat" --licenses
                """
            }
        }

        stage('Build APK') {
            steps {
                echo 'Build APK dimulai...'
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
            echo 'Build APK gagal. Cek log untuk detail.'
        }
    }
}

