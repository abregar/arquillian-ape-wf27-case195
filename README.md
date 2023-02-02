# arquillian-ape-wf27-case195
arquillian ape reproducible case on dbunit rollback - does not work with wf27

### setup

Requires h2 db started in **server** mode. This way persistence survives the test and shows 
the unexpected state after IT test(s) are completed. If required, I may update this Readme on details how I have started the h2 database in server mode.


Then, wildfly 27 server is started as container-remote and must be manually updated on h2 connection string, to connect h2 started previously, like:
```
<datasource jndi-name="java:jboss/datasources/ExampleDS" pool-name="ExampleDS" enabled="true" use-java-context="true" statistics-enabled="${wildfly.datasources.statistics-enabled:${wildfly.statistics-enabled:false}}">
    <connection-url>jdbc:h2:tcp://localhost/~/test;MODE=${wildfly.h2.compatibility.mode:REGULAR}</connection-url>
    <driver>h2</driver>
    <security>
        <user-name>sa</user-name>
    </security>
</datasource>
```

Then create the test table manually in from the h2 console:
```
CREATE TABLE TEST(
    id INT PRIMARY KEY,
    name VARCHAR);
```

To maven package this test case, two specifically updated dependant project branches are required to 
mvn install locally (version is set to non-SNAPSHOT for exact match)
```
https://github.com/abregar/arquillian-extension-transaction/tree/arquillian_core_1.7.0.alpha13

https://github.com/abregar/arquillian-extension-persistence/tree/arquillian_core_1.7.0.alpha13
```
 

| :exclamation:  NOTE: those two project are really limited draft, fixed only in parts where support working environment for this specific use case. Need some more love to promote into PR ... |
|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|

Then start the standalone Wildfly 27 and run the test **should_populate_and_rollback_data**

After the test, state in db is not as expected - prepopulated rows remain in the table.
