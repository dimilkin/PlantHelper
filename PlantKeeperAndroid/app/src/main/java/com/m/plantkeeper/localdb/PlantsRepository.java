package com.m.plantkeeper.localdb;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.m.plantkeeper.localdb.dao.PlantDao;
import com.m.plantkeeper.models.Plant;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class PlantsRepository {

    private PlantDao plantsDao;
    private LiveData<List<Plant>> allPlantsForTheUser;
    private static PlantsRepository instance;

    private PlantsRepository(Application application) {
        ApplicationDatabase applicationDatabase = ApplicationDatabase.getInstance(application);
        plantsDao = applicationDatabase.plantDao();
        allPlantsForTheUser = plantsDao.getAllPlants();
    }

    public static synchronized PlantsRepository getPlantRepositoryInstance(Application application) {
        if (instance == null) {
            instance = new PlantsRepository(application);
        }
        return instance;
    }

    private static class InsertPlantAsyncTask extends AsyncTask<Plant, Void, Void> {
        private PlantDao plantDao;

        private InsertPlantAsyncTask(PlantDao plantDao) {
            this.plantDao = plantDao;
        }

        @Override
        protected Void doInBackground(Plant... plants) {
            plantDao.insert(plants[0]);
            Log.i("Successfully added plant :", plants[0].getCommonName());
            return null;
        }
    }

    private static class UpdatePlantAsyncTask extends AsyncTask<Plant, Void, Void> {
        private PlantDao plantDao;

        public UpdatePlantAsyncTask(PlantDao plantDao) {
            this.plantDao = plantDao;
        }

        @Override
        protected Void doInBackground(Plant... plants) {
            plantDao.update(plants[0]);
            return null;
        }
    }

    private static class DeletePlantAsyncTask extends AsyncTask<Plant, Void, Void> {
        private PlantDao plantDao;

        public DeletePlantAsyncTask(PlantDao plantDao) {
            this.plantDao = plantDao;
        }

        @Override
        protected Void doInBackground(Plant... plants) {
            plantDao.delete(plants[0]);
            return null;
        }
    }

    private static class GetPlantFromDbById extends AsyncTask<Integer, Void, Plant> {
        private PlantDao plantDao;

        private GetPlantFromDbById(PlantDao plantDao) {
            this.plantDao = plantDao;
        }

        @Override
        protected Plant doInBackground(Integer... integers) {
            return plantDao.getPlantById(integers[0]);
        }
    }

    public void insert(Plant plant) {
        new InsertPlantAsyncTask(plantsDao).execute(plant);
    }

    public void update(Plant plant) {
        new UpdatePlantAsyncTask(plantsDao).execute(plant);
    }

    public void delete(Plant plant) {
        new DeletePlantAsyncTask(plantsDao).execute(plant);
    }

    public Plant getPlantFromDbById(int plantid) throws ExecutionException, InterruptedException {
        Plant plant = new GetPlantFromDbById(plantsDao).execute(plantid).get();
        return plant;
    }

    public boolean plantWithIdExists(int plantId) {
        try {
            Plant plant = getPlantFromDbById(plantId);
            return plant != null;
        } catch (ExecutionException | InterruptedException e) {
            Log.e("Failed To Get Plant From Db", "Failed To Get Plant From Db", e);
            return false;
        }
    }
}
