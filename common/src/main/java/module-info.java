module com.bookrecommender.common {
    exports com.bookrecommender.common;
    exports com.bookrecommender.common.dto;
    exports com.bookrecommender.common.enums.auth;
    exports com.bookrecommender.common.enums.library;
    exports com.bookrecommender.common.enums.rating;
    exports com.bookrecommender.common.enums.suggestion;
    requires java.rmi;
    requires java.sql;
}