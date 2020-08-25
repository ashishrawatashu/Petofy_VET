package com.cynoteck.petofyvet.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.cynoteck.petofyvet.R;
import com.cynoteck.petofyvet.api.ApiClient;
import com.cynoteck.petofyvet.api.ApiResponse;
import com.cynoteck.petofyvet.api.ApiService;
import com.cynoteck.petofyvet.params.addPetClinicParamRequest.AddPetClinicParam;
import com.cynoteck.petofyvet.params.addPetClinicParamRequest.AddPetClinicRequest;
import com.cynoteck.petofyvet.response.addPet.imageUpload.ImageResponse;
import com.cynoteck.petofyvet.response.addPetClinicresponse.AddpetClinicResponse;
import com.cynoteck.petofyvet.response.clinicVisist.ClinicVisitResponse;
import com.cynoteck.petofyvet.response.getPetReportsResponse.GetReportsTypeResponse;
import com.cynoteck.petofyvet.utils.Config;
import com.cynoteck.petofyvet.utils.Methods;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class AddClinicActivity extends AppCompatActivity implements View.OnClickListener, ApiResponse {

    ImageView back_arrow_IV;
    String strNatureOfVist="", strDocumentUrl="",visitId="",natureOfVisit="",pet_id="",pet_name="",pet_owner_name="",pet_sex="",pet_unique_id="";
    Bundle data = new Bundle();
    TextView Dewormer_name_ET,Dewormer_name_TV,Dewormer_ET,Dewormer_TV,vaccine_TV,clinicFolow_up_dt_view,
             clinicCalenderTextViewVisitDt,clinicIlness_onset,date_of_illness_TV,upload_documents,clinic_peto_edit_reg_number_dialog;
    ImageView document_name,clinic_back_arrow_IV;
    LinearLayout addPrescriptionButton;
    EditText clinicVeterian_name_ET,clinicCescription_ET,clinicTreatment_remarks_ET,
            weight_ET,clinicTemparature_ET,clinicDiagnosis_ET,vacine_ET;
    AppCompatSpinner clinicNature_of_visit_spinner,clinicNext_visit_spinner;
    LinearLayout clinicDocument_layout;
    Button clinicCancel_clinic_add_dialog,clinicSave_clinic_data;
    Methods methods;

    ArrayList<String> nextVisitList;
    ArrayList<String> natureOfVisitList;

    HashMap<String,String> nextVisitHas=new HashMap<>();
    HashMap<String,String> natureOfVisitHashMap=new HashMap<>();

    DatePickerDialog picker;

    private static final String IMAGE_DIRECTORY = "/Picture";
    private int GALLERY = 1, CAMERA = 2;
    File file = null;
    Dialog dialog;
    Bitmap bitmap, thumbnail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clinic);

        init();
        requestMultiplePermissions();
    }

    private void init() {
        methods = new Methods(this);
        Bundle extras = getIntent().getExtras();
        clinic_peto_edit_reg_number_dialog = findViewById(R.id.clinic_peto_edit_reg_number_dialog);
        clinicVeterian_name_ET = findViewById(R.id.veterian_name_ET);
        clinicNature_of_visit_spinner = findViewById(R.id.nature_of_visit_spinner);
        clinicCalenderTextViewVisitDt = findViewById(R.id.calenderTextViewVisitDt);
        clinicCescription_ET = findViewById(R.id.description_ET);
        date_of_illness_TV = findViewById(R.id.date_of_illness_TV);
        document_name = findViewById(R.id.document_name);
        upload_documents = findViewById(R.id.upload_documents);
        weight_ET = findViewById(R.id.weight_ET);
        vaccine_TV = findViewById(R.id.vaccine_TV);
        vacine_ET = findViewById(R.id.vacine_ET);
        Dewormer_TV = findViewById(R.id.Dewormer_TV);
        Dewormer_ET = findViewById(R.id.Dewormer_ET);
        Dewormer_name_TV = findViewById(R.id.Dewormer_name_TV);
        Dewormer_name_ET = findViewById(R.id.Dewormer_name_ET);
        clinicTemparature_ET = findViewById(R.id.temparature_ET);
        clinicIlness_onset = findViewById(R.id.ilness_onset);
        clinicDiagnosis_ET = findViewById(R.id.diagnosis_ET);
        clinicTreatment_remarks_ET = findViewById(R.id.treatment_remarks_ET);
        clinicNext_visit_spinner = findViewById(R.id.next_visit_spinner);
        clinicFolow_up_dt_view = findViewById(R.id.folow_up_dt_view);
        clinicDocument_layout = findViewById(R.id.document_layout);
        clinicSave_clinic_data = findViewById(R.id.save_clinic_data);
        clinic_back_arrow_IV = findViewById(R.id.clinic_back_arrow_IV);

        clinicCalenderTextViewVisitDt.setOnClickListener(this);
        clinicIlness_onset.setOnClickListener(this);
        clinicFolow_up_dt_view.setOnClickListener(this);
        clinicSave_clinic_data.setOnClickListener(this);
        upload_documents.setOnClickListener(this);
        clinic_back_arrow_IV.setOnClickListener(this);

        if (extras != null) {
            pet_id = extras.getString("pet_id");
            pet_name = extras.getString("pet_name");
            pet_owner_name = extras.getString("pet_owner_name");
            pet_sex = extras.getString("pet_sex");
            pet_unique_id = extras.getString("pet_unique_id");
            clinic_peto_edit_reg_number_dialog.setText(pet_unique_id);
        }

        if (methods.isInternetOn()){
            getClientVisit();
            getVisitTypes();
        }else {

            methods.DialogInternet();
        }

      }

    private void getClientVisit() {
        ApiService<ClinicVisitResponse> service = new ApiService<>();
        service.get( this, ApiClient.getApiInterface().getClinicVisit(Config.token), "GetClinicVisitRoutineFollowupTypes");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.calenderTextViewVisitDt:
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                clinicCalenderTextViewVisitDt.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
                break;
            case R.id.ilness_onset:
                final Calendar cldrIlness = Calendar.getInstance();
                int dayIlnes = cldrIlness.get(Calendar.DAY_OF_MONTH);
                int monthIlnes = cldrIlness.get(Calendar.MONTH);
                int yearIlnes = cldrIlness.get(Calendar.YEAR);
                picker = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                clinicIlness_onset.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, dayIlnes, monthIlnes, yearIlnes);
                picker.show();
                break;
            case R.id.folow_up_dt_view:
                final Calendar cldrFolwUpDt = Calendar.getInstance();
                int dayFolwUpDt = cldrFolwUpDt.get(Calendar.DAY_OF_MONTH);
                int monthFolwUpDt = cldrFolwUpDt.get(Calendar.MONTH);
                int yearFolwUpDt = cldrFolwUpDt.get(Calendar.YEAR);
                picker = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                clinicFolow_up_dt_view.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, dayFolwUpDt, monthFolwUpDt, yearFolwUpDt);
                picker.show();
                break;
            case R.id.save_clinic_data:
                String veterian_name=clinicVeterian_name_ET.getText().toString();
                String descrisption=clinicCescription_ET.getText().toString();
                String Remarks=clinicTreatment_remarks_ET.getText().toString();
                String visitDate=clinicCalenderTextViewVisitDt.getText().toString();
                String dtOfOnset=clinicIlness_onset.getText().toString();
                String flowUpDt=clinicFolow_up_dt_view.getText().toString();
                String weight=weight_ET.getText().toString();
                String temparature=clinicTemparature_ET.getText().toString();
                String diagnosis=clinicDiagnosis_ET.getText().toString();
                String strVacine=vacine_ET.getText().toString();
                String strDewormerName=Dewormer_ET.getText().toString();
                String strDewormerDose=Dewormer_name_ET.getText().toString();
                if(veterian_name.isEmpty()){
                    clinicVeterian_name_ET.setError("Enter Veterinarian Name");
                    clinicCescription_ET.setError(null);
                    clinicTreatment_remarks_ET.setError(null);
                    clinicDiagnosis_ET.setError(null);
                    vacine_ET.setError(null);
                    Dewormer_name_ET.setError(null);
                    Dewormer_ET.setError(null);
                }
                else if(descrisption.isEmpty()){
                    clinicVeterian_name_ET.setError(null);
                    clinicCescription_ET.setError("Enter Description");
                    clinicTreatment_remarks_ET.setError(null);
                    clinicDiagnosis_ET.setError(null);
                    vacine_ET.setError(null);
                    Dewormer_name_ET.setError(null);
                    Dewormer_ET.setError(null);
                }
                else if(Remarks.isEmpty()){
                    clinicVeterian_name_ET.setError(null);
                    clinicCescription_ET.setError(null);
                    clinicTreatment_remarks_ET.setError("Enter Remarks");
                    clinicDiagnosis_ET.setError(null);
                    vacine_ET.setError(null);
                    Dewormer_name_ET.setError(null);
                    Dewormer_ET.setError(null);
                }
                else if(diagnosis.isEmpty()){
                    clinicVeterian_name_ET.setError(null);
                    clinicCescription_ET.setError(null);
                    clinicTreatment_remarks_ET.setError(null);
                    clinicDiagnosis_ET.setError("Enter Diagnosis");
                    vacine_ET.setError(null);
                    Dewormer_name_ET.setError(null);
                    Dewormer_ET.setError(null);
                }
                else if(natureOfVisit.isEmpty()||(natureOfVisit.equals("Select Visit"))){
                    clinicVeterian_name_ET.setError(null);
                    clinicCescription_ET.setError(null);
                    clinicTreatment_remarks_ET.setError(null);
                    clinicDiagnosis_ET.setError(null);
                    vacine_ET.setError(null);
                    Dewormer_name_ET.setError(null);
                    Dewormer_ET.setError(null);
                    Toast.makeText(this, "Select Nature of Visit", Toast.LENGTH_SHORT).show();
                }
                else if((natureOfVisit.equals("Immunization"))&&(strVacine.isEmpty()))
                    {
                        clinicVeterian_name_ET.setError(null);
                        clinicCescription_ET.setError(null);
                        clinicTreatment_remarks_ET.setError(null);
                        clinicDiagnosis_ET.setError(null);
                        vacine_ET.setError("Enter Vaccine name");
                        Dewormer_name_ET.setError(null);
                        Dewormer_ET.setError(null);
                    }
                    else if((natureOfVisit.equals("Deworming"))&&(strDewormerName.isEmpty()))
                    {
                        clinicVeterian_name_ET.setError(null);
                        clinicCescription_ET.setError(null);
                        clinicTreatment_remarks_ET.setError(null);
                        clinicDiagnosis_ET.setError(null);
                        Dewormer_name_ET.setError(null);
                        vacine_ET.setError(null);
                        Dewormer_ET.setError("Enter Dewormer Name");
                    }
                    else if((natureOfVisit.equals("Deworming"))&&(strDewormerDose.isEmpty()))
                    {
                        clinicVeterian_name_ET.setError(null);
                        clinicCescription_ET.setError(null);
                        clinicTreatment_remarks_ET.setError(null);
                        clinicDiagnosis_ET.setError(null);
                        vacine_ET.setError(null);
                        Dewormer_name_ET.setError(null);
                        Dewormer_ET.setError("Enter Dose");
                    }
                    else
                    {
                        clinicVeterian_name_ET.setError(null);
                        clinicCescription_ET.setError(null);
                        clinicTreatment_remarks_ET.setError(null);
                        clinicDiagnosis_ET.setError(null);
                        vacine_ET.setError(null);
                        Dewormer_name_ET.setError(null);
                        Dewormer_ET.setError(null);
                        AddPetClinicParam addPetClinicParam=new AddPetClinicParam();
                        addPetClinicParam.setPetId(pet_id);
                        addPetClinicParam.setVeterinarian(veterian_name);
                        addPetClinicParam.setVisitDate(visitDate);
                        addPetClinicParam.setNatureOfVisitId(strNatureOfVist);
                        addPetClinicParam.setVaccine(strVacine);
                        addPetClinicParam.setDescription(descrisption);
                        addPetClinicParam.setWeightLbs(weight);
                        addPetClinicParam.setWeightOz(weight);
                        addPetClinicParam.setTemperature(temparature);
                        addPetClinicParam.setDateOfOnset(dtOfOnset);
                        addPetClinicParam.setDewormerName(strDewormerName);
                        addPetClinicParam.setTreatmentRemarks(Remarks);
                        addPetClinicParam.setDiagnosisProcedure(diagnosis);
                        addPetClinicParam.setFollowUpId("");
                        addPetClinicParam.setFollowUpDate(flowUpDt);
                        addPetClinicParam.setDocuments(strDocumentUrl);
                        AddPetClinicRequest addPetClinicRequest=new AddPetClinicRequest();
                        addPetClinicRequest.setAddPetParams(addPetClinicParam);
                        if (methods.isInternetOn()){
                            addPetClinicData(addPetClinicRequest);
                        }else {

                            methods.DialogInternet();
                        }
                    }
                break;
            case R.id.upload_documents:
                showPictureDialog();
                break;
            case R.id.clinic_back_arrow_IV:
                onBackPressed();
                break;

        }
    }

    private void showPictureDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_layout);

        TextView select_camera = (TextView) dialog.findViewById(R.id.select_camera);
        TextView select_gallery = (TextView) dialog.findViewById(R.id.select_gallery);
        TextView cancel_dialog = (TextView) dialog.findViewById(R.id.cancel_dialog);

        select_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoFromCamera();
            }
        });

        select_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhotoFromGallary();
            }
        });

        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void choosePhotoFromGallary() {


        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);

    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dialog.dismiss();
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {

                Uri contentURI = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentURI);
                    document_name.setImageBitmap(bitmap);
                    saveImage(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(AddClinicActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        else if (requestCode == CAMERA) {

            if (data.getData() == null)
            {
                thumbnail = (Bitmap) data.getExtras().get("data");
                Log.e("jghl",""+thumbnail);
                document_name.setImageBitmap(thumbnail);
                    saveImage(thumbnail);
                    Toast.makeText(AddClinicActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
            }

            else{
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(AddClinicActivity.this.getContentResolver(), data.getData());
                    document_name.setImageBitmap(bitmap);
                    saveImage(bitmap);
                    Toast.makeText(AddClinicActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return;
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
                file = new File(wallpaperDirectory, Calendar.getInstance()
                        .getTimeInMillis() + ".png");
                file.createNewFile();
                FileOutputStream fo = new FileOutputStream(file);
                fo.write(bytes.toByteArray());
                MediaScannerConnection.scanFile(this,
                        new String[]{file.getPath()},
                        new String[]{"image/png"}, null);
                fo.close();
                Log.d("TAG", "File Saved::---&gt;" + file.getAbsolutePath());
                UploadImages(file);
                return file.getAbsolutePath();

        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void UploadImages(File absolutePath) {
        MultipartBody.Part userDpFilePart = null;
        if (absolutePath != null) {
            RequestBody userDpFile = RequestBody.create(MediaType.parse("image/*"), absolutePath);
            userDpFilePart = MultipartBody.Part.createFormData("file", absolutePath.getName(), userDpFile);
        }

        ApiService<ImageResponse> service = new ApiService<>();
        service.get( this, ApiClient.getApiInterface().uploadImages(Config.token,userDpFilePart), "UploadDocument");
        Log.e("DATALOG","check1=> "+service);

    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(AddClinicActivity.this, "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }


                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(AddClinicActivity.this, "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void getVisitTypes() {
        ApiService<GetReportsTypeResponse> service = new ApiService<>();
        service.get(this, ApiClient.getApiInterface().getReportsType(Config.token), "GetReportsType");
    }

    private void addPetClinicData(AddPetClinicRequest addPetClinicRequest) {
        ApiService<AddpetClinicResponse> service = new ApiService<>();
        service.get( this, ApiClient.getApiInterface().addClinicVisit(Config.token,addPetClinicRequest), "AddClinicVisit");
        Log.d("DIALOG==>",""+addPetClinicRequest);
    }

    @Override
    public void onResponse(Response arg0, String key) {
        switch (key) {
            case "GetClinicVisitRoutineFollowupTypes":
                try {
                    ClinicVisitResponse clinicVisitResponse = (ClinicVisitResponse) arg0.body();
                    Log.d("GetClinicVisit", clinicVisitResponse.toString());
                    int responseCode = Integer.parseInt(clinicVisitResponse.getResponse().getResponseCode());

                    if (responseCode== 109){
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                        nextVisitList=new ArrayList<>();
                        nextVisitList.add("Select Visit");
                        for(int i=0;i<clinicVisitResponse.getData().size();i++)
                        {
                            nextVisitList.add(clinicVisitResponse.getData().get(i).getFollowUpTitle());
                            nextVisitHas.put(clinicVisitResponse.getData().get(i).getFollowUpTitle(),clinicVisitResponse.getData().get(i).getId());
                        }
                        setSpinnerNextClinicVisit();
                    }
                    else if (responseCode==614){
                        Toast.makeText(this, clinicVisitResponse.getResponse().getResponseMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, "Please Try Again !", Toast.LENGTH_SHORT).show();
                    }

                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                break;
            case "UploadDocument":
                try {
                    Log.d("UploadDocument",arg0.body().toString());
                    ImageResponse imageResponse = (ImageResponse) arg0.body();
                    int responseCode = Integer.parseInt(imageResponse.getResponse().getResponseCode());
                    if (responseCode== 109){
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                            strDocumentUrl=imageResponse.getData().getDocumentUrl();
                    }else if (responseCode==614){
                        Toast.makeText(this, imageResponse.getResponse().getResponseMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "Please Try Again !", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                break;
            case "AddClinicVisit":
                try {
                    AddpetClinicResponse addpetClinicResponse = (AddpetClinicResponse) arg0.body();
                    Log.d("AddClinicVisit", addpetClinicResponse.toString());
                    int responseCode = Integer.parseInt(addpetClinicResponse.getResponse().getResponseCode());

                    if (responseCode== 109){

                    }
                    else if (responseCode==614){
                        Toast.makeText(this, addpetClinicResponse.getResponse().getResponseMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, "Please Try Again !", Toast.LENGTH_SHORT).show();
                    }

                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                break;

            case "GetReportsType":
                try {
                    Log.d("GetPetServiceTypes",arg0.body().toString());
                    GetReportsTypeResponse petServiceResponse = (GetReportsTypeResponse) arg0.body();
                    int responseCode = Integer.parseInt(petServiceResponse.getResponse().getResponseCode());
                    if (responseCode== 109){
                        natureOfVisitList=new ArrayList<>();
                        natureOfVisitList.add("Select Visit");
                        for(int i=0;i<petServiceResponse.getData().size();i++)
                        {
                            natureOfVisitList.add(petServiceResponse.getData().get(i).getNature());
                            natureOfVisitHashMap.put(petServiceResponse.getData().get(i).getNature(),petServiceResponse.getData().get(i).getId());
                        }
                        setSpinnerNatureofVisit();
                    }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onError(Throwable t, String key) {

    }

    private void setSpinnerNextClinicVisit() {
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,nextVisitList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        clinicNext_visit_spinner.setAdapter(aa);
        clinicNext_visit_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                // Showing selected spinner item
                Log.d("spnerType",""+item);
                visitId=nextVisitHas.get(item);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setSpinnerNatureofVisit() {
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,natureOfVisitList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        clinicNature_of_visit_spinner.setAdapter(aa);
        clinicNature_of_visit_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                // Showing selected spinner item
                natureOfVisit=item;
                strNatureOfVist=natureOfVisitHashMap.get(natureOfVisit);
                if(natureOfVisit.equals("Immunization"))
                {
                    date_of_illness_TV.setVisibility(View.GONE);
                    clinicIlness_onset.setVisibility(View.GONE);
                    vaccine_TV.setVisibility(View.VISIBLE);
                    vacine_ET.setVisibility(View.VISIBLE);
                }
                else if(natureOfVisit.equals("Deworming"))
                {
                    date_of_illness_TV.setVisibility(View.GONE);
                    clinicIlness_onset.setVisibility(View.GONE);
                    vaccine_TV.setVisibility(View.GONE);
                    vacine_ET.setVisibility(View.GONE);
                    Dewormer_TV.setVisibility(View.VISIBLE);
                    Dewormer_ET.setVisibility(View.VISIBLE);
                    Dewormer_name_TV.setVisibility(View.VISIBLE);
                    Dewormer_name_ET.setVisibility(View.VISIBLE);

                }
                else{
                    date_of_illness_TV.setVisibility(View.VISIBLE);
                    clinicIlness_onset.setVisibility(View.VISIBLE);
                    vaccine_TV.setVisibility(View.GONE);
                    vacine_ET.setVisibility(View.GONE);
                    Dewormer_TV.setVisibility(View.GONE);
                    Dewormer_ET.setVisibility(View.GONE);
                    Dewormer_name_TV.setVisibility(View.GONE);
                    Dewormer_name_ET.setVisibility(View.GONE);

                }
                Log.d("spnerType",""+item);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}