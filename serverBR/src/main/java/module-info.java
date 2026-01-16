module com.bookrecommender.server {
    requires com.bookrecommender.common;
    requires java.rmi;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires org.jetbrains.annotations;
    requires org.checkerframework.checker.qual;

    exports com.bookrecommender.server;
}