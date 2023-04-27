package com.example.hikingdom.ui.main.hiking

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.view.View
import androidx.fragment.app.viewModels
import com.example.hikingdom.BuildConfig
import com.example.hikingdom.databinding.FragmentHikingBinding
import com.example.hikingdom.ui.BaseFragment
import com.example.hikingdom.ui.main.group.GroupFragment
import java.time.LocalDateTime

class HikingFragment(): BaseFragment<FragmentHikingBinding>(FragmentHikingBinding::inflate) {
    private lateinit var locationService : LocationService
    private val hikingViewModel : HikingViewModel by viewModels()

    private var bound: Boolean = false
    lateinit var startTime: LocalDateTime
    lateinit var finishTime: LocalDateTime

    override fun initAfterBinding() {
        binding.lifecycleOwner = this
        binding.hikingFragmentViewModel = hikingViewModel

        setHikingService()
    }

    companion object {
        fun newInstance(): GroupFragment = GroupFragment()
        const val  ACTION_STOP = "${BuildConfig.APPLICATION_ID}.stop"
    }

    // Service와 HikingFragment를 Binding
    /* Defines callbacks for service binding, passed to bindService()  */
    // bound service를 통해 Activity 등 구성요소를 서비스에 바인딩하고, 요청을 보내고, 응답을 수신하며, 프로세스 간 통신(IPC)을 실행할 수 있다.
    // HikingFragment의 액티비티인 MainActivity를 서비스에 바인딩한다.
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, ibinder: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = ibinder as LocationService.LocationBinder
            locationService = binder.getService()
            bound = true

            // 사용자의 실시간 위치 정보 화면에 띄워주기
            loadLocationInfo()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            bound = false
        }
    }

    fun setHikingService(){
        val hikingStartBtn = binding.hikingStartBtn
        val hikingFinishBtn = binding.hikingFinishBtn
        hikingStartBtn.setOnClickListener {
            val startServiceIntent = Intent(activity, LocationService::class.java)
            activity?.startService(startServiceIntent)
            Intent(activity, LocationService::class.java).also{
                intent ->
                activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
            }

            showToast("등산 기록을 시작합니다.")
            hikingFinishBtn.visibility = View.VISIBLE
            hikingStartBtn.visibility = View.GONE

            startTime = LocalDateTime.now()     // 시작시간 세팅
        }

        hikingFinishBtn.setOnClickListener {
            val stopServiceIntent = Intent(activity, LocationService::class.java)
            stopServiceIntent.action = ACTION_STOP
            activity?.startService(stopServiceIntent)
            Intent(activity, LocationService::class.java).also{
                    intent ->
                activity?.unbindService(connection)
            }
            bound = false
            showToast("등산 기록을 종료합니다.")
            hikingFinishBtn.visibility = View.GONE
            hikingStartBtn.visibility = View.VISIBLE

            finishTime = LocalDateTime.now()    // 종료시간 세팅
        }
    }

    fun loadLocationInfo() {

        locationService.totalDistance.observe(this) {
            hikingViewModel.setTotalDistance(it)
        }

        locationService.duration.observe(this) {
            hikingViewModel.setDuration(it)
        }

//        locationService.lastLocation.observe(this){
//            hikingViewModel.setTotalAltitude(it.altitude)
//        }
    }

}