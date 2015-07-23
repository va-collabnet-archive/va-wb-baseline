mvn -B release:prepare -DreleaseVersion=4.0 -DdevelopmentVersion=4.1-SNAPSHOT -Dtag=4.0
mvn -B release:perform -Darguments="-DaltDeploymentRepository=maestro::default::https://va.maestrodev.com/archiva/repository/va-releases -Dmaven.deploy.skip=true"

Use SCP to push the files onto Archiva / Maestrodev