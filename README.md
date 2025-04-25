Build WAR file
If using Maven:
mvn clean package
WAR file will be created under target/.
2. Transfer WAR to EC2
scp -i "path/to/mysql_key.pem" target/cs370_s25fileuploadServlet-rahman-shiwlee.war ubuntu@<EC2-PUBLIC-IP>:/home/ubuntu/
3. Deploy to Tomcat
ssh -i "path/to/mysql_key.pem" ubuntu@<EC2-PUBLIC-IP>
sudo mv /home/ubuntu/cs370_s25fileuploadServlet-rahman-shiwlee.war /opt/tomcat/webapps/
sudo /opt/tomcat/bin/shutdown.sh
sleep 2
sudo /opt/tomcat/bin/startup.sh
4. Access in Browser
http://3.145.65.29:8080/cs370_s25fileuploadServlet-rahman-shiwlee/index.html
________________________________________
 Database Table Structure
CREATE TABLE uploaded_files (
  id INT AUTO_INCREMENT PRIMARY KEY,
  file_name VARCHAR(255),
  file_content LONGBLOB
);
________________________________________
MySQL SSL Connection
Ensured MySQL server has SSL enabled and the connection URL includes:
jdbc:mysql://<host>:3306/<db>?useSSL=true&requireSSL=true
________________________________________
Student: Shiwlee Rahman
Course: CS370 (Software Engineering)
Semester: Spring 2025


