package com.ingwersen.kyle.cs125_project;

import android.os.Parcel;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kyle on 3/1/2018.
 */

public class StoreItem implements android.os.Parcelable
{
    private String mName;
    private int mQuantity;
    private double mTimeMean;
    private double mTimeVar;
    private Date mTimeLast;

    private boolean mInCart;
    private boolean mVisible;

    public StoreItem(String name)
    {
        init(name, 0, 0, 0, new Date(0), false, true);
    }

    public StoreItem(String name, int quantity, double timeMean, double timeVar, Date timeLast)
    {
        init(name, quantity, timeMean, timeVar, timeLast, false, true);
    }

    public StoreItem(String name, int quantity, double timeMean, double timeVar, Date timeLast, boolean inCart, boolean visible)
    {
        init(name, quantity, timeMean, timeVar, timeLast, inCart, visible);
    }

    private void init(String name, int quantity, double timeMean, double timeVar, Date timeLast, boolean inCart, boolean visible)
    {
        mName = name;
        mQuantity = quantity;
        mTimeMean = timeMean;
        mTimeVar = timeVar;
        mTimeLast = timeLast;

        mInCart = inCart;
        mVisible = visible;
    }

    public void setInCart(boolean value)
    {
        mInCart = value;
    }
    public void setVisible(boolean value)
    {
        mVisible = value;
    }
    public boolean getInCart()
    {
        return mInCart;
    }
    public boolean getVisible()
    {
        return mVisible;
    }

    public static ArrayList<StoreItem> buildEmptyList()
    {
        ArrayList<StoreItem> result = new ArrayList<>();
        for (StoreType value: StoreType.values()) {
            result.add(new StoreItem(value.getTypeName(), 0, 0, 0,  new Date(0)));
        }
        return result;
    }

    public static ArrayList<StoreItem> loadList(File path)
    {
        // TODO: Implement
        return buildEmptyList();
    }

    public static void saveList(File path, ArrayList<StoreItem> items) {
        // TODO: Implement
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(mName);
        parcel.writeInt(mQuantity);
        parcel.writeDouble(mTimeMean);
        parcel.writeDouble(mTimeVar);
        parcel.writeSerializable(mTimeLast);
        parcel.writeLong(mTimeLast.getTime());

        // TODO Write Booleans
    }

    public void readFromParcel(Parcel parcel)
    {
        // TODO
    }

    public enum StoreType
    {
        APPLE(0, "Apple"),
        BANANA(1, "Banana"),
        PEAR(2, "Pear"),
        PEACH(3, "Peach"),
        MANGO(4, "Mango");

        private int mTypeId;
        private String mTypeName;

        StoreType(int typeId, String typeName)
        {
            mTypeId = typeId;
            mTypeName = typeName;
        }

        public int getTypeId() { return mTypeId; }

        public String getTypeName() { return mTypeName; }

    }
}
