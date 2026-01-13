module com.lab_b.server {
    requires com.lab_b.common;
    requires java.rmi;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires org.jetbrains.annotations;
    requires org.checkerframework.checker.qual;

    exports com.lab_b.server;
}