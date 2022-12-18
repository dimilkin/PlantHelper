package com.m.plantkeeper.localdb;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.m.plantkeeper.localdb.dao.PotentialPlantProblemsDao;
import com.m.plantkeeper.models.PotentialPlantProblems;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class PotentialPlantProblemsRepository {

    private PotentialPlantProblemsDao potentialPlantProblemsDao;
    private static PotentialPlantProblemsRepository instance;

    private PotentialPlantProblemsRepository(Application application) {
        ApplicationDatabase applicationDatabase = ApplicationDatabase.getInstance(application);
        potentialPlantProblemsDao = applicationDatabase.potentialPlantProblemsDao();
    }

    public static synchronized PotentialPlantProblemsRepository getPlantRepositoryInstance(Application application) {
        if (instance == null) {
            instance = new PotentialPlantProblemsRepository(application);
        }
        return instance;
    }

    private static class InsertPotentialPlantProblemsForPlant extends AsyncTask<PotentialPlantProblems, Void, Void> {
        private PotentialPlantProblemsDao potentialPlantProblemsDao;

        private InsertPotentialPlantProblemsForPlant(PotentialPlantProblemsDao potentialPlantProblemsDao) {
            this.potentialPlantProblemsDao = potentialPlantProblemsDao;
        }

        @Override
        protected Void doInBackground(PotentialPlantProblems... additionalPlantInfo) {
            potentialPlantProblemsDao.insert(additionalPlantInfo[0]);
            return null;
        }
    }

    private static class UpdatePotentialPlantProblemsTask extends AsyncTask<PotentialPlantProblems, Void, Void> {
        private PotentialPlantProblemsDao potentialPlantProblemsDao;

        public UpdatePotentialPlantProblemsTask(PotentialPlantProblemsDao potentialPlantProblemsDao) {
            this.potentialPlantProblemsDao = potentialPlantProblemsDao;
        }

        @Override
        protected Void doInBackground(PotentialPlantProblems... additionalPlantInfo) {
            potentialPlantProblemsDao.update(additionalPlantInfo[0]);
            return null;
        }
    }

    private static class DeletePotentialPlantProblemsPlantAsyncTask extends AsyncTask<PotentialPlantProblems, Void, Void> {
        private PotentialPlantProblemsDao potentialPlantProblemsDao;

        public DeletePotentialPlantProblemsPlantAsyncTask(PotentialPlantProblemsDao potentialPlantProblemsDao) {
            this.potentialPlantProblemsDao = potentialPlantProblemsDao;
        }

        @Override
        protected Void doInBackground(PotentialPlantProblems... additionalPlantInfo) {
            potentialPlantProblemsDao.delete(additionalPlantInfo[0]);
            return null;
        }
    }

    private static class GetPotentialProblemsForPlantById extends AsyncTask<Integer, Void, List<PotentialPlantProblems>> {
        private PotentialPlantProblemsDao potentialPlantProblemsDao;

        private GetPotentialProblemsForPlantById(PotentialPlantProblemsDao potentialPlantProblemsDao) {
            this.potentialPlantProblemsDao = potentialPlantProblemsDao;
        }

        @Override
        protected List<PotentialPlantProblems> doInBackground(Integer... integers) {
            return potentialPlantProblemsDao.getPlantProblemsForSpecificPlant(integers[0]);
        }
    }

    public void insert(PotentialPlantProblems additionalPlantInfo) {
        new PotentialPlantProblemsRepository.InsertPotentialPlantProblemsForPlant(potentialPlantProblemsDao).execute(additionalPlantInfo);
    }

    public void update(PotentialPlantProblems additionalPlantInfo) {
        new PotentialPlantProblemsRepository.UpdatePotentialPlantProblemsTask(potentialPlantProblemsDao).execute(additionalPlantInfo);
    }

    public void delete(PotentialPlantProblems additionalPlantInfo) {
        new PotentialPlantProblemsRepository.DeletePotentialPlantProblemsPlantAsyncTask(potentialPlantProblemsDao).execute(additionalPlantInfo);
    }

    public List<PotentialPlantProblems> getPotentialProblemdForPlantById(int plantid) throws ExecutionException, InterruptedException {
        List<PotentialPlantProblems> potentialPlantProblems = new PotentialPlantProblemsRepository.GetPotentialProblemsForPlantById(potentialPlantProblemsDao).execute(plantid).get();
        return potentialPlantProblems;
    }
}
