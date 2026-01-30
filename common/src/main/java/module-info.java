/**
 * Modulo contenente il codice condiviso da client e server
 */
module com.bookrecommender.common {
    requires java.rmi;
    requires java.sql;

    exports com.bookrecommender.common.dto;
    exports com.bookrecommender.common;
    exports com.bookrecommender.common.enums.auth;
    exports com.bookrecommender.common.enums.suggestion;
    exports com.bookrecommender.common.enums.library;
    exports com.bookrecommender.common.enums.rating;
    exports com.bookrecommender.common.extended_dto;
}