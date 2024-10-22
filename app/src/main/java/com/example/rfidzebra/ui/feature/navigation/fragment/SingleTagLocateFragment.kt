//package com.example.rfidzebra.ui.feature.navigation.fragment
//
//import android.app.Activity
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.content.IntentFilter
//import android.graphics.Color
//import android.os.Build
//import android.os.Bundle
//import android.text.InputFilter
//import android.text.InputFilter.AllCaps
//import android.text.SpannableStringBuilder
//import android.text.Spanned
//import android.text.style.BackgroundColorSpan
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ArrayAdapter
//import android.widget.AutoCompleteTextView
//import androidx.fragment.app.Fragment
//import com.example.rfidzebra.R
//import com.example.rfidzebra.ui.feature.home.locate.utils.Application
//import com.example.rfidzebra.ui.feature.home.locate.utils.RFIDController
//import com.example.rfidzebra.ui.feature.home.locate.utils.ResponseHandlerInterfaces
//import com.example.rfidzebra.ui.feature.home.locate.utils.asciitohex
//import com.example.rfidzebra.ui.feature.home.locate.utils.hextoascii
//import com.example.rfidzebra.ui.feature.home.locate.view.RangeGraph
//import com.google.android.material.floatingactionbutton.FloatingActionButton
//import com.zebra.rfid.api3.RFIDResults
//
//class SingleTagLocateFragment : Fragment(), ResponseHandlerInterfaces.TriggerEventHandler {
//
//    private var locationBar: RangeGraph? = null
//
//    private var btnLocate: FloatingActionButton? = null
//    private var etLocateTag: AutoCompleteTextView? = null
//    private var adapter: ArrayAdapter<String>? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        val filter = IntentFilter()
//        filter.addAction(resources.getString(R.string.dw_action))
//        filter.addCategory(resources.getString(R.string.dw_category))
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            requireActivity().registerReceiver(scanResultBroadcast, filter, Context.RECEIVER_EXPORTED)
//        } else {
//            requireActivity().registerReceiver(scanResultBroadcast, filter)
//        }
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_single_tag_locate, container, false)
//    }
//
//    override fun onAttach(activity: Activity) {
//        super.onAttach(activity)
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//
//        locationBar = requireActivity().findViewById<View>(R.id.locationBar) as RangeGraph
//        btnLocate = requireActivity().findViewById<View>(R.id.btn_locate) as FloatingActionButton
//        etLocateTag = requireActivity().findViewById<View>(R.id.lt_et_epc) as AutoCompleteTextView
//
//        if (RFIDController.asciiMode) {
//            etLocateTag!!.filters = arrayOf<InputFilter>(filter)
//        } else {
//            etLocateTag!!.filters = arrayOf<InputFilter>(filter, AllCaps())
//        }
//
//        RFIDController.getInstance().updateTagIDs()
//
//        adapter = ArrayAdapter(
//            requireActivity(),
//            android.R.layout.simple_dropdown_item_1line,
//            Application.tagIDs!!
//        )
//        etLocateTag!!.setAdapter(adapter)
//
//
//        etLocateTag!!.setAdapter(adapter)
//        locationBar!!.setValue(0)
//
//        if (RFIDController.isLocatingTag) {
//            if (etLocateTag != null) {
//                if (RFIDController.asciiMode) etLocateTag!!.setText(
//                    hextoascii.convert(
//                        RFIDController.currentLocatingTag
//                    )
//                ) else etLocateTag!!.setText(asciitohex.convert(RFIDController.currentLocatingTag))
//            }
//            etLocateTag!!.isFocusable = false
//            if (btnLocate != null) {
//                btnLocate!!.setImageResource(R.drawable.ic_play_stop)
//            }
//            showTagLocationingDetails()
//        } else {
//            if (etLocateTag != null && Application.locateTag != null) {
//                if (RFIDController.asciiMode) etLocateTag!!.setText(
//                    hextoascii.convert(
//                        Application.locateTag
//                    )
//                ) else etLocateTag!!.setText(asciitohex.convert(Application.locateTag))
//            }
//            if (btnLocate != null) {
//                btnLocate!!.setImageResource(android.R.drawable.ic_media_play)
//            }
//        }
//
//        if (RFIDController.asciiMode) {
//            val printTag = SpannableStringBuilder(etLocateTag!!.text)
//            for (i in printTag.indices) {
//                if (printTag[i] == ' ') {
//                    val bcs = BackgroundColorSpan(Color.YELLOW)
//                    printTag.setSpan(bcs, i, i + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
//                }
//            }
//            etLocateTag!!.setText(printTag)
//        }
//
//    }
//
//    override fun onResume() {
//        super.onResume()
//        //((ActiveDeviceActivity)getActivity()).enableScanner();
//    }
//
//    override fun onPause() {
//        super.onPause()
//        //((ActiveDeviceActivity)getActivity()).disableScanner();
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        Application.locateTag = etLocateTag!!.text.toString()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        requireActivity().unregisterReceiver(scanResultBroadcast)
//    }
//
//    private fun showTagLocationingDetails() {
//        if (RFIDController.TagProximityPercent.toInt() !== -1) {
//            /* if (distance != null)
//                distance.setText(RFIDController.tagProximityPercent.Proximitypercent + "%");*/
//            if (locationBar != null && RFIDController.TagProximityPercent.toInt() !== -1) {
//                locationBar!!.setValue(RFIDController.TagProximityPercent as Short)
//                locationBar!!.invalidate()
//                locationBar!!.requestLayout()
//            }
//        }
//    }
//
//    fun handleLocateTagResponse() {
//        requireActivity().runOnUiThread { showTagLocationingDetails() }
//    }
//
//    @Synchronized
//    override fun triggerPressEventRecieved() {
//        if (!RFIDController.isLocatingTag) {
//            requireActivity().runOnUiThread {
//                val activity: ActiveDeviceActivity? = activity as ActiveDeviceActivity?
//                if (activity != null) {
//                    activity.locationingButtonClicked(btnLocate)
//                }
//            }
//        }
//    }
//
//    @Synchronized
//    override fun triggerReleaseEventRecieved() {
//        if (RFIDController.isLocatingTag) {
//            requireActivity().runOnUiThread {
//                val activity: ActiveDeviceActivity? = activity as ActiveDeviceActivity?
//                if (activity != null) {
//                    activity.locationingButtonClicked(btnLocate)
//                }
//            }
//        }
//    }
//
//    fun resetLocationingDetails(isDeviceDisconnected: Boolean) {
//        requireActivity().runOnUiThread {
//            if (btnLocate != null) {
//                btnLocate!!.setImageResource(android.R.drawable.ic_media_play)
//            }
//            if (isDeviceDisconnected && locationBar != null) {
//                locationBar!!.setValue(0)
//                locationBar!!.invalidate()
//                locationBar!!.requestLayout()
//            }
//            if (etLocateTag != null) {
//                etLocateTag!!.isFocusableInTouchMode = true
//                etLocateTag!!.isFocusable = true
//            }
//        }
//    }
//
//    fun handleStatusResponse(results: RFIDResults) {
//        requireActivity().runOnUiThread(object : Runnable {
//            override fun run() {
//                if (results != RFIDResults.RFID_API_SUCCESS) {
//                    //String command = statusData.command.trim();
//                    //if (command.equalsIgnoreCase("lt") || command.equalsIgnoreCase("locatetag"))
//                    run {
//                        RFIDController.isLocatingTag = false
//                        if (btnLocate != null) {
//                            btnLocate!!.setImageResource(android.R.drawable.ic_media_play)
//                        }
//                        if (etLocateTag != null) {
//                            etLocateTag!!.isFocusableInTouchMode = true
//                            etLocateTag!!.isFocusable = true
//                        }
//                    }
//                }
//            }
//        })
//    }
//
//    fun onUpdate() {}
//    fun onRefresh() {}
//
//    private val scanResultBroadcast: BroadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            val action = intent.action
//            if (action != null && action == resources.getString(R.string.dw_action)) {
//                displayScanResult(intent)
//            }
//        }
//    }
//
//    private fun displayScanResult(initiatingIntent: Intent) {
//        val decodedData =
//            initiatingIntent.getStringExtra(resources.getString(R.string.datawedge_intent_key_data))
//        if (decodedData != null && etLocateTag != null) {
//            etLocateTag!!.setText(decodedData)
//            etLocateTag!!.setSelection(decodedData.length)
//            if (btnLocate != null) {
//                btnLocate!!.performClick()
//            }
//        }
//    }
//
//    companion object {
//        fun newInstance(): SingleTagLocateFragment {
//            return SingleTagLocateFragment()
//        }
//    }
//}
