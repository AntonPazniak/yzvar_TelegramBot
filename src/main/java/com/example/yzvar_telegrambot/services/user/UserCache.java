package com.example.yzvar_telegrambot.services.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCache {

//    private final UserRepository userRepository;
//    private final Map<Long, User> userCache = new ConcurrentHashMap<>();
//
//    @PostConstruct
//    private void initCache() {
//        List<User> users = userRepository.findAll();
//        for (User user : users) {
//            userCache.put(user.getId(), user);
//        }
//    }
//
//    public User getById(Long id) {
//        return userCache.get(id);
//    }
//
//    public void refresh() {
//        userCache.clear();
//        initCache();
//    }
//
//    public void update(User user) {
//        userCache.put(user.getId(), user);
//    }
//
//    public void remove(Long id) {
//        userCache.remove(id);
//    }
}
