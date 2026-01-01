# استخدم JDK 17 خفيف الحجم
FROM eclipse-temurin:17-jdk-alpine

# مجلد العمل داخل الحاوية
WORKDIR /app

# نسخ Jar
COPY target/user-service-0.0.1-SNAPSHOT.jar app.jar

# بورت الخدمة
EXPOSE 8081

# أمر التشغيل
ENTRYPOINT ["java","-jar","app.jar"]
