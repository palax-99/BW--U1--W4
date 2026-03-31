package AntoninoPalazzolo.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "id_user")
    private UUID idUser;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_surname", nullable = false)
    private String userSurname;

    @Column(name = "user_date_of_birth", nullable = false)
    private LocalDate userDateOfBirth;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    protected User() {
    }

    public User(String userName, String userSurname, LocalDate userDateOfBirth, UserRole userRole, String userEmail) {
        this.userName = userName;
        this.userSurname = userSurname;
        this.userDateOfBirth = userDateOfBirth;
        this.userRole = userRole;
        this.userEmail = userEmail;
    }

    public UUID getIdUser() {
        return idUser;
    }

    public LocalDate getUserDateOfBirth() {
        return userDateOfBirth;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", userName='" + userName + '\'' +
                ", userSurname='" + userSurname + '\'' +
                ", userDateOfBirth=" + userDateOfBirth +
                ", userRole=" + userRole +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }
}
