package com.example.c_mvvm_androidviewmodel_sp;
//3.創建ViewModel,繼承AndroidViewModel,即可取得Application
//4.建構式取得Application,並多帶一個SavedStateHandle參數MyViewModel(@NonNull Application application , SavedStateHandle savedStateHandle)
//5.如果SavedStateHandle,沒有我所需的數據,那我需要從Sp去讀取,如果有就不用讀取
//6.讀取key的值,並寫入SavedStateHandle裡
//7.儲存liveData的key ,LiveData<Integer> 的值
//8.從savedStateHandle取得值用liveData包覆
//9.加法方法,依據你傳來的參數，判斷是加法還是剪法,存到savedStateHandle裡,此方法用在databinding的xml add(1) , add(-1)
//10.每次修改數據就儲存,那數據肯定不會丟失,缺點是我常常去使用就會去掉用SharedPreferences,這樣會比較花時間，改到onPause時才儲存
//11..改到onPause時才去儲存數據,因為就算被系統殺死還是會跑pause,而且也不會因為add時多次掉用影響時間
/*12.dataBinding綁定三樣數據
//13.dataBinding設定setContentView
//14.取得ＶiewModel設定,SavedStateViewModelFactory()
//15.xml,DataBinding綁定ViewModel數據
 *  android:text="@{String.valueOf(data.getNumber())}" />
 *  android:onClick="@{() -> data.add(1)}"
 *  android:onClick="@{() -> data.add(-1)}"
 * */


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends AndroidViewModel {
    private SavedStateHandle savedStateHandle;
    private String key = getApplication().getResources().getString(R.string.data_key);
    private String spName = getApplication().getResources().getString(R.string.sp_name);
    public static String TAG = "hank";

    //4.建構式取得Application,並多帶一個SavedStateHandle參數MyViewModel(@NonNull Application application , SavedStateHandle savedStateHandle)
    public MyViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);
        this.savedStateHandle = handle;

        //5.如果SavedStateHandle,沒有我所需的數據,那我需要從Sp去讀取,如果有就不用讀取
        if (!savedStateHandle.contains(key)) {
            load();
            Log.v(TAG, "(!savedStateHandle.contains(key) -> ");
        } else {
            Log.v(TAG, "(savedStateHandle.contains(key) -> key:" + savedStateHandle.get(key));
        }
    }

    //8.從savedStateHandle取得值用liveData包覆
    public LiveData<Integer> getNumber() {
        Log.v(TAG, "(getNumber() -> savedStateHandle.getLiveData(key) value:" + savedStateHandle.getLiveData(key).getValue());
        return savedStateHandle.getLiveData(key);
    }


    //6.讀取key的值,並寫入SavedStateHandle裡
    private void load() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(spName, Context.MODE_PRIVATE);
        int x = sharedPreferences.getInt(key, 0);
        savedStateHandle.set(key, x);
        Log.v(TAG, "(load()");
    }

    //7.儲存liveData的key ,LiveData<Integer> 的值
    public void save() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, getNumber().getValue());
        editor.apply();
        Log.v(TAG, "(save()");
    }

    //9.加法方法,依據你傳來的參數，判斷是加法還是剪法,存到savedStateHandle裡,此方法用在databinding的xml add(1) , add(-1)
    public void add(int value) {
        savedStateHandle.set(key, getNumber().getValue() + value);
        //11.每次修改數據就儲存,那數據肯定不會丟失,缺點是我常常去使用就會去掉用SharedPreferences,這樣會比較花時間，改到onPause時才儲存
//        save();
    }
}

