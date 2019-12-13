package br.gov.sp.educacao.minhaescola.webViewClient;

import android.os.Build;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BoletimWebViewClient extends WebViewClient {
    private String boletimJson;

    public BoletimWebViewClient(String boletimJson) {
        super();
        this.boletimJson = boletimJson;
    }

    @Override
    public void onPageFinished(WebView webView, String url) {
        super.onPageFinished(webView, url);

        String javascript = boletimJson + ";BlobDownloader.download=function(a,b){var c=new FileReader;c.onload=function(){var d=new Int8Array(c.result);BoletimActivity.boletimPdfData(Array.from(d))};c.readAsArrayBuffer(b)};GerarBoletimUnificado(oBoletim)";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            webView.evaluateJavascript(javascript, null);
        }
        else {

            webView.setWebViewClient(null);
            webView.loadUrl("javascript:" + javascript);
        }
    }
}
