package com.nagaja.app.android.Data

import java.io.Serializable

class DeviceData: Serializable {
    private var mBoard: String? = ""
    private var mBrand: String? = ""
    private var mCpuAbi: String? = ""
    private var mDevice: String? = ""
    private var mDisplay: String? = ""
    private var mFingerPrint: String? = ""
    private var mHost: String? = ""
    private var mId: String? = ""
    private var mManufacturer: String? = ""
    private var mModel: String? = ""
    private var mProduct: String? = ""
    private var mTime: Long = 0
    private var mType: String? = ""

    fun setBoard(board: String) {
        mBoard = board
    }

    fun getBoard(): String? {
        return mBoard
    }

    fun setBrand(brand: String) {
        mBrand = brand
    }

    fun getBrand(): String? {
        return mBrand
    }

    fun setCpuAbi(cpuAbi: String) {
        mCpuAbi = cpuAbi
    }

    fun getCpuAbi(): String? {
        return mCpuAbi
    }

    fun setDevice(device: String) {
        mDevice = device
    }

    fun getDevice(): String? {
        return mDevice
    }

    fun setDisplay(display: String) {
        mDisplay = display
    }

    fun getDisplay(): String? {
        return mDisplay
    }

    fun setFingerPrint(fingerPrint: String) {
        mFingerPrint = fingerPrint
    }

    fun getFingerPrint(): String? {
        return mFingerPrint
    }

    fun setHost(host: String) {
        mHost = host
    }

    fun getHost(): String? {
        return mHost
    }

    fun setId(id: String) {
        mId = id
    }

    fun getId(): String? {
        return mId
    }

    fun setManufacturer(manufacturer: String) {
        mManufacturer = manufacturer
    }

    fun getManufacturer(): String? {
        return mManufacturer
    }

    fun setModel(model: String) {
        mModel = model
    }

    fun getModel(): String? {
        return mModel
    }

    fun setProduct(product: String) {
        mProduct = product
    }

    fun getProduct(): String? {
        return mProduct
    }

    fun setTime(time: Long) {
        mTime = time
    }

    fun getTime(): Long {
        return mTime
    }

    fun setType(type: String) {
        mType = type
    }

    fun getType(): String? {
        return mType
    }
}