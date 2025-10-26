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

        stage('Install SDK Command-line Tools') {
            steps {
                script {
                    def sdkZip = "${env.WORKSPACE}\\commandlinetools.zip"
                    def sdkLatest = "${env.ANDROID_SDK_DIR}\\cmdline-tools\\latest"

                    bat "if not exist \"${sdkLatest}\" mkdir \"${sdkLatest}\""

                    // Download jika belum ada
                    bat """
                    if not exist "${sdkZip}" (
                        powershell -Command "Invoke-WebRequest -Uri 'https://dl.google.com/android/repository/commandlinetools-win-9477386_latest.zip' -OutFile '${sdkZip}'"
                    )
                    """

                    // Extract ke temporary folder
                    def tempExtract = "${env.WORKSPACE}\\temp_sdk"
                    bat "if exist \"${tempExtract}\" rmdir /s /q \"${tempExtract}\""
                    bat "mkdir \"${tempExtract}\""
                    bat """
                    powershell -Command "Expand-Archive -Path '${sdkZip}' -DestinationPath '${tempExtract}' -Force"
                    """

                    // Pindahkan isi cmdline-tools ke folder latest (hilangkan nested)
                    bat """
                    xcopy /E /I /Y "${tempExtract}\\cmdline-tools\\*" "${sdkLatest}"
                    rmdir /S /Q "${tempExtract}"
                    """
                }
            }
        }

        stage('Accept SDK Licenses') {
            steps {
                bat "\"${env.ANDROID_SDK_DIR}\\cmdline-tools\\latest\\bin\\sdkmanager.bat\" --licenses --verbose"
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
