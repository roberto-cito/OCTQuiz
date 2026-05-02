package com.oct.octquiz.Model.User;

import com.oct.octquiz.Model.Categoria.CategoriaEntity;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + email));
        return new CustomUserDetails(user);
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + email));
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public void updatePassword(String newPassword, String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + email));
        user.setHash_password(PasswordUtility.hashPassword(newPassword));
        userRepository.save(user);
        userRepository.flush();
    }

    public void addCategory(CategoriaEntity categoria, String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + email));
        user.getCategorie().add(categoria);
        userRepository.save(user);
        userRepository.flush();
    }

    public void resetCategories(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + email));
        user.getCategorie().clear();
        userRepository.save(user);
        userRepository.flush();
    }

    @Transactional
    public void removeUser(UserEntity user) {
        UserEntity userEntity = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + user.getEmail()));
        userRepository.delete(userEntity);
        userRepository.flush();
    }

    public void removeAllCompleteCategory(CategoriaEntity categoria) {
        List<UserEntity> userEntities=userRepository.findAll();
        userEntities.forEach(userEntity -> userEntity.getCategorie().remove(categoria));
        userRepository.saveAll(userEntities);
    }
    
    public long getNumberOfUsers() {
        List<UserEntity> userEntities=userRepository.findAll();
        userEntities.removeIf(userEntity -> userEntity.getRuolo().equals("ADMIN"));
        return userEntities.size();
    }

    @Transactional
    public void addUser(UserEntity user) {
        userRepository.save(user);
    }

    public void save(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    public boolean exists(String registerEmail) {
        return userRepository.existsById(registerEmail);
    }
}