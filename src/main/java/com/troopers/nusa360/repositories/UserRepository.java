package com.troopers.nusa360.repositories;

import com.troopers.nusa360.dtos.UserDto;
import com.troopers.nusa360.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // --USER--
    //Search By Username
    List<User> findByUsername(String username);
    List<User> findByUsernameLike(String username);
    List<User> findByUsernameLikeIgnoreCase(String username);
    //Search and Sort By Username (Ascending)
    List<User> findByUsernameOrderByUsernameAsc(String username);
    //Limit Top 5 Search and Sort By Username (Ascending)
    List<User> findTop5ByUsernameOrderByUsernameAsc(String username);
    //Search By Email
    Optional<User> findByEmail(String email);

    //--USERDTO--
    //Search By Username
    @Query(value = "SELECT * FROM users WHERE username = :username", nativeQuery = true)
    List<UserDto> findUserDataByUsername(@Param("username") String Username);
    @Query(value = "SELECT * FROM users WHERE username LIKE %:username%", nativeQuery = true)
    List<UserDto> findUserDataByUsernameLike(@Param("username") String username);
    @Query(value = "SELECT * FROM users WHERE LOWER(username) LIKE LOWER(CONCAT('%', :username, '%'))", nativeQuery = true)
    List<UserDto> findUserDataByUsernameLikeIgnoreCase(@Param("username") String Username);
    //Search and Sort By Username (Ascending)
    @Query(value = "SELECT * FROM users WHERE username = :username ORDER BY username ASC", nativeQuery = true )
    List<UserDto> findUserDataByUsernameOrderByUsernameAsc(@Param("username") String Username);
    //Search By Email
    @Query(value = "SELECT * FROM users u WHERE u.email = :email", nativeQuery = true)
    List<UserDto> findUserDataByEmail(@Param("email") String email);

    boolean existsByEmail(String email);
}