package com.swrve.sdk.conversations.ui;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.swrve.sdk.conversations.engine.model.Content;
import com.swrve.sdk.conversations.engine.model.ConversationAtom;

public class HtmlVideoView extends WebView implements ConversationContent {
    private static final String LOG_TAG = "SwrveSDK";
    public static final String PLAYER_VIDEO_VIMEO = "vimeo";
    public static final String PLAYER_VIDEO_YOUTUBE = "youtube";
    private String url;
    private int height;
    // This is a link to the video to be displayed in case the video is not visible to the customers
    private String errorHtml;
    // This is the final output. This will be the tags and information that is rendered in the application
    private String videoHtml;
    private Content model;

    public HtmlVideoView(Context context, Content model) {
        super(context);
        init(model);
    }

    protected void init(Content model) {
        this.model = model;
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
            videoHtml = "<iframe width='100%' height='" + height + "' src=" + url + " frameborder='0' webkitAllowFullScreen mozallowfullscreen allowFullScreen></iframe>" + errorHtml;
        } else if (url.toLowerCase().contains(PLAYER_VIDEO_YOUTUBE)) {
            videoHtml = "<iframe width='100%' height='" + height + "' src=" + url + " frameborder='0' allowFullScreen></iframe>" + errorHtml;
        } else {
            Toast.makeText(this.getContext(), "Unknown Video Player Detected", Toast.LENGTH_SHORT).show();
            videoHtml = errorHtml;
        }

        // Setup the WebView for video playback
        this.getSettings().setJavaScriptEnabled(true);
        this.loadDataWithBaseURL("fake://fake", videoHtml, "text/html", "UTF-8", null);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        if (visibility != View.VISIBLE) {
            Log.i(LOG_TAG, "Stopping the Video!");
            this.stopLoading();
            // Load some blank data into the webview
            this.loadData("<p></p>", "text/html", "utf8");
        }
    }

    @Override
    public ConversationAtom getModel() {
        return model;
    }
}
