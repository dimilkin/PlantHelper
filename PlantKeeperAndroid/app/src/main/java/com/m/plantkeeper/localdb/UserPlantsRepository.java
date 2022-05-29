package com.m.plantkeeper.localdb;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.m.plantkeeper.localdb.dao.UserPlantDao;
import com.m.plantkeeper.models.UserPlant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserPlantsRepository {

    private UserPlantDao userPlantDao;
    private LiveData<List<UserPlant>> allUserPlants;
    private static UserPlantsRepository instance;

    private UserPlantsRepository(Application application) {
        ApplicationDatabase applicationDatabase = ApplicationDatabase.getInstance(application);
        userPlantDao = applicationDatabase.userPlantDao();
        allUserPlants = userPlantDao.getAllUserPlantsLiveData();
    }

    public static synchronized UserPlantsRepository getInstance(Application application) {
        if (instance == null) {
            instance = new UserPlantsRepository(application);
        }
        return instance;
    }

    private static class InsertUserPlantAsyncTask extends AsyncTask<UserPlant, Void, Void> {
        private UserPlantDao userPlantDao;

        private InsertUserPlantAsyncTask(UserPlantDao userPlantDao) {
            this.userPlantDao = userPlantDao;
        }

        @Override
        protected Void doInBackground(UserPlant... userPlants) {
            userPlantDao.insert(userPlants[0]);
            Log.i("Successfully added plant :", userPlants[0].getProvidedName());
            return null;
        }
    }

    private static class UpdateUserPlantAsyncTask extends AsyncTask<UserPlant, Void, Void> {
        private UserPlantDao plantDao;

        public UpdateUserPlantAsyncTask(UserPlantDao plantDao) {
            this.plantDao = plantDao;
        }

        @Override
        protected Void doInBackground(UserPlant... plants) {
            plantDao.update(plants[0]);
            return null;
        }
    }

    private static class DeleteUserPlantAsyncTask extends AsyncTask<UserPlant, Void, Void> {
        private UserPlantDao plantDao;

        public DeleteUserPlantAsyncTask(UserPlantDao plantDao) {
            this.plantDao = plantDao;
        }

        @Override
        protected Void doInBackground(UserPlant... userPlants) {
            plantDao.delete(userPlants[0]);
            return null;
        }

    }

    private static class GetUserPlantFromDbById extends AsyncTask<Integer, Void, UserPlant> {
        private UserPlantDao plantDao;

        private GetUserPlantFromDbById(UserPlantDao plantDao) {
            this.plantDao = plantDao;
        }

        @Override
        protected UserPlant doInBackground(Integer... integers) {
            return plantDao.getPlantById(integers[0]);
        }
    }

    private static class GetUserPlantsAsyncTask extends AsyncTask<Integer, Integer, List<UserPlant>> {
        private UserPlantDao userPlantDao;

        private GetUserPlantsAsyncTask(UserPlantDao userPlantDao) {
            this.userPlantDao = userPlantDao;
        }

        @Override
        protected List<UserPlant> doInBackground(Integer... userIds) {
            try {
                return userPlantDao.getAllUserPlantsByUserId(userIds[0]);
            } catch (ArrayIndexOutOfBoundsException exception) {
                Log.i("INFO", "No info in local storage, returning empty list");
                return new ArrayList<>();
            }
        }
    }

    public void insert(UserPlant userPlant) {
        new UserPlantsRepository.InsertUserPlantAsyncTask(userPlantDao).execute(userPlant);
    }

    public void update(UserPlant userPlant) {
        new UserPlantsRepository.UpdateUserPlantAsyncTask(userPlantDao).execute(userPlant);
    }

    public void delete(UserPlant userPlant) {
        new UserPlantsRepository.DeleteUserPlantAsyncTask(userPlantDao).execute(userPlant);
    }

    public LiveData<List<UserPlant>> getAllUserPlantsLiveData() {
        return allUserPlants;
    }

    public List<UserPlant> getAllUserPlantsListForUserById(int userId) throws ExecutionException, InterruptedException {
        return new UserPlantsRepository.GetUserPlantsAsyncTask(userPlantDao).execute(userId).get();
    }

    public UserPlant getUserPlantFromDbById(int plantid) throws ExecutionException, InterruptedException {
        return new UserPlantsRepository.GetUserPlantFromDbById(userPlantDao).execute(plantid).get();
    }

    public boolean userPlantWithIdExists(int plantId) {
        try {
            return getUserPlantFromDbById(plantId) != null;
        } catch (ExecutionException | InterruptedException e) {
            Log.e("Failed To Get Plant From Db", "Failed To Get Plant From Db", e);
            return false;
        }
    }
}
