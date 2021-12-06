package com.example.c_mvvm_androidviewmodel_sp;
//目地：在系統重新啟動時,資料存在Sp裡不被刪除,且可以正常讀取,加一減一重啟App後會成功讀取資料

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.example.c_mvvm_androidviewmodel_sp.databinding.ActivityMainBinding;
//1.dataBinding {
//        enabled = true
//    }

//2.ui準備增跟減按鈕,顯示的textView
//已下在ＭyViewModel做處理
//3.創建ViewModel,繼承AndroidViewModel,即可取得Application
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
 *  android:text="@{String.valueOf(data.getNumber())}" />
 *  android:onClick="@{() -> data.add(1)}"
 *  android:onClick="@{() -> data.add(-1)}"
 * */
//13.dataBinding設定setContentView
//14.取得ＶiewModel設定,SavedStateViewModelFactory()
//15.xml,DataBinding綁定ViewModel數據
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MyViewModel myViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //13.dataBinding設定setContentView
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //12.取得ＶiewModel設定,SavedStateViewModelFactory()
        myViewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(getApplication(), this)).get(MyViewModel.class);

        //15.xml,DataBinding綁定ViewModel數據
        binding.setData(myViewModel);
        binding.setLifecycleOwner(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        myViewModel.save();
        Log.v(MyViewModel.TAG ,"onPause() -> myViewModel.save() ");
    }
}