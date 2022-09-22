/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspdevelopment.KidsScore.repositories;

import com.tspdevelopment.kidsscore.data.model.Role;
import com.tspdevelopment.kidsscore.data.model.User;
import com.tspdevelopment.kidsscore.data.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Java6Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author tobiesp
 */
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTestEmbedded {
 
    @Autowired
    private UserRepository userRepository;
 
    @Test
    public void shouldSaveUser() {
        User user = new User();
        user.setFullName("Test User");
        user.setPassword("secret password");
        user.setEnabled(true);
        user.setUsername("test user");
        User savedUser = userRepository.save(user);
        assertThat(savedUser).usingRecursiveComparison().ignoringFields("userId").isEqualTo(user);
    }
 
}
