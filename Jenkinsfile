pipeline {
    agent { label 'hlg' }
    options {
        buildDiscarder(logRotator(numToKeepStr:'5'))
    }
    stages {
        stage('Build') {
            steps {
                script {
                    properties([
                        parameters([
                            choice(name: 'produto', choices: [
                                'Cartao de Credito'], description: ''),
                            string(name: 'nome', defaultValue: '')
                        ])
                    ])
                }
            }
        }
        stage('Feature') {
            steps {
                sh "sed -i 's/palavra/"+params.produto+"/g' src/test/resources/features/test.feature"
                sh 'cat src/test/resources/features/test.feature'
            }
        }
        stage('Test') {
            steps {
                sh './gradlew cucumber -P tags='+params.tags
            }
        }
    }
}
