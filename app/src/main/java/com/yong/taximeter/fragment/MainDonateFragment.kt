package com.yong.taximeter.fragment

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.preference.PreferenceManager
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.consumePurchase
import com.yong.taximeter.R
import com.yong.taximeter.util.Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainDonateFragment : Fragment() {
    private lateinit var btnAdremove: TextView
    private lateinit var btnBigmac: TextView
    private lateinit var btnCoffee: TextView
    private lateinit var btnCoke: TextView
    private lateinit var btnDinner: TextView
    private lateinit var btnMoney: TextView

    private lateinit var billingClient: BillingClient
    private lateinit var queryProductDetailsParams: QueryProductDetailsParams

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_donate, container, false)
        btnAdremove = view.findViewById(R.id.tv_donate_adremove)
        btnBigmac = view.findViewById(R.id.tv_donate_bigmac)
        btnCoffee = view.findViewById(R.id.tv_donate_coffee)
        btnCoke = view.findViewById(R.id.tv_donate_coke)
        btnDinner = view.findViewById(R.id.tv_donate_dinner)
        btnMoney = view.findViewById(R.id.tv_donate_money)
        btnAdremove.setOnClickListener(btnListener)
        btnBigmac.setOnClickListener(btnListener)
        btnCoffee.setOnClickListener(btnListener)
        btnCoke.setOnClickListener(btnListener)
        btnDinner.setOnClickListener(btnListener)
        btnMoney.setOnClickListener(btnListener)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && Util.isUsingNightModeResources(requireContext())) {
            btnAdremove.setCompoundDrawablesWithIntrinsicBounds(null,
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_donate_adremove_dark),
                null, null)
            btnBigmac.setCompoundDrawablesWithIntrinsicBounds(null,
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_donate_bigmac_dark),
                null, null)
            btnCoffee.setCompoundDrawablesWithIntrinsicBounds(null,
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_donate_coffee_dark),
                null, null)
            btnCoke.setCompoundDrawablesWithIntrinsicBounds(null,
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_donate_coke_dark),
                null, null)
            btnDinner.setCompoundDrawablesWithIntrinsicBounds(null,
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_donate_dinner_dark),
                null, null)
            btnMoney.setCompoundDrawablesWithIntrinsicBounds(null,
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_donate_money_dark),
                null, null)
        }

        initBillingClient()

        return view
    }

    private fun initBillingClient() {
        billingClient = BillingClient.newBuilder(requireContext())
            .setListener(purchaseUpdateListener)
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if(billingResult.responseCode !=  BillingResponseCode.OK) {
                    Log.e("BILLING_CLIENT", billingResult.debugMessage)
                }
            }
            override fun onBillingServiceDisconnected() {
                Log.e("BILLING_CLIENT", "Failed to Connect to Google Play")
            }
        })
    }

    private fun queryProduct(productID: String) {
        queryProductDetailsParams = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(productID)
                        .setProductType(ProductType.INAPP)
                        .build()
                )
            )
            .build()

        billingClient.queryProductDetailsAsync(queryProductDetailsParams) { billingResult, productDetailsList ->
            when (billingResult.responseCode) {
                BillingResponseCode.OK -> purchaseProduct(productDetailsList[0])
                else -> Log.e("QUERY_PRODUCT", billingResult.debugMessage)
            }
        }
    }

    private fun purchaseProduct(productDetails: ProductDetails) {
        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        billingClient.launchBillingFlow(requireActivity(), billingFlowParams)
    }
    private suspend fun consumePurchase(purchase: Purchase) {
        if(purchase.products[0].equals("ad_remove")) {
            val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
            val prefEd = pref.edit()
            prefEd.putBoolean("ad_remove", true)
            prefEd.apply()
        }

        val consumeParams =
            ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
        withContext(Dispatchers.IO) {
            billingClient.consumePurchase(consumeParams)
        }
    }

    private val purchaseUpdateListener = PurchasesUpdatedListener { billingResult, purchases ->
        if(billingResult.responseCode == BillingResponseCode.OK && purchases != null) {
            purchases.forEach { purchase ->
                Toast.makeText(requireContext(), getString(R.string.noti_toast_purchase_success), Toast.LENGTH_SHORT).show()
                CoroutineScope(Dispatchers.IO).launch {
                    consumePurchase(purchase)
                }
            }
        } else if(billingResult.responseCode == BillingResponseCode.ITEM_ALREADY_OWNED && purchases != null) {
            Toast.makeText(requireContext(), getString(R.string.noti_toast_purchase_already), Toast.LENGTH_SHORT).show()
            purchases.forEach { purchase ->
                Toast.makeText(requireContext(), getString(R.string.noti_toast_purchase_success), Toast.LENGTH_SHORT).show()
                CoroutineScope(Dispatchers.IO).launch {
                    consumePurchase(purchase)
                }
            }
        } else if(billingResult.responseCode == BillingResponseCode.USER_CANCELED) {
            Toast.makeText(requireContext(), getString(R.string.noti_toast_purchase_canceled), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), String.format(getString(R.string.noti_toast_purchase_error, billingResult.responseCode, billingResult.debugMessage)), Toast.LENGTH_SHORT).show()
            Log.e("PURCHASE_UPDATE", billingResult.debugMessage)
        }
    }

    private val btnListener = View.OnClickListener { view ->
        when(view.id) {
            R.id.tv_donate_adremove -> {
                queryProduct("ad_remove")
            }

            R.id.tv_donate_bigmac -> {
                queryProduct("donation_10000")
            }

            R.id.tv_donate_coffee -> {
                queryProduct("donation_5000")
            }

            R.id.tv_donate_coke -> {
                queryProduct("donation_1000")
            }

            R.id.tv_donate_dinner -> {
                queryProduct("donation_50000")
            }

            R.id.tv_donate_money -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://paypal.me/usefulmin")))
            }
        }
    }
}