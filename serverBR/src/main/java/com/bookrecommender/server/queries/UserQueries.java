package com.bookrecommender.server.queries;

import com.bookrecommender.common.dto.User;
import com.bookrecommender.server.DatabaseManager;
import org.intellij.lang.annotations.Language;

import java.util.LinkedList;

public class UserQueries {
    public synchronized static User getUserInfo(String userId) {
        @Language("PostgreSQL")
        String query = "SELECT * FROM \"UtentiRegistrati\" WHERE userid = ?";
        return DatabaseManager.getInstance().executeQuery(
            query,
            User::new,
            new Object[] {userId}
        ).getFirst();
    }
}
