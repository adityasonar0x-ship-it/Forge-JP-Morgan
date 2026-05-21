package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.entity.UserRecord;
import org.springframework.stereotype.Component;

@Component
public class BalanceQuerier {
    private final UserRepository userRepository;

    public BalanceQuerier(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Balance query(Long userId) {
        UserRecord user = userRepository.findById(userId);
        if (user == null) {
            return new Balance(0);
        }
        return new Balance(user.getBalance());
    }
}
