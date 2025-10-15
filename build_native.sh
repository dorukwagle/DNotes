
mvn clean package

java -agentlib:native-image-agent=config-output-dir=META-INF/native-image -jar target/DNotes-1.0.0.jar


native-image -jar target/DNotes-1.0.0.jar \
-H:ConfigurationFileDirectories=META-INF/native-image \
-H:Name=DNotes \
--no-fallback \
--enable-all-security-services \
--report-unsupported-elements-at-runtime \
-H:+AddAllCharsets \
-H:EnableURLProtocols=http,https \
-H:IncludeResources='.*\.css|.*\.png|.*\.ttf|.*\.fxml|org/sqlite/native/Linux/x86_64/libsqlitejdbc.so|org/sqlite/native/Linux/x86_64/libsqlite3.so' \
-cp $HOME/.m2/repository/org/xerial/sqlite-jdbc/3.50.2.0/sqlite-jdbc-3.50.2.0.jar
