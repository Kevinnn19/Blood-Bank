package com.example.bloodbank.project;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderExample 
{

  public static void main(String[] args) 
  {
      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
      
      String rawPassword = "admin1";

      String encryptedPassword = encoder.encode(rawPassword);

      System.out.println("Encrypted Password: " + encryptedPassword);
  }
}

//INSERT INTO `user` (username, password, enabled) 
//VALUES ('admin', '$2a$10$wUZoWH0efGkkTW.ceCZ/IuYaJXQ/wWH7D0KJRoWBmvaGCuCaHI1GG', 1);
//
//
//INSERT INTO authority (user_id, authority) 
//VALUES ((SELECT id FROM `user` WHERE username = 'admin' LIMIT 1), 'ROLE_ADMIN');

