package com.example.handlerthreaddemo

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.text.Html
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private  val mTvServiceInfo:TextView by lazy {
         findViewById(R.id.tv)
     }
    private val mCheckMsgThread :HandlerThread by lazy {
        HandlerThread("checkMsg")
    }

    private val mCheckMsgHandler :Handler by lazy {
        Handler(mCheckMsgThread.looper){
            checkForUpdate()
            mCheckMsgHandler.sendEmptyMessageDelayed(MSG_UPDATE_INFO,1000)
        }
    }
    private val MSG_UPDATE_INFO = 0x110

    /**
     * Ui Handler
     */
    private val uiHandler:Handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initBackThread()
    }

    override fun onResume() {
        super.onResume()
        mCheckMsgHandler.sendEmptyMessageDelayed(MSG_UPDATE_INFO,1000)
    }

    override fun onPause() {
        super.onPause()
        mCheckMsgHandler.removeMessages(MSG_UPDATE_INFO)
    }

    private  fun initBackThread(){
        mCheckMsgThread.start()

    }

    private  fun checkForUpdate(){
        Thread.sleep(200)
        uiHandler.post {
            var result = "实时更新中，当前大盘指数：<font color = 'red'>%d</font>";
            result = String.format(result, (Math.random() * 3000 + 1000).toInt())
            mTvServiceInfo.text = Html.fromHtml(result)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mCheckMsgThread.quit()
    }
}