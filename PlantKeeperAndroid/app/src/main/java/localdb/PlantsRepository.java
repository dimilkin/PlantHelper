package localdb;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.m.plantkeeper.models.Plant;

import java.util.List;
import java.util.concurrent.ExecutionException;

import localdb.dao.PlantDao;

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
        protected Void doInBackground(Plant... children) {
            plantDao.update(children[0]);
            return null;
        }
    }

    private static class DeletePlantAsyncTask extends AsyncTask<Plant, Void, Void> {
        private PlantDao plantDao;

        public DeletePlantAsyncTask(PlantDao plantDao) {
            this.plantDao = plantDao;
        }

        @Override
        protected Void doInBackground(Plant... children) {
            plantDao.delete(children[0]);
            return null;
        }

    }

    private static class ReloadPlantsAlarmInfoAsyncTask extends AsyncTask<Plant, Void, List<Plant>> {
        private PlantDao plantDao;

        private ReloadPlantsAlarmInfoAsyncTask(PlantDao plantDao){
            this.plantDao = plantDao;
        }

        @Override
        protected List<Plant> doInBackground(Plant... children) {
            return plantDao.reloadPlantsAlarmInfo();
        }
    }

    public void insert(Plant child) {
        new InsertPlantAsyncTask(plantsDao).execute(child);
    }

    public void update(Plant child) {
        new UpdatePlantAsyncTask(plantsDao).execute(child);
    }

    public void delete(Plant child) {
        new DeletePlantAsyncTask(plantsDao).execute(child);
    }

    public LiveData<List<Plant>> getAllPlants() {
        return allPlantsForTheUser;
    }

    public List<Plant> reloadPlantAlarmInfo() throws ExecutionException, InterruptedException {
        return new ReloadPlantsAlarmInfoAsyncTask(plantsDao).execute().get();
    }
}
