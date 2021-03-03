
package com.cynoteck.petofyOPHR.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cynoteck.petofyOPHR.R;
import com.cynoteck.petofyOPHR.adapters.ImmunizationHistoryAdopter;
import com.cynoteck.petofyOPHR.adapters.VaccineTypeAdapter;
import com.cynoteck.petofyOPHR.api.ApiClient;
import com.cynoteck.petofyOPHR.api.ApiResponse;
import com.cynoteck.petofyOPHR.api.ApiService;
import com.cynoteck.petofyOPHR.params.getVaccinationDetails.GetVaccinationModelParameter;
import com.cynoteck.petofyOPHR.params.getVaccinationDetails.GetVaccinationRequest;
import com.cynoteck.petofyOPHR.params.petReportsRequest.PetClinicVisitDetailsRequest;
import com.cynoteck.petofyOPHR.params.petReportsRequest.PetClinicVistsDetailsParams;
import com.cynoteck.petofyOPHR.params.vaccinationSaveParams.VaccinationParameter;
import com.cynoteck.petofyOPHR.params.vaccinationSaveParams.VaccinationRequest;
import com.cynoteck.petofyOPHR.response.getLastPrescriptionResponse.GetLastPrescriptionResponse;
import com.cynoteck.petofyOPHR.response.getPetReportsResponse.getClinicVisitDetails.GetClinicVisitsDetailsResponse;
import com.cynoteck.petofyOPHR.response.getVaccinationResponse.GetVaccineResponse;
import com.cynoteck.petofyOPHR.response.getVaccinationResponse.GetVaccineResponseModel;
import com.cynoteck.petofyOPHR.response.immuniztionHistory.ImmunizationHistoryResponse;
import com.cynoteck.petofyOPHR.response.immuniztionHistory.ImmunizationHistorymodel;
import com.cynoteck.petofyOPHR.response.loginRegisterResponse.UserPermissionMasterList;
import com.cynoteck.petofyOPHR.response.saveImmunizationData.SaveImmunizationResponse;
import com.cynoteck.petofyOPHR.response.staffPermissionListResponse.CheckStaffPermissionResponse;
import com.cynoteck.petofyOPHR.utils.Config;
import com.cynoteck.petofyOPHR.utils.ImmunizationOnclickListener;
import com.cynoteck.petofyOPHR.utils.Methods;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Response;

public class PetDetailsActivity extends AppCompatActivity implements View.OnClickListener, ApiResponse, ImmunizationOnclickListener {
    String pet_id, pet_name, patent_name, pet_bread, pet_unique_id = "", pet_sex = "", pet_age = "", pet_DOB = "", pet_encrypted_id = "", lastVisitEncryptedId = "", pet_cat_id = "";
    TextView pet_nameTV, pet_parentNameTV, pet_id_TV;
    ImageView back_arrow_IV, view_clinicVisits_arrow, view_xrayReport_arrow, view_labTestReport_arrow, view_Hospitalization_arrow, last_prescription_arrow, recent_visits_arrow, print_id_card_arrow, view_history_arrow;
    CardView clinic_test, xray_test, lab_test_report, hospitalization_sugeries, last_prescription, print_id_card, view_history, immunization_CV;
    Methods methods;
    WebView webview;

    ArrayList<String> nextVisitList = new ArrayList<>();
    private ArrayList<ImmunizationHistorymodel> getImmunizationHistory;
    private ArrayList<GetVaccineResponseModel> getVaccineResponseModels;
    HashMap<String, String> nextVisitHas = new HashMap<>();

    private ArrayList<String> nextVisitDateList;
    private ArrayList<String> vaccineClassList;
    private ArrayList<String> vaccineList;
    private ArrayList<String> immunizationDateList;

