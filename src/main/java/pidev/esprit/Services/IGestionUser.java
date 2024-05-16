package pidev.esprit.Services;

import pidev.esprit.Entities.User;

import java.util.List;



    public interface IGestionUser<T> {
        // Create
        void createUser(T t);

        // Read

        List<User> afficherUser();

        // Update
        void updateUser(T t);

        // Delete
        void deleteUser(int userId);
    }


