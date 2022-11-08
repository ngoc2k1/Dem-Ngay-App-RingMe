package com.bichngoc.demngayapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bichngoc.demngayapp.databinding.ActivitySplashBinding
import com.bichngoc.demngayapp.room.UserDB
import com.bichngoc.demngayapp.room.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private var checked = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayPhotoVp2()
        bindDataForEdittext()

        binding.btGoToMain.setOnClickListener {
            val userChanged = getDataFromEdittext()
            storeUserInDatabase(userChanged)
            transferDataToMainActivity(userChanged)
        }
    }

    private fun bindDataForEdittext() {
        val bundle = intent.extras
        if (bundle != null) {
            checked = bundle.getInt("update")
            if (checked == 1) {
                val userDB = intent.extras?.getSerializable("user_update") as UserDB
                with(binding) {
                    edStartDate.setText(userDB.startDate)
                    edNameFemale.setText(userDB.nameFemale)
                    edDobMale.setText(userDB.dobMale)
                    edNameMale.setText(userDB.nameMale)
                    edDobFemale.setText(userDB.dobFemale)
                }
            }
        }
    }

    private fun transferDataToMainActivity(user: UserDB) {
        val intent = Intent(this, MainActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("user_change", user)
        bundle.putInt("change", 1)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun storeUserInDatabase(user: UserDB) {
        CoroutineScope(Dispatchers.IO).launch {
            if (checked == 1) {
                UserDatabase.getInstance(applicationContext).userDAO().updateUser(user)
                Log.d(
                    "db_user", "onCreate: Splash: user update"
                )
            } else {
                UserDatabase.getInstance(applicationContext).userDAO().deleteUser()
                UserDatabase.getInstance(applicationContext).userDAO().insertUser(user)
                Log.d(
                    "db_user", "onCreate: Splash: user insert"
                )
            }
        }
    }

    private fun getDataFromEdittext(): UserDB {
        val dobMale = binding.edDobMale.text.toString()
        val dobFemale = binding.edDobFemale.text.toString()
        val nameMale = binding.edNameMale.text.toString()
        val nameFemale = binding.edNameFemale.text.toString()
        val startDate = binding.edStartDate.text.toString()
        return UserDB(nameMale, nameFemale, startDate, dobMale, dobFemale)
    }

    private fun displayPhotoVp2() {
        val mListPhoto = initListPhoto()
        val photoSplashAdapter = PhotoViewPagerAdapter(mListPhoto)
        binding.vpImages.adapter = photoSplashAdapter
        binding.ciImages.setViewPager(binding.vpImages)
        binding.ciImages.animatePageSelected(1)
        binding.vpImages.currentItem = 1
    }

    private fun initListPhoto(): ArrayList<Photo> {
        val listPhoto = ArrayList<Photo>()
        listPhoto.add(Photo(R.drawable.background1))
        listPhoto.add(Photo(R.drawable.background))
        listPhoto.add(Photo(R.drawable.bg))
        return listPhoto
    }
}