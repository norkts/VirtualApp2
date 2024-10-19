package com.carlos.common.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.carlos.common.ui.activity.base.VActivity;
import com.carlos.common.utils.StringUtils;
import com.carlos.common.utils.location.CoordinateBean;
import com.carlos.common.utils.location.PositionConvertUtil;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import com.kook.librelease.R;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import com.kook.librelease.R.menu;
import com.kook.librelease.R.string;
import com.kook.librelease.R.style;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VirtualLocationManager;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.remote.vloc.VLocation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GDChooseLocationActivity extends VActivity implements GeocodeSearch.OnGeocodeSearchListener, Inputtips.InputtipsListener {
   private AMap mMap;
   private MapView mapView;
   private MenuItem mSearchMenuItem;
   private GeocodeSearch geocoderSearch;
   private View mSearchLayout;
   private TextView mLatText;
   private TextView mLngText;
   private TextView mAddressText;
   private ArrayAdapter<MapSearchResult> mSearchAdapter;
   private View mMockImg;
   private View mMockingView;
   private View mMockBtn;
   private View mSearchTip;
   private TextView mMockText;
   private String mCity;
   private String mCurPkg;
   private int mCurUserId;
   private VLocation mLocation;
   private boolean isFindLocation;
   private boolean mMocking;
   private String mAddress;

   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      this.setResult(0);
      this.setContentView(layout.activity_mock_location);
      Toolbar toolbar = (Toolbar)this.bind(id.top_toolbar);
      toolbar.setTitle(string.activity_choose_location);
      toolbar.setTitleTextColor(-1);
      this.setSupportActionBar(toolbar);
      this.enableBackHome();
      ListView mSearchResult = (ListView)this.bind(id.search_results);
      this.mapView = (MapView)this.findViewById(id.map);
      this.mLatText = (TextView)this.bind(id.tv_lat);
      this.mLngText = (TextView)this.bind(id.tv_lng);
      this.mMockImg = this.bind(id.img_mock);
      this.mMockText = (TextView)this.bind(id.tv_mock);
      this.mSearchLayout = this.bind(id.search_layout);
      this.mAddressText = (TextView)this.bind(id.tv_address);
      this.mMockingView = this.bind(id.img_stop);
      this.mMockBtn = this.findViewById(id.img_go_mock);
      this.mSearchTip = this.findViewById(id.tv_tip_search);
      this.mapView.onCreate(savedInstanceState);
      this.mMap = this.mapView.getMap();
      this.mSearchAdapter = new ArrayAdapter(this, 17367043, new ArrayList());
      mSearchResult.setAdapter(this.mSearchAdapter);
      this.geocoderSearch = new GeocodeSearch(this);
      this.geocoderSearch.setOnGeocodeSearchListener(this);
      GDChooseLocationActivity.MapSearchResult.NULL.setAddress(this.getString(string.tip_no_find_points));
      mSearchResult.setOnItemClickListener((adapterView, view, position, id) -> {
         MapSearchResult searchResult = (MapSearchResult)this.mSearchAdapter.getItem(position);
         if (searchResult != null && searchResult != GDChooseLocationActivity.MapSearchResult.NULL) {
            this.mSearchMenuItem.collapseActionView();
            this.gotoLocation(searchResult.address, searchResult.lat, searchResult.lng, true);
         }

      });
      this.mMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
         public void onCameraChange(CameraPosition cameraPosition) {
         }

         public void onCameraChangeFinish(CameraPosition cameraPosition) {
            GDChooseLocationActivity.this.gotoLocation((String)null, cameraPosition.target.latitude, cameraPosition.target.longitude, false);
         }
      });
      this.findViewById(id.img_stop_mock).setOnClickListener((v) -> {
         VirtualLocationManager.get().setMode(this.mCurUserId, this.mCurPkg, 0);
         this.updateMock(false);
         Intent intent = this.getIntent();
         this.mLocation.latitude = 0.0;
         this.mLocation.longitude = 0.0;
         intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgHx4oKi0qOWUzLCVlN1RF")), this.mLocation);
         intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgV1k/LRg2KG4gDSZsETgqIz4+IWIFSFo=")), this.mCurPkg);
         intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgV1k/LRg2KG4gDSZvDjAgKS4YIA==")), this.mCurUserId);
         intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgV1k/LRg2KG4gDSZoASwvKS4uD2EjSFo=")), this.mAddress);
         this.setResult(-1, intent);
      });
      this.mMockBtn.setOnClickListener((v) -> {
         if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCRjDiw7Lz0hDm4jMCxsNwYaLgQcCGMKQSBqEVRF")).equals(this.mCurPkg)) {
            CoordinateBean coordinateBean = PositionConvertUtil.gcj02ToWgs84(this.mLocation.getLatitude(), this.mLocation.getLongitude());
            this.mLocation.latitude = coordinateBean.getLatitude();
            this.mLocation.longitude = coordinateBean.getLongitude();
         }

         VLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ITw9DQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhgIQGozLDdmHgY1KjkmVg==")) + this.mLocation.getLatitude() + "  " + this.mLocation.getLongitude());
         VirtualCore.get().killApp(this.mCurPkg, this.mCurUserId);
         VirtualLocationManager.get().setMode(this.mCurUserId, this.mCurPkg, 2);
         VirtualLocationManager.get().setLocation(this.mCurUserId, this.mCurPkg, this.mLocation);
         this.updateMock(true);
         Intent intent = this.getIntent();
         intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgHx4oKi0qOWUzLCVlN1RF")), this.mLocation);
         intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgV1k/LRg2KG4gDSZsETgqIz4+IWIFSFo=")), this.mCurPkg);
         intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgV1k/LRg2KG4gDSZvDjAgKS4YIA==")), this.mCurUserId);
         intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgV1k/LRg2KG4gDSZoASwvKS4uD2EjSFo=")), this.mAddress);
         this.setResult(-1, intent);
      });
      this.findViewById(id.img_loc).setOnClickListener((v) -> {
         this.startLocation();
      });
      ((CheckBox)this.findViewById(id.checkbox)).setOnCheckedChangeListener((v, b) -> {
         this.showInputWindow();
      });
      this.mMockingView.setOnClickListener((v) -> {
      });
      this.mCurPkg = this.getIntent().getStringExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgV1k/LRg2KG4gDSZsETgqIz4+IWIFSFo=")));
      this.mCurUserId = this.getIntent().getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgV1k/LRg2KG4gDSZvDjAgKS4YIA==")), 0);
      VLocation vLocation = this.getIntent().hasExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgHx4oKi0qOWUzLCVlN1RF"))) ? (VLocation)this.getIntent().getParcelableExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgHx4oKi0qOWUzLCVlN1RF"))) : null;
      if (vLocation != null) {
         CoordinateBean coordinateBean = PositionConvertUtil.wgs84ToGcj02(vLocation.latitude, vLocation.longitude);
         VLocation mLatLng = new VLocation(coordinateBean.getLatitude(), coordinateBean.getLongitude());
         if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCRjDiw7Lz0hDm4jMCxsNwYaLgQcCGMKQSBqEVRF")).equals(this.mCurPkg)) {
            this.mLocation = mLatLng;
         } else {
            this.mLocation = vLocation;
         }

         this.updateMock(VirtualLocationManager.get().isUseVirtualLocation(this.mCurUserId, this.mCurPkg));
         this.gotoLocation((String)null, vLocation.getLatitude(), vLocation.getLongitude(), true);
      } else {
         this.mLocation = new VLocation();
         Location location = this.mMap.getMyLocation();
         if (location != null) {
            this.gotoLocation((String)null, location.getLatitude(), location.getLongitude(), false);
         }

         this.startLocation();
      }

   }

   public boolean onCreateOptionsMenu(Menu menu) {
      //FIXME
      this.getMenuInflater().inflate(R.menu.map_menu, menu);
      MenuItem menuItem = menu.findItem(id.action_search);
      this.mSearchMenuItem = menuItem;
      this.mSearchMenuItem.setEnabled(!this.mMocking);
      SearchView mSearchView = (SearchView)menuItem.getActionView();
      mSearchView.setImeOptions(3);
      mSearchView.setQueryHint(this.getString(string.tip_input_keywords));
      menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
         public boolean onMenuItemActionExpand(MenuItem item) {
            GDChooseLocationActivity.this.mSearchLayout.setVisibility(0);
            return true;
         }

         public boolean onMenuItemActionCollapse(MenuItem item) {
            GDChooseLocationActivity.this.mSearchLayout.setVisibility(8);
            return true;
         }
      });
      mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
         public boolean onQueryTextSubmit(String newText) {
            if (!TextUtils.isEmpty(newText)) {
               HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4uLWQFNDBmVgJF")) + newText);
               InputtipsQuery inputquery = new InputtipsQuery(newText, newText);
               Inputtips inputTips = new Inputtips(GDChooseLocationActivity.this, inputquery);
               inputTips.setInputtipsListener(GDChooseLocationActivity.this);
               inputTips.requestInputtipsAsyn();
            } else {
               GDChooseLocationActivity.this.mSearchAdapter.clear();
               GDChooseLocationActivity.this.mSearchAdapter.notifyDataSetChanged();
            }

            return true;
         }

         public boolean onQueryTextChange(String newText) {
            return true;
         }
      });
      return true;
   }

   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case 16908332:
            this.finish();
            return true;
         default:
            return super.onOptionsItemSelected(item);
      }
   }

   private void gotoLocation(String address, double lat, double lng, boolean move) {
      if (move) {
         this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 18.0F));
      } else {
         this.mLocation.latitude = StringUtils.doubleFor8(lat);
         this.mLocation.longitude = StringUtils.doubleFor8(lng);
         this.mLatText.setText(String.valueOf(this.mLocation.latitude));
         this.mLngText.setText(String.valueOf(this.mLocation.longitude));
      }

      if (TextUtils.isEmpty(address)) {
         LatLonPoint latLonPoint = new LatLonPoint(lat, lng);
         RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200.0F, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGozBjdmNAZF")));
         this.geocoderSearch.getFromLocationAsyn(query);
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxsBHEYvHytZEDkUAhpYKkcUWghBKAcTAAw3HVlbDzJEBTkBOD4iO29WBygaJ1RF")) + lat + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsFHiZiICci")) + lng);
      } else {
         this.setAddress(address);
      }

   }

   private void startLocation() {
      if (this.isFindLocation) {
         Toast.makeText(this, string.tip_find_location, 0).show();
      } else {
         this.isFindLocation = true;
      }
   }

   private void updateMock(boolean mock) {
      this.mMocking = mock;
      this.mMockImg.setSelected(mock);
      if (mock) {
         this.mMockText.setText(string.mocking);
         this.mMockingView.setVisibility(0);
         this.mMockBtn.setVisibility(8);
         if (this.mSearchMenuItem != null) {
            this.mSearchMenuItem.setEnabled(false);
         }
      } else {
         this.mMockText.setText(string.no_mock);
         this.mMockingView.setVisibility(8);
         this.mMockBtn.setVisibility(0);
         if (this.mSearchMenuItem != null) {
            this.mSearchMenuItem.setEnabled(true);
         }
      }

      this.mMockText.setSelected(mock);
   }

   private void setAddress(String text) {
      this.runOnUiThread(() -> {
         this.mAddress = text;
         this.mAddressText.setText(text);
      });
   }

   protected void onResume() {
      super.onResume();
      this.mapView.onResume();
   }

   protected void onPause() {
      super.onPause();
      this.mapView.onPause();
   }

   protected void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);
      this.mapView.onSaveInstanceState(outState);
   }

   protected void onDestroy() {
      super.onDestroy();
      this.mapView.onDestroy();
   }

   private void showInputWindow() {
      AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(), style.VACustomTheme);
      View view1 = this.getLayoutInflater().inflate(layout.dialog_change_loc, (ViewGroup)null);
      builder.setView(view1);
      Dialog dialog = builder.show();
      ((Dialog)dialog).setCanceledOnTouchOutside(false);
      EditText editText1 = (EditText)view1.findViewById(id.edt_lat);
      editText1.setText(StringUtils.doubleFor8String(this.mLocation.getLatitude()));
      EditText editText2 = (EditText)view1.findViewById(id.edt_lon);
      editText2.setText(StringUtils.doubleFor8String(this.mLocation.getLongitude()));
      ((Dialog)dialog).setCancelable(false);
      view1.findViewById(id.btn_cancel).setOnClickListener((v2) -> {
         dialog.dismiss();
      });
      view1.findViewById(id.btn_ok).setOnClickListener((v2) -> {
         dialog.dismiss();

         try {
            double lat = Double.parseDouble(editText1.getText().toString());
            double lon = Double.parseDouble(editText2.getText().toString());
            this.gotoLocation((String)null, lat, lon, true);
         } catch (Exception var9) {
            Toast.makeText(this, string.input_loc_error, 0).show();
         }

      });
   }

   public void onGeocodeSearched(GeocodeResult result, int rCode) {
   }

   public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
      HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uKWwVHgZLVgJF")) + result + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsKFhNgJAo/PT5SVg==")) + rCode);
      if (rCode == 1000 && result != null && result.getRegeocodeAddress() != null && result.getRegeocodeAddress().getFormatAddress() != null) {
         String addressName = result.getRegeocodeAddress().getFormatAddress() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BhkVWUMWB0g="));
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggqPG8jNANhIlk7KgcLOngVSFo=")) + addressName);
         this.mCity = result.getRegeocodeAddress().getFormatAddress();
         this.setAddress(addressName);
      }

   }

   public void onGetInputtips(List<Tip> list, int rCode) {
      HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4cWmgaMAlgNyQvLBg2MWowDSh+N1RF")) + list + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsKFhNgJAo/PT5SVg==")) + rCode);
      if (rCode == 1000) {
         if (this.mSearchTip.getVisibility() != 8) {
            this.runOnUiThread(() -> {
               this.mSearchTip.setVisibility(8);
            });
         }

         this.mSearchAdapter.clear();
         if (list.size() == 0) {
            this.mSearchAdapter.add(GDChooseLocationActivity.MapSearchResult.NULL);
         } else {
            Iterator var3 = list.iterator();

            while(var3.hasNext()) {
               Tip item = (Tip)var3.next();
               MapSearchResult result = new MapSearchResult(item.getAddress(), item.getPoint().getLatitude(), item.getPoint().getLongitude());
               result.setCity(item.getAddress());
               this.mSearchAdapter.add(result);
            }
         }

         this.mSearchAdapter.notifyDataSetChanged();
      }

   }

   private static class MapSearchResult {
      private static final MapSearchResult NULL = new MapSearchResult();
      private String address;
      private double lat;
      private double lng;
      private String city;

      private MapSearchResult() {
      }

      public MapSearchResult(String address) {
         this.address = address;
      }

      private MapSearchResult(String address, double lat, double lng) {
         this.address = address;
         this.lat = lat;
         this.lng = lng;
      }

      private void setAddress(String address) {
         this.address = address;
      }

      private void setCity(String city) {
         this.city = city;
      }

      public String toString() {
         return this.address;
      }

      // $FF: synthetic method
      MapSearchResult(String x0, double x1, double x2, Object x3) {
         this(x0, x1, x2);
      }
   }
}
