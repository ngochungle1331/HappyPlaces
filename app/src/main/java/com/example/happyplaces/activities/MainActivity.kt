package com.example.happyplaces.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.happyplaces.R
import com.example.happyplaces.adapters.HappyPlacesAdapter
import com.example.happyplaces.database.DatabaseHandler
import com.example.happyplaces.databinding.ActivityMainBinding
import com.example.happyplaces.models.HappyPlaceModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabAddHappyPlace.setOnClickListener {
            val intent = Intent(this@MainActivity, AddHappyPlaceActivity::class.java)
            startActivityForResult(intent, ADD_PLACE_ACTIVITY_REQUEST_CODE)
        }
        getHappyPlacesListFromLocalDatabase()
    }

    private fun getHappyPlacesListFromLocalDatabase() {
        val dbHandler = DatabaseHandler(this@MainActivity)
        val getHappyPlaceList: ArrayList<HappyPlaceModel> = dbHandler.getHappyPlacesList()

        if (getHappyPlaceList.size > 0) {
            rvHappyPlacesList.visibility = View.VISIBLE
            tvNoRecordsAvailable.visibility = View.GONE
            setupHappyPlacesRecyclerView(getHappyPlaceList)
        } else {
            rvHappyPlacesList.visibility = View.GONE
            tvNoRecordsAvailable.visibility = View.VISIBLE
        }
    }

    private fun setupHappyPlacesRecyclerView(happyPlaceList: ArrayList<HappyPlaceModel>) {
        rvHappyPlacesList.layoutManager = LinearLayoutManager(this@MainActivity)
        rvHappyPlacesList.setHasFixedSize(true)
        val placesAdapter = HappyPlacesAdapter(this@MainActivity, happyPlaceList)
        rvHappyPlacesList.adapter = placesAdapter

        placesAdapter.setOnClickListener(object : HappyPlacesAdapter.OnClickListener {
            override fun onClick(position: Int, model: HappyPlaceModel) {
                val intent = Intent(this@MainActivity, HappyPlaceDetailActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAIL, model)
                startActivity(intent)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_PLACE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                getHappyPlacesListFromLocalDatabase()
            } else {
                Log.e("Activity", "Cancelled or Back Pressed")
            }
        }
    }

    companion object {
        const val ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
        const val EXTRA_PLACE_DETAIL = "extra_place_details"
    }


}