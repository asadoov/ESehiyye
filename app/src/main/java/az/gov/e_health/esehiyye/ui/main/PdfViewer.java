package az.gov.e_health.esehiyye.ui.main;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import az.gov.e_health.esehiyye.Model.Database.DbSelect;
import az.gov.e_health.esehiyye.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PdfViewer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PdfViewer extends Fragment {

    Bundle bundle;
    ProgressDialog mWaitingDialog;
    DbSelect select = new DbSelect();

    public PdfViewer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PdfViewer.
     */
    // TODO: Rename and change types and number of parameters
    public static PdfViewer newInstance(String param1, String param2) {
        PdfViewer fragment = new PdfViewer();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pdf_viewer, container, false);
        final ImageButton backButton = getActivity().findViewById(R.id.backBtn);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                getFragmentManager().popBackStack();
                backButton.setVisibility(View.GONE);
            }
        });

        bundle = this.getArguments();
        long fileID = bundle.getInt("fileID");
        if (bundle != null) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    mWaitingDialog = ProgressDialog.show(getContext(), "", "Yüklənir...", true);
                }
            });
            final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("MySharedPref",
                    MODE_PRIVATE);
            final String cypher1 = sharedPreferences.getString("cypher1", null);
            final String cypher2 = sharedPreferences.getString("cypher2", null);

            WebView mWebView = view.findViewById(R.id.pdfViewer);
            mWebView.setWebChromeClient(new WebChromeClient());
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
            mWebView.getSettings().setLoadWithOverviewMode(true);
            mWebView.getSettings().setUseWideViewPort(true);
            mWebView.getSettings().setSupportZoom(true);
            // mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.getSettings().setLoadsImagesAutomatically(true);

            // mWebView.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");
            //mWebView.getSettings().setPluginsEnabled(true);
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(final WebView view, String url) {

                    if (mWaitingDialog.isShowing()) {
                        mWaitingDialog.dismiss();
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                select.refreshCypher(cypher1, cypher2, view);

                            }
                        });
                        thread.start();
                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        backButton.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(getContext(), "Xəta:" + description, Toast.LENGTH_SHORT).show();

                }
            });

//            File pdf = new File("file:///android_asset/pdf/pdfviewer/web/report.pdf");
//            try {
//                pdf.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                downloadFile("https://eservice.e-health.gov.az/iosmobileapplication/document/docview?cypher1=" + cypher1 + "&cypher2=" + cypher2 + "&id=" + fileID, pdf);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            //String url = "file:///android_asset/pdf/pdfviewer/web/viewer.html?file=report.pdf";
            String url = "https://eservice.e-health.gov.az/iosmobileapplication/document/docview?cypher1=" + cypher1 + "&cypher2=" + cypher2 + "&id=" + fileID;

            mWebView.loadDataWithBaseURL(null, "<html><head></head><body><table style=\"width:100%; height:100%;\"><tr><td style=\"vertical-align:middle;text-align:center;width:100%; height:100%;\"><img src=\"" + url + "\"></td></tr></table></body></html>", "text/html", "utf-8", null);
            //  mWebView.loadUrl(url);


        }
        return view;
    }

    private static void downloadFile(final String url, final File outputFile) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL u = new URL(url);
                    URLConnection conn = u.openConnection();
                    int contentLength = conn.getContentLength();

                    DataInputStream stream = new DataInputStream(u.openStream());

                    byte[] buffer = new byte[contentLength];
                    stream.readFully(buffer);
                    stream.close();

                    DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
                    fos.write(buffer);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    return; // swallow a 404
                } catch (IOException e) {
                    return; // swallow a 404
                } catch (Exception e) {
                    return;
                }

            }
        });
        thread.start();
        thread.join();
    }
}