    VaccineTypeAdapter vaccineTypeAdapter;
    ImmunizationHistoryAdopter immunizationHistoryAdopter;
    SharedPreferences sharedPreferences;
    String permissionId = "";
    Dialog vaccineDetailsDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details);
        methods = new Methods(this);
        Bundle extras = getIntent().getExtras();
        pet_id = extras.getString("pet_id");
        pet_name = extras.getString("pet_name");
        patent_name = extras.getString("pet_parent");
        pet_unique_id = extras.getString("pet_unique_id");
        pet_sex = extras.getString("pet_sex");
        pet_age = extras.getString("pet_age");
        pet_DOB = extras.getString("pet_DOB");
        pet_encrypted_id = extras.getString("pet_encrypted_id");
        pet_cat_id = extras.getString("pet_cat_id");
        lastVisitEncryptedId = extras.getString("lastVisitEncryptedId");
        Log.d("PET_DETAILS", "" + pet_DOB + " " + pet_id + " " + " " + pet_encrypted_id + " " + pet_cat_id + " " + lastVisitEncryptedId);

        pet_nameTV = findViewById(R.id.pet_name_TV);
        pet_parentNameTV = findViewById(R.id.pet_owner_name_TV);
        back_arrow_IV = findViewById(R.id.back_arrow_IV);
        pet_id_TV = findViewById(R.id.pet_id_TV);
        view_Hospitalization_arrow = findViewById(R.id.view_Hospitalization_arrow);
        hospitalization_sugeries = findViewById(R.id.hospitalization_sugeries);
        clinic_test = findViewById(R.id.clinic_test);
        recent_visits_arrow = findViewById(R.id.recent_visits_arrow);
        print_id_card_arrow = findViewById(R.id.print_id_card_arrow);
        print_id_card = findViewById(R.id.print_id_card);
        view_clinicVisits_arrow = findViewById(R.id.view_clinicVisits_arrow);
        last_prescription = findViewById(R.id.last_prescription);
        view_history_arrow = findViewById(R.id.view_history_arrow);
        view_history = findViewById(R.id.view_history);
        immunization_CV = findViewById(R.id.immunization_CV);
        view_xrayReport_arrow = findViewById(R.id.view_xrayReport_arrow);
        xray_test = findViewById(R.id.xray_test);
        view_labTestReport_arrow = findViewById(R.id.view_labTestReport_arrow);
        lab_test_report = findViewById(R.id.lab_test_report);
        last_prescription_arrow = findViewById(R.id.last_prescription_arrow);
        webview = findViewById(R.id.webview);

        view_clinicVisits_arrow.setOnClickListener(this);
        view_labTestReport_arrow.setOnClickListener(this);
        lab_test_report.setOnClickListener(this);
        view_Hospitalization_arrow.setOnClickListener(this);
        hospitalization_sugeries.setOnClickListener(this);
        clinic_test.setOnClickListener(this);
        recent_visits_arrow.setOnClickListener(this);
        print_id_card_arrow.setOnClickListener(this);
        print_id_card.setOnClickListener(this);
        view_history_arrow.setOnClickListener(this);
        view_history.setOnClickListener(this);
        immunization_CV.setOnClickListener(this);
        back_arrow_IV.setOnClickListener(this);
        last_prescription.setOnClickListener(this);
        view_xrayReport_arrow.setOnClickListener(this);
        xray_test.setOnClickListener(this);
        sharedPreferences = getSharedPreferences("userdetails", 0);

        if (methods.isInternetOn()) {
            methods.showCustomProgressBarDialog(this);
            getImmunizationHistory();
            getVaccinationDetails();
        } else {
            methods.DialogInternet();
        }

        pet_nameTV.setText(pet_name.substring(0, 1).toUpperCase() + pet_name.substring(1) + "(" + pet_sex + ")");
        pet_parentNameTV.setText(patent_name);
        pet_id_TV.setText(pet_unique_id);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xray_test:
                Intent petDetailsXray = new Intent(this, NewEntrysDetailsActivity.class);
                Bundle dataXray = new Bundle();
                dataXray.putString("pet_id", pet_id);
                dataXray.putString("pet_name", pet_name);
                dataXray.putString("pet_parent", patent_name);
                dataXray.putString("pet_unique_id", pet_unique_id);
                dataXray.putString("reports_id", "7.0");
                dataXray.putString("pet_sex", pet_sex);
                dataXray.putString("pet_age", pet_age);
                dataXray.putString("pet_DOB", pet_DOB);
                dataXray.putString("pet_encrypted_id", pet_encrypted_id);
                dataXray.putString("add_button_text", "Test/X-rays");
                dataXray.putString("button_type", "update");
                petDetailsXray.putExtras(dataXray);
                startActivity(petDetailsXray);
                break;
            case R.id.lab_test_report:
                Intent petDetailsLabWork = new Intent(this, NewEntrysDetailsActivity.class);
                Bundle dataLabwork = new Bundle();
                dataLabwork.putString("pet_id", pet_id);
                dataLabwork.putString("pet_name", pet_name);
                dataLabwork.putString("pet_parent", patent_name);
                dataLabwork.putString("pet_unique_id", pet_unique_id);
                dataLabwork.putString("reports_id", "8.0");
                dataLabwork.putString("pet_sex", pet_sex);
                dataLabwork.putString("pet_age", pet_age);
                dataLabwork.putString("pet_DOB", pet_DOB);
                dataLabwork.putString("pet_encrypted_id", pet_encrypted_id);
                dataLabwork.putString("button_type", "update");
                dataLabwork.putString("add_button_text", "Lab Work");
                petDetailsLabWork.putExtras(dataLabwork);
                startActivity(petDetailsLabWork);
                break;
            case R.id.hospitalization_sugeries:
                Intent petDetailsHospitalization = new Intent(this, NewEntrysDetailsActivity.class);
                Bundle dataLabworkHospitalization = new Bundle();
                dataLabworkHospitalization.putString("pet_id", pet_id);
                dataLabworkHospitalization.putString("pet_name", pet_name);
                dataLabworkHospitalization.putString("pet_parent", patent_name);
                dataLabworkHospitalization.putString("pet_unique_id", pet_unique_id);
                dataLabworkHospitalization.putString("reports_id", "9.0");
                dataLabworkHospitalization.putString("pet_sex", pet_sex);
                dataLabworkHospitalization.putString("pet_age", pet_age);
                dataLabworkHospitalization.putString("pet_DOB", pet_DOB);
                dataLabworkHospitalization.putString("pet_encrypted_id", pet_encrypted_id);
                dataLabworkHospitalization.putString("button_type", "update");
                dataLabworkHospitalization.putString("add_button_text", "Hospitalization");
                petDetailsHospitalization.putExtras(dataLabworkHospitalization);
                startActivity(petDetailsHospitalization);
                break;
            case R.id.view_history:
                Intent petHistory = new Intent(this, SelectPetReportsActivity.class);
                Bundle dataHistry = new Bundle();
