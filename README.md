
---
#  ProjectEagleVertx Configurations
---
#
#
#
#
#

## Import into Intellij IDE
---
1. Clone this repo.

2. Open project to Intellij
    - In Intellij under FILE > NEW > PROJECT FROM EXISTING SOURCES
    - Find the directory where you clone the project to and select the folder.
    - This is a Maven project. Select "Import project from external model" and select Maven. 
    - Continue to next windows until you have to select project SDK. You want to then select your JDK jdk1.8.0_102 home path if not already set.
    - Continue on until finished...
    - Project is now loaded into Intellij.
    
3. Make sure all the maven dependencies are imported.
    - If Maven dependencies need to be imported then right click on pom.xml file and go to MAVEN > REIMPORT.

4. You project should be set up to run now. You still need to add your own Couchbase or Postgres server as you can see in the below section.     

## Database Options
---------------------

##### CouchBase Community Server

1. Download CouchBase Community Server

    https://www.couchbase.com/downloads/thankyou/community?product=couchbase-server&version=6.0.0&platform=windows&addon=false&beta=false

2. Open CouchBase Admin Console in a web browser.

3. Check the project DBConfig file.
    - Match the username, password to the Admin Console.
    - Create bucket with the name from DBConfig file.
    - Create a primary index: (CREATE PRIMARY INDEX `employees_index` ON `Employees`)
    
4. Add data to bucket.

#
#
# --- OR ---
#
#

##### Postgres

1. Download your choice of Postgres local server.
2. Create 'Employees' table with columns name and type:
    - empid (character varying)
    - employee (jsonb)
3. Add data to table.

## Kafka 
---
*** If you dont want to run Kafka yet, it can be disabled by changing producer flag to 0 and to 1 to be enabled. ***

You can run the Kafka server locally by downloading Kafka 2.3.0.
https://kafka.apache.org/downloads

Local Kafka Server

- Follow these instructions. Stop at step 3. 
- https://kafka.apache.org/quickstart

#
#
# --- OR ---
#
#

You can create a remote Kafka server through aiven. (Free Trial 30 days).
https://console.aiven.io/index.html

Aiven

- Go to the Java keystore management section and follow that in the below link. This is intructions to generate keystores and truststores.
- https://help.aiven.io/articles/489572-getting-started-with-aiven-kafka






