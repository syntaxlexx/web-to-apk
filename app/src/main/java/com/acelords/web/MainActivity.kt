package com.acelords.web

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowInsetsController
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var showOrHideWebViewInitialUse = "show"
    private var myUrl: String = "https://acelords.com" // Change this  to your website hostname

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // webView config
        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)

        webView.webViewClient = CustomWebViewClient()
        webView.settings.javaScriptEnabled = true // Enable JavaScript if needed
        webView.settings.domStorageEnabled = true // Enable DOM storage
        webView.overScrollMode = WebView.OVER_SCROLL_NEVER // Disable over-scrolling

        // Show the loading indicator until page is fully loaded
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress == 100) {
                    progressBar.visibility = View.GONE // Hide progress bar
                }
            }
        }

        // Hide both status bar and navigation bar
        hideSystemUI()

        // Load the URL
        webView.loadUrl(myUrl)
    }

    // Custom WebViewClient to manage loading behavior
    private inner class CustomWebViewClient : WebViewClient() {
        // Handle SSL error
        @SuppressLint("WebViewClientOnReceivedSslError")
        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setMessage(R.string.notification_error_ssl_cert_invalid)

            builder.setPositiveButton("Continue") { _, _ -> handler.proceed() }
            builder.setNegativeButton("Cancel") { _, _ -> handler.cancel() }

            val dialog = builder.create()
            dialog.show()
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            if (showOrHideWebViewInitialUse == "show") {
                view.visibility = View.INVISIBLE
            }
        }

        override fun onPageFinished(view: WebView, url: String) {
            showOrHideWebViewInitialUse = "hide"
            progressBar.visibility = View.GONE
            view.visibility = View.VISIBLE
            super.onPageFinished(view, url)
        }

        // Show custom error page
        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            // Only handle main frame errors, ignoring subresource errors
            if (request.isForMainFrame) {
                myUrl = view.url.toString()
                setContentView(R.layout.error)
            }
            super.onReceivedError(view, request, error)
        }
    }

    // Handle the back button to either go back in the WebView or exit the app with a confirmation dialog
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(R.string.exit_app)
                builder.setPositiveButton("Yes") { _, _ -> finish() }
                builder.setNegativeButton("No") { _, _ -> /* Do nothing */ }

                val dialog = builder.create()
                dialog.show()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun tryAgain() {
        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)

        webView.settings.javaScriptEnabled = true // Enable JavaScript if needed
        webView.settings.domStorageEnabled = true // Enable DOM storage
        webView.overScrollMode = WebView.OVER_SCROLL_NEVER // Disable over-scrolling

        webView.loadUrl(myUrl)
    }

    private fun hideSystemUI() {
        // Get the WindowInsetsController
        val insetsController = window.insetsController

        // Hide status bar and navigation bar
        insetsController?.hide(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE)
    }

    override fun onResume() {
        super.onResume()
        // Ensure the system UI stays hidden after resume
        hideSystemUI()
    }

    override fun onPause() {
        super.onPause()
        // Optionally restore the system UI visibility when paused (if needed)
        window.insetsController?.show(WindowInsetsController.BEHAVIOR_DEFAULT)
    }

}