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
                git branch: 'main', 
                    credentialsId: "${GIT_CREDENTIALS_ID}", 
                    url: "${GIT_REPO}"
            }
        }

        stage('Install SDK Command-line Tools') {
            steps {
                script {
                    def sdkZipPath = "${env.WORKSPACE}\\commandlinetools.zip"
                    def sdkExtractPath = "${env.ANDROID_SDK_DIR}\\cmdline-tools\\latest"

                    // Buat folder SDK jika belum ada
                    bat "if not exist \"${env.ANDROID_SDK_DIR}\" mkdir \"${env.ANDROID_SDK_DIR}\""
                    bat "if not exist \"${sdkExtractPath}\" mkdir \"${sdkExtractPath}\""

                    // Download command-line tools jika belum ada
                    bat """
                    if not exist "${sdkZipPath}" (
                        powershell -Command "Invoke-WebRequest -Uri 'https://dl.google.com/android/repository/commandlinetools-win-9477386_latest.zip' -OutFile '${sdkZipPath}'"
                    )
                    """

                    // Extract ZIP ke folder temporary
                    def tempExtract = "${env.WORKSPACE}\\temp_sdk"
                    bat "if exist \"${tempExtract}\" rmdir /s /q \"${tempExtract}\""
                    bat "mkdir \"${tempExtract}\""
                    bat """
                    powershell -Command "Expand-Archive -Path '${sdkZipPath}' -DestinationPath '${tempExtract}' -Force"
                    """

                    // Pindahkan folder cmdline-tools ke sdkExtractPath
                    bat """
                    xcopy /E /I /Y "${tempExtract}\\cmdline-tools" "${sdkExtractPath}"
                    rmdir /S /Q "${tempExtract}"
                    """

                    // Deteksi lokasi sdkmanager.bat
                    def sdkManagerCandidates = ["${sdkExtractPath}\\tools\\bin\\sdkmanager.bat",
                                                "${sdkExtractPath}\\bin\\sdkmanager.bat"]
                    for (candidate in sdkManagerCandidates) {
                        if (fileExists(candidate)) {
                            env.SDKMANAGER_PATH = candidate
                            break
                        }
                    }

                    if (!env.SDKMANAGER_PATH) {
                        error "sdkmanager.bat tidak ditemukan di folder SDK!"
                    }
                }
            }
        }

        stage('Accept SDK Licenses') {
            steps {
                bat "echo y | \"%SDKMANAGER_PATH%\" --licenses"
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
            echo 'Build APK gagal. Periksa log Jenkins untuk detail.'
        }
    }
}

