module com.bookrecommender.serverBR {
    requires java.rmi;
    requires java.sql;
    requires org.checkerframework.checker.qual;
    requires org.jetbrains.annotations;

    requires com.bookrecommender.common;

    exports com.bookrecommender.server;
}