//        Toast.makeText(getContext(), ""+profileList.get(position).getId(), Toast.LENGTH_SHORT).show();
                dataHistry.putString("pet_id", pet_id.substring(0, pet_id.length() - 2));
                dataHistry.putString("pet_name",pet_name);
                dataHistry.putString("pet_unique_id",pet_unique_id);
                dataHistry.putString("pet_sex",pet_sex);
                dataHistry.putString("pet_owner_name",patent_name);
                dataHistry.putString("pet_owner_contact","");
                dataHistry.putString("pet_DOB",pet_DOB);
                dataHistry.putString("pet_encrypted_id",pet_encrypted_id);
                petHistory.putExtras(dataHistry);
                startActivity(petHistory);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);

                break;
            case R.id.immunization_CV:
                vaccineDetailsDialog();
                break;
            case R.id.recent_visits_arrow:
                Intent petDetailsLabVisits = new Intent(this, NewEntrysDetailsActivity.class);
                Bundle dataLabworkVisits = new Bundle();
                dataLabworkVisits.putString("pet_id", pet_id);
                dataLabworkVisits.putString("pet_name", pet_name);
                dataLabworkVisits.putString("pet_parent", patent_name);
                dataLabworkVisits.putString("pet_unique_id", pet_unique_id);
                dataLabworkVisits.putString("reports_id", "11.0");
                dataLabworkVisits.putString("pet_sex", pet_sex);
                dataLabworkVisits.putString("pet_age", pet_age);
                dataLabworkVisits.putString("pet_DOB", pet_DOB);
                dataLabworkVisits.putString("pet_encrypted_id", pet_encrypted_id);
                dataLabworkVisits.putString("add_button_text", "RecentVisit");
                petDetailsLabVisits.putExtras(dataLabworkVisits);
                startActivity(petDetailsLabVisits);
                break;
            case R.id.print_id_card:
                Intent intent = new Intent(this, PetIdCardActivity.class);
                Bundle dataLabworkIdCard = new Bundle();
                dataLabworkIdCard.putString("id", pet_id);
                intent.putExtras(dataLabworkIdCard);
                startActivity(intent);
                break;
            case R.id.clinic_test:
                String userTYpe = sharedPreferences.getString("user_type", "");
                if (userTYpe.equals("Vet Staff")) {
                    Gson gson = new Gson();
                    String json = sharedPreferences.getString("userPermission", null);
                    Type type = new TypeToken<ArrayList<UserPermissionMasterList>>() {
                    }.getType();
                    ArrayList<UserPermissionMasterList> arrayList = gson.fromJson(json, type);
                    Log.e("ArrayList", arrayList.toString());
                    Log.d("UserType", userTYpe);
                    permissionId = "6";
                    methods.showCustomProgressBarDialog(this);
                    String url = "user/CheckStaffPermission/" + permissionId;
                    Log.e("URL", url);
                    ApiService<CheckStaffPermissionResponse> service = new ApiService<>();
                    service.get(this, ApiClient.getApiInterface().getCheckStaffPermission(Config.token, url), "CheckPermission");
                } else if (userTYpe.equals("Veterinarian")) {
                    Intent petDetailsClinicVisits = new Intent(this, NewEntrysDetailsActivity.class);
                    Bundle dataClinicVisits = new Bundle();
                    dataClinicVisits.putString("pet_id", pet_id);
                    dataClinicVisits.putString("pet_name", pet_name);
                    dataClinicVisits.putString("pet_parent", patent_name);
                    dataClinicVisits.putString("pet_unique_id", pet_unique_id);
                    dataClinicVisits.putString("reports_id", "1.0");
                    dataClinicVisits.putString("pet_sex", pet_sex);
                    dataClinicVisits.putString("pet_age", pet_age);
                    dataClinicVisits.putString("add_button_text", "Clinic Visits");
                    dataClinicVisits.putString("pet_DOB", pet_DOB);
                    dataClinicVisits.putString("pet_encrypted_id", pet_encrypted_id);
                    dataClinicVisits.putString("pet_cat_id", pet_cat_id);
                    petDetailsClinicVisits.putExtras(dataClinicVisits);
                    startActivity(petDetailsClinicVisits);
                }

                break;
            case R.id.last_prescription:
                if (lastVisitEncryptedId==null) {
                    Toast.makeText(this, "No record found!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent lastPrescriptionIntent = new Intent(this, PdfEditorActivity.class);
                    lastPrescriptionIntent.putExtra("encryptId", lastVisitEncryptedId);
                    startActivity(lastPrescriptionIntent);
                }
                break;
            case R.id.back_arrow_IV:
                onBackPressed();
                Config.backCall = "hitUnique";
                break;
        }

    }

    private void getImmunizationHistory() {
        GetVaccinationModelParameter getVaccinationModelParameter = new GetVaccinationModelParameter();
        getVaccinationModelParameter.setEncryptedId(pet_encrypted_id);
        GetVaccinationRequest getVaccinationRequest = new GetVaccinationRequest();
        getVaccinationRequest.setData(getVaccinationModelParameter);
        ApiService<ImmunizationHistoryResponse> service = new ApiService<>();
        service.get(this, ApiClient.getApiInterface().getPetImmunizationHistory(getVaccinationRequest), "GetPetImmunizationHistory");
        Gson gson = new Gson();
        String getImmunizationHistory = gson.toJson(getVaccinationRequest);
        Log.e("getImmunHistory=>", "" + getImmunizationHistory);
    }

    private void getVaccinationDetails() {
        GetVaccinationModelParameter getVaccinationModelParameter = new GetVaccinationModelParameter();
        getVaccinationModelParameter.setEncryptedId(pet_encrypted_id);
        GetVaccinationRequest getVaccinationRequest = new GetVaccinationRequest();
        getVaccinationRequest.setData(getVaccinationModelParameter);
        ApiService<GetVaccineResponse> service = new ApiService<>();
        service.get(this, ApiClient.getApiInterface().getVaccinationScheduleChart(Config.token, getVaccinationRequest), "GetVaccinationScheduleChart");
        Log.e("GetVaccinSchedule==>", "" + getVaccinationRequest);
        Gson gson = new Gson();
        String getVaccinationDetails = gson.toJson(getVaccinationRequest);
        Log.e("getVaccinationDetails=>", "" + getVaccinationDetails);

    }

    private void getclinicVisitsReportDetails() {
        PetClinicVistsDetailsParams petClinicVistsDetailsParams = new PetClinicVistsDetailsParams();
        petClinicVistsDetailsParams.setId(pet_id.substring(0, pet_id.length() - 2));
        PetClinicVisitDetailsRequest petClinicVisitDetailsRequest = new PetClinicVisitDetailsRequest();
        petClinicVisitDetailsRequest.setData(petClinicVistsDetailsParams);
        Log.e("petClinicVisitDetail", methods.getRequestJson(petClinicVisitDetailsRequest));
        ApiService<JsonObject> service = new ApiService<>();
        service.get(this, ApiClient.getApiInterface().getLastPrescriptionUrl(Config.token, petClinicVisitDetailsRequest), "LastPrescriptionUrl");
    }

    private void saveVaccineData(VaccinationRequest vaccinationRequest) {
        ApiService<SaveImmunizationResponse> service = new ApiService<>();
        service.get(this, ApiClient.getApiInterface().saveImmunizationDetails(Config.token, vaccinationRequest), "SaveImmunizationDetails");
        Log.e("SaveVaccinSchedule==>", "" + vaccinationRequest);
    }

    @Override
    public void onResponse(Response arg0, String key) {
        Log.d("kkakakak", "" + key + " response: " + arg0);
        switch (key) {
            case "CheckPermission":
                try {
                    methods.customProgressDismiss();
                    CheckStaffPermissionResponse checkStaffPermissionResponse = (CheckStaffPermissionResponse) arg0.body();
                    Log.d("GetPetList", checkStaffPermissionResponse.toString());
                    int responseCode = Integer.parseInt(checkStaffPermissionResponse.getResponse().getResponseCode());

                    if (responseCode == 109) {
                        if (checkStaffPermissionResponse.getData().equals("true")) {
                            if (permissionId.equals("6")) {
                                Intent petDetailsClinicVisits = new Intent(this, NewEntrysDetailsActivity.class);
                                Bundle dataClinicVisits = new Bundle();
                                dataClinicVisits.putString("pet_id", pet_id);
                                dataClinicVisits.putString("pet_name", pet_name);
                                dataClinicVisits.putString("pet_parent", patent_name);
                                dataClinicVisits.putString("pet_unique_id", pet_unique_id);
                                dataClinicVisits.putString("reports_id", "1.0");
                                dataClinicVisits.putString("pet_sex", pet_sex);
                                dataClinicVisits.putString("pet_age", pet_age);
                                dataClinicVisits.putString("add_button_text", "Clinic Visits");
                                dataClinicVisits.putString("pet_DOB", pet_DOB);
                                dataClinicVisits.putString("pet_encrypted_id", pet_encrypted_id);
                                dataClinicVisits.putString("pet_cat_id", pet_cat_id);
                                dataClinicVisits.putString("lastVisitEncryptedId", lastVisitEncryptedId);
                                petDetailsClinicVisits.putExtras(dataClinicVisits);
                                startActivity(petDetailsClinicVisits);
                            } else if (permissionId.equals("7")) {
                                if (methods.isInternetOn()) {
                                    getclinicVisitsReportDetails();

                                } else {
                                    methods.DialogInternet();
                                }
                            }
                        } else {
                            Toast.makeText(this, "Permission not Granted!!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Please Try Again!!", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;


            case "GetPetClinicVisitDetails":
                try {
                    methods.customProgressDismiss();
                    Log.d("ResponseClinicVisit", arg0.body().toString());
                    GetLastPrescriptionResponse getClinicVisitsDetailsResponse = (GetLastPrescriptionResponse) arg0.body();
                    int responseCode = Integer.parseInt(getClinicVisitsDetailsResponse.getResponse().getResponseCode());
                    if (responseCode == 109) {
                        if (getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getNatureOfVisitId().equals("1.0")) {
                            String str = methods.pdfGenarator(getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getPetName(),
                                    getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getPetAge(),
                                    getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getPetSex(),
                                    getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getPetParentName(),
                                    getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getTemperature(),
                                    getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getHistory(),
                                    getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getDescription(),
                                    getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getDiagnosisProcedure(),
                                    getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getTreatmentRemarks(),
                                    getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getFollowUpDate(),
                                    getClinicVisitsDetailsResponse.getData().getVeterinarianDetails().getVetRegistrationNumber());
                            webview.loadDataWithBaseURL(null, str, "text/html", "utf-8", null);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    methods.customProgressDismiss();
                                    Context context = PetDetailsActivity.this;
                                    PrintManager printManager = (PrintManager) getSystemService(context.PRINT_SERVICE);
                                    PrintDocumentAdapter adapter = null;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                        adapter = webview.createPrintDocumentAdapter();
                                    }
                                    String JobName = getString(R.string.app_name) + "Document";
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                        PrintJob printJob = printManager.print(JobName, adapter, new PrintAttributes.Builder().build());
                                    }
                                }
                            }, 500);

                        } else if (getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getNatureOfVisitId().equals("5.0")) {
                            String str = methods.pdfGenaratorDeworming(getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getPetName(),
                                    getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getPetAge(),
                                    getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getPetSex(),
                                    getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getPetParentName(),
                                    getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getTemperature(),
                                    getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getDescription(),
                                    getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getHistory(),
                                    getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getDewormerName(),
                                    getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getDewormerDose(),
                                    getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getTreatmentRemarks(),
                                    getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getFollowUpDate(),
                                    getClinicVisitsDetailsResponse.getData().getVeterinarianDetails().getVetRegistrationNumber(),
                                    getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getWeightLbs()
                            );
                            webview.loadDataWithBaseURL(null, str, "text/html", "utf-8", null);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    methods.customProgressDismiss();
                                    Context context = PetDetailsActivity.this;
                                    PrintManager printManager = (PrintManager) getSystemService(context.PRINT_SERVICE);
                                    PrintDocumentAdapter adapter = null;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                        adapter = webview.createPrintDocumentAdapter();
                                    }
                                    String JobName = getString(R.string.app_name) + "Document";
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                        PrintJob printJob = printManager.print(JobName, adapter, new PrintAttributes.Builder().build());
                                    }
                                }
                            }, 500);
                        } else if (getClinicVisitsDetailsResponse.getData().getPetClinicVisitDetails().getNatureOfVisitId().equals("4.0")) {
                            ArrayList<String> immunizationDate = new ArrayList<>();
                            ArrayList<String> vaccineClass = new ArrayList<>();
                            ArrayList<String> nextDueDate = new ArrayList<>();
                            ArrayList<String> vaccineType = new ArrayList<>();

                            ArrayList<String> immunizationDatePending = new ArrayList<>();
                            ArrayList<String> vaccineClassPending = new ArrayList<>();
                            ArrayList<String> nextDueDatePending = new ArrayList<>();
                            ArrayList<String> vaccineTypePending = new ArrayList<>();

                            for (int i = 0; i < getClinicVisitsDetailsResponse.getData().getPendingVaccinations().size(); i++) {
                                if (getClinicVisitsDetailsResponse.getData().getPendingVaccinations().get(i).getIsVaccinated().equals("true")) {
                                    immunizationDate.add(getClinicVisitsDetailsResponse.getData().getPendingVaccinations().get(i).getVaccinationDate());
                                    vaccineClass.add(getClinicVisitsDetailsResponse.getData().getPendingVaccinations().get(i).getVaccineName());
                                    nextDueDate.add(getClinicVisitsDetailsResponse.getData().getPendingVaccinations().get(i).getNextVaccinationDate().substring(0, getClinicVisitsDetailsResponse.getData().getPendingVaccinations().get(i).getNextVaccinationDate().length() - 9));
                                    vaccineType.add(getClinicVisitsDetailsResponse.getData().getPendingVaccinations().get(i).getVaccineType());

                                } else {
                                    immunizationDatePending.add(getClinicVisitsDetailsResponse.getData().getPendingVaccinations().get(i).getVaccinationDate());
                                    vaccineClassPending.add(getClinicVisitsDetailsResponse.getData().getPendingVaccinations().get(i).getVaccineName());
                                    nextDueDatePending.add(getClinicVisitsDetailsResponse.getData().getPendingVaccinations().get(i).getNextVaccinationDate().substring(0, getClinicVisitsDetailsResponse.getData().getPendingVaccinations().get(i).getNextVaccinationDate().length() - 9));
                                    vaccineTypePending.add(getClinicVisitsDetailsResponse.getData().getPendingVaccinations().get(i).getVaccineType());

                                }
                            }
                            final JSONArray date = new JSONArray(immunizationDate);
                            final JSONArray vaccine = new JSONArray(vaccineClass);
                            final JSONArray nextDate = new JSONArray(nextDueDate);
                            final JSONArray vType = new JSONArray(vaccineType);

                            Log.d("jsjsjjsjs", "" + date.length());

                            final JSONArray datePending = new JSONArray(immunizationDatePending);
                            final JSONArray vaccinePending = new JSONArray(vaccineClassPending);
                            final JSONArray nextDatePending = new JSONArray(nextDueDatePending);
                            final JSONArray vTypePending = new JSONArray(vaccineTypePending);

                            Log.d("jsjsjjsjs", "" + datePending.length());

                            Log.e("aaaaaa", vaccineClass.toString());
                            Log.e("aaaaaa", vaccine.toString());
                            methods.customProgressDismiss();
                            String immunizationSet = methods.immunizationPdfGenarator(pet_name, pet_age, pet_sex, pet_parentNameTV.getText().toString(), Config.user_verterian_reg_no, vType, vaccine, nextDate, vTypePending, vaccinePending, nextDatePending);
                            WebSettings webSettings = webview.getSettings();
                            webSettings.setJavaScriptEnabled(true);
                            webview.loadDataWithBaseURL(null, immunizationSet, "text/html", "utf-8", null);
                            new Handler().postDelayed(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void run() {
                                    Context context = PetDetailsActivity.this;
                                    PrintManager printManager = (PrintManager) PetDetailsActivity.this.getSystemService(context.PRINT_SERVICE);
                                    PrintDocumentAdapter adapter = null;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                        adapter = webview.createPrintDocumentAdapter();
                                    }
                                    String JobName = getString(R.string.app_name) + "Document";
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                        PrintJob printJob = printManager.print(JobName, adapter, new PrintAttributes.Builder().build());
                                    }
                                }
                            }, 500);

                        }

                    } else if (responseCode == 404) {
                        Toast.makeText(this, getClinicVisitsDetailsResponse.getResponse().getResponseMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Please try again!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "GetPetImmunizationHistory":
                try {
                    Log.d("GetImmuniHistory", arg0.body().toString());
                    ImmunizationHistoryResponse immunizationHistoryResponse = (ImmunizationHistoryResponse) arg0.body();
                    int responseCode = Integer.parseInt(immunizationHistoryResponse.getResponse().getResponseCode());
                    if (responseCode == 109) {
                        getImmunizationHistory = immunizationHistoryResponse.getData();

                        nextVisitDateList = new ArrayList<>();
                        vaccineClassList = new ArrayList<>();
                        vaccineList = new ArrayList<>();
                        immunizationDateList = new ArrayList<>();

                        for (int outer = 0; outer < immunizationHistoryResponse.getData().size(); outer++) {
                            if (immunizationHistoryResponse.getData().get(outer).getPetVaccinationDetail().size() > 0) {
                                for (int inner = 0; inner < immunizationHistoryResponse.getData().get(outer).getPetVaccinationDetail().size(); inner++) {
                                    nextVisitDateList.add(immunizationHistoryResponse.getData().get(outer).getFollowUpDate());
                                    vaccineClassList.add(immunizationHistoryResponse.getData().get(outer).getPetVaccinationDetail().get(inner).getVaccineType());
                                    vaccineList.add(immunizationHistoryResponse.getData().get(outer).getPetVaccinationDetail().get(inner).getVaccine());
                                    immunizationDateList.add(immunizationHistoryResponse.getData().get(outer).getPetVaccinationDetail().get(inner).getImmunizationDate());
                                }

                            }
                        }
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "GetVaccinationScheduleChart":
                try {
                    methods.customProgressDismiss();
                    Log.d("GetVaccinationSchedul", arg0.body().toString());
                    GetVaccineResponse getVaccineResponse = (GetVaccineResponse) arg0.body();
                    int responseCode = Integer.parseInt(getVaccineResponse.getResponse().getResponseCode());
                    if (responseCode == 109) {
                        Log.e("amamamammamamam", "Vaccine Detailsss" + getVaccineResponse.getData().size());
                        getVaccineResponseModels = getVaccineResponse.getData();
                        Log.e("VaccineResponseModels", methods.getRequestJson(arg0.body()));
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "SaveImmunizationDetails":
                try {
                    Log.d("SaveImmunizationDetails", arg0.body().toString());
                    SaveImmunizationResponse getVaccineResponse = (SaveImmunizationResponse) arg0.body();
                    int responseCode = Integer.parseInt(getVaccineResponse.getResponse().getResponseCode());
                    if (responseCode == 109) {
                        Toast.makeText(this, "Save Successfully", Toast.LENGTH_SHORT).show();
                        vaccineDetailsDialog.dismiss();
                    } else {
                        Toast.makeText(this, "Not Save", Toast.LENGTH_SHORT).show();
                        vaccineDetailsDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "LastPrescriptionUrl":
                try {
                    Log.d("LastPrescriptionUrl", arg0.body().toString());
                    JsonObject lastPrescriptionUrl = (JsonObject) arg0.body();
                    JsonObject response = lastPrescriptionUrl.getAsJsonObject("response");
                    Log.d("hhshshhs", "" + response);

                    int responseCode = Integer.parseInt(String.valueOf(response.get("responseCode")));

                    if (responseCode == 109) {
                        String urlData = String.valueOf(lastPrescriptionUrl.get("data"));
                        Log.e("URL", urlData);
                        Intent intent = new Intent(this, PdfEditorActivity.class);
                        intent.putExtra("URL", urlData);
                        startActivity(intent);
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    private void vaccineDetailsDialog() {
        vaccineDetailsDialog = new Dialog(this);
        vaccineDetailsDialog.setContentView(R.layout.vaccine_deatils_dilog);
        CardView age_group_CV = vaccineDetailsDialog.findViewById(R.id.age_group_CV);
        CardView periodic_vaccine_CV = vaccineDetailsDialog.findViewById(R.id.periodic_vaccine_CV);
        CardView history_CV = vaccineDetailsDialog.findViewById(R.id.history_CV);
        final LinearLayout immunization_history_layout = vaccineDetailsDialog.findViewById(R.id.immunization_history_layout);
        final TextView age_group_TV = vaccineDetailsDialog.findViewById(R.id.age_group_TV);
        final TextView periodic_vaccine_TV = vaccineDetailsDialog.findViewById(R.id.periodic_vaccine_TV);
        final TextView history_TV = vaccineDetailsDialog.findViewById(R.id.history_TV);

        ImageView clinic_back_arrow_IV = vaccineDetailsDialog.findViewById(R.id.clinic_back_arrow_IV);
        final RecyclerView vaccine_type_name_list = vaccineDetailsDialog.findViewById(R.id.vaccine_type_name_list);
        final RecyclerView pereodic_list = vaccineDetailsDialog.findViewById(R.id.pereodic_list);
        final RecyclerView immunization_history_list = vaccineDetailsDialog.findViewById(R.id.immunization_history_list);

        pereodic_list.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PetDetailsActivity.this);
        vaccine_type_name_list.setLayoutManager(linearLayoutManager);
        vaccine_type_name_list.setNestedScrollingEnabled(false);
        if ((getVaccineResponseModels != null)) {
            if (getVaccineResponseModels.size() > 0) {
                vaccineTypeAdapter = new VaccineTypeAdapter(PetDetailsActivity.this, getVaccineResponseModels, this);
                vaccine_type_name_list.setAdapter(vaccineTypeAdapter);
                vaccineTypeAdapter.notifyDataSetChanged();
            }
        }

        //////Set Immunization History List

        LinearLayoutManager linearLayoutManagerone = new LinearLayoutManager(PetDetailsActivity.this);
        immunization_history_list.setLayoutManager(linearLayoutManagerone);
        immunization_history_list.setNestedScrollingEnabled(false);

        if (nextVisitDateList != null) {
            if ((nextVisitDateList.size() > 0)) {
                immunizationHistoryAdopter = new ImmunizationHistoryAdopter(PetDetailsActivity.this, nextVisitDateList, vaccineClassList, vaccineList, immunizationDateList);
                immunization_history_list.setAdapter(immunizationHistoryAdopter);
                immunizationHistoryAdopter.notifyDataSetChanged();
            }
        }

        clinic_back_arrow_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaccineDetailsDialog.dismiss();
            }
        });

        age_group_CV.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                age_group_TV.setTextColor(R.color.black_color);
                periodic_vaccine_TV.setTextColor(R.color.grayColorCode);
                history_TV.setTextColor(R.color.grayColorCode);
                vaccine_type_name_list.setVisibility(View.VISIBLE);
                pereodic_list.setVisibility(View.GONE);
                immunization_history_list.setVisibility(View.GONE);
                immunization_history_layout.setVisibility(View.GONE);
            }
        });

        periodic_vaccine_CV.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                age_group_TV.setTextColor(R.color.grayColorCode);
                periodic_vaccine_TV.setTextColor(R.color.black_color);
                history_TV.setTextColor(R.color.grayColorCode);
                vaccine_type_name_list.setVisibility(View.GONE);
                pereodic_list.setVisibility(View.VISIBLE);
                immunization_history_list.setVisibility(View.GONE);
                immunization_history_layout.setVisibility(View.GONE);
            }
        });

        history_CV.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                age_group_TV.setTextColor(R.color.grayColorCode);
                periodic_vaccine_TV.setTextColor(R.color.grayColorCode);
                history_TV.setTextColor(R.color.black_color);
                vaccine_type_name_list.setVisibility(View.GONE);
                pereodic_list.setVisibility(View.GONE);
                immunization_history_list.setVisibility(View.VISIBLE);
                immunization_history_layout.setVisibility(View.VISIBLE);
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = vaccineDetailsDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        vaccineDetailsDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        vaccineDetailsDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        vaccineDetailsDialog.show();

    }

    @Override
    public void onError(Throwable t, String key) {

    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemClickImmunizationPrimary(int position) {
        Log.e("vaccinlist_primery", "");
        VaccinationParameter vaccinationParameter = new VaccinationParameter();
        vaccinationParameter.setEncryptedId(pet_encrypted_id);
        vaccinationParameter.setVaccinationScheduleId(getVaccineResponseModels.get(position).getVaccinationSchedule().getId());
        vaccinationParameter.setVaccine(getVaccineResponseModels.get(position).getVaccinationSchedule().getPrimaryVaccine());
        vaccinationParameter.setVaccineType("Primary");
        VaccinationRequest vaccinationRequest = new VaccinationRequest();
        vaccinationRequest.setData(vaccinationParameter);
        saveVaccineData(vaccinationRequest);

    }

    @Override
    public void onItemClickImmunizationBoosterOne(int position) {
        Log.e("vaccinlist_booster_one", "");
        VaccinationParameter vaccinationParameter = new VaccinationParameter();
        vaccinationParameter.setEncryptedId(pet_encrypted_id);
        vaccinationParameter.setVaccinationScheduleId(getVaccineResponseModels.get(position).getVaccinationSchedule().getId());
        vaccinationParameter.setVaccine(getVaccineResponseModels.get(position).getVaccinationSchedule().getPrimaryVaccine());
        vaccinationParameter.setVaccineType("Booster One");
        VaccinationRequest vaccinationRequest = new VaccinationRequest();
        vaccinationRequest.setData(vaccinationParameter);
        saveVaccineData(vaccinationRequest);
    }


    @Override
    public void onItemClickImmunizationBoosterTwo(int position) {
        Log.e("vaccinlist_Booster_two", "");
        VaccinationParameter vaccinationParameter = new VaccinationParameter();
        vaccinationParameter.setEncryptedId(pet_encrypted_id);
        vaccinationParameter.setVaccinationScheduleId(getVaccineResponseModels.get(position).getVaccinationSchedule().getId());
        vaccinationParameter.setVaccine(getVaccineResponseModels.get(position).getVaccinationSchedule().getPrimaryVaccine());
        vaccinationParameter.setVaccineType("Booster Two");
        VaccinationRequest vaccinationRequest = new VaccinationRequest();
        vaccinationRequest.setData(vaccinationParameter);
        saveVaccineData(vaccinationRequest);
    }
}
