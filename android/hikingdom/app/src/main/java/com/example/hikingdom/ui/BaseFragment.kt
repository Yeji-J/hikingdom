package com.example.hikingdom.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.hikingdom.ApplicationClass
import com.example.hikingdom.config.WebInterface
import com.example.hikingdom.data.local.AppDatabase
import com.example.hikingdom.utils.Inflate
import com.example.hikingdom.utils.getAccessToken
import com.example.hikingdom.utils.getRefreshToken

// Base Fatgment: ActivityBinding::inflate을 전달받아 ViewBinding 생성
abstract class BaseFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!
    lateinit var activityContext: Activity // 부모 aictivity의 Context
    var db: AppDatabase? = null // 데이터베이스
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment의 ViewBinding
        _binding = inflate(inflater, container, false)
        return binding.root
    }

    // fragment에서 부모 Activity에 접근할수 있도록 설정
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityContext = context as Activity
        db = AppDatabase.getInstance(activityContext) // 데이터베이스
    }

    override fun onStart() {
        super.onStart()
        initAfterBinding()
    }

    // onStart 이 후 실행될 함수
    protected abstract fun initAfterBinding()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Toast 메시지 띄우는 함수
    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    // fragment의 webView 레아이웃을 셋팅하는 함수
    fun webViewSetting(context: Activity, webView: WebView, url: String,){
        var refreshToken = getRefreshToken()
        Log.d("refresh", "$refreshToken")

        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.addJavascriptInterface(WebInterface(context), "Kotlin")
//        webView.evaluateJavascript("sendRefreshToken('$refreshToken')", null)
        webView.loadUrl(url)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    System.exit(0)
                }
            }
        })
    }
}