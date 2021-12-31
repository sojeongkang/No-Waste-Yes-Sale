package com.example.myhackaton.activity;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.Edits;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myhackaton.FirebaseDB;
import com.example.myhackaton.L;
import com.example.myhackaton.R;
import com.example.myhackaton.auth.FirebaseAuth;
import com.example.myhackaton.helper.MessageUtils;
import com.example.myhackaton.location.AltitudeListener;
import com.example.myhackaton.location.LocationManager;
import com.example.myhackaton.model.Sales;
import com.example.myhackaton.model.dto.SalesDto;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirstFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    @BindView(R.id.title_location)
    TextView mTitle;
    @BindView(R.id.near_store_layout)
    LinearLayout nearStoreLayout;
    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.salesRecyclerView)
    RecyclerView mSalesRecyclerView;
    ReviewAdapter mReviewAdapter;

    GoogleMap mGoogleMap;
    View mView;
    private UiSettings mUiSettings;
    private boolean mIsFromHostJoin = false;
    private DatabaseReference mSalesReference = null;
    private ArrayList<SalesDto> mSalesList = new ArrayList<>();
    private AltitudeListener mAltitudeListener = new AltitudeListener() {
        @Override
        public void onResult(LatLng location) {
            L.e("location = " + location);
            AlarmActivity aActivity = getAlarmActivity();
            if (location != null) {

                if (aActivity != null) {
                    aActivity.hideProgressDialog();
                }
                if (mIsFromHostJoin) {
                    setLongClickOnMap();
                    moveToCurrentLocation(location);
                } else {
                    getSales(location);
                }

            }
        }
    };

    public FirstFragment() {
        // required
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_first, container, false);
        return mView;
        //SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);

        //mapView = (MapView)layout.findViewById(R.id.map);
        //mapView.onCreate(savedInstanceState);
        //mapView.onResume();
        //mapView.getMapAsync(this);

        //return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (getAlarmActivity() != null) {
            mIsFromHostJoin = getAlarmActivity().getIsFromHostJoin();
            if (mIsFromHostJoin) {
                mTitle.setText("가게가 위치한 지도의 영역을 롱 터치해주세요..");
                nearStoreLayout.setVisibility(View.GONE);
            }
        }
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        if (!mIsFromHostJoin) {
            mSalesReference = FirebaseDatabase.getInstance().getReference().child(FirebaseDB.HY_SALES);
            initSalesView();
        }
    }

    private void initSalesView() {
        mSalesRecyclerView.setHasFixedSize(true);
        mSalesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mReviewAdapter = new ReviewAdapter();
        mSalesRecyclerView.setAdapter(mReviewAdapter);
    }

    private void getSales(LatLng location) {
        mSalesList.clear();
        getAlarmActivity().setLoading("주변 상점들을 불러오고 있습니다.");
        mSalesReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    if (getAlarmActivity() != null) {
                        getAlarmActivity().hideProgressDialog();
                    }
                    return;
                }
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Sales sales = child.getValue(Sales.class);
                    L.i(":::sales : " + sales);
                    L.i(":::sales seq  : " + child.getKey());
                    if (sales != null) {
                        mSalesList.add(SalesDto.builder().mSeq(child.getKey()).sale(sales).build());
                        //선택한 신청항목 키값을 이용하여 항목 삭제
                    }
                }
                sortByDistanceAndRefresh(location, mSalesList);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sortByDistanceAndRefresh(LatLng myLoc, ArrayList<SalesDto> mSalesList) {
        L.e("before = "+ mSalesList.toString());
        HashMap<Double, SalesDto> locationMap = new HashMap<>();
        ArrayList<SalesDto> sortList = new ArrayList<>();

        for(SalesDto sDto : mSalesList){
                String [] locationStr = sDto.getSale().getStoreLocation().split(",");
                LatLng storeLoc= new LatLng(Double.parseDouble(locationStr[0]), Double.parseDouble(locationStr[1]));
                double distanceTo = getDistanceTo(myLoc,storeLoc);
                locationMap.put(distanceTo, sDto);
        }
        HashMap<Double, SalesDto> copyMap = (HashMap<Double, SalesDto>) locationMap.clone();
        Set<Double> keys = new HashSet<>();
        keys = copyMap.keySet();
        Double[] keyArrays = keys.toArray(new Double[keys.size()]);

        double temp;
        for(int i = 0 ; i < keyArrays.length-1 ; i ++) {
            for(int j = i+1 ; j < keyArrays.length ; j ++) {
                if(keyArrays[i] > keyArrays[j]) {
                    temp = keyArrays[j];
                    keyArrays[j] = keyArrays[i];
                    keyArrays[i] = temp;
                }
            }
        }


        L.e("after = "+ locationMap);
        for(double keyss : keyArrays){
            L.e("after = "+ keyss +", locationMap.get(keys) = "+ locationMap.get(keyss));
            sortList.add(locationMap.get(keyss));
        }
        mReviewAdapter.onRefresh(sortList);

        long delay = sortList.size() == 0 ? 0 : sortList.size() * 33;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                moveToCurrentLocation(myLoc);
                getAlarmActivity().hideProgressDialog();
            }
        },delay);

    }

    private float getDistanceTo(LatLng myLoc, LatLng storeLoc) {

        Location myLocation = new Location("myLocation");
        Location storeLocation = new Location("storeLocation");
        myLocation.setLatitude(myLoc.latitude);
        myLocation.setLongitude(myLoc.longitude);
        storeLocation.setLatitude(storeLoc.latitude);
        storeLocation.setLongitude(storeLoc.longitude);
        float distanceTo = myLocation.distanceTo(storeLocation);
        return distanceTo;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mapView != null)
            mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mapView != null)
            mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null)
            mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null)
            mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null)
            mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null)
            mapView.onLowMemory();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private AlarmActivity getAlarmActivity() {
        Activity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            AlarmActivity mainActivity = (AlarmActivity) activity;
            return mainActivity;
        }
        return null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap == null) return;

        if (getAlarmActivity() != null) {
            getAlarmActivity().setLoading("현재 위치 정보를 수집중입니다..");
        }


        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        mUiSettings = mGoogleMap.getUiSettings();
        mUiSettings.setMyLocationButtonEnabled(true);

        LocationManager locationManager = new LocationManager(getActivity());
        locationManager.registerAltitudeListener(mAltitudeListener);
        locationManager.startLocationUpdate();
    }

    private void setLongClickOnMap() {
        mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Geocoder geocoder = new Geocoder(getActivity(), Locale.KOREA);
                List<Address> address;
                if (geocoder != null) {
                    try {
                        address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        if (address != null && address.size() > 0) {
                            // 주소 받아오기
                            String currAddress = address.get(0).getAddressLine(0);
                            MessageUtils.showLongToastMsg(getActivity(), "선택한 " + currAddress + " 주소로 설정합니다.");
                            if (getAlarmActivity() != null) {
                                getAlarmActivity().setLoading("선택 한 주소 : " + currAddress);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlarmActivity aActivity = getAlarmActivity();
                                        if (aActivity != null && !aActivity.isFinishing()) {
                                            aActivity.hideProgressDialog();
                                            Intent intent = aActivity.getIntent();
                                            intent.putExtra("location", latLng);
                                            aActivity.setResult(Activity.RESULT_OK, intent);
                                            aActivity.onBackPressed();

                                        }
                                    }
                                }, 2000);
                            }

                        }
                    } catch (IOException e) {
                        MessageUtils.showLongToastMsg(getActivity(), "현재 주소를 가져오는데 실패하였습니다.");
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void moveToCurrentLocation(LatLng location) {
//        LatLng seoul = new LatLng(37.56, 126.97);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(location);
//        markerOptions.title("서울");
//        markerOptions.snippet("수도");
        mGoogleMap.addMarker(markerOptions);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        CameraPosition position = new CameraPosition.Builder().target(location)
                .zoom(16f)
                .bearing(0)
                .build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), Math.max(30, 1), null);

    }

    @Override
    public void onMapLoaded() {

    }

    public static class ReviewHolder extends RecyclerView.ViewHolder {
        TextView storeName;
        TextView food;
        TextView saleType;
        TextView salePercent;

        public ReviewHolder(View itemView) {
            super(itemView);
            storeName = itemView.findViewById(R.id.storenameText);
            food = itemView.findViewById(R.id.foodText);
            saleType = itemView.findViewById(R.id.salekindText);
            salePercent = itemView.findViewById(R.id.salepersantText);

        }
    }

    private class ReviewAdapter extends RecyclerView.Adapter<ReviewHolder> {

        ArrayList<SalesDto> sales = new ArrayList<>();

        @Override
        public ReviewHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_guest_main_sale_list_item, parent, false);
            return new ReviewHolder(view);
        }


        @Override
        public void onBindViewHolder(ReviewHolder rowViewHolder, final int pos) {
            L.e("pos = " + pos);
            SalesDto saleDto = sales.get(pos);
            Sales sale = saleDto.getSale();
            String [] locations  = sale.getStoreLocation().split(",");
            L.e("locations [0] = "+ locations[0] +", locations[1] = "+ locations[1]);
            LatLng location = new LatLng(Double.parseDouble(locations[0]), Double.parseDouble(locations[1]));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(location);
            markerOptions.title(sale.getStoreName());
            markerOptions.snippet(sale.getSaleType());
            mGoogleMap.addMarker(markerOptions);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            L.e("sale = "+ sale);
            rowViewHolder.storeName.setText(sale.getStoreName().isEmpty() ? "-" : sale.getStoreName());
            rowViewHolder.food.setText(sale.getMenu().isEmpty() ? "-" : sale.getMenu());
            rowViewHolder.saleType.setText(sale.getSaleType().isEmpty() ? "-" : sale.getSaleType());
            rowViewHolder.salePercent.setText(sale.getSalePercent().isEmpty() ? "-" : sale.getSalePercent());

            rowViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getAlarmActivity(), FoodViewerActivity.class);
                    ////////
                    ArrayList<SalesDto> list = new ArrayList<>();
                    list.add(saleDto);

                    String [] locations  = saleDto.getSale().getStoreLocation().split(",");
                    L.e("locations [0] = "+ locations[0] +", locations[1] = "+ locations[1]);
                    LatLng location = new LatLng(Double.parseDouble(locations[0]), Double.parseDouble(locations[1]));
                    moveToCurrentLocation(location);


                    intent.putExtra("sale", list);
                    getAlarmActivity().startActivityWithAni(intent);

                }
            });


        }

        @Override
        public int getItemCount() {
            return sales.size();
        }

        public void onRefresh(ArrayList<SalesDto> sales) {
            this.sales = sales;
            notifyDataSetChanged();
        }
    }
}


