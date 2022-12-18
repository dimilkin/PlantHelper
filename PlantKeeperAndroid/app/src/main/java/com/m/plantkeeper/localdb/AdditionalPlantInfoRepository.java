package com.m.plantkeeper.localdb;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.m.plantkeeper.localdb.dao.AdditionalPlantInfoDao;
import com.m.plantkeeper.models.AdditionalPlantInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AdditionalPlantInfoRepository {

    private AdditionalPlantInfoDao additionalPlantInfoDao;
    private static AdditionalPlantInfoRepository instance;

    private AdditionalPlantInfoRepository(Application application) {
        ApplicationDatabase applicationDatabase = ApplicationDatabase.getInstance(application);
        additionalPlantInfoDao = applicationDatabase.additionalPlantInfoDao();
    }

    public static synchronized AdditionalPlantInfoRepository getPlantRepositoryInstance(Application application) {
        if (instance == null) {
            instance = new AdditionalPlantInfoRepository(application);
        }
        return instance;
    }

    private static class InsertAdditionalInfoForPlant extends AsyncTask<AdditionalPlantInfo, Void, Void> {
        private AdditionalPlantInfoDao additionalPlantInfoDao;

        private InsertAdditionalInfoForPlant(AdditionalPlantInfoDao additionalPlantInfoDao) {
            this.additionalPlantInfoDao = additionalPlantInfoDao;
        }

        @Override
        protected Void doInBackground(AdditionalPlantInfo... additionalPlantInfo) {
            additionalPlantInfoDao.insert(additionalPlantInfo[0]);
            return null;
        }
    }

    private static class UpdateAdditionalPlantInfoTask extends AsyncTask<AdditionalPlantInfo, Void, Void> {
        private AdditionalPlantInfoDao additionalPlantInfoDao;

        public UpdateAdditionalPlantInfoTask(AdditionalPlantInfoDao additionalPlantInfoDao) {
            this.additionalPlantInfoDao = additionalPlantInfoDao;
        }

        @Override
        protected Void doInBackground(AdditionalPlantInfo... additionalPlantInfo) {
            additionalPlantInfoDao.update(additionalPlantInfo[0]);
            return null;
        }
    }

    private static class DeleteAdditionalInfoForPlantAsyncTask extends AsyncTask<AdditionalPlantInfo, Void, Void> {
        private AdditionalPlantInfoDao additionalPlantInfoDao;

        public DeleteAdditionalInfoForPlantAsyncTask(AdditionalPlantInfoDao additionalPlantInfoDao) {
            this.additionalPlantInfoDao = additionalPlantInfoDao;
        }

        @Override
        protected Void doInBackground(AdditionalPlantInfo... additionalPlantInfo) {
            additionalPlantInfoDao.delete(additionalPlantInfo[0]);
            return null;
        }
    }

    private static class GetAdditionalInfoListForPlantById extends AsyncTask<Integer, Void, List<AdditionalPlantInfo>> {
        private AdditionalPlantInfoDao additionalPlantInfoDao;

        private GetAdditionalInfoListForPlantById(AdditionalPlantInfoDao additionalPlantInfoDao) {
            this.additionalPlantInfoDao = additionalPlantInfoDao;
        }

        @Override
        protected List<AdditionalPlantInfo> doInBackground(Integer... integers) {
            return additionalPlantInfoDao.getAdditinalInfoForSpecificPlant(integers[0]);
        }
    }

    public void insert(AdditionalPlantInfo additionalPlantInfo) {
        new AdditionalPlantInfoRepository.InsertAdditionalInfoForPlant(additionalPlantInfoDao).execute(additionalPlantInfo);
    }

    public void update(AdditionalPlantInfo additionalPlantInfo) {
        new AdditionalPlantInfoRepository.UpdateAdditionalPlantInfoTask(additionalPlantInfoDao).execute(additionalPlantInfo);
    }

    public void delete(AdditionalPlantInfo additionalPlantInfo) {
        new AdditionalPlantInfoRepository.DeleteAdditionalInfoForPlantAsyncTask(additionalPlantInfoDao).execute(additionalPlantInfo);
    }

    public List<AdditionalPlantInfo> getAdditionalInforListFroPlantbyId(int plantid) throws ExecutionException, InterruptedException {
        List<AdditionalPlantInfo> additionalPlantInfoList = new AdditionalPlantInfoRepository.GetAdditionalInfoListForPlantById(additionalPlantInfoDao).execute(plantid).get();
        return additionalPlantInfoList;
    }
}
