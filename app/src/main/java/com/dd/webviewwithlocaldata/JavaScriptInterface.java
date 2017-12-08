package com.dd.webviewwithlocaldata;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import java.lang.ref.WeakReference;
public class JavaScriptInterface {

    public static final String INVOKE_WEB_METHOD = "javascript:try{window.ApiCore.invokeWebMethod('%s','%s')}catch(e){if(console)console.log(e)}";
    private WeakReference<WebView> webViewHolder = null;
    private Handler mtHandler = new Handler(Looper.getMainLooper());

    public JavaScriptInterface(WebView view) {
        if (view != null) {
            webViewHolder = new WeakReference<WebView>(view);
        }
    }

    @TargetApi(11)
    public void release() {
        if (webViewHolder != null) {
            WebView webView = webViewHolder.get();
            if (webView != null) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                    webView.removeJavascriptInterface("AndroidJSInterfaceV2");
                }
            }
        }
    }

    /*
    * JS call Android
    * web端API将此方法包装为 invokeClientMethod
    * */
    @JavascriptInterface
    public String invoke(final String module, final String name, final String parameters, final String callback) {
        invokeJSCallback(callback,"dd");
        return "dd";
    }

    /**
     * Android call JS
     * @param cbName
     * @param jsonParam
     */
    private void invokeJSCallback(final String cbName, final String jsonParam) {
        if (webViewHolder != null) {
            final WebView webView = webViewHolder.get();
            if (webView != null) {
                mtHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String invokeStr = String.format(INVOKE_WEB_METHOD, cbName, jsonParam);
                            if (Build.VERSION.SDK_INT <= 18) {
                                webView.loadUrl(invokeStr);
                            } else {
                                try {
                                    webView.evaluateJavascript(invokeStr, null);
                                } catch (Exception e) {
                                    webView.loadUrl(invokeStr);
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                });
            }
        }
    }

    private IJSCallback genJSCallback(final String callbackName) {
        if (callbackName != null && callbackName.length() > 0) {
            return new IJSCallback() {
                @Override
                public void invokeCallback(String param) {
                    invokeJSCallback(callbackName,param);
                }
            };
        }
        return null;
    }

    public interface IJSCallback{
        public void invokeCallback(String param);
    }

}
