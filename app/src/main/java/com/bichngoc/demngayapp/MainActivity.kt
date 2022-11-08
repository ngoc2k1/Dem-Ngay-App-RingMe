package com.bichngoc.demngayapp

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bichngoc.demngayapp.databinding.ActivityMainBinding
import com.bichngoc.demngayapp.room.UserDB
import com.bichngoc.demngayapp.room.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var millisecondsPeriod: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ibHeart.setOnClickListener {
            binding.ibSmallheart.animate().apply {
                duration = 2000
                rotationYBy(360f)
            }
        }
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.getInt("change") == 1) {
                val userChanged = bundle.getSerializable("user_change") as UserDB
                setDataForView(userChanged)
                Log.d("db_user", "onCreate: Main: user change")
                addressEditAndDelete(userChanged)
            }
        } else {
            lifecycleScope.launch {
                val userList = UserDatabase.getInstance(applicationContext).userDAO().getAllUsers()
                if (userList.isEmpty()) startActivity(
                    Intent(
                        this@MainActivity, SplashActivity::class.java
                    )
                )
                else {
                    val userDB = userList[userList.size - 1]
                    setDataForView(userDB)
                    Log.d("db_user", "onCreate: Main: user room")
                    addressEditAndDelete(userDB)
                }
            }
        }
    }

    private fun addressEditAndDelete(user: UserDB) {
        binding.tvEdit.setOnClickListener {
            transferDataToSplashActivity(user)
        }

        binding.tvDelete.setOnClickListener {
            confirmDeleteAndReturnSplashActivity()
        }

        binding.ibMenu.setOnClickListener {
            if (!binding.ibMenu.isSelected) {
                binding.ibMenu.isSelected = true
                binding.llSettings.visibility = View.VISIBLE

            } else {
                binding.ibMenu.isSelected = false
                binding.llSettings.visibility = View.GONE
            }
        }
    }

    private fun confirmDeleteAndReturnSplashActivity() {
        val dialogBuilder = AlertDialog.Builder(this@MainActivity)

        dialogBuilder.setMessage("You want to delete this relationship, right?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {//async      //tạo riêng + 3 cach
                    UserDatabase.getInstance(applicationContext).userDAO().deleteUser()
                }
                startActivity(Intent(this@MainActivity, SplashActivity::class.java))
            }.setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
        val alert = dialogBuilder.create()
        alert.setTitle("CONFIRM DELETE")
        alert.show()
    }

    private fun transferDataToSplashActivity(userDB: UserDB) {
        val intent = Intent(this@MainActivity, SplashActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("update", 1)
        bundle.putSerializable("user_update", userDB)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun setDataForView(userDB: UserDB) {
        binding.apply {
            var arrayDob = userDB.dobFemale.split("/")
            var age = 2022 - arrayDob[arrayDob.size - 1].toInt()
            tvAgeFemale.text = age.toString()
            arrayDob = userDB.dobMale.split("/")
            age = 2022 - arrayDob[arrayDob.size - 1].toInt()
            tvAgeMale.text = age.toString()

            tvNameFemale.text = userDB.nameFemale
            tvNameMale.text = userDB.nameMale
            tvStartDate.text = userDB.startDate
        }
        val formatedStartDate = SimpleDateFormat("dd/MM/yyyy").parse(userDB.startDate)
        val millisecondsStartDate = formatedStartDate.time
        val millisecondsTodayDate = Date().time
        millisecondsPeriod = millisecondsTodayDate - millisecondsStartDate
        updateMillisecondPeriodUsingCoroutine()
        //        updateMillsecondPeriodUsingTimer()
    }


    private fun updateMillisecondPeriodUsingCoroutine() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                millisecondsPeriod += 1000
                Thread.sleep(1000)
                withContext(Dispatchers.Main) {
                    convertMillionSecondsToDate()
                }
            }
        }
    }

//    private fun updateMillsecondPeriodUsingTimer() {
//        Timer().scheduleAtFixedRate(
//            object : TimerTask() {
//                override fun run() {
//                    runOnUiThread {//muốn xử lí và set lên UI user
//                        millisecondsPeriod += 1000
//                        convertMillionSecondsToDate()
//                    }
//
//                }
//            }, TimeUnit.SECONDS.toMillis(1), TimeUnit.SECONDS.toMillis(1)
//        )
//    }

    fun convertMillionSecondsToDate() {
        val time = millisecondsPeriod / 1000
        val year = time / 60 / 60 / 24 / 365
        var leftSeconds = time - year * 60 * 60 * 24 * 365
        val month = leftSeconds / 60 / 60 / 24 / 30
        leftSeconds -= month * 60 * 60 * 24 * 30
        val day = leftSeconds / 60 / 60 / 24
        leftSeconds -= day * 60 * 60 * 24
        val hour = leftSeconds / 60 / 60
        leftSeconds -= hour * 60 * 60
        val minute = leftSeconds / 60
        leftSeconds -= minute * 60

        with(binding) {
            tvYear.text = year.toString()
            tvMonth.text = month.toString()
            tvDay.text = day.toString()
            tvHour.text = hour.toString()
            tvMinute.text = minute.toString()
            tvSecond.text = leftSeconds.toString()

            tvLeftday.text = "${year * 365 + month * 30 + day}"
        }
    }
}

