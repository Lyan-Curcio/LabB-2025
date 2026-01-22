module com.bookrecommender.serverBR {
    requires java.rmi;
    requires java.sql;
    requires org.checkerframework.checker.qual;
    requires org.jetbrains.annotations;

    requires com.bookrecommender.common;
    requires io.github.cdimascio.dotenv.java;
    requires org.postgresql.jdbc;

    exports com.bookrecommender.server;
}