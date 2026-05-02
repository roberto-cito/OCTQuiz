package com.oct.octquiz;

import com.oct.octquiz.Model.User.CustomUserDetails;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserStats {

    private final SessionRegistry sessionRegistry;

    public UserStats(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    public List<String> getUsersOnline(String username) {
        List<String> names = new ArrayList<>();
        for (Object u : sessionRegistry.getAllPrincipals()) {
            if (!sessionRegistry.getAllSessions(u, false).isEmpty()) {
                CustomUserDetails user = (CustomUserDetails) u;
                if(user.getUsername().equals(username)) continue;
                names.add(user.getUsername());
            }
        }
        return names;
    }
}
