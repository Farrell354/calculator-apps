pipeline {
    agent any

    environment {
        GIT_REPO = 'https://github.com/Farrell354/calculator-apps.git'
        GIT_CREDENTIALS_ID = 'github-pat-credential'

        ANDROID_SDK_DIR = 'C:\\Android\\Sdk'
        JAVA_HOME = 'C:\\Program Files\\Android\\Android Studio\\jbr'
        PATH = "${env.ANDROID_SDK_DIR}\\platform-tools;${env.PATH}"

        // Gradle cache
        GRADLE_USER_HOME = "${env.WORKSPACE}\\.gradle"

        // Keystore info (gunakan Jenkins Credentials untuk keamanan)
        KEYSTORE_PATH = 'C:\\Users\\Farrel\\keystore\\my-release-key.jks'
        KEYSTORE_ALIAS = 'myalias'
        KEYSTORE_PASSWORD = 'password_keystore'
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
                    def sdkZipPath = "${env.WORKSPACE}\\commandlinetools.zip"
                    def sdkExtractPath = "${env.ANDROID_SDK_DIR}\\cmdline-tools\\latest"

                    bat "if not exist \"${sdkExtractPath}\" mkdir \"${sdkExtractPath}\""

                    // Download jika belum ada
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

                    // Debug: list isi folder temp
                    bat "dir \"${tempExtract}\" /s /b"

                    // Pindahkan cmdline-tools ke sdkExtractPath
                    bat """
                    xcopy /E /I /Y "${tempExtract}\\cmdline-tools" "${sdkExtractPath}"
                    rmdir /S /Q "${tempExtract}"
                    """

                    // Deteksi sdkmanager.bat
                    def sdkManagerCandidates = ["${sdkExtractPath}\\tools\\bin\\sdkmanager.bat",
                                                "${sdkExtractPath}\\bin\\sdkmanager.bat"]
                    for (candidate in sdkManagerCandidates) {
                        if (fileExists(candidate)) {
                            env.SDKMANAGER_PATH = candidate
                            break
                        }
                    }

                    if (!env.SDKMANAGER_PATH) {
                        error "sdkmanager.bat tidak ditemukan!"
                    } else {
                        echo "Detected sdkmanager.bat at: ${env.SDKMANAGER_PATH}"
                    }
                }
            }
        }

        stage('Accept SDK Licenses') {
            steps {
                bat "echo y | \"%SDKMANAGER_PATH%\" --licenses"
            }
        }

        stage('Build APK Debug') {
            steps {
                bat "gradlew.bat assembleDebug --stacktrace --info"
            }
        }

        stage('Archive APK Debug') {
            steps {
                archiveArtifacts artifacts: 'app\\build\\outputs\\apk\\debug\\*.apk', allowEmptyArchive: false
            }
        }

        stage('Build APK Release') {
            steps {
                // Set environment untuk signing
                bat """
                set ORG_GRADLE_PROJECT_STORE_FILE=${KEYSTORE_PATH}
                set ORG_GRADLE_PROJECT_KEY_ALIAS=${KEYSTORE_ALIAS}
                set ORG_GRADLE_PROJECT_STORE_PASSWORD=${KEYSTORE_PASSWORD}
                set ORG_GRADLE_PROJECT_KEY_PASSWORD=${KEYSTORE_PASSWORD}
                gradlew.bat assembleRelease --stacktrace --info
                """
            }
        }

        stage('Archive APK Release') {
            steps {
                archiveArtifacts artifacts: 'app\\build\\outputs\\apk\\release\\*.apk', allowEmptyArchive: false
            }
        }
    }

    post {
        success {
            echo 'Build APK debug & release berhasil!'
        }
        failure {
            echo 'Build gagal. Periksa log Jenkins untuk detail.'
        }
    }
}




