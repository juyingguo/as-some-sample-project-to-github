package com.wd.room.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import com.wd.room.R;
import com.wd.room.databinding.FragTwoBinding;
import com.wd.room.viewmodel.ListValueViewModel;

public class TwoFragment extends Fragment implements LifecycleOwner {
    private static final String TAG = "TwoFragment";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final FragTwoBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.frag_two,container,false);

        final ListValueViewModel viewModel = new ViewModelProvider(getActivity())
                .get(ListValueViewModel.class);
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.setValue(s);
                Log.i(TAG, "onChanged s:" + viewModel.hashCode() + "");
                binding.notifyChange();
            }
        };
        viewModel.getName().observe(this, observer);
        //谁观察生命周期  就注册谁  两个角色定义好后，需要让他们之间建立联系
        //获取Lifecycle
        getLifecycle().addObserver(new LocationListener());
        return binding.getRoot();
    }
    public class LocationListener implements LifecycleObserver {
        private static final String TAG = "LocationListener";

        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        public void onActivityCreate(LifecycleOwner owner) {
            Log.w(TAG, "onActivityCreate");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void onActivityDestroy(LifecycleOwner owner) {
            Log.w(TAG, "onActivityDestroy");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void onActivityPause(LifecycleOwner owner) {
            Log.w(TAG, "onActivityPause");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void onActivityResume(LifecycleOwner owner) {
            Log.w(TAG, "onActivityResume");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        public void onActivityStart(LifecycleOwner owner) {
            Log.w(TAG, "onActivityStart");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public void onActivityStop(LifecycleOwner owner) {
            Log.w(TAG, "onActivityStop");
        }

    }
}
