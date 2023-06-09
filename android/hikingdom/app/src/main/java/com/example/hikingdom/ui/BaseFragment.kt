package com.example.hikingdom.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
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

    companion object {
        const val IMAGE_SELECTOR_REQ = 1
    }

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

    // fragment 종료 시, bidnding 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Toast 메시지 띄우는 함수
    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    // fragment의 webView 레아이웃을 셋팅하는 함수
    fun webViewSetting(webView: WebView, url: String){
        var refreshToken = getRefreshToken() // sharedPreference에서 refresh token 가져오기
        var accessToken = getAccessToken() // sharedPreference에서 access token 가져오기

        // 웹뷰 설정
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true   // 웹뷰 자바스크립트 허용
        webView.settings.domStorageEnabled = true   // 웹뷰 로컬 스토리지 허용
        webView.addJavascriptInterface(WebInterface(activityContext), "Kotlin") // 웹뷰와의 인터페이스 연결

        // 웹뷰로 http 리소스 불러 오기 허용
        val webSetting: WebSettings = webView.settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        // 페이지가 로드 되었을 때 JWT 토큰을 전달해줌
        webView.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                Log.d("Token", "$refreshToken")
                webView.evaluateJavascript("saveTokens('$accessToken', '$refreshToken')", null)
            }
        })
        
        webView.loadUrl(url) // 전달받은 url로 웹뷰 연결

        // 뒤로 가기 불가능 시 종료
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