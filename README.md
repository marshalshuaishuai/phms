# phms
一个免费开源的个人健康信息管理系统

## application.properties配置
####数据库配置
spring.datasource.url=jdbc:mysql://your_server:3306/PhmsDb?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.initialization-mode=always
spring.datasource.data=classpath:data-mysql.sql
spring.jpa.database=mysql
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update

####服务端口配置
server.port=8000
server.servlet.context-path=/phms

####邮件发送配置
spring.mail.host=your_host
spring.mail.username=your_email_addr
spring.mail.password=your_password
