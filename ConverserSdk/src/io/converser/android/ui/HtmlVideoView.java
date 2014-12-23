package io.converser.android.ui;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.widget.Toast;

import io.converser.android.model.Content;
import io.converser.android.model.ConversationAtom;

public class HtmlVideoView extends WebView implements ConverserContent {

    public static final String PLAYER_VIDEO_VIMEO = "vimeo";
    public static final String PLAYER_VIDEO_YOUTUBE = "youtube";
    String url;
    int height;
    String player;
    String errorHtml; // This is a link to the video to be displayed in case the video is not visible to the customers
    String videoHtml; // This is the final output. This will be the tags and information that is rendered in the application
    private Content model;

    public HtmlVideoView(Context context, Content model) {
        super(context);
        init(model);
    }

    protected void init(Content model) {
        url = model.getValue();
        height = Integer.parseInt(model.getHeight());
        if (height == 0) {
            height = 220;
        }
        String aStyle = "style= 'font-size: 0.6875em; color: #666; width:100%;'";
        String pStyle = "style='text-align:center; margin-top:8px'";

        errorHtml = ("<p " + pStyle + ">") + ("<a " + aStyle + " href='" + url.toString() + "'>") + ("Can't see the video?</a>");

        if (url == null) {
            Toast.makeText(this.getContext(), "Unknown Video Player Detected", Toast.LENGTH_SHORT).show();
            videoHtml = "<p>Sorry, a malformed URL was detected. This video cannot be played.</p> ";
        } else if (url == "" || url == " ") {
            Toast.makeText(this.getContext(), "Unknown Video Player Detected", Toast.LENGTH_SHORT).show();
            videoHtml = "<p>Sorry, The video URL does not exist. The video cannot be played.</p> ";
        } else if (url.toLowerCase().contains(PLAYER_VIDEO_VIMEO)) {
            player = PLAYER_VIDEO_VIMEO;
            videoHtml = "<iframe width='100%' height='" + height + "' src=" + url + " frameborder='0' webkitAllowFullScreen mozallowfullscreen allowFullScreen></iframe>" + errorHtml;
        } else if (url.toLowerCase().contains(PLAYER_VIDEO_YOUTUBE)) {
            player = PLAYER_VIDEO_YOUTUBE;
            videoHtml = "<iframe width='100%' height='" + height + "' src=" + url + " frameborder='0' allowFullScreen></iframe>" + errorHtml;
        } else {
            Toast.makeText(this.getContext(), "Unknown Video Player Detected", Toast.LENGTH_SHORT).show();
            videoHtml = errorHtml;
        }

        // Setup the WebView for video playback
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setRenderPriority(RenderPriority.HIGH);
        this.getSettings().setLoadsImagesAutomatically(true);
        this.getSettings().setPluginState(PluginState.ON);
        this.getSettings().setPluginState(WebSettings.PluginState.ON);

        this.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // If we ever decide to use a loaded for the webview or video loading
            }
        });
        // this.loadData(genericStyle, "text/html", "UTF-8");
        this.loadDataWithBaseURL("fake://fake", videoHtml, "text/html", "UTF-8", null);

    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        if (visibility != View.VISIBLE) {
            Log.i("HTMLVideoView", "Stopping the Video!");
            this.stopLoading();
            this.freeMemory();
            this.loadData("<p></p>", "text/html", "utf8"); // Load some blank data into the webview
        }
    }

    @Override
    public ConversationAtom getModel() {
        return model;
    }

}
