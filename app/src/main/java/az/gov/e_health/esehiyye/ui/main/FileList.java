package az.gov.e_health.esehiyye.ui.main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import az.gov.e_health.esehiyye.Model.Database.DbInsert;
import az.gov.e_health.esehiyye.Model.Database.DbSelect;
import az.gov.e_health.esehiyye.Model.FileStruct;
import az.gov.e_health.esehiyye.Model.FileUploadStatusStruct;
import az.gov.e_health.esehiyye.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FileList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DbSelect select = new DbSelect();
    DbInsert insert = new DbInsert();
    ProgressDialog mWaitingDialog;
    public static final int PICK_IMAGE = 1;
    int catID;
    Bundle bundle;
    TextView notFoundLabel;
    AlertDialog alertDialog;
    List<FileStruct> sortedFiles = new ArrayList<>();
    List<FileStruct> files = new ArrayList<>();
    ListView fileList;
    LinearLayout filesLayout;
    public FileList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DocList.
     */
    // TODO: Rename and change types and number of parameters
    public static FileList newInstance(String param1, String param2) {
        FileList fragment = new FileList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_file_list, container, false);
        fileList = view.findViewById(R.id.files);
        filesLayout = view.findViewById(R.id.fileListLayout);
        bundle = this.getArguments();

        notFoundLabel = view.findViewById(R.id.notFoundLabel);
        notFoundLabel.setVisibility(View.GONE);


        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(bundle.getString("catName"));
        final ImageButton backButton = getActivity().findViewById(R.id.backBtn);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                backButton.setVisibility(View.GONE);
            }
        });
        getFileList(view);
        final SearchView searchView = view.findViewById(R.id.fileSearch);
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {

                    // Override onQueryTextSubmit method
                    // which is call
                    // when submitquery is searched

                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        return false;
                    }

                    // This method is overridden to filter
                    // the adapter according to a search query
                    // when the user is typing search
                    @Override
                    public boolean onQueryTextChange(String newText) {

                        sortedFiles.clear();
                        for (FileStruct item : files) {
                            if (item.NAME.toLowerCase().contains(newText.toLowerCase())) {
                                sortedFiles.add(item);

                            }


                        }
                        if (sortedFiles != null) {
                            if (sortedFiles.size() > 0) {
                                new Handler(Looper.getMainLooper()).post(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                filesLayout.setVisibility(View.VISIBLE);
                                                notFoundLabel.setVisibility(View.GONE);
                                                //  Log.d("UI thread", "I am the UI thread");
                                            }
                                        });
                                List<Map<String, String>> data = new ArrayList<Map<String, String>>();

                                for (FileStruct item : sortedFiles) {
                                    Map<String, String> datum = new HashMap<String, String>(2);
                                    datum.put("title", item.NAME);
                                    datum.put("date", item.DT);
                                    data.add(datum);
                                }
                                final SimpleAdapter adapter = new SimpleAdapter(getContext(), data,
                                        android.R.layout.simple_list_item_2,
                                        new String[]{"title", "date"},
                                        new int[]{android.R.id.text1,
                                                android.R.id.text2});
                                adapter.notifyDataSetChanged();
                                new Handler(Looper.getMainLooper()).post(
                                        new Runnable() {
                                            @Override
                                            public void run() {

                                                fileList.setAdapter(adapter);


                                                fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt("fileID", Integer.parseInt(files.get(position).ID));
                                                        PdfViewer fragment2 = new PdfViewer();
                                                        fragment2.setArguments(bundle);
                                                        FragmentManager fragmentManager = getFragmentManager();
                                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                        fragmentTransaction.replace(R.id.container, fragment2);
                                                        fragmentTransaction.addToBackStack(null);
                                                        fragmentTransaction.commit();

                                                    }
                                                });
                                            }
                                        });

                            } else {

                                new Handler(Looper.getMainLooper()).post(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                filesLayout.setVisibility(View.GONE);
                                                notFoundLabel.setVisibility(View.VISIBLE);
                                                //  Log.d("UI thread", "I am the UI thread");
                                            }
                                        });
                            }
                        }


                        return true;
                    }
                });

        if (bundle != null) {

            view.findViewById(R.id.addFile).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getIntent.setType("image/*");

                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");

                    Intent chooserIntent = Intent.createChooser(getIntent, "Sənədi seçin");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

//                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    Intent[] intentArray = {cameraIntent};
//                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                    startActivityForResult(chooserIntent, PICK_IMAGE);
                }
            });


            // handle your code here.
        }


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == PICK_IMAGE) {
            //TODO: action
            if (data == null) {
                //Display an error
                return;
            }
            // String name = data.getStringExtra("name");
            // Log.d("EXTRA_CHOSEN_COMPONENT",name);
//            try {
//
//                if(inputStream.exists()){
//                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                    ImageView myImageview = (ImageView) findViewById(R.id.myImageView);
//                    myImageview.setImageBitmap(myBitmap);
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }

            try {

                final Uri imageUri = data.getData();
                final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

//                final InputStream imageStream = getContext().getContentResolver().openInputStream(data.getData());
//                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                //final Bitmap selectedImage = (Bitmap) data.getExtras().get("data");

//                // Creating file
//                File photoFile = null;
//                try {
//                    photoFile = createImageFile();
//                } catch (IOException ex) {
//                    Log.d(TAG, "Error occurred while creating the file");
//                }
//
//                InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
//                FileOutputStream fileOutputStream = new FileOutputStream(photoFile);
//                // Copying
//                copyStream(inputStream, fileOutputStream);
//                fileOutputStream.close();
//                inputStream.close();


                //create a file to write bitmap data
                final File f = new File(getContext().getCacheDir(), "filename");
                f.createNewFile();

//Convert bitmap to byte array
                Bitmap bitmap = selectedImage;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
                final FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();


                final AlertDialog.Builder ImageDialogBuilder = new AlertDialog.Builder(getContext());
                ImageDialogBuilder.setCancelable(false);
                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                //ImageDialog.setTitle("Title");
                ImageView showImage = new ImageView(getContext());
                showImage.setImageBitmap(selectedImage);
                layout.addView(showImage);
                final EditText imageName = new EditText(getContext());
                imageName.setHint("Sənədin adını daxil edin");
                layout.addView(imageName);
                ImageDialogBuilder.setView(layout);


                // final File finalPhotoFile = photoFile;
                ImageDialogBuilder.setPositiveButton("Yüklə", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                mWaitingDialog.show();
                                alertDialog = new AlertDialog.Builder(getContext()).create();
                            }
                        });
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                if (imageName.getText().toString() != "") {

                                    List<FileUploadStatusStruct> status;

                                    // String encodedImage = encodeImage(selectedImage);


                                    status = insert.FileUpload(catID, imageName.getText().toString(), selectedImage, getView());


                                    if (status != null) {
                                        if (status.size() > 0) {
                                            alertDialog.setTitle("Sənəd uğurlu yerləşdirildi");

                                            //  alertDialog.setMessage("Alert message to be shown");
                                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            getFileList(getView());
                                                        }
                                                    });
                                        }

                                    }

                                    mWaitingDialog.dismiss();
                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            alertDialog.show();
                                        }
                                    });

                                }


                            }
                        }).start();

                    }
                });
                ImageDialogBuilder.setNegativeButton("Bağla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                final AlertDialog ImageDialog = ImageDialogBuilder.create();
                ImageDialog.show();
                ((AlertDialog) ImageDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                imageName.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before,
                                              int count) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub

                        if (s.toString().equals("")) {
                            ImageDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        } else {
                            ImageDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        }
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
                // Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //  mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }


    private void getFileList(final View view) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                mWaitingDialog = ProgressDialog.show(getContext(), "", "Yüklənir...", true);
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {

                catID = bundle.getInt("catID");
                files = select.GetFileList(catID, view);


                if (files != null) {
                    if (files.size() > 0) {

                        new Handler(Looper.getMainLooper()).post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        filesLayout.setVisibility(View.VISIBLE);
                                        notFoundLabel.setVisibility(View.GONE);
                                        //  Log.d("UI thread", "I am the UI thread");
                                    }
                                });

                        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

                        for (FileStruct item : files) {
                            Map<String, String> datum = new HashMap<String, String>(2);
                            datum.put("title", item.NAME);
                            datum.put("date", item.DT);
                            data.add(datum);
                        }
                        final SimpleAdapter adapter = new SimpleAdapter(getContext(), data,
                                android.R.layout.simple_list_item_2,
                                new String[]{"title", "date"},
                                new int[]{android.R.id.text1,
                                        android.R.id.text2});
                        adapter.notifyDataSetChanged();
                        new Handler(Looper.getMainLooper()).post(
                                new Runnable() {
                                    @Override
                                    public void run() {

                                        fileList.setAdapter(adapter);


                                        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Bundle bundle = new Bundle();
                                                bundle.putInt("fileID", Integer.parseInt(files.get(position).ID));
                                                PdfViewer fragment2 = new PdfViewer();
                                                fragment2.setArguments(bundle);
                                                FragmentManager fragmentManager = getFragmentManager();
                                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                fragmentTransaction.replace(R.id.container, fragment2);
                                                fragmentTransaction.addToBackStack(null);
                                                fragmentTransaction.commit();

                                            }
                                        });
                                    }
                                });

                    }
                }
                mWaitingDialog.dismiss();
            }
        }).start();


    }
}