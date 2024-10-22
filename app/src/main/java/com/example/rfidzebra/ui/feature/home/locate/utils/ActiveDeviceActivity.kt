//package com.example.rfidzebra.ui.feature.home.locate.utils
//
//import android.Manifest
//import android.R
//import android.app.Activity
//import android.app.AlertDialog
//import android.app.Dialog
//import android.app.NotificationManager
//import android.app.ProgressDialog
//import android.content.Context
//import android.content.DialogInterface
//import android.content.Intent
//import android.content.SharedPreferences
//import android.content.pm.ActivityInfo
//import android.content.pm.PackageManager
//import android.content.res.Configuration
//import android.content.res.Resources
//import android.graphics.Color
//import android.net.Uri
//import android.nfc.NdefMessage
//import android.nfc.NdefRecord
//import android.nfc.NfcAdapter
//import android.nfc.NfcAdapter.CreateNdefMessageCallback
//import android.os.AsyncTask
//import android.os.Build
//import android.os.Bundle
//import android.os.Environment
//import android.os.Handler
//import android.provider.DocumentsContract
//import android.provider.Settings
//import android.util.Log
//import android.util.Xml
//import android.view.LayoutInflater
//import android.view.MenuItem
//import android.view.View
//import android.view.ViewGroup
//import android.view.Window
//import android.view.WindowManager
//import android.widget.BaseExpandableListAdapter
//import android.widget.Button
//import android.widget.ExpandableListView
//import android.widget.ImageView
//import android.widget.TextView
//import android.widget.Toast
//import androidx.activity.result.ActivityResult
//import androidx.activity.result.ActivityResultCallback
//import androidx.activity.result.ActivityResultLauncher
//import androidx.appcompat.app.ActionBar
//import androidx.appcompat.app.ActionBarDrawerToggle
//import androidx.appcompat.widget.Toolbar
//import androidx.core.view.GravityCompat
//import androidx.drawerlayout.widget.DrawerLayout
//import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentTransaction
//import androidx.viewpager.widget.ViewPager
//import androidx.viewpager.widget.ViewPager.OnPageChangeListener
//import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
//import com.google.android.material.floatingactionbutton.FloatingActionButton
//import com.google.android.material.navigation.NavigationView
//import com.google.android.material.tabs.TabLayout
//import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
//import com.google.android.material.tabs.TabLayoutMediator
//import com.zebra.demo.application.Application
//import com.zebra.demo.application.Application.DEVICE_PREMIUM_PLUS_MODE
//import com.zebra.demo.application.Application.DEVICE_STD_MODE
//import com.zebra.demo.application.Application.RFD_DEVICE_MODE
//import com.zebra.demo.application.Application.TAG_LIST_LOADED
//import com.zebra.demo.application.Application.TAG_LIST_MATCH_MODE
//import com.zebra.demo.application.Application.UNIQUE_TAGS_CSV
//import com.zebra.demo.application.Application.inventoryList
//import com.zebra.demo.application.Application.matchingTags
//import com.zebra.demo.application.Application.missedTags
//import com.zebra.demo.application.Application.tagListMap
//import com.zebra.demo.application.Application.tagsListCSV
//import com.zebra.demo.application.Application.tagsReadInventory
//import com.zebra.demo.discover_connect.nfc.PairOperationsFragment
//import com.zebra.demo.rfidreader.access_operations.AccessOperationsFragment
//import com.zebra.demo.rfidreader.common.MatchModeFileLoader
//import com.zebra.demo.rfidreader.home.RFIDBaseActivity
//import com.zebra.demo.rfidreader.inventory.InventoryListItem
//import com.zebra.demo.rfidreader.inventory.RFIDInventoryFragment
//import com.zebra.demo.rfidreader.locate_tag.LocateOperationsFragment
//import com.zebra.demo.rfidreader.manager.FactoryResetFragment
//import com.zebra.demo.rfidreader.manager.ManagerFragment
//import com.zebra.demo.rfidreader.manager.ScanHomeSettingsFragment
//import com.zebra.demo.rfidreader.rapidread.RapidReadFragment
//import com.zebra.demo.rfidreader.reader_connection.RFIDReadersListFragment
//import com.zebra.demo.rfidreader.rfid.RFIDController.mConnectedReader
//import com.zebra.demo.rfidreader.rfid.RFIDController.mIsInventoryRunning
//import com.zebra.demo.rfidreader.rfid.RFIDController.mReaderDisappeared
//import com.zebra.demo.rfidreader.settings.AdvancedOptionItemFragment
//import com.zebra.demo.rfidreader.settings.AdvancedOptionsContent
//import com.zebra.demo.rfidreader.settings.AntennaSettingsFragment
//import com.zebra.demo.rfidreader.settings.ApplicationSettingsFragment
//import com.zebra.demo.rfidreader.settings.BackPressedFragment
//import com.zebra.demo.rfidreader.settings.BatteryFragment
//import com.zebra.demo.rfidreader.settings.BatteryStatsFragment
//import com.zebra.demo.rfidreader.settings.BeeperFragment
//import com.zebra.demo.rfidreader.settings.ChargeTerminalFragment
//import com.zebra.demo.rfidreader.settings.DPOSettingsFragment
//import com.zebra.demo.rfidreader.settings.ISettingsUtil
//import com.zebra.demo.rfidreader.settings.KeyRemapFragment
//import com.zebra.demo.rfidreader.settings.LedFragment
//import com.zebra.demo.rfidreader.settings.PreFilterFragment
//import com.zebra.demo.rfidreader.settings.ProfileFragment
//import com.zebra.demo.rfidreader.settings.ProfileFragmentRE40
//import com.zebra.demo.rfidreader.settings.RegulatorySettingsFragment
//import com.zebra.demo.rfidreader.settings.SaveConfigurationsFragment
//import com.zebra.demo.rfidreader.settings.SettingListFragment
//import com.zebra.demo.rfidreader.settings.SettingsDetailActivity
//import com.zebra.demo.rfidreader.settings.SingulationControlFragment
//import com.zebra.demo.rfidreader.settings.StartStopTriggersFragment
//import com.zebra.demo.rfidreader.settings.TagReportingFragment
//import com.zebra.demo.rfidreader.settings.UsbMiFiFragment
//import com.zebra.demo.rfidreader.settings.WifiFragment
//import com.zebra.demo.scanner.activities.AssertFragment
//import com.zebra.demo.scanner.activities.BaseActivity
//import com.zebra.demo.scanner.activities.BatteryStatistics
//import com.zebra.demo.scanner.activities.BeeperActionsFragment
//import com.zebra.demo.scanner.activities.ImageActivity
//import com.zebra.demo.scanner.activities.IntelligentImageCaptureActivity
//import com.zebra.demo.scanner.activities.LEDActivity
//import com.zebra.demo.scanner.activities.NavigationHelpActivity
//import com.zebra.demo.scanner.activities.SampleBarcodes
//import com.zebra.demo.scanner.activities.ScaleActivity
//import com.zebra.demo.scanner.activities.ScanSpeedAnalyticsActivity
//import com.zebra.demo.scanner.activities.SsaSetSymbologyActivity
//import com.zebra.demo.scanner.activities.SymbologiesFragment
//import com.zebra.demo.scanner.activities.UpdateFirmware
//import com.zebra.demo.scanner.activities.VibrationFeedback
//import com.zebra.demo.scanner.fragments.AdvancedFragment
//import com.zebra.demo.scanner.fragments.BarcodeFargment
//import com.zebra.demo.scanner.fragments.ReaderDetailsFragment
//import com.zebra.demo.scanner.fragments.SettingsFragment
//import com.zebra.demo.scanner.fragments.Static_ipconfig
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.ANTENNA_SETTINGS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.APPLICATION_SETTINGS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.ASSERT_DEVICE_INFO_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.BARCODE_SYMBOLOGIES_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.BARCODE_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.BATTERY_STATISTICS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.BEEPER_ACTION_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.CHARGE_TERMINAL_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.DEVICE_PAIR_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.DEVICE_RESET_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.DPO_SETTING_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.FACTORY_RESET_FRAGMENT_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.INVENTORY_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.KEYREMAP_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.LOCATE_TAG_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.LOGGER_FRAGMENT_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.MAIN_GENERAL_SETTINGS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.MAIN_HOME_SETTINGS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.MAIN_RFID_SETTINGS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.NONOPER_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.RAPID_READ_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.READERS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.READER_DETAILS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.READER_LIST_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.READER_WIFI_SETTINGS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.RFID_ACCESS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.RFID_ADVANCED_OPTIONS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.RFID_BEEPER_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.RFID_LED_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.RFID_PREFILTERS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.RFID_PROFILES_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.RFID_REGULATORY_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.RFID_SETTINGS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.RFID_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.SAVE_CONFIG_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.SCAN_ADVANCED_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.SCAN_DATAVIEW_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.SCAN_HOME_SETTINGS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.SCAN_SETTINGS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.SCAN_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.SETTINGS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.SINGULATION_CONTROL_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.START_STOP_TRIGGER_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.STATIC_IP_CONFIG
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.TAG_REPORTING_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.UPDATE_FIRMWARE_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.USB_MIFI_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.WIFI_MAIN_PAGE
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.WIFI_STATUS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDevicePremiumAdapter
//import com.zebra.demo.scanner.helpers.ActiveDeviceStandardAdapter
//import com.zebra.demo.scanner.helpers.Constants
//import com.zebra.demo.scanner.helpers.Constants.DEBUG_TYPE.TYPE_DEBUG
//import com.zebra.demo.scanner.helpers.CustomProgressDialog
//import com.zebra.demo.scanner.helpers.DotsProgressBar
//import com.zebra.demo.scanner.helpers.ScannerAppEngine
//import com.zebra.demo.scanner.receivers.NotificationsReceiver
//import com.zebra.demo.wifi.ReaderWifiSettingsFragment
//import com.zebra.demo.wifi.WiFiMainPage
//import com.zebra.rfid.api3.ENUM_TRIGGER_MODE
//import com.zebra.rfid.api3.InvalidUsageException
//import com.zebra.rfid.api3.OperationFailureException
//import com.zebra.rfid.api3.RFIDResults
//import com.zebra.rfid.api3.ReaderDevice
//import com.zebra.scannercontrol.DCSSDKDefs.DCSSDK_COMMAND_OPCODE
//import com.zebra.scannercontrol.FirmwareUpdateEvent
//import com.zebra.scannercontrol.RMDAttributes
//import org.xmlpull.v1.XmlPullParser
//import java.io.StringReader
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//
//package com.zebra.demo
//import com.zebra.demo.application.Application
//import com.zebra.demo.discover_connect.nfc.PairOperationsFragment
//import com.zebra.demo.rfidreader.access_operations.AccessOperationsFragment
//import com.zebra.demo.rfidreader.common.MatchModeFileLoader
//import com.zebra.demo.rfidreader.home.RFIDBaseActivity
//import com.zebra.demo.rfidreader.inventory.InventoryListItem
//import com.zebra.demo.rfidreader.inventory.RFIDInventoryFragment
//import com.zebra.demo.rfidreader.locate_tag.LocateOperationsFragment
//import com.zebra.demo.rfidreader.manager.FactoryResetFragment
//import com.zebra.demo.rfidreader.manager.ManagerFragment
//import com.zebra.demo.rfidreader.manager.ScanHomeSettingsFragment
//import com.zebra.demo.rfidreader.rapidread.RapidReadFragment
//import com.zebra.demo.rfidreader.reader_connection.RFIDReadersListFragment
//import com.zebra.demo.rfidreader.settings.AdvancedOptionItemFragment
//import com.zebra.demo.rfidreader.settings.AdvancedOptionsContent
//import com.zebra.demo.rfidreader.settings.AntennaSettingsFragment
//import com.zebra.demo.rfidreader.settings.ApplicationSettingsFragment
//import com.zebra.demo.rfidreader.settings.BackPressedFragment
//import com.zebra.demo.rfidreader.settings.BatteryFragment
//import com.zebra.demo.rfidreader.settings.BatteryStatsFragment
//import com.zebra.demo.rfidreader.settings.BeeperFragment
//import com.zebra.demo.rfidreader.settings.ChargeTerminalFragment
//import com.zebra.demo.rfidreader.settings.DPOSettingsFragment
//import com.zebra.demo.rfidreader.settings.ISettingsUtil
//import com.zebra.demo.rfidreader.settings.KeyRemapFragment
//import com.zebra.demo.rfidreader.settings.LedFragment
//import com.zebra.demo.rfidreader.settings.PreFilterFragment
//import com.zebra.demo.rfidreader.settings.ProfileFragment
//import com.zebra.demo.rfidreader.settings.ProfileFragmentRE40
//import com.zebra.demo.rfidreader.settings.RegulatorySettingsFragment
//import com.zebra.demo.rfidreader.settings.SaveConfigurationsFragment
//import com.zebra.demo.rfidreader.settings.SettingListFragment
//import com.zebra.demo.rfidreader.settings.SettingsDetailActivity
//import com.zebra.demo.rfidreader.settings.SingulationControlFragment
//import com.zebra.demo.rfidreader.settings.StartStopTriggersFragment
//import com.zebra.demo.rfidreader.settings.TagReportingFragment
//import com.zebra.demo.rfidreader.settings.UsbMiFiFragment
//import com.zebra.demo.rfidreader.settings.WifiFragment
//import com.zebra.demo.scanner.activities.AssertFragment
//import com.zebra.demo.scanner.activities.BaseActivity
//import com.zebra.demo.scanner.activities.BatteryStatistics
//import com.zebra.demo.scanner.activities.BeeperActionsFragment
//import com.zebra.demo.scanner.activities.ImageActivity
//import com.zebra.demo.scanner.activities.IntelligentImageCaptureActivity
//import com.zebra.demo.scanner.activities.LEDActivity
//import com.zebra.demo.scanner.activities.NavigationHelpActivity
//import com.zebra.demo.scanner.activities.SampleBarcodes
//import com.zebra.demo.scanner.activities.ScaleActivity
//import com.zebra.demo.scanner.activities.ScanSpeedAnalyticsActivity
//import com.zebra.demo.scanner.activities.SsaSetSymbologyActivity
//import com.zebra.demo.scanner.activities.SymbologiesFragment
//import com.zebra.demo.scanner.activities.UpdateFirmware
//import com.zebra.demo.scanner.activities.VibrationFeedback
//import com.zebra.demo.scanner.fragments.AdvancedFragment
//import com.zebra.demo.scanner.fragments.BarcodeFargment
//import com.zebra.demo.scanner.fragments.ReaderDetailsFragment
//import com.zebra.demo.scanner.fragments.SettingsFragment
//import com.zebra.demo.scanner.fragments.Static_ipconfig
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter
//import com.zebra.demo.scanner.helpers.ActiveDevicePremiumAdapter
//import com.zebra.demo.scanner.helpers.ActiveDeviceStandardAdapter
//import com.zebra.demo.scanner.helpers.Constants
//import com.zebra.demo.scanner.helpers.CustomProgressDialog
//import com.zebra.demo.scanner.helpers.DotsProgressBar
//import com.zebra.demo.scanner.helpers.ScannerAppEngine
//import com.zebra.demo.scanner.receivers.NotificationsReceiver
//import com.zebra.demo.wifi.ReaderWifiSettingsFragment
//import com.zebra.demo.wifi.WiFiMainPage
//import com.zebra.demo.application.Application.DEVICE_PREMIUM_PLUS_MODE
//import com.zebra.demo.application.Application.DEVICE_STD_MODE
//import com.zebra.demo.application.Application.RFD_DEVICE_MODE
//import com.zebra.demo.application.Application.TAG_LIST_LOADED
//import com.zebra.demo.application.Application.TAG_LIST_MATCH_MODE
//import com.zebra.demo.application.Application.UNIQUE_TAGS_CSV
//import com.zebra.demo.application.Application.inventoryList
//import com.zebra.demo.application.Application.matchingTags
//import com.zebra.demo.application.Application.missedTags
//import com.zebra.demo.application.Application.tagListMap
//import com.zebra.demo.application.Application.tagsListCSV
//import com.zebra.demo.application.Application.tagsReadInventory
//import com.zebra.demo.rfidreader.rfid.RFIDController.mConnectedReader
//import com.zebra.demo.rfidreader.rfid.RFIDController.mIsInventoryRunning
//import com.zebra.demo.rfidreader.rfid.RFIDController.mReaderDisappeared
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.ANTENNA_SETTINGS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.APPLICATION_SETTINGS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.ASSERT_DEVICE_INFO_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.BARCODE_SYMBOLOGIES_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.BARCODE_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.BATTERY_STATISTICS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.BEEPER_ACTION_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.CHARGE_TERMINAL_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.DEVICE_PAIR_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.DEVICE_RESET_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.DPO_SETTING_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.FACTORY_RESET_FRAGMENT_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.INVENTORY_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.KEYREMAP_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.LOCATE_TAG_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.LOGGER_FRAGMENT_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.MAIN_GENERAL_SETTINGS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.MAIN_HOME_SETTINGS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.MAIN_RFID_SETTINGS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.NONOPER_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.RAPID_READ_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.READERS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.READER_DETAILS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.READER_LIST_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.READER_WIFI_SETTINGS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.RFID_ACCESS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.RFID_ADVANCED_OPTIONS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.RFID_BEEPER_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.RFID_LED_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.RFID_PREFILTERS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.RFID_PROFILES_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.RFID_REGULATORY_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.RFID_SETTINGS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.RFID_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.WIFI_STATUS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.SAVE_CONFIG_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.SCAN_ADVANCED_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.SCAN_DATAVIEW_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.SCAN_HOME_SETTINGS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.SCAN_SETTINGS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.SCAN_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.SETTINGS_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.SINGULATION_CONTROL_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.START_STOP_TRIGGER_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.STATIC_IP_CONFIG
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.TAG_REPORTING_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.UPDATE_FIRMWARE_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.USB_MIFI_TAB
//import com.zebra.demo.scanner.helpers.ActiveDeviceAdapter.WIFI_MAIN_PAGE
//import com.zebra.demo.scanner.helpers.Constants.DEBUG_TYPE.TYPE_DEBUG
//
//class ActiveDeviceActivity : BaseActivity(),
//    AdvancedOptionItemFragment.OnAdvancedListFragmentInteractionListener,
//    ActionBar.TabListener, ScannerAppEngine.IScannerAppEngineDevEventsDelegate,
//    ScannerAppEngine.IScannerAppEngineDevConnectionsDelegate, ISettingsUtil,
//    NavigationView.OnNavigationItemSelectedListener,
//    RFIDBaseActivity.CreateFileInterface {
//    private var viewPager: ViewPager? = null
//    var mAdapter: ActiveDeviceAdapter? = null
//    var tabLayout: TabLayout? = null
//    private val expandableListView: ExpandableListView? = null
//    private val listDataChild: HashMap<String, List<String>>? = null
//    private val listDataHeader: List<String>? = null
//    var inventoryBT: ExtendedFloatingActionButton? = null
//    private val mTabLayoutMediator: TabLayoutMediator? = null
//    private var mActiveDeviceActivity: ActiveDeviceActivity? = null
//    private var onSaveInstanceState = false
//    private var dialogFwRebooting: Dialog? = null
//    private var dotProgressBar: DotsProgressBar? = null
//    protected var progressDialog: ProgressDialog? = null
//    var nfcData: String? = null
//    var scannerID = 0
//        private set
//    private var scannerType = 0
//    var barcodeCount: TextView? = null
//    var iBarcodeCount = 0
//    var btnFindScanner: Button? = null
//    var ssaSupportedAttribs: MutableList<Int>? = null
//    var drawer: DrawerLayout? = null
//    var iv_batteryLevel: ImageView? = null
//    var iv_headerImageView: ImageView? = null
//    var battery_percentage: TextView? = null
//    var btn_disconnect: Button? = null
//    protected fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        if (mConnectedReader != null) {
//            initializeView()
//        } else {
//            val intent: Intent = Intent(
//                this,
//                DeviceDiscoverActivity::class.java
//            )
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            finishAffinity()
//            startActivity(intent)
//            finish()
//        }
//        val allAntennas =
//            ShortArray(RFIDController.mConnectedReader.ReaderCapabilities.getNumAntennaSupported())
//        for (i in 1..RFIDController.mConnectedReader.ReaderCapabilities.getNumAntennaSupported()) {
//            allAntennas[i - 1] = i.toShort()
//        }
//        RFIDController.getInstance().setSelectedAntennas(allAntennas)
//    }
//
//    private fun initializeView() {
//        val configuration = resources.configuration
//        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            if (configuration.smallestScreenWidthDp < Application.minScreenWidth) {
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
//            }
//        } else {
//            if (configuration.screenWidthDp < Application.minScreenWidth) {
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
//            }
//        }
//        setContentView(R.layout.activity_active_scanner)
//        ssaSupportedAttribs = ArrayList()
//        mActiveDeviceActivity = this
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//        val toolbar = findViewById(R.id.toolbar) as Toolbar
//        setSupportActionBar(toolbar)
//        getSupportActionBar().setTitle(resources.getString(R.string.title_empty_readers))
//        drawer = findViewById(R.id.drawer_layout) as DrawerLayout?
//        val toggle = ActionBarDrawerToggle(
//            mActiveDeviceActivity,
//            drawer,
//            toolbar,
//            R.string.navigation_drawer_open,
//            R.string.navigation_drawer_close
//        )
//        iv_batteryLevel = findViewById(R.id.batterylevel) as ImageView?
//        battery_percentage = findViewById(R.id.battery_percentage) as TextView?
//        btn_disconnect = findViewById(R.id.disconnect_btn)
//        val navigationView = findViewById(R.id.nav_view) as NavigationView
//        navigationView.setNavigationItemSelectedListener(mActiveDeviceActivity)
//        val headerImageView = navigationView.getHeaderView(0)
//        iv_headerImageView = headerImageView.findViewById<ImageView>(R.id.imageView)
//        iv_headerImageView.setOnClickListener(View.OnClickListener {
//            if (drawer!!.isDrawerOpen(GravityCompat.START)) {
//                drawer!!.closeDrawer(GravityCompat.START)
//            }
//        })
//        drawer!!.addDrawerListener(object : DrawerListener {
//            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
//            override fun onDrawerOpened(drawerView: View) {
//                if (viewPager!!.currentItem != mAdapter.getCurrentActivePosition()) {
//                    viewPager!!.currentItem = mAdapter.getCurrentActivePosition()
//                }
//                if (RFIDController.BatteryData != null) {
//                    deviceStatusReceived(
//                        RFIDController.BatteryData.getLevel(),
//                        RFIDController.BatteryData.getCharging(),
//                        RFIDController.BatteryData.getCause()
//                    )
//                }
//                if (mConnectedReader != null && mConnectedReader.isConnected()) {
//                    btn_disconnect!!.isEnabled = true
//                    if (mConnectedReader.getHostName().contains("MC33")) {
//                        btn_disconnect!!.text = "DISCONNECT"
//                    } else {
//                        btn_disconnect!!.text = "DISCONNECT " + mConnectedReader.getHostName()
//                    }
//                } else {
//                    btn_disconnect!!.isEnabled = false
//                    btn_disconnect.setText(R.string.disconnectrfid)
//                }
//            }
//
//            override fun onDrawerClosed(drawerView: View) {
//                battery_percentage!!.text = 0.toString() + "%"
//                iv_batteryLevel!!.setImageLevel(0)
//            }
//
//            override fun onDrawerStateChanged(newState: Int) {}
//        })
//        toggle.syncState()
//        btn_disconnect!!.setOnClickListener {
//            if (mConnectedReader != null && mConnectedReader.isConnected()) {
//                viewPager!!.currentItem = READERS_TAB
//                var fragment = getCurrentFragment(READERS_TAB)
//                if (fragment is PairOperationsFragment) {
//                    loadNextFragment(READER_LIST_TAB)
//                    fragment = getCurrentFragment(READERS_TAB)
//                }
//                if (fragment is ReaderDetailsFragment) {
//                    loadNextFragment(READER_LIST_TAB)
//                    fragment = getCurrentFragment(READERS_TAB)
//                }
//                if (fragment is ReaderWifiSettingsFragment) {
//                    loadNextFragment(READER_LIST_TAB)
//                    fragment = getCurrentFragment(READERS_TAB)
//                }
//                if (fragment is RFIDReadersListFragment) {
//                    (fragment as RFIDReadersListFragment?).disconnectConnectedReader()
//                }
//                val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
//                if (drawer.isDrawerOpen(GravityCompat.START)) {
//                    drawer.closeDrawer(GravityCompat.START)
//                }
//            }
//        }
//        addDevConnectionsDelegate(mActiveDeviceActivity)
//        scannerID = getIntent().getIntExtra(Constants.SCANNER_ID, -1)
//        BaseActivity.lastConnectedScannerID = scannerID
//        val scannerName: String = getIntent().getStringExtra(Constants.SCANNER_NAME)
//        val address: String = getIntent().getStringExtra(Constants.SCANNER_ADDRESS)
//        scannerType = getIntent().getIntExtra(Constants.SCANNER_TYPE, -1)
//        picklistMode = getIntent().getIntExtra(Constants.PICKLIST_MODE, 0)
//        isPagerMotorAvailable = getIntent().getBooleanExtra(Constants.PAGER_MOTOR_STATUS, false)
//        Application.currentScannerId = scannerID
//        Application.currentScannerName = scannerName
//        Application.currentScannerAddress = address
//        mRFIDBaseActivity = RFIDBaseActivity.getInstance()
//        mRFIDBaseActivity.onCreate(mActiveDeviceActivity)
//        viewPager = findViewById(R.id.activeScannerPager) as ViewPager?
//        if (mConnectedReader != null) {
//            RFD_DEVICE_MODE = mRFIDBaseActivity.getDeviceMode(
//                mConnectedReader.getHostName(),
//                Application.currentScannerId
//            )
//            if (RFD_DEVICE_MODE === DEVICE_STD_MODE) mAdapter = ActiveDeviceStandardAdapter(
//                this,
//                getSupportFragmentManager(),
//                DEVICE_STD_MODE
//            ) else mAdapter = ActiveDevicePremiumAdapter(
//                this,
//                getSupportFragmentManager(),
//                DEVICE_PREMIUM_PLUS_MODE
//            )
//            mAdapter.setDeviceModelName(mConnectedReader.getHostName())
//            viewPager!!.adapter = mAdapter
//            viewPager!!.addOnAdapterChangeListener { viewPager, oldAdapter, newAdapter -> viewPager.adapter!!.notifyDataSetChanged() }
//        }
//        tabLayout = findViewById(R.id.tablayout) as TabLayout?
//        tabLayout!!.setupWithViewPager(viewPager)
//        tabLayout!!.addOnTabSelectedListener(object : OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                mAdapter.setCurrentActivePosition(tab.position)
//                Log.d(TAG, tab.toString())
//                when (tab.position) {
//                    RFID_TAB -> if (mAdapter.getRFIDMOde() === RFID_ACCESS_TAB) {
//                        // mAdapter.notifyDataSetChanged();
//                    } else if (mAdapter.getRFIDMOde() === INVENTORY_TAB) {
//                        // mAdapter.notifyDataSetChanged();
//                        val fragment = getCurrentFragment(RFID_TAB)
//                        if (fragment != null && fragment is RFIDInventoryFragment) {
//                            //  ((RFIDInventoryFragment)fragment).onRFIDFragmentSelected();
//                        }
//                    } else if (mAdapter.getRFIDMOde() === RAPID_READ_TAB) {
//                        val fragment = getCurrentFragment(RFID_TAB)
//                        if (fragment != null && fragment is RapidReadFragment) {
//                            //       ((RapidReadFragment)fragment).onRapidReadSelected();
//                        }
//                    }
//
//                    SETTINGS_TAB -> {}
//                    SCAN_TAB -> {}
//                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {
//                Log.d(TAG, tab.toString())
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab) {
//                Log.d(TAG, tab.toString())
//            }
//        })
//        mAdapter.notifyDataSetChanged()
//        iBarcodeCount = 0
//        viewPager!!.addOnPageChangeListener(object : OnPageChangeListener {
//            override fun onPageSelected(position: Int) {
//                Constants.logAsMessage(
//                    TYPE_DEBUG, javaClass.simpleName,
//                    " Position is --- $position"
//                )
//                mAdapter.setCurrentActivePosition(position)
//                when (position) {
//                    RFID_TAB -> {
//                        if (mAdapter.getReaderListMOde() === UPDATE_FIRMWARE_TAB) {
//                            getSupportFragmentManager().beginTransaction()
//                                .remove(getCurrentFragment(mAdapter.getSettingsTab())).commit()
//                            getSupportFragmentManager().beginTransaction().addToBackStack(null)
//                            getSupportFragmentManager().executePendingTransactions()
//                            mAdapter.setReaderListMOde(MAIN_HOME_SETTINGS_TAB)
//                        } else {
//                            if (getCurrentFragment(mAdapter.getSettingsTab()) != null) {
//                                getSupportFragmentManager().beginTransaction()
//                                    .remove(getCurrentFragment(mAdapter.getSettingsTab())).commit()
//                                getSupportFragmentManager().beginTransaction().addToBackStack(null)
//                                getSupportFragmentManager().executePendingTransactions()
//                            }
//                        }
//                        if (mConnectedReader != null && mConnectedReader.getHostName()
//                                .startsWith("RFD8500")
//                        ) setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE)
//                        if (mAdapter.getRFIDMOde() === RAPID_READ_TAB) loadNextFragment(
//                            RAPID_READ_TAB
//                        ) else if (mAdapter.getRFIDMOde() === INVENTORY_TAB) loadNextFragment(
//                            INVENTORY_TAB
//                        ) else if (mAdapter.getRFIDMOde() === RFID_ACCESS_TAB) loadNextFragment(
//                            RFID_ACCESS_TAB
//                        ) else if (mAdapter.getRFIDMOde() === RFID_PREFILTERS_TAB) loadNextFragment(
//                            RFID_PREFILTERS_TAB
//                        ) else if (mAdapter.getRFIDMOde() === LOCATE_TAG_TAB) loadNextFragment(
//                            LOCATE_TAG_TAB
//                        ) else if (mAdapter.getRFIDMOde() === RFID_SETTINGS_TAB) loadNextFragment(
//                            RFID_SETTINGS_TAB
//                        ) else if (mAdapter.getRFIDMOde() === NONOPER_TAB) loadNextFragment(
//                            RAPID_READ_TAB
//                        )
//                    }
//
//                    READERS_TAB -> {
//                        if (mAdapter.getReaderListMOde() === UPDATE_FIRMWARE_TAB) {
//                            getSupportFragmentManager().beginTransaction()
//                                .remove(getCurrentFragment(mAdapter.getSettingsTab())).commit()
//                            getSupportFragmentManager().beginTransaction().addToBackStack(null)
//                            getSupportFragmentManager().executePendingTransactions()
//                            mAdapter.setReaderListMOde(MAIN_HOME_SETTINGS_TAB)
//                        }
//                        loadNextFragment(READER_LIST_TAB)
//                    }
//
//                    SCAN_TAB -> {
//                        if (RFD_DEVICE_MODE === DEVICE_PREMIUM_PLUS_MODE) {
//                            if (getCurrentFragment(mAdapter.getSettingsTab()) != null) {
//                                getSupportFragmentManager().beginTransaction()
//                                    .remove(getCurrentFragment(mAdapter.getSettingsTab())).commit()
//                                getSupportFragmentManager().beginTransaction().addToBackStack(null)
//                                getSupportFragmentManager().executePendingTransactions()
//                            }
//                            loadNextFragment(SCAN_DATAVIEW_TAB)
//                            barcodeCount = findViewById(R.id.barcodesListCount) as TextView?
//                            barcodeCount!!.text =
//                                "Barcodes Scanned: " + Integer.toString(iBarcodeCount)
//                            if (mAdapter.getReaderListMOde() === UPDATE_FIRMWARE_TAB) {
//                                getSupportFragmentManager().beginTransaction()
//                                    .remove(getCurrentFragment(mAdapter.getSettingsTab())).commit()
//                                getSupportFragmentManager().beginTransaction().addToBackStack(null)
//                                getSupportFragmentManager().executePendingTransactions()
//                                mAdapter.setReaderListMOde(MAIN_HOME_SETTINGS_TAB)
//                            }
//                            if (mConnectedReader != null && mConnectedReader.getHostName()
//                                    .startsWith("RFD8500")
//                            ) setTriggerMode(ENUM_TRIGGER_MODE.BARCODE_MODE)
//                            break
//                        } else if (RFD_DEVICE_MODE === DEVICE_STD_MODE) {
//                            getSupportFragmentManager().beginTransaction()
//                                .remove(getCurrentFragment(mAdapter.getSettingsTab())).commit()
//                            getSupportFragmentManager().beginTransaction().addToBackStack(null)
//                            getSupportFragmentManager().executePendingTransactions()
//                        }
//                        loadNextFragment(MAIN_HOME_SETTINGS_TAB)
//                    }
//
//                    SETTINGS_TAB -> loadNextFragment(MAIN_HOME_SETTINGS_TAB)
//                }
//            }
//
//            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
//            override fun onPageScrollStateChanged(arg0: Int) {}
//        })
//        if (getIntent().getBooleanExtra(
//                Constants.SHOW_BARCODE_VIEW,
//                false
//            )
//        ) viewPager!!.currentItem =
//            BARCODE_TAB
//        val ns = Context.NOTIFICATION_SERVICE
//        val nMgr = mActiveDeviceActivity.getSystemService(ns) as NotificationManager?
//        nMgr?.cancel(NotificationsReceiver.DEFAULT_NOTIFICATION_ID)
//        setActionBarTitle("Rapid")
//        viewPager!!.currentItem = RFID_TAB
//        mAdapter.setCurrentActivePosition(RFID_TAB)
//        reInit()
//        try {
//            if (RFIDController.regionNotSet === false) {
//                RFIDController.getInstance().updateReaderConnection(false)
//            }
//        } catch (e: InvalidUsageException) {
//            Log.d(TAG, "Returned SDK Exception")
//        } catch (e: OperationFailureException) {
//            Log.d(TAG, "Returned SDK Exception")
//        }
//        initScanner()
//        setParentActivity(this)
//        createDWProfile()
//    }
//
//    fun initBatchRequest(v: View?) {
//        val inXML = "<inArgs><scannerID>$scannerID</scannerID></inArgs>"
//        executeCommand(DCSSDK_COMMAND_OPCODE.DCSSDK_DEVICE_BATCH_REQUEST, inXML, null, scannerID)
//    }
//
//    fun deviceStatusReceived(level: Int, charging: Boolean, cause: String?) {
//        battery_percentage!!.text = "$level%"
//        iv_batteryLevel!!.setImageLevel(level)
//    }
//
//    protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // do your stuff here after SecondActivity finished.
//        Log.d(TAG, "Set regulatory done..")
//    }
//
//    fun setRFIDTab() {
//        viewPager!!.currentItem = RFID_TAB
//    }
//
//    protected fun onStart() {
//        super.onStart()
//    }
//
//    protected fun onResume() {
//        super.onResume()
//        if (waitingForFWReboot) {
//            showFwRebootdialog()
//        }
//        if (onSaveInstanceState == true && mReaderDisappeared != null && RFIDController.regionNotSet === false) {
//            //loadNextFragment(MAIN_HOME_SETTINGS_TAB);
//            setCurrentTabFocus(READERS_TAB)
//        }
//        onSaveInstanceState = false
//        mRFIDBaseActivity.activityResumed()
//        if (RFIDController.regionNotSet === true) {
//            val detailsIntent: Intent = Intent(
//                getApplicationContext(),
//                SettingsDetailActivity::class.java
//            )
//            detailsIntent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
//            detailsIntent.putExtra(
//                com.zebra.demo.rfidreader.common.Constants.SETTING_ITEM_ID,
//                R.id.regulatory
//            )
//            detailsIntent.putExtra(
//                com.zebra.demo.rfidreader.common.Constants.SETTING_ON_FACTORY,
//                true
//            )
//            startActivityForResult(detailsIntent, 0)
//        } else if (Application.updateReaderConnection === true) {
//            try {
//                RFIDController.getInstance().updateReaderConnection(false)
//                Application.updateReaderConnection = false
//            } catch (e: InvalidUsageException) {
//                Log.d(TAG, "Returned SDK Exception")
//            } catch (e: OperationFailureException) {
//                if (e != null && e.stackTrace.size > 0) {
//                    Log.e(TAG, e.stackTrace[0].toString())
//                }
//            }
//        } else {
//        }
//    }
//
//    fun showFwRebootdialog() {
//        val fwStatus: String
//        dialogFwRebooting = Dialog(mActiveDeviceActivity)
//        dialogFwRebooting.setContentView(R.layout.dialog_fw_rebooting)
//        val Status = dialogFwRebooting!!.findViewById<View>(R.id.fwstatus) as TextView
//        if (Application.isFirmwareUpdateSuccess === true) {
//            fwStatus =
//                "Firmware update Success. " //Toast.makeText(this, "Firmware update Success", Toast.LENGTH_SHORT).show();
//        } else {
//            fwStatus =
//                "Firmware update failed. " //Toast.makeText(this, "Firmware update Failed", Toast.LENGTH_SHORT).show();
//            Status.setTextColor(Color.RED)
//        }
//
//        //dialogFwRebooting.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        val counter = dialogFwRebooting!!.findViewById<View>(R.id.counter) as TextView
//        Status.text = fwStatus + Status.text
//        dotProgressBar = dialogFwRebooting!!.findViewById<View>(R.id.progressBar) as DotsProgressBar
//        dotProgressBar.setDotsCount(6)
//        dialogFwRebooting!!.setCancelable(false)
//        dialogFwRebooting!!.setCanceledOnTouchOutside(false)
//        dialogFwRebooting!!.show()
//    }
//
//    val isWaitingForFWReboot: Boolean
//        get() {
//            if (waitingForFWReboot) {
//                setWaitingForFWReboot(false)
//                if (dialogFwRebooting != null) {
//                    dialogFwRebooting!!.dismiss()
//                    dialogFwRebooting = null
//                }
//                return true
//            }
//            return false
//        }
//
//    fun reInit() {
//        removeDevEventsDelegate(this)
//        addDevEventsDelegate(this)
//        removeDevConnectiosDelegate(this)
//        addDevConnectionsDelegate(this)
//        //addMissedBarcodes();
//        val ns = Context.NOTIFICATION_SERVICE
//        val nMgr = this.getSystemService(ns) as NotificationManager?
//        nMgr?.cancel(NotificationsReceiver.DEFAULT_NOTIFICATION_ID)
//        scannerID = Application.currentConnectedScannerID
//        mRFIDBaseActivity.reInit(this)
//        //SetTunnelMode(null);
//    }
//
//    protected fun onNewIntent(intent: Intent) {
//        /**
//         * This method gets called, when a new Intent gets associated with the current activity instance.
//         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
//         * at the documentation.
//         *
//         * In our case this method gets called, when the user attaches a Tag to the device.
//         */
//        super.onNewIntent(intent)
//        handleIntent(intent)
//    }
//
//    private fun handleIntent(intent: Intent) {
//        val action = intent.action
//        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
//            nfcData =
//                (getApplication() as com.zebra.demo.application.Application).processNFCData(intent)
//        } else if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) processTAGData(intent)
//    }
//
//    private fun processTAGData(intent: Intent) {
//        Log.d(TAG, "ProcessTAG data ")
//        val rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_TAG)
//        if (rawMessages != null && rawMessages.size > 0) {
//            val messages = arrayOfNulls<NdefMessage>(rawMessages.size)
//            for (i in rawMessages.indices) {
//                messages[i] = rawMessages[i] as NdefMessage
//            }
//            Log.i(TAG, "message size = " + messages.size)
//            val msg = rawMessages[0] as NdefMessage
//            val base = String(msg.records[0].payload)
//            val str = String.format(
//                Locale.getDefault(),
//                "Message entries=%d. Base message is %s",
//                rawMessages.size,
//                base
//            )
//            Log.i(TAG, "message  = $str")
//        }
//    }
//
//    fun copyNfcContent(): String? {
//        return nfcData
//    }
//
//    protected fun onPause() {
//        super.onPause()
//    }
//
//    protected fun onDestroy() {
//        super.onDestroy()
//        removeDevEventsDelegate(this)
//        removeDevConnectiosDelegate(this)
//        if (mRFIDBaseActivity != null) {
//            mRFIDBaseActivity.onDestroy()
//        }
//    }
//
//    private fun minimizeApp() {
//        val startMain = Intent(Intent.ACTION_MAIN)
//        startMain.addCategory(Intent.CATEGORY_HOME)
//        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivity(startMain)
//    }
//
//    fun onBackPressed() {
//        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START)
//        }
//        val position = currentTabPosition
//        val fragment = getCurrentFragment(position)
//        when (position) {
//            READERS_TAB -> if (fragment is PairOperationsFragment || fragment is ReaderDetailsFragment) {
//                loadNextFragment(READER_LIST_TAB)
//            } else {
//                minimizeApp()
//            }
//
//            SCAN_TAB -> {
//                if (RFD_DEVICE_MODE === DEVICE_PREMIUM_PLUS_MODE) {
//                    minimizeApp()
//                }
//                if (fragment != null && fragment is BackPressedFragment) {
//                    (fragment as BackPressedFragment).onBackPressed()
//                }
//                if (fragment is ManagerFragment) {
//                    loadNextFragment(MAIN_HOME_SETTINGS_TAB)
//                } else if (fragment is SettingListFragment) {
//                    loadNextFragment(MAIN_HOME_SETTINGS_TAB)
//                } else if (fragment is BeeperActionsFragment) {
//                    loadNextFragment(SCAN_SETTINGS_TAB)
//                } else if (fragment is SymbologiesFragment) {
//                    loadNextFragment(SCAN_SETTINGS_TAB)
//                } else if (fragment is ScanHomeSettingsFragment) {
//                    loadNextFragment(MAIN_HOME_SETTINGS_TAB)
//                } else if (fragment is SettingsFragment) {
//                    loadNextFragment(MAIN_HOME_SETTINGS_TAB)
//                } else if (fragment is AdvancedFragment) {
//                    loadNextFragment(MAIN_HOME_SETTINGS_TAB)
//                } else if (fragment is AdvancedOptionItemFragment
//                    || fragment is ProfileFragment
//                    || fragment is ProfileFragmentRE40
//                    || fragment is LedFragment
//                ) {
//                    loadNextFragment(MAIN_RFID_SETTINGS_TAB)
//                    //RFID_ADVANCED_OPTIONS_TAB
//                } else if (fragment is BeeperFragment) {
//                } else if (fragment is RegulatorySettingsFragment) {
//                } else if (fragment is WifiFragment) {
//                } else if (fragment is ChargeTerminalFragment) {
//                } else if (fragment is AntennaSettingsFragment) {
//                    loadNextFragment(RFID_ADVANCED_OPTIONS_TAB)
//                } else if (fragment is StartStopTriggersFragment) {
//                } else if (fragment is SingulationControlFragment) {
//                    //loadNextFragment(RFID_ADVANCED_OPTIONS_TAB);
//                } else if (fragment is DPOSettingsFragment) {
//                } else if (fragment is SaveConfigurationsFragment) {
//                } else if (fragment is TagReportingFragment) {
//                } else if (fragment is FactoryResetFragment) {
//                    loadNextFragment(MAIN_GENERAL_SETTINGS_TAB)
//                } else if (fragment is LoggerFragment) {
//                    loadNextFragment(MAIN_GENERAL_SETTINGS_TAB)
//                } else if (fragment is ApplicationSettingsFragment) {
//                    loadNextFragment(MAIN_HOME_SETTINGS_TAB)
//                } else if (fragment is KeyRemapFragment) {
//                    loadNextFragment(MAIN_GENERAL_SETTINGS_TAB)
//                } else if (fragment is UpdateFirmware) {
//                    loadNextFragment(MAIN_GENERAL_SETTINGS_TAB)
//                } else if (fragment is AssertFragment) {
//                    loadNextFragment(MAIN_GENERAL_SETTINGS_TAB)
//                } else if (fragment is Static_ipconfig) {
//                    loadNextFragment(MAIN_GENERAL_SETTINGS_TAB)
//                } else if (fragment is BatteryStatsFragment) {
//                    loadNextFragment(MAIN_GENERAL_SETTINGS_TAB)
//                } else if (fragment is BatteryFragment) {
//                    loadNextFragment(MAIN_GENERAL_SETTINGS_TAB)
//                } else if (fragment is UsbMiFiFragment) {
//                } else if (fragment is ReaderWifiSettingsFragment) {
//                    loadNextFragment(WIFI_MAIN_PAGE)
//                } else if (fragment is WiFiMainPage) {
//                    loadNextFragment(MAIN_HOME_SETTINGS_TAB)
//                } else {
//                    minimizeApp()
//                }
//            }
//
//            SETTINGS_TAB -> {
//                if (fragment != null && fragment is BackPressedFragment) {
//                    (fragment as BackPressedFragment).onBackPressed()
//                }
//                if (fragment is ManagerFragment) {
//                    loadNextFragment(MAIN_HOME_SETTINGS_TAB)
//                } else if (fragment is SettingListFragment) {
//                    loadNextFragment(MAIN_HOME_SETTINGS_TAB)
//                } else if (fragment is BeeperActionsFragment) {
//                    loadNextFragment(SCAN_SETTINGS_TAB)
//                } else if (fragment is SymbologiesFragment) {
//                    loadNextFragment(SCAN_SETTINGS_TAB)
//                } else if (fragment is ScanHomeSettingsFragment) {
//                    loadNextFragment(MAIN_HOME_SETTINGS_TAB)
//                } else if (fragment is SettingsFragment) {
//                    loadNextFragment(MAIN_HOME_SETTINGS_TAB)
//                } else if (fragment is AdvancedFragment) {
//                    loadNextFragment(MAIN_HOME_SETTINGS_TAB)
//                } else if (fragment is AdvancedOptionItemFragment
//                    || fragment is ProfileFragment
//                    || fragment is ProfileFragmentRE40
//                    || fragment is LedFragment
//                ) {
//                    loadNextFragment(MAIN_RFID_SETTINGS_TAB)
//                } else if (fragment is BeeperFragment) {
//                } else if (fragment is RegulatorySettingsFragment) {
//                } else if (fragment is WifiFragment) {
//                } else if (fragment is ChargeTerminalFragment) {
//                } else if (fragment is AntennaSettingsFragment) {
//                    loadNextFragment(RFID_ADVANCED_OPTIONS_TAB)
//                } else if (fragment is StartStopTriggersFragment) {
//                } else if (fragment is SingulationControlFragment) {
//                } else if (fragment is DPOSettingsFragment) {
//                } else if (fragment is SaveConfigurationsFragment) {
//                } else if (fragment is TagReportingFragment) {
//                } else if (fragment is FactoryResetFragment) {
//                    loadNextFragment(MAIN_GENERAL_SETTINGS_TAB)
//                } else if (fragment is LoggerFragment) {
//                    loadNextFragment(MAIN_GENERAL_SETTINGS_TAB)
//                } else if (fragment is ApplicationSettingsFragment) {
//                    loadNextFragment(MAIN_HOME_SETTINGS_TAB)
//                } else if (fragment is KeyRemapFragment) {
//                    loadNextFragment(MAIN_GENERAL_SETTINGS_TAB)
//                } else if (fragment is UpdateFirmware) {
//                    loadNextFragment(MAIN_GENERAL_SETTINGS_TAB)
//                } else if (fragment is AssertFragment) {
//                    loadNextFragment(MAIN_GENERAL_SETTINGS_TAB)
//                } else if (fragment is Static_ipconfig) {
//                    loadNextFragment(MAIN_GENERAL_SETTINGS_TAB)
//                } else if (fragment is BatteryStatsFragment) {
//                    loadNextFragment(MAIN_GENERAL_SETTINGS_TAB)
//                } else if (fragment is BatteryFragment) {
//                    loadNextFragment(MAIN_GENERAL_SETTINGS_TAB)
//                } else if (fragment is UsbMiFiFragment) {
//                } else if (fragment is ReaderWifiSettingsFragment) {
//                    loadNextFragment(WIFI_MAIN_PAGE)
//                } else if (fragment is WiFiMainPage) {
//                    loadNextFragment(MAIN_HOME_SETTINGS_TAB)
//                } else {
//                    minimizeApp()
//                }
//            }
//
//            RFID_TAB -> if (fragment is PreFilterFragment) {
//                (fragment as PreFilterFragment?).onBackPressed()
//                loadNextFragment(INVENTORY_TAB)
//            } else if (fragment is LocateOperationsFragment || fragment is AccessOperationsFragment) {
//                loadNextFragment(RAPID_READ_TAB)
//            } else {
//                minimizeApp()
//            }
//        }
//        // Fragment fragment = getCurrentFragment(SETTINGS_TAB);
//        return
//    }
//
//    override fun onTabSelected(tab: ActionBar.Tab, fragmentTransaction: FragmentTransaction) {
//        Constants.logAsMessage(
//            TYPE_DEBUG,
//            javaClass.simpleName,
//            "onTabSelected() Position is --- " + tab.position
//        )
//        // on tab selected
//        // show respected fragment view
//        viewPager!!.currentItem = tab.position
//    }
//
//    override fun onTabUnselected(tab: ActionBar.Tab, fragmentTransaction: FragmentTransaction) {}
//    override fun onTabReselected(tab: ActionBar.Tab, fragmentTransaction: FragmentTransaction) {}
//    fun startFirmware(view: View?) {
//        val in_xml = "<inArgs><scannerID>$scannerID</scannerID></inArgs>"
//        cmdExecTask = com.zebra.demo.ActiveDeviceActivity.MyAsyncTask(
//            scannerID,
//            DCSSDK_COMMAND_OPCODE.DCSSDK_START_NEW_FIRMWARE,
//            null
//        )
//        cmdExecTask.execute(*arrayOf(in_xml))
//    }
//
//    fun abortFirmware(view: View?) {
//        val in_xml = "<inArgs><scannerID>$scannerID</scannerID></inArgs>"
//        cmdExecTask = com.zebra.demo.ActiveDeviceActivity.MyAsyncTask(
//            scannerID,
//            DCSSDK_COMMAND_OPCODE.DCSSDK_ABORT_UPDATE_FIRMWARE,
//            null
//        )
//        cmdExecTask.execute(*arrayOf(in_xml))
//    }
//
//    fun loadLedActions(view: View?) {
//        val intent: Intent = Intent(this, LEDActivity::class.java)
//        intent.putExtra(Constants.SCANNER_ID, scannerID)
//        intent.putExtra(Constants.SCANNER_NAME, getIntent().getStringExtra(Constants.SCANNER_NAME))
//        startActivity(intent)
//    }
//
//    fun loadBeeperActions(view: View?) {
//        loadNextFragment(BEEPER_ACTION_TAB)
//    }
//
//    fun beeperAction(view: View?) {
//        val position = currentTabPosition
//        val fragment = getCurrentFragment(position)
//        if (fragment is BeeperActionsFragment) (fragment as BeeperActionsFragment?).beeperAction(
//            view
//        )
//    }
//
//    fun factoryResetClicked(view: View?) {
//        loadNextFragment(FACTORY_RESET_FRAGMENT_TAB)
//    }
//
//    fun enableLoggingClicked(view: View?) {
//        if (RFIDController.mConnectedReader != null && RFIDController.mConnectedReader.getHostName()
//                .startsWith("MC33")
//        ) {
//            Toast.makeText(this, "Real time log not supported for MC33", Toast.LENGTH_SHORT).show()
//        }
//        loadNextFragment(LOGGER_FRAGMENT_TAB)
//        return
//    }
//
//    fun generalSettingsClicked(view: View?) {
//        loadNextFragment(MAIN_GENERAL_SETTINGS_TAB)
//    }
//
//    fun showRFIDSettings(view: View?) {
//        loadNextFragment(RFID_SETTINGS_TAB)
//    }
//
//    fun wifiStatusClicked(view: View?) {
//        loadNextFragment(WIFI_STATUS_TAB)
//    }
//
//    fun wifiConfigPageClicked(view: View?) {
//        val tab: Int = if (RFD_DEVICE_MODE === DEVICE_STD_MODE) SCAN_TAB else SETTINGS_TAB
//        setCurrentTabFocus(tab)
//        loadNextFragment(READER_WIFI_SETTINGS_TAB)
//    }
//
//    fun scanSettingsClicked(view: View?) {
//        loadNextFragment(SCAN_SETTINGS_TAB)
//    }
//
//    fun wifiSettingsClicked(view: View?) {
//        loadNextFragment(WIFI_MAIN_PAGE)
//    }
//
//    fun applicationSettingsClicked(view: View?) {
//        loadNextFragment(APPLICATION_SETTINGS_TAB)
//        return
//    }
//
//    fun deviceResetClicked(view: View?) {
//        loadNextFragment(DEVICE_RESET_TAB)
//    }
//
//    fun showDeviceInfoClicked(view: View?) {
//        loadNextFragment(ASSERT_DEVICE_INFO_TAB)
//    }
//
//    fun staticIpConfig(view: View?) {
//        loadNextFragment(STATIC_IP_CONFIG)
//    }
//
//    fun keyRemapClicked(view: View) {
//        if (RFIDController.mConnectedReader != null) {
//            if (RFIDController.mConnectedReader.getHostName().startsWith("RFD40")
//                || RFIDController.mConnectedReader.getHostName().startsWith("RFD90")
//            ) {
//                loadNextFragment(KEYREMAP_TAB)
//            } else {
//                view.isEnabled = false
//                Toast.makeText(
//                    this,
//                    "Trigger Mapping feature is not supported for " + mConnectedReader.getHostName(),
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        } else {
//            Toast.makeText(
//                this,
//                "Trigger Mapping feature is not supported when device not connected ",
//                Toast.LENGTH_LONG
//            ).show()
//        }
//    }
//
//    fun loadAssert(view: View?) {
//        loadNextFragment(ASSERT_DEVICE_INFO_TAB)
//    }
//
//    fun symbologiesClicked(view: View?) {
//        loadNextFragment(BARCODE_SYMBOLOGIES_TAB)
//    }
//
//    fun enableScanning(view: View?) {
//        val in_xml = "<inArgs><scannerID>$scannerID</scannerID></inArgs>"
//        cmdExecTask = com.zebra.demo.ActiveDeviceActivity.MyAsyncTask(
//            scannerID,
//            DCSSDK_COMMAND_OPCODE.DCSSDK_DEVICE_SCAN_ENABLE,
//            null
//        )
//        cmdExecTask.execute(*arrayOf(in_xml))
//    }
//
//    fun disableScanning(view: View?) {
//        val in_xml = "<inArgs><scannerID>$scannerID</scannerID></inArgs>"
//        cmdExecTask = com.zebra.demo.ActiveDeviceActivity.MyAsyncTask(
//            scannerID,
//            DCSSDK_COMMAND_OPCODE.DCSSDK_DEVICE_SCAN_DISABLE,
//            null
//        )
//        cmdExecTask.execute(*arrayOf(in_xml))
//    }
//
//    fun aimOn(view: View?) {
//        val in_xml = "<inArgs><scannerID>$scannerID</scannerID></inArgs>"
//        cmdExecTask = com.zebra.demo.ActiveDeviceActivity.MyAsyncTask(
//            scannerID,
//            DCSSDK_COMMAND_OPCODE.DCSSDK_DEVICE_AIM_ON,
//            null
//        )
//        cmdExecTask.execute(*arrayOf(in_xml))
//    }
//
//    fun aimOff(view: View?) {
//        val in_xml = "<inArgs><scannerID>$scannerID</scannerID></inArgs>"
//        cmdExecTask = com.zebra.demo.ActiveDeviceActivity.MyAsyncTask(
//            scannerID,
//            DCSSDK_COMMAND_OPCODE.DCSSDK_DEVICE_AIM_OFF,
//            null
//        )
//        cmdExecTask.execute(*arrayOf(in_xml))
//    }
//
//    fun vibrationFeedback(view: View?) {
//        val intent: Intent = Intent(this, VibrationFeedback::class.java)
//        intent.putExtra(Constants.SCANNER_ID, scannerID)
//        intent.putExtra(Constants.SCANNER_NAME, getIntent().getStringExtra(Constants.SCANNER_NAME))
//        startActivity(intent)
//    }
//
//    fun setTriggerMode(triggerMode: ENUM_TRIGGER_MODE?) {
//        mRFIDBaseActivity.setTriggerMode(triggerMode)
//    }
//
//    fun pullTrigger(view: View?) {
//        val in_xml = "<inArgs><scannerID>$scannerID</scannerID></inArgs>"
//        cmdExecTask = com.zebra.demo.ActiveDeviceActivity.MyAsyncTask(
//            scannerID,
//            DCSSDK_COMMAND_OPCODE.DCSSDK_DEVICE_PULL_TRIGGER,
//            null
//        )
//        cmdExecTask.execute(*arrayOf(in_xml))
//    }
//
//    fun releaseTrigger(view: View?) {
//        val in_xml = "<inArgs><scannerID>$scannerID</scannerID></inArgs>"
//        cmdExecTask = com.zebra.demo.ActiveDeviceActivity.MyAsyncTask(
//            scannerID,
//            DCSSDK_COMMAND_OPCODE.DCSSDK_DEVICE_RELEASE_TRIGGER,
//            null
//        )
//        cmdExecTask.execute(*arrayOf(in_xml))
//        // view.setEnabled(true);
//        // view.setOnClickListener(new View.OnClickListener() {
//        //     @Override
//        //    public void onClick(View v) {
//        //        scanTrigger(v);
//        //    }
//        // });
//    }
//
//    fun SetTunnelMode(view: View?) {
//        val inXML =
//            "<inArgs><scannerID>" + Application.currentConnectedScannerID + "</scannerID><cmdArgs><arg-int>" +
//                    18 + "</arg-int></cmdArgs></inArgs>"
//        val outXML = StringBuilder()
//        executeCommand(
//            DCSSDK_COMMAND_OPCODE.DCSSDK_SET_ACTION,
//            inXML,
//            outXML,
//            Application.currentConnectedScannerID
//        )
//    }
//
//    var pickListMode: Int
//        get() {
//            //String in_xml = "<inArgs><scannerID>" + Application.currentConnectedScannerID + "</scannerID><cmdArgs><arg-xml><attrib_list>402</attrib_list></arg-xml></cmdArgs></inArgs>";
//            var attrVal = 0
//            val in_xml =
//                "<inArgs><scannerID>" + Application.currentConnectedScannerID + "</scannerID><cmdArgs><arg-xml><attrib_list>402</attrib_list></arg-xml></cmdArgs></inArgs>"
//            val outXML = StringBuilder()
//            executeCommand(
//                DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_GET,
//                in_xml,
//                outXML,
//                Application.currentConnectedScannerID
//            )
//            try {
//                val parser = Xml.newPullParser()
//                parser.setInput(StringReader(outXML.toString()))
//                var event = parser.eventType
//                var text: String? = null
//                while (event != XmlPullParser.END_DOCUMENT) {
//                    val name = parser.name
//                    when (event) {
//                        XmlPullParser.START_TAG -> {}
//                        XmlPullParser.TEXT -> text = parser.text
//                        XmlPullParser.END_TAG -> if (name == "value") {
//                            attrVal = text?.trim { it <= ' ' }!!.toInt()
//                        }
//                    }
//                    event = parser.next()
//                }
//            } catch (e: Exception) {
//                Log.e(TAG, e.toString())
//            }
//            return attrVal
//        }
//        set(picklistInt) {
//            val in_xml =
//                "<inArgs><scannerID>$scannerID</scannerID><cmdArgs><arg-xml><attrib_list><attribute><id>402</id><datatype>B</datatype><value>$picklistInt</value></attribute></attrib_list></arg-xml></cmdArgs></inArgs>"
//            val outXML = StringBuilder()
//            cmdExecTask = com.zebra.demo.ActiveDeviceActivity.MyAsyncTask(
//                scannerID,
//                DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_SET,
//                outXML
//            )
//            cmdExecTask.execute(*arrayOf(in_xml))
//        }
//
//    private fun addMissedBarcodes() {
//        if (barcodeQueue.size() !== iBarcodeCount) {
//            for (i in iBarcodeCount until barcodeQueue.size()) {
//                scannerBarcodeEvent(
//                    barcodeQueue.get(i).getBarcodeData(),
//                    barcodeQueue.get(i).getBarcodeType(),
//                    barcodeQueue.get(i).getFromScannerID()
//                )
//            }
//        }
//    }
//
//    @Synchronized
//    fun scannerBarcodeEvent(barcodeData: ByteArray?, barcodeType: Int, scannerID: Int) {
//        if (viewPager!!.currentItem != BARCODE_TAB) {
//            Log.d(TAG, "Cached barcode Data")
//            return
//        }
//        Log.d(TAG, "Rendering barcode Data")
//        runOnUiThread(Runnable {
//            // Fragment fragment = mAdapter.getRegisteredFragment(BARCODE_TAB);
//            // if((fragment instanceof BarcodeFargment) == false)
//            //     return;
//            val barcodeFargment: BarcodeFargment =
//                mAdapter.getRegisteredFragment(BARCODE_TAB) as BarcodeFargment
//            if (barcodeFargment != null) {
//                barcodeFargment.showBarCode()
//                barcodeCount = findViewById(R.id.barcodesListCount) as TextView?
//                barcodeCount!!.text = "Barcodes Scanned: " + Integer.toString(++iBarcodeCount)
//                if (iBarcodeCount > 0) {
//                    val btnClear = findViewById(R.id.btnClearList) as Button
//                    btnClear.isEnabled = true
//                }
//                if (!Application.isFirmwareUpdateInProgress) {
//                    //viewPager.setCurrentItem(BARCODE_TAB);
//                }
//            }
//        })
//    }
//
//    fun scannerFirmwareUpdateEvent(firmwareUpdateEvent: FirmwareUpdateEvent?) {
//        val setTab: Int = if (RFD_DEVICE_MODE === DEVICE_STD_MODE) SCAN_TAB else SETTINGS_TAB
//        val fragment = getCurrentFragment(setTab)
//        if (fragment is UpdateFirmware) (fragment as UpdateFirmware?).scannerFirmwareUpdateEvent(
//            firmwareUpdateEvent
//        )
//    }
//
//    fun scannerImageEvent(imageData: ByteArray?) {}
//    fun scannerVideoEvent(videoData: ByteArray?) {}
//    fun clearList(view: View?) {
//        val barcodeFargment: BarcodeFargment =
//            mAdapter.getRegisteredFragment(BARCODE_TAB) as BarcodeFargment
//        if (barcodeFargment != null) {
//            barcodeFargment.clearList()
//            barcodeCount = findViewById(R.id.barcodesListCount) as TextView?
//            iBarcodeCount = 0
//            barcodeCount!!.text = "Barcodes Scanned: " + Integer.toString(iBarcodeCount)
//            val btnClear = findViewById(R.id.btnClearList) as Button
//            btnClear.isEnabled = false
//        }
//    }
//
//    fun scanTrigger(view: View?) {
//        val in_xml =
//            "<inArgs><scannerID>" + Application.currentConnectedScannerID + "</scannerID></inArgs>"
//        cmdExecTask = com.zebra.demo.ActiveDeviceActivity.MyAsyncTask(
//            Application.currentConnectedScannerID,
//            DCSSDK_COMMAND_OPCODE.DCSSDK_DEVICE_PULL_TRIGGER,
//            null
//        )
//        cmdExecTask.execute(*arrayOf(in_xml))
//    }
//
//    /**
//     * Navigate to Scale view
//     *
//     * @param view
//     */
//    fun loadScale(view: View?) {
//        val in_xml = "<inArgs><scannerID>$scannerID</scannerID></inArgs>"
//        com.zebra.demo.ActiveDeviceActivity.AsyncTaskScaleAvailable(
//            scannerID, DCSSDK_COMMAND_OPCODE.DCSSDK_READ_WEIGHT, this,
//            ScaleActivity::class.java
//        ).execute(*arrayOf<String>(in_xml))
//    }
//
//    fun locationingButtonClicked(v: View?) {
//        mRFIDBaseActivity.locationingButtonClicked(v)
//    }
//
//    fun accessOperationsReadClicked(v: View?) {
//        mRFIDBaseActivity.accessOperationsReadClicked(v)
//    }
//
//    fun accessOperationLockClicked(v: View?) {
//        mRFIDBaseActivity.accessOperationLockClicked(v)
//    }
//
//    fun accessOperationsWriteClicked(v: View?) {
//        mRFIDBaseActivity.accessOperationsWriteClicked(v)
//    }
//
//    fun accessOperationsKillClicked(v: View?) {
//        mRFIDBaseActivity.accessOperationsKillClicked(v)
//    }
//
//    @Synchronized
//    fun locationingButtonClicked(btn_locate: FloatingActionButton?) {
//        mRFIDBaseActivity.locationingButtonClicked(btn_locate)
//    }
//
//    @Synchronized
//    fun multiTagLocateStartOrStop(view: View?) {
//        mRFIDBaseActivity.multiTagLocateStartOrStop(view)
//    }
//
//    @Synchronized
//    fun multiTagLocateAddTagItem(view: View?) {
//        mRFIDBaseActivity.multiTagLocateAddTagItem(view)
//    }
//
//    @Synchronized
//    fun multiTagLocateDeleteTagItem(view: View?) {
//        mRFIDBaseActivity.multiTagLocateDeleteTagItem(view)
//    }
//
//    @Synchronized
//    fun multiTagLocateReset(view: View?) {
//        mRFIDBaseActivity.multiTagLocateReset(view)
//    }
//
//    @Synchronized
//    fun multiTagLocateClearTagItems(view: View?) {
//        mRFIDBaseActivity.multiTagLocateClearTagItems(view)
//    }
//
//    fun showBatteryStatsClicked(view: View?) {
//        loadNextFragment(BATTERY_STATISTICS_TAB)
//    }
//
//    fun showBatteryStats() {
//        viewPager!!.currentItem = mAdapter.getSettingsTab()
//        loadNextFragment(BATTERY_STATISTICS_TAB)
//    }
//
//    fun callBackPressed() {
//        mRFIDBaseActivity.callBackPressed()
//    }
//
//    fun selectItem(i: Int) {}
//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        drawer!!.closeDrawer(GravityCompat.START)
//        val id = item.itemId
//        if (item.itemId == R.id.home) {
//            finish()
//        }
//        when (id) {
//            R.id.home -> {
//                onBackPressed()
//                return true
//            }
//
//            R.id.nav_fw_update -> if (mConnectedReader != null && mConnectedReader.isConnected()) {
//                loadUpdateFirmware(item.actionView)
//            } else {
//                Toast.makeText(this, "No device in connected state", Toast.LENGTH_SHORT).show()
//            }
//
//            R.id.nav_battery_statics ->
////                Intent detailsIntent = new Intent(this, SettingsDetailActivity.class);
////                detailsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                detailsIntent.putExtra(com.zebra.demo.rfidreader.common.Constants.SETTING_ITEM_ID, R.id.battery);
////                startActivity(detailsIntent);
//                showBatteryStats()
//
//            R.id.nav_about -> showDeviceInfoClicked(item.actionView)
//            R.id.nav_settings -> {
//                viewPager!!.currentItem = mAdapter.getSettingsTab()
//                loadNextFragment(MAIN_HOME_SETTINGS_TAB)
//            }
//
//            R.id.menu_readers -> {
//                val fragment = getCurrentFragment(READERS_TAB)
//                if (fragment is PairOperationsFragment) loadNextFragment(READER_LIST_TAB)
//                viewPager!!.currentItem = READERS_TAB
//            }
//
//            R.id.nav_connection_help -> {
//                val helpIntent: Intent = Intent(
//                    this,
//                    NavigationHelpActivity::class.java
//                )
//                startActivity(helpIntent)
//            }
//        }
//        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
//        drawer.closeDrawer(GravityCompat.START)
//        drawer.isSelected = true
//        return true
//    }
//
//    fun setActionBarTitle(title: String?) {
//        getSupportActionBar().setTitle(title)
//    }
//
//    val currentTabPosition: Int
//        get() = mAdapter.getCurrentActivePosition()
//
//    protected fun onStop() {
//        super.onStop()
//        onSaveInstanceState = true
//    }
//
//    fun onSaveInstanceState(savedInstanceState: Bundle) {
//        // Always call the superclass so it can save the view hierarchy state
//        super.onSaveInstanceState(savedInstanceState)
//        savedInstanceState.putString("default_tab", "readers")
//        onSaveInstanceState = true
//    }
//
//    protected fun onRestoreInstanceState(savedInstanceState: Bundle?) {
//        super.onRestoreInstanceState(savedInstanceState)
//        if (waitingForFWReboot === false) {
//            //viewPager.setCurrentItem(READERS_TAB);
//            setCurrentTabFocus(READERS_TAB)
//            try {
//                Thread.sleep(500)
//            } catch (e: InterruptedException) {
//                if (e != null && e.stackTrace.size > 0) {
//                    Log.e(TAG, e.stackTrace[0].toString())
//                }
//            }
//        }
//        onSaveInstanceState = false
//    }
//
//    fun setCurrentTabFocus(pos: Int) {
//        if (onSaveInstanceState == false) {
//            //loadNextFragment(MAIN_HOME_SETTINGS_TAB);
//            if (mAdapter.getCurrentActivePosition() !== pos) viewPager!!.currentItem = pos
//        }
//    }
//
//    fun setCurrentTabFocus(pos: Int, fragment: Int) {
//        if (!onSaveInstanceState) {
//            if (mAdapter.getCurrentActivePosition() !== pos) {
//                viewPager!!.currentItem = pos
//                loadNextFragment(fragment)
//            }
//        }
//    }
//
//    fun sendNotification(actionReaderStatusObtained: String?, info: String?) {
//        mRFIDBaseActivity.sendNotification(actionReaderStatusObtained, info)
//    }
//
//    fun loadWifiReaderSettings() {
//        val tab: Int = if (RFD_DEVICE_MODE === DEVICE_STD_MODE) SCAN_TAB else SETTINGS_TAB
//        setCurrentTabFocus(tab)
//        loadNextFragment(READER_WIFI_SETTINGS_TAB)
//    }
//
//    fun loadScanSettings(view: View?) {
//        loadNextFragment(SCAN_SETTINGS_TAB)
//    }
//
//    fun loadScanAdvancedSettings(view: View?) {
//        loadNextFragment(SCAN_ADVANCED_TAB)
//    }
//
//    fun OnAdvancedListFragmentInteractionListener(item: AdvancedOptionsContent.SettingItem) {
//        var fragment: Fragment? = null
//        val settingItemSelected: Int = item.id.toInt()
//        when (settingItemSelected) {
//            R.id.antenna -> loadNextFragment(ANTENNA_SETTINGS_TAB)
//            R.id.singulation_control -> loadNextFragment(SINGULATION_CONTROL_TAB)
//            R.id.start_stop_triggers -> loadNextFragment(START_STOP_TRIGGER_TAB)
//            R.id.tag_reporting -> loadNextFragment(TAG_REPORTING_TAB)
//            R.id.save_configuration -> loadNextFragment(SAVE_CONFIG_TAB)
//            R.id.power_management -> {
//                fragment = DPOSettingsFragment.newInstance()
//                loadNextFragment(DPO_SETTING_TAB)
//            }
//        }
//        setTitle(item.content)
//    }
//
//    fun timerDelayRemoveDialog(time: Long, d: Dialog?, command: String, isPressBack: Boolean) {
//        Handler().postDelayed({
//            if (d != null && d.isShowing) {
//                sendNotification(
//                    com.zebra.demo.rfidreader.common.Constants.ACTION_READER_STATUS_OBTAINED,
//                    "$command timeout"
//                )
//                d.dismiss()
//                if (isActivityVisible && isPressBack) callBackPressed()
//            }
//        }, time)
//    }
//
//    /**
//     * Method called when save config button is clicked
//     *
//     * @param v - View to be addressed
//     */
//    fun saveConfigClicked(v: View?) {
//        if (mConnectedReader != null && mConnectedReader.isConnected()) {
//            progressDialog =
//                CustomProgressDialog(this, getString(R.string.save_config_progress_title))
//            progressDialog!!.show()
//            timerDelayRemoveDialog(
//                com.zebra.demo.rfidreader.common.Constants.SAVE_CONFIG_RESPONSE_TIMEOUT,
//                progressDialog,
//                getString(R.string.status_failure_message),
//                false
//            )
//            object : AsyncTask<Void?, Void?, Boolean>() {
//                private var operationFailureException: OperationFailureException? = null
//                protected override fun doInBackground(vararg voids: Void): Boolean {
//                    var bResult = false
//                    try {
//                        mConnectedReader.Config.saveConfig()
//                        bResult = true
//                    } catch (e: InvalidUsageException) {
//                        if (e != null && e.stackTrace.size > 0) {
//                            Log.e(TAG, e.stackTrace[0].toString())
//                        }
//                    } catch (e: OperationFailureException) {
//                        if (e != null && e.stackTrace.size > 0) {
//                            Log.e(TAG, e.stackTrace[0].toString())
//                        }
//                        operationFailureException = e
//                    }
//                    return bResult
//                }
//
//                override fun onPostExecute(result: Boolean) {
//                    super.onPostExecute(result)
//                    progressDialog!!.dismiss()
//                    if (!result) {
//                        Toast.makeText(
//                            getApplicationContext(),
//                            "This feature is Unsupported for this device",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    } else Toast.makeText(
//                        getApplicationContext(),
//                        resources.getString(R.string.status_success_message),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }.execute()
//        } else Toast.makeText(
//            getApplicationContext(),
//            resources.getString(R.string.error_disconnected),
//            Toast.LENGTH_SHORT
//        ).show()
//    }
//
//    fun changeAdapter(tabCount: Int) {
//        if (tabCount == DEVICE_PREMIUM_PLUS_MODE) mAdapter = ActiveDevicePremiumAdapter(
//            this,
//            getSupportFragmentManager(),
//            DEVICE_PREMIUM_PLUS_MODE
//        ) else mAdapter =
//            ActiveDeviceStandardAdapter(this, getSupportFragmentManager(), DEVICE_STD_MODE)
//        RFD_DEVICE_MODE = tabCount
//        viewPager!!.adapter = mAdapter
//        mAdapter.setDeviceModelName(mConnectedReader.getHostName())
//        //viewPager.getAdapter().notifyDataSetChanged();
//    }
//
//    fun onFactoryReset(readerDevice: ReaderDevice?) {
//        mRFIDBaseActivity.onFactoryReset(readerDevice)
//    }
//
//    fun createFile1(uri: Uri?) {
//        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        intent.setType("application/csv")
//        intent.putExtra(Intent.EXTRA_TITLE, filename)
//        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri)
//        exportresultLauncher.launch(intent)
//    }
//
//    private val filename: String
//        private get() {
//            val sdf = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss.SSS")
//            /* if(ActiveProfile.id.equals("1")) {
//                 if(Application.cycleCountProfileData != null) {
//                     File root = Environment.getExternalStorageDirectory();
//                     File dir = new File(root.getAbsolutePath() + "/inventory");
//                     File file = new File(dir, Application.cycleCountProfileData);
//                     if(file.exists()) {
//                         file.delete();
//                     }
//                 }
//                 Application.cycleCountProfileData = mConnectedReader + "_" + sdf.format(new Date()) + ".csv";
//                 return Application.cycleCountProfileData;
//             }*/return "RFID" + "_" + sdf.format(Date()) + ".csv"
//        }
//    val deviceAdapter: ActiveDeviceAdapter?
//        get() = mAdapter
//
//    /**
//     * scale availability check
//     */
//    private inner class AsyncTaskScaleAvailable(
//        var scannerId: Int,
//        var opcode: DCSSDK_COMMAND_OPCODE,
//        var context: Context,
//        var targetClass: Class<*>
//    ) :
//        AsyncTask<String?, Int?, Boolean>() {
//        private var progressDialog: CustomProgressDialog? = null
//        override fun onPreExecute() {
//            super.onPreExecute()
//            progressDialog = CustomProgressDialog(this@ActiveDeviceActivity, "Please wait...")
//            progressDialog.setCancelable(false)
//            progressDialog.show()
//        }
//
//        protected override fun doInBackground(vararg strings: String): Boolean {
//            val sb = StringBuilder()
//            val result: Boolean = executeCommand(opcode, strings[0], sb, scannerId)
//            if (opcode == DCSSDK_COMMAND_OPCODE.DCSSDK_READ_WEIGHT) {
//                if (result) {
//                    return true
//                }
//            }
//            return false
//        }
//
//        override fun onPostExecute(scaleAvailability: Boolean) {
//            super.onPostExecute(scaleAvailability)
//            if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss()
//            val intent = Intent(context, targetClass)
//            intent.putExtra(Constants.SCANNER_ID, scannerID)
//            intent.putExtra(
//                Constants.SCANNER_NAME,
//                getIntent().getStringExtra(Constants.SCANNER_NAME)
//            )
//            intent.putExtra(Constants.SCALE_STATUS, scaleAvailability)
//            startActivity(intent)
//        }
//    }
//
//    fun updateBarcodeCount() {
//        if (barcodeQueue.size() !== iBarcodeCount) {
//            barcodeCount = findViewById(R.id.barcodesListCount) as TextView?
//            iBarcodeCount = barcodeQueue.size()
//            barcodeCount!!.text = "Barcodes Scanned: " + Integer.toString(iBarcodeCount)
//            if (iBarcodeCount > 0) {
//                val btnClear = findViewById(R.id.btnClearList) as Button
//                btnClear.isEnabled = true
//            }
//        }
//    }
//
//    fun scannerHasAppeared(scannerID: Int): Boolean {
//        return false
//    }
//
//    fun scannerHasDisappeared(scannerID: Int): Boolean {
//        if (null != cmdExecTask) {
//            cmdExecTask.cancel(true)
//        }
//        barcodeQueue.clear()
//        return true
//    }
//
//    fun scannerHasConnected(scannerID: Int): Boolean {
//        barcodeQueue.clear()
//        return false
//    }
//
//    fun scannerHasDisconnected(scannerID: Int): Boolean {
//        barcodeQueue.clear()
//        return true
//    }
//
//    fun updateFirmware(view: View?) {
//        val setTab: Int = if (RFD_DEVICE_MODE === DEVICE_STD_MODE) SCAN_TAB else SETTINGS_TAB
//        val fragment = getCurrentFragment(setTab)
//        if (fragment is UpdateFirmware) (fragment as UpdateFirmware?).updateFirmware(view)
//    }
//
//    /**
//     * select firmware file
//     * @param view user interface
//     */
//    fun selectFirmware(view: View?) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if (Environment.isExternalStorageManager()) {
//                selectFirmwareFile()
//            } else {
//                val intent = Intent()
//                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
//                /*intent.addCategory("android.intent.category.DEFAULT");
//                String uriString = String.format("package:%s",getPackageName());
//                Log.i(TAG,"URI for Permission - "+uriString);
//                intent.setData(Uri.parse(uriString));*/allFilesAccessLauncher.launch(intent)
//            }
//        } else {
//            selectFirmwareFile()
//        }
//    }
//
//    private fun selectFirmwareFile() {
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        intent.setType("*/*")
//        val uri =
//            Uri.parse("content://com.android.externalstorage.documents/document/primary:Download")
//        intent.putExtra("DocumentsContract.EXTRA_INITIAL_URI", uri)
//        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        activityResultLauncher.launch(intent)
//    }
//
//    var activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
//        StartActivityForResult(),
//        object : ActivityResultCallback<ActivityResult?> {
//            override fun onActivityResult(result: ActivityResult) {
//                if (result.resultCode == Activity.RESULT_OK) {
//                    val data = result.data
//                    val documentUri: Uri?
//                    if (data != null) {
//                        if (data.data.toString().contains("content://com.android.providers")) {
//                            runOnUiThread { this.ShowPlugInPathChangeDialog() }
//                        } else {
//                            val setTab: Int =
//                                if (RFD_DEVICE_MODE === DEVICE_STD_MODE) SCAN_TAB else SETTINGS_TAB
//                            val fragment = getCurrentFragment(setTab)
//                            if (fragment is UpdateFirmware) {
//                                documentUri = data.data
//                                //((UpdateFirmware) fragment).selectedFile(data.getData());
//                                (fragment as UpdateFirmware?).selectedFile(documentUri)
//                            }
//                        }
//                    }
//                }
//            }
//
//            private fun ShowPlugInPathChangeDialog() {
//                if (!isFinishing()) {
//                    val dialog = Dialog(this@ActiveDeviceActivity)
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//                    dialog.setContentView(R.layout.dialog_plugin_path_change)
//                    dialog.setCancelable(false)
//                    dialog.setCanceledOnTouchOutside(false)
//                    dialog.show()
//                    val declineButton = dialog.findViewById<View>(R.id.btn_ok) as TextView
//                    declineButton.setOnClickListener { v: View? -> dialog.dismiss() }
//                }
//            }
//        })
//    var exportresultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
//        StartActivityForResult(),
//        object : ActivityResultCallback<ActivityResult?> {
//            override fun onActivityResult(result: ActivityResult) {
//                if (result.resultCode == Activity.RESULT_OK) {
//                    val data = result.data
//                    val uri = data!!.data
//                    if (data != null) {
//                        mRFIDBaseActivity.exportData(uri)
//                    }
//                }
//            }
//        })
//    var allFilesAccessLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
//        StartActivityForResult()
//    ) { result ->
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
//            selectFirmwareFile()
//        }
//    }
//
//    fun loadUpdateFirmware(view: View?) {
//        val tab: Int = if (RFD_DEVICE_MODE === DEVICE_STD_MODE) SCAN_TAB else SETTINGS_TAB
//        setCurrentTabFocus(tab)
//        loadNextFragment(UPDATE_FIRMWARE_TAB)
//    }
//
//    fun loadReaderDetails(readerDevice: ReaderDevice) {
//        connectedReaderDetails(readerDevice)
//        loadNextFragment(READER_DETAILS_TAB)
//    }
//
//    private fun connectedReaderDetails(readerDevice: ReaderDevice) {
//        mConnectedReaderDetails = readerDevice
//    }
//
//    fun connectedReaderDetails(): ReaderDevice? {
//        return mConnectedReaderDetails
//    }
//
//    fun ImageVideo(view: View?) {
//        if (scannerType != 2) {
//            val message = "Video feature not supported in bluetooth scanners."
//            alertShow(message, false)
//        } else {
//            loadImageVideo()
//        }
//    }
//
//    private fun alertShow(message: String, error: Boolean) {
//        if (error) {
//        } else {
//            val dialog = AlertDialog.Builder(this@ActiveDeviceActivity)
//            dialog.setTitle("Video not supported")
//                .setMessage(message)
//                .setPositiveButton(
//                    "Ok"
//                ) { dialoginterface, i -> loadImageVideo() }.show()
//        }
//    }
//
//    private fun loadImageVideo() {
//        val intent: Intent = Intent(this, ImageActivity::class.java)
//        intent.putExtra(Constants.SCANNER_ID, scannerID)
//        intent.putExtra(Constants.SCANNER_NAME, getIntent().getStringExtra(Constants.SCANNER_NAME))
//        intent.putExtra(Constants.SCANNER_TYPE, scannerType)
//        startActivity(intent)
//    }
//
//    fun loadIdc(view: View?) {
//        val intent: Intent = Intent(
//            this,
//            IntelligentImageCaptureActivity::class.java
//        )
//        intent.putExtra(Constants.SCANNER_ID, scannerID)
//        intent.putExtra(Constants.SCANNER_NAME, getIntent().getStringExtra(Constants.SCANNER_NAME))
//        startActivity(intent)
//    }
//
//    fun loadBatteryStatistics(view: View?) {
//        val in_xml = "<inArgs><scannerID>$scannerID</scannerID></inArgs>"
//        com.zebra.demo.ActiveDeviceActivity.AsyncTaskBatteryAvailable(
//            scannerID, DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_GETALL, this,
//            BatteryStatistics::class.java
//        ).execute(*arrayOf<String>(in_xml))
//    }
//
//    /**
//     * Navigate to Scan Speed Analytics views
//     *
//     * @param view
//     */
//    fun loadScanSpeedAnalytics(view: View?) {
//        // Scan speed analytics symbology type has set
//        if (SsaSetSymbologyActivity.SSA_SYMBOLOGY_ENABLED_FLAG) {
//            // navigate to scan speed analytics view
//            val intent: Intent = Intent(
//                this,
//                ScanSpeedAnalyticsActivity::class.java
//            )
//            intent.putExtra(Constants.SCANNER_ID, scannerID)
//            intent.putExtra(
//                Constants.SYMBOLOGY_SSA_ENABLED,
//                SsaSetSymbologyActivity.SSA_ENABLED_SYMBOLOGY_OBJECT
//            )
//            intent.putExtra(
//                Constants.SCANNER_NAME,
//                getIntent().getStringExtra(Constants.SCANNER_NAME)
//            )
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            var ssaStatus = 0
//            if (scannerType != 1) {
//                ssaStatus = 2
//            }
//            intent.putExtra(Constants.SSA_STATUS, ssaStatus)
//            getApplicationContext().startActivity(intent)
//        } else { // Scan speed analytics symbology type has not set
//            // navigate to Scan speed analytics set view
//            val in_xml = "<inArgs><scannerID>$scannerID</scannerID></inArgs>"
//            com.zebra.demo.ActiveDeviceActivity.AsyncTaskSSASvailable(
//                scannerID, DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_GETALL, this,
//                SsaSetSymbologyActivity::class.java
//            ).execute(*arrayOf<String>(in_xml))
//        }
//    }
//
//    private inner class AsyncTaskBatteryAvailable(
//        var scannerId: Int,
//        var opcode: DCSSDK_COMMAND_OPCODE,
//        var context: Context,
//        var targetClass: Class<*>
//    ) :
//        AsyncTask<String?, Int?, Boolean>() {
//        private var progressDialog: CustomProgressDialog? = null
//
//        init {
//            var res: RFIDResults
//        }
//
//        override fun onPreExecute() {
//            super.onPreExecute()
//            progressDialog = CustomProgressDialog(this@ActiveDeviceActivity, "Please wait...")
//            progressDialog.setCancelable(false)
//            progressDialog.show()
//        }
//
//        protected override fun doInBackground(vararg strings: String): Boolean {
//            val sb = StringBuilder()
//            val result: Boolean = executeCommand(opcode, strings[0], sb, scannerId)
//            if (opcode == DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_GETALL) {
//                if (result) {
//                    try {
//                        val i = 0
//                        val parser = Xml.newPullParser()
//                        parser.setInput(StringReader(sb.toString()))
//                        var event = parser.eventType
//                        var text: String? = null
//                        while (event != XmlPullParser.END_DOCUMENT) {
//                            val name = parser.name
//                            when (event) {
//                                XmlPullParser.START_TAG -> {}
//                                XmlPullParser.TEXT -> text = parser.text
//                                XmlPullParser.END_TAG -> if (name == "attribute") {
//                                    if (text != null && text.trim { it <= ' ' } == "30018") {
//                                        return true
//                                    }
//                                }
//                            }
//                            event = parser.next()
//                        }
//                    } catch (e: Exception) {
//                        Log.e(TAG, e.toString())
//                    }
//                }
//            }
//            return false
//        }
//
//        override fun onPostExecute(b: Boolean) {
//            super.onPostExecute(b)
//            if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss()
//            val intent = Intent(context, targetClass)
//            intent.putExtra(Constants.SCANNER_ID, scannerID)
//            intent.putExtra(
//                Constants.SCANNER_NAME,
//                getIntent().getStringExtra(Constants.SCANNER_NAME)
//            )
//            intent.putExtra(Constants.BATTERY_STATUS, b)
//            startActivity(intent)
//        }
//    }
//
//    private inner class AsyncTaskSSASvailable(
//        var scannerId: Int,
//        var opcode: DCSSDK_COMMAND_OPCODE,
//        var context: Context,
//        var targetClass: Class<*>
//    ) :
//        AsyncTask<String?, Int?, Boolean>() {
//        private var progressDialog: CustomProgressDialog? = null
//        override fun onPreExecute() {
//            super.onPreExecute()
//            progressDialog = CustomProgressDialog(this@ActiveDeviceActivity, "Please wait...")
//            progressDialog.setCancelable(false)
//            progressDialog.show()
//        }
//
//        protected override fun doInBackground(vararg strings: String): Boolean {
//            val sb = StringBuilder()
//            var result: Boolean = executeCommand(opcode, strings[0], sb, scannerId)
//            if (opcode == DCSSDK_COMMAND_OPCODE.DCSSDK_RSM_ATTR_GETALL) {
//                if (result) {
//                    try {
//                        val i = 0
//                        val parser = Xml.newPullParser()
//                        parser.setInput(StringReader(sb.toString()))
//                        var event = parser.eventType
//                        var text: String? = null
//                        ssaSupportedAttribs = ArrayList()
//                        while (event != XmlPullParser.END_DOCUMENT) {
//                            val name = parser.name
//                            when (event) {
//                                XmlPullParser.START_TAG -> {}
//                                XmlPullParser.TEXT -> text = parser.text
//                                XmlPullParser.END_TAG -> if (name == "attribute") {
//                                    if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_UPC
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_UPC)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_EAN_JAN
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_EAN_JAN)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_2_OF_5
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_2_OF_5)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_CODEBAR
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_CODEBAR)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_CODE_11
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_CODE_11)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_CODE_128
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_CODE_128)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_CODE_39
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_CODE_39)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_CODE_93
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_CODE_93)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_COMPOSITE
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_COMPOSITE)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_GS1_DATABAR
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_GS1_DATABAR)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_MSI
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_MSI)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_DATAMARIX
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_DATAMARIX)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_PDF
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_PDF)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_POSTAL_CODES
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_POSTAL_CODES)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_QR
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_QR)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_AZTEC
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_AZTEC)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_OCR
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_OCR)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_MAXICODE
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_MAXICODE)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_GS1_DATAMATRIX
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_GS1_DATAMATRIX)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_GS1_QR_CODE
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_GS1_QR_CODE)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_COUPON
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_COUPON)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_DIGIMARC_UPC
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_DIGIMARC_UPC)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_DIGIMARC_EAN_JAN
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_DIGIMARC_EAN_JAN)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_DIGIMARC_OTHER
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_DIGIMARC_OTHER)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_OTHER_1D
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_OTHER_1D)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_OTHER_2D
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_OTHER_2D)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_OTHER
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_OTHER)
//                                        result = true
//                                    } else if (text != null && text.trim { it <= ' ' } == Integer.toString(
//                                            RMDAttributes.RMD_ATTR_VALUE_SSA_HISTOGRAM_UNUSED_ID
//                                        )) {
//                                        ssaSupportedAttribs.add(RMDAttributes.RMD_ATTR_VALUE_SSA_DECODE_COUNT_UNUSED_ID)
//                                        result = true
//                                    }
//                                }
//                            }
//                            event = parser.next()
//                        }
//                    } catch (e: Exception) {
//                        Log.e(TAG, e.toString())
//                    }
//                }
//            }
//            return result
//        }
//
//        override fun onPostExecute(b: Boolean) {
//            super.onPostExecute(b)
//            if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss()
//            val settings: SharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0)
//            var ssaStatus = 0
//            if (ssaSupportedAttribs!!.size == 0) {
//                ssaStatus = 1
//            } else if (scannerType != 1) {
//                ssaStatus = 2
//            }
//            val intent = Intent(context, targetClass)
//            intent.putExtra(Constants.SCANNER_ID, scannerID)
//            intent.putIntegerArrayListExtra(
//                Constants.SYMBOLOGY_SSA,
//                ssaSupportedAttribs as ArrayList<Int>?
//            )
//            intent.putExtra(Constants.SSA_STATUS, ssaStatus)
//            startActivity(intent)
//        }
//    }
//
//    /**
//     * method to send connect command request to reader
//     * after connect button clicked on connect password pairTaskDailog
//     *
//     * @param password     - reader password
//     * @param readerDevice
//     */
//    fun connectClicked(password: String?, readerDevice: ReaderDevice?) {
//        val fragment: Fragment = getSupportFragmentManager().findFragmentByTag(
//            TAG_RFID_FRAGMENT
//        )
//        if (fragment is RFIDReadersListFragment) {
//            (fragment as RFIDReadersListFragment).ConnectwithPassword(password, readerDevice)
//        }
//    }
//
//    /**
//     * method which will exe cute after cancel button clicked on connect pwd pairTaskDailog
//     *
//     * @param readerDevice
//     */
//    fun cancelClicked(readerDevice: ReaderDevice?) {
//        val fragment: Fragment = getSupportFragmentManager().findFragmentByTag(
//            TAG_RFID_FRAGMENT
//        )
//        if (fragment is RFIDReadersListFragment) {
//            (fragment as RFIDReadersListFragment).readerDisconnected(readerDevice, true)
//        }
//    }
//
//    fun findScanner(view: View?) {
//        btnFindScanner = findViewById(R.id.btn_find_scanner) as Button?
//        if (btnFindScanner != null) {
//            btnFindScanner!!.isEnabled = false
//        }
//        com.zebra.demo.ActiveDeviceActivity.FindScannerTask(scannerID).execute()
//    }
//
//    fun loadSampleBarcodes(view: View?) {
//        val intent: Intent = Intent(this, SampleBarcodes::class.java)
//        intent.putExtra(Constants.SCANNER_ID, scannerID)
//        intent.putExtra(Constants.SCANNER_NAME, getIntent().getStringExtra(Constants.SCANNER_NAME))
//        startActivity(intent)
//    }
//
//    private inner class MyAsyncTask(
//        var scannerId: Int,
//        var opcode: DCSSDK_COMMAND_OPCODE,
//        var outXML: StringBuilder
//    ) :
//        AsyncTask<String?, Int?, Boolean>() {
//        private var progressDialog: CustomProgressDialog? = null
//        override fun onPreExecute() {
//            super.onPreExecute()
//            progressDialog = CustomProgressDialog(this@ActiveDeviceActivity, "Execute Command...")
//            progressDialog.show()
//        }
//
//        protected override fun doInBackground(vararg strings: String): Boolean {
//            return executeCommand(opcode, strings[0], outXML, scannerId)
//        }
//
//        override fun onPostExecute(b: Boolean) {
//            super.onPostExecute(b)
//            if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss()
//            if (!b) {
//                Toast.makeText(
//                    this@ActiveDeviceActivity,
//                    "Cannot perform the action",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }
//
//    private inner class FindScannerTask(var scannerId: Int) :
//        AsyncTask<String?, Int?, Boolean>() {
//        override fun onPreExecute() {
//            super.onPreExecute()
//        }
//
//        protected override fun doInBackground(vararg strings: String): Boolean {
//            val t0 = System.currentTimeMillis()
//            TurnOnLEDPattern()
//            BeepScanner()
//            try {
//                Thread.sleep(400)
//            } catch (e: InterruptedException) {
//                if (e != null && e.stackTrace.size > 0) {
//                    Log.e(TAG, e.stackTrace[0].toString())
//                }
//            }
//            while (System.currentTimeMillis() - t0 < 3000) {
//                VibrateScanner()
//                try {
//                    Thread.sleep(400)
//                } catch (e: InterruptedException) {
//                    if (e != null && e.stackTrace.size > 0) {
//                        Log.e(TAG, e.stackTrace[0].toString())
//                    }
//                }
//                VibrateScanner()
//                try {
//                    Thread.sleep(400)
//                } catch (e: InterruptedException) {
//                    if (e != null && e.stackTrace.size > 0) {
//                        Log.e(TAG, e.stackTrace[0].toString())
//                    }
//                }
//                BeepScanner()
//                try {
//                    Thread.sleep(400)
//                } catch (e: InterruptedException) {
//                    if (e != null && e.stackTrace.size > 0) {
//                        Log.e(TAG, e.stackTrace[0].toString())
//                    }
//                }
//                VibrateScanner()
//            }
//            try {
//                Thread.sleep(400)
//            } catch (e: InterruptedException) {
//                if (e != null && e.stackTrace.size > 0) {
//                    Log.e(TAG, e.stackTrace[0].toString())
//                }
//            }
//            TurnOffLEDPattern()
//            return true
//        }
//
//        override fun onPostExecute(b: Boolean) {
//            super.onPostExecute(b)
//            if (btnFindScanner != null) {
//                btnFindScanner!!.isEnabled = true
//            }
//        }
//
//        private fun TurnOnLEDPattern() {
//            val inXML = "<inArgs><scannerID>" + scannerID + "</scannerID><cmdArgs><arg-int>" +
//                    88 + "</arg-int></cmdArgs></inArgs>"
//            val outXML = StringBuilder()
//            executeCommand(DCSSDK_COMMAND_OPCODE.DCSSDK_SET_ACTION, inXML, outXML, scannerID)
//        }
//
//        private fun TurnOffLEDPattern() {
//            val inXML = "<inArgs><scannerID>" + scannerID + "</scannerID><cmdArgs><arg-int>" +
//                    90 + "</arg-int></cmdArgs></inArgs>"
//            val outXML = StringBuilder()
//            executeCommand(DCSSDK_COMMAND_OPCODE.DCSSDK_SET_ACTION, inXML, outXML, scannerID)
//        }
//
//        private fun VibrateScanner() {
//            val inXML = "<inArgs><scannerID>$scannerID</scannerID><cmdArgs>"
//            val outXML = StringBuilder()
//            executeCommand(
//                DCSSDK_COMMAND_OPCODE.DCSSDK_DEVICE_VIBRATION_FEEDBACK,
//                inXML,
//                outXML,
//                scannerID
//            )
//        }
//
//        private fun BeepScanner() {
//            val inXML = "<inArgs><scannerID>" + scannerID + "</scannerID><cmdArgs><arg-int>" +
//                    RMDAttributes.RMD_ATTR_VALUE_ACTION_HIGH_HIGH_LOW_LOW_BEEP + "</arg-int></cmdArgs></inArgs>"
//            val outXML = StringBuilder()
//            executeCommand(DCSSDK_COMMAND_OPCODE.DCSSDK_SET_ACTION, inXML, outXML, scannerID)
//        }
//    }
//
//    /////////////////////   RFID  Functions  ///////////////
//    private fun PrepareMatchModeList() {
//        Log.d(TAG, "PrepareMatchModeList")
//        if (TAG_LIST_MATCH_MODE && !TAG_LIST_LOADED) {
//            //This for loop will reset all the items in the tagsListCSV(making Tag count to zero)
//            for (i in 0 until tagsListCSV.size()) {
//                var inv: InventoryListItem? = null
//                if (tagsListCSV.get(i).getCount() !== 0) {
//                    inv = tagsListCSV.remove(i)
//                    val inventoryListItem =
//                        InventoryListItem(inv.getTagID(), 0, null, null, null, null, null, null)
//                    inventoryListItem.setTagDetails(inv.getTagDetails())
//                    tagsListCSV.add(i, inventoryListItem)
//                } else {
//                    if (tagsListCSV.get(i).isVisible()) {
//                        tagsListCSV.get(i).setVisible(false)
//                    }
//                }
//            }
//            UNIQUE_TAGS_CSV = tagsListCSV.size()
//            tagsReadInventory.addAll(tagsListCSV)
//            inventoryList.putAll(tagListMap)
//            missedTags = tagsListCSV.size()
//            matchingTags = 0
//            TAG_LIST_LOADED = true
//            Log.d(TAG, "PrepareMatchModeList done")
//        }
//    }
//
//    @Synchronized
//    fun inventoryStartOrStop(view: View?) {
//        mRFIDBaseActivity.inventoryStartOrStop()
//    }
//
//    @Synchronized
//    fun showAntennaSelection(view: View?) {
//        if (mIsInventoryRunning) {
//            Toast.makeText(
//                getApplicationContext(),
//                "Operation in progress-command not allowed",
//                Toast.LENGTH_SHORT
//            ).show()
//            return
//        }
//        mRFIDBaseActivity.showAntennaSelection()
//    }
//
//    fun loadNextFragment(fragmentType: Int) {
//        val settingsTab: Int = mAdapter.getSettingsTab()
//        var PageTitle = " "
//        try {
//            when (fragmentType) {
//                RAPID_READ_TAB -> {
//                    PageTitle = "Rapid"
//                    mAdapter.setRFIDMOde(RAPID_READ_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(RFID_TAB)).commit()
//                }
//
//                LOCATE_TAG_TAB -> {
//                    PageTitle = "Locate"
//                    mAdapter.setRFIDMOde(LOCATE_TAG_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(RFID_TAB)).commit()
//                }
//
//                INVENTORY_TAB -> {
//                    PageTitle = "Inventory"
//                    mAdapter.setRFIDMOde(INVENTORY_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(RFID_TAB)).commit()
//                }
//
//                RFID_PREFILTERS_TAB -> {
//                    PageTitle = "Prefilter"
//                    mAdapter.setRFIDMOde(RFID_PREFILTERS_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(RFID_TAB)).commit()
//                }
//
//                RFID_ACCESS_TAB -> {
//                    PageTitle = "Tag Write"
//                    mAdapter.setRFIDMOde(RFID_ACCESS_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(RFID_TAB)).commit()
//                }
//
//                RFID_SETTINGS_TAB -> {
//                    PageTitle = "RFID Settings"
//                    mAdapter.setSettingsMode(RFID_SETTINGS_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                SCAN_SETTINGS_TAB -> {
//                    PageTitle = "Scan"
//                    mAdapter.setSettingsMode(SCAN_SETTINGS_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                SCAN_DATAVIEW_TAB -> {
//                    PageTitle = "Scan Data"
//                    mAdapter.setSCANMOde(SCAN_DATAVIEW_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(SCAN_TAB)).commit()
//                }
//
//                SCAN_ADVANCED_TAB -> {
//                    PageTitle = "Advanced Scan"
//                    mAdapter.setReaderListMOde(SCAN_ADVANCED_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                SCAN_HOME_SETTINGS_TAB -> {
//                    PageTitle = "Settings"
//                    mAdapter.setReaderListMOde(SCAN_HOME_SETTINGS_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                READER_LIST_TAB -> {
//                    PageTitle = "Readers"
//                    mAdapter.setReaderListMOde(READER_LIST_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(READERS_TAB)).commit()
//                }
//
//                DEVICE_PAIR_TAB -> {
//                    PageTitle = "Scan" //"Pair";
//                    mAdapter.setReaderListMOde(DEVICE_PAIR_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(READERS_TAB)).commit()
//                }
//
//                READER_DETAILS_TAB -> {
//                    PageTitle = "Details"
//                    mAdapter.setReaderListMOde(READER_DETAILS_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(READERS_TAB)).commit()
//                }
//
//                READER_WIFI_SETTINGS_TAB -> {
//                    PageTitle = "WiFi Settings"
//                    mAdapter.setSettingsMode(READER_WIFI_SETTINGS_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                MAIN_RFID_SETTINGS_TAB -> {
//                    PageTitle = "RFID Settings"
//                    mAdapter.setSettingsMode(MAIN_RFID_SETTINGS_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                MAIN_HOME_SETTINGS_TAB -> {
//                    PageTitle = "Settings"
//                    mAdapter.setSettingsMode(MAIN_HOME_SETTINGS_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                MAIN_GENERAL_SETTINGS_TAB -> {
//                    PageTitle = "General Settings"
//                    mAdapter.setSettingsMode(MAIN_GENERAL_SETTINGS_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                APPLICATION_SETTINGS_TAB -> {
//                    PageTitle = "Application"
//                    mAdapter.setSettingsMode(APPLICATION_SETTINGS_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                RFID_PROFILES_TAB -> {
//                    PageTitle = "Profiles"
//                    mAdapter.setSettingsMode(RFID_PROFILES_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                RFID_ADVANCED_OPTIONS_TAB -> {
//                    PageTitle = "RFID Advanced Settings"
//                    mAdapter.setSettingsMode(RFID_ADVANCED_OPTIONS_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                RFID_REGULATORY_TAB -> {
//                    PageTitle = "Regulatory"
//                    mAdapter.setSettingsMode(RFID_REGULATORY_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                RFID_BEEPER_TAB -> {
//                    PageTitle = "Beeper"
//                    mAdapter.setSettingsMode(RFID_BEEPER_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                RFID_LED_TAB -> {
//                    PageTitle = "LED"
//                    mAdapter.setSettingsMode(RFID_LED_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                WIFI_STATUS_TAB -> {
//                    PageTitle = "WiFi Status"
//                    mAdapter.setSettingsMode(WIFI_STATUS_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                CHARGE_TERMINAL_TAB -> {
//                    PageTitle = "Charge Terminal"
//                    mAdapter.setSettingsMode(CHARGE_TERMINAL_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                ANTENNA_SETTINGS_TAB -> {
//                    PageTitle = "Antenna"
//                    mAdapter.setSettingsMode(ANTENNA_SETTINGS_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                SINGULATION_CONTROL_TAB -> {
//                    PageTitle = "Singulation"
//                    mAdapter.setSettingsMode(SINGULATION_CONTROL_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                START_STOP_TRIGGER_TAB -> {
//                    PageTitle = "Start/Stop"
//                    mAdapter.setSettingsMode(START_STOP_TRIGGER_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                TAG_REPORTING_TAB -> {
//                    PageTitle = "Tag Reporting"
//                    mAdapter.setSettingsMode(TAG_REPORTING_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                SAVE_CONFIG_TAB -> {
//                    PageTitle = "Save"
//                    mAdapter.setSettingsMode(SAVE_CONFIG_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                DPO_SETTING_TAB -> {
//                    PageTitle = "Power"
//                    mAdapter.setSettingsMode(DPO_SETTING_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                FACTORY_RESET_FRAGMENT_TAB -> {
//                    PageTitle = "Factory Reset"
//                    mAdapter.setSettingsMode(FACTORY_RESET_FRAGMENT_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                LOGGER_FRAGMENT_TAB -> {
//                    PageTitle = "Logging"
//                    mAdapter.setSettingsMode(LOGGER_FRAGMENT_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                DEVICE_RESET_TAB -> {
//                    PageTitle = "Device Reset"
//                    mAdapter.setSettingsMode(DEVICE_RESET_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                KEYREMAP_TAB -> {
//                    PageTitle = "Trigger Map"
//                    mAdapter.setSettingsMode(KEYREMAP_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                UPDATE_FIRMWARE_TAB -> {
//                    PageTitle = "FW Update"
//                    mAdapter.setSettingsMode(UPDATE_FIRMWARE_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                ASSERT_DEVICE_INFO_TAB -> {
//                    PageTitle = "Device Info"
//                    mAdapter.setSettingsMode(ASSERT_DEVICE_INFO_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                STATIC_IP_CONFIG -> {
//                    PageTitle = "Network Ip Configuration"
//                    mAdapter.setSettingsMode(STATIC_IP_CONFIG)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                BARCODE_SYMBOLOGIES_TAB -> {
//                    PageTitle = "Symbologies"
//                    mAdapter.setSettingsMode(BARCODE_SYMBOLOGIES_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                BEEPER_ACTION_TAB -> {
//                    PageTitle = "Beeper"
//                    mAdapter.setSettingsMode(BEEPER_ACTION_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                BATTERY_STATISTICS_TAB -> {
//                    PageTitle = "Battery Statistics"
//                    mAdapter.setSettingsMode(BATTERY_STATISTICS_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                USB_MIFI_TAB -> {
//                    PageTitle = "USB MiFi"
//                    mAdapter.setSettingsMode(USB_MIFI_TAB)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//
//                WIFI_MAIN_PAGE -> {
//                    PageTitle = "WiFi"
//                    mAdapter.setSettingsMode(WIFI_MAIN_PAGE)
//                    getSupportFragmentManager().beginTransaction()
//                        .remove(getCurrentFragment(settingsTab)).commit()
//                }
//            }
//            setActionBarTitle(PageTitle)
//        } catch (ne: NullPointerException) {
//            return
//        } catch (ise: IllegalStateException) {
//            return
//        }
//
//        //getSupportFragmentManager().beginTransaction().remove(getCurrentFragment(RFID_TAB)).commit();
//        getSupportFragmentManager().beginTransaction().addToBackStack(null)
//        getSupportFragmentManager().executePendingTransactions()
//        mAdapter.notifyDataSetChanged()
//    }
//
//    fun getCurrentFragment(position: Int): Fragment? {
//
//        //return mAdapter.getCurrentFragment();
//        return mAdapter.getRegisteredFragment(position)
//    }
//
//    fun exportData() {
//        if (mConnectedReader != null) {
//            val uri =
//                Uri.parse("content://com.android.externalstorage.documents/document/primary:Download")
//            createFile1(uri)
//            // new DataExportTask(getApplicationContext(), tagsReadInventory, mConnectedReader.getHostName(), TOTAL_TAGS, UNIQUE_TAGS, mRRStartedTime).execute();
//        }
//    }
//
//    @Throws(InvalidUsageException::class, OperationFailureException::class)
//    fun resetFactoryDefault() {
//        val btCon = false
//        try {
//            if (mConnectedReader != null && mConnectedReader.getTransport() != null &&
//                mConnectedReader.getTransport().equals("BLUETOOTH")
//            ) {
//                mRFIDBaseActivity.resetFactoryDefault()
//                Thread.sleep(2000)
//                mRFIDBaseActivity.onFactoryReset(RFIDController.mConnectedDevice)
//            } else if (RFIDController.mConnectedDevice != null) {
//                mRFIDBaseActivity.resetFactoryDefault()
//            }
//        } catch (e: OperationFailureException) {
//            throw e
//        } catch (e: InterruptedException) {
//        }
//    }
//
//    fun onRadioButtonClicked(view: View?) {
//        val fragment = getCurrentFragment(SETTINGS_TAB)
//        if (fragment is FactoryResetFragment) {
//            (fragment as FactoryResetFragment?).changeResetMode(view)
//        }
//    }
//
//    @Throws(InvalidUsageException::class, OperationFailureException::class)
//    fun deviceReset(commandString: String?): Boolean {
//        return mRFIDBaseActivity.deviceReset()
//    }
//
//    fun checkForExportPermission(code: Int) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED) {
//                when (code) {
//                    REQUEST_CODE_ASK_PERMISSIONS -> exportData()
//                    REQUEST_CODE_ASK_PERMISSIONS_CSV -> MatchModeFileLoader.getInstance(this)
//                        .LoadMatchModeCSV()
//                }
//            } else {
//                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    //Toast.makeText(this,"Write to external storage permission needed to export inventory.",Toast.LENGTH_LONG).show();
//                    showMessageOKCancel(
//                        "Write to external storage permission needed to export the inventory."
//                    ) { dialog, which ->
//                        requestPermissions(
//                            arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                            code
//                        )
//                    }
//                    return
//                }
//                requestPermissions(
//                    arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                    code
//                )
//            }
//        }
//    }
//
//    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
//        AlertDialog.Builder(this@ActiveDeviceActivity)
//            .setMessage(message)
//            .setPositiveButton("OK", okListener)
//            .setNegativeButton("Cancel", null)
//            .create()
//            .show()
//    }
//
//    fun disableScanner() {
//        mRFIDBaseActivity.disableScanner()
//    }
//
//    fun enableScanner() {
//        mRFIDBaseActivity.enableScanner()
//    }
//
//    fun LoadTagListCSV() {
//        Log.d(TAG, "LoadTagListCSV")
//        mRFIDBaseActivity.LoadTagListCSV()
//    }
//
//    fun startbeepingTimer() {
//        mRFIDBaseActivity.startbeepingTimer()
//    }
//
//    inner class ExpandableListAdapter(
//        private val _context: Context,
//        // header titles
//        private val _listDataHeader: List<String>,
//        // child data in format of header title, child title
//        private val _listDataChild: HashMap<String, List<String>>
//    ) : BaseExpandableListAdapter() {
//        init {
//            _listDataChild = _listDataChild
//        }
//
//        override fun getChild(groupPosition: Int, childPosititon: Int): Any {
//            return _listDataChild[_listDataHeader[groupPosition]]
//                .get(childPosititon)
//        }
//
//        override fun getChildId(groupPosition: Int, childPosition: Int): Long {
//            return childPosition.toLong()
//        }
//
//        override fun getChildView(
//            groupPosition: Int, childPosition: Int,
//            isLastChild: Boolean, convertView: View, parent: ViewGroup
//        ): View {
//            var convertView = convertView
//            val childText = getChild(
//                groupPosition,
//                childPosition
//            ) as String
//            if (convertView == null) {
//                val infalInflater = _context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//                convertView = infalInflater.inflate(R.layout.drawer_list_sub_item, null)
//            }
//            val txtListChild = convertView
//                .findViewById<View>(R.id.drawerItemName) as TextView
//            txtListChild.text = childText
//
//            // adding icon to expandable list view
//            val imgListGroup = convertView
//                .findViewById<View>(R.id.drawerIcon) as ImageView
//            if (groupPosition == 1) {
//                imgListGroup.setImageResource(managexx_icon[childPosition])
//            }
//            //imgListGroup.setImageResource(icon[groupPosition+childPosition]);
//            return convertView
//        }
//
//        override fun getChildrenCount(groupPosition: Int): Int {
//            return _listDataChild[_listDataHeader[groupPosition]]!!.size
//        }
//
//        override fun getGroup(groupPosition: Int): Any {
//            return _listDataHeader[groupPosition]
//        }
//
//        override fun getGroupCount(): Int {
//            return _listDataHeader.size
//        }
//
//        override fun getGroupId(groupPosition: Int): Long {
//            return groupPosition.toLong()
//        }
//
//        override fun getGroupView(
//            groupPosition: Int, isExpanded: Boolean,
//            convertView: View, parent: ViewGroup
//        ): View {
//            var convertView = convertView
//            val headerTitle = getGroup(groupPosition) as String
//            if (convertView == null) {
//                val infalInflater = _context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//                convertView = infalInflater.inflate(R.layout.drawer_list_item, null)
//            }
//            val lblListHeader = convertView
//                .findViewById<View>(R.id.drawerItemName) as TextView
//            lblListHeader.text = headerTitle
//
//            // adding icon to expandable list view
//            val imgListGroup = convertView
//                .findViewById<View>(R.id.drawerIcon) as ImageView
//            imgListGroup.setImageResource(icon[groupPosition])
//            return convertView
//        }
//
//        override fun hasStableIds(): Boolean {
//            return false
//        }
//
//        override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
//            return true
//        }
//    }
//
//    fun performtagmatchClick() {
//        if (inventoryBT != null) {
//            if (mIsInventoryRunning) {
//                inventoryBT!!.performClick()
//                startbeepingTimer()
//            }
//        }
//    }
//
//    val resources: Resources
//        get() {
//            val res: Resources = super.getResources()
//            val config = Configuration()
//            config.setToDefaults()
//            res.updateConfiguration(config, res.displayMetrics)
//            return res
//        }
//
//    fun onConfigurationChanged(newConfig: Configuration) {
//        if (newConfig.fontScale != 1f) //Non-default
//            resources
//        super.onConfigurationChanged(newConfig)
//    }
//
//    var _onNfcCreateCallback = CreateNdefMessageCallback {
//        Log.i(TAG, "createNdefMessage")
//        createMessage()
//    }
//
//    private fun createMessage(): NdefMessage {
//        val text = """
//             Hello there from another device!
//
//             Beam Time: ${System.currentTimeMillis()}
//             """.trimIndent()
//        return NdefMessage(
//            arrayOf(
//                NdefRecord.createMime(
//                    "application/com.bluefletch.nfcdemo.mimetype", text.toByteArray()
//                )
//                /**
//                 * The Android Application Record (AAR) is commented out. When a device
//                 * receives a push with an AAR in it, the application specified in the AAR
//                 * is guaranteed to run. The AAR overrides the tag dispatch system.
//                 * You can add it back in to guarantee that this
//                 * activity starts when receiving a beamed message. For now, this code
//                 * uses the tag dispatch system.
//                 */
//                /**
//                 * The Android Application Record (AAR) is commented out. When a device
//                 * receives a push with an AAR in it, the application specified in the AAR
//                 * is guaranteed to run. The AAR overrides the tag dispatch system.
//                 * You can add it back in to guarantee that this
//                 * activity starts when receiving a beamed message. For now, this code
//                 * uses the tag dispatch system.
//                 */
//                //,NdefRecord.createApplicationRecord("com.example.android.beam")
//            )
//        )
//    }
//
//    private fun ShowPlugInPathChangeDialog() {
//        if (!isFinishing()) {
//            val dialog = Dialog(this@ActiveDeviceActivity)
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            dialog.setContentView(R.layout.dialog_plugin_path_change)
//            dialog.setCancelable(false)
//            dialog.setCanceledOnTouchOutside(false)
//            dialog.show()
//            val declineButton = dialog.findViewById<View>(R.id.btn_ok) as TextView
//            declineButton.setOnClickListener { v: View? -> dialog.dismiss() }
//        }
//    }
//
//    fun createDWProfile() {
//        // MAIN BUNDLE PROPERTIES
//        if (RFIDController.mConnectedReader != null && RFIDController.mConnectedReader.getHostName() != null && RFIDController.mConnectedReader.getHostName()
//                .startsWith("MC33")
//        ) {
//            val bMain = Bundle()
//            bMain.putString("PROFILE_NAME", "RFIDMobileApp")
//            bMain.putString("PROFILE_ENABLED", "true") // <- that will be enabled
//            bMain.putString("CONFIG_MODE", "CREATE_IF_NOT_EXIST") // <- or created if necessary.
//            // PLUGIN_CONFIG BUNDLE PROPERTIES
//            val scanBundle = Bundle()
//            scanBundle.putString("PLUGIN_NAME", "BARCODE") // barcode plugin
//            scanBundle.putString("RESET_CONFIG", "true")
//            // PARAM_LIST BUNDLE PROPERTIES
//            val scanParams = Bundle()
//            scanParams.putString("scanner_selection", "auto")
//            scanParams.putString("scanner_input_enabled", "false") // Mainly disable scanner plugin
//            // NEST THE BUNDLE "bParams" WITHIN THE BUNDLE "bConfig"
//            scanBundle.putBundle("PARAM_LIST", scanParams)
//            val keystrokeBundle = Bundle()
//            keystrokeBundle.putString("PLUGIN_NAME", "KEYSTROKE")
//            val keyStrokeParams = Bundle()
//            keyStrokeParams.putString("keystroke_output_enabled", "false")
//            keyStrokeParams.putString("keystroke_action_char", "9") // 0, 9 , 10, 13
//            keyStrokeParams.putString("keystroke_delay_extended_ascii", "500")
//            keyStrokeParams.putString("keystroke_delay_control_chars", "800")
//            keystrokeBundle.putBundle("PARAM_LIST", keyStrokeParams)
//            val rfidConfigParamList = Bundle()
//            rfidConfigParamList.putString("rfid_input_enabled", "false")
//            val rfidConfigBundle = Bundle()
//            rfidConfigBundle.putString("PLUGIN_NAME", "RFID")
//            rfidConfigBundle.putString("RESET_CONFIG", "true")
//            rfidConfigBundle.putBundle("PARAM_LIST", rfidConfigParamList)
//            val bConfigIntent = Bundle()
//            val bParamsIntent = Bundle()
//            bParamsIntent.putString("intent_output_enabled", "true")
//            bParamsIntent.putString("intent_action", "com.symbol.dwudiusertokens.udi")
//            bParamsIntent.putString("intent_category", "zebra.intent.dwudiusertokens.UDI")
//            bParamsIntent.putInt(
//                "intent_delivery",
//                2
//            ) //Use "0" for Start Activity, "1" for Start Service, "2" for Broadcast, "3" for start foreground service
//            bConfigIntent.putString("PLUGIN_NAME", "INTENT")
//            bConfigIntent.putString("RESET_CONFIG", "true")
//            bConfigIntent.putBundle("PARAM_LIST", bParamsIntent)
//
//
//            // THEN NEST THE "bConfig" BUNDLE WITHIN THE MAIN BUNDLE "bMain"
//            val bundleArrayList = ArrayList<Bundle>()
//            bundleArrayList.add(scanBundle)
//            bundleArrayList.add(rfidConfigBundle)
//            bundleArrayList.add(keystrokeBundle)
//            bundleArrayList.add(bConfigIntent)
//
//            // following requires arrayList
//            bMain.putParcelableArrayList("PLUGIN_CONFIG", bundleArrayList)
//            // CREATE APP_LIST BUNDLES (apps and/or activities to be associated with the Profile)
//            val ActivityList = Bundle()
//            ActivityList.putString(
//                "PACKAGE_NAME",
//                getPackageName()
//            ) // Associate the profile with this app
//            ActivityList.putStringArray("ACTIVITY_LIST", arrayOf("*"))
//
//            // NEXT APP_LIST BUNDLE(S) INTO THE MAIN BUNDLE
//            bMain.putParcelableArray(
//                "APP_LIST", arrayOf(
//                    ActivityList
//                )
//            )
//            val i = Intent()
//            i.setAction("com.symbol.datawedge.api.ACTION")
//            i.putExtra("com.symbol.datawedge.api.SET_CONFIG", bMain)
//            i.putExtra("SEND_RESULT", "true")
//            i.putExtra("COMMAND_IDENTIFIER", Application.RFID_DATAWEDGE_PROFILE_CREATION)
//            sendBroadcast(i)
//        }
//    }
//
//    companion object {
//        private const val TAG_RFID_FRAGMENT = "RFID_FRAGMENT"
//        const val MIME_TEXT_PLAIN = "text/plain"
//        const val isActivityVisible = false
//        var picklistMode = 0
//        private const val REQUEST_CODE_ASK_PERMISSIONS = 10
//        private const val REQUEST_CODE_ASK_PERMISSIONS_CSV = 11
//        private var mRFIDBaseActivity: RFIDBaseActivity? = null
//        const val BRAND_ID = "brandid"
//        const val EPC_LEN = "epclen"
//        const val IS_BRANDID_CHECK = "brandidcheck"
//        var mConnectedReaderDetails: ReaderDevice? = null
//        var isPagerMotorAvailable = false
//            get() = Companion.field
//        var cmdExecTask: com.zebra.demo.ActiveDeviceActivity.MyAsyncTask? = null
//        const val ENABLE_FIND_NEW_SCANNER = 1
//        var icon = intArrayOf(
//            R.drawable.nav_available_scanners, R.drawable.nav_pair_new_bt_scanner,
//            R.drawable.ic_firmware_update, R.drawable.nav_about
//        )
//        var managexx_icon = intArrayOf(
//            R.drawable.ic_reset_factory, R.drawable.ic_btn_reset, R.drawable.ic_logging,
//            R.drawable.ic_export_config, R.drawable.ic_report
//        )
//    }
//}
