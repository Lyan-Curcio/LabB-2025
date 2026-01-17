module com.bookrecommender.common {
    exports com.bookrecommender.common;
    exports com.bookrecommender.common.dto;
    exports com.bookrecommender.common.enums.auth;
    exports com.bookrecommender.common.enums.library;
    requires java.rmi;
    requires java.sql;
}