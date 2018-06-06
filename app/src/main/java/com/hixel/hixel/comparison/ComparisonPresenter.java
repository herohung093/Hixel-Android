package com.hixel.hixel.comparison;

import android.support.annotation.NonNull;
import android.util.Log;
import com.hixel.hixel.models.Company;
import com.hixel.hixel.network.Client;
import com.hixel.hixel.network.ServerInterface;
import com.hixel.hixel.search.SearchEntry;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComparisonPresenter implements ComparisonContract.Presenter {
    private List<Company> listCompareCompanies = new ArrayList<>();
    private final ComparisonContract.View mComparisonView;

    private static List<String> names;
    private CompositeDisposable disposable;
    private PublishSubject<String> publishSubject;

    ComparisonPresenter(ComparisonContract.View mComparisonView) {
        this.mComparisonView = mComparisonView;
        listCompareCompanies.clear();

        names = new ArrayList<>();
        disposable = new CompositeDisposable();
        publishSubject = PublishSubject.create();

    }

    /*
    public void setListCompareCompanies(List<Company> listCompareCompanies) {
        this.listCompareCompanies = listCompareCompanies;
    }*/

    @Override
    public void start() {
        names.add(""); //TODO: Figure out why this is even a thing, and remove it probably..?

    }

    /*
    public void removeCompareFromList(int position){
        listCompareCompanies.remove(position);

    }

    public void compare(){

    }*/

    public List<Company> getListCompareCompanies(){
        return listCompareCompanies;
    }

    public void addToCompare(String ticker) {

        if (listCompareCompanies.size() <= 1) {
            ServerInterface client = Client
                    .getClient()
                    .create(ServerInterface.class);

            Call<ArrayList<Company>> call = client
                    .doGetCompanies(StringUtils.join(ticker, ','), 5);

            call.enqueue(new Callback<ArrayList<Company>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Company>> call,
                                       @NonNull Response<ArrayList<Company>> response) {

                    if (response.body() == null) {
                        mComparisonView.userNotification("Company not found@");

                    } else {
                        Log.d("addToCompare", Objects.requireNonNull(response.body()).get(0).getIdentifiers().getTicker());

                        listCompareCompanies.add(Objects.requireNonNull(response.body()).get(0));

                        mComparisonView.selectedListChanged();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Company>> call, @NonNull Throwable t) {
                    Log.d("addToCompare",
                            "Failed to load company data from the server: " + t.getMessage());
                }
            });
        }
        else {
            mComparisonView.userNotification("Can only compare 2 companies!");
        }

    }

    /*
    @Override
    public void removeLastItemFromList() {
        if (listCompareCompanies.size()!= 0) {
            listCompareCompanies.remove(listCompareCompanies.size() - 1);
        }
    }*/

    @Override
    public void loadSearchResult(String query) {
        publishSubject.onNext(query);
    }

    /*
    @Override
    public List<String> getNames() {
        return names;
    }*/

}
