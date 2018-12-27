node {
	def dockerImage = "";

    stage ('Clone') {
        checkout([$class: 'GitSCM', branches: [[name: '**']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'STASH_ACCOUNT', url: 'http://localhost/git/todo.git']]])
    }

    stage ('Gradle clean') {
        sh'''
        ./gradlew clean
        '''
    }

    stage ('Gradle test') {
        sh'''
        ./gradlew build test jacocoTestReport
        '''
    }

    stage ('Generate HTML Reports') {
        publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'build/reports/checkstyle/', reportFiles: 'main.html', reportName: 'Checkstyle HTML Report', reportTitles: ''])

        publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'build/reports/tests/test/', reportFiles: 'index.html', reportName: 'JUnit HTML Report', reportTitles: ''])

        publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'build/reports/jacoco/test/html/', reportFiles: 'index.html', reportName: 'Jacoco HTML Report', reportTitles: ''])

    }

    stage ('Gradle bootJar') {
        sh'''
        ./gradlew bootJar
        '''
        fileExists './build/libs/application-1.0.jar'
    }

    stage ('Docker Build') {
        dockerImage = docker.build('todo/backend')
    }
}
