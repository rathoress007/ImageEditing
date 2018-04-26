package nnk.com.imageproject;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.view.ContextMenu.ContextMenuInfo;


public class MyWebView extends AppCompatActivity {

    private WebView mWebview ;
    View v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_my_web_view);

        mWebview = new WebView(this);

        mWebview.getSettings().setAllowFileAccess(true);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setBuiltInZoomControls(true);// enable javascript

        final Activity activity = this;

        mWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
        });


        mWebview.loadUrl("https://www.google.co.in/search?q=cute+couple+images&source=lnms&tbm=isch&sa=X&ved=0ahUKEwie__ClhsnTAhUDSI8KHfJSAgMQ_AUICigB&biw=1536&bih=736");
        setContentView(mWebview);
        registerForContextMenu(mWebview);

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
       //  getMenuInflater().inflate(R.menu.image,menu);
            menu.setHeaderTitle("Select The Action");
            menu.add(0, v.getId(), 0, "Select image");//groupId, itemId, order, title
            menu.add(0, v.getId(), 0, "Cancel");
        this.v = v;


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle() == "Select image")
        {
            if (v instanceof WebView) {
                WebView.HitTestResult result = ((WebView) v).getHitTestResult();

                if (result != null) {
                    int type = result.getType();

                    // Confirm type is an image
                    if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                        String imageUrl = result.getExtra();
                      //  Toast.makeText(this, imageUrl, Toast.LENGTH_LONG).show();
                        // file_download(imageUrl);
                        Intent intent = new Intent();
                        intent.putExtra("URL",imageUrl);
                        setResult(RESULT_OK,intent);
                        finish();


                    }
                }

            }
            return true;
        }

        else
        {
            
            return true;
        }


    }

    /*public void file_download(String uRl) {
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/image_files");

        if (!direct.exists()) {
            direct.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("Demo")
                .setDescription("Something useful. No, really.")
                .setDestinationInExternalPublicDir("/dhaval_files", "test.jpg");

        mgr.enqueue(request);

    }*/

}





