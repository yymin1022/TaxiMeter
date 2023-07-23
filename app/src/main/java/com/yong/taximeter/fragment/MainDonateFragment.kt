package com.yong.taximeter.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.yong.taximeter.R

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

    private val purchaseUpdateListener = PurchasesUpdatedListener { billingResult, purchases ->
        if(billingResult.responseCode == BillingResponseCode.OK && purchases != null) {
            Toast.makeText(requireContext(), "Purchased", Toast.LENGTH_SHORT).show()
        } else if(billingResult.responseCode == BillingResponseCode.USER_CANCELED) {
            Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show()
        } else if(billingResult.responseCode == BillingResponseCode.ITEM_ALREADY_OWNED) {
            Toast.makeText(requireContext(), "Already Owned", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Error : ${billingResult.debugMessage}", Toast.LENGTH_SHORT).show()
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
        }
    }
}