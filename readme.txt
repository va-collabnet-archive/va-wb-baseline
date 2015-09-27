mvn -B release:prepare -DreleaseVersion=4.1 -DdevelopmentVersion=4.2-SNAPSHOT -Dtag=4.1
mvn -B release:perform -Darguments="-DaltDeploymentRepository=maestro::default::https://va.maestrodev.com/archiva/repository/va-releases -Dmaven.deploy.skip=true"

Use SCP to push the files onto Archiva / Maestrodev