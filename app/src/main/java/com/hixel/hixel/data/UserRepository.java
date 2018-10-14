package com.hixel.hixel.data;



import android.databinding.ObservableBoolean;
import android.util.Log;
import com.hixel.hixel.data.api.ServerInterface;
import com.hixel.hixel.data.database.UserDao;
import com.hixel.hixel.data.models.LoginData;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class UserRepository {

    private static final int FRESH_TIMEOUT_IN_MINUTES = 10;

    @SuppressWarnings("unused")
    private static final String TAG = UserRepository.class.getSimpleName();

    private ServerInterface serverInterface;
    private final UserDao userDao;
    private final Executor executor;

    // Temporary workaround
    public final ObservableBoolean isValidUser = new ObservableBoolean();

    @Inject
    public UserRepository(ServerInterface serverInterface, UserDao userDao, Executor executor) {
        this.serverInterface = serverInterface;
        this.userDao = userDao;
        this.executor = executor;
    }

